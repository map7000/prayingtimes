#!/bin/sh
set -eu

# The Rust service serves both the web UI and the prayer-times API.
prayer-times-calculator &
APP_PID=$!

# Timeskeeper exposes the public REST API used by the Telegram bot.
java -jar /app/timeskeeper.jar &
TIMESKEEPER_PID=$!

BOT_PID=""
if [ -n "${BOT_TOKEN:-}" ]; then
  java -jar /app/telegrambot.jar &
  BOT_PID=$!
else
  echo "BOT_TOKEN is not set; starting without the Telegram bot."
fi

shutdown() {
  kill "$APP_PID" "$TIMESKEEPER_PID" ${BOT_PID:+"$BOT_PID"} 2>/dev/null || true
}
trap shutdown INT TERM

# Keep the container alive as long as the UI/API process runs.
wait "$APP_PID"
