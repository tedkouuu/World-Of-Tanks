# World of Tanks 🎮

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1%2B-brightgreen)](https://spring.io)
[![Coverage](https://img.shields.io/badge/Line%20Coverage-63%25-yellowgreen)](https://testing.com)

A Spring Boot web application for tank enthusiasts to battle with style! Features secure authentication, real-time gameplay mechanics, and responsive design.

## 🚀 Features

### Technical Stack
- **MySQL Database** with JPA/Hibernate integration
- **Spring Security** with role-based access (ADMIN/USER)
- **Dockerized SMTP Server** (MailHog) for email testing
- Model Mapper for DTO conversions
- Maintenance Interceptor for system updates
- Scheduler for automated tasks

### Validation & Security
- Client/Server-side form validation
- Localization Interceptor for multi-language support
- Logging Interceptor for request tracking

### Gameplay Mechanics
- Tank category-based **bonus damage system** ⚔️
- Responsive UI for all device sizes 📱💻

### Testing
- 63% line coverage with JUnit/Mockito
- Comprehensive exception handling

## ⚙️ Installation

### Prerequisites
- Java 17+
- Docker (for SMTP server)
- MySQL 8+

1. **Start MailHog SMTP Server**
```bash
docker run -d -p 1025:1025 -p 8025:8025 mailhog/mailhog