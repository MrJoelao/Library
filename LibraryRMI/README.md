# Sistema Biblioteca RMI

Un'applicazione distribuita Java per la gestione di una biblioteca, che utilizza Java RMI per la comunicazione client-server.

## Struttura del Progetto

- `model/` - Contiene le classi del modello dati
- `rmi/` - Contiene l'interfaccia e l'implementazione del servizio RMI
- `server/` - Contiene il server RMI
- `client/` - Contiene il client di test
- `resources/static/` - Contiene i file per l'interfaccia web (HTML, CSS, JS)

## Requisiti

- Java 11 o superiore
- Gradle

## Compilazione

```bash
./gradlew build
```

## Esecuzione

1. Avviare il server:
```bash
./gradlew runServer
```

2. In un altro terminale, avviare il client di test:
```bash
./gradlew runClient
```

## Funzionalità

- Visualizzazione lista libri
- Ricerca libri per titolo o autore
- Prenotazione libri
- Restituzione libri

## Struttura RMI

- Il server RMI viene avviato sulla porta 1099 (default)
- Il client si connette al server tramite l'interfaccia `LibraryService`
- La comunicazione è gestita tramite Java RMI

## Sviluppi Futuri

- Implementazione dell'interfaccia web
- Aggiunta del sistema di autenticazione
- Gestione delle categorie dei libri
- Sistema di notifiche per le prenotazioni
