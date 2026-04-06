# MicroGuardAI — AI-Powered Architecture Reviewer

A full-stack microservices application that analyzes software architecture definitions using AI and returns security vulnerability reports.

## Tech Stack
- **Backend:** Spring Boot Microservices (Config Server, Eureka, API Gateway)
- **Messaging:** Apache Kafka
- **Authentication:** Keycloak with Google OAuth2
- **Databases:** PostgreSQL (users/projects), MongoDB (AI analysis results)
- **AI:** Mistral AI API
- **Frontend:** Angular 17 with SSR
- **Deployment:** Docker Compose on Google Cloud

## Architecture
- Config Server (port 8888)
- Eureka Service Discovery (port 8761)
- API Gateway (port 8080)
- User Service (port 8081) — PostgreSQL
- Architecture Service (port 8082) — MongoDB + Kafka
- AI Analysis Service (port 8083) — MongoDB + Kafka + Mistral AI
- Angular SSR Frontend (port 4000)
- Keycloak Authentication (port 8080)
- Nginx Reverse Proxy

## Live Demo
https://34.14.214.173.nip.io

## Setup
See `.env.example` for required environment variables.
