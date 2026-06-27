import { z } from 'zod';

export const schema = z.object({
  id: z.string(),
  firstName: z.string(),
  lastName: z.string(),
  company: z.string(),
  jobTitle: z.string(),
  phone: z.string(),
  email: z.string(),
  street: z.string(),
  city: z.string(),
  state: z.string(),
  postalCode: z.string(),
  country: z.string(),
  birthday: z.string(),
  notes: z.string(),
});

export type Contact = z.infer<typeof schema>;

export function contactName(contact: Contact) {
  return [contact.firstName, contact.lastName].filter(Boolean).join(' ') || 'Unnamed';
}

export function contactAddress(contact: Contact) {
  return [
    contact.street,
    contact.city,
    contact.state,
    contact.postalCode,
    contact.country,
  ]
    .filter(Boolean)
    .join(', ');
}

export function formatDate(value: string) {
  if (!value) {
    return '';
  }

  const [year, month, day] = value.split('-').map(Number);
  const date = year && month && day ? new Date(year, month - 1, day) : new Date(value);

  return date.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  });
}
