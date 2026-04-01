package com.example;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        String strAnställda = "";
        
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
        long antalProg = anställda.stream().filter(a -> a.getTyp().equals("Programmerare")).count();
        long antalProjled = anställda.stream().filter(a -> a.getTyp().equals("Projektledare")).count();
        List<Personal> toppList = anställda.stream().sorted((a1, a2) -> a2.getLon() - a1.getLon()).limit(5).toList()
                .stream().sorted().toList(); // in alphabetiska ordning
        int summaToppList = toppList.stream().mapToInt(p -> p.getLon()).sum();

        for (Personal p : sortanställda) {
            IO.println("> " + p.getNamn());
        }
        IO.println("\nANTAL Programmerare: " + antalProg);
        IO.println("ANTAL Projektledare: " + antalProjled + "\n");

        for (Personal p : toppList) {
            IO.println("> " + p.getNamn() + " lön: " + p.getLon());
        }

        IO.println("Summa toppLista: " + summaToppList);

        /// ------ EXTRA UPPGIFTER ---------------

        double medellönAlla = anställda.stream().mapToInt(p -> p.getLon()).sum() / anställda.size();
        double medellönProg = anställda.stream().filter(a -> a.getTyp().equals("Programmerare"))
                .mapToInt(p -> p.getLon()).sum() / antalProg;
        double medellönProj = anställda.stream().filter(a -> a.getTyp().equals("Projektledare"))
                .mapToInt(p -> p.getLon()).sum() / antalProjled;

        IO.println("--------------------");
        IO.println("MEDELLÖN: " + medellönAlla);
        IO.println("MEDELLÖN FÖR Programmerare: " + medellönProg);
        IO.println("MEDELLÖN FÖR Projektledare: " + medellönProj);
        ArrayList<Programmerare> allaProg = new ArrayList<>();
        anställda.stream().filter(a -> a.getTyp().equals("Programmerare"))
                .forEach(p -> allaProg.add((Programmerare) p));
        Map<String, Long> sprakFreq = allaProg.stream()
                .collect(Collectors.groupingBy(p -> p.getProgramSprak(), Collectors.counting()));
        sprakFreq.forEach((sprak, antal) -> IO.println(sprak + ": " + antal));

        IO.println();

        Map<String, Long> lonAvdelning = anställda.stream()
                .collect(Collectors.groupingBy(Personal::getAvdelning, Collectors.summingLong(Personal::getLon)));
        List<Map.Entry<String, Long>> sortLonAvd = lonAvdelning.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()).toList();
        sortLonAvd.forEach(p -> IO.println(p.getKey() + ", TOTAL KOSTNAD: " + p.getValue()));

        ArrayList<Projektledare> allaProj = new ArrayList<>();
        anställda.stream().filter(a -> a.getTyp().equals("Projektledare"))
                .forEach(p -> allaProj.add((Projektledare) p));
        Optional<Projektledare> högstBelasProjektledare = allaProj.stream().sorted((a1, a2) -> a2.getAntalProjekt() - a1.getAntalProjekt()).limit(1).findFirst();
        högstBelasProjektledare.ifPresent(p -> IO.println(p.getNamn() + " ANTAL PROJEKT: " + p.getAntalProjekt()));
    }
}