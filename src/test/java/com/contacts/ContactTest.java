package com.contacts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link Contact} domain model. */
public class ContactTest {

    @Test
    void canSetFirstName() {
        Contact contact = new Contact();

        contact.setFirstName("John");

        assertEquals("John", contact.getFirstName());
    }

    @Test
    void canSetMiddleName() {
        Contact contact = new Contact();

        contact.setMiddleName("Lex");

        assertEquals("Lex", contact.getMiddleName());
    }

    @Test
    void canSetLastName() {
        Contact contact = new Contact();

        contact.setLastName("Doe");

        assertEquals("Doe", contact.getLastName());
    }

    @Test
    void canSetNickName() {
        Contact contact = new Contact();

        contact.setNickName("Joe");

        assertEquals("Joe", contact.getNickName());
    }

    @Test
    void listSettersDefensivelyCopyValues() {
        Contact contact = new Contact();
        List<String> phoneNumbers = new ArrayList<>(List.of("111"));

        contact.setPhoneNumbers(phoneNumbers);
        phoneNumbers.add("222");

        assertEquals(List.of("111"), contact.getPhoneNumbers());
        assertThrows(UnsupportedOperationException.class, () -> contact.getPhoneNumbers().add("333"));
    }

    @Test
    void canSetCompany() {
        Contact contact = new Contact();

        contact.setCompany("Acme Corp");

        assertEquals("Acme Corp", contact.getCompany());
    }

    @Test
    void canSetJobTitle() {
        Contact contact = new Contact();

        contact.setJobTitle("Engineer");

        assertEquals("Engineer", contact.getJobTitle());
    }

    @Test
    void canSetDepartment() {
        Contact contact = new Contact();

        contact.setDepartment("Platform");

        assertEquals("Platform", contact.getDepartment());
    }

    @Test
    void canSetNotes() {
        Contact contact = new Contact();

        contact.setNotes("Some notes here.");

        assertEquals("Some notes here.", contact.getNotes());
    }

    @Test
    void canSetBirthday() {
        Contact contact = new Contact();
        LocalDate birthday = LocalDate.of(1990, 6, 15);

        contact.setBirthday(birthday);

        assertEquals(birthday, contact.getBirthday());
    }

    @Test
    void canSetEmailAddresses() {
        Contact contact = new Contact();

        contact.setEmailAddresses(List.of("a@example.com", "b@example.com"));

        assertEquals(List.of("a@example.com", "b@example.com"), contact.getEmailAddresses());
    }

    @Test
    void canSetUrlAddresses() {
        Contact contact = new Contact();

        contact.setUrlAddresses(List.of("https://example.com"));

        assertEquals(List.of("https://example.com"), contact.getUrlAddresses());
    }

    @Test
    void canSetAddresses() {
        Contact contact = new Contact();
        Address address = new Address().setStreet("1 Main St").setCity("NYC");

        contact.setAddresses(List.of(address));

        assertEquals(List.of(address), contact.getAddresses());
    }

    @Test
    void canSetDates() {
        Contact contact = new Contact();
        List<LocalDate> dates = List.of(LocalDate.of(2020, 1, 1));

        contact.setDates(dates);

        assertEquals(dates, contact.getDates());
    }

    @Test
    void canSetSocialProfiles() {
        Contact contact = new Contact();

        contact.setSocialProfiles(List.of("@johndoe"));

        assertEquals(List.of("@johndoe"), contact.getSocialProfiles());
    }

    @Test
    void canSetInstantMessageHandles() {
        Contact contact = new Contact();

        contact.setInstantMessageHandles(List.of("johndoe@xmpp"));

        assertEquals(List.of("johndoe@xmpp"), contact.getInstantMessageHandles());
    }

    @Test
    void listSetterWithNullProducesEmptyList() {
        Contact contact = new Contact();

        contact.setPhoneNumbers(null);

        assertEquals(List.of(), contact.getPhoneNumbers());
    }

    @Test
    void listSetterWithEmptyListProducesEmptyList() {
        Contact contact = new Contact();

        contact.setEmailAddresses(List.of());

        assertEquals(List.of(), contact.getEmailAddresses());
    }

