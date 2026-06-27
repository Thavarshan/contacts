# Contacts Web

Next.js dashboard for the Contacts monorepo. The dashboard talks to the Java HTTP API through local
Next API routes.

## Quality Gates

```bash
npm run format:check
npm run lint
npm run typecheck
npm run test:run
npm run build
```

Use `npm run verify` before opening a pull request.

Repository policy is owned by the monorepo root. Do not add web-local GitHub workflows, Husky hooks,
`.editorconfig`, `.gitignore`, Prettier, or commit-message configuration here.

## Getting Started

Install dependencies from the monorepo root:

```bash
npm ci --prefix web
```

Start the Java API from the monorepo root:

```bash
./mvnw -B -ntp -pl api -am package
CONTACTS_CSV_PATH=contacts.csv CONTACTS_API_PORT=7070 \
  java -jar api/target/contacts-api-1.0.0-SNAPSHOT-api.jar
```

Start the web development server:

```bash
CONTACTS_API_URL=http://127.0.0.1:7070 npm --prefix web run dev
```

Open [http://localhost:3000](http://localhost:3000).

The dashboard uses the Pages Router. Start with `pages/dashboard.tsx`,
`components/blocks/dashboard-content.tsx`, and `lib/contact-store.ts`.

## Configuration

| Variable           | Default                 | Description                                |
| ------------------ | ----------------------- | ------------------------------------------ |
| `CONTACTS_API_URL` | `http://127.0.0.1:7070` | Java API base URL used by Next API routes. |

## Deployment

Deploy the `web` directory to a Next.js-capable platform and configure `CONTACTS_API_URL` to point
at a running Contacts API deployment.
