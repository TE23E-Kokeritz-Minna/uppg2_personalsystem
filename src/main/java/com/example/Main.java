package com.example;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.IOException;
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

        // Sträng för json
        String strAnställda = "";

        // Läs in asntällda
        try {
            strAnställda = Files.readString(Paths.get("anställda.json"));
        } catch (FileNotFoundException e) {
            IO.println("FEL: filen hittades inte: " + e.getMessage());
            return;
        } catch (IOException e) {
            IO.println("FEL med filen: " + e.getMessage());
            return;
        }

        // Läser in till en Json Array
        JsonArray jsonLista = JsonParser.parseString(strAnställda).getAsJsonArray();

        // Lista med alla anställda
        ArrayList<Personal> allaAnställda = new ArrayList<>();
        // För kortare stream utryck
        ArrayList<Programmerare> allaProg = new ArrayList<>();
        ArrayList<Projektledare> allaProj = new ArrayList<>();

        // Lägger till korrekt objekt från JSON filen i allaanstälda listan
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

        // Gör om till en HashSet för att ta bort dubletter
        HashSet<Personal> anställda = new HashSet<>(allaAnställda);

        // Lägger till alla prog och proj i rätt lista
        anställda.stream().filter(a -> a.getTyp().equals("Programmerare"))
                .forEach(p -> allaProg.add((Programmerare) p));
        anställda.stream().filter(a -> a.getTyp().equals("Projektledare"))
                .forEach(p -> allaProj.add((Projektledare) p));

        // -------------------- DATA ANALYS ------------------------ //

        // Sorterar listan
        List<Personal> sortanställda = anställda.stream().sorted().toList();

        // räknar antal Prog och Proj
        long antalProg = allaProg.stream().count();
        long antalProjled = allaProj.stream().count();

        // Filterar de fem som får mest lön
        List<Personal> toppList = anställda.stream().sorted((a1, a2) -> a2.getLon() - a1.getLon()).limit(5).toList()
                .stream().sorted().toList(); // in alphabetiska ordning
        // Tar deras summa på lön
        int summaToppList = toppList.stream().mapToInt(p -> p.getLon()).sum();

        // Skriver ut alla anställda
        for (Personal p : sortanställda) {
            IO.println("> " + p.getNamn());
        }
        // Skriver ut antal
        IO.println("\nANTAL Programmerare: " + antalProg);
        IO.println("ANTAL Projektledare: " + antalProjled + "\n");

        // Skriver ut topplistan
        for (Personal p : toppList) {
            IO.println("> " + p.getNamn() + " lön: " + p.getLon());
        }
        IO.println("Summa toppLista: " + summaToppList);

        /// ------ EXTRA UPPGIFTER ---------------

        // Räknar medel;n 
        double medellönAlla = anställda.stream().mapToInt(p -> p.getLon()).sum() / anställda.size();
        double medellönProg = allaProg.stream().mapToInt(p -> p.getLon()).sum() / antalProg;
        double medellönProj = allaProj.stream().mapToInt(p -> p.getLon()).sum() / antalProjled;

        IO.println("--------------------");
        IO.println("MEDELLÖN: " + medellönAlla);
        IO.println("MEDELLÖN FÖR Programmerare: " + medellönProg);
        IO.println("MEDELLÖN FÖR Projektledare: " + medellönProj);

        Map<String, Long> sprakFreq = allaProg.stream()
                .collect(Collectors.groupingBy(p -> p.getProgramSprak(), Collectors.counting()));
        sprakFreq.forEach((sprak, antal) -> IO.println(sprak + ": " + antal));

        IO.println();

        Map<String, Long> lonAvdelning = anställda.stream()
                .collect(Collectors.groupingBy(Personal::getAvdelning, Collectors.summingLong(Personal::getLon)));
        List<Map.Entry<String, Long>> sortLonAvd = lonAvdelning.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()).toList();
        sortLonAvd.forEach(p -> IO.println(p.getKey() + ", TOTAL KOSTNAD: " + p.getValue()));

        Optional<Projektledare> högstBelasProjektledare = allaProj.stream()
                .sorted((a1, a2) -> a2.getAntalProjekt() - a1.getAntalProjekt()).limit(1).findFirst();
        högstBelasProjektledare.ifPresent(p -> IO.println(p.getNamn() + " ANTAL PROJEKT: " + p.getAntalProjekt()));
    }
}