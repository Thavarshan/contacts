package com.filemanager.csv;

import com.filemanager.Writer;
import com.opencsv.CSVWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

/**
 * CSV writer implementation backed by OpenCSV.
 *
 * <p>This class keeps the underlying writer open for repeated writes and only closes the resource
 * when {@link #close()} is called. This allows clients to write multiple rows without reopening the
 * file each time.
 */
public class CsvWriter implements Writer {
    private final Path filePath;
    private CSVWriter writer;

    /**
     * Create a CSV writer for the specified file.
     *
     * @param filePath target CSV file path
     * @throws IOException when the file cannot be opened for writing
     */
    public CsvWriter(Path filePath) throws IOException {
        this.filePath = Objects.requireNonNull(filePath, "filePath must not be null");
    }

    /**
     * Write a single row to the target destination.
     *
     * @param data row values to write
     * @return {@code true} when the row is successfully written, otherwise {@code false}
     */
    @Override
    public boolean write(String[] data) {
        if (data == null) {
            return false;
        }

        try {
            openIfNeeded();
            writer.writeNext(data);
            writer.flush();
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    /**
     * Write a group of rows, replacing any existing file contents.
     *
     * <p>Bulk writes are used by contact persistence as a full-file replacement operation, so the
     * underlying writer is reset before rows are written.
     *
     * @param rows rows to write
     * @return {@code true} when every row is written
     */
    @Override
    public boolean writeAll(List<String[]> rows) {
        if (rows == null) {
            return false;
        }

        try {
            close();
            writer = null;
        } catch (IOException exception) {
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
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    /**
     * Open the underlying CSV writer on first use.
     *
     * <p>Laziness avoids truncating a file when a writer is injected into a repository that only
     * performs reads.
     *
     * @throws IOException when the target file cannot be opened
     */
    private void openIfNeeded() throws IOException {
        if (writer != null) {
            return;
        }

        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        this.writer =
                new CSVWriter(
                        Files.newBufferedWriter(
                                filePath,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.TRUNCATE_EXISTING,
                                StandardOpenOption.WRITE));
    }
}
