package com.example;

import java.util.Objects;

public abstract class Personal implements Comparable{
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

    @Override
    public String toString() {
        return namn + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Personal person = (Personal) o;
        return personnummer == person.personnummer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personnummer);
    }

    @Override
    public int compareTo(Object o) {
        Personal annan = (Personal)o;
        
        return namn.compareTo(annan.getNamn());
    }

}
