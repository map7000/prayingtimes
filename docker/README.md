# All-in-one image

`Dockerfile.all` builds a single image containing:

| Component | Port | Purpose |
| --- | --- | --- |
| prayer-times-calculator | 3000 | Rust API **and** the built web UI at `/` |
| prayingtimes-timeskeeper | 8080 | Public REST API for the Telegram bot |
| prayingtimes-telegrambot | — | Started only when `BOT_TOKEN` is set |
| prayer-times-web-ui | 3000 | Static assets served by the calculator |

The web UI is compiled with a relative API base, so it talks to the same origin
that serves it (`/api/*`, `/health`). Timeskeeper talks to the calculator over
`127.0.0.1:3000`.

## Build

The build context must be the directory containing all three repositories:

```sh
cd /path/to/workspace   # contains prayingtimes/, prayer-times-calculator/, prayer-times-web-ui/
docker build -f prayingtimes/Dockerfile.all -t prayingtimes-all .
```

## Run

UI + calculator only:

```sh
docker run --rm -p 3000:3000 -p 8080:8080 prayingtimes-all
# http://localhost:3000
```

With the Telegram bot (requires PostgreSQL):

```sh
docker run --rm -p 3000:3000 -p 8080:8080 \
  -e BOT_TOKEN=... \
  -e DATABASE_URL=jdbc:postgresql://host:5432/postgres \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=... \
  prayingtimes-all
```

## Notes

- `PRAYER_TIMES_API_URL` defaults to `http://127.0.0.1:3000` and can be
  overridden if timeskeeper should use a different provider instance.
- `STATIC_DIR` defaults to `/app/static`.
- The image is intentionally small: Rust is built against musl and the runtime
  is a JRE Alpine image; Maven/Node/Rust toolchains stay in the build stages.
