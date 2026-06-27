# API Reference

The HTTP API is a lightweight Javalin adapter over the same core service used by the CLI. It is
intended for the Next.js dashboard and local automation.

## Run the API

Build the runnable API jar:

```bash
./mvnw -B -ntp verify
```

Start the server:

```bash
CONTACTS_CSV_PATH=contacts.csv CONTACTS_API_PORT=7070 \
  java -jar api/target/contacts-api-1.0.0-SNAPSHOT-api.jar
```

Environment variables:

| Variable            | Default        | Description                           |
| ------------------- | -------------- | ------------------------------------- |
| `CONTACTS_CSV_PATH` | `contacts.csv` | CSV file read and written by the API. |
| `CONTACTS_API_PORT` | `7070`         | HTTP port for the Javalin server.     |

## Contact Payload

The dashboard uses a flat JSON representation:

```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "firstName": "Jane",
  "lastName": "Doe",
  "company": "Acme",
  "jobTitle": "Engineer",
  "phone": "+15550100",
  "email": "jane@example.com",
  "street": "1 Main St",
  "city": "Colombo",
  "state": "WP",
  "postalCode": "00100",
  "country": "Sri Lanka",
  "birthday": "1990-01-02",
  "notes": "Created through API"
}
```

The API maps these fields to the richer core `Contact` model by using the first phone number, first
email address, and first address.

## Endpoints

### `GET /health`

Returns API health.

```bash
curl http://127.0.0.1:7070/health
```

Response:

```json
{ "status": "ok" }
```

### `GET /api/contacts`

Lists contacts.

```bash
curl http://127.0.0.1:7070/api/contacts
```

Response:

```json
{
  "contacts": []
}
```

### `POST /api/contacts`

Creates a contact.

```bash
curl --request POST http://127.0.0.1:7070/api/contacts \
  --header 'Content-Type: application/json' \
  --data '{"firstName":"Jane","lastName":"Doe","email":"jane@example.com"}'
```

Response status: `201 Created`

Response:

```json
{
  "contact": {
    "id": "generated-id",
    "firstName": "Jane",
    "lastName": "Doe",
    "company": "",
    "jobTitle": "",
    "phone": "",
    "email": "jane@example.com",
    "street": "",
    "city": "",
    "state": "",
    "postalCode": "",
    "country": "",
    "birthday": "",
    "notes": ""
  }
}
```

### `PUT /api/contacts/{id}`

Replaces a contact by stable ID.

```bash
curl --request PUT http://127.0.0.1:7070/api/contacts/generated-id \
  --header 'Content-Type: application/json' \
  --data '{"firstName":"Jane","lastName":"Doe","company":"Acme Labs"}'
```

Response status: `200 OK` when updated, or `404 Not Found` when no contact matches the ID.

### `DELETE /api/contacts/{id}`

Deletes a contact by stable ID.

```bash
curl --request DELETE http://127.0.0.1:7070/api/contacts/generated-id
```

Response:

```json
{ "deleted": 1 }
```

Response status: `200 OK` when deleted, or `404 Not Found` when no contact matches the ID.

### `DELETE /api/contacts`

Deletes multiple contacts by stable ID.

```bash
curl --request DELETE http://127.0.0.1:7070/api/contacts \
  --header 'Content-Type: application/json' \
  --data '{"ids":["first-id","second-id"]}'
```

Response:

```json
{ "deleted": 2 }
```

## Error Responses

Validation errors return `400 Bad Request`:

```json
{ "message": "Invalid value" }
```

Unexpected I/O failures return `500 Internal Server Error`:

```json
{ "message": "Unable to read contacts.csv" }
```

## Web Dashboard Integration

The Next.js dashboard lives in `web`. It calls its local API routes, which proxy to this Java API.
Configure the Java API base URL in the web app with:

```bash
CONTACTS_API_URL=http://127.0.0.1:7070
```
