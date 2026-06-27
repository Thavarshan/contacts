output "application_url" {
  description = "Public HTTP URL for the application load balancer."
  value       = "http://${aws_lb.main.dns_name}"
}

output "api_ecr_repository_url" {
  description = "ECR repository URL for the contacts API image."
  value       = aws_ecr_repository.api.repository_url
}

output "web_ecr_repository_url" {
  description = "ECR repository URL for the contacts web image."
  value       = aws_ecr_repository.web.repository_url
}

output "ecs_cluster_name" {
  description = "ECS cluster name."
  value       = aws_ecs_cluster.main.name
}

output "api_service_name" {
  description = "ECS API service name."
  value       = aws_ecs_service.api.name
}

output "web_service_name" {
  description = "ECS web service name."
  value       = aws_ecs_service.web.name
}
