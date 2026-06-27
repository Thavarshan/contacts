package com.filemanager.csv;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests for OpenCSV-backed row writing. */
class CsvWriterTest {
    @TempDir Path tmpDir;

    @Test
    void writeNullRowReturnsFalse() throws IOException {
        try (CsvWriter writer = new CsvWriter(tmpDir.resolve("contacts.csv"))) {
            assertFalse(writer.write(null));
        }
    }

    @Test
    void writeAllNullReturnsFalse() throws IOException {
        try (CsvWriter writer = new CsvWriter(tmpDir.resolve("contacts.csv"))) {
            assertFalse(writer.writeAll(null));
        }
    }

    @Test
    void writeAllEmptyListSucceeds() throws IOException {
        try (CsvWriter writer = new CsvWriter(tmpDir.resolve("contacts.csv"))) {
            assertTrue(writer.writeAll(List.of()));
        }
    }

    @Test
    void writeSingleRowCreatesFile() throws IOException {
        Path filePath = tmpDir.resolve("contacts.csv");

        try (CsvWriter writer = new CsvWriter(filePath)) {
            boolean written = writer.write(new String[] {"John", "Doe"});
            assertTrue(written);
        }

        assertTrue(Files.exists(filePath));
    }

    @Test
    void writeAllPersistsRows() throws IOException {
        Path filePath = tmpDir.resolve("contacts.csv");

        try (CsvWriter writer = new CsvWriter(filePath)) {
            writer.writeAll(
                    List.of(new String[] {"firstName"}, new String[] {"Alice"}, new String[] {"Bob"}));
        }

        try (CsvReader reader = new CsvReader(filePath)) {
            List<String[]> rows = reader.readAll();
            assertEquals(3, rows.size());
            assertEquals("firstName", rows.get(0)[0]);
            assertEquals("Alice", rows.get(1)[0]);
            assertEquals("Bob", rows.get(2)[0]);
        }
    }

    @Test
    void writeAllTruncatesExistingContent() throws IOException {
        Path filePath = tmpDir.resolve("contacts.csv");

        try (CsvWriter writer = new CsvWriter(filePath)) {
            writer.writeAll(List.<String[]>of(new String[] {"old"}));
            writer.writeAll(List.<String[]>of(new String[] {"new"}));
        }

        try (CsvReader reader = new CsvReader(filePath)) {
            List<String[]> rows = reader.readAll();
            assertEquals(1, rows.size());
            assertEquals("new", rows.get(0)[0]);
        }
    }

    @Test
    void closeWhenNeverOpenedDoesNotThrow() throws IOException {
        CsvWriter writer = new CsvWriter(tmpDir.resolve("contacts.csv"));

        assertDoesNotThrow(writer::close);
    }

    @Test
    void closeAfterWriteDoesNotThrow() throws IOException {
        Path filePath = tmpDir.resolve("contacts.csv");
        CsvWriter writer = new CsvWriter(filePath);
        writer.write(new String[] {"data"});

        assertDoesNotThrow(writer::close);
    }

    @Test
    void createsParentDirectoriesWhenMissing() throws IOException {
        Path nested = tmpDir.resolve("a").resolve("b").resolve("contacts.csv");

        try (CsvWriter writer = new CsvWriter(nested)) {
            boolean written = writer.write(new String[] {"test"});
            assertTrue(written);
        }

        assertTrue(Files.exists(nested));
    }

    @Test
    void consecutiveWritesAppendToSameFile() throws IOException {
        Path filePath = tmpDir.resolve("contacts.csv");

        try (CsvWriter writer = new CsvWriter(filePath)) {
            writer.write(new String[] {"row1"});
            writer.write(new String[] {"row2"});
        }

        try (CsvReader reader = new CsvReader(filePath)) {
            List<String[]> rows = reader.readAll();
            assertEquals(2, rows.size());
            assertEquals("row1", rows.get(0)[0]);
            assertEquals("row2", rows.get(1)[0]);
        }
    }
}
