#!/usr/bin/env bash
# Simple dev starter for Qare (backend + frontend)

# --- settings (override via env if you want) ---
BACKEND_DIR="${BACKEND_DIR:-backend}"
FRONTEND_DIR="${FRONTEND_DIR:-frontend}"
BACKEND_PORT="${BACKEND_PORT:-8080}"
FRONTEND_PORT="${FRONTEND_PORT:-5173}"

echo "⚠️  Make sure no other apps are using:"
echo "   - Backend port: ${BACKEND_PORT}"
echo "   - Frontend port: ${FRONTEND_PORT}"
echo

# Start Spring Boot (background)
(
  cd "$BACKEND_DIR" || exit 1
  MVN="./mvnw"; [ -x "$MVN" ] || MVN="mvn"
  echo "▶ Starting Spring Boot on :${BACKEND_PORT} ..."
  exec "$MVN" -q spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=${BACKEND_PORT}"
) & backend_pid=$!

# Start Vite (background)
(
  cd "$FRONTEND_DIR" || exit 1
  echo "▶ Starting Vite on :${FRONTEND_PORT} ..."
  exec npm run dev -- --port "${FRONTEND_PORT}"
) & frontend_pid=$!

# Final message
echo
echo "✅ Both processes started."
echo "Open the app:  http://localhost:${FRONTEND_PORT}"
echo "API endpoint:  http://localhost:${BACKEND_PORT}"
echo "Press Ctrl+C to stop both."
echo

# Stop both on Ctrl+C
trap 'echo; echo "⏹ Stopping..."; kill "$backend_pid" "$frontend_pid" 2>/dev/null; wait; exit 0' INT

# Keep the script alive while either runs
wait -n "$backend_pid" "$frontend_pid"
