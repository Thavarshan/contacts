package com.contacts.cli;

import com.contacts.Address;
import com.contacts.Contact;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Matches contacts against text queries.
 *
 * <p>The matcher performs case-insensitive substring matching across all fields that a user is
 * likely to remember: names, organization fields, contact methods, notes, dates, and address parts.
 */
public class ContactMatcher {

    /**
     * Return whether a contact contains the query in any searchable field.
     *
     * @param contact contact to inspect
     * @param query search query
     * @return {@code true} when the contact matches
     */
    public boolean matches(Contact contact, String query) {
        if (query == null || query.isBlank()) {
            return false;
        }

        String normalizedQuery = normalize(query);
        return contains(contact.getFirstName(), normalizedQuery)
                || contains(contact.getMiddleName(), normalizedQuery)
                || contains(contact.getLastName(), normalizedQuery)
                || contains(contact.getNickName(), normalizedQuery)
                || contains(contact.getCompany(), normalizedQuery)
                || contains(contact.getJobTitle(), normalizedQuery)
                || contains(contact.getDepartment(), normalizedQuery)
                || contains(contact.getNotes(), normalizedQuery)
                || containsDate(contact.getBirthday(), normalizedQuery)
                || containsAny(contact.getPhoneNumbers(), normalizedQuery)
                || containsAny(contact.getEmailAddresses(), normalizedQuery)
                || containsAny(contact.getUrlAddresses(), normalizedQuery)
                || containsAny(contact.getSocialProfiles(), normalizedQuery)
                || containsAny(contact.getInstantMessageHandles(), normalizedQuery)
                || containsAnyDate(contact.getDates(), normalizedQuery)
                || containsAnyAddress(contact.getAddresses(), normalizedQuery);
    }

    /**
     * Search a list of strings for the normalized query.
     *
     * @param values values to inspect
     * @param query normalized query
     * @return {@code true} when any value contains the query
     */
    private static boolean containsAny(List<String> values, String query) {
        for (String value : values) {
            if (contains(value, query)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Search a list of dates using their ISO string representation.
     *
     * @param values date values
     * @param query normalized query
     * @return {@code true} when any date contains the query
     */
    private static boolean containsAnyDate(List<LocalDate> values, String query) {
        for (LocalDate value : values) {
            if (containsDate(value, query)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Search all address parts for the normalized query.
     *
     * @param addresses addresses to inspect
     * @param query normalized query
     * @return {@code true} when any address part contains the query
     */
    private static boolean containsAnyAddress(List<Address> addresses, String query) {
        for (Address address : addresses) {
            if (contains(address.getStreet(), query)
                    || contains(address.getCity(), query)
                    || contains(address.getState(), query)
                    || contains(address.getPostalCode(), query)
                    || contains(address.getCountry(), query)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Search a single date using its ISO string representation.
     *
     * @param date date to inspect
     * @param query normalized query
     * @return {@code true} when the date contains the query
     */
    private static boolean containsDate(LocalDate date, String query) {
        return date != null && contains(date.toString(), query);
    }

    /**
     * Search one nullable string value for a normalized query.
     *
     * @param value value to inspect
     * @param query normalized query
     * @return {@code true} when the value contains the query
     */
    private static boolean contains(String value, String query) {
        return value != null && normalize(value).contains(query);
    }

    /**
     * Normalize text for case-insensitive matching.
     *
     * @param value source text
     * @return lowercase text using locale-independent rules
     */
    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT);
    }
}
