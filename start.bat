@echo off
setlocal

if not exist ".initialized" (
  rem Write Docker daemon DNS config
  >"%ProgramData%\Docker\config\daemon.json" (
    echo {
    echo   "dns": ["1.1.1.1", "8.8.8.8"]
    echo }
  )

  rem Restart Docker service
  net stop com.docker.service
  net start com.docker.service

  rem Run Maven builder once
  docker compose --profile build run --rm builder

  rem Build all service images
  docker compose build

  rem Mark initialization complete
  >".initialized" echo done
)

rem Launch all services
docker compose up
