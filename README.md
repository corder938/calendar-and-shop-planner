# MyCalendar & Shop Planner

This is my personal project — an Android app that combines a calendar and a shopping list. I built it to practice **Jetpack Compose** and modern Android development.

## Technologies I used
- **Kotlin**: The main language.
- **Jetpack Compose**: For building the UI without XML.
- **Coroutines & Flow**: To handle data and keep the UI responsive.
- **MVVM**: To separate business logic from the UI.
- **Material 3**: For a modern look and dark mode support.

## Main Features

### Calendar
- I created a grid logic that shows 42 days (6 weeks) to correctly display any month.
- You can switch months using arrows.
- It highlights "today" and handles dates using the `java.time` library.

### Shopping List
- I used `StateFlow` to make the list reactive. When you check an item, it moves to the "Completed" section automatically.
- You can add new products and they are linked to specific dates.
- I used `AtomicLong` for generating unique IDs for items in memory.

## How it's built
- **Clean Logic:** I kept the UI "stateless" by moving all the data processing into the `ViewModel`.
- **Flow Operators:** I used `combine` and `map` to filter shopping items in real-time.
- **YearMonth API:** This helps to easily calculate how many days to show before and after the current month.

## Screenshots
![Screenshot_20260120_181537_MyCalendar](https://github.com/user-attachments/assets/386fe11e-3e99-40b3-af23-0e660cab9a33)
![Screenshot_20260120_181520_MyCalendar](https://github.com/user-attachments/assets/6dd35794-35a5-48ce-a0d8-953f29fc8dd2)
![Screenshot_20260120_183114_MyCalendar](https://github.com/user-attachments/assets/ca1d7567-e16d-413b-9bc7-ddca81251939)
![Screenshot_20260120_183059_MyCalendar](https://github.com/user-attachments/assets/6ba9b5d5-cf6a-47f6-81ee-9ba43df41f01)


## 🛠 How to run
1. Clone the repo.
2. Open it in Android Studio.
3. Run it on any device with **API 26+**.

## 🛠 Work in Progress & Future Plans

This project is currently in the early stages of development. I'm actively working on it and plan to add the following features soon:

* **Local Database (Room):** Right now, data is stored in memory and resets after the app closes. I am going to implement **Room Database** for persistent storage.
* **Calendar Views:** I plan to add different view modes, allowing users to switch between **Month, Week, and Day** layouts (currently only Month is available).
* **Navigation Component (Bottom Bar):** Currently, screens are switched manually. I plan to implement **Jetpack Navigation** with a Bottom Navigation Bar to allow smooth transitions between the Calendar, Shop Planner, and Settings screens.
* **Dependency Injection (Hilt):** I want to refactor the project and add **Hilt** to manage ViewModels and Data layers more professionally.
* **Reminders & Notifications:** Adding local notifications to remind users about their planned events.
* **Unit Testing:** Writing tests for the calendar logic to ensure all date calculations are 100% accurate.

I’m constantly learning new Android best practices, so I will be refactoring this code as I gain more experience!