    @Test
    void listGettersReturnImmutableLists() {
        Contact contact = new Contact().setPhoneNumbers(List.of("111"));

        assertThrows(UnsupportedOperationException.class, () -> contact.getPhoneNumbers().add("222"));
        assertThrows(
                UnsupportedOperationException.class,
                () -> contact.getEmailAddresses().add("x@example.com"));
        assertThrows(UnsupportedOperationException.class, () -> contact.getUrlAddresses().add("url"));
        assertThrows(
                UnsupportedOperationException.class, () -> contact.getAddresses().add(new Address()));
        assertThrows(
                UnsupportedOperationException.class, () -> contact.getDates().add(LocalDate.now()));
        assertThrows(UnsupportedOperationException.class, () -> contact.getSocialProfiles().add("x"));
        assertThrows(
                UnsupportedOperationException.class, () -> contact.getInstantMessageHandles().add("x"));
    }

    @Test
    void settersReturnThisForChaining() {
        Contact contact = new Contact();

        Contact returned =
                contact
                        .setFirstName("John")
                        .setMiddleName("M")
                        .setLastName("Doe")
                        .setNickName("JD")
                        .setCompany("Acme")
                        .setJobTitle("Engineer")
                        .setDepartment("Platform")
                        .setNotes("Notes");

        assertEquals(contact, returned);
    }

    @Test
    void defaultStringFieldsAreNull() {
        Contact contact = new Contact();

        assertNull(contact.getFirstName());
        assertNull(contact.getMiddleName());
        assertNull(contact.getLastName());
        assertNull(contact.getNickName());
        assertNull(contact.getCompany());
        assertNull(contact.getJobTitle());
        assertNull(contact.getDepartment());
        assertNull(contact.getNotes());
        assertNull(contact.getBirthday());
    }

    @Test
    void defaultListFieldsAreEmpty() {
        Contact contact = new Contact();

        assertEquals(List.of(), contact.getPhoneNumbers());
        assertEquals(List.of(), contact.getEmailAddresses());
        assertEquals(List.of(), contact.getUrlAddresses());
        assertEquals(List.of(), contact.getAddresses());
        assertEquals(List.of(), contact.getDates());
        assertEquals(List.of(), contact.getSocialProfiles());
        assertEquals(List.of(), contact.getInstantMessageHandles());
    }

    @Test
    void equalWhenAllFieldsMatch() {
        Contact a =
                new Contact()
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setEmailAddresses(List.of("john@example.com"))
                        .setBirthday(LocalDate.of(1990, 1, 1));
        Contact b =
                new Contact()
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setEmailAddresses(List.of("john@example.com"))
                        .setBirthday(LocalDate.of(1990, 1, 1));

        assertEquals(a, b);
    }

    @Test
    void notEqualWhenFirstNameDiffers() {
        Contact a = new Contact().setFirstName("John");
        Contact b = new Contact().setFirstName("Jane");

        assertNotEquals(a, b);
    }

    @Test
    void notEqualWhenLastNameDiffers() {
        Contact a = new Contact().setFirstName("John").setLastName("Doe");
        Contact b = new Contact().setFirstName("John").setLastName("Smith");

        assertNotEquals(a, b);
    }

    @Test
    void notEqualWhenEmailsDiffer() {
        Contact a = new Contact().setEmailAddresses(List.of("a@example.com"));
        Contact b = new Contact().setEmailAddresses(List.of("b@example.com"));

        assertNotEquals(a, b);
    }

    @Test
    void notEqualToNull() {
        Contact contact = new Contact().setFirstName("John");

        assertNotEquals(null, contact);
    }

    @Test
    void notEqualToDifferentType() {
        Contact contact = new Contact().setFirstName("John");

        assertNotEquals("John", contact);
    }

    @Test
    void equalToItself() {
        Contact contact = new Contact().setFirstName("John");

        assertEquals(contact, contact);
    }

    @Test
    void hashCodeConsistentWithEquals() {
        Contact a =
                new Contact()
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setEmailAddresses(List.of("john@example.com"));
        Contact b =
                new Contact()
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setEmailAddresses(List.of("john@example.com"));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toStringContainsNameFields() {
        Contact contact =
                new Contact().setFirstName("John").setMiddleName("M").setLastName("Doe").setNickName("JD");

        String result = contact.toString();

        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }
}
