document.addEventListener('DOMContentLoaded', () => {
    checkServerStatus();
    setInterval(checkServerStatus, 5000);

    document.getElementById('listBooks').addEventListener('click', listBooks);
    document.getElementById('addBook').addEventListener('click', showAddBookForm);
});

function checkServerStatus() {
    fetch('http://localhost:8081/api/server/status')
        .then(response => response.json())
        .then(data => {
            const statusElement = document.getElementById('serverStatus');
            statusElement.textContent = data.status;
            statusElement.className = data.status === 'online' ? 'text-success' : 'text-danger';
        })
        .catch(() => {
            const statusElement = document.getElementById('serverStatus');
            statusElement.textContent = 'offline';
            statusElement.className = 'text-danger';
        });
}

function listBooks() {
    const resultDiv = document.getElementById('result');
    resultDiv.innerHTML = '<div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div>';

    fetch('http://localhost:8081/api/books')
        .then(response => response.json())
        .then(books => {
            let html = '<div class="list-group">';
            books.forEach(book => {
                html += `
                    <div class="list-group-item">
                        <h5 class="mb-1">${book.title}</h5>
                        <p class="mb-1">Author: ${book.author}</p>
                        <small>Status: ${book.available ? 'Available' : 'Checked Out'}</small>
                    </div>`;
            });
            html += '</div>';
            resultDiv.innerHTML = html;
        })
        .catch(error => {
            resultDiv.innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
        });
}

function showAddBookForm() {
    const resultDiv = document.getElementById('result');
    resultDiv.innerHTML = `
        <div class="card">
            <div class="card-header">Add New Book</div>
            <div class="card-body">
                <form id="addBookForm">
                    <div class="mb-3">
                        <label for="title" class="form-label">Title</label>
                        <input type="text" class="form-control" id="title" required>
                    </div>
                    <div class="mb-3">
                        <label for="author" class="form-label">Author</label>
                        <input type="text" class="form-control" id="author" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Add Book</button>
                </form>
            </div>
        </div>`;

    document.getElementById('addBookForm').addEventListener('submit', submitNewBook);
}

function submitNewBook(event) {
    event.preventDefault();
    const title = document.getElementById('title').value;
    const author = document.getElementById('author').value;

    fetch('http://localhost:8081/api/books', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            title: title,
            author: author,
            available: true
        })
    })
    .then(response => response.json())
    .then(data => {
        const resultDiv = document.getElementById('result');
        resultDiv.innerHTML = '<div class="alert alert-success">Book added successfully!</div>';
        setTimeout(listBooks, 1500);
    })
    .catch(error => {
        const resultDiv = document.getElementById('result');
        resultDiv.innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
    });
}
