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
    setupNavbar();
    
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

function setupNavbar() {
    const navbar = document.querySelector('.navbar-nav');
    if (navbar) {
        const username = localStorage.getItem('username');
        navbar.innerHTML += `
            <li class="nav-item">
                <span class="nav-link">
                    <i class="fas fa-user me-2"></i>
                    ${username}
                </span>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link" onclick="logout()">
                    <i class="fas fa-sign-out-alt me-2"></i>
                    Logout
                </a>
            </li>
        `;
    }
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
    const tableBody = document.getElementById('booksTableBody');
    tableBody.innerHTML = '';
    
    if (books.length === 0) {
        const emptyRow = document.createElement('tr');
        emptyRow.innerHTML = `
            <td colspan="4" class="text-center py-5">
                <i class="fas fa-book-open fa-3x mb-3 text-muted"></i>
                <p class="text-muted">Nessun libro trovato</p>
            </td>
        `;
        tableBody.appendChild(emptyRow);
        return;
    }
    
    books.forEach(book => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>
                <div class="book-cover">
                    <i class="fas fa-book"></i>
                </div>
            </td>
            <td>
                <div class="book-title">${book.title}</div>
                <div class="book-author">${book.author}</div>
            </td>
            <td>
                <div class="${book.available ? 'status-available' : 'status-unavailable'}">
                    <i class="fas fa-${book.available ? 'check-circle' : 'times-circle'}"></i>
                    ${book.available ? 'Disponibile' : 'In prestito a ' + book.owner}
                </div>
            </td>
            <td>
            ${book.available ?
                `<button class="btn btn-success btn-action" onclick="reserveBook('${book.id}')">
                    <i class="fas fa-bookmark"></i>
                    Prenota
                </button>` :
                (book.owner === localStorage.getItem('username') ?
                    `<button class="btn btn-warning btn-action" onclick="returnBook('${book.id}')">
                        <i class="fas fa-undo"></i>
                        Restituisci
                    </button>` :
                    `<button class="btn btn-secondary btn-action" disabled>
                        <i class="fas fa-lock"></i>
                        Non disponibile
                    </button>`
                )
            }
            </td>
        `;
        tableBody.appendChild(row);
    });
}
