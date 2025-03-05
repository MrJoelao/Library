// Toast instance
let toast;

document.addEventListener('DOMContentLoaded', () => {
    // Check authentication
    if (!localStorage.getItem('username')) {
        window.location.href = 'login.html';
        return;
    }

    loadBooks();
    initializeToast();
    setupTheme();
    setupSidebar();
    
    // Real-time search with debounce
    let searchTimeout;
    document.getElementById('searchInput').addEventListener('input', (e) => {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            searchBooks();
        }, 300); // Wait 300ms after user stops typing
    });

    // Keep enter key support as fallback
    document.getElementById('searchInput').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            clearTimeout(searchTimeout);
            searchBooks();
        }
    });
});

function initializeToast() {
    toast = new bootstrap.Toast(document.getElementById('notificationToast'));
}

function showNotification(message, isError = false) {
    const toastElement = document.getElementById('notificationToast');
    const toastBody = toastElement.querySelector('.toast-body');
    toastBody.textContent = message;
    
    toastElement.classList.remove('bg-danger', 'bg-success');
    toastElement.classList.add(isError ? 'bg-danger' : 'bg-success');
    
    toast.show();
}

function loadBooks() {
    const container = document.getElementById('booksContainer');
    container.classList.add('loading');
    
    fetch('/api/books')
        .then(response => response.json())
        .then(books => {
            displayBooks(books);
            container.classList.remove('loading');
        })
        .catch(error => {
            console.error('Errore nel caricamento dei libri:', error);
            showNotification('Errore nel caricamento dei libri', true);
            container.classList.remove('loading');
        });
}

function searchBooks() {
    const query = document.getElementById('searchInput').value;
    const container = document.getElementById('booksContainer');
    container.classList.add('loading');
    
    fetch(`/api/books/search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(books => {
            displayBooks(books);
            container.classList.remove('loading');
        })
        .catch(error => {
            console.error('Errore nella ricerca:', error);
            showNotification('Errore durante la ricerca', true);
            container.classList.remove('loading');
        });
}

// Theme Management
function setupTheme() {
    // Load saved theme or default to light
    const savedTheme = localStorage.getItem('theme') || 'light';
    setTheme(savedTheme);
    
    document.getElementById('userNameDisplay').textContent = localStorage.getItem('username');
}

let selectedTheme = null;

function openThemeModal() {
    const modal = document.getElementById('themeModal');
    modal.classList.add('show');
    // Set currently active theme
    const currentTheme = document.body.getAttribute('data-theme');
    setActiveTheme(currentTheme);
    selectedTheme = currentTheme;
}

function closeThemeModal() {
    const modal = document.getElementById('themeModal');
    modal.classList.remove('show');
    // Reset preview to current theme
    const currentTheme = document.body.getAttribute('data-theme');
    document.body.setAttribute('data-theme', currentTheme);
}

function previewTheme(theme) {
    document.body.setAttribute('data-theme', theme);
    setActiveTheme(theme);
    selectedTheme = theme;
}

function setActiveTheme(theme) {
    document.querySelectorAll('.theme-item').forEach(item => {
        item.classList.remove('active');
        if (item.getAttribute('onclick').includes(theme)) {
            item.classList.add('active');
        }
    });
}

function applyTheme() {
    if (selectedTheme) {
        localStorage.setItem('theme', selectedTheme);
        closeThemeModal();
    }
}

// Load saved theme or default to light
function setupTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.body.setAttribute('data-theme', savedTheme);
    setActiveTheme(savedTheme);
}

// Sidebar Management
function setupSidebar() {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');
    
    // Toggle sidebar on button click
    sidebarToggle.addEventListener('click', () => {
        document.body.classList.toggle('sidebar-collapsed');
    });
    
    // Handle small screens
    if (window.innerWidth <= 768) {
        document.body.classList.add('sidebar-collapsed');
    }
    
    // Close sidebar when clicking outside on small screens
    document.addEventListener('click', (e) => {
        if (window.innerWidth <= 768 && 
            !sidebar.contains(e.target) && 
            !sidebarToggle.contains(e.target)) {
            document.body.classList.add('sidebar-collapsed');
        }
    });
    
    // Handle window resize
    window.addEventListener('resize', () => {
        if (window.innerWidth <= 768) {
            document.body.classList.add('sidebar-collapsed');
        }
    });
    
    // Setup submenu toggles
    document.querySelectorAll('.sidebar-nav .dropdown').forEach(dropdown => {
        const toggle = dropdown.querySelector('[data-bs-toggle]');
        toggle.addEventListener('click', () => {
            toggle.classList.toggle('collapsed');
        });
    });
}

function logout() {
    localStorage.removeItem('username');
    window.location.href = 'login.html';
}

function reserveBook(id) {
    const username = localStorage.getItem('username');
    fetch(`/api/books/${id}/reserve?username=${encodeURIComponent(username)}`, { method: 'POST' })
        .then(response => {
            if (response.ok) {
                loadBooks();
                showNotification('Libro prenotato con successo');
            } else {
                showNotification('Impossibile prenotare il libro', true);
            }
        })
        .catch(error => {
            console.error('Errore nella prenotazione:', error);
            showNotification('Errore durante la prenotazione', true);
        });
}

function returnBook(id) {
    const username = localStorage.getItem('username');
    fetch(`/api/books/${id}/return?username=${encodeURIComponent(username)}`, { method: 'POST' })
        .then(response => {
            if (response.ok) {
                loadBooks();
                showNotification('Libro restituito con successo');
            } else {
                showNotification('Impossibile restituire il libro', true);
            }
        })
        .catch(error => {
            console.error('Errore nella restituzione:', error);
            showNotification('Errore durante la restituzione', true);
        });
}

function displayBooks(books) {
    const container = document.getElementById('booksContainer');
    container.innerHTML = '';
    
    if (books.length === 0) {
        container.innerHTML = `
            <div class="text-center py-5">
                <i class="fas fa-book-open fa-3x mb-3 text-muted"></i>
                <p class="text-muted">Nessun libro trovato</p>
            </div>
        `;
        return;
    }
    
    books.forEach(book => {
        const card = document.createElement('div');
        card.className = 'book-card';
        card.innerHTML = `
            <div class="book-cover">
                <i class="fas fa-book fa-3x"></i>
            </div>
            <div class="book-info">
                <h3 class="book-title">${book.title}</h3>
                <p class="book-author">${book.author}</p>
                <div class="${book.available ? 'status-available' : 'status-unavailable'} mb-3">
                    <i class="fas fa-${book.available ? 'check-circle' : 'times-circle'}"></i>
                    ${book.available ? 'Disponibile' : 'In prestito a ' + book.owner}
                </div>
                ${book.available ?
                    `<button class="btn btn-success btn-action w-100" onclick="reserveBook('${book.id}')">
                        <i class="fas fa-bookmark"></i>
                        Prenota
                    </button>` :
                    (book.owner === localStorage.getItem('username') ?
                        `<button class="btn btn-warning btn-action w-100" onclick="returnBook('${book.id}')">
                            <i class="fas fa-undo"></i>
                            Restituisci
                        </button>` :
                        `<button class="btn btn-secondary btn-action w-100" disabled>
                            <i class="fas fa-lock"></i>
                            Non disponibile
                        </button>`
                    )
                }
            </div>
        `;
        container.appendChild(card);
    });
}
