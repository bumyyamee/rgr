const TOKEN_KEY = 'token';              //сюда все для апи и токена
  
function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function setToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function removeToken() {
    localStorage.removeItem(TOKEN_KEY);
}

async function apiRequest(url, method = 'GET', body = null) {
    const headers = {
        'Content-Type': 'application/json'
    };

    const token = getToken();
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    const config = {
        method,
        headers
    };

    if (body && method !== 'GET') {
        config.body = JSON.stringify(body);
    }

    const response = await fetch(url, config);

    // Если токен истёк или недействителен — на логин
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