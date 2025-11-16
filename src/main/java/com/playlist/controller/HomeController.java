package com.playlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para servir las vistas HTML
 */
@Controller
public class HomeController {

    /**
     * Página principal de la aplicación
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}

// Coment para testear pipelineas