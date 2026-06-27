# Deployment

The production deployment targets AWS and treats the monorepo as three buildable products:

- `core` and `cli`: Maven-built Java artifacts.
- `api`: Java API packaged as a Docker image and deployed to ECS Fargate.
- `web`: Next.js dashboard packaged as a Docker image and deployed to ECS Fargate.

## Runtime Architecture

```text
Internet
  |
Application Load Balancer
  |-- /api/* and /health --> ECS service: contacts API
  |                         |-- EFS mount: /data/contacts.csv
  |
  |-- all other paths ----> ECS service: Next.js web dashboard
```

The web dashboard calls `/api/contacts` from the browser. In AWS, the load balancer sends those
requests straight to the Java API. During local development, Next.js API routes proxy to the Java
API through `CONTACTS_API_URL`.

## Required GitHub Variables

Configure these as repository variables or production environment variables:

| Name                  | Example                                                 | Purpose                                    |
| --------------------- | ------------------------------------------------------- | ------------------------------------------ |
| `AWS_REGION`          | `us-east-1`                                             | AWS region for Terraform and image pushes. |
| `AWS_ROLE_TO_ASSUME`  | `arn:aws:iam::123456789012:role/github-contacts-deploy` | OIDC role assumed by GitHub Actions.       |
| `TF_STATE_BUCKET`     | `contacts-terraform-state`                              | S3 bucket that stores Terraform state.     |
| `TF_STATE_LOCK_TABLE` | `contacts-terraform-locks`                              | DynamoDB table used for state locking.     |
| `TF_STATE_KEY`        | `contacts/prod/terraform.tfstate`                       | Optional state object key.                 |

Protect the `production` GitHub environment and require review before deployment if this repository
is used for a real AWS account.

The deploy workflow skips automatically until `AWS_ROLE_TO_ASSUME`, `TF_STATE_BUCKET`, and
`TF_STATE_LOCK_TABLE` are configured. This keeps the main branch green before AWS infrastructure has
been bootstrapped.

## CI/CD Workflows

| Workflow    | Trigger                                          | Responsibility                                                                                               |
| ----------- | ------------------------------------------------ | ------------------------------------------------------------------------------------------------------------ |
| `CI`        | Push, pull request, manual                       | Java verification, web formatting/lint/typecheck/tests/build, Docker build validation, Terraform validation. |
| `Terraform` | Pull requests touching `infra/terraform`, manual | Authenticates to AWS and produces a Terraform plan.                                                          |
| `Deploy`    | Successful `CI` run on `main`, manual            | Builds immutable API and web images, pushes them to ECR, and applies Terraform.                              |
| `CodeQL`    | Push, pull request, weekly, manual               | Static security analysis for Java and TypeScript.                                                            |
| `Release`   | Version tag, manual                              | Publishes CLI/API JARs to a GitHub release.                                                                  |

## Bootstrap

1. Create an S3 state bucket and DynamoDB lock table.
2. Create a GitHub OIDC IAM role in AWS.
3. Add the GitHub variables listed above.
4. Run the `Terraform` workflow to review the initial plan.
5. Merge to `main` and let CI trigger deployment, or run the `Deploy` workflow manually.

The deploy workflow first targets the ECR repositories so image pushes can succeed on a fresh AWS
account, then applies the full stack with the newly built image tags.

## Local Docker Builds

```sh
npm run docker:build
```

Run the API container locally:

```sh
docker run --rm -p 7070:7070 -v "$PWD/.local-data:/data" contacts-api:local
```

Run the web container locally:

```sh
docker run --rm -p 3000:3000 -e CONTACTS_API_URL=http://host.docker.internal:7070 contacts-web:local
```

## Terraform Commands

```sh
npm run terraform:fmt:check
npm run terraform:validate
```

For real plans and applies, initialize the S3 backend as described in
[infra/terraform/README.md](../infra/terraform/README.md).
