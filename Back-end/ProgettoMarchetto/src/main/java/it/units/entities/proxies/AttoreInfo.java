package it.units.entities.proxies;

import it.units.entities.storage.Attore;

public class AttoreInfo {

    private String username;

    private String name;

    private String email;

    private String logo;

    public AttoreInfo() {
    }

    public AttoreInfo(Attore attore) {
        this.username = attore.getUsername();
        this.name = attore.getName();
        this.email = attore.getEmail();
        this.logo = attore.getLogo();
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
