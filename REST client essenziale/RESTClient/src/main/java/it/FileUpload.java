package it;

/**
 *
 * @author Giovanni Marchetto
 */
public class FileUpload {

    private final String file;

    private final String nameFile;

    private final String hashtag;

    private final String usernameCons;

    private final String nameCons;

    private final String emailCons;

    /**
     * Costruttore base con tutti i parametri:
     *
     * @param file il file in base 64
     * @param nameFile il nome del file
     * @param hashtag una string di hashtag riguardante il file
     * @param usernameCons username del consumer a cui è rivolto
     * @param nameCons nome del consumer
     * @param emailCons email del consumer
     */
    public FileUpload(String file, String nameFile, String hashtag, String usernameCons, String nameCons, String emailCons) {
        this.file = file;
        this.nameFile = nameFile;
        this.hashtag = hashtag;
        this.usernameCons = usernameCons;
        this.nameCons = nameCons;
        this.emailCons = emailCons;
    }

    /**
     *
     * @return the file in base 64
     */
    public String getFile() {
        return file;
    }

    /**
     *
     * @return name of the file
     */
    public String getNameFile() {
        return nameFile;
    }

    /**
     *
     * @return hashtags of the file
     */
    public String getHashtag() {
        return hashtag;
    }

    /**
     *
     * @return username of consumer
     */
    public String getUsernameCons() {
        return usernameCons;
    }

    /**
     *
     * @return name of consumer
     */
    public String getNameCons() {
        return nameCons;
    }

    /**
     *
     * @return email of the consumer
     */
    public String getEmailCons() {
        return emailCons;
    }
}
