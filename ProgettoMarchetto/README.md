ProgettoMarchetto
============================
Progetto per l'esame di programmazione web sviluppato in base alle specifiche.

## Mappa del progetto
Parte back-end (src/main/java/it/units):
- api: contiene i web services esposti
- assistant: contiene classi di supporto che assistono in varie operazioni ma che non fanno direttamente alcuna operazione
- entities:
    - proxies, entità che servono al filtraggio delle informazioni che poi verranno passate al front-end
    - storage, entità che vengono effettivamente salvate nel database
    - support, entità di supporto per la ricezione dei dati o altre operazioni interne
- filters: contiene le classi che filtrano le richieste
- listners: contiene le classi che servono all'inizializzazione di ogni istanza
- persistance: contiene le classi che accedono direttamente alle informazioni del database
- utils: contiene altre classi che possono essere utili per varie operazioni

Parte del front-end (src/main/webapp)
- WEB-INF: contiene i file a cui nessuno esterno al progettista può avere accesso
- il resto viene dalla compilazione del progetto di Vue

pom.xml: contiene le dipendenze necessarie per il progetto

Default_readme: contiene delle informazioni legate alla creazione del progetto

## Descrizione back-end

### api
I vari web service qui contenuti contengono ciascuno anche una breve documentazione, inoltre si può ottenere la documentazione anche con swagger poiché è stato aggiunto anch'esso nel progetto.

- AttoriManager.java, accessibile al path "/attori", espone 4 web services:
    - /reigstration, serve per la registrazione di un utente tramite una POST.
    - /modInfo, permetta la modifica, se si hanno i privilegi, di un utente tramite una POST. Se non vengono fatte effettive modifiche allora viene ritornato un messaggio di warning.
    - /delete/{username}, serve ad eliminare l'utente passato nell'URI tramite una DELETE. Se viene eliminato un consumer allora il contenuto dei suoi files viene eliminato. Se viene eliminato un uploader allora tutti i files ad esso collegati vengono eliminati completamente dal database.

- FilesManager.java, accessibile al path "/files", espone 4 web services:
    - /download/{id}, serve per il download di un file dal sito tramite una GET. Dopo aver controllato il token si affida al metodo downloadFile.
    - /downloadDirect/{fileId}/{tokenDownload}, serve per il download diretto tramite il link che viene mandato per mail al consumer. Viene eseguito tramite una GET. Dopo il controllo del token inserito nell'URI si affida al metodo downloadFile
    - downloadFile, metodo che viene utilizzato per fare il download del file. Se è il primo download allora salva anche l'indirizzo IP e la data del download. Siccome *deve* essere utilizzato solo in questa classe si è deciso di lasciarlo al suo interno.
    - /upload, serve (ad un Uploader) per caricare il file tramite una POST.
    - /delete/{fileId}, serve (ad un Uploader) per eliminare un certo file tramite una DELETE.

- ListManager.java, accessibile al path "/list", espone 6 web services:
    - /uploaders, serve ad ottenere tutti gli uploader che hanno caricato files per il consumer che richiede il servizio tramite GET.
    - /filesConsumer, serve ad ottenere tutti i file appartenenti al consumer che richiede il servizio tramite GET.
    - /consumers, serve ad ottenere tutti i consumer all'uploader che richiede il servizio tramite GET.
    - /filesUploader, serve ad ottenere tutti i files caricati dall'uploader che richiede il servizio tramite GET.
    - /resumeForAdmin, serve ad ottenere il resoconto dei file caricati da ogni uploader nel periodo specificato nella richiesta dell'amministratore che richiede il servizio tramite POST.
    - /administrators, serve ad ottenere la lista di tutti gli amministratori per l'amministratore che richiede il servizio tramite GET.

- LoginManager.java, accessibile al path "/login", espone il web service per i login.

### assistant
- FilterAssistant: contiene il metodo per filtrare le richieste in base al token jwt e, se indicato, in base ad un ruolo. Viene utilizzato in tutti i filtri di questo genere.
- JWTAssistant: gestisce la creazione, la decodifica (e il recupero di informazioni) e la verifica dei token jwt. Viene utilizzato ovunque si interagisca con il token jwt.
- ListToProxiesAssistant: gestisce il filtraggio delle informazioni dalle entità storage a quelle proxies. In particolare si occupa delle liste (ordinate).
- MailAssistant: gestisce l'invio delle mail. Ci sono due configurazioni per due tipologie di e-mail: per la notifica e per la creazione di un utente.
- PasswordAssistant: gestiscono la crittografia delle password con il metodo dell'hash&salt. Si può cambiare la lunghezza e l'algoritmo a piacere.
- TokenDownloadAssistant: gestiscono la creazione e la verifica del token per il download diretto. Esso consiste in un jwt con un segreto diverso dagli altri jwt e con un diverso soggetto.

### entities
Tutte le classi hanno i rispettivi getter e setter e almeno il costruttore vuoto. Per una descrizione delle caratteristiche delle entità vedi le specifiche del progetto.

