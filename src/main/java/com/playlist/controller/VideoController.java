package com.playlist.controller;

import com.playlist.model.Video;
import com.playlist.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para gestionar operaciones sobre videos
 */
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * GET /api/videos - Obtiene todos los videos
     */
    @GetMapping
    public ResponseEntity<List<Video>> obtenerTodos() {
        List<Video> videos = videoService.obtenerTodos();
        return ResponseEntity.ok(videos);
    }

    /**
     * GET /api/videos/{id} - Obtiene un video por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Video> obtenerPorId(@PathVariable String id) {
        Optional<Video> video = videoService.obtenerPorId(id);
        return video.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/videos - Agrega un nuevo video
     */
    @PostMapping
    public ResponseEntity<Video> agregarVideo(@RequestBody Map<String, String> payload) {
        String nombre = payload.get("nombre");
        String url = payload.get("url");
        
        if (nombre == null || nombre.trim().isEmpty() || url == null || url.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Video video = videoService.agregarVideo(nombre, url);
        return ResponseEntity.status(HttpStatus.CREATED).body(video);
    }

    /**
     * DELETE /api/videos/{id} - Elimina un video
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVideo(@PathVariable String id) {
        boolean eliminado = videoService.eliminarVideo(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST /api/videos/{id}/like - Da like a un video
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Video> darLike(@PathVariable String id) {
        Optional<Video> video = videoService.darLike(id);
        return video.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/videos/{id}/favorito - Marca/desmarca como favorito
     */
    @PostMapping("/{id}/favorito")
    public ResponseEntity<Video> toggleFavorito(@PathVariable String id) {
        Optional<Video> video = videoService.toggleFavorito(id);
        return video.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/videos/favoritos - Obtiene solo los videos favoritos
     */
    @GetMapping("/favoritos")
    public ResponseEntity<List<Video>> obtenerFavoritos() {
        List<Video> favoritos = videoService.obtenerFavoritos();
        return ResponseEntity.ok(favoritos);
    }
}
