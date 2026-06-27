package com.contacts.api;

import com.contacts.Address;
import com.contacts.Contact;
import java.time.LocalDate;
import java.util.List;

/** JSON representation used by the web dashboard. */
public record ContactPayload(
        String id,
        String firstName,
        String lastName,
        String company,
        String jobTitle,
        String phone,
        String email,
        String street,
        String city,
        String state,
        String postalCode,
        String country,
        String birthday,
        String notes) {

    /**
     * Create a payload from a domain contact.
     *
     * @param contact contact to convert
     * @return JSON payload for the contact
     */
    public static ContactPayload from(Contact contact) {
        Address address = contact.getAddresses().isEmpty() ? new Address() : contact.getAddresses().get(0);
        return new ContactPayload(
                text(contact.getId()),
                text(contact.getFirstName()),
                text(contact.getLastName()),
                text(contact.getCompany()),
                text(contact.getJobTitle()),
                first(contact.getPhoneNumbers()),
                first(contact.getEmailAddresses()),
                text(address.getStreet()),
                text(address.getCity()),
                text(address.getState()),
                text(address.getPostalCode()),
                text(address.getCountry()),
                contact.getBirthday() == null ? "" : contact.getBirthday().toString(),
                text(contact.getNotes()));
    }

    /**
     * Convert this payload into a domain contact.
     *
     * @return domain contact
     */
    public Contact toContact() {
        Contact contact =
                new Contact()
                        .setFirstName(blankToNull(firstName))
                        .setLastName(blankToNull(lastName))
                        .setCompany(blankToNull(company))
                        .setJobTitle(blankToNull(jobTitle))
                        .setPhoneNumbers(phone == null || phone.isBlank() ? List.of() : List.of(phone))
                        .setEmailAddresses(email == null || email.isBlank() ? List.of() : List.of(email))
                        .setAddresses(address())
                        .setBirthday(parseDate(birthday))
                        .setNotes(blankToNull(notes));

        if (id != null && !id.isBlank()) {
            contact.setId(id);
        }

        return contact;
    }

    private List<Address> address() {
        if (isBlank(street) && isBlank(city) && isBlank(state) && isBlank(postalCode) && isBlank(country)) {
            return List.of();
        }
        return List.of(
                new Address()
                        .setStreet(blankToNull(street))
                        .setCity(blankToNull(city))
                        .setState(blankToNull(state))
                        .setPostalCode(blankToNull(postalCode))
                        .setCountry(blankToNull(country)));
    }

    private static LocalDate parseDate(String value) {
        return value == null || value.isBlank() ? null : LocalDate.parse(value);
    }

    private static String text(String value) {
        return value == null ? "" : value;
    }

    private static String first(List<String> values) {
        return values.isEmpty() ? "" : values.get(0);
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
