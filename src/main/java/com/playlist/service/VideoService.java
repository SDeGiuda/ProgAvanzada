package com.playlist.service;

import com.playlist.model.Video;
import com.playlist.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que contiene la lógica de negocio para gestionar videos
 */
@Service
public class VideoService {

    private final VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    /**
     * Obtiene todos los videos
     */
    public List<Video> obtenerTodos() {
        return videoRepository.findAll();
    }

    /**
     * Obtiene un video por ID
     */
    public Optional<Video> obtenerPorId(String id) {
        return videoRepository.findById(id);
    }

    /**
     * Agrega un nuevo video a la playlist
     */
    public Video agregarVideo(String nombre, String url) {
        Video video = new Video(nombre, url);
        return videoRepository.save(video);
    }

    /**
     * Elimina un video de la playlist
     */
    public boolean eliminarVideo(String id) {
        return videoRepository.deleteById(id);
    }

    /**
     * Incrementa los likes de un video
     */
    public Optional<Video> darLike(String id) {
        Optional<Video> videoOpt = videoRepository.findById(id);
        if (videoOpt.isPresent()) {
            Video video = videoOpt.get();
            video.incrementarLikes();
            videoRepository.update(video);
            return Optional.of(video);
        }
        return Optional.empty();
    }

    /**
     * Marca o desmarca un video como favorito
     */
    public Optional<Video> toggleFavorito(String id) {
        Optional<Video> videoOpt = videoRepository.findById(id);
        if (videoOpt.isPresent()) {
            Video video = videoOpt.get();
            video.toggleFavorito();
            videoRepository.update(video);
            return Optional.of(video);
        }
        return Optional.empty();
    }

    /**
     * Obtiene todos los videos favoritos
     */
    public List<Video> obtenerFavoritos() {
        return videoRepository.findFavorites();
    }

    /**
     * Obtiene el total de videos en la playlist
     */
    public long contarVideos() {
        return videoRepository.count();
    }
}
