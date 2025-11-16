package com.playlist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase que representa un video musical en la playlist
 */
public class Video {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("likes")
    private int likes;
    
    @JsonProperty("favorito")
    private boolean favorito;

    // Constructor vacío para Jackson
    public Video() {
        this.id = UUID.randomUUID().toString();
        this.likes = 0;
        this.favorito = false;
    }

    // Constructor con parámetros
    public Video(String nombre, String url) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.url = url;
        this.likes = 0;
        this.favorito = false;
    }

    // CODE SMELL: Long Method - Este método hace demasiadas cosas
    // Debería refactorizarse usando Extract Method
    @JsonIgnore
    public String getEmbedUrl() {
        String embedUrl = this.url;
        
        // Procesar URL de YouTube
        if (embedUrl.contains("youtube.com/watch?v=")) {
            String videoId = embedUrl.substring(embedUrl.indexOf("watch?v=") + 8);
            if (videoId.contains("&")) {
                videoId = videoId.substring(0, videoId.indexOf("&"));
            }
            embedUrl = "https://www.youtube.com/embed/" + videoId;
        } else if (embedUrl.contains("youtu.be/")) {
            String videoId = embedUrl.substring(embedUrl.indexOf("youtu.be/") + 9);
            if (videoId.contains("?")) {
                videoId = videoId.substring(0, videoId.indexOf("?"));
            }
            embedUrl = "https://www.youtube.com/embed/" + videoId;
        }
        
        // Procesar URL de Vimeo
        if (embedUrl.contains("vimeo.com/")) {
            String videoId = embedUrl.substring(embedUrl.lastIndexOf("/") + 1);
            if (videoId.contains("?")) {
                videoId = videoId.substring(0, videoId.indexOf("?"));
            }
            embedUrl = "https://player.vimeo.com/video/" + videoId;
        }
        
        // Procesar URL de Dailymotion
        if (embedUrl.contains("dailymotion.com/video/")) {
            String videoId = embedUrl.substring(embedUrl.indexOf("video/") + 6);
            if (videoId.contains("?")) {
                videoId = videoId.substring(0, videoId.indexOf("?"));
            }
            if (videoId.contains("_")) {
                videoId = videoId.substring(0, videoId.indexOf("_"));
            }
            embedUrl = "https://www.dailymotion.com/embed/video/" + videoId;
        }
        
        return embedUrl;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public void incrementarLikes() {
        this.likes++;
    }

    public void toggleFavorito() {
        this.favorito = !this.favorito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(id, video.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", url='" + url + '\'' +
                ", likes=" + likes +
                ", favorito=" + favorito +
                '}';
    }
}
