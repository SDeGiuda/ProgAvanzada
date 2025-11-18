let allVideos = [];
let currentFilter = 'all';

document.addEventListener('DOMContentLoaded', function() {
    cargarVideos();
});

document.getElementById('addVideoForm').addEventListener('submit', function(e) {
    e.preventDefault();
    agregarVideo();
});

async function cargarVideos() {
    try {
        const response = await fetch('/api/videos');
        allVideos = await response.json();
        mostrarVideos();
    } catch (error) {
        console.error('Error al cargar videos:', error);
    }
}

async function agregarVideo() {
    const nombre = document.getElementById('videoName').value;
    const url = document.getElementById('videoUrl').value;

    try {
        const response = await fetch('/api/videos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ nombre, url })
        });

        if (response.ok) {
            document.getElementById('addVideoForm').reset();
            await cargarVideos();
        }
    } catch (error) {
        console.error('Error al agregar video:', error);
    }
}

async function eliminarVideo(id) {
    if (!confirm('¿Estás seguro de eliminar este video?')) return;

    try {
        const response = await fetch(`/api/videos/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            await cargarVideos();
        }
    } catch (error) {
        console.error('Error al eliminar video:', error);
    }
}

async function darLike(id) {
    try {
        const response = await fetch(`/api/videos/${id}/like`, {
            method: 'POST'
        });

        if (response.ok) {
            const videoActualizado = await response.json();

            const index = allVideos.findIndex(v => v.id === id);
            if (index !== -1) {
                allVideos[index] = videoActualizado;
            }

            const videoCard = document.querySelector(`[data-video-id="${id}"]`);
            if (videoCard) {
                const likesCount = videoCard.querySelector('.likes-count');
                if (likesCount) {
                    likesCount.textContent = videoActualizado.likes;
                }
            }
        }
    } catch (error) {
        console.error('Error al dar like:', error);
    }
}

async function toggleFavorito(id) {
    try {
        const response = await fetch(`/api/videos/${id}/favorito`, {
            method: 'POST'
        });

        if (response.ok) {
            const videoActualizado = await response.json();


            const index = allVideos.findIndex(v => v.id === id);
            if (index !== -1) {
                allVideos[index] = videoActualizado;
            }

            const videoCard = document.querySelector(`[data-video-id="${id}"]`);
            if (videoCard) {
                const existingBadge = videoCard.querySelector('.favorite-badge');
                if (videoActualizado.favorito && !existingBadge) {
                    const badge = document.createElement('span');
                    badge.className = 'favorite-badge';
                    badge.innerHTML = '<i class="fas fa-star"></i> Favorito';
                    videoCard.insertBefore(badge, videoCard.firstChild);
                } else if (!videoActualizado.favorito && existingBadge) {
                    existingBadge.remove();
                }

                const btnFavorite = videoCard.querySelector('.btn-favorite');
                const btnText = videoCard.querySelector('.btn-favorite-text');
                if (btnFavorite && btnText) {
                    if (videoActualizado.favorito) {
                        btnFavorite.classList.add('active');
                        btnText.textContent = 'Favorito';
                    } else {
                        btnFavorite.classList.remove('active');
                        btnText.textContent = 'Marcar';
                    }
                }
            }
        }
    } catch (error) {
        console.error('Error al marcar favorito:', error);
    }
}

function filterVideos(filter) {
    currentFilter = filter;

    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');

    mostrarVideos();
}

function mostrarVideos() {
    const container = document.getElementById('videosContainer');

    let videosToShow = allVideos;
    if (currentFilter === 'favorites') {
        videosToShow = allVideos.filter(v => v.favorito);
    }

    document.getElementById('videoCount').textContent = `${allVideos.length} videos`;

    if (videosToShow.length === 0) {
        container.innerHTML = `
            <div class="col-12 empty-state">
                <i class="fas fa-music"></i>
                <h3>${currentFilter === 'favorites' ? 'No tienes videos favoritos' : 'No hay videos en tu playlist'}</h3>
                <p>${currentFilter === 'favorites' ? 'Marca algunos videos como favoritos' : 'Agrega tu primera canción usando el formulario de arriba'}</p>
            </div>
        `;
        return;
    }

    container.innerHTML = videosToShow.map(video => {
        const isSpotify = video.url.includes('spotify.com');
        const containerClass = isSpotify ? 'video-container spotify-container' : 'video-container';

        return `
        <div class="col-md-6 col-lg-4">
            <div class="video-card" data-video-id="${video.id}">
                ${video.favorito ? '<span class="favorite-badge"><i class="fas fa-star"></i> Favorito</span>' : ''}
                <div class="${containerClass}">
                    <iframe src="${getEmbedUrl(video.url)}"
                            frameborder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                            allowfullscreen>
                    </iframe>
                </div>
                <div class="video-info">
                    <div class="video-title">${video.nombre}</div>
                    <div class="video-actions">
                        <button class="btn-like" onclick="darLike('${video.id}')">
                            <i class="fas fa-heart"></i> <span class="likes-count">${video.likes}</span>
                        </button>
                        <button class="btn-favorite ${video.favorito ? 'active' : ''}" onclick="toggleFavorito('${video.id}')">
                            <i class="fas fa-star"></i> <span class="btn-favorite-text">${video.favorito ? 'Favorito' : 'Marcar'}</span>
                        </button>
                        <button class="btn-delete" onclick="eliminarVideo('${video.id}')">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        `;
    }).join('');
}

function getEmbedUrl(url) {
    let embedUrl = url;

    if (url.includes('youtube.com/watch?v=')) {
        const videoId = url.split('watch?v=')[1].split('&')[0];
        embedUrl = `https://www.youtube.com/embed/${videoId}`;
    } else if (url.includes('youtu.be/')) {
        const videoId = url.split('youtu.be/')[1].split('?')[0];
        embedUrl = `https://www.youtube.com/embed/${videoId}`;
    }

    if (url.includes('vimeo.com/')) {
        const videoId = url.split('vimeo.com/')[1].split('?')[0];
        embedUrl = `https://player.vimeo.com/video/${videoId}`;
    }

    // Spotify: tracks, albums, playlists
    if (url.includes('open.spotify.com/')) {
        // Convertir URLs de Spotify al formato embed
        embedUrl = url.replace('open.spotify.com/', 'open.spotify.com/embed/');
        // Limpiar parámetros innecesarios
        if (embedUrl.includes('?')) {
            embedUrl = embedUrl.split('?')[0];
        }
    }

    return embedUrl;
}
