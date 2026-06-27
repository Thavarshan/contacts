package com.contacts.api;

import com.contacts.ContactService;
import com.contacts.CsvContactMapper;
import com.contacts.CsvContactRepository;
import com.filemanager.csv.CsvReader;
import com.filemanager.csv.CsvWriter;
import java.io.IOException;
import java.nio.file.Path;

/** Entry point for the lightweight contacts HTTP API. */
public final class ApiMain {
    private static final int DEFAULT_PORT = 7070;
    private static final String DEFAULT_CONTACTS_FILE = "contacts.csv";

    private ApiMain() {
        // Entry-point utility class.
    }

    /**
     * Start the contacts API.
     *
     * @param args unused command-line arguments
     * @throws IOException when the CSV repository cannot be initialized
     */
    public static void main(String[] args) throws IOException {
        Path contactsPath = Path.of(env("CONTACTS_CSV_PATH", DEFAULT_CONTACTS_FILE));
        int port = Integer.parseInt(env("CONTACTS_API_PORT", Integer.toString(DEFAULT_PORT)));
        ContactService service =
                new ContactService(
                        new CsvContactRepository(
                                new CsvReader(contactsPath),
                                new CsvWriter(contactsPath),
                                new CsvContactMapper()));

        ContactsApi.create(service).start(port);
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
