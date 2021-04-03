package it.units.entities.support;

import it.units.utils.UtilsMiscellaneous;

public class SupportFileUpload {

    private String file;

    private String nameFile;

    private String hashtag;

    private String dataCaricamento;

    private String usernameUpl;

    private String usernameCons;

    private String nameCons;

    private String emailCons;

    public SupportFileUpload() {
    }

    public SupportFileUpload(String file, String nameFile, String hashtag, String usernameCons, String nameCons, String emailCons) {
        this.file = file;
        this.nameFile = nameFile;
        this.hashtag = hashtag;
        this.dataCaricamento = UtilsMiscellaneous.getDataString();
        this.usernameUpl = "";
        this.usernameCons = usernameCons;
        this.nameCons = nameCons;
        this.emailCons = emailCons;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getDataCaricamento() {
        return dataCaricamento;
    }

    public void setDataCaricamento(String dataCaricamento) {
        this.dataCaricamento = dataCaricamento;
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

    public String getNameCons() {
        return nameCons;
    }

    public void setNameCons(String nameCons) {
        this.nameCons = nameCons;
    }

    public String getEmailCons() {
        return emailCons;
    }

    public void setEmailCons(String emailCons) {
        this.emailCons = emailCons;
    }
}
