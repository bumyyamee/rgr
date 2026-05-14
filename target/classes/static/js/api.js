// api.js — общие функции для работы с API и токеном

const TOKEN_KEY = 'token';

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function setToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function removeToken() {
    localStorage.removeItem(TOKEN_KEY);
}

async function apiRequest(url, method = 'GET', body = null, isFormData = false) {
    const headers = {};
    
    if (!isFormData) {
        headers['Content-Type'] = 'application/json';
    }

    const token = getToken();
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    const config = { method, headers };

    if (body && method !== 'GET') {
        config.body = isFormData ? body : JSON.stringify(body);
    }

    const response = await fetch(url, config);

    if (response.status === 401) {
        removeToken();
        window.location.href = '/login.html';
        throw new Error('Unauthorized');
    }

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Ошибка запроса');
    }

    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
        return await response.json();
    }
    return null;
}