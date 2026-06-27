# Recommended Repository Settings

These settings cannot be committed directly, but they should be configured in GitHub.

## Branch Protection

Protect `main` or `master` with:

- Require pull request reviews before merging.
- Require status checks to pass before merging.
- Require branches to be up to date before merging.
- Require the `CI` workflow.
- Require the `CodeQL` workflow.
- Block force pushes.
- Block branch deletion.

## Pull Requests

Recommended:

- Squash merge by default.
- Delete branches after merge.
- Require linear history for small teams that prefer a clean history.

## Security

Recommended:

- Enable private vulnerability reporting.
- Enable secret scanning.
- Enable push protection.
- Enable CodeQL default setup or keep the repository workflow enabled.

Dependabot should remain disabled for this repository unless the team explicitly changes the policy.
