package com.filemanager;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Coordinates row-level reading and writing.
 *
 * <p>This class is deliberately generic: it validates that callers provide dependencies and
 * delegates all actual storage behavior to {@link Reader} and {@link Writer} implementations.
 */
public class Manager {

    private final Reader reader;
    private final Writer writer;

    /**
     * Create a manager with the specified reader and writer.
     *
     * @param reader reader implementation used to load data
     * @param writer writer implementation used to save data
     */
    public Manager(Reader reader, Writer writer) {
        this.reader = Objects.requireNonNull(reader, "reader must not be null");
        this.writer = Objects.requireNonNull(writer, "writer must not be null");
    }

    /**
     * Initialize data using the provided input stream.
     *
     * @param data input stream containing contact data
     */
    public void init(InputStream data) {
        Objects.requireNonNull(data, "data must not be null");
    }

    /**
     * Load every row from the configured reader.
     *
     * @return rows from the reader
     * @throws java.io.IOException when the source cannot be read
     */
    public List<String[]> readAll() throws java.io.IOException {
        return reader.readAll();
    }

    /**
     * Write every row through the configured writer.
     *
     * @param rows rows to write
     * @return {@code true} when every row is written
     */
    public boolean writeAll(List<String[]> rows) {
        return writer.writeAll(rows);
    }
}
