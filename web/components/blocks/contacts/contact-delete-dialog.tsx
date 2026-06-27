'use client';

import { TriangleAlertIcon } from 'lucide-react';

import type { Contact } from '@/components/blocks/contacts/data-table';
import { Button } from '@/components/ui/button';
import {
  AlertDialog,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogMedia,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';

interface ContactDeleteDialogProps {
  contact: Contact | null;
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onConfirm: (contact: Contact) => void;
}

export function ContactDeleteDialog({
  contact,
  open,
  onOpenChange,
  onConfirm,
}: ContactDeleteDialogProps) {
  return (
    <AlertDialog open={open} onOpenChange={onOpenChange}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogMedia>
            <TriangleAlertIcon />
          </AlertDialogMedia>
          <AlertDialogTitle>Delete contact?</AlertDialogTitle>
          <AlertDialogDescription>
            {contact
              ? `This will permanently remove ${[contact.firstName, contact.lastName].filter(Boolean).join(' ')} from your contacts.`
              : 'This contact will be permanently removed.'}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <Button
            variant="destructive"
            onClick={() => {
              if (contact) {
                onConfirm(contact);
              }
            }}
          >
            Delete contact
          </Button>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
