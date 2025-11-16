package com.playlist.service;

import com.playlist.model.Video;
import com.playlist.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para VideoService
 */
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;

    private Video videoTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        videoTest = new Video("Bohemian Rhapsody", "https://www.youtube.com/watch?v=fJ9rUzIMcZQ");
        videoTest.setId("test-id-123");
    }

    @Test
    @DisplayName("Debe obtener todos los videos correctamente")
    void testObtenerTodos() {
        // Arrange
        Video video1 = new Video("Song 1", "http://example.com/1");
        Video video2 = new Video("Song 2", "http://example.com/2");
        List<Video> videos = Arrays.asList(video1, video2);
        
        when(videoRepository.findAll()).thenReturn(videos);

        // Act
        List<Video> resultado = videoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(videoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe agregar un video correctamente")
    void testAgregarVideo() {
        // Arrange
        when(videoRepository.save(any(Video.class))).thenReturn(videoTest);

        // Act
        Video resultado = videoService.agregarVideo("Bohemian Rhapsody", "https://www.youtube.com/watch?v=fJ9rUzIMcZQ");

        // Assert
        assertNotNull(resultado);
        assertEquals("Bohemian Rhapsody", resultado.getNombre());
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    @DisplayName("Debe eliminar un video correctamente")
    void testEliminarVideo() {
        // Arrange
        when(videoRepository.deleteById(anyString())).thenReturn(true);

        // Act
        boolean resultado = videoService.eliminarVideo("test-id-123");

        // Assert
        assertTrue(resultado);
        verify(videoRepository, times(1)).deleteById("test-id-123");
    }

    @Test
    @DisplayName("Debe incrementar likes correctamente")
    void testDarLike() {
        // Arrange
        int likesIniciales = videoTest.getLikes();
        when(videoRepository.findById(anyString())).thenReturn(Optional.of(videoTest));
        when(videoRepository.update(any(Video.class))).thenReturn(videoTest);

        // Act
        Optional<Video> resultado = videoService.darLike("test-id-123");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(likesIniciales + 1, resultado.get().getLikes());
        verify(videoRepository, times(1)).update(any(Video.class));
    }

    @Test
    @DisplayName("Debe marcar como favorito correctamente")
    void testToggleFavorito() {
        // Arrange
        assertFalse(videoTest.isFavorito());
        when(videoRepository.findById(anyString())).thenReturn(Optional.of(videoTest));
        when(videoRepository.update(any(Video.class))).thenReturn(videoTest);

        // Act
        Optional<Video> resultado = videoService.toggleFavorito("test-id-123");

        // Assert
        assertTrue(resultado.isPresent());
        assertTrue(resultado.get().isFavorito());
        verify(videoRepository, times(1)).update(any(Video.class));
    }

    @Test
    @DisplayName("Debe retornar Optional.empty cuando el video no existe")
    void testDarLikeVideoNoExiste() {
        // Arrange
        when(videoRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<Video> resultado = videoService.darLike("id-inexistente");

        // Assert
        assertFalse(resultado.isPresent());
        verify(videoRepository, never()).update(any(Video.class));
    }

    @Test
    @DisplayName("Debe contar los videos correctamente")
    void testContarVideos() {
        // Arrange
        when(videoRepository.count()).thenReturn(5L);

        // Act
        long count = videoService.contarVideos();

        // Assert
        assertEquals(5L, count);
        verify(videoRepository, times(1)).count();
    }

    @Test
    @DisplayName("Debe obtener videos favoritos correctamente")
    void testObtenerFavoritos() {
        // Arrange
        Video favorito1 = new Video("Favorito 1", "http://example.com/1");
        favorito1.setFavorito(true);
        Video favorito2 = new Video("Favorito 2", "http://example.com/2");
        favorito2.setFavorito(true);
        
        List<Video> favoritos = Arrays.asList(favorito1, favorito2);
        when(videoRepository.findFavorites()).thenReturn(favoritos);

        // Act
        List<Video> resultado = videoService.obtenerFavoritos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Video::isFavorito));
        verify(videoRepository, times(1)).findFavorites();
    }
}
