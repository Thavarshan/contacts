# Release Process

The project currently builds a shaded CLI jar. Releases are tag-driven through GitHub Actions.

## Release Artifact

The primary artifact is:

```text
target/contacts-<version>-cli.jar
```

It includes runtime dependencies and has `com.contacts.Main` as the entry point.

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
3. Run formatting and verification:

```bash
./mvnw -B -ntp spotless:apply
./mvnw -B -ntp clean verify
```

4. Smoke-test the jar:

```bash
tmpfile=$(mktemp)
java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --file "$tmpfile" add \
  --first-name Jane \
  --last-name Doe \
  --email jane@example.com
java -jar target/contacts-1.0.0-SNAPSHOT-cli.jar --file "$tmpfile" list
rm -f "$tmpfile"
```

5. Confirm generated files under `target/` are not committed.

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
5. Upload `target/*-cli.jar`.

## Manual Tag Flow

From a clean repository:

```bash
git tag v1.0.0
git push origin v1.0.0
```

The release workflow creates or updates the GitHub release for that tag.

## Post-release

After a release:

- Confirm the GitHub release contains the CLI jar.
- Download the jar and run `--version`.
- Start the next development version in `pom.xml` when appropriate.
- Add the next `Unreleased` section in `CHANGELOG.md`.
