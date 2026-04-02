## Personal Finance Companion

This project is a basic Kotlin Multiplatform personal finance app built with Jetpack Compose and a simple MVVM structure. It is designed as an assessment-style submission that focuses on product clarity, clean code organization, and a polished mobile-friendly UI inspired by the provided purple finance dashboard reference.

### What is included

- Home dashboard with current balance, income, expenses, savings progress, and a pie chart
- Transaction management with add, edit, delete, search, and filter flows
- Goal screen with a monthly savings challenge and budget tracker cards
- Insights screen with compact summary cards and category comparisons
- Shared UI and business logic for Android and iOS through Compose Multiplatform
- Local in-memory repository with seeded sample data to keep the project simple and easy to review

### Architecture

The shared code uses a lightweight MVVM structure:

- `data/model`: finance entities and form models
- `data/repository`: repository abstraction plus a simple in-memory implementation
- `presentation`: `FinanceViewModel`, UI state, and tab/filter logic
- `presentation/components`: reusable cards, charts, bottom navigation, and dialog components
- `presentation/screens`: Home, Transactions, Goals, and Insights screens
- `theme`: colors and typography matching the visual direction of the reference

### Design decisions

- The app keeps the interaction model straightforward instead of introducing advanced navigation or persistence
- Mock local data was chosen so the UI, state handling, and app structure remain the main focus
- The color palette, white card treatment, rounded corners, and gradient header follow the reference image while staying practical in Compose
- The screens are intentionally compact and phone-first so the app feels like a real mobile product rather than a desktop dashboard compressed onto a small display

### Build and run

For Android:

```sh
./gradlew :composeApp:assembleDebug
```

For iOS:

1. Open `/iosApp` in Xcode
2. Make sure an iOS Simulator runtime is installed
3. Run the `iosApp` scheme

### Assumptions

- Data is stored in memory for this assignment
- Dates are entered as `YYYY-MM-DD` for simplicity
- The project prioritizes readable engineering practices and complete flows over production-level backend integration
