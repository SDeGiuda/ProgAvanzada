package com.playlist.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.model.Video;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar la persistencia de videos en archivo JSON
 */
@Repository
public class VideoRepository {
    
    private static final String DATA_FILE = "videos.json";
    private final ObjectMapper objectMapper;
    private List<Video> videos;

    public VideoRepository() {
        this.objectMapper = new ObjectMapper();
        this.videos = loadVideos();
    }

    /**
     * Carga los videos desde el archivo JSON
     */
    private List<Video> loadVideos() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, new TypeReference<List<Video>>() {});
            } catch (IOException e) {
                System.err.println("Error al cargar videos: " + e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Guarda los videos en el archivo JSON
     */
    private void saveVideos() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), videos);
        } catch (IOException e) {
            System.err.println("Error al guardar videos: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los videos
     */
    public List<Video> findAll() {
        return new ArrayList<>(videos);
    }

    /**
     * Busca un video por ID
     */
    public Optional<Video> findById(String id) {
        return videos.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    /**
     * Guarda un nuevo video
     */
    public Video save(Video video) {
        videos.add(video);
        saveVideos();
        return video;
    }

    /**
     * Actualiza un video existente
     */
    public Video update(Video video) {
        videos.removeIf(v -> v.getId().equals(video.getId()));
        videos.add(video);
        saveVideos();
        return video;
    }

    /**
     * Elimina un video por ID
     */
    public boolean deleteById(String id) {
        boolean removed = videos.removeIf(v -> v.getId().equals(id));
        if (removed) {
            saveVideos();
        }
        return removed;
    }

    /**
     * Obtiene todos los videos favoritos
     */
    public List<Video> findFavorites() {
        return videos.stream()
                .filter(Video::isFavorito)
                .toList();
    }

    /**
     * Cuenta el total de videos
     */
    public long count() {
        return videos.size();
    }
}
