package com.playlist.service;

import com.playlist.model.Video;
import com.playlist.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class VideoService {

    private final VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }


    public List<Video> obtenerTodos() {
        return videoRepository.findAll();
    }

    public Optional<Video> obtenerPorId(String id) {
        return videoRepository.findById(id);
    }

    public Video agregarVideo(String nombre, String url) {
        Video video = new Video(nombre, url);
        return videoRepository.save(video);
    }

    public boolean eliminarVideo(String id) {
        return videoRepository.deleteById(id);
    }

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

    public List<Video> obtenerFavoritos() {
        return videoRepository.findFavorites();
    }

    public long contarVideos() {
        return videoRepository.count();
    }
}
