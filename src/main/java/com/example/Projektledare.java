package com.example;

public class Projektledare extends Personal{
    private int antalProjekt;

    public Projektledare(String namn, long personnummer, int lon, String avdelning, int antalProjekt) {
        super("Projektledare", namn, personnummer, lon, avdelning);
        this.antalProjekt = antalProjekt;
    }

    

    public Projektledare() {
    }



    public int getAntalProjekt() {
        return antalProjekt;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        
        return super.clone();
    }
  

  
}
