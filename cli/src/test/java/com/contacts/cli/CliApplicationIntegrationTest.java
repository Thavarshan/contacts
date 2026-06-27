package com.contacts.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Integration tests for command-line flows using real CSV files. */
class CliApplicationIntegrationTest {
    @TempDir Path tmpDir;

    @Test
    void addThenListPersistsContactThroughCsv() {
        Path contactsFile = tmpDir.resolve("contacts.csv");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        CliApplication app =
                new CliApplication(
                        new PrintStream(out, true, StandardCharsets.UTF_8),
                        new PrintStream(err, true, StandardCharsets.UTF_8));

        int addCode =
                app.run(
                        new String[] {
                            "--file",
                            contactsFile.toString(),
                            "add",
                            "--first-name",
                            "Jane",
                            "--last-name",
                            "Doe",
                            "--email",
                            "jane@example.com",
                            "--phone",
                            "+15550100"
                        });
        int listCode = app.run(new String[] {"--file", contactsFile.toString(), "list"});

        assertEquals(0, addCode);
        assertEquals(0, listCode);
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("Contact added:"));
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("1. Jane Doe ["));
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("<jane@example.com> +15550100"));
        assertEquals("", err.toString(StandardCharsets.UTF_8));
    }

    @Test
    void listMissingFilePrintsNoContacts() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        CliApplication app =
                new CliApplication(
                        new PrintStream(out, true, StandardCharsets.UTF_8),
                        new PrintStream(err, true, StandardCharsets.UTF_8));

        int exitCode =
                app.run(new String[] {"--file", tmpDir.resolve("missing.csv").toString(), "list"});

        assertEquals(0, exitCode);
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("No contacts found."));
        assertEquals("", err.toString(StandardCharsets.UTF_8));
    }

    @Test
    void unknownCommandReturnsUsageError() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        CliApplication app =
                new CliApplication(
                        new PrintStream(out, true, StandardCharsets.UTF_8),
                        new PrintStream(err, true, StandardCharsets.UTF_8));

        int exitCode = app.run(new String[] {"bogus"});

        assertEquals(2, exitCode);
        assertEquals("", out.toString(StandardCharsets.UTF_8));
        assertTrue(err.toString(StandardCharsets.UTF_8).contains("Unmatched argument"));
        assertTrue(err.toString(StandardCharsets.UTF_8).contains("Usage:"));
    }

    @Test
    void searchFindsMatchingContactsOnly() {
        Path contactsFile = tmpDir.resolve("contacts.csv");
        TestCli cli = new TestCli();

        cli.app.run(
                new String[] {
                    "--file",
                    contactsFile.toString(),
                    "add",
                    "--first-name",
                    "Jane",
                    "--last-name",
                    "Doe",
                    "--email",
                    "jane@example.com"
                });
        cli.app.run(
                new String[] {
                    "--file",
                    contactsFile.toString(),
                    "add",
                    "--first-name",
                    "Bob",
                    "--last-name",
                    "Smith",
                    "--email",
                    "bob@example.com"
                });

        int exitCode = cli.app.run(new String[] {"--file", contactsFile.toString(), "search", "jane"});

        assertEquals(0, exitCode);
        assertTrue(cli.output().contains("1. Jane Doe ["));
        assertTrue(cli.output().contains("<jane@example.com>"));
        assertTrue(!cli.output().contains("2. Bob Smith ["));
    }

    @Test
    void showPrintsFullContactDetails() {
        Path contactsFile = tmpDir.resolve("contacts.csv");
        TestCli cli = new TestCli();
        cli.app.run(
                new String[] {
                    "--file",
                    contactsFile.toString(),
                    "add",
                    "--first-name",
                    "Jane",
                    "--last-name",
                    "Doe",
                    "--company",
                    "Acme",
                    "--notes",
                    "Important"
                });

        int exitCode = cli.app.run(new String[] {"--file", contactsFile.toString(), "show", "1"});

        assertEquals(0, exitCode);
        assertTrue(cli.output().contains("Name: Jane Doe"));
        assertTrue(cli.output().contains("Company: Acme"));
        assertTrue(cli.output().contains("Notes: Important"));
    }

    @Test
    void deleteRemovesSelectedContact() {
        Path contactsFile = tmpDir.resolve("contacts.csv");
        TestCli cli = new TestCli();
        cli.app.run(new String[] {"--file", contactsFile.toString(), "add", "--first-name", "Jane"});
        cli.app.run(new String[] {"--file", contactsFile.toString(), "add", "--first-name", "Bob"});

        int deleteCode = cli.app.run(new String[] {"--file", contactsFile.toString(), "delete", "1"});
        int listCode = cli.app.run(new String[] {"--file", contactsFile.toString(), "list"});

        assertEquals(0, deleteCode);
        assertEquals(0, listCode);
        assertTrue(cli.output().contains("Deleted "));
        assertTrue(cli.output().contains("1. Bob"));
    }

    @Test
    void clearRemovesAllContacts() {
        Path contactsFile = tmpDir.resolve("contacts.csv");
        TestCli cli = new TestCli();
        cli.app.run(new String[] {"--file", contactsFile.toString(), "add", "--first-name", "Jane"});

        int clearCode = cli.app.run(new String[] {"--file", contactsFile.toString(), "clear"});
        int listCode = cli.app.run(new String[] {"--file", contactsFile.toString(), "list"});

        assertEquals(0, clearCode);
        assertEquals(0, listCode);
        assertTrue(cli.output().contains("All contacts cleared."));
        assertTrue(cli.output().contains("No contacts found."));
    }

    @Test
    void invalidDeleteIndexReturnsUsageError() {
        TestCli cli = new TestCli();

        int exitCode = cli.app.run(new String[] {"delete", "0"});

        assertEquals(2, exitCode);
        assertTrue(cli.error().contains("No contact found for reference: 0"));
    }

    @Test
    void updateChangesContactByIndex() {
        Path contactsFile = tmpDir.resolve("contacts.csv");
        TestCli cli = new TestCli();
        cli.app.run(new String[] {"--file", contactsFile.toString(), "add", "--first-name", "Jane"});

        int updateCode =
                cli.app.run(
                        new String[] {
                            "--file",
                            contactsFile.toString(),
                            "update",
                            "1",
                            "--last-name",
                            "Doe",
                            "--email",
                            "jane@example.com"
                        });
        int showCode = cli.app.run(new String[] {"--file", contactsFile.toString(), "show", "1"});

        assertEquals(0, updateCode);
        assertEquals(0, showCode);
        assertTrue(cli.output().contains("Contact updated:"));
        assertTrue(cli.output().contains("Name: Jane Doe"));
        assertTrue(cli.output().contains("Emails: jane@example.com"));
    }

    @Test
    void exportWritesContactsToDestinationCsv() {
        Path contactsFile = tmpDir.resolve("contacts.csv");
        Path exportFile = tmpDir.resolve("export").resolve("contacts.csv");
        TestCli cli = new TestCli();
        cli.app.run(new String[] {"--file", contactsFile.toString(), "add", "--first-name", "Jane"});

        int exportCode = cli.app.run(new String[] {"--file", contactsFile.toString(), "export", exportFile.toString()});

        assertEquals(0, exportCode);
        assertTrue(cli.output().contains("Exported 1 contacts."));
        assertTrue(Files.exists(exportFile));
    }

    @Test
    void importAppendsContactsFromSourceCsv() {
        Path sourceFile = tmpDir.resolve("source.csv");
        Path destinationFile = tmpDir.resolve("destination.csv");
        TestCli source = new TestCli();
        TestCli destination = new TestCli();
        source.app.run(new String[] {"--file", sourceFile.toString(), "add", "--first-name", "Imported"});
        destination.app.run(new String[] {"--file", destinationFile.toString(), "add", "--first-name", "Existing"});

        int importCode = destination.app.run(new String[] {"--file", destinationFile.toString(), "import", sourceFile.toString()});
        int listCode = destination.app.run(new String[] {"--file", destinationFile.toString(), "list"});

        assertEquals(0, importCode);
        assertEquals(0, listCode);
        assertTrue(destination.output().contains("Imported 1 contacts."));
        assertTrue(destination.output().contains("1. Existing ["));
        assertTrue(destination.output().contains("2. Imported ["));
    }

    /** Captures CLI output streams while exercising the application in-process. */
    private static class TestCli {
        private final ByteArrayOutputStream out = new ByteArrayOutputStream();
        private final ByteArrayOutputStream err = new ByteArrayOutputStream();
        private final CliApplication app =
                new CliApplication(
                        new PrintStream(out, true, StandardCharsets.UTF_8),
                        new PrintStream(err, true, StandardCharsets.UTF_8));

        private String output() {
            return out.toString(StandardCharsets.UTF_8);
        }

        private String error() {
            return err.toString(StandardCharsets.UTF_8);
        }
    }
}
