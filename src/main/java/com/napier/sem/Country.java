package com.napier.sem;

public class Country {
    private String name;
    private int population;
    private int urbanPopulation;
    private int urbanPopulationPerc;
    private int ruralPopulation;
    private int ruralPopulationPerc;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getUrbanPopulation() {
        return urbanPopulation;
    }

    public void setUrbanPopulation(int urbanPopulation) {
        this.urbanPopulation = urbanPopulation;
    }

    public int getUrbanPopulationPerc() {
        return urbanPopulationPerc;
    }

    public void setUrbanPopulationPerc(int urbanPopulationPerc) {
        this.urbanPopulationPerc = urbanPopulationPerc;
    }

    public int getRuralPopulation() {
        return ruralPopulation;
    }

    public void setRuralPopulation(int ruralPopulation) {
        this.ruralPopulation = ruralPopulation;
    }

    public int getRuralPopulationPerc() {
        return ruralPopulationPerc;
    }

    public void setRuralPopulationPerc(int ruralPopulationPerc) {
        this.ruralPopulationPerc = ruralPopulationPerc;
    }
}
