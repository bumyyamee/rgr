// для всех модалок

const Modal = {
    open(modalId) {
        const modal = document.getElementById(modalId);
        if (!modal) return;
        modal.classList.add('active');
        document.body.style.overflow = 'hidden';
    },

    close(modalId) {
        const modal = document.getElementById(modalId);
        if (!modal) return;
        modal.classList.remove('active');

        if (!document.querySelector('.modal.active')) {
            document.body.style.overflow = '';
        }
    },

    closeAll() {
        document.querySelectorAll('.modal.active').forEach(modal => {
            modal.classList.remove('active');
        });
        document.body.style.overflow = '';
    }
};


document.addEventListener('DOMContentLoaded', () => {
    document.addEventListener('click', (e) => {
        if (e.target.hasAttribute('data-close')) {
            const modal = e.target.closest('.modal');
            if (modal) Modal.close(modal.id);
        }
    });

    //закрытие если тыкнуть по фону
    document.addEventListener('click', (e) => {
        if (e.target.classList.contains('modal') && e.target.classList.contains('active')) {
            Modal.close(e.target.id);
        }
    });

    //закрытие через эскейп
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            const openModals = document.querySelectorAll('.modal.active');
            if (openModals.length > 0) {
                Modal.close(openModals[openModals.length - 1].id);
            }
        }
    });
});