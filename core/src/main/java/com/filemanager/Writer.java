package com.filemanager;

import java.io.IOException;
import java.util.List;

/**
 * Generic writer contract for application file writers.
 *
 * <p>Implementations are responsible for writing a single record at a time and closing any
 * underlying resources when they are no longer needed.
 */
public interface Writer extends AutoCloseable {

    /**
     * Write a single row to the target destination.
     *
     * @param data row values to write
     * @return {@code true} when the row is successfully written, otherwise {@code false}
     */
    boolean write(String[] data);

    /**
     * Write multiple rows to the target destination.
     *
     * @param rows rows to write
     * @return {@code true} when every row is accepted for writing
     */
    default boolean writeAll(List<String[]> rows) {
        if (rows == null) {
            return false;
        }

        for (String[] row : rows) {
            if (!write(row)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Close the underlying writer and release resources.
     *
     * @throws IOException when an I/O error occurs during close
     */
    @Override
    void close() throws IOException;
}
