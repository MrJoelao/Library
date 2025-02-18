# Sistema Biblioteca con Java RMI e Web Interface

Questo progetto implementa un sistema di gestione biblioteca utilizzando Java RMI per il backend e un'interfaccia web realizzata con Spring Boot e HTML/CSS/JavaScript per il frontend.

## Struttura del Progetto

Il progetto è diviso in due moduli principali:

- `rmi-server`: Contiene l'implementazione del server RMI che gestisce la logica della biblioteca
- `web-client`: Contiene l'interfaccia web che si connette al server RMI tramite REST API

## Requisiti

- Java 11 o superiore
- Gradle

## Come Eseguire

1. Avviare il Server RMI:
   ```bash
   cd LibraryRMI/rmi-server
   ./gradlew runServer
   ```

2. Avviare l'Interfaccia Web:
   ```bash
   cd LibraryRMI/web-client
   ./gradlew bootRun
   ```

3. Aprire il browser e navigare a:
   ```
   http://localhost:8080
   ```

## Funzionalità

- Visualizzazione di tutti i libri disponibili
- Ricerca libri per titolo o autore
- Prenotazione di un libro
- Restituzione di un libro

## API REST

L'interfaccia web comunica con il server RMI attraverso le seguenti API REST:

- `GET /api/books`: Recupera tutti i libri
- `GET /api/books/{id}`: Recupera un libro specifico
- `GET /api/books/search?query=`: Cerca libri per titolo o autore
- `POST /api/books/{id}/reserve`: Prenota un libro
- `POST /api/books/{id}/return`: Restituisce un libro

## Note di Sicurezza

Il file `security.policy` è configurato per consentire le operazioni RMI necessarie. In un ambiente di produzione, configurare le policy di sicurezza in modo appropriato.
