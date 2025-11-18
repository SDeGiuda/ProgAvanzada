package com.playlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PlaylistApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaylistApplication.class, args);
        System.out.println("\n==============================================");
        System.out.println("  Mi Playlist Musical - Aplicación Iniciada");
        System.out.println("  Accede en: http://localhost:8080");
        System.out.println("==============================================\n");
    }
}
