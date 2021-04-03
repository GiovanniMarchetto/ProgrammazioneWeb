package it.units.entities.proxies;

import it.units.entities.storage.Files;

public class FilesInfo {

    private String id;

    private String usernameUpl;

    private String usernameCons;

    private String name;

    private String dataCaricamento;

    private String dataVisualizzazione;

    private String indirizzoIP;

    private String hashtag;

    public FilesInfo() {
    }

    public FilesInfo(Files originale) {
        this.id = originale.getId();
        this.usernameUpl = originale.getUsernameUpl();
        this.usernameCons = originale.getUsernameCons();
        this.name = originale.getName();
        this.dataCaricamento = originale.getDataCaricamento();
        this.dataVisualizzazione = originale.getDataVisualizzazione();
        this.indirizzoIP = originale.getIndirizzoIP();
        this.hashtag = originale.getHashtag();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsernameUpl() {
        return usernameUpl;
    }

    public void setUsernameUpl(String usernameUpl) {
        this.usernameUpl = usernameUpl;
    }

    public String getUsernameCons() {
        return usernameCons;
    }

    public void setUsernameCons(String usernameCons) {
        this.usernameCons = usernameCons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataCaricamento() {
        return dataCaricamento;
    }

    public void setDataCaricamento(String dataCaricamento) {
        this.dataCaricamento = dataCaricamento;
    }

    public String getDataVisualizzazione() {
        return dataVisualizzazione;
    }

    public void setDataVisualizzazione(String dataVisualizzazione) {
        this.dataVisualizzazione = dataVisualizzazione;
    }

    public String getIndirizzoIP() {
        return indirizzoIP;
    }

    public void setIndirizzoIP(String indirizzoIP) {
        this.indirizzoIP = indirizzoIP;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}

