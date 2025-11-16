pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.0'
        jdk 'JDK_18'
    }

    environment {
        APP_NAME = 'mi-playlist'
        DOCKER_IMAGE = 'mi-playlist-app'
        CONTAINER_NAME = 'mi-playlist-app'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '========== Obteniendo código del repositorio =========='
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '========== Compilando el proyecto =========='
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo '========== Ejecutando tests unitarios =========='
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo '========== Empaquetando la aplicación =========='
                sh 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '========== Construyendo imagen Docker =========='
                script {
                    // Construir imagen Docker
                    sh """
                        docker build -t ${DOCKER_IMAGE}:latest \
                            -t ${DOCKER_IMAGE}:${BUILD_NUMBER} \
                            -f Dockerfile .
                    """
                }
            }
        }

        stage('Stop Old Container') {
            steps {
                echo '========== Deteniendo contenedor anterior =========='
                script {
                    sh """
                        docker stop ${CONTAINER_NAME} || true
                        docker rm ${CONTAINER_NAME} || true
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                echo '========== Desplegando nueva versión =========='
                script {
                    sh """
                        docker run -d \
                            --name ${CONTAINER_NAME} \
                            --network mi-playlist-network \
                            -p 8080:8080 \
                            --restart unless-stopped \
                            ${DOCKER_IMAGE}:latest
                    """
                }
            }
        }

        stage('Health Check') {
            steps {
                echo '========== Verificando que la aplicación esté funcionando =========='
                script {
                    sh """
                        echo "Esperando 20 segundos a que la app inicie..."
                        sleep 20

                        echo "Verificando health check..."
                        for i in {1..10}; do
                            if docker exec ${CONTAINER_NAME} wget --quiet --tries=1 --spider http://localhost:8080; then
                                echo "✓ Aplicación respondiendo correctamente"
                                exit 0
                            fi
                            echo "Intento \$i de 10..."
                            sleep 5
                        done

                        echo "✗ La aplicación no responde"
                        exit 1
                    """
                }
            }
        }
    }

    post {
        success {
            echo '========== Pipeline ejecutado exitosamente =========='
            echo '✓ Build completado'
            echo '✓ Tests pasados'
            echo '✓ Imagen Docker creada'
            echo '✓ Aplicación desplegada'
            echo ''
            echo '🌐 Aplicación disponible en: http://localhost:8080'
            echo '🐳 Contenedor: ' + env.CONTAINER_NAME
            echo '📦 Imagen: ' + env.DOCKER_IMAGE + ':' + env.BUILD_NUMBER
        }
        failure {
            echo '========== Pipeline falló =========='
            echo '✗ Revisa los logs para identificar el error'

            // Mostrar logs del contenedor si existe
            script {
                sh "docker logs ${CONTAINER_NAME} || true"
            }
        }
        always {
            echo '========== Limpiando workspace =========='
            // Limpiar imágenes antiguas (mantener últimas 3)
            script {
                sh """
                    docker images ${DOCKER_IMAGE} --format "{{.Tag}}" | \
                        grep -v latest | \
                        sort -rn | \
                        tail -n +4 | \
                        xargs -r -I {} docker rmi ${DOCKER_IMAGE}:{} || true
                """
            }
            cleanWs()
        }
    }
}
