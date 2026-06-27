package com.contacts.cli;

import com.contacts.Contact;
import com.contacts.ContactService;
import com.contacts.CsvContactMapper;
import com.contacts.CsvContactRepository;
import com.filemanager.csv.CsvReader;
import com.filemanager.csv.CsvWriter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/** Contacts command-line application. */
@Command(
        name = "contacts",
        mixinStandardHelpOptions = true,
        version = "contacts 1.0.0-SNAPSHOT",
        description = "Manage CSV-backed contacts.",
        subcommands = {
            CliApplication.ListCommand.class,
            CliApplication.AddCommand.class,
            CliApplication.SearchCommand.class,
            CliApplication.ShowCommand.class,
            CliApplication.UpdateCommand.class,
            CliApplication.DeleteCommand.class,
            CliApplication.ClearCommand.class,
            CliApplication.ImportCommand.class,
            CliApplication.ExportCommand.class
        })
public class CliApplication implements Callable<Integer> {
    private final PrintStream out;
    private final PrintStream err;

    @Option(names = "--file", description = "Contacts CSV file.", defaultValue = "contacts.csv")
    private Path contactsFile;

    /**
     * Create a CLI application.
     *
     * @param out standard output stream
     * @param err standard error stream
     */
    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "The CLI application intentionally writes to the provided process streams.")
    public CliApplication(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    /**
     * Run the CLI application.
     *
     * @param args raw CLI arguments
     * @return process exit code
     */
    public int run(String[] args) {
        CommandLine commandLine = new CommandLine(this);
        commandLine.setOut(new PrintWriter(out, true, StandardCharsets.UTF_8));
        commandLine.setErr(new PrintWriter(err, true, StandardCharsets.UTF_8));
        commandLine.setExecutionExceptionHandler(this::handleExecutionException);
        return commandLine.execute(args);
    }

    @Override
    public Integer call() {
        new CommandLine(this).usage(out);
        return 0;
    }

    private int handleExecutionException(Exception exception, CommandLine commandLine, CommandLine.ParseResult ignored) {
        Throwable cause = exception.getCause() == null ? exception : exception.getCause();
        if (cause instanceof IOException) {
            err.println("I/O error: " + cause.getMessage());
            return 1;
        }
        err.println(cause.getMessage());
        commandLine.usage(err);
        return 2;
    }

    private ContactService service() throws IOException {
        return service(contactsFile);
    }

    private static ContactService service(Path contactsPath) throws IOException {
        CsvContactMapper mapper = new CsvContactMapper();
        return new ContactService(
                new CsvContactRepository(
                        new CsvReader(contactsPath),
                        new CsvWriter(contactsPath),
                        mapper));
    }

    private Contact requireContact(String reference) throws IOException {
        List<Contact> contacts = service().getAllContacts();
        Optional<Contact> byId = contacts.stream().filter(contact -> reference.equals(contact.getId())).findFirst();
        if (byId.isPresent()) {
            return byId.get();
        }
        try {
            int index = Integer.parseInt(reference);
            if (index > 0 && index <= contacts.size()) {
                return contacts.get(index - 1);
            }
        } catch (NumberFormatException ignored) {
            // Non-numeric references are treated as stable IDs only.
        }
        throw new IllegalArgumentException("No contact found for reference: " + reference);
    }

    private static Contact applyOptions(Contact contact, ContactOptions options, boolean overwriteLists) {
        if (options.firstName != null) {
            contact.setFirstName(options.firstName);
        }
        if (options.middleName != null) {
            contact.setMiddleName(options.middleName);
        }
        if (options.lastName != null) {
            contact.setLastName(options.lastName);
        }
        if (options.nickName != null) {
            contact.setNickName(options.nickName);
        }
        if (options.company != null) {
            contact.setCompany(options.company);
        }
        if (options.jobTitle != null) {
            contact.setJobTitle(options.jobTitle);
        }
        if (options.department != null) {
            contact.setDepartment(options.department);
        }
        if (options.phones != null || overwriteLists) {
            contact.setPhoneNumbers(options.phones);
        }
        if (options.emails != null || overwriteLists) {
            contact.setEmailAddresses(options.emails);
        }
        if (options.urls != null || overwriteLists) {
            contact.setUrlAddresses(options.urls);
        }
        if (options.birthday != null) {
            contact.setBirthday(LocalDate.parse(options.birthday));
        }
        if (options.socialProfiles != null || overwriteLists) {
            contact.setSocialProfiles(options.socialProfiles);
        }
        if (options.imHandles != null || overwriteLists) {
            contact.setInstantMessageHandles(options.imHandles);
        }
        if (options.notes != null) {
            contact.setNotes(options.notes);
        }
        return contact;
    }

    static class ContactOptions {
        @Option(names = "--first-name", description = "First name.")
        String firstName;

        @Option(names = "--middle-name", description = "Middle name.")
        String middleName;

        @Option(names = "--last-name", description = "Last name.")
        String lastName;

        @Option(names = "--nick-name", description = "Nickname.")
        String nickName;

        @Option(names = "--company", description = "Company.")
        String company;

        @Option(names = "--job-title", description = "Job title.")
        String jobTitle;

        @Option(names = "--department", description = "Department.")
        String department;

        @Option(names = "--phone", description = "Phone number. Repeatable.")
        List<String> phones;

        @Option(names = "--email", description = "Email address. Repeatable.")
        List<String> emails;

        @Option(names = "--url", description = "URL. Repeatable.")
        List<String> urls;

        @Option(names = "--birthday", description = "Birthday in yyyy-MM-dd format.")
        String birthday;

        @Option(names = "--social", description = "Social profile. Repeatable.")
        List<String> socialProfiles;

        @Option(names = "--im", description = "Instant message handle. Repeatable.")
        List<String> imHandles;

        @Option(names = "--notes", description = "Notes.")
        String notes;
    }

    @Command(name = "list", description = "List all contacts.")
    static class ListCommand implements Callable<Integer> {
        @CommandLine.ParentCommand CliApplication parent;

        @Override
        public Integer call() throws IOException {
            List<Contact> contacts = parent.service().getAllContacts();
            ContactFormatter formatter = new ContactFormatter();
            if (contacts.isEmpty()) {
                parent.out.println("No contacts found.");
                return 0;
            }
            for (int index = 0; index < contacts.size(); index++) {
                parent.out.println(formatter.format(index + 1, contacts.get(index)));
            }
            return 0;
        }
    }

    @Command(name = "add", description = "Add a contact.")
    static class AddCommand implements Callable<Integer> {
        @CommandLine.ParentCommand CliApplication parent;
        @CommandLine.Mixin ContactOptions options;

        @Override
        public Integer call() throws IOException {
            Contact contact = applyOptions(new Contact(), options, true);
            parent.service().addContact(contact);
            parent.out.println("Contact added: " + contact.getId());
            return 0;
        }
    }

    @Command(name = "search", description = "Search contacts.")
    static class SearchCommand implements Callable<Integer> {
        @CommandLine.ParentCommand CliApplication parent;

        @Parameters(index = "0", description = "Search query.")
        String query;

        @Override
        public Integer call() throws IOException {
            List<Contact> contacts = parent.service().getAllContacts();
            ContactMatcher matcher = new ContactMatcher();
            ContactFormatter formatter = new ContactFormatter();
            int matches = 0;
            for (int index = 0; index < contacts.size(); index++) {
                Contact contact = contacts.get(index);
                if (matcher.matches(contact, query)) {
                    parent.out.println(formatter.format(index + 1, contact));
                    matches++;
                }
            }
            if (matches == 0) {
                parent.out.println("No matching contacts found.");
            }
            return 0;
        }
    }

    @Command(name = "show", description = "Show one contact by ID or list index.")
    static class ShowCommand implements Callable<Integer> {
        @CommandLine.ParentCommand CliApplication parent;

        @Parameters(index = "0", description = "Contact ID or one-based list index.")
        String reference;

        @Override
        public Integer call() throws IOException {
            print(parent.requireContact(reference));
            return 0;
        }

        private void print(Contact contact) {
            parent.out.println("ID: " + contact.getId());
            print("Name", join(contact.getFirstName(), contact.getMiddleName(), contact.getLastName()));
            print("Nickname", contact.getNickName());
            print("Company", contact.getCompany());
            print("Job title", contact.getJobTitle());
            print("Department", contact.getDepartment());
            printList("Phones", contact.getPhoneNumbers());
            printList("Emails", contact.getEmailAddresses());
            printList("URLs", contact.getUrlAddresses());
            print("Birthday", contact.getBirthday() == null ? null : contact.getBirthday().toString());
            printList("Social profiles", contact.getSocialProfiles());
            printList("IM handles", contact.getInstantMessageHandles());
            print("Notes", contact.getNotes());
        }

        private void print(String label, String value) {
            if (value != null && !value.isBlank()) {
                parent.out.println(label + ": " + value);
            }
        }

        private void printList(String label, List<String> values) {
            if (!values.isEmpty()) {
                parent.out.println(label + ": " + String.join(", ", values));
            }
        }

        private static String join(String... values) {
            StringBuilder result = new StringBuilder();
            for (String value : values) {
                if (value != null && !value.isBlank()) {
                    if (!result.isEmpty()) {
                        result.append(" ");
                    }
                    result.append(value);
                }
            }
            return result.toString();
        }
    }

    @Command(name = "update", description = "Update a contact by ID or list index.")
    static class UpdateCommand implements Callable<Integer> {
        @CommandLine.ParentCommand CliApplication parent;

        @Parameters(index = "0", description = "Contact ID or one-based list index.")
        String reference;

        @CommandLine.Mixin ContactOptions options;

        @Override
        public Integer call() throws IOException {
            Contact existing = parent.requireContact(reference);
            boolean updated =
                    parent.service()
                            .updateContact(existing.getId(), contact -> applyOptions(contact, options, false));
            if (!updated) {
                throw new IllegalArgumentException("No contact found for reference: " + reference);
            }
            parent.out.println("Contact updated: " + existing.getId());
            return 0;
        }
    }

    @Command(name = "delete", description = "Delete a contact by ID or list index.")
    static class DeleteCommand implements Callable<Integer> {
        @CommandLine.ParentCommand CliApplication parent;

        @Parameters(index = "0", description = "Contact ID or one-based list index.")
        String reference;

        @Override
        public Integer call() throws IOException {
            Contact contact = parent.requireContact(reference);
            parent.service().deleteById(contact.getId());
            parent.out.println("Deleted " + contact.getId() + ".");
            return 0;
        }
    }

    @Command(name = "clear", description = "Delete all contacts.")
    static class ClearCommand implements Callable<Integer> {
        @CommandLine.ParentCommand CliApplication parent;

        @Override
        public Integer call() throws IOException {
            parent.service().replaceContacts(List.of());
            parent.out.println("All contacts cleared.");
            return 0;
        }
    }

    @Command(name = "import", description = "Import contacts from a CSV file.")
    static class ImportCommand implements Callable<Integer> {
        @CommandLine.ParentCommand CliApplication parent;

        @Parameters(index = "0", description = "Source CSV file.")
        Path source;

        @Option(names = "--replace", description = "Replace existing contacts instead of appending.")
        boolean replace;

        @Override
        public Integer call() throws IOException {
            List<Contact> imported = service(source).getAllContacts();
            if (replace) {
                parent.service().replaceContacts(imported);
            } else {
                parent.service().importContacts(imported);
            }
            parent.out.println("Imported " + imported.size() + " contacts.");
            return 0;
        }
    }

    @Command(name = "export", description = "Export contacts to a CSV file.")
    static class ExportCommand implements Callable<Integer> {
        @CommandLine.ParentCommand CliApplication parent;

        @Parameters(index = "0", description = "Destination CSV file.")
        Path destination;

        @Override
        public Integer call() throws IOException {
            Path parentPath = destination.getParent();
            if (parentPath != null) {
                Files.createDirectories(parentPath);
            }
            List<Contact> contacts = parent.service().getAllContacts();
            service(destination).replaceContacts(contacts);
            parent.out.println("Exported " + contacts.size() + " contacts.");
            return 0;
        }
    }
}
