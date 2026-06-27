package com.contacts.api;

import com.contacts.Contact;
import com.contacts.ContactService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Javalin route adapter for contact use cases. */
public final class ContactsApi {
    private final ContactService service;

    private ContactsApi(ContactService service) {
        this.service = Objects.requireNonNull(service, "service must not be null");
    }

    /**
     * Create a configured Javalin application.
     *
     * @param service contact application service
     * @return configured Javalin app
     */
    public static Javalin create(ContactService service) {
        ContactsApi api = new ContactsApi(service);
        return Javalin.create(
                        config ->
                                config.bundledPlugins.enableCors(
                                        cors -> cors.addRule(rule -> rule.anyHost())))
                .get("/health", context -> context.json(Map.of("status", "ok")))
                .get("/api/contacts", context -> context.json(Map.of("contacts", api.list())))
                .post(
                        "/api/contacts",
                        context -> {
                            Contact contact = context.bodyAsClass(ContactPayload.class).toContact();
                            api.service.addContact(contact);
                            context.status(HttpStatus.CREATED).json(Map.of("contact", ContactPayload.from(contact)));
                        })
                .put(
                        "/api/contacts/{id}",
                        context -> {
                            String id = context.pathParam("id");
                            ContactPayload payload = context.bodyAsClass(ContactPayload.class);
                            boolean updated = api.service.updateContact(id, ignored -> payload.toContact());
                            if (!updated) {
                                context.status(HttpStatus.NOT_FOUND).json(Map.of("message", "Contact not found"));
                                return;
                            }
                            Contact contact = api.service.findById(id).orElseThrow();
                            context.json(Map.of("contact", ContactPayload.from(contact)));
                        })
                .delete(
                        "/api/contacts/{id}",
                        context -> {
                            boolean deleted = api.service.deleteById(context.pathParam("id"));
                            if (!deleted) {
                                context.status(HttpStatus.NOT_FOUND).json(Map.of("message", "Contact not found"));
                                return;
                            }
                            context.json(Map.of("deleted", 1));
                        })
                .delete(
                        "/api/contacts",
                        context -> {
                            DeleteContactsRequest request = context.bodyAsClass(DeleteContactsRequest.class);
                            int deleted = api.deleteAll(request.ids());
                            context.json(Map.of("deleted", deleted));
                        })
                .exception(
                        IOException.class,
                        (exception, context) ->
                                context.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .json(Map.of("message", exception.getMessage())))
                .exception(
                        IllegalArgumentException.class,
                        (exception, context) ->
                                context.status(HttpStatus.BAD_REQUEST)
                                        .json(Map.of("message", exception.getMessage())));
    }

    private List<ContactPayload> list() throws IOException {
        return service.getAllContacts().stream().map(ContactPayload::from).toList();
    }

    private int deleteAll(List<String> ids) throws IOException {
        int deleted = 0;
        for (String id : ids == null ? List.<String>of() : ids) {
            if (service.deleteById(id)) {
                deleted++;
            }
        }
        return deleted;
    }

    /** Request body for bulk deletion. */
    public static final class DeleteContactsRequest {
        private List<String> ids = List.of();

        /**
         * Return IDs requested for deletion.
         *
         * @return immutable contact IDs
         */
        public List<String> ids() {
            return List.copyOf(ids);
        }

        /**
         * Set IDs requested for deletion.
         *
         * @param ids contact IDs
         */
        public void setIds(List<String> ids) {
            this.ids = ids == null ? List.of() : List.copyOf(ids);
        }
    }
}
