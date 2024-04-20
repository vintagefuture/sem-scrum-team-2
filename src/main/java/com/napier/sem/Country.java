package com.napier.sem;

/**
 * Represents a country with its code, name, continent, region, population, and capital city.
 */
public class Country {

    private String code;

    private String name;
    private String continent;
    private String region;

    private int population;

    private String capital;

    /**
     * Get the country code.
     * @return The country code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the country code.
     * @param code The country code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Get the name of the country.
     * @return The name of the country.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the country.
     * @param name The name of the country.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the continent where the country is located.
     * @return The continent.
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Set the continent where the country is located.
     * @param continent The continent.
     */
    public void setContinent(String continent) {
        this.continent = continent;
    }

    /**
     * Get the region where the country is located.
     * @return The region.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Set the region where the country is located.
     * @param region The region.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Get the population of the country.
     * @return The population of the country.
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Set the population of the country.
     * @param population The population of the country.
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Get the capital city of the country.
     * @return The capital city.
     */
    public String getCapital() {
        return capital;
    }

    /**
     * Set the capital city of the country.
     * @param capital The capital city.
     */
    public void setCapital(String capital) {
        this.capital = capital;
    }
}
