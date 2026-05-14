document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('registerForm');
    const errorDiv = document.getElementById('error');
    const successDiv = document.getElementById('success');

    registerForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        errorDiv.textContent = '';
        successDiv.textContent = '';

        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        try {
            await apiRequest('/api/auth/register', 'POST', { username, email, password });
            successDiv.textContent = 'Регистрация успешна! Перенаправляем на страницу входа...';
            setTimeout(() => {
                window.location.href = '/login.html';
            }, 2500);
        } catch (error) {
            errorDiv.textContent = error.message;
        }
    });
});