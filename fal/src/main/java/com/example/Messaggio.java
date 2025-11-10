package com.example;

public class Messaggio {

    private String testo;
    private String autore;

    public Messaggio(String testo, String autore) {

        this.testo = testo;
        this.autore = autore;

    }

    public Messaggio(String testo) {

        this.testo = testo;
        this.autore = "unknown";

    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

}
