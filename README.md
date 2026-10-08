# Morpion (Android)

Jeu de morpion à deux joueurs, Kotlin + Jetpack Compose.
Développé avec une méthode « AI-native SDLC » : spécification → plan → implémentation → tests → revue (voir `docs/` et `CLAUDE.md`).

## Lancer
Ouvrir le dossier dans Android Studio (Koala+), laisser Gradle synchroniser, puis Run ▶.
Tests de la logique : `gradle :app:testDebugUnitTest` (ou via Android Studio).

## Autres projets
- [`blocmon/`](blocmon/) : explorer un monde en blocs et capturer des créatures (Android, Compose). APK : onglet Actions → artifact `blocmon-debug-apk`.
