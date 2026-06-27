'use client';

import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import type { Contact } from '@/components/blocks/contacts/data-table';

export type ContactFormValues = Omit<Contact, 'id'>;

interface ContactFormDialogProps {
  contact: Contact | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSave: (contact: ContactFormValues, id?: string) => void | Promise<void>;
}

const emptyForm: ContactFormValues = {
  firstName: '',
  lastName: '',
  company: '',
  jobTitle: '',
  phone: '',
  email: '',
  street: '',
  city: '',
  state: '',
  postalCode: '',
  country: '',
  birthday: '',
  notes: '',
};

export function ContactFormDialog({
  contact,
  open,
  onOpenChange,
  onSave,
}: ContactFormDialogProps) {
  const isEditing = Boolean(contact);
  const formValues = contact ?? emptyForm;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-2xl">
        <DialogHeader>
          <DialogTitle>{isEditing ? 'Edit contact' : 'Add contact'}</DialogTitle>
          <DialogDescription>
            Store the same core details you would expect in a phone contact.
          </DialogDescription>
        </DialogHeader>
        <form
          key={`${contact?.id ?? 'new'}-${open.toString()}`}
          className="grid gap-4"
          onSubmit={(event) => {
            event.preventDefault();
            const formData = new FormData(event.currentTarget);
            void onSave(
              {
                firstName: readFormString(formData, 'firstName'),
                lastName: readFormString(formData, 'lastName'),
                company: readFormString(formData, 'company'),
                jobTitle: readFormString(formData, 'jobTitle'),
                phone: readFormString(formData, 'phone'),
                email: readFormString(formData, 'email'),
                street: readFormString(formData, 'street'),
                city: readFormString(formData, 'city'),
                state: readFormString(formData, 'state'),
                postalCode: readFormString(formData, 'postalCode'),
                country: readFormString(formData, 'country'),
                birthday: readFormString(formData, 'birthday'),
                notes: readFormString(formData, 'notes'),
              },
              contact?.id,
            );
          }}
        >
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="contact-first-name">First name</Label>
              <Input
                id="contact-first-name"
                name="firstName"
                defaultValue={formValues.firstName}
                required
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="contact-last-name">Last name</Label>
              <Input
                id="contact-last-name"
                name="lastName"
                defaultValue={formValues.lastName}
              />
            </div>
          </div>
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="contact-company">Company</Label>
              <Input
                id="contact-company"
                name="company"
                defaultValue={formValues.company}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="contact-job-title">Job title</Label>
              <Input
                id="contact-job-title"
                name="jobTitle"
                defaultValue={formValues.jobTitle}
              />
            </div>
          </div>
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="contact-phone">Phone</Label>
              <Input id="contact-phone" name="phone" defaultValue={formValues.phone} />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="contact-email">Email</Label>
              <Input
                id="contact-email"
                name="email"
                type="email"
                defaultValue={formValues.email}
              />
            </div>
          </div>
          <div className="grid gap-2">
            <Label htmlFor="contact-street">Street</Label>
            <Input id="contact-street" name="street" defaultValue={formValues.street} />
          </div>
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-4">
            <div className="grid gap-2 sm:col-span-2">
              <Label htmlFor="contact-city">City</Label>
              <Input id="contact-city" name="city" defaultValue={formValues.city} />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="contact-state">State</Label>
              <Input id="contact-state" name="state" defaultValue={formValues.state} />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="contact-postal-code">Postal code</Label>
              <Input
                id="contact-postal-code"
                name="postalCode"
                defaultValue={formValues.postalCode}
              />
            </div>
          </div>
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="contact-country">Country</Label>
              <Input
                id="contact-country"
                name="country"
                defaultValue={formValues.country}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="contact-birthday">Birthday</Label>
              <Input
                id="contact-birthday"
                name="birthday"
                type="date"
                defaultValue={formValues.birthday}
              />
            </div>
          </div>
          <div className="grid gap-2">
            <Label htmlFor="contact-notes">Notes</Label>
            <Input
              id="contact-notes"
              name="notes"
              defaultValue={formValues.notes}
              placeholder="Anything useful to remember"
            />
          </div>
          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={() => {
                onOpenChange(false);
              }}
            >
              Cancel
            </Button>
            <Button type="submit">{isEditing ? 'Save changes' : 'Create contact'}</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

function readFormString(formData: FormData, key: string, fallback = '') {
  const value = formData.get(key);
  return typeof value === 'string' ? value : fallback;
}
