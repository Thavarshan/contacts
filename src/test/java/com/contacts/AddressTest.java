package com.contacts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Unit tests for the {@link Address} domain model. */
class AddressTest {

    @Test
    void canSetStreet() {
        Address address = new Address();

        address.setStreet("1 Main St");

        assertEquals("1 Main St", address.getStreet());
    }

    @Test
    void canSetCity() {
        Address address = new Address();

        address.setCity("Springfield");

        assertEquals("Springfield", address.getCity());
    }

    @Test
    void canSetState() {
        Address address = new Address();

        address.setState("NY");

        assertEquals("NY", address.getState());
    }

    @Test
    void canSetPostalCode() {
        Address address = new Address();

        address.setPostalCode("10001");

        assertEquals("10001", address.getPostalCode());
    }

    @Test
    void canSetCountry() {
        Address address = new Address();

        address.setCountry("USA");

        assertEquals("USA", address.getCountry());
    }

    @Test
    void settersReturnThisForChaining() {
        Address address = new Address();

        Address returned =
                address
                        .setStreet("1 Main St")
                        .setCity("Springfield")
                        .setState("NY")
                        .setPostalCode("10001")
                        .setCountry("USA");

        assertEquals(address, returned);
    }

    @Test
    void defaultFieldsAreNull() {
        Address address = new Address();

        assertNull(address.getStreet());
        assertNull(address.getCity());
        assertNull(address.getState());
        assertNull(address.getPostalCode());
        assertNull(address.getCountry());
    }

    @Test
    void equalWhenAllFieldsMatch() {
        Address a =
                new Address()
                        .setStreet("1 Main St")
                        .setCity("Springfield")
                        .setState("NY")
                        .setPostalCode("10001")
                        .setCountry("USA");
        Address b =
                new Address()
                        .setStreet("1 Main St")
                        .setCity("Springfield")
                        .setState("NY")
                        .setPostalCode("10001")
                        .setCountry("USA");

        assertEquals(a, b);
    }

    @Test
    void notEqualWhenStreetDiffers() {
        Address a = new Address().setStreet("1 Main St").setCity("Springfield");
        Address b = new Address().setStreet("2 Other Ave").setCity("Springfield");

        assertNotEquals(a, b);
    }

    @Test
    void notEqualWhenCityDiffers() {
        Address a = new Address().setStreet("1 Main St").setCity("Springfield");
        Address b = new Address().setStreet("1 Main St").setCity("Shelbyville");

        assertNotEquals(a, b);
    }

    @Test
    void notEqualWhenStateDiffers() {
        Address a = new Address().setState("NY");
        Address b = new Address().setState("CA");

        assertNotEquals(a, b);
    }

    @Test
    void notEqualWhenPostalCodeDiffers() {
        Address a = new Address().setPostalCode("10001");
        Address b = new Address().setPostalCode("90210");

        assertNotEquals(a, b);
    }

    @Test
    void notEqualWhenCountryDiffers() {
        Address a = new Address().setCountry("USA");
        Address b = new Address().setCountry("CAN");

        assertNotEquals(a, b);
    }

    @Test
    void hashCodeConsistentWithEquals() {
        Address a =
                new Address()
                        .setStreet("1 Main St")
                        .setCity("Springfield")
                        .setState("NY")
                        .setPostalCode("10001")
                        .setCountry("USA");
        Address b =
                new Address()
                        .setStreet("1 Main St")
                        .setCity("Springfield")
                        .setState("NY")
                        .setPostalCode("10001")
                        .setCountry("USA");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toStringContainsAllFields() {
        Address address =
                new Address()
                        .setStreet("1 Main St")
                        .setCity("Springfield")
                        .setState("NY")
                        .setPostalCode("10001")
                        .setCountry("USA");

        String result = address.toString();

        assertTrue(result.contains("1 Main St"));
        assertTrue(result.contains("Springfield"));
        assertTrue(result.contains("NY"));
        assertTrue(result.contains("10001"));
        assertTrue(result.contains("USA"));
    }

    @Test
    void toStringWorksWithNullFields() {
        Address address = new Address();

        assertDoesNotThrow(() -> address.toString());
    }

    @Test
    void notEqualToNull() {
        Address address = new Address().setStreet("1 Main St");

        assertNotEquals(null, address);
    }

    @Test
    void notEqualToDifferentType() {
        Address address = new Address().setStreet("1 Main St");

        assertNotEquals("1 Main St", address);
    }

    @Test
    void equalToItself() {
        Address address = new Address().setStreet("1 Main St");

        assertEquals(address, address);
    }
}
