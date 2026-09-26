# Game KCN Android V1

Android 2.5D prototype for Game KCN. Backend is independent and uses `https://game.foodkcn.com/api/`.

## GitHub Actions

The repository includes `.github/workflows/build-apk.yml`. It installs Gradle 8.10.2 and builds a debug APK.

## Local build

Requires JDK 17 and Android SDK. If Gradle is installed:

`gradle :app:assembleDebug`

The first V1 focuses on login/register, KCN selection, character creation, a simple 2.5D-style map, work missions, wallet, vehicle/house/shop/skill/romance screens.
