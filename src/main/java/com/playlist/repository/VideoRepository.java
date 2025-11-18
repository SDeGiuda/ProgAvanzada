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


@Repository
public class VideoRepository {
    
    private static final String DATA_FILE = "videos.json";
    private final ObjectMapper objectMapper;
    private List<Video> videos;

    public VideoRepository() {
        this.objectMapper = new ObjectMapper();
        this.videos = loadVideos();
    }

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


    private void saveVideos() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), videos);
        } catch (IOException e) {
            System.err.println("Error al guardar videos: " + e.getMessage());
        }
    }


    public List<Video> findAll() {
        return new ArrayList<>(videos);
    }


    public Optional<Video> findById(String id) {
        return videos.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }


    public Video save(Video video) {
        videos.add(video);
        saveVideos();
        return video;
    }


    public Video update(Video video) {
        videos.removeIf(v -> v.getId().equals(video.getId()));
        videos.add(video);
        saveVideos();
        return video;
    }


    public boolean deleteById(String id) {
        boolean removed = videos.removeIf(v -> v.getId().equals(id));
        if (removed) {
            saveVideos();
        }
        return removed;
    }


    public List<Video> findFavorites() {
        return videos.stream()
                .filter(Video::isFavorito)
                .toList();
    }


    public long count() {
        return videos.size();
    }
}
