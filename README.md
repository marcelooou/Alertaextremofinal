# 🌧️ AlertaChuva API

API REST Java Spring Boot para monitoramento de áreas de risco, desenvolvida como parte da Global Solution FIAP 2025/1.

## ✅ Funcionalidades

- Cadastro de áreas monitoradas
- Documentação Swagger
- Paginação e validação
- Segurança com Spring Security (config básica)
- Deploy com Docker

## 📦 Tecnologias

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Springdoc OpenAPI
- Spring Security
- H2 Database
- Docker

## 🚀 Executando localmente

```bash
./mvnw spring-boot:run
```

Acesse: `http://localhost:8080/swagger-ui/index.html`

## ☁️ Deploy no Render

1. Suba este projeto para um repositório no GitHub
2. Acesse [https://render.com](https://render.com)
3. Clique em "New Web Service"
4. Escolha seu repositório e conecte
5. O Render detectará o Dockerfile automaticamente

## 📁 Estrutura

```
├── src/main/java/com/fiap/alertachuva
│   ├── controller
│   ├── entity
│   ├── repository
│   ├── config
│   ├── security
├── pom.xml
├── Dockerfile
├── render.yaml
```

## 👤 Autores

- Felipe Orikasa
- Marcelo Bonfim
- Antonio Caue

