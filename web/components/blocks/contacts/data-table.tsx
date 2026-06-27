'use client';

import * as React from 'react';
import {
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useReactTable,
  type ColumnDef,
  type ColumnFiltersState,
  type RowSelectionState,
  type SortingState,
  type VisibilityState,
} from '@tanstack/react-table';
import { toast } from 'sonner';
import {
  ArrowUpDownIcon,
  Building2Icon,
  CalendarDaysIcon,
  ChevronDownIcon,
  ChevronLeftIcon,
  ChevronRightIcon,
  Columns3Icon,
  EllipsisVerticalIcon,
  MailIcon,
  MapPinIcon,
  PhoneIcon,
  PlusIcon,
  SearchIcon,
  UsersRoundIcon,
} from 'lucide-react';

import {
  contactAddress,
  contactName,
  formatDate,
  schema,
  type Contact,
} from '@/lib/contact-record';
import { ContactDeleteDialog } from '@/components/blocks/contacts/contact-delete-dialog';
import { ADD_CONTACT_EVENT } from '@/components/blocks/contacts/app-sidebar';
import {
  ContactFormDialog,
  type ContactFormValues,
} from '@/components/blocks/contacts/contact-form-dialog';
import { Button } from '@/components/ui/button';
import { Checkbox } from '@/components/ui/checkbox';
import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerDescription,
  DrawerFooter,
  DrawerHeader,
  DrawerTitle,
  DrawerTrigger,
} from '@/components/ui/drawer';
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Input } from '@/components/ui/input';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';

export { contactAddress, contactName, formatDate, schema };
export type { Contact };

function getColumns({
  onEdit,
  onDelete,
}: {
  onEdit: (contact: Contact) => void;
  onDelete: (contact: Contact) => void;
}): ColumnDef<Contact>[] {
  return [
    {
      id: 'select',
      header: ({ table }) => (
        <div className="flex items-center justify-center">
          <Checkbox
            checked={table.getIsAllPageRowsSelected()}
            indeterminate={
              table.getIsSomePageRowsSelected() && !table.getIsAllPageRowsSelected()
            }
            onCheckedChange={(value) => {
              table.toggleAllPageRowsSelected(value);
            }}
            aria-label="Select all contacts on this page"
          />
        </div>
      ),
      cell: ({ row }) => (
        <div className="flex items-center justify-center">
          <Checkbox
            checked={row.getIsSelected()}
            onCheckedChange={(value) => {
              row.toggleSelected(value);
            }}
            aria-label="Select contact"
          />
        </div>
      ),
      enableSorting: false,
      enableHiding: false,
    },
    {
      id: 'name',
      accessorFn: (row) => contactName(row),
      header: ({ column }) => (
        <Button
          variant="ghost"
          className="-ml-2 px-2"
          onClick={() => {
            column.toggleSorting(column.getIsSorted() === 'asc');
          }}
        >
          Name
          <ArrowUpDownIcon data-icon="inline-end" />
        </Button>
      ),
      cell: ({ row }) => <ContactViewer contact={row.original} />,
      filterFn: (row, _id, value: string) => {
        const contact = row.original;
        return [
          contactName(contact),
          contact.company,
          contact.jobTitle,
          contact.phone,
          contact.email,
          contactAddress(contact),
          contact.notes,
        ]
          .join(' ')
          .toLowerCase()
          .includes(value.toLowerCase());
      },
      enableHiding: false,
    },
    {
      accessorKey: 'phone',
      header: 'Phone',
      cell: ({ row }) => (
        <a href={`tel:${row.original.phone}`} className="font-medium">
          {row.original.phone}
        </a>
      ),
    },
    {
      accessorKey: 'email',
      header: 'Email',
      cell: ({ row }) => (
        <a href={`mailto:${row.original.email}`} className="text-muted-foreground">
          {row.original.email}
        </a>
      ),
    },
    {
      accessorKey: 'company',
      header: ({ column }) => (
        <Button
          variant="ghost"
          className="-ml-2 px-2"
          onClick={() => {
            column.toggleSorting(column.getIsSorted() === 'asc');
          }}
        >
          Company
          <ArrowUpDownIcon data-icon="inline-end" />
        </Button>
      ),
      cell: ({ row }) => (
        <span className="text-muted-foreground">
          {[row.original.company, row.original.jobTitle].filter(Boolean).join(' - ')}
        </span>
      ),
    },
    {
      accessorKey: 'birthday',
      header: 'Birthday',
      cell: ({ row }) => formatDate(row.original.birthday),
    },
    {
      id: 'address',
      accessorFn: (row) => contactAddress(row),
      header: 'Address',
      cell: ({ row }) => (
        <span className="line-clamp-1 max-w-[280px] text-muted-foreground">
          {contactAddress(row.original) || 'No address'}
        </span>
      ),
    },
    {
      id: 'actions',
      enableHiding: false,
      cell: ({ row }) => (
        <DropdownMenu>
          <DropdownMenuTrigger
            render={
              <Button
                variant="ghost"
                className="flex size-8 text-muted-foreground data-open:bg-muted"
                size="icon"
              />
            }
          >
            <EllipsisVerticalIcon />
            <span className="sr-only">Open contact menu</span>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-44">
            <DropdownMenuItem
              onClick={() => {
                onEdit(row.original);
              }}
            >
              Edit
            </DropdownMenuItem>
            <DropdownMenuItem
              onClick={() => {
                window.location.href = `tel:${row.original.phone}`;
              }}
            >
              Call
            </DropdownMenuItem>
            <DropdownMenuItem
              onClick={() => {
                window.location.href = `mailto:${row.original.email}`;
              }}
            >
              Send email
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem
              variant="destructive"
              onClick={() => {
                onDelete(row.original);
              }}
            >
              Delete
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      ),
    },
  ];
}

