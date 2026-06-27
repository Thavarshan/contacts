package com.contacts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.filemanager.csv.CsvReader;
import com.filemanager.csv.CsvWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Integration-style tests for CSV-backed contact persistence. */
class CsvContactRepositoryTest {
    @TempDir Path tmpDir;

    @Test
    void savesAndLoadsContacts() throws IOException {
        Path contactsPath = tmpDir.resolve("contacts.csv");
        CsvContactRepository repository = repository(contactsPath);
        Contact contact =
                new Contact()
                        .setFirstName("Jane")
                        .setLastName("Doe")
                        .setEmailAddresses(List.of("jane@example.com"));

        repository.saveAll(List.of(contact));

        assertEquals(List.of(contact), repository.findAll());
    }

    @Test
    void saveAllReplacesExistingContacts() throws IOException {
        Path contactsPath = tmpDir.resolve("contacts.csv");
        CsvContactRepository repository = repository(contactsPath);

        repository.saveAll(List.of(new Contact().setFirstName("Old")));
        repository.saveAll(List.of(new Contact().setFirstName("New")));

        assertEquals(List.of(new Contact().setFirstName("New")), repository.findAll());
    }

    @Test
    void findAllOnMissingFileReturnsEmpty() throws IOException {
        Path contactsPath = tmpDir.resolve("does-not-exist.csv");
        CsvContactRepository repository = repository(contactsPath);

        assertEquals(List.of(), repository.findAll());
    }

    @Test
    void findAllSkipsHeaderRow() throws IOException {
        Path contactsPath = tmpDir.resolve("contacts.csv");
        CsvContactRepository repository = repository(contactsPath);

        repository.saveAll(List.of());

        assertEquals(List.of(), repository.findAll());
    }

    @Test
    void findAllReturnsMultipleContacts() throws IOException {
        Path contactsPath = tmpDir.resolve("contacts.csv");
        CsvContactRepository repository = repository(contactsPath);
        List<Contact> contacts =
                List.of(
                        new Contact().setFirstName("Alice"),
                        new Contact().setFirstName("Bob"),
                        new Contact().setFirstName("Carol"));

        repository.saveAll(contacts);

        assertEquals(contacts, repository.findAll());
    }

    @Test
    void saveDefaultMethodAppendsContact() throws IOException {
        Path contactsPath = tmpDir.resolve("contacts.csv");
        CsvContactRepository repository = repository(contactsPath);
        repository.saveAll(List.of(new Contact().setFirstName("Existing")));

        repository.save(new Contact().setFirstName("Added"));

        assertEquals(
                List.of(new Contact().setFirstName("Existing"), new Contact().setFirstName("Added")),
                repository.findAll());
    }

    @Test
    void saveAllThrowsOnNullContacts() throws IOException {
        CsvContactRepository repository = repository(tmpDir.resolve("contacts.csv"));

        assertThrows(NullPointerException.class, () -> repository.saveAll(null));
    }

    @Test
    void constructorRejectsNullReader() throws IOException {
        assertThrows(
                NullPointerException.class,
                () ->
                        new CsvContactRepository(
                                null, new CsvWriter(tmpDir.resolve("c.csv")), new CsvContactMapper()));
    }

    @Test
    void constructorRejectsNullWriter() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new CsvContactRepository(
                                new CsvReader(tmpDir.resolve("c.csv")), null, new CsvContactMapper()));
    }

    @Test
    void constructorRejectsNullMapper() throws IOException {
        assertThrows(
                NullPointerException.class,
                () ->
                        new CsvContactRepository(
                                new CsvReader(tmpDir.resolve("c.csv")),
                                new CsvWriter(tmpDir.resolve("c.csv")),
                                null));
    }

    @Test
    void findAllSkipsBlankRows() throws IOException {
        Path contactsPath = tmpDir.resolve("contacts.csv");
        Files.writeString(
                contactsPath,
                String.join(",", CsvContactMapper.header())
                        + System.lineSeparator()
                        + System.lineSeparator());
        CsvContactRepository repository = repository(contactsPath);

        assertEquals(List.of(), repository.findAll());
    }

    private static CsvContactRepository repository(Path contactsPath) throws IOException {
        return new CsvContactRepository(
                new CsvReader(contactsPath), new CsvWriter(contactsPath), new CsvContactMapper());
    }
}
