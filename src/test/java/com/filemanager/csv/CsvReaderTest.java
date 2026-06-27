package com.filemanager.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests for OpenCSV-backed row reading. */
public class CsvReaderTest {
    @TempDir Path tmpDir;

    @Test
    public void mustBeCreatedWithAPathToCSVFile() throws IOException {
        assertTrue(Files.isDirectory(this.tmpDir), "Should be a directory");
        Path contacts = tmpDir.resolve("contacts.csv");

        try (CsvReader reader = new CsvReader(contacts)) {
            assertEquals(contacts, reader.getFilePath());
        }
    }

    @Test
    public void readsCsvRows() throws IOException {
        Path contacts = tmpDir.resolve("contacts.csv");
        Files.writeString(contacts, "firstName,lastName\nJohn,Doe\n");

        List<String[]> rows;
        try (CsvReader reader = new CsvReader(contacts)) {
            rows = reader.readAll();
        }

        assertEquals(2, rows.size());
        assertEquals("John", rows.get(1)[0]);
        assertEquals("Doe", rows.get(1)[1]);
    }

    @Test
    public void returnsEmptyListWhenFileDoesNotExist() throws IOException {
        Path missing = tmpDir.resolve("does-not-exist.csv");

        List<String[]> rows;
        try (CsvReader reader = new CsvReader(missing)) {
            rows = reader.readAll();
        }

        assertEquals(List.of(), rows);
    }

    @Test
    public void constructorRejectsNullPath() {
        assertThrows(NullPointerException.class, () -> new CsvReader(null));
    }

    @Test
    public void getFilePathReturnsConfiguredPath() throws IOException {
        Path path = tmpDir.resolve("contacts.csv");

        try (CsvReader reader = new CsvReader(path)) {
            assertEquals(path, reader.getFilePath());
        }
    }

    @Test
    public void readsMultipleRows() throws IOException {
        Path contacts = tmpDir.resolve("contacts.csv");
        Files.writeString(contacts, "h1,h2\nrow1a,row1b\nrow2a,row2b\n");

        List<String[]> rows;
        try (CsvReader reader = new CsvReader(contacts)) {
            rows = reader.readAll();
        }

        assertEquals(3, rows.size());
        assertEquals("row1a", rows.get(1)[0]);
        assertEquals("row2a", rows.get(2)[0]);
    }

    @Test
    public void readsEmptyFile() throws IOException {
        Path contacts = tmpDir.resolve("contacts.csv");
        Files.writeString(contacts, "");

        List<String[]> rows;
        try (CsvReader reader = new CsvReader(contacts)) {
            rows = reader.readAll();
        }

        assertEquals(List.of(), rows);
    }

    @Test
    public void readsQuotedCommasAndNewLines() throws IOException {
        Path contacts = tmpDir.resolve("contacts.csv");
        Files.writeString(contacts, "firstName,notes\nJane,\"hello, world\"\nJohn,\"line1\nline2\"\n");

        List<String[]> rows;
        try (CsvReader reader = new CsvReader(contacts)) {
            rows = reader.readAll();
        }

        assertEquals("hello, world", rows.get(1)[1]);
        assertEquals("line1\nline2", rows.get(2)[1]);
    }
}
