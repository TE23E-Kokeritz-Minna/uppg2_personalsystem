package com.example;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String strAnställda = "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type typ = new TypeToken<ArrayList<Personal>>() {
        }.getType();

        try {
            strAnställda = Files.readString(Paths.get("anställda.json"));
        } catch (Exception e) {
            IO.println("FEL med inlässningen: " + e.getMessage());
            return;
        }

        JsonArray jsonLista = JsonParser.parseString(strAnställda).getAsJsonArray();

        ArrayList<Personal> allaAnställda = new ArrayList<>();

        for (JsonElement element : jsonLista) {
            JsonObject obj = element.getAsJsonObject();

            String namn = obj.get("namn").getAsString();
            Long personnummer = obj.get("personnummer").getAsLong();
            int lon = obj.get("lon").getAsInt();
            String avdelning = obj.get("avdelning").getAsString();

            if (obj.get("typ").getAsString().equals("Programmerare")) {
                String progSpråk = obj.get("programSprak").getAsString();
                allaAnställda.add(new Programmerare(namn, personnummer, lon, avdelning, progSpråk));
            } else {
                int antalProjekt = obj.get("antalProjekt").getAsInt();
                allaAnställda.add(new Projektledare(namn, personnummer, lon, avdelning, antalProjekt));
            }
        }

        HashSet<Personal> anställda = new HashSet<>(allaAnställda);
        List<Personal> sortanställda = anställda.stream().sorted().toList();
        long antalProg = anställda.stream().filter(a -> a.getTyp().equals("Projektledare")).count();
        long antalProjled = anställda.stream().filter(a -> a.getTyp().equals("Programmerare")).count();
        List<Personal> toppList = anställda.stream().sorted((a1, a2) -> a2.getLon() - a1.getLon()).limit(5).toList()
                .stream().sorted().toList();            // in alphabetiska ordning

        int summaToppList = toppList.stream().mapToInt(p -> p.getLon()).sum();        
        for (Personal p : sortanställda) {
            IO.println("> " + p.getNamn() );
        }
        IO.println("\nANTAL Programmerare: " + antalProg);
        IO.println("ANTAL Projektledare: " + antalProjled + "\n");

        for (Personal p : toppList) {
            IO.println("> " + p.getNamn() + " lön: " + p.getLon());
        }

        IO.println("Summa toppLista: " + summaToppList);
    }
}