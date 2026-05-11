document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const errorDiv = document.getElementById('error');

    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        errorDiv.textContent = '';

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const data = await apiRequest('/api/auth/login', 'POST', { username, password });
            setToken(data.token);
            window.location.href = '/index.html';
        } catch (error) {
            errorDiv.textContent = error.message;
        }
    });
});