interface DataTableProps {
  data: Contact[];
  apiEnabled?: boolean;
}

export function DataTable({ data, apiEnabled = false }: DataTableProps) {
  const [contacts, setContacts] = React.useState(() => data);
  const [loading, setLoading] = React.useState(apiEnabled);
  const [syncing, setSyncing] = React.useState(false);
  const [sorting, setSorting] = React.useState<SortingState>([]);
  const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([]);
  const [columnVisibility, setColumnVisibility] = React.useState<VisibilityState>({});
  const [rowSelection, setRowSelection] = React.useState<RowSelectionState>({});
  const [formOpen, setFormOpen] = React.useState(false);
  const [editingContact, setEditingContact] = React.useState<Contact | null>(null);
  const [deleteOpen, setDeleteOpen] = React.useState(false);
  const [deletingContact, setDeletingContact] = React.useState<Contact | null>(null);

  const columns = React.useMemo(
    () =>
      getColumns({
        onEdit: (contact) => {
          setEditingContact(contact);
          setFormOpen(true);
        },
        onDelete: (contact) => {
          setDeletingContact(contact);
          setDeleteOpen(true);
        },
      }),
    [],
  );

  React.useEffect(() => {
    setContacts(data);
  }, [data]);

  React.useEffect(() => {
    if (!apiEnabled) {
      return;
    }

    let active = true;

    async function loadContacts() {
      try {
        setLoading(true);
        const nextContacts = await requestContacts();

        if (active) {
          setContacts(nextContacts);
        }
      } catch (error) {
        toast.error(readErrorMessage(error));
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    }

    void loadContacts();

    return () => {
      active = false;
    };
  }, [apiEnabled]);

  React.useEffect(() => {
    function openAddContactDialog() {
      setEditingContact(null);
      setFormOpen(true);
    }

    document.addEventListener(ADD_CONTACT_EVENT, openAddContactDialog);

    return () => {
      document.removeEventListener(ADD_CONTACT_EVENT, openAddContactDialog);
    };
  }, []);

  // eslint-disable-next-line react-hooks/incompatible-library -- TanStack Table exposes stable table APIs through this hook.
  const table = useReactTable({
    data: contacts,
    columns,
    state: {
      sorting,
      columnFilters,
      columnVisibility,
      rowSelection,
    },
    getRowId: (row) => row.id,
    enableRowSelection: true,
    onSortingChange: setSorting,
    onColumnFiltersChange: setColumnFilters,
    onColumnVisibilityChange: setColumnVisibility,
    onRowSelectionChange: setRowSelection,
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    getSortedRowModel: getSortedRowModel(),
    initialState: {
      pagination: {
        pageSize: 8,
      },
    },
  });

  const selectedContacts = table
    .getFilteredSelectedRowModel()
    .rows.map((row) => row.original);
  const selectedCount = selectedContacts.length;

  function emailSelectedContacts() {
    if (!selectedCount) {
      toast.error('Select one or more contacts first');
      return;
    }

    const bcc = selectedContacts.map((contact) => contact.email).join(',');
    window.location.href = `mailto:?bcc=${encodeURIComponent(bcc)}`;
  }

  async function deleteSelectedContacts() {
    if (!selectedCount) {
      toast.error('Select one or more contacts first');
      return;
    }

    const selectedIds = new Set(selectedContacts.map((contact) => contact.id));
    await runMutation(async () => {
      if (apiEnabled) {
        await requestJson<{ deleted: number }>('/api/contacts', {
          body: JSON.stringify({ ids: [...selectedIds] }),
          method: 'DELETE',
        });
      }

      setContacts((current) => current.filter((contact) => !selectedIds.has(contact.id)));
      table.resetRowSelection();
      toast.success(`Deleted ${selectedCount.toString()} contact(s)`);
    }, 'Unable to delete selected contacts');
  }

  async function saveContact(values: ContactFormValues, id?: string) {
    if (id !== undefined) {
      await runMutation(async () => {
        const contact = apiEnabled
          ? (
              await requestJson<{ contact: Contact }>(
                `/api/contacts/${encodeURIComponent(id)}`,
                {
                  body: JSON.stringify(values),
                  method: 'PUT',
                },
              )
            ).contact
          : { ...values, id };

        setContacts((current) =>
          current.map((item) => (item.id === id ? contact : item)),
        );
        toast.success('Contact updated');
      }, 'Unable to update contact');
    } else {
      await runMutation(async () => {
        const contact = apiEnabled
          ? (
              await requestJson<{ contact: Contact }>('/api/contacts', {
                body: JSON.stringify(values),
                method: 'POST',
              })
            ).contact
          : { ...values, id: createBrowserId() };

        setContacts((current) => [contact, ...current]);
        toast.success('Contact created');
      }, 'Unable to create contact');
    }

    setFormOpen(false);
    setEditingContact(null);
  }

  async function deleteContact(contact: Contact) {
    await runMutation(async () => {
      if (apiEnabled) {
        await requestJson<{ deleted: number }>(
          `/api/contacts/${encodeURIComponent(contact.id)}`,
          { method: 'DELETE' },
        );
      }

      setContacts((current) => current.filter((item) => item.id !== contact.id));
      setDeleteOpen(false);
      setDeletingContact(null);
      table.resetRowSelection();
      toast.success('Contact deleted');
    }, 'Unable to delete contact');
  }

  async function runMutation(mutation: () => Promise<void>, fallbackMessage: string) {
    try {
      setSyncing(true);
      await mutation();
    } catch (error) {
      toast.error(readErrorMessage(error, fallbackMessage));
    } finally {
      setSyncing(false);
    }
  }

  return (
    <>
      <DashboardSummary contacts={contacts} loading={loading} syncing={syncing} />
      <section id="contacts" className="rounded-lg border bg-card">
        <div className="flex flex-col gap-3 border-b p-4">
          <div className="flex flex-col gap-3 @3xl/main:flex-row @3xl/main:items-center @3xl/main:justify-between">
            <div className="relative @3xl/main:w-80">
              <SearchIcon className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                value={
                  (table.getColumn('name')?.getFilterValue() as string | undefined) ?? ''
                }
                onChange={(event) => {
                  table.getColumn('name')?.setFilterValue(event.target.value);
                }}
                placeholder="Search contacts"
                className="pl-9"
              />
            </div>
            <div className="flex flex-col gap-2 sm:flex-row sm:items-center">
              <DropdownMenu>
                <DropdownMenuTrigger render={<Button variant="outline" />}>
                  <Columns3Icon data-icon="inline-start" />
                  Columns
                  <ChevronDownIcon data-icon="inline-end" />
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="w-44">
                  {table
                    .getAllColumns()
                    .filter((column) => column.getCanHide())
                    .map((column) => (
                      <DropdownMenuCheckboxItem
                        key={column.id}
                        className="capitalize"
                        checked={column.getIsVisible()}
                        onCheckedChange={(value) => {
                          column.toggleVisibility(value);
                        }}
                      >
                        {column.id}
                      </DropdownMenuCheckboxItem>
                    ))}
                </DropdownMenuContent>
              </DropdownMenu>
              <Button
                onClick={() => {
                  setEditingContact(null);
                  setFormOpen(true);
                }}
                disabled={syncing}
              >
                <PlusIcon data-icon="inline-start" />
                Add contact
              </Button>
            </div>
          </div>

          {selectedCount > 0 && (
            <div className="flex flex-col gap-2 rounded-lg border bg-muted/30 p-3 text-sm @3xl/main:flex-row @3xl/main:items-center @3xl/main:justify-between">
              <span className="font-medium">
                {selectedCount.toString()} contact(s) selected
              </span>
              <div className="flex flex-wrap gap-2">
                <Button variant="outline" size="sm" onClick={emailSelectedContacts}>
                  Email selected
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => {
                    table.resetRowSelection();
                  }}
                >
                  Clear
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => {
                    void deleteSelectedContacts();
                  }}
                  disabled={syncing}
                >
                  Delete selected
                </Button>
              </div>
            </div>
          )}
        </div>

        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              {table.getHeaderGroups().map((headerGroup) => (
                <TableRow key={headerGroup.id}>
                  {headerGroup.headers.map((header) => (
                    <TableHead key={header.id} colSpan={header.colSpan}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(header.column.columnDef.header, header.getContext())}
                    </TableHead>
                  ))}
                </TableRow>
              ))}
            </TableHeader>
            <TableBody>
              {table.getRowModel().rows.length ? (
                table.getRowModel().rows.map((row) => (
                  <TableRow key={row.id} data-state={row.getIsSelected() && 'selected'}>
                    {row.getVisibleCells().map((cell) => (
                      <TableCell key={cell.id}>
                        {flexRender(cell.column.columnDef.cell, cell.getContext())}
                      </TableCell>
                    ))}
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={columns.length} className="h-24 text-center">
                    {loading ? 'Loading contacts...' : 'No contacts found.'}
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </div>

        <div className="flex flex-col gap-3 border-t p-4 text-sm text-muted-foreground @3xl/main:flex-row @3xl/main:items-center @3xl/main:justify-between">
          <span>
            Showing {table.getRowModel().rows.length.toString()} of{' '}
            {table.getFilteredRowModel().rows.length.toString()} filtered contact(s)
          </span>
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => {
                table.previousPage();
              }}
              disabled={!table.getCanPreviousPage()}
            >
              <ChevronLeftIcon data-icon="inline-start" />
              Previous
            </Button>
            <span className="min-w-20 text-center font-medium text-foreground">
              Page {(table.getState().pagination.pageIndex + 1).toString()} of{' '}
              {table.getPageCount().toString()}
            </span>
            <Button
              variant="outline"
              size="sm"
              onClick={() => {
                table.nextPage();
              }}
              disabled={!table.getCanNextPage()}
            >
              Next
              <ChevronRightIcon data-icon="inline-end" />
            </Button>
          </div>
        </div>
      </section>
      <ContactFormDialog
        contact={editingContact}
        open={formOpen}
        onOpenChange={(open) => {
          setFormOpen(open);
          if (!open) {
            setEditingContact(null);
          }
        }}
        onSave={(values, id) => {
          void saveContact(values, id);
        }}
      />
      <ContactDeleteDialog
        contact={deletingContact}
        open={deleteOpen}
        onOpenChange={(open) => {
          setDeleteOpen(open);
          if (!open) {
            setDeletingContact(null);
          }
        }}
        onConfirm={(contact) => {
          void deleteContact(contact);
        }}
      />
    </>
  );
}

function DashboardSummary({
  contacts,
  loading,
  syncing,
}: {
  contacts: Contact[];
  loading: boolean;
  syncing: boolean;
}) {
  const companies = new Set(contacts.map((contact) => contact.company).filter(Boolean));
  const withEmail = contacts.filter((contact) => contact.email).length;
  const birthdays = contacts.filter((contact) => contact.birthday).length;
  const stats = [
    {
      label: 'Total contacts',
      value: contacts.length.toString(),
      icon: UsersRoundIcon,
    },
    {
      label: 'Companies',
      value: companies.size.toString(),
      icon: Building2Icon,
    },
    {
      label: 'Email ready',
      value: withEmail.toString(),
      icon: MailIcon,
    },
    {
      label: 'Birthdays saved',
      value: birthdays.toString(),
      icon: CalendarDaysIcon,
    },
  ];

  return (
    <section className="mb-4 grid grid-cols-1 gap-3 @3xl/main:grid-cols-4">
      {stats.map((stat) => (
        <div key={stat.label} className="rounded-lg border bg-card p-4">
          <div className="flex items-center justify-between gap-3">
            <div>
              <p className="text-sm text-muted-foreground">{stat.label}</p>
              <p className="mt-1 text-2xl font-semibold">{loading ? '-' : stat.value}</p>
            </div>
            <span className="flex size-9 items-center justify-center rounded-md bg-muted">
              <stat.icon className="size-4 text-muted-foreground" />
            </span>
          </div>
        </div>
      ))}
      {syncing && (
        <p className="sr-only" role="status">
          Saving contact changes
        </p>
      )}
    </section>
  );
}

function ContactViewer({ contact }: { contact: Contact }) {
  const address = contactAddress(contact);

  return (
    <Drawer direction="right">
      <DrawerTrigger asChild>
        <Button
          variant="link"
          className="h-auto w-fit px-0 py-0 text-left text-foreground"
        >
          <span className="flex flex-col items-start">
            <span className="font-medium">{contactName(contact)}</span>
            <span className="text-xs text-muted-foreground">{contact.email}</span>
          </span>
        </Button>
      </DrawerTrigger>
      <DrawerContent>
        <DrawerHeader className="gap-1">
          <DrawerTitle>{contactName(contact)}</DrawerTitle>
          <DrawerDescription>
            {[contact.jobTitle, contact.company].filter(Boolean).join(' at ')}
          </DrawerDescription>
        </DrawerHeader>
        <div className="flex flex-col gap-5 overflow-y-auto px-4 text-sm">
          <div className="grid gap-3 rounded-lg border p-3">
            <a href={`tel:${contact.phone}`} className="flex items-center gap-2">
              <PhoneIcon className="size-4 text-muted-foreground" />
              {contact.phone || 'No phone'}
            </a>
            <a href={`mailto:${contact.email}`} className="flex items-center gap-2">
              <MailIcon className="size-4 text-muted-foreground" />
              {contact.email || 'No email'}
            </a>
            <div className="flex items-start gap-2">
              <MapPinIcon className="mt-0.5 size-4 text-muted-foreground" />
              <span>{address || 'No address'}</span>
            </div>
          </div>
          <div className="grid gap-3">
            <div>
              <p className="text-xs text-muted-foreground">Birthday</p>
              <p>{formatDate(contact.birthday) || 'Not set'}</p>
            </div>
            <div>
              <p className="text-xs text-muted-foreground">Notes</p>
              <p>{contact.notes || 'No notes'}</p>
            </div>
          </div>
        </div>
        <DrawerFooter>
          <DrawerClose asChild>
            <Button variant="outline">Done</Button>
          </DrawerClose>
        </DrawerFooter>
      </DrawerContent>
    </Drawer>
  );
}

async function requestContacts() {
  return (await requestJson<{ contacts: Contact[] }>('/api/contacts')).contacts;
}

async function requestJson<T>(url: string, init?: RequestInit) {
  const response = await fetch(url, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  const data = (await response.json()) as unknown;

  if (!response.ok) {
    throw new Error(readApiMessage(data));
  }

  return data as T;
}

function readApiMessage(data: unknown) {
  if (typeof data === 'object' && data !== null && 'message' in data) {
    const message = data.message;

    if (typeof message === 'string') {
      return message;
    }
  }

  return 'Request failed';
}

function readErrorMessage(error: unknown, fallback = 'Unable to load contacts') {
  return error instanceof Error ? error.message : fallback;
}

function createBrowserId() {
  return globalThis.crypto.randomUUID();
}
