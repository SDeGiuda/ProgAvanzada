pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.0'
        jdk 'JDK_18'
    }
    
    environment {
        APP_NAME = 'mi-playlist'
        APP_PORT = '8080'
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
                script {
                    if (isUnix()) {
                        sh 'mvn clean compile'
                    } else {
                        bat 'mvn clean compile'
                    }
                }
            }
        }
        
        stage('Test') {
            steps {
                echo '========== Ejecutando tests unitarios =========='
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
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
                script {
                    if (isUnix()) {
                        sh 'mvn package -DskipTests'
                    } else {
                        bat 'mvn package -DskipTests'
                    }
                }
            }
        }
        
        stage('Deploy') {
            steps {
                echo '========== Desplegando la aplicación =========='
                script {
                    if (isUnix()) {
                        sh '''
                            chmod +x deploy.sh
                            ./deploy.sh
                        '''
                    } else {
                        bat 'call deploy.bat'
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo '========== Pipeline ejecutado exitosamente =========='
            echo "Aplicación disponible en http://localhost:${APP_PORT}"
        }
        failure {
            echo '========== Pipeline falló =========='
        }
        always {
            echo '========== Limpiando workspace =========='
            cleanWs()
        }
    }
}
