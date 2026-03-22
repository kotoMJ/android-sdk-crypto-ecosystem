# SDK Crypto ecosystem

Case study of the SDK ecosystem with real crypto use-case.

[![Build Status](https://github.com/kotoMJ/android-sdk-crypto-ecosystem/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/kotoMJ/android-sdk-crypto-ecosystem/actions)

### 🛡️ CodeGuard Status

| Pipeline Step       | Status                                                                                                                                                                                                     | Description                         |
|:--------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------|
| **Formatting**      | [![Spotless](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/kotoMJ/ed736b27c2e58b024ffaed9c81ff24d3/raw/badge-spotless.json)](https://github.com/kotoMJ/android-sdk-crypto-ecosystem/actions)          | Code style and formatting checks    |
| **Static Analysis** | [![Detekt](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/kotoMJ/ed736b27c2e58b024ffaed9c81ff24d3/raw/badge-detekt.json)](https://github.com/kotoMJ/android-sdk-crypto-ecosystem/actions)              | Kotlin static analysis and linting  |
| **Module Versions** | [![Unit Tests](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/kotoMJ/ed736b27c2e58b024ffaed9c81ff24d3/raw/badge-module-versions.json)](https://github.com/kotoMJ/android-sdk-crypto-ecosystem/actions) | Module versions verification        |
| **Unit Tests** | [![Unit Tests](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/kotoMJ/ed736b27c2e58b024ffaed9c81ff24d3/raw/badge-tests-unit.json)](https://github.com/kotoMJ/android-sdk-crypto-ecosystem/actions)      | JUnit logic verification            |
| **Device Tests** | [![Instrumented](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/kotoMJ/ed736b27c2e58b024ffaed9c81ff24d3/raw/badge-tests-instr.json)](https://github.com/kotoMJ/android-sdk-crypto-ecosystem/actions)   | Android Instrumented tests (API 35) |

## Sample app

<table>
  <tr>
    <td>
      <img src="extras/screens/crypto-market-list.png" width="250" alt="Kotox Crypto main screen" />
    </td>
    <td>
      <img src="extras/screens/crypto-market-detail.png" width="250" alt="Kotox Crypto detail screen" />
    </td>
  </tr>
<tr>
    <td>
      <img src="extras/screens/crypto-market-currency.png" width="250" alt="Kotox Crypto main screen" />
    </td>
    <td>
      <img src="extras/screens/crypto-news-list.png" width="250" alt="Kotox Crypto detail screen" />
    </td>
  </tr>
</table>

## Documentation

[SDK Documentation pages]

### GitHub
SDK Documentation pages source code is available on separated GitHub:  
https://github.com/kotoMJ/android-sdk-crypto-ecosystem-doc

### Versioning
SDK Ecosystem implements content based automated versioning.  
For details about versioning visit [SDK Versioning] section.

### Backend For Frontend
SDK Ecosystem has its own backend for frontend in order to handle API keys securely.

Complete backend code is available on separated GitHub:
https://github.com/kotoMJ/sdk-crypto-ecosystem-bff

For details about BFF authentication (Play Integrity vs debug bypass) see [BFF Authentication].


### Public Speaking
Basic principles of the SDK Ecosystem was presented on public Android meetups in Brno and Prague (Czech Republic)
and here are slides from Prague meetup:

**November 18, 2025** - Android Meetup in Prague at STRV   
[![Talk about SDK Ecosystem](extras/presentation/AnatomySDKEcosystem_teaser.png)](extras/presentation/AnatomySDKEcosystem_slides.pdf)

## Contribution

In order to contribute to this codebase read [Conventions] part.

[SDK Documentation pages]: https://kotomj.github.io/android-sdk-crypto-ecosystem-doc/
[SDK Versioning]: sdk/bom/VERSIONING.md
[BFF Authentication]: docs/bff-authentication.md
[Conventions]: extras/CONVENTIONS.md
