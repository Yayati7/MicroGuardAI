# 🚀 MicroGuard AI — AI-Powered Architecture Reviewer

MicroGuard AI is a cloud-native microservices platform that allows users to submit their distributed system architectures and receive AI-powered security analysis, risk detection, and recommendations. Built with Java Spring Boot, Apache Kafka, Angular, and Mistral AI, MicroGuard simulates real-world backend infrastructure while providing intelligent architecture reviews.

---

## ✨ Features
- 🤖 AI-powered architecture analysis using Mistral API with async processing via Apache Kafka  
- 🧩 Submit microservice architecture details (services, databases, communication patterns)  
- 📂 Per-user project history stored in PostgreSQL (no repeated AI calls)  
- 🔐 Secure Google SSO authentication via Keycloak OAuth2  
- ⚡ Event-driven backend with decoupled communication across 8 microservices  
- 🌐 Service discovery & load balancing via Spring Cloud Eureka  
- 🛠 Centralized configuration management via Spring Cloud Config Server  
- 🚪 API routing & JWT validation via Spring Cloud API Gateway  
- 🗄 Persistent analysis results stored in MongoDB with write-back to PostgreSQL  
- 🎨 Responsive Angular frontend with dark-themed UI and real-time loading states  

---

## 🧱 Tech Stack
**Frontend:** Angular, TypeScript, CSS  
**Backend:** Java, Spring Boot, Spring Cloud (Eureka, Config Server, API Gateway)  
**Messaging:** Apache Kafka, Zookeeper  
**Databases:** PostgreSQL, MongoDB  
**AI:** Mistral API  
**Authentication:** Keycloak, OAuth2, Google SSO  
**Deployment:** Docker, Docker Compose, Nginx, GCP  

---

## 🏗 Architecture Overview
Browser → Nginx → Angular Frontend (SSR)
→ API Gateway → User Service → PostgreSQL
→ Architecture Service → MongoDB + Kafka
→ AI Analysis Service → Mistral API + MongoDB

---

## ⚙️ Prerequisites
- Docker and Docker Compose  
- Node.js (v20.x or higher)  
- Java 17  
- Google OAuth2 credentials  
- Mistral API key  

---

## ▶️ Usage
1. Open the application in your browser  
2. Sign in with your Google account via Keycloak SSO  
3. Fill in your project details — name, services, database, and communication pattern  
4. Click **Run AI Analysis**  
5. View the AI-generated security analysis and recommendations  
6. Return to the dashboard to revisit past analyses without additional API calls  

---

## 🙌 Acknowledgements
Spring Boot • Spring Cloud • Apache Kafka • Angular • Keycloak • Mistral AI • MongoDB • PostgreSQL  

---

💡 Inspired by real-world microservices deployment patterns and the practical challenges of securing distributed system architectures at scale.
