package com.playlist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;

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


    public Video() {
        this.id = UUID.randomUUID().toString();
        this.likes = 0;
        this.favorito = false;
    }


    public Video(String nombre, String url) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.url = url;
        this.likes = 0;
        this.favorito = false;
    }


    @JsonIgnore
    public String getEmbedUrl() {
        String embedUrl = this.url;


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


        if (embedUrl.contains("vimeo.com/")) {
            String videoId = embedUrl.substring(embedUrl.lastIndexOf("/") + 1);
            if (videoId.contains("?")) {
                videoId = videoId.substring(0, videoId.indexOf("?"));
            }
            embedUrl = "https://player.vimeo.com/video/" + videoId;
        }


        if (embedUrl.contains("open.spotify.com/")) {
            embedUrl = embedUrl.replace("open.spotify.com/", "open.spotify.com/embed/");
            if (embedUrl.contains("?")) {
                embedUrl = embedUrl.substring(0, embedUrl.indexOf("?"));
            }
        }


        return embedUrl;
    }


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
