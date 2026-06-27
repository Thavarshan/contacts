package com.contacts;

import com.filemanager.Reader;
import com.filemanager.Writer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CSV-backed contact repository.
 *
 * <p>The repository coordinates row-level file I/O with {@link CsvContactMapper}. It owns the
 * persistence policy for contact collections: CSV headers are skipped during reads, blank rows are
 * ignored, and saves replace the full file with a new header plus contact rows.
 */
public class CsvContactRepository implements ContactRepository {
    private final Reader reader;
    private final Writer writer;
    private final CsvContactMapper mapper;

    /**
     * Create a repository backed by row reader and writer dependencies.
     *
     * @param reader row reader
     * @param writer row writer
     * @param mapper contact mapper
     */
    public CsvContactRepository(Reader reader, Writer writer, CsvContactMapper mapper) {
        this.reader = Objects.requireNonNull(reader, "reader must not be null");
        this.writer = Objects.requireNonNull(writer, "writer must not be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper must not be null");
    }

    @Override
    public List<Contact> findAll() throws IOException {
        List<Contact> contacts = new ArrayList<>();
        for (String[] row : reader.readAll()) {
            if (isBlank(row) || mapper.isHeader(row)) {
                continue;
            }
            contacts.add(mapper.fromRow(row));
        }
        return contacts;
    }

    @Override
    public void saveAll(List<Contact> contacts) throws IOException {
        Objects.requireNonNull(contacts, "contacts must not be null");

        List<String[]> rows = new ArrayList<>();
        rows.add(CsvContactMapper.header());
        for (Contact contact : contacts) {
            rows.add(mapper.toRow(contact));
        }

        if (!writer.writeAll(rows)) {
            throw new IOException("Unable to write contacts");
        }
    }

    /**
     * Return whether a CSV row has no meaningful cell content.
     *
     * @param row row to inspect
     * @return {@code true} when every cell is blank or the row has no cells
     */
    private static boolean isBlank(String[] row) {
        if (row.length == 0) {
            return true;
        }
        for (String cell : row) {
            if (cell != null && !cell.isBlank()) {
                return false;
            }
        }
        return true;
    }
}
