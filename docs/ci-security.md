# CI and Security

## GitHub Actions

The repository includes these workflows:

| Workflow             | File                              | Purpose                                                             |
| -------------------- | --------------------------------- | ------------------------------------------------------------------- |
| CI                   | `.github/workflows/ci.yml`        | Runs Java, web, Docker, and Terraform verification.                 |
| Terraform            | `.github/workflows/terraform.yml` | Produces AWS Terraform plans through GitHub OIDC.                   |
| Deploy               | `.github/workflows/deploy.yml`    | Builds images after successful CI, pushes to ECR, and applies AWS.  |
| CodeQL               | `.github/workflows/codeql.yml`    | Performs Java and TypeScript CodeQL analysis.                       |
| Pull Request Hygiene | `.github/workflows/pr-title.yml`  | Enforces a minimum PR title length.                                 |
| Release              | `.github/workflows/release.yml`   | Builds and uploads the shaded CLI and API jars to a GitHub release. |

## CI Workflow

The CI workflow runs on pushes to `main` or `develop`, pull requests, and manual dispatch.

The Java job performs:

1. Checkout.
2. Java 17 setup with Maven cache.
3. Maven Wrapper version check.
4. `./mvnw -B -ntp verify`.
5. Upload of Surefire reports.
6. Upload of static-analysis and coverage reports on Ubuntu.
7. Upload of the shaded CLI and API jars on Ubuntu.

The web job performs:

1. Checkout.
2. Node.js setup with npm cache.
3. `npm ci` in `web`.
4. `npm run format:check`.
5. `npm run lint`.
6. `npm run typecheck`.
7. `npm run test:run`.
8. `npm run build`.

The Docker job validates that the API and web production container images can be built from a clean
checkout.

The infrastructure job performs:

1. Checkout.
2. Terraform setup.
3. `terraform fmt -recursive -check`.
4. `terraform init -backend=false`.
5. `terraform validate`.

The workflow uses concurrency cancellation so superseded runs on the same ref are cancelled.

## Deployment Workflows

The Terraform and Deploy workflows authenticate to AWS using GitHub OIDC. They require the variables
documented in [Deployment](deployment.md), including `AWS_ROLE_TO_ASSUME`, `TF_STATE_BUCKET`, and
`TF_STATE_LOCK_TABLE`.

The Deploy workflow runs automatically only after CI succeeds on `main`, or manually through workflow
dispatch. It uses immutable image tags in the form `<commit-sha>-<run-attempt>` so rerunning a
deployment does not collide with ECR tag immutability.

## CodeQL

CodeQL runs on pushes, pull requests, a weekly schedule, and manual dispatch. It analyzes Java and
TypeScript. The Java build uses:

```bash
./mvnw -B -ntp -DskipTests package
```

Then it performs analysis through GitHub's CodeQL action.

## Permissions

Workflow permissions are intentionally scoped:

- CI has read access plus check and pull request write permissions for reporting.
- Terraform and Deploy have OIDC token permission and read-only repository contents permission.
- CodeQL has security event write access.
- Release has contents write access so it can create releases and upload artifacts.

## Repository Policy Location

GitHub workflows, issue templates, pull request templates, Husky hooks, Prettier settings,
`.editorconfig`, and `.gitignore` are owned at the monorepo root. Application folders such as `web/`
should not add their own duplicate repository policy files.

## Dependency Policy

Dependabot is intentionally not enabled. Dependency updates are manual and must be validated with:

```bash
./mvnw -B -ntp verify
```

Before merging dependency updates, review:

- Release notes.
- License changes.
- Transitive dependency changes.
- Security advisories.
- Any changes to generated CLI behavior or CSV handling.

## Security Reporting

Sensitive security issues should not be reported through public issues. See [Security Policy](../SECURITY.md).

Recommended GitHub settings are documented in [Repository Settings](repository-settings.md).
