#!/bin/sh
HOST=${1%:*}
PORT=${1#*:}
shift
sleep 5
echo "Waiting for ${HOST}:${PORT}..."
while ! nc -z "${HOST}" "${PORT}"; do
  echo "Still waiting for ${HOST}:${PORT}..."
  sleep 1
done
echo "${HOST}:${PORT} is available. Giving Oracle extra time to fully initialize..."
sleep 45
echo "Extra wait complete. Starting application..."
exec "$@"