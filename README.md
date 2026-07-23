<div align="right">
  <a href="README_FR.md">🇫🇷 Lire en français</a>
</div>
<div align="center">
  <h1>ChocoStock</h1>
  <p><strong>Android app that manages your chocolate stock using AI-powered photo recognition and expiry tracking</strong></p>

  <p>
    <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform">
    <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Language">
    <img src="https://img.shields.io/github/license/Rebine0xFF/ChocoStock?style=for-the-badge" alt="GitHub License">
  </p>

  <p>
    <img src="https://img.shields.io/badge/Status-In_Progress-orange?style=for-the-badge" alt="Status">
  </p>

  <p align="center">
    <img src="https://img.shields.io/github/last-commit/Rebine0xFF/ChocoStock?style=flat-square" alt="GitHub last commit">
    <img src="https://img.shields.io/github/languages/code-size/Rebine0xFF/ChocoStock" alt="GitHub code size in bytes">
    <img src="https://img.shields.io/github/commit-activity/m/Rebine0xFF/ChocoStock?style=flat-square" alt="GitHub commit activity">
    <img src="https://img.shields.io/github/v/release/Rebine0xFF/ChocoStock?style=flat-square&label=latest%20release" alt="Latest release">
    <img src="https://img.shields.io/badge/Min%20SDK-26-blue?style=flat-square" alt="Min SDK">
  </p>
</div>

---

## Overview

ChocoStock keeps track of your chocolate stock. Take a photo of the packaging and a close-up of the expiry date. Gemini Flash identifies the product and reads the date automatically. The list is always sorted by expiry date, so you always know what to eat first.

## Features

- **Two-photo capture flow :** front of the packaging + expiry date close-up
- **AI-powered recognition :** automatic product title and expiry date extraction
- **Background analysis :** items appear in the list instantly, AI processing happens asynchronously
- **Sorted by expiry date :** closest expiry always on top
- **Manual editing :** override the AI's title or date at any time
- **Bring your own API key :** your Gemini key stays local on your device

## Demo

*demo gif*

## Tech stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose (Material 3) |
| Architecture | MVVM + Repository pattern |
| Local database | Room |
| Camera capture | CameraX |
| Networking | OkHttp |
| Image loading | Coil |
| Local settings storage | DataStore Preferences |
| Navigation | Navigation Compose |
| AI vision & OCR | Google Gemini API (`gemini-3.1-flash-lite`) |
| Testing | JUnit (unit tests) · AndroidX Test + in-memory Room database (instrumented tests) |

## Installation

### Download the APK

1. Go to the [Releases](../../releases) page and download the latest `.apk`
2. Enable "Install from unknown sources" for your file manager or browser
3. Install the APK
4. Get a free API key at [aistudio.google.com/apikey](https://aistudio.google.com/apikey)
5. Open the app, go to **Settings**, and paste your API key

### OR Build from source

```bash
git clone https://github.com/Rebine0xFF/ChocoStock.git
```

1. Open the project in Android Studio
2. Let Gradle sync the dependencies
3. Run the app on an emulator or a physical device
4. Get a free API key at [aistudio.google.com/apikey](https://aistudio.google.com/apikey) and paste it in the app's **Settings** screen
