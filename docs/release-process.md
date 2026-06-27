# Release Process

The project currently builds shaded CLI and API jars and a production Next.js dashboard bundle.
Releases are tag-driven through GitHub Actions.

## Release Artifact

The primary artifacts are:

```text
cli/target/contacts-cli-<version>-cli.jar
api/target/contacts-api-<version>-api.jar
```

Both include runtime dependencies. The CLI uses `com.contacts.Main` as the entry point. The API uses
`com.contacts.api.ApiMain` as the entry point.

## Versioning

The Maven version lives in `pom.xml`.

Current development versions use `-SNAPSHOT`, for example:

```text
1.0.0-SNAPSHOT
```

Release tags use:

```text
v<version>
```

Example:

```text
v1.0.0
```

## Pre-release Checklist

1. Confirm the changelog describes the release.
2. Confirm documentation is up to date.
3. Install web dependencies if needed:

```bash
npm ci --prefix web
```

4. Run formatting and verification:

```bash
./mvnw -B -ntp spotless:apply
./mvnw -B -ntp clean verify
npm --prefix web run verify
```

5. Smoke-test the jars:

```bash
tmpfile=$(mktemp)
java -jar cli/target/contacts-cli-1.0.0-SNAPSHOT-cli.jar --file "$tmpfile" add \
  --first-name Jane \
  --last-name Doe \
  --email jane@example.com
java -jar cli/target/contacts-cli-1.0.0-SNAPSHOT-cli.jar --file "$tmpfile" list
CONTACTS_CSV_PATH="$tmpfile" CONTACTS_API_PORT=7070 \
  java -jar api/target/contacts-api-1.0.0-SNAPSHOT-api.jar &
api_pid=$!
curl --fail http://127.0.0.1:7070/health
kill "$api_pid"
rm -f "$tmpfile"
```

6. Confirm generated files under `target/`, `web/.next/`, and `web/node_modules/` are not committed.

## Maven Release Plugin

The Maven Release Plugin is configured with:

- Tag format: `v@{project.version}`.
- Preparation goals: `clean verify`.
- Release commit prefix: `[release]`.

Use it only when the repository is ready for Maven-managed release commits and tags.

## GitHub Release Workflow

The release workflow runs when a `v*` tag is pushed or when manually dispatched with an existing tag.

It performs:

1. Checkout at the tag.
2. Java 17 setup.
3. `./mvnw -B -ntp verify`.
4. Create a GitHub release if one does not already exist.
5. Upload `cli/target/*-cli.jar` and `api/target/*-api.jar`.

## Manual Tag Flow

From a clean repository:

```bash
git tag v1.0.0
git push origin v1.0.0
```

The release workflow creates or updates the GitHub release for that tag.

## Post-release

After a release:

- Confirm the GitHub release contains the CLI and API jars.
- Download the CLI jar and run `--version`.
- Start the next development version in `pom.xml` when appropriate.
- Add the next `Unreleased` section in `CHANGELOG.md`.
