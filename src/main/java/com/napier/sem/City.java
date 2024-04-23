package com.napier.sem;

/**
 * Represents a city with its name, country code, country name, district, and population.
 */
public class City {

    private String name;
    private String countryCode;
    private String country;
    private String district;
    private int population;

    /**
     * Get the name of the city.
     * @return The name of the city.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the city.
     * @param name The name of the city.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the country code of the country where the city is located.
     * @return The country code of the country.
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Set the country code of the country where the city is located.
     * @param countryCode The country code.
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Get the name of the country where the city is located.
     * @return The name of the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the name of the country where the city is located.
     * @param country The name of the country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get the district where the city is located.
     * @return The district or region.
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Set the district where the city is located.
     * @param district The district or region.
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Get the population of the city.
     * @return The population of the city.
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Set the population of the city.
     * @param population The population of the city.
     */
    public void setPopulation(int population) {
        this.population = population;
    }
}
