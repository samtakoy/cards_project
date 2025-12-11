# Cards

Приложение для просмотра флеш‑карточек на Kotlin Multiplatform с таргетами Android, iOS и JVM Desktop.

## Возможности

- Импорт наборов карточек из файла.
- Просмотр карточек.
- Прослушивание текста карточек через Text‑to‑Speech.

## Стек

- **Kotlin Multiplatform** — общий код для Android, iOS и Desktop.
- **Compose Multiplatform** — UI и навигация (Compose Navigation 2).
- **Compose Multiplatform Resources** — общие ресурсы (строки, картинки и т.п.).
- **Koin** — DI.
- **Kotlin Flows** — реактивные стримы данных.
- **Room** — локальная база данных.
- **Ktorfit** — сетевые запросы.
- **FileKit** — работа с файлами (импорт карточек).
- **KodeView** — отображение кода/контента карточек.
- **TextToSpeechKt** — озвучивание текста карточек.
- **Napier** — логирование.

## Статус проекта

Проект предназначен для демонстрации кода и базовой функциональности по импорту, просмотру и прослушиванию карточек. Остальные части находятся в процессе портирования с Android‑версии.

## Демонстрация

<div align="center">
      <img src="./docs/demo.gif" width="336" height="720" alt="Cards Project Demo">
  <br><small>Android</small>
</div>

## Сборка и запуск

Кратко:

- **Android**: открыть проект в Android Studio, выбрать `androidApp` и запустить на эмуляторе/устройстве.
- **Desktop (JVM)**: запустить из Gradle/IDE задачу `run` для модуля `desktopApp`.
- **iOS**: открыть проект из `iosApp` в Xcode и запустить на симуляторе/устройстве.
