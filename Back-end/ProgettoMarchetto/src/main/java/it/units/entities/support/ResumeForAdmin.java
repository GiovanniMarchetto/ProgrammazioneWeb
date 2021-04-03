package it.units.entities.support;


public class ResumeForAdmin {

    private String username;

    private String name;

    private String email;

    private int numDocCaricati;

    private int numConsDiversi;

    public ResumeForAdmin() {
    }

    public ResumeForAdmin(String uploader, String nameUploader, String emailUploader, int numDocCaricati, int numConsDiversi) {
        this.username = uploader;
        this.name = nameUploader;
        this.email = emailUploader;
        this.numDocCaricati = numDocCaricati;
        this.numConsDiversi = numConsDiversi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumDocCaricati() {
        return numDocCaricati;
    }

    public void setNumDocCaricati(int numDocCaricati) {
        this.numDocCaricati = numDocCaricati;
    }

    public int getNumConsDiversi() {
        return numConsDiversi;
    }

    public void setNumConsDiversi(int numConsDiversi) {
        this.numConsDiversi = numConsDiversi;
    }
}