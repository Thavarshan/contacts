# AWS Infrastructure

This Terraform stack deploys the contacts monorepo to AWS as two independently versioned ECS
Fargate services:

- `api`: Java/Javalin contacts API, backed by an encrypted EFS file system mounted at `/data`.
- `web`: Next.js dashboard, built with standalone output and served by Node.js.

An Application Load Balancer exposes the system. The default route forwards to the web service,
while `/api/*` and `/health` forward directly to the Java API.

## Resources

- VPC with public and private subnets across two availability zones.
- NAT gateways for private ECS task egress.
- ECR repositories with immutable tags and image scanning.
- ECS cluster, task definitions, services, and CloudWatch log groups.
- Application Load Balancer, listeners, target groups, and listener rules.
- EFS file system, mount targets, and access point for persistent CSV storage.

## GitHub OIDC

Create an AWS IAM role that trusts GitHub Actions for this repository and allows Terraform to manage
the resources in this stack. Store the role ARN in the GitHub environment variable
`AWS_ROLE_TO_ASSUME`.

At minimum, the workflows expect these repository or environment variables:

| Variable              | Purpose                                                            |
| --------------------- | ------------------------------------------------------------------ |
| `AWS_REGION`          | AWS region, for example `us-east-1`.                               |
| `AWS_ROLE_TO_ASSUME`  | IAM role ARN assumed by GitHub Actions via OIDC.                   |
| `TF_STATE_BUCKET`     | S3 bucket for Terraform state.                                     |
| `TF_STATE_LOCK_TABLE` | DynamoDB table used for Terraform state locking.                   |
| `TF_STATE_KEY`        | Optional state key. Defaults to `contacts/prod/terraform.tfstate`. |

## Local Usage

Initialize Terraform with your backend configuration:

```sh
terraform -chdir=infra/terraform init \
  -backend-config="bucket=<state-bucket>" \
  -backend-config="key=contacts/prod/terraform.tfstate" \
  -backend-config="region=us-east-1" \
  -backend-config="dynamodb_table=<lock-table>" \
  -backend-config="encrypt=true"
```

Validate the stack:

```sh
terraform -chdir=infra/terraform fmt -check
terraform -chdir=infra/terraform validate
```

Plan a deployment:

```sh
terraform -chdir=infra/terraform plan \
  -var-file=terraform.tfvars \
  -var="api_image=<api-image-uri>" \
  -var="web_image=<web-image-uri>"
```

Apply only from a protected CI environment unless you are intentionally testing an isolated AWS
account.
