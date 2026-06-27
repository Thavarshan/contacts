package com.contacts.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.contacts.Contact;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Unit tests for single-line contact summaries shown by list and search commands. */
class ContactFormatterTest {

    @Test
    void formatsNameEmailPhoneAndCompany() {
        Contact contact =
            new Contact()
                    .setId("contact-123")
                    .setFirstName("Jane")
                        .setLastName("Doe")
                        .setCompany("Acme")
                        .setEmailAddresses(List.of("jane@example.com"))
                        .setPhoneNumbers(List.of("+15550100"));

        String formatted = new ContactFormatter().format(3, contact);

        assertEquals("3. Jane Doe [contact-123] <jane@example.com> +15550100 - Acme", formatted);
    }

    @Test
    void formatsUnnamedContact() {
        String formatted = new ContactFormatter().format(1, new Contact().setId("contact-123"));

        assertEquals("1. (unnamed) [contact-123]", formatted);
    }
}
