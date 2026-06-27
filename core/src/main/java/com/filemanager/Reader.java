package com.filemanager;

import java.io.IOException;
import java.util.List;

/** Reader contract for loading row-oriented data from a source. */
public interface Reader extends AutoCloseable {

    /**
     * Read all rows from the underlying source.
     *
     * @return rows from the source
     * @throws IOException when the source cannot be read
     */
    List<String[]> readAll() throws IOException;

    /**
     * Close the underlying reader and release resources.
     *
     * @throws IOException when an I/O error occurs during close
     */
    @Override
    default void close() throws IOException {
        // Implementations may not keep resources open between reads.
    }
}
