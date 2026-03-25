package com.example;

public abstract class Personal {
    protected String typ;
    protected String namn;
    protected long personnummer;
    protected int lon;
    protected String avdelning;

    public Personal(String typ, String namn, long personnummer, int lon, String avdelning) {
        this.typ = typ;
        this.namn = namn;
        this.personnummer = personnummer;
        this.lon = lon;
        this.avdelning = avdelning;
    }

    public String getTyp() {
        return typ;
    }

    public String getNamn() {
        return namn;
    }

    public long getPersonnummer() {
        return personnummer;
    }

    public int getLon() {
        return lon;
    }

    public String getAvdelning() {
        return avdelning;
    }

}
