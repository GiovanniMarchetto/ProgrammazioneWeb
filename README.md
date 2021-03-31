# ProgrammazioneWeb
Progetto per l'esame di programmazione web.

## Introduzione alle specifiche di progetto
Realizzare una piattaforma web con la quale poter distribuire documenti ai propri clienti finali, tenendo traccia della loro lettura (che corrisponde al download del file).

I documenti potranno essere caricati tramite un’interfaccia web, oppure tramite un web service (SOAP o REST, a vostra scelta). 

Dovrete realizzare anche un client (essenziale) che dimostri il funzionamento del vostro web service.

Gli elementi marcati con “EXTRA:” servono a guadagnare punti in più (es. per la lode), ma non sono strettamente necessari.


## Entità
Vi sono due tipi di entità, i Files e gli attori; gli attori si divideranno in:

● Administrators: i gestori della piattaforma

● Uploaders: sono gli utenti del sistema che possono caricare e distribuire files

● Consumers: gli utenti finali del sistema, destinatari dei files. Lo stesso Consumer potrà essere gestito da più Uploaders (es: un Uploader è la mia assicurazione, l’altro la mia banca).

L’autenticazione avverrà con username-password (EXTRA: se volete potete usare Firebase, ma non è strettamente necessario).

### Administrators
All’avvio della piattaforma per la prima volta, vi sarà un solo utente amministratore.
Ogni Administrator potrà:

● modificare la propria password

● EXTRA: modificare le proprie informazioni (eccetto lo username)

● EXTRA: creare/modificare/eliminare altri amministratori (eccetto lo username)

● creare/modificare/eliminare Uploaders

● visualizzare un resoconto in cui per ogni Uploader sia elencato:

--- il numero di documenti caricati

--- il numero di consumers diversi cui tali documenti afferiscono il resoconto mostrerà di default i dati del mese precedente, ma il periodo di analisi potrà essere modificato usando due campi data (da...a).

#### Campi previsti
● Username (indirizzo email)

● Nome/cognome (singolo campo)

● e-mail

### Uploaders
Ogni Uploader potrà:

● modificare la propria password

● Modificare le proprie informazioni (eccetto lo username)

● creare/modificare/eliminare Consumers

● caricare/eliminare files per un Consumer

#### Campi previsti
● Username (quattro caratteri alfanumerici)

● Nome/cognome (singolo campo)

● E-mail

● Immagine del logo

### Consumers
Ogni consumer potrà:

● scaricare i propri files

● Cambiare le proprie informazioni (eccetto lo username)

#### Campi previsti
● Username (corrisponde al codice fiscale)

● Nome/cognome (singolo campo)

● E-mail

### Files
Ogni file è stato caricato da un Uploader e indirizzato ad un Consumer.

#### Campi previsti

● Nome del file

● Data/ora di caricamento

● Data/ora di visualizzazione

● Indirizzo IP usato per visualizzare

● Una stringa contenente una lista di hashtags (non definiti a priori)


## Funzionamento piattaforma

### Schermata Principale
La schermata principale mostra al centro la richiesta di username/password per l’autenticazione, oppure un link per la registrazione al sistema (usabile solo dai Consumers).

### Schermata Principale - Consumers
Nel caso in cui il Consumer abbia ricevuto documenti da più uploaders, mostra la lista degli Uploaders che gli hanno inviato documenti (logo + descrizione); cliccando su uno di essi, appare la lista dei documenti caricati da questi.

Nel caso in cui il Consumer abbia ricevuto documenti da un solo Uploader, mostra direttamente la lista dei documenti caricati da questi (in sintesi, non si mostra la schermata di scelta Uploader).

### Lista Documenti - Consumers
In alto a sinistra vi è il logo dell’Uploader, come se il sistema fosse suo.

Vanno inoltre mostrati i documenti sotto forma di tabella, ordinati dal più recente al meno recente, avendo comunque in cima sempre quelli non ancora letti); la tabella deve mostrare il nome del documento, la data di caricamento e l’eventuale data di lettura da parte del Consumer.

Questa maschera mostra anche la lista degli hashtag collegati ai documenti presenti, e l’utente può filtrarli selezionando l’hash tag corrispondente.

### Schermata Principale - Uploaders
L’Uploader visualizzerà una lista di Consumers, con la possibilità di crearne uno nuovo o di eliminarne uno esistente.

Cliccando su un Consumer si vedranno i files caricati, inclusi i tag-data di visualizzazione-indirizzo IP visualizzazione.

L’uploader deve quindi poter caricare un nuovo file (specificandone nome e lista di hashtag), oppure eliminarne uno già presente; ogni volta che si carica un file, il Consumer riceve una Notifica (v. paragrafo dedicato).

### Schermata Principale - Administrators
Libera, a vostra scelta.


## Notifica
Ogni qual volta il sistema riceve un nuovo file, provvederà ad inviare al Consumer una mail di notifica contenente:

● il nome dell’Uploader che l’ha inviato,

● il nome del file,

● un link alla home page del sistema,

● un link per scaricare il file direttamente.

Attenzione: il sistema deve registrare il download del file anche quando questi avviene dall’email di conferma.


## Web Service
Il sistema deve esporre un web-service che permetta di inviare un file ad un Consumer.

Il metodo dovrà ricevere in input:

● Codice Fiscale del Consumer

● E-mail del Consumer

● Nome/Cognome del Consumer

● Nome del File

● HashTag

● Il file da caricare

Se il Consumer esiste già, si limiterà a caricare il file ed inviargli una Notifica (v. paragrafo dedicato); altrimenti, come prima cosa ne creerà uno nuovo.
