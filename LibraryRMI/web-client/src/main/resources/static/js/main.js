// Carica tutti i libri all'avvio
document.addEventListener('DOMContentLoaded', loadBooks);

function loadBooks() {
    fetch('/api/books')
        .then(response => response.json())
        .then(books => displayBooks(books))
        .catch(error => console.error('Errore nel caricamento dei libri:', error));
}

function searchBooks() {
    const query = document.getElementById('searchInput').value;
    fetch(`/api/books/search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(books => displayBooks(books))
        .catch(error => console.error('Errore nella ricerca:', error));
}

function reserveBook(id) {
    fetch(`/api/books/${id}/reserve`, { method: 'POST' })
        .then(response => {
            if (response.ok) {
                loadBooks();
            } else {
                alert('Impossibile prenotare il libro');
            }
        })
        .catch(error => console.error('Errore nella prenotazione:', error));
}

function returnBook(id) {
    fetch(`/api/books/${id}/return`, { method: 'POST' })
        .then(response => {
            if (response.ok) {
                loadBooks();
            } else {
                alert('Impossibile restituire il libro');
            }
        })
        .catch(error => console.error('Errore nella restituzione:', error));
}

function displayBooks(books) {
    const tableBody = document.getElementById('booksTableBody');
    tableBody.innerHTML = '';
    
    books.forEach(book => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td class="${book.available ? 'status-available' : 'status-unavailable'}">
                ${book.available ? 'Disponibile' : 'Non disponibile'}
            </td>
            <td>
                ${book.available ?
                    `<button class="btn btn-sm btn-success btn-action" onclick="reserveBook('${book.id}')">
                        Prenota
                    </button>` :
                    `<button class="btn btn-sm btn-warning btn-action" onclick="returnBook('${book.id}')">
                        Restituisci
                    </button>`
                }
            </td>
        `;
        tableBody.appendChild(row);
    });
}
