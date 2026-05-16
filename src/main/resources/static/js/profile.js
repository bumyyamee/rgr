// profile.js — страница профиля

function checkAuth() {
    if (!getToken()) {
        window.location.href = '/login.html';
        return false;
    }
    return true;
}

function logout() {
    removeToken();
    window.location.href = '/login.html';
}

async function loadProfile() {
    try {
        const user = await apiRequest('/api/users/me');
        document.getElementById('profileUsername').textContent = user.username;
        document.getElementById('currentUserName').textContent = user.username + ' ▼';
    } catch (e) {
        console.error('Ошибка загрузки профиля:', e);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    if (!checkAuth()) return;

    loadProfile();

    document.getElementById('userMenu').addEventListener('click', () => {
        const dropdown = document.getElementById('profileDropdown');
        dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none';
    });

    document.addEventListener('click', (e) => {
        if (!e.target.closest('#userMenu')) {
            document.getElementById('profileDropdown').style.display = 'none';
        }
    });

    document.getElementById('logoutBtn').addEventListener('click', (e) => {
        e.preventDefault();
        logout();
    });

    // Переключение вкладок
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
        });
    });
});