package com.contacts.cli;

import com.contacts.Contact;

/**
 * Formats contacts for terminal output.
 *
 * <p>The formatter intentionally prints a compact single-line summary for list and search results.
 * More detailed multi-line output is handled by the Picocli {@code show} subcommand.
 */
public class ContactFormatter {

    /**
     * Format a contact as one concise line.
     *
     * @param index one-based display index
     * @param contact contact to format
     * @return display line
     */
    public String format(int index, Contact contact) {
        String name = join(contact.getFirstName(), contact.getMiddleName(), contact.getLastName());
        if (name.isBlank()) {
            name = "(unnamed)";
        }

        String email = contact.getEmailAddresses().isEmpty() ? "" : contact.getEmailAddresses().get(0);
        String phone = contact.getPhoneNumbers().isEmpty() ? "" : contact.getPhoneNumbers().get(0);
        String company = contact.getCompany() == null ? "" : contact.getCompany();

    return "%d. %s [%s]%s%s%s"
            .formatted(
                    index,
                    name,
                    contact.getId(),
                    email.isBlank() ? "" : " <" + email + ">",
                    phone.isBlank() ? "" : " " + phone,
                    company.isBlank() ? "" : " - " + company);
    }

    /**
     * Join name parts while collapsing missing middle names and extra spaces.
     *
     * @param first first name
     * @param middle middle name
     * @param last last name
     * @return normalized display name
     */
    private static String join(String first, String middle, String last) {
        return String.join(" ", nonNull(first), nonNull(middle), nonNull(last))
                .trim()
                .replaceAll(" +", " ");
    }

    /**
     * Convert nullable name parts into empty strings for joining.
     *
     * @param value source value
     * @return source value or empty string
     */
    private static String nonNull(String value) {
        return value == null ? "" : value;
    }
}
