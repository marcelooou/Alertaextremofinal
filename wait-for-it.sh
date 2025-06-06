#!/bin/sh
# wait-for-it.sh

# Este script espera até que um host:port esteja acessível
# e então espera um tempo adicional para garantir que o serviço de banco de dados esteja realmente pronto.
# Uso: ./wait-for-it.sh host:port [-- command]

HOST=${1%:*}
PORT=${1#*:}
shift

# Atraso inicial opcional para garantir que o serviço de rede suba
sleep 5

echo "Waiting for ${HOST}:${PORT}..."

# Espera até que a porta TCP esteja aberta
while ! nc -z "${HOST}" "${PORT}"; do
  echo "Still waiting for ${HOST}:${PORT}..."
  sleep 1
done

echo "${HOST}:${PORT} is available. Giving Oracle extra time to fully initialize..."

# ADICIONE UM TEMPO DE ESPERA EXTRA AQUI (ex: 30-60 segundos)
# Isso é para o caso de o listener estar UP, mas a instância do banco ainda não totalmente pronta.
sleep 45 # <--- AUMENTADO PARA 45 SEGUNDOS. Pode tentar 60 se necessário.

echo "Extra wait complete. Starting application..."

# Executa o comando final (sua aplicação Java)
exec "$@"