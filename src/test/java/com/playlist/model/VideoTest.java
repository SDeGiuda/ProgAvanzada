package com.playlist.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Video
 */
class VideoTest {

    private Video video;

    @BeforeEach
    void setUp() {
        video = new Video("Imagine", "https://www.youtube.com/watch?v=YkgkThdzX-8");
    }

    @Test
    @DisplayName("Debe crear un video con valores por defecto")
    void testCrearVideo() {
        assertNotNull(video.getId());
        assertEquals("Imagine", video.getNombre());
        assertEquals("https://www.youtube.com/watch?v=YkgkThdzX-8", video.getUrl());
        assertEquals(0, video.getLikes());
        assertFalse(video.isFavorito());
    }

    @Test
    @DisplayName("Debe incrementar likes correctamente")
    void testIncrementarLikes() {
        int likesIniciales = video.getLikes();
        video.incrementarLikes();
        assertEquals(likesIniciales + 1, video.getLikes());
    }

    @Test
    @DisplayName("Debe alternar favorito correctamente")
    void testToggleFavorito() {
        assertFalse(video.isFavorito());
        video.toggleFavorito();
        assertTrue(video.isFavorito());
        video.toggleFavorito();
        assertFalse(video.isFavorito());
    }

    @Test
    @DisplayName("Debe convertir URL de YouTube watch a embed")
    void testGetEmbedUrlYouTubeWatch() {
        String embedUrl = video.getEmbedUrl();
        assertTrue(embedUrl.contains("youtube.com/embed/"));
        assertTrue(embedUrl.contains("YkgkThdzX-8"));
    }

    @Test
    @DisplayName("Debe convertir URL de YouTube youtu.be a embed")
    void testGetEmbedUrlYouTubeShort() {
        Video videoShort = new Video("Test", "https://youtu.be/dQw4w9WgXcQ");
        String embedUrl = videoShort.getEmbedUrl();
        assertTrue(embedUrl.contains("youtube.com/embed/"));
        assertTrue(embedUrl.contains("dQw4w9WgXcQ"));
    }

    @Test
    @DisplayName("Debe convertir URL de Vimeo a embed")
    void testGetEmbedUrlVimeo() {
        Video vimeoVideo = new Video("Test Vimeo", "https://vimeo.com/123456789");
        String embedUrl = vimeoVideo.getEmbedUrl();
        assertTrue(embedUrl.contains("player.vimeo.com/video/"));
        assertTrue(embedUrl.contains("123456789"));
    }

    @Test
    @DisplayName("Debe convertir URL de Dailymotion a embed")
    void testGetEmbedUrlDailymotion() {
        Video dailyVideo = new Video("Test Daily", "https://www.dailymotion.com/video/x8abcde");
        String embedUrl = dailyVideo.getEmbedUrl();
        assertTrue(embedUrl.contains("dailymotion.com/embed/video/"));
        assertTrue(embedUrl.contains("x8abcde"));
    }

    @Test
    @DisplayName("Debe manejar URLs con parámetros adicionales")
    void testGetEmbedUrlConParametros() {
        Video videoParam = new Video("Test", "https://www.youtube.com/watch?v=YkgkThdzX-8&t=30s");
        String embedUrl = videoParam.getEmbedUrl();
        assertTrue(embedUrl.contains("youtube.com/embed/"));
        assertTrue(embedUrl.contains("YkgkThdzX-8"));
        assertFalse(embedUrl.contains("&t="));
    }

    @Test
    @DisplayName("Debe comparar videos por ID correctamente")
    void testEquals() {
        Video video1 = new Video("Song 1", "http://example.com/1");
        Video video2 = new Video("Song 2", "http://example.com/2");
        Video video3 = new Video("Song 3", "http://example.com/3");
        
        video3.setId(video1.getId());
        
        assertEquals(video1, video3);
        assertNotEquals(video1, video2);
    }

    @Test
    @DisplayName("ToString debe incluir información del video")
    void testToString() {
        String toString = video.toString();
        assertTrue(toString.contains("Imagine"));
        assertTrue(toString.contains("youtube.com"));
    }
}