#### proxies
Contiene le classi utili per il filtraggio delle informazioni prima di passarle al client.
- AttoreInfo: proxies per le entità di tipo Attore. Il costruttore prende in input un Attore. Ha come attributi sotto forma di stringhe: username, name, email e logo.
- FilesInfo: proxies per le entità di tipo Files. Il costruttore prende in input un Files. Ha come attributi sotto forma di stringhe: id, usernameUpl (username dell'uploader), usernameCons (username del consumer), name (del file), dataCaricamento, dataVisualizzazione, indirizzoIP (del primo download), hashtag.
#### storage
Contiene le entità così come vengono effettivamente salvate nel database, quindi sono marcate come @Entity.
- Attore: come chiave primaria utilizza l'username. Ha inoltre le stringhe: password (hash della password), salt, name, email, role (indicizzato), logo. Il costruttore prende in input tutti i valori tranne il salt che verrà impostato manualmente.
- Files: come chiave primaria utilizza una stringa id univoca generata tramite una libreria. Ci sono due parametri in più rispetto alle specifiche: usernameUpl e usernameCons, entrambi stringhe indicizzate. Il file vero e proprio è salvato come array di bit. Poi sono salvati come stringhe: name, dataCaricamento, dataVisualizzazione, indirizzoIP, hashtag.
#### support
- FromTo: serve per la gestione delle date per la redazione del resoconto. Prende due stringhe come parametri: from e to.
- ResumeFromAdmin: serve per la creazione del resoconto. Prende in ingresso tre stringhe: username, name e email (dell'uploader); e due interi: numDocCaricati, numConsDiversi.
- SupportFileUpload: serve per la gestione dell'upload del file, che viene trasmesso come stringa in base64. Prende in input solo stringhe: file, nameFile, hashtag, dataCaricamento, usernameUpl, usernameCons, nameCons, emailCons.

### filters
Contiene i filtri dell'applicazione. Tranne il CORSFilter tutti sfruttano il FilterAssistant. Sono tutti annotati con @WebFilter, ma per gestire la gerarchia di esecuzione si è dovuto ricorrere alla mappatura tramite web.xml.

- AdministratorFilter: controlla se si ha un token jwt valido e si appartiene alla categoria degli administrator, altrimenti risponde con il codice 403-Forbidden. Filtraggio valido per le liste degli amministratori e per il resoconto.
- AuthenticationFilter: controlla se si ha un token jwt valido.
- ConsumerFilter: controlla se si ha un token jwt valido e si appartiene alla categoria dei consumer, altrimenti risponde con il codice 403-Forbidden. Filtraggio valido per la lista degli uploader che hanno caricato file per il consumer, per la lista dei file del consumer e per il download dei file.
- CORSFilter: serve per la Cross-Origin Policy.
- UploaderFilter: controlla se si ha un token jwt valido e si appartiene alla categoria degli uploader, altrimenti risponde con il codice 403-Forbidden. Filtraggio valido per la lista dei consumer, la lista dei file caricati dall'uploader, per l'upload e per l'eliminazione dei file.

### listners
- DatiDefault: serve per la creazione di un administrator predefinito.
- Objectifystarter: serve ad inizializzare Objectify nelle nuove istanze.

### persistance
Contiene le classi che si relazionano direttamente con il database.
- AbstractHelper: è una classe astratta per salvare ed eliminare delle entità, oppure per trovare una particolare entità attraverso la sua chiave primaria e alla tipologia di entità.
- AttoreHelper: estensione dell'AbstractHelper che implementa il salvataggio di un attore cambiando la password (quindi che sia nuovo o si voglia solo modificare la password), quindi eseguendo l'hash e salvando il sale. Implementa anche la ricerca nel database degli attori per ruolo.
- FilesHelper: estensione dell'AbstractHelper che implementa la ricerca dei file nel database, in base ai diversi parametri indicizzati.

### utils
- FixedVariables: contiene delle variabili fisse che servono trasversalmente in tutto il progetto.
- MyException: estende le eccezioni. In questo momento è utile soltanto per differenziare le eccezioni di questo progetto.
- SortByDataCaricamento: implementa un comparatore per i FilesInfo ed è utile per l'ordinamento dei files.
- UtilsMiscellaneous: contiene vari metodi utili.

### web.xml
Oltre alla mappatura dei filtri e dei listner già descritta nei rispettivi sottocapitoli, c'è la mappatura dell'ObjectifyFilter, necessaria per la corretta esecuzione di Objectify. Questo filtro passa a controllo tutti gli url-pattern.   
Il welcome-file è stato impostato su index.html. Infine contiene la configurazione della servlet per il servizio REST di Jersey.

## Proposte finali per miglioramenti
Varie proposte finali per migliorare alcuni aspetti, ma che richiederebbero troppo tempo e si sono reputate superflue per l'esame:
- Astrazione dell'entità dell'attore così da eliminare il campo stringa con il ruolo. Era stato fatto nel primo approccio al progetto ma poi per scelte seguenti si era scelto per l'approccio corrente.
- Aggiunta di Firebase per il login e la gestione password.L'autenticazione verrebbe fatta su Firebase e nel datastore verrebbero salvate solo le altre informazioni per ogni utente. Il vincolo del login con username e password potrebbe essere sorpassato obbligando ogni utente ad avere anche una mail univoca.
Ci sarebbe anche la possibilità di aggiungerlo direttamente nel client.
- Mettere una scadenza ai token mandati via mail. Non si è messa per una supposizione sulle specifiche del progetto.
- Creare la parte di test.