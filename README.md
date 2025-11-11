<img width="983" height="459" alt="image" src="https://github.com/user-attachments/assets/d37e5a4b-7809-4d29-8250-0a8ff531559b" /># Arkanoid Game - Object-Oriented Programming Project

> A modern take on the classic Arkanoid game, developed using Java 17+ and JavaFX.
> This is the final project for the INT2204 (Object-Oriented Programming) course.

---

##  Demo & Screenshots

### Screenshots

* <img width="1593" height="897" alt="image" src="https://github.com/user-attachments/assets/640b3793-e4bc-4d5a-9e10-c31eefddc48d" />

* <img width="1601" height="942" alt="image" src="https://github.com/user-attachments/assets/97486254-c7ad-4af8-a9aa-cf2a631758a9" />

* <img width="675" height="894" alt="image" src="https://github.com/user-attachments/assets/c6abd3bf-af02-4d52-a483-a24dbe7fadde" />

* 

### 


https://github.com/user-attachments/assets/e7726b80-29b7-453f-881e-b798af1c39b3

Video DemoFull gameplay video is available in `docs/demo/gameplay.mp4`

---

##  About The Game

This is a classic Arkanoid game developed in Java as a final project for an Object-Oriented Programming course. The project demonstrates the implementation of OOP principles and design patterns through a feature-rich, playable game.

---

##  How to Play

### Controls

| Key | Action |
| :--- | :--- |
| `←`| Move paddle left |
| `→`| Move paddle right |
| `SPACE` | Launch ball / Shoot laser |
| `ESC` | Pause game |

### Gameplay Guide

1.  **Start the game:** Click "Start" from the main menu.
2.  **Control the paddle:** Use arrow keys to move left and right.
3.  **Launch the ball:** Press `SPACE` to launch the ball from the paddle.
4.  **Destroy bricks:** Bounce the ball to hit and destroy bricks.
5.  **Collect power-ups:** Catch falling power-ups for special abilities.
6.  **Avoid losing the ball:** Keep the ball from falling below the paddle.
7.  **Complete the level:** Destroy all destructible bricks to advance.

### Power-ups

| Name | Effect |
| :--- | :--- |
| **Expand Paddle** | Increases paddle width for 10 seconds |
| **Multi Ball** | Spawns 2 additional balls |
| **Laser Gun** | Shoot lasers to destroy bricks for 15 seconds |
| **StickyPaddle** | Ball sticks to paddle; launch with `SPACE` |
| **Pierce Ball** | Ball passes through bricks for 12 seconds |

### Scoring System

* **Normal Brick:** 10 points
* **Strong Brick:** 20 points
* **Explosive Brick:** 20 points + nearby bricks

---

##  Getting Started (Installation & Setup)

### Prerequisites

* `Java 17+`
* `JavaFX 19.0.2+` (Bundled or set up in your IDE)
* A Java-supporting IDE (e.g., IntelliJ IDEA)
* `Maven 3.9+` (for build management)

### Running the Game

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/hdungz/Arkanoid
    ```
2.  **Open the project:** Open the cloned project folder in your preferred IDE (e.g., IntelliJ IDEA).
3.  **Build the project:** Allow Maven to download dependencies and build the project.
4.  **Run the game:** Find and run the main application file (e.g., `Main.java`).

---

##  Technical Architecture

This project is built on a solid foundation of OOP principles and advanced techniques to ensure high performance.

### OOP Principles

The game fully implements the four pillars of OOP:
* **Encapsulation**
* **Inheritance**
* **Polymorphism**
* **Abstraction**

### Design Patterns Implemented

* **Singleton Pattern:** Used for `GameModel`,`LevelManager`,`LoadLevelManager`,. to ensure a single instance.
* **Factory Method Pattern:**  Used in package Paddle ,  to create different types of power-ups.
* **Strategy Pattern:** Used to define different damage-taking behaviors (takeDamage) for each Brick subclass.
* **State Pattern:** Used to manage different game states (e.g., GameState, ScenceType...).

### Multithreading Model

The game utilizes multiple concurrent threads to ensure a smooth, lag-free experience:

* **Game Loop Thread:** The "heart" of the game, updating logic (physics, collisions) by animation timer.
* **Rendering Thread:** The "artist" of the game, dedicated to drawing graphics on the screen (The main JavaFX UI Thread).
* **Audio Thread Pool:** Handles sound effects and music asynchronously, preventing audio loading from freezing the game.
* **I/O Thread:** Manages slow tasks like Saving/Loading game data without blocking the UI.

## Technologies Used

| Technology | Version | Purpose |
| :--- | :--- | :--- |
| **Java** | 17+ | Core programming language |
| **JavaFX** | 19.0.2 | GUI framework for rendering the game |
| **Maven** | 3.9+ | Build automation and dependency management |

## Future Improvements

### Planned Features

* **Additional Game Modes:**
    * Time Attack Mode
    * Survival Mode (Endless)
    * Co-op Multiplayer Mode
* **Enhanced Gameplay:**
    * Boss battles at the end of each "world"
    * More power-up varieties (e.g., Freeze Time, Shield Wall)
    * Achievements System
* **Technical Improvements:**
    * Add particle effects and advanced animations
    * Implement an AI opponent mode
    * Add an online leaderboard with a database backend

## 📄 License & Notes

* **License:** This project is developed for educational purposes only.
* **Academic Integrity:** This code is provided as a reference. Please follow your institution's academic integrity policies.
* **Assets:** Some assets (images, sounds) may be used for educational purposes under fair use.



