package com.contacts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Data model for a contact entry. */
public class Contact extends Model {
    private String id = UUID.randomUUID().toString();

    // Name
    private String firstName;
    private String middleName;
    private String lastName;
    private String nickName;

    // Company
    private String company;
    private String jobTitle;
    private String department;

    // Contact details (multiple values per field)
    private List<String> phoneNumbers = List.of();
    private List<String> emailAddresses = List.of();
    private List<String> urlAddresses = List.of();

    // Addresses (multiple)
    private List<Address> addresses = List.of();

    // Important dates
    private LocalDate birthday;
    private List<LocalDate> dates = List.of(); // anniversaries, custom dates

    // Social & messaging
    private List<String> socialProfiles = List.of();
    private List<String> instantMessageHandles = List.of();

    // Other
    private String notes;

    /**
     * Returns the stable contact identifier.
     *
     * @return contact identifier
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the stable contact identifier.
     *
     * @param id identifier to set
     * @return this contact instance
     */
    public Contact setId(String id) {
        this.id = id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
        return this;
    }

    /**
     * Returns the contact's first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Sets the contact's first name.
     *
     * @param firstName the first name to set
     * @return this contact instance
     */
    public Contact setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Returns the contact's middle name.
     *
     * @return the middle name
     */
    public String getMiddleName() {
        return this.middleName;
    }

    /**
     * Sets the contact's middle name.
     *
     * @param middleName the middle name to set
     * @return this contact instance
     */
    public Contact setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    /**
     * Returns the contact's last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Sets the contact's last name.
     *
     * @param lastName the last name to set
     * @return this contact instance
     */
    public Contact setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Returns the contact's nickname.
     *
     * @return the nickname
     */
    public String getNickName() {
        return this.nickName;
    }

    /**
     * Sets the contact's nickname.
     *
     * @param nickName the nickname to set
     * @return this contact instance
     */
    public Contact setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    /**
     * Returns the contact's company.
     *
     * @return the company
     */
    public String getCompany() {
        return this.company;
    }

    /**
     * Sets the contact's company.
     *
     * @param company the company to set
     * @return this contact instance
     */
    public Contact setCompany(String company) {
        this.company = company;
        return this;
    }

    /**
     * Returns the contact's job title.
     *
     * @return the job title
     */
    public String getJobTitle() {
        return this.jobTitle;
    }

    /**
     * Sets the contact's job title.
     *
     * @param jobTitle the job title to set
     * @return this contact instance
     */
    public Contact setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    /**
     * Returns the contact's department.
     *
     * @return the department
     */
    public String getDepartment() {
        return this.department;
    }

    /**
     * Sets the contact's department.
     *
     * @param department the department to set
     * @return this contact instance
     */
    public Contact setDepartment(String department) {
        this.department = department;
        return this;
    }

    /**
     * Returns the contact's phone numbers.
     *
     * @return the phone numbers
     */
    public List<String> getPhoneNumbers() {
        return List.copyOf(this.phoneNumbers);
    }

