package com.example;

public class Programmerare extends Personal{
    private String programSprak;

    public Programmerare(String namn, long personnummer, int lon, String avdelning, String programSprak) {
        super("Programmerare", namn, personnummer, lon, avdelning);
        this.programSprak = programSprak;
    }

    public String getProgramSprak() {
        return programSprak;
    }
    
    
}
