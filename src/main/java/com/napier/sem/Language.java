package com.napier.sem;

public class Language {

    private String name;
    private long population;
    private String percentage;

    /**
     * Get the name of the language.
     * @return The name of the language.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the language.
     * @param name The name of the language.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the population for the language.
     * @return Population for the language.
     */
    public long getPopulation() {
        return population;
    }

    /**
     * Set the population for the language.
     * @param population The population
     * */
    public void setPopulation(long population) {
        this.population = population;
    }

    /**
     * Get the percentage of world population for the language.
     * @return The percentage.
     */
    public String getPercentage() {
        return percentage;
    }

    /**
     * Set the percentage of world population for the language.
     * @param percentage The percentage
     * */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
