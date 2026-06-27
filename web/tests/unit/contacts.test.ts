import { describe, expect, it } from 'vitest';

import {
  contactAddress,
  contactName,
  formatDate,
  schema,
  type Contact,
} from '@/components/blocks/contacts/data-table';

const contact: Contact = {
  id: '42',
  firstName: 'Sarah',
  lastName: 'Chen',
  company: 'Northstar Labs',
  jobTitle: 'Product Lead',
  phone: '+1 (415) 555-0132',
  email: 'sarah.chen@example.com',
  street: '120 Market Street',
  city: 'San Francisco',
  state: 'CA',
  postalCode: '94105',
  country: 'United States',
  birthday: '1988-04-12',
  notes: 'Prefers email for follow-ups.',
};

describe('contact helpers', () => {
  it('formats a contact display name', () => {
    expect(contactName(contact)).toBe('Sarah Chen');
    expect(contactName({ ...contact, lastName: '' })).toBe('Sarah');
    expect(contactName({ ...contact, firstName: '' })).toBe('Chen');
    expect(contactName({ ...contact, firstName: '', lastName: '' })).toBe('Unnamed');
  });

  it('formats a contact address without empty segments', () => {
    expect(contactAddress(contact)).toBe(
      '120 Market Street, San Francisco, CA, 94105, United States',
    );
    expect(contactAddress({ ...contact, street: '', postalCode: '' })).toBe(
      'San Francisco, CA, United States',
    );
    expect(
      contactAddress({
        ...contact,
        street: '',
        city: '',
        state: '',
        postalCode: '',
        country: '',
      }),
    ).toBe('');
  });

  it('formats date input values as local calendar dates', () => {
    expect(formatDate('1988-04-12')).toBe('Apr 12, 1988');
    expect(formatDate('')).toBe('');
    expect(formatDate('1988/04/12')).toBe('Apr 12, 1988');
  });
});

describe('contact schema', () => {
  it('accepts a complete contact record', () => {
    expect(schema.parse(contact)).toEqual(contact);
  });

  it('rejects records missing required contact fields', () => {
    expect(() => schema.parse({ ...contact, email: undefined })).toThrow();
  });
});
