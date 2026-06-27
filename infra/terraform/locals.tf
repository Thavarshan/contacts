locals {
  name_prefix = "${var.project_name}-${var.environment}"

  tags = {
    Application = var.project_name
    Environment = var.environment
    ManagedBy   = "terraform"
    Repository  = "Thavarshan/contacts"
  }
}
