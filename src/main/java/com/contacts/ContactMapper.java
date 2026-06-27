package com.contacts;

/** Maps contacts to and from storage-specific row formats. */
public interface ContactMapper<T> {

    /**
     * Convert a storage row into a contact.
     *
     * @param row source row
     * @return mapped contact
     */
    Contact fromRow(T row);

    /**
     * Convert a contact into a storage row.
     *
     * @param contact source contact
     * @return mapped row
     */
    T toRow(Contact contact);
}
