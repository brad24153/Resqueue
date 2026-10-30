# Resqueue – Shelter Management Application

A native Android application designed to streamline animal shelter management and adoption workflows.

> **Status:** Active Development (Early-Stage Architecture)
> The application currently uses the **Shelterluv API** so I can test with a reliable data source.
> Write access is limited to each device's local version of the database.
> Migration to AWS or Firebase is planned for future development.

## Tech Stack & Architecture
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Dependency Injection:** Hilt
- **APIs & Data:** RESTful APIs, JSON

## Key Features & Modules
- Modular package structure for shelter intake, pet profiles, and adoption workflows.
- Custom Compose components built with responsive, accessible layout principles.
- Integrated dependency injection using Hilt for testable, scalable state management.

## Packages with Progress
- [Data Layer](./app/src/main/java/com/blsoftware/resqueue/data)
- [Data Layer Tests](./app/src/test/java/com/blsoftware/resqueue/data)
- [Animal List Screen](./app/src/main/java/com/blsoftware/resqueue/animallist)

## Getting Started
1. Clone the repository: `git clone https://github.com/brad24153/resqueue.git`
2. Open in **Android Studio**.
3. Build & Run on an Android Emulator or physical device (API 24+).

## License
Copyright © 2026 Brad Lambert. All rights reserved.

*This codebase is displayed publicly for portfolio demonstration purposes only.*