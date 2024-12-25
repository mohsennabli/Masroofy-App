package com.example.masroofy;

public class Depense {
    private int id;
    private double montant;
    private String categorie;
    private String description;
    private String date;

    public Depense(int id, double montant, String categorie, String description, String date) {
        this.id = id;
        this.montant = montant;
        this.categorie = categorie;
        this.description = description;
        this.date = date;
    }

    public int getId() { return id; }
    public double getMontant() { return montant; }
    public String getCategorie() { return categorie; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
}
