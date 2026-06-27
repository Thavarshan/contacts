package com.filemanager.csv;

import com.filemanager.Reader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * CSV reader implementation used to load row data.
 *
 * <p>The reader opens the file for each {@link #readAll()} call and closes it immediately after
 * parsing. Missing files are treated as empty sources so first-run CLI usage works without manual
 * setup.
 */
public class CsvReader implements Reader {
    private final Path filePath;

    /**
     * Create a CSV reader for the specified file.
     *
     * @param filePath source CSV file path
     */
    public CsvReader(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath, "filePath must not be null");
    }

    /**
     * Return the source CSV file path.
     *
     * @return configured file path
     */
    public Path getFilePath() {
        return this.filePath;
    }

    /**
     * Read all rows from the configured CSV file.
     *
     * @return CSV rows
     * @throws IOException when the file cannot be read or parsed
     */
    @Override
    public List<String[]> readAll() throws IOException {
        if (!Files.exists(filePath)) {
            return List.of();
        }

        try (CSVReader reader = new CSVReader(Files.newBufferedReader(filePath))) {
            return reader.readAll();
        } catch (CsvException exception) {
            throw new IOException("Unable to parse CSV file: " + filePath, exception);
        }
    }
}
