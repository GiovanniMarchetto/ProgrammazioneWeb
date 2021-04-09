package it.units.entities.storage;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Base64;
import java.util.UUID;

@Entity
public class Files {
    @Id
    private String id;

    @Index
    private String usernameUpl;

    @Index
    private String usernameCons;

    private byte[] file;

    private String name;

    private String dataCaricamento;

    private String dataVisualizzazione;

    private String indirizzoIP;

    private String hashtag;

    public Files() {
    }

    public Files(String usernameUpl, String usernameCons, String file, String name, String dataCaricamento, String hashtag) {
        this.id = UUID.randomUUID().toString();
        this.usernameUpl = usernameUpl;
        this.usernameCons = usernameCons;
        this.file = Base64.getDecoder().decode(file);
        this.name = name;
        this.dataCaricamento = dataCaricamento;
        this.hashtag = hashtag;
        this.dataVisualizzazione = "";
        this.indirizzoIP = "";
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

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
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
