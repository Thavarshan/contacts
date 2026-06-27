package com.contacts;

import java.util.Objects;

/** Data model for a postal address. */
public class Address extends Model {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    /**
     * Returns the street address.
     *
     * @return the street
     */
    public String getStreet() {
        return this.street;
    }

    /**
     * Sets the street address.
     *
     * @param street the street to set
     * @return this address instance
     */
    public Address setStreet(String street) {
        this.street = street;
        return this;
    }

    /**
     * Returns the city.
     *
     * @return the city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Sets the city.
     *
     * @param city the city to set
     * @return this address instance
     */
    public Address setCity(String city) {
        this.city = city;
        return this;
    }

    /**
     * Returns the state or region.
     *
     * @return the state
     */
    public String getState() {
        return this.state;
    }

    /**
     * Sets the state or region.
     *
     * @param state the state to set
     * @return this address instance
     */
    public Address setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * Returns the postal code.
     *
     * @return the postal code
     */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * Sets the postal code.
     *
     * @param postalCode the postal code to set
     * @return this address instance
     */
    public Address setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    /**
     * Returns the country.
     *
     * @return the country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Sets the country.
     *
     * @param country the country to set
     * @return this address instance
     */
    public Address setCountry(String country) {
        this.country = country;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        Address other = (Address) o;
        return Objects.equals(street, other.street)
                && Objects.equals(city, other.city)
                && Objects.equals(state, other.state)
                && Objects.equals(postalCode, other.postalCode)
                && Objects.equals(country, other.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, postalCode, country);
    }

    @Override
    public String toString() {
        return "Address{"
                + "street='"
                + street
                + '\''
                + ", city='"
                + city
                + '\''
                + ", state='"
                + state
                + '\''
                + ", postalCode='"
                + postalCode
                + '\''
                + ", country='"
                + country
                + '\''
                + '}';
    }
}
