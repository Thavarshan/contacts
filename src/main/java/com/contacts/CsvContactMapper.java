package com.contacts;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Maps contacts to and from the application's CSV schema.
 *
 * <p>The mapper keeps the CSV format isolated from the domain model. Collection fields are stored
 * in one CSV cell by URL-encoding each element and joining the encoded values with a pipe
 * character. Addresses are encoded as nested values inside that list using a tilde character
 * between address parts.
 */
public class CsvContactMapper implements ContactMapper<String[]> {
    private static final String[] HEADER = {
        "id",
        "firstName",
        "middleName",
        "lastName",
        "nickName",
        "company",
        "jobTitle",
        "department",
        "phoneNumbers",
        "emailAddresses",
        "urlAddresses",
        "addresses",
        "birthday",
        "dates",
        "socialProfiles",
        "instantMessageHandles",
        "notes"
    };
    private static final String LIST_DELIMITER = "|";
    private static final String ADDRESS_PART_DELIMITER = "~";

    @Override
    public Contact fromRow(String[] row) {
        Objects.requireNonNull(row, "row must not be null");
        if (row.length != HEADER.length) {
            throw new IllegalArgumentException(
                    "Expected " + HEADER.length + " CSV columns but got " + row.length);
        }

        Contact contact = new Contact().setId(value(row, 0));
        return contact
                .setFirstName(value(row, 1))
                .setMiddleName(value(row, 2))
                .setLastName(value(row, 3))
                .setNickName(value(row, 4))
                .setCompany(value(row, 5))
                .setJobTitle(value(row, 6))
                .setDepartment(value(row, 7))
                .setPhoneNumbers(splitList(value(row, 8), Function.identity()))
                .setEmailAddresses(splitList(value(row, 9), Function.identity()))
                .setUrlAddresses(splitList(value(row, 10), Function.identity()))
                .setAddresses(splitList(value(row, 11), this::decodeAddress))
                .setBirthday(parseDate(value(row, 12)))
                .setDates(splitList(value(row, 13), CsvContactMapper::parseDate))
                .setSocialProfiles(splitList(value(row, 14), Function.identity()))
                .setInstantMessageHandles(splitList(value(row, 15), Function.identity()))
                .setNotes(value(row, 16));
    }

    @Override
    public String[] toRow(Contact contact) {
        Objects.requireNonNull(contact, "contact must not be null");
        return new String[] {
            text(contact.getId()),
            text(contact.getFirstName()),
            text(contact.getMiddleName()),
            text(contact.getLastName()),
            text(contact.getNickName()),
            text(contact.getCompany()),
            text(contact.getJobTitle()),
            text(contact.getDepartment()),
            joinList(contact.getPhoneNumbers(), Function.identity()),
            joinList(contact.getEmailAddresses(), Function.identity()),
            joinList(contact.getUrlAddresses(), Function.identity()),
            joinList(contact.getAddresses(), this::encodeAddress),
            formatDate(contact.getBirthday()),
            joinList(contact.getDates(), CsvContactMapper::formatDate),
            joinList(contact.getSocialProfiles(), Function.identity()),
            joinList(contact.getInstantMessageHandles(), Function.identity()),
            text(contact.getNotes())
        };
    }

    /**
     * Return whether a row is the contact CSV header.
     *
     * @param row row to test
     * @return {@code true} when the row matches the header
     */
    public boolean isHeader(String[] row) {
        return Arrays.equals(HEADER, row);
    }

    /**
     * Return the ordered CSV header used by the contact CSV schema.
     *
     * @return CSV header row
     */
    public static String[] header() {
        return HEADER.clone();
    }

    /**
     * Read a nullable value from a row.
     *
     * @param row CSV row
     * @param index cell index
     * @return cell value, or {@code null}
     */
    private static String value(String[] row, int index) {
        if (index >= row.length || row[index] == null || row[index].isBlank()) {
            return null;
        }
        return row[index];
    }

    /**
     * Convert a nullable string into a CSV-safe blank value.
     *
     * @param value source value
     * @return original value or an empty string
     */
    private static String text(String value) {
        return value == null ? "" : value;
    }

    /**
     * Parse an ISO-8601 date from a CSV cell.
     *
     * @param value CSV cell value
     * @return parsed date, or {@code null} for blank cells
     */
    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Invalid ISO date: " + value, exception);
        }
    }

    /**
     * Format an ISO-8601 date for CSV storage.
     *
     * @param date date to format
     * @return formatted date, or an empty string
     */
    private static String formatDate(LocalDate date) {
        return date == null ? "" : date.toString();
    }

    /**
     * Decode a URL-encoded, delimiter-separated list from one CSV cell.
     *
     * @param value encoded CSV cell
     * @param decoder value decoder
     * @return decoded list values
     * @param <T> decoded value type
     */
    private static <T> List<T> splitList(String value, Function<String, T> decoder) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        List<T> values = new ArrayList<>();
        for (String encodedItem : value.split("\\" + LIST_DELIMITER, -1)) {
            if (!encodedItem.isBlank()) {
                values.add(decoder.apply(decode(encodedItem)));
            }
        }
        return values;
    }

    /**
     * Encode a list into one CSV cell while preserving delimiter characters inside values.
     *
     * @param values source values
     * @param encoder value encoder
     * @return encoded CSV cell
     * @param <T> source value type
     */
    private static <T> String joinList(List<T> values, Function<T, String> encoder) {
        if (values == null || values.isEmpty()) {
            return "";
        }

        List<String> encodedValues = new ArrayList<>();
        for (T value : values) {
            if (value != null) {
                encodedValues.add(encode(encoder.apply(value)));
            }
        }
        return String.join(LIST_DELIMITER, encodedValues);
    }

    /**
     * Encode an address into a compact nested string used inside the address list cell.
     *
     * @param address source address
     * @return encoded address value
     */
    private String encodeAddress(Address address) {
        return String.join(
                ADDRESS_PART_DELIMITER,
                encode(text(address.getStreet())),
                encode(text(address.getCity())),
                encode(text(address.getState())),
                encode(text(address.getPostalCode())),
                encode(text(address.getCountry())));
    }

    /**
     * Decode a nested address value from the address list cell.
     *
     * @param value encoded address value
     * @return decoded address
     */
    private Address decodeAddress(String value) {
        String[] parts = value.split(ADDRESS_PART_DELIMITER, -1);
        return new Address()
                .setStreet(decodedPart(parts, 0))
                .setCity(decodedPart(parts, 1))
                .setState(decodedPart(parts, 2))
                .setPostalCode(decodedPart(parts, 3))
                .setCountry(decodedPart(parts, 4));
    }

    /**
     * Return a nullable address part from a split address row.
     *
     * @param parts split address parts
     * @param index requested part index
     * @return decoded part, or {@code null}
     */
    private static String decodedPart(String[] parts, int index) {
        return index >= parts.length || parts[index].isBlank() ? null : decode(parts[index]);
    }

    /**
     * URL-encode a value so list and address delimiters cannot corrupt the CSV schema.
     *
     * @param value source value
     * @return encoded value
     */
    private static String encode(String value) {
        return URLEncoder.encode(text(value), StandardCharsets.UTF_8);
    }

    /**
     * Decode a URL-encoded value from the CSV schema.
     *
     * @param value encoded value
     * @return decoded value
     */
    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
