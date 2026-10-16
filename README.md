# Daily Expenses

Невеликий Android-застосунок для обліку щоденних витрат. Можна додавати витрати з назвою та сумою, переглядати загальний підсумок за день і видаляти записи після підтвердження.

## Реалізовано

- інтерфейс на Jetpack Compose і Material 3;
- перевірка назви та суми перед додаванням;
- список витрат і автоматичний перерахунок загальної суми;
- підтвердження перед видаленням запису;
- зберігання стану екрана у `ViewModel` за допомогою `StateFlow`.

Дані зберігаються в пам’яті застосунку й очищуються після його перезапуску.

## Технології

- Kotlin
- Android SDK (`minSdk 24`)
- Jetpack Compose
- AndroidX Lifecycle ViewModel и Kotlin Coroutines/Flow
- Gradle

## Запуск

Відкрийте проєкт в Android Studio, дочекайтеся завершення Gradle Sync, виберіть конфігурацію `app` та емулятор або під’єднаний Android-пристрій, а потім натисніть **Run**.

## Структура проєкту

- `app/src/main/java/com/example/dailyexpenses/model/Expense.kt` — модель витрати;
- `app/src/main/java/com/example/dailyexpenses/viewmodel/ExpenseViewModel.kt` — стан і операції додавання та видалення;
- `app/src/main/java/com/example/dailyexpenses/ui/` — екран і картка витрати;
- `app/src/main/java/com/example/dailyexpenses/MainActivity.kt` — точка входу застосунку.
