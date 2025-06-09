# 🔥🌲 ALERTA INCÊNDIO FLORESTAL API - Monitoramento de Eventos Extremos (FIAP Global Solution) 🚨

Bem-vindo(a) ao repositório da API "Alerta Incêndio Florestal"! Este projeto é uma API RESTful desenvolvida em Spring Boot para gerenciar e responder a **eventos extremos, com foco em incêndios florestais**, visando a prevenção, detecção e resposta rápida a focos de fogo.

---

## 🚀 Sobre o Projeto

A API gerencia dados de áreas monitoradas, sensores, leituras, **alertas de incêndio** e equipes de resposta. Implementa segurança JWT e é conteinerizada com Docker e Docker Compose, seguindo práticas de infraestrutura como código.

---

## 🛠️ Tecnologias Utilizadas

* **Backend:** Java 17, Spring Boot, Spring Data JPA.
* **Banco de Dados:** Oracle XE (Docker Container).
* **Conteinerização:** Docker, Docker Compose.
* **Segurança:** Spring Security, JWT.
* **Documentação API:** Swagger/OpenAPI (SpringDoc).
* **Automação CI/CD:** GitHub Actions.

---

## ⚙️ Pré-requisitos

* JDK 17
* Maven
* Docker Desktop
* Postman ou Insomnia
* Conta no Docker Hub e GitHub

---

## 🚀 Como Rodar Localmente

Sua API estará disponível em `http://localhost:8080`.

1.  **Clone o Repositório:**
    ```bash
    git clone [https://github.com/marcelooou/Alertaextremofinal.git](https://github.com/marcelooou/Alertaextremofinal.git)
    cd Alertaextremofinal
    ```
2.  **Gere e Configure a Chave Secreta JWT:**
    * Execute no terminal para gerar a chave:
        ```bash
        mvn compile exec:java -Dexec.mainClass="io.jsonwebtoken.security.Keys" -Dexec.args="HS256" -q
        ```
    * **COPIE A CHAVE INTEIRA.**
    * No `docker-compose.yml`, na seção `app-java`, cole a chave copiada em `JWT_SECRET`.
3.  **Suba o Ambiente Docker:**
    * No terminal:
        ```bash
        docker-compose down -v
        docker-compose up --build -d
        ```
    * Aguarde a inicialização completa.

4.  **Use a API (Swagger UI):**
    * Acesse `http://localhost:8080/swagger-ui.html` no navegador.

---

## 🔒 Fluxo de Autenticação JWT

1.  **Registrar Usuário (`POST /auth/register`)**
    * **URL:** `http://localhost:8080/auth/register`
    * **Body (JSON):** `{"username": "seu_usuario", "password": "SuaSenhaSegura123!"}`
    * **Espere:** `200 OK` com `{"message": "Usuário registrado com sucesso!"}`.

2.  **Fazer Login (`POST /auth/login`)**
    * **URL:** `http://localhost:8080/auth/login`
    * **Body (JSON):** Use as credenciais do registro.
    * **Espere:** `200 OK` com seu `token` JWT. **COPIE ESTE TOKEN INTEIRO.**

3.  **Acessar Rotas Protegidas (`GET /areas`)**
    * **URL:** `http://localhost:8080/areas`
    * **Auth:** `Bearer Token`, cole o JWT completo.
    * **Espere:** `200 OK` com a lista de áreas.

---

## 🚧 Desafios e Soluções

Na jornada de desenvolvimento, superamos desafios comuns em projetos DevOps e Cloud:

* **Condição de Corrida Oracle (`ORA-12514`, `ORA-01033`):** O Oracle demorava para inicializar. Implementamos um script `wait-for-it.sh` no `Dockerfile` que faz a API esperar pela porta do Oracle e adiciona um tempo extra para o banco estabilizar.
* **Chave JWT Inválida (`Illegal base64 character`):** A chave secreta inicial não era Base64. Geramos uma nova chave Base64 válida.
* **`403 Forbidden` no Registro:** O endpoint `POST /auth/register` era bloqueado. Corrigimos ajustando as regras de autorização no `SecurityConfig.java` e desabilitando o CSRF para APIs.
* **`sqlplus: not found`:** O script `wait-for-it.sh` tentou usar `sqlplus` que não estava no container da aplicação. Solução: Simplificamos o `wait-for-it.sh` para apenas verificar a porta TCP.

---

## ☁️ Deploy em Nuvem & CI/CD

* **Deploy Automatizado:** O projeto é implantado em uma VM Linux no Azure. Após clonar o repositório e instalar Docker/Docker Compose na VM, basta rodar `docker-compose up --build -d`. A porta `8080` deve ser aberta no firewall da VM (NSG no Azure).
* **Pipeline CI/CD:** Um workflow no GitHub Actions automatiza o build da aplicação Java e o push da imagem Docker para o Docker Hub a cada `git push` para a branch `main`. As credenciais do Docker Hub são gerenciadas com segurança via GitHub Secrets.

---

## 👨‍💻 Autores

* **Marcelo Siqueira Bonfim** – RM558254
* **ANTONIO CAUE ARAUJODA SILVA** - RM558891
* **Felipe Gomes Costa Orikasa** - RM557435

---