#!/usr/bin/env bash
set -e

FLAG=".initialized"

if [ ! -f "$FLAG" ]; then
  cat <<EOF | tee /etc/docker/daemon.json > /dev/null
{
  "dns": ["1.1.1.1", "8.8.8.8"]
}
EOF
  systemctl restart docker
  docker compose --profile build run --rm builder
  docker compose build
  touch "$FLAG"
fi

docker compose up
