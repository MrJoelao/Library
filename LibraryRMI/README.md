# Sistema Biblioteca con Java RMI e Web Interface

Questo progetto implementa un sistema di gestione biblioteca utilizzando Java RMI per il backend e un'interfaccia web realizzata con Spring Boot e HTML/CSS/JavaScript per il frontend.

## Architettura del Sistema

Il sistema è implementato con un'architettura a tre livelli:
1. **Frontend**: Interfaccia utente in HTML/CSS/JavaScript
2. **Web Client**: Applicazione Spring Boot che espone API REST
3. **RMI Server**: Server Java RMI che implementa la logica di business

### Componenti Principali

#### 1. Server RMI
- Implementa la logica di business principale
- Gestisce un database in-memory di libri italiani classici
- Offre funzionalità attraverso `LibraryService`:
  - Visualizzazione catalogo completo
  - Ricerca libri per titolo/autore
  - Prenotazione libri
  - Restituzione libri
  - Consultazione dettagli libro

#### 2. Web Client
- Funge da ponte tra frontend e server RMI
- Implementato con Spring Boot
- Espone REST API per tutte le operazioni
- Gestisce la comunicazione con il server RMI

#### 3. Modello Dati
La classe `Book` rappresenta i libri con:
- id: identificativo univoco
- title: titolo del libro
- author: autore
- available: stato di disponibilità

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

## API REST

L'interfaccia web espone le seguenti API REST:

- `GET /api/books`: Recupera tutti i libri
- `GET /api/books/{id}`: Recupera un libro specifico
- `GET /api/books/search?query=`: Cerca libri per titolo o autore
- `POST /api/books/{id}/reserve`: Prenota un libro
- `POST /api/books/{id}/return`: Restituisce un libro
