package com.filemanager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Unit tests for the row-level file manager facade. */
class ManagerTest {

    /** Reader stub that returns preconfigured rows. */
    private static class StubReader implements Reader {
        private final List<String[]> rows;

        StubReader(List<String[]> rows) {
            this.rows = rows;
        }

        @Override
        public List<String[]> readAll() {
            return rows;
        }
    }

    /** Writer stub that captures rows passed through the manager. */
    private static class StubWriter implements Writer {
        List<String[]> lastWritten;
        boolean writeAllResult = true;

        @Override
        public boolean write(String[] data) {
            return true;
        }

        @Override
        public boolean writeAll(List<String[]> rows) {
            this.lastWritten = rows;
            return writeAllResult;
        }

        @Override
        public void close() {}
    }

    @Test
    void constructorRejectsNullReader() {
        assertThrows(NullPointerException.class, () -> new Manager(null, new StubWriter()));
    }

    @Test
    void constructorRejectsNullWriter() {
        assertThrows(NullPointerException.class, () -> new Manager(new StubReader(List.of()), null));
    }

    @Test
    void readAllDelegatesToReader() throws IOException {
        List<String[]> rows = List.of(new String[] {"a", "b"}, new String[] {"c", "d"});
        Manager manager = new Manager(new StubReader(rows), new StubWriter());

        assertEquals(rows, manager.readAll());
    }

    @Test
    void readAllReturnsEmptyListWhenReaderHasNoRows() throws IOException {
        Manager manager = new Manager(new StubReader(List.of()), new StubWriter());

        assertEquals(List.of(), manager.readAll());
    }

    @Test
    void writeAllDelegatesToWriter() {
        StubWriter writer = new StubWriter();
        Manager manager = new Manager(new StubReader(List.of()), writer);
        List<String[]> rows = List.of(new String[] {"x", "y"}, new String[] {"a", "b"});

        boolean result = manager.writeAll(rows);

        assertTrue(result);
        assertEquals(rows, writer.lastWritten);
    }

    @Test
    void writeAllReturnsFalseWhenWriterFails() {
        StubWriter writer = new StubWriter();
        writer.writeAllResult = false;
        Manager manager = new Manager(new StubReader(List.of()), writer);
        List<String[]> rows = List.of();

        assertFalse(manager.writeAll(rows));
    }

    @Test
    void initRejectsNullData() {
        Manager manager = new Manager(new StubReader(List.of()), new StubWriter());

        assertThrows(NullPointerException.class, () -> manager.init(null));
    }

    @Test
    void initAcceptsValidInputStream() {
        Manager manager = new Manager(new StubReader(List.of()), new StubWriter());

        assertDoesNotThrow(() -> manager.init(new ByteArrayInputStream(new byte[0])));
    }
}
