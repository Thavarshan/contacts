package com.contacts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Unit tests for contact-to-CSV mapping and CSV schema edge cases. */
class CsvContactMapperTest {

    @Test
    void mapsContactToCsvRowAndBack() {
        CsvContactMapper mapper = new CsvContactMapper();
        Contact contact =
                new Contact()
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setPhoneNumbers(List.of("+1 555 0100", "+1 555 0101"))
                        .setEmailAddresses(List.of("john@example.com"))
                        .setAddresses(
                                List.of(
                                        new Address()
                                                .setStreet("1 Main St")
                                                .setCity("New York")
                                                .setState("NY")
                                                .setPostalCode("10001")
                                                .setCountry("USA")))
                        .setBirthday(LocalDate.of(1990, 1, 2))
                        .setDates(List.of(LocalDate.of(2020, 5, 6)))
                        .setNotes("CSV-safe, with punctuation | and symbols ~");

        Contact mapped = mapper.fromRow(mapper.toRow(contact));

        assertEquals(contact, mapped);
    }

    @Test
    void isHeaderRecognizesActualHeader() {
        CsvContactMapper mapper = new CsvContactMapper();

        assertTrue(mapper.isHeader(CsvContactMapper.header()));
    }

    @Test
    void isHeaderReturnsFalseForDataRow() {
        CsvContactMapper mapper = new CsvContactMapper();
        String[] dataRow = mapper.toRow(new Contact().setFirstName("John").setLastName("Doe"));

        assertFalse(mapper.isHeader(dataRow));
    }

    @Test
    void isHeaderReturnsFalseForEmptyRow() {
        CsvContactMapper mapper = new CsvContactMapper();

        assertFalse(mapper.isHeader(new String[0]));
    }

    @Test
    void headerReturnsCopyNotOriginalArray() {
        String[] first = CsvContactMapper.header();
        String[] second = CsvContactMapper.header();

        assertNotSame(first, second);

        first[0] = "mutated";
        assertEquals("id", CsvContactMapper.header()[0]);
    }

    @Test
    void fromRowThrowsOnNull() {
        CsvContactMapper mapper = new CsvContactMapper();

        assertThrows(NullPointerException.class, () -> mapper.fromRow(null));
    }

    @Test
    void fromRowRejectsRowsWithUnexpectedColumnCount() {
        CsvContactMapper mapper = new CsvContactMapper();
        String[] rowWithoutId =
                new String[] {"John", "", "Doe", "", "", "", "", "", "", "", "", "", "", "", "", ""};

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> mapper.fromRow(rowWithoutId));

        assertEquals("Expected 17 CSV columns but got 16", exception.getMessage());
    }

    @Test
    void toRowThrowsOnNull() {
        CsvContactMapper mapper = new CsvContactMapper();

        assertThrows(NullPointerException.class, () -> mapper.toRow(null));
    }

    @Test
    void mapsEmptyContactToBlankRow() {
        CsvContactMapper mapper = new CsvContactMapper();
        Contact empty = new Contact();

        String[] row = mapper.toRow(empty);

        assertEquals(CsvContactMapper.header().length, row.length);
        assertFalse(row[0].isBlank());
        for (int index = 1; index < row.length; index++) {
            String cell = row[index];
            assertTrue(cell.isEmpty(), "Expected blank cell but got: " + cell);
        }
    }

    @Test
    void mapsContactWithNullFieldsToBlankCells() {
        CsvContactMapper mapper = new CsvContactMapper();

        Contact mapped = mapper.fromRow(mapper.toRow(new Contact()));

        assertNull(mapped.getFirstName());
        assertNull(mapped.getLastName());
        assertNull(mapped.getBirthday());
        assertEquals(List.of(), mapped.getPhoneNumbers());
        assertEquals(List.of(), mapped.getEmailAddresses());
        assertEquals(List.of(), mapped.getAddresses());
    }

    @Test
    void mapsContactWithMultipleAddresses() {
        CsvContactMapper mapper = new CsvContactMapper();
        Contact contact =
                new Contact()
                        .setAddresses(
                                List.of(
                                        new Address().setStreet("1 Main St").setCity("NYC").setCountry("USA"),
                                        new Address().setStreet("2 Side St").setCity("LA").setCountry("USA")));

        Contact mapped = mapper.fromRow(mapper.toRow(contact));

        assertEquals(contact.getAddresses(), mapped.getAddresses());
    }

    @Test
    void mapsContactWithMultipleDates() {
        CsvContactMapper mapper = new CsvContactMapper();
        Contact contact =
                new Contact()
                        .setDates(
                                List.of(
                                        LocalDate.of(2020, 1, 1),
                                        LocalDate.of(2021, 6, 15),
                                        LocalDate.of(2022, 12, 31)));

        Contact mapped = mapper.fromRow(mapper.toRow(contact));

        assertEquals(contact.getDates(), mapped.getDates());
    }

    @Test
    void pipesAndTildesInFieldValuesRoundTripSafely() {
        CsvContactMapper mapper = new CsvContactMapper();
        Contact contact =
                new Contact().setFirstName("Pipe|Tilde~User").setNotes("has | pipe and ~ tilde");

        Contact mapped = mapper.fromRow(mapper.toRow(contact));

        assertEquals(contact, mapped);
    }

    @Test
    void mapsAllSocialAndMessagingFields() {
        CsvContactMapper mapper = new CsvContactMapper();
        Contact contact =
                new Contact()
                        .setSocialProfiles(List.of("twitter:johndoe", "linkedin:johndoe"))
                        .setInstantMessageHandles(List.of("johndoe@jabber"));

        Contact mapped = mapper.fromRow(mapper.toRow(contact));

        assertEquals(contact.getSocialProfiles(), mapped.getSocialProfiles());
        assertEquals(contact.getInstantMessageHandles(), mapped.getInstantMessageHandles());
    }

    @Test
    void mapsFullAddressWithAllParts() {
        CsvContactMapper mapper = new CsvContactMapper();
        Address address =
                new Address()
                        .setStreet("Apt 4B, 123 Elm Street")
                        .setCity("San Francisco")
                        .setState("CA")
                        .setPostalCode("94107")
                        .setCountry("USA");
        Contact contact = new Contact().setAddresses(List.of(address));

        Contact mapped = mapper.fromRow(mapper.toRow(contact));

        assertEquals(address, mapped.getAddresses().get(0));
    }
}
