package com.contacts.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.contacts.ContactService;
import com.contacts.CsvContactMapper;
import com.contacts.CsvContactRepository;
import com.filemanager.csv.CsvReader;
import com.filemanager.csv.CsvWriter;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Integration tests for the contacts HTTP API. */
class ContactsApiTest {
    @TempDir Path tempDir;

    @Test
    void healthReturnsOk() {
        JavalinTest.test(app(), (server, client) -> assertEquals(200, client.get("/health").code()));
    }

    @Test
    void createsListsUpdatesAndDeletesContact() {
        JavalinTest.test(
                app(),
                (server, client) -> {
                    ContactPayload createPayload =
                            new ContactPayload(
                                    "",
                                    "Jane",
                                    "Doe",
                                    "Acme",
                                    "Engineer",
                                    "+15550100",
                                    "jane@example.com",
                                    "1 Main St",
                                    "Colombo",
                                    "WP",
                                    "00100",
                                    "Sri Lanka",
                                    "1990-01-02",
                                    "Created through API");

                    try (var createResponse = client.post("/api/contacts", createPayload)) {
                        assertEquals(201, createResponse.code());
                        String responseBody = createResponse.body().string();
                        assertTrue(responseBody.contains("\"firstName\":\"Jane\""));
                    }

                    String id;
                    try (var listResponse = client.get("/api/contacts")) {
                        assertEquals(200, listResponse.code());
                        String responseBody = listResponse.body().string();
                        assertTrue(responseBody.contains("jane@example.com"));
                        id = responseBody.replaceFirst(".*\"id\":\"([^\"]+)\".*", "$1");
                    }

                    ContactPayload updatePayload =
                            new ContactPayload(
                                    id,
                                    "Jane",
                                    "Doe",
                                    "Acme Labs",
                                    "Engineer",
                                    "+15550100",
                                    "jane@example.com",
                                    "1 Main St",
                                    "Colombo",
                                    "WP",
                                    "00100",
                                    "Sri Lanka",
                                    "1990-01-02",
                                    "Created through API");
                    try (var updateResponse = client.put("/api/contacts/" + id, updatePayload)) {
                        assertEquals(200, updateResponse.code());
                        assertTrue(updateResponse.body().string().contains("Acme Labs"));
                    }

                    try (var deleteResponse = client.delete("/api/contacts/" + id)) {
                        assertEquals(200, deleteResponse.code());
                        assertTrue(deleteResponse.body().string().contains("\"deleted\":1"));
                    }
                });
    }

    @Test
    void unknownContactReturnsNotFound() {
        JavalinTest.test(
                app(),
                (server, client) -> assertEquals(404, client.delete("/api/contacts/missing").code()));
    }

    private Javalin app() {
        return ContactsApi.create(service());
    }

    private ContactService service() {
        Path file = tempDir.resolve("contacts.csv");
        try {
            return new ContactService(
                    new CsvContactRepository(
                            new CsvReader(file),
                            new CsvWriter(file),
                            new CsvContactMapper()));
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