    /**
     * Sets the contact's phone numbers.
     *
     * @param phoneNumbers the phone numbers to set
     * @return this contact instance
     */
    public Contact setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = copyOf(phoneNumbers);
        return this;
    }

    /**
     * Returns the contact's email addresses.
     *
     * @return the email addresses
     */
    public List<String> getEmailAddresses() {
        return List.copyOf(this.emailAddresses);
    }

    /**
     * Sets the contact's email addresses.
     *
     * @param emailAddresses the email addresses to set
     * @return this contact instance
     */
    public Contact setEmailAddresses(List<String> emailAddresses) {
        this.emailAddresses = copyOf(emailAddresses);
        return this;
    }

    /**
     * Returns the contact's URL addresses.
     *
     * @return the URL addresses
     */
    public List<String> getUrlAddresses() {
        return List.copyOf(this.urlAddresses);
    }

    /**
     * Sets the contact's URL addresses.
     *
     * @param urlAddresses the URL addresses to set
     * @return this contact instance
     */
    public Contact setUrlAddresses(List<String> urlAddresses) {
        this.urlAddresses = copyOf(urlAddresses);
        return this;
    }

    /**
     * Returns the contact's addresses.
     *
     * @return the addresses
     */
    public List<Address> getAddresses() {
        return List.copyOf(this.addresses);
    }

    /**
     * Sets the contact's addresses.
     *
     * @param addresses the addresses to set
     * @return this contact instance
     */
    public Contact setAddresses(List<Address> addresses) {
        this.addresses = copyOf(addresses);
        return this;
    }

    /**
     * Returns the contact's birthday.
     *
     * @return the birthday
     */
    public LocalDate getBirthday() {
        return this.birthday;
    }

    /**
     * Sets the contact's birthday.
     *
     * @param birthday the birthday to set
     * @return this contact instance
     */
    public Contact setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    /**
     * Returns the contact's important dates.
     *
     * @return the important dates
     */
    public List<LocalDate> getDates() {
        return List.copyOf(this.dates);
    }

    /**
     * Sets the contact's important dates.
     *
     * @param dates the dates to set
     * @return this contact instance
     */
    public Contact setDates(List<LocalDate> dates) {
        this.dates = copyOf(dates);
        return this;
    }

    /**
     * Returns the contact's social profiles.
     *
     * @return the social profiles
     */
    public List<String> getSocialProfiles() {
        return List.copyOf(this.socialProfiles);
    }

    /**
     * Sets the contact's social profiles.
     *
     * @param socialProfiles the profiles to set
     * @return this contact instance
     */
    public Contact setSocialProfiles(List<String> socialProfiles) {
        this.socialProfiles = copyOf(socialProfiles);
        return this;
    }

    /**
     * Returns the contact's instant message handles.
     *
     * @return the instant message handles
     */
    public List<String> getInstantMessageHandles() {
        return List.copyOf(this.instantMessageHandles);
    }

    /**
     * Sets the contact's instant message handles.
     *
     * @param instantMessageHandles the handles to set
     * @return this contact instance
     */
    public Contact setInstantMessageHandles(List<String> instantMessageHandles) {
        this.instantMessageHandles = copyOf(instantMessageHandles);
        return this;
    }

    /**
     * Return an immutable defensive copy for collection fields.
     *
     * @param values source values
     * @return immutable copy, or an empty list
     * @param <T> collection element type
     */
    private static <T> List<T> copyOf(List<T> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return List.copyOf(new ArrayList<>(values));
    }

    /**
     * Returns the contact's notes.
     *
     * @return the notes
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Sets the contact's notes.
     *
     * @param notes the notes to set
     * @return this contact instance
     */
    public Contact setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) o;
        return Objects.equals(firstName, other.firstName)
                && Objects.equals(middleName, other.middleName)
                && Objects.equals(lastName, other.lastName)
                && Objects.equals(nickName, other.nickName)
                && Objects.equals(company, other.company)
                && Objects.equals(jobTitle, other.jobTitle)
                && Objects.equals(department, other.department)
                && Objects.equals(phoneNumbers, other.phoneNumbers)
                && Objects.equals(emailAddresses, other.emailAddresses)
                && Objects.equals(urlAddresses, other.urlAddresses)
                && Objects.equals(addresses, other.addresses)
                && Objects.equals(birthday, other.birthday)
                && Objects.equals(dates, other.dates)
                && Objects.equals(socialProfiles, other.socialProfiles)
                && Objects.equals(instantMessageHandles, other.instantMessageHandles)
                && Objects.equals(notes, other.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                firstName,
                middleName,
                lastName,
                nickName,
                company,
                jobTitle,
                department,
                phoneNumbers,
                emailAddresses,
                urlAddresses,
                addresses,
                birthday,
                dates,
                socialProfiles,
                instantMessageHandles,
                notes);
    }

    @Override
    public String toString() {
        return "Contact{"
                + "id='"
                + id
                + '\''
                + ", firstName='"
                + firstName
                + '\''
                + ", middleName='"
                + middleName
                + '\''
                + ", lastName='"
                + lastName
                + '\''
                + ", nickName='"
                + nickName
                + '\''
                + ", company='"
                + company
                + '\''
                + ", jobTitle='"
                + jobTitle
                + '\''
                + ", department='"
                + department
                + '\''
                + ", phoneNumbers="
                + phoneNumbers
                + ", emailAddresses="
                + emailAddresses
                + ", urlAddresses="
                + urlAddresses
                + ", addresses="
                + addresses
                + ", birthday="
                + birthday
                + ", dates="
                + dates
                + ", socialProfiles="
                + socialProfiles
                + ", instantMessageHandles="
                + instantMessageHandles
                + ", notes='"
                + notes
                + '\''
                + '}';
    }
}
