variable "project_name" {
  description = "Short project name used in AWS resource names."
  type        = string
  default     = "contacts"
}

variable "environment" {
  description = "Deployment environment name."
  type        = string
  default     = "prod"
}

variable "aws_region" {
  description = "AWS region for all regional resources."
  type        = string
  default     = "us-east-1"
}

variable "vpc_cidr" {
  description = "CIDR block for the application VPC."
  type        = string
  default     = "10.40.0.0/16"
}

variable "availability_zones" {
  description = "Availability zones to use. Leave empty to select the first two available AZs in the region."
  type        = list(string)
  default     = []
}

variable "api_image" {
  description = "Fully qualified API container image URI."
  type        = string
  default     = "public.ecr.aws/docker/library/eclipse-temurin:17-jre"
}

variable "web_image" {
  description = "Fully qualified web container image URI."
  type        = string
  default     = "public.ecr.aws/docker/library/node:24-alpine"
}

variable "desired_count" {
  description = "Desired ECS task count for each service."
  type        = number
  default     = 2
}

variable "api_cpu" {
  description = "API Fargate task CPU units."
  type        = number
  default     = 512
}

variable "api_memory" {
  description = "API Fargate task memory in MiB."
  type        = number
  default     = 1024
}

variable "web_cpu" {
  description = "Web Fargate task CPU units."
  type        = number
  default     = 512
}

variable "web_memory" {
  description = "Web Fargate task memory in MiB."
  type        = number
  default     = 1024
}

variable "certificate_arn" {
  description = "Optional ACM certificate ARN. When set, HTTPS listener is enabled."
  type        = string
  default     = ""
}

variable "health_check_grace_period_seconds" {
  description = "Grace period before ECS starts enforcing load balancer health checks."
  type        = number
  default     = 60
}
