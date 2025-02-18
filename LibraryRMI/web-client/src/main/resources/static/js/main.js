// Toast instance
let toast;

document.addEventListener('DOMContentLoaded', () => {
    loadBooks();
    initializeToast();
    
    // Add enter key support for search
    document.getElementById('searchInput').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
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

function reserveBook(id) {
    fetch(`/api/books/${id}/reserve`, { method: 'POST' })
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
    fetch(`/api/books/${id}/return`, { method: 'POST' })
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
                    ${book.available ? 'Disponibile' : 'Non disponibile'}
                </div>
            </td>
            <td>
                ${book.available ?
                    `<button class="btn btn-success btn-action" onclick="reserveBook('${book.id}')">
                        <i class="fas fa-bookmark"></i>
                        Prenota
                    </button>` :
                    `<button class="btn btn-warning btn-action" onclick="returnBook('${book.id}')">
                        <i class="fas fa-undo"></i>
                        Restituisci
                    </button>`
                }
            </td>
        `;
        tableBody.appendChild(row);
    });
}
