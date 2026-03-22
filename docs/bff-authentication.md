# BFF Authentication & Deployment

All SDK network calls to the BFF (`bff-service-1029057924274.us-central1.run.app`) require authentication. The mechanism differs between debug and release builds.

## How it works

Every BFF request carries a **Play Integrity token** in the JSON body (e.g., `{ "integrityToken": "..." }`). The BFF server validates this token to verify the request comes from a genuine, unmodified app on a real device.

In debug builds, Play Integrity validation would fail (unsigned APK, sideloaded install), so a **bypass header** is used instead.

### Request flow

```
┌─────────────────┐         POST /sentry/android          ┌─────────┐
│   Android App   │ ──────────────────────────────────────>│   BFF   │
│                 │  Body: { integrityToken: "..." }       │         │
│                 │  Header (debug only):                  │         │
│                 │    X-Kotox-Bypass-Key: <secret>        │         │
└─────────────────┘                                        └─────────┘
```

## Debug builds (local development)

**Auth method:** `X-Kotox-Bypass-Key` header

The bypass key lets you call the BFF without passing Play Integrity validation. It is only available in debug builds.

### Setup

Set `BFF_CRYPTO_ADMIN_BYPASS_SECRET` as either:

- An **environment variable**: `export BFF_CRYPTO_ADMIN_BYPASS_SECRET=your_secret`
- A **Gradle property** in `local.properties`: `BFF_CRYPTO_ADMIN_BYPASS_SECRET=your_secret`

The build reads it via `getPropertyOrVariable()` and writes it into `BuildConfig` for the debug variant only.

### How it's wired

1. **Build-time** (`sdk/internal/integrity/build.gradle.kts`):
   - `debug` → injects the real secret into `BuildConfig.BFF_CRYPTO_ADMIN_BYPASS_SECRET`
   - `release` → injects `""` (empty string)

2. **Runtime** (`IntegrityImpl.getSecurityHeader()`):
   - Returns `SdkSecurityHeader("X-Kotox-Bypass-Key", secret)` only when **both** `BuildConfig.DEBUG == true` **and** the secret is not blank
   - Otherwise returns `null`

3. **Network layer**:
   - `KtorfitFactory` installs the header as a `DefaultRequest` plugin (used by News, CoinData, etc.)
   - `SentryProvider` attaches it per-request via `integrity.getSecurityHeader()?.let { header(it.key, it.value) }`

4. **BFF server-side**: When it sees a valid `X-Kotox-Bypass-Key`, it skips Play Integrity token verification.

## Release builds (production)

**Auth method:** Play Integrity token verification

No bypass key is available. The BFF validates the integrity token in the request body against Google's Play Integrity API.

### How it's wired

1. `IntegrityProvider.prepare()` warms up the `StandardIntegrityTokenProvider` during SDK init
2. Before each BFF call, `getFreshToken(requestHash)` obtains a token bound to the specific request content (SHA-256 hash)
3. The token is sent in the JSON body (e.g., `{ "integrityToken": "..." }`)
4. `getSecurityHeader()` returns `null` in release — no bypass header is attached
5. The BFF validates the token server-side and rejects requests that fail verification (HTTP 403)

## Security guarantees

The bypass secret is protected by **two independent safeguards**:

| Layer      | Mechanism                                                                 |
|------------|---------------------------------------------------------------------------|
| Build-time | `build.gradle.kts` injects `""` for all non-debug build types            |
| Runtime    | `IntegrityImpl` checks `BuildConfig.DEBUG` before returning the header    |

Even if someone were to inject a secret via a Gradle property into a release build, the runtime `BuildConfig.DEBUG` check would still prevent it from being used.

## CI

GitHub Actions CI sets `BFF_CRYPTO_ADMIN_BYPASS_SECRET` from repository secrets (`.github/workflows/ci.yml`), enabling debug-variant integration tests to call the BFF.