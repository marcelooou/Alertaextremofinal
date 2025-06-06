# Estágio de build:
# Usa uma imagem base com Maven e JDK para compilar a aplicação
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

# Define o diretório de trabalho dentro do container de build
WORKDIR /app

# Copia o arquivo pom.xml (que contém as dependências)
# Isso permite que o Maven baixe as dependências primeiro, aproveitando o cache do Docker
COPY pom.xml .

# Copia o código-fonte da aplicação
COPY src ./src

# Compila e empacota a aplicação Spring Boot em um JAR executável
# O '-Dmaven.test.skip=true' pula os testes unitários durante o build do Docker
RUN mvn clean install -Dmaven.test.skip=true

# Estágio de execução:
# Usa uma imagem base mais leve com JRE (Java Runtime Environment) para rodar a aplicação
FROM eclipse-temurin:17-jre-alpine

# Define o diretório de trabalho dentro do container de execução
WORKDIR /app

# Expõe a porta que a aplicação Spring Boot irá usar
EXPOSE 8080

# Copia o JAR compilado do estágio de build para o estágio de execução
COPY --from=build /app/target/alertachuva-api-0.0.1-SNAPSHOT.jar app.jar

# COPIA O SCRIPT DE ESPERA AGORA (ainda como root)
COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh
# TORNA O SCRIPT EXECUTÁVEL (ainda como root)
RUN chmod +x /usr/local/bin/wait-for-it.sh

# ---- SEÇÃO DE CRIAÇÃO DO USUÁRIO E MUDANÇA DE PERMISSÃO ----
# ESTA É A ORDEM CRÍTICA PARA IMAGENS ALPINE
# 1. Cria o grupo e o usuário (ainda como root)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# 2. Muda a propriedade do diretório /app para o novo usuário/grupo (ainda como root)
RUN chown -R appuser:appgroup /app

# 3. AGORA, e somente AGORA, mude para o usuário não-root
USER appuser
# ---- FIM DA SEÇÃO DE CRIAÇÃO DO USUÁRIO ----

# DEFINE O PONTO DE ENTRADA DO CONTAINER
# O script 'wait-for-it.sh' será executado primeiro para esperar pela porta do banco de dados
# Ele esperará pelo serviço 'oracle-db' na porta '1521'
# O '--' é importante para separar os argumentos do script dos argumentos para o comando final (java -jar)
ENTRYPOINT ["/usr/local/bin/wait-for-it.sh", "oracle-db:1521", "--", "java", "-jar", "app.jar"]

# O CMD é ignorado se ENTRYPOINT é usado, mas é bom ter como fallback ou documentação
CMD ["java", "-jar", "app.jar"]