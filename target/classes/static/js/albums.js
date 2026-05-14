function privacyLabel(privacy) {
    const labels = {
        'PUBLIC': 'Публичный',
        'FRIENDS': 'Для друзей',
        'PRIVATE': 'Приватный'
    };
    return labels[privacy] || privacy;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

//сетку здесь рендерить
function renderAlbums(albums) {
    const grid = document.getElementById('albumGrid');
    
    if (!albums || albums.length === 0) {
        grid.innerHTML = '<p class="empty-state">У вас пока нет альбомов. Создайте первый!</p>';
        return;
    }

    grid.innerHTML = albums.map(album => `
        <div class="album-card" data-id="${album.id}">
            <div class="album-cover">
                <img src="${album.coverPhotoUrl || 'https://via.placeholder.com/400x180'}" 
                     alt="Обложка" loading="lazy">
            </div>
            <div class="album-info">
                <div class="album-title">${escapeHtml(album.title)}</div>
                <div class="album-meta">
                    <span>📷 ${album.photoCount}</span>
                    <span>${privacyLabel(album.privacy)}</span>
                </div>
                <button class="btn-view-album" data-action="view" data-id="${album.id}">
                    Открыть
                </button>
            </div>
        </div>
    `).join('');
}

//альбомная модалка
async function loadAlbumIntoModal(albumId) {
    const modal = document.getElementById('albumModal');
    
    document.getElementById('modalTitle').textContent = 'Загрузка...';
    document.getElementById('modalDescription').textContent = '';
    document.getElementById('modalPrivacy').textContent = '';
    document.getElementById('modalPhotoCount').textContent = '...';
    document.getElementById('modalPhotos').innerHTML = '<p>Загрузка...</p>';
    Modal.open('albumModal');

    try {
        const [album, photos] = await Promise.all([
            apiRequest(`/api/albums/${albumId}`),
            apiRequest(`/api/albums/${albumId}/photos`)
        ]);

        document.getElementById('modalTitle').textContent = album.title;
        document.getElementById('modalDescription').textContent = album.description || 'Без описания';
        document.getElementById('modalPrivacy').textContent = privacyLabel(album.privacy);
        document.getElementById('modalPhotoCount').textContent = album.photoCount;

        const photosContainer = document.getElementById('modalPhotos');
        if (photos.length === 0) {
            photosContainer.innerHTML = '<p class="empty-state">В альбоме пока нет фотографий</p>';
        } else {
            photosContainer.innerHTML = photos.map(photo => `
                <div class="photo-thumb">
                    <img src="${photo.filePath}" alt="Фото" loading="lazy">
                </div>
            `).join('');
        }
    } catch (error) {
        document.getElementById('modalTitle').textContent = 'Ошибка';
        document.getElementById('modalPhotos').innerHTML = 
            `<p class="error">Не удалось загрузить: ${error.message}</p>`;
    }
}

async function loadAlbums() {
    const grid = document.getElementById('albumGrid');
    grid.innerHTML = '<p>Загрузка альбомов...</p>';
    
    try {
        const albums = await apiRequest('/api/albums/my');
        renderAlbums(albums);
    } catch (error) {
        grid.innerHTML = `<p class="error">Ошибка: ${error.message}</p>`;
    }
}

async function createAlbum(formData) {
    const params = new URLSearchParams({
        title: formData.get('title'),
        description: formData.get('description') || '',
        privacy: formData.get('privacy') || 'PRIVATE'
    });

    await apiRequest(`/api/albums?${params}`, 'POST');
    Modal.close('createAlbumModal');
    await loadAlbums();
}

// при загрузке страницы навешиваем обработчики
document.addEventListener('DOMContentLoaded', () => {
    loadAlbums();

    document.getElementById('albumGrid').addEventListener('click', (e) => {
        const btn = e.target.closest('[data-action="view"]');
        if (btn) {
            loadAlbumIntoModal(btn.dataset.id);
        }
    });

    document.getElementById('createAlbumBtn').addEventListener('click', () => {
        Modal.open('createAlbumModal');
    });

    document.getElementById('createAlbumForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const btn = e.target.querySelector('button[type="submit"]');
        btn.disabled = true;
        btn.textContent = 'Создание...';
        
        try {
            await createAlbum(new FormData(e.target));
            e.target.reset();
        } catch (error) {
            alert('Ошибка: ' + error.message);
        } finally {
            btn.disabled = false;
            btn.textContent = 'Создать';
        }
    });
});