package com.contacts.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.contacts.Address;
import com.contacts.Contact;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Unit tests for contact search matching. */
class ContactMatcherTest {

    @Test
    void matchesNameCaseInsensitively() {
        Contact contact = new Contact().setFirstName("Jane").setLastName("Doe");

        assertTrue(new ContactMatcher().matches(contact, "jane"));
    }

    @Test
    void matchesEmailPhoneCompanyNotesAndAddress() {
        Contact contact =
                new Contact()
                        .setCompany("Acme")
                        .setNotes("VIP customer")
                        .setEmailAddresses(List.of("jane@example.com"))
                        .setPhoneNumbers(List.of("+15550100"))
                        .setAddresses(List.of(new Address().setCity("Colombo")));
        ContactMatcher matcher = new ContactMatcher();

        assertTrue(matcher.matches(contact, "example.com"));
        assertTrue(matcher.matches(contact, "555"));
        assertTrue(matcher.matches(contact, "acme"));
        assertTrue(matcher.matches(contact, "vip"));
        assertTrue(matcher.matches(contact, "colombo"));
    }

    @Test
    void matchesDates() {
        Contact contact =
                new Contact()
                        .setBirthday(LocalDate.of(1990, 1, 2))
                        .setDates(List.of(LocalDate.of(2024, 5, 6)));

        assertTrue(new ContactMatcher().matches(contact, "2024-05"));
    }

    @Test
    void doesNotMatchBlankOrMissingQuery() {
        Contact contact = new Contact().setFirstName("Jane");
        ContactMatcher matcher = new ContactMatcher();

        assertFalse(matcher.matches(contact, null));
        assertFalse(matcher.matches(contact, ""));
        assertFalse(matcher.matches(contact, "bob"));
    }
}
