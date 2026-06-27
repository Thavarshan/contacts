package com.contacts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link ContactService} service-layer behavior. */
class ContactServiceTest {

    /** In-memory repository stub used to isolate service behavior from CSV persistence. */
    private static class StubRepository implements ContactRepository {
        private List<Contact> contacts = new ArrayList<>();
        private IOException findAllException;
        private IOException saveAllException;

        @Override
        public List<Contact> findAll() throws IOException {
            if (findAllException != null) {
                throw findAllException;
            }
            return List.copyOf(contacts);
        }

        @Override
        public void saveAll(List<Contact> contacts) throws IOException {
            if (saveAllException != null) {
                throw saveAllException;
            }
            this.contacts = new ArrayList<>(contacts);
        }
    }

    @Test
    void constructorRejectsNullRepository() {
        assertThrows(NullPointerException.class, () -> new ContactService(null));
    }

    @Test
    void getAllContactsReturnsRepositoryContents() throws IOException {
        StubRepository repo = new StubRepository();
        repo.contacts.add(new Contact().setFirstName("Alice"));
        ContactService service = new ContactService(repo);

        List<Contact> contacts = service.getAllContacts();

        assertEquals(List.of(new Contact().setFirstName("Alice")), contacts);
    }

    @Test
    void getAllContactsReturnsEmptyListWhenNoContacts() throws IOException {
        ContactService service = new ContactService(new StubRepository());

        assertEquals(List.of(), service.getAllContacts());
    }

    @Test
    void addContactRejectsNull() {
        ContactService service = new ContactService(new StubRepository());

        assertThrows(NullPointerException.class, () -> service.addContact(null));
    }

    @Test
    void addContactAppendsToExistingContacts() throws IOException {
        StubRepository repo = new StubRepository();
        repo.contacts.add(new Contact().setFirstName("Existing"));
        ContactService service = new ContactService(repo);

        service.addContact(new Contact().setFirstName("New"));

        assertEquals(
                List.of(new Contact().setFirstName("Existing"), new Contact().setFirstName("New")),
                service.getAllContacts());
    }

    @Test
    void addContactSavesWhenRepositoryIsEmpty() throws IOException {
        StubRepository repo = new StubRepository();
        ContactService service = new ContactService(repo);
        Contact contact = new Contact().setFirstName("Only");

        service.addContact(contact);

        assertEquals(List.of(contact), service.getAllContacts());
    }

    @Test
    void replaceContactsRejectsNull() {
        ContactService service = new ContactService(new StubRepository());

        assertThrows(NullPointerException.class, () -> service.replaceContacts(null));
    }

    @Test
    void replaceContactsOverwritesExistingContacts() throws IOException {
        StubRepository repo = new StubRepository();
        repo.contacts.add(new Contact().setFirstName("Old"));
        ContactService service = new ContactService(repo);

        service.replaceContacts(List.of(new Contact().setFirstName("New")));

        assertEquals(List.of(new Contact().setFirstName("New")), service.getAllContacts());
    }

    @Test
    void replaceContactsWithEmptyListClearsAll() throws IOException {
        StubRepository repo = new StubRepository();
        repo.contacts.add(new Contact().setFirstName("Existing"));
        ContactService service = new ContactService(repo);

        service.replaceContacts(List.of());

        assertEquals(List.of(), service.getAllContacts());
    }

    @Test
    void replaceContactsPreservesMultipleContacts() throws IOException {
        ContactService service = new ContactService(new StubRepository());
        List<Contact> contacts =
                List.of(
                        new Contact().setFirstName("Alice"),
                        new Contact().setFirstName("Bob"),
                        new Contact().setFirstName("Carol"));

        service.replaceContacts(contacts);

        assertEquals(contacts, service.getAllContacts());
    }

    @Test
    void getAllContactsPropagatesRepositoryFailure() {
        StubRepository repo = new StubRepository();
        repo.findAllException = new IOException("load failed");
        ContactService service = new ContactService(repo);

        IOException exception = assertThrows(IOException.class, service::getAllContacts);

        assertEquals("load failed", exception.getMessage());
    }

    @Test
    void addContactPropagatesSaveFailure() {
        StubRepository repo = new StubRepository();
        repo.saveAllException = new IOException("save failed");
        ContactService service = new ContactService(repo);

        IOException exception =
                assertThrows(
                        IOException.class, () -> service.addContact(new Contact().setFirstName("Jane")));

        assertEquals("save failed", exception.getMessage());
    }

    @Test
    void replaceContactsDefensivelyCopiesInputListBeforeSaving() throws IOException {
        StubRepository repo = new StubRepository();
        ContactService service = new ContactService(repo);
        List<Contact> contacts = new ArrayList<>(List.of(new Contact().setFirstName("Original")));

        service.replaceContacts(contacts);
        contacts.add(new Contact().setFirstName("Mutated"));

        assertEquals(List.of(new Contact().setFirstName("Original")), service.getAllContacts());
    }
}
