<div align="right">
  <a href="README.md">🇬🇧 Read in English</a>
</div>
<div align="center">
  <h1>ChocoStock</h1>
  <p><strong>Application Android qui gère ton stock de chocolat grâce à la reconnaissance d'image par IA et au suivi des dates de péremption</strong></p>

  <p>
    <img src="https://img.shields.io/badge/Plateforme-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Plateforme">
    <img src="https://img.shields.io/badge/Langage-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Langage">
    <img src="https://img.shields.io/github/license/Rebine0xFF/ChocoStock?style=for-the-badge" alt="Licence GitHub">
  </p>

  <p>
    <img src="https://img.shields.io/badge/Statut-En_cours-orange?style=for-the-badge" alt="Statut">
  </p>

  <p align="center">
    <img src="https://img.shields.io/github/last-commit/Rebine0xFF/ChocoStock?style=flat-square" alt="Dernier commit">
    <img src="https://img.shields.io/github/languages/code-size/Rebine0xFF/ChocoStock" alt="Taille du code">
    <img src="https://img.shields.io/github/commit-activity/m/Rebine0xFF/ChocoStock?style=flat-square" alt="Activité des commits">
    <img src="https://img.shields.io/github/v/release/Rebine0xFF/ChocoStock?style=flat-square&label=derni%C3%A8re%20version" alt="Dernière version">
    <img src="https://img.shields.io/badge/SDK%20min-26-blue?style=flat-square" alt="SDK minimum">
  </p>
</div>

---

## Présentation

ChocoStock aide à garder un œil sur ton stock de chocolat. Prends une photo de l'emballage puis un gros plan sur la date de péremption. Gemini Flash identifie le produit et lit la date automatiquement. La liste reste toujours triée par date de péremption, pour savoir en un coup d'œil quoi manger en premier.

## Fonctionnalités

- **Capture en deux photos :** devant de l'emballage + zoom sur la date de péremption
- **Reconnaissance par IA :** titre du produit et date de péremption extraits automatiquement
- **Analyse en arrière-plan :** chaque chocolat apparaît immédiatement dans la liste, l'IA travaille en tâche de fond
- **Tri automatique par date de péremption :** le plus urgent toujours en haut
- **Modification manuelle :** possibilité de corriger à tout moment le titre ou la date proposés par l'IA
- **Ta clé, ton contrôle :** chacun utilise sa propre clé API Gemini, stockée uniquement sur son téléphone

## Démo

https://github.com/user-attachments/assets/d771ff7f-dd7f-499b-861a-91d89dd3f697

## Stack technique

| Composant | Technologie |
|---|---|
| Langage | Kotlin |
| Interface | Jetpack Compose (Material 3) |
| Architecture | MVVM + pattern Repository |
| Base de données locale | Room |
| Capture caméra | CameraX |
| Réseau | OkHttp |
| Chargement d'images | Coil |
| Stockage des réglages | DataStore Preferences |
| Navigation | Navigation Compose |
| Vision par IA et OCR | API Google Gemini (`gemini-3.5-flash-lite`) |
| Tests | JUnit (tests unitaires) · AndroidX Test + base Room en mémoire (tests instrumentés) |

## Installation

### Télécharger l'APK

1. Rends-toi sur la page [Releases](../../releases) et télécharge le dernier `.apk`
2. Autorise l'installation depuis une source inconnue si ton navigateur ou gestionnaire de fichiers le demande
3. Installe l'APK
4. Récupère une clé API gratuite sur [aistudio.google.com/apikey](https://aistudio.google.com/apikey)
5. Ouvre l'application, va dans **Réglages**, et colle ta clé

### OU compiler depuis les sources

```bash
git clone https://github.com/Rebine0xFF/ChocoStock.git
```

1. Ouvre le projet dans Android Studio
2. Laisse Gradle synchroniser les dépendances
3. Lance l'application sur un émulateur ou un téléphone physique
4. Récupère une clé API gratuite sur [aistudio.google.com/apikey](https://aistudio.google.com/apikey) et colle-la dans l'écran **Réglages** de l'application
