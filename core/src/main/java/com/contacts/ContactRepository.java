package com.contacts;

import java.io.IOException;
import java.util.List;

/** Persistence contract for contacts. */
public interface ContactRepository {

    /**
     * Find all persisted contacts.
     *
     * @return contacts from the backing store
     * @throws IOException when contacts cannot be loaded
     */
    List<Contact> findAll() throws IOException;

    /**
     * Replace all persisted contacts.
     *
     * @param contacts contacts to persist
     * @throws IOException when contacts cannot be saved
     */
    void saveAll(List<Contact> contacts) throws IOException;

    /**
     * Append a contact to the persisted collection by loading the current contacts and replacing the
     * backing store.
     *
     * @param contact contact to save
     * @throws IOException when contacts cannot be loaded or saved
     */
    default void save(Contact contact) throws IOException {
        List<Contact> contacts = new java.util.ArrayList<>(findAll());
        contacts.add(contact);
        saveAll(contacts);
    }
}
