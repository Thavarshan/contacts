# Example CLI Session

Build the project:

```bash
./mvnw verify
```

Add contacts:

```bash
java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --file contacts.csv add \
    --first-name Jane \
    --last-name Doe \
    --email jane@example.com \
    --phone +15550100
```

List contacts:

```bash
java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --file contacts.csv list
```

Search contacts:

```bash
java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --file contacts.csv search jane
```

Update a contact by list index:

```bash
java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --file contacts.csv update 1 --company Acme
```

Export contacts:

```bash
java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --file contacts.csv export backup.csv
```
