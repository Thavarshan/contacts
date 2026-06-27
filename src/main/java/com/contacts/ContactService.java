package com.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

/** Application service for contact use cases. */
public class ContactService {
    private final ContactRepository repository;

    /**
     * Create a contact service.
     *
     * @param repository persistence dependency
     */
    public ContactService(ContactRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    /**
     * Return all contacts.
     *
     * @return all contacts
     * @throws IOException when contacts cannot be loaded
     */
    public List<Contact> getAllContacts() throws IOException {
        return repository.findAll();
    }

    /**
     * Find one contact by stable identifier.
     *
     * @param id contact identifier
     * @return matching contact, if present
     * @throws IOException when contacts cannot be loaded
     */
    public Optional<Contact> findById(String id) throws IOException {
        Objects.requireNonNull(id, "id must not be null");
        return repository.findAll().stream().filter(contact -> id.equals(contact.getId())).findFirst();
    }

    /**
     * Add a contact.
     *
     * @param contact contact to add
     * @throws IOException when the contact cannot be saved
     */
    public void addContact(Contact contact) throws IOException {
        repository.save(Objects.requireNonNull(contact, "contact must not be null"));
    }

    /**
     * Update a contact by stable identifier.
     *
     * @param id contact identifier
     * @param updater update function
     * @return {@code true} when a contact was updated
     * @throws IOException when contacts cannot be loaded or saved
     */
    public boolean updateContact(String id, UnaryOperator<Contact> updater) throws IOException {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(updater, "updater must not be null");
        List<Contact> contacts = new ArrayList<>(repository.findAll());
        for (int index = 0; index < contacts.size(); index++) {
            Contact contact = contacts.get(index);
            if (id.equals(contact.getId())) {
                Contact updated = Objects.requireNonNull(updater.apply(contact), "updated contact must not be null");
                updated.setId(id);
                contacts.set(index, updated);
                repository.saveAll(contacts);
                return true;
            }
        }
        return false;
    }

    /**
     * Delete a contact by stable identifier.
     *
     * @param id contact identifier
     * @return {@code true} when a contact was deleted
     * @throws IOException when contacts cannot be loaded or saved
     */
    public boolean deleteById(String id) throws IOException {
        Objects.requireNonNull(id, "id must not be null");
        List<Contact> contacts = new ArrayList<>(repository.findAll());
        boolean removed = contacts.removeIf(contact -> id.equals(contact.getId()));
        if (removed) {
            repository.saveAll(contacts);
        }
        return removed;
    }

    /**
     * Replace all contacts.
     *
     * @param contacts contacts to persist
     * @throws IOException when contacts cannot be saved
     */
    public void replaceContacts(List<Contact> contacts) throws IOException {
        repository.saveAll(List.copyOf(Objects.requireNonNull(contacts, "contacts must not be null")));
    }

    /**
     * Append imported contacts to the existing store.
     *
     * @param contacts contacts to append
     * @throws IOException when contacts cannot be saved
     */
    public void importContacts(List<Contact> contacts) throws IOException {
        Objects.requireNonNull(contacts, "contacts must not be null");
        List<Contact> merged = new ArrayList<>(repository.findAll());
        merged.addAll(contacts);
        repository.saveAll(merged);
    }
}
