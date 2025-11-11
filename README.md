# Arkanoid Game - Object-Oriented Programming Project

## Author

Group [Điền số nhóm của bạn] - Class INT2204

1. Nguyễn Hữu Dũng - 24020091
2. Bùi Huy Hoàng - 24020136
3. Nguyễn Hữu Hiếu - 24020127
4. Phạm Đức Trọng - 24020334

**Instructor:** Kiều Văn Tuyền
**Semester:** HK1 - 2025-2026

## Description

This is a classic Arkanoid game developed in Java as a final project for Object-Oriented Programming course. The project demonstrates the implementation of OOP principles and design patterns.

### Key features:

* The game is developed using `Java 17+` with `JavaFX` for GUI.
* Implements core OOP principles: Encapsulation, Inheritance, Polymorphism, and Abstraction.
* Applies multiple design patterns: Singleton, Factory Method, Strategy, Observer, and State.
* Features multithreading for smooth gameplay and responsive UI.
* Includes sound effects, animations, and power-up systems.
* Supports save/load game functionality.

### Game mechanics:

* **Core Gameplay:** Control a paddle to bounce a ball and destroy bricks.
* **Lives System:** Players start with a limited number of lives and lose one when the ball falls off the bottom of the screen.
* **Advanced Physics:** The ball's reflection angle changes dynamically based on where it hits the paddle.
* **Power-ups:** Collect falling power-ups to gain special abilities.
* **Level Progression:** Advance through multiple levels with increasing difficulty and challenges.
* **In-Game Economy:**
    * Earn points (score) and collect **Coins**.
    * Use Coins in the **Shop** to purchase new **skins** for the paddle and ball.
## UML Diagram
### Class Diagram
## Multithreading Implementation
The game uses multiple threads to ensure smooth performance:
* **Game Loop Thread:** Updates game logic at 60 FPS.
* **Rendering Thread:** Handles graphics rendering (EDT for JavaFX Application Thread).
* **Audio Thread Pool:** Plays sound effects asynchronously.
* **I/O Thread:** Handles save/load operations without blocking UI.
* **Asset Loading Thread:** Asset Loading Thread: Pre-loads game resources (e.g., images, sound files, level maps) for the next level in the background.
## Installation

1.  Clone the project from the repository.
    ```bash
    git clone https://github.com/hdungz/Arkanoid
    ```
2.  Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
3.  Build and run the main application file.

# Arkanoid Game - Object-Oriented Programming Project

> A modern take on the classic Arkanoid game, developed using Java 17+ and JavaFX.
> This is the final project for the INT2204 (Object-Oriented Programming) course.

---

##  Demo & Screenshots

### Screenshots

* 
* 
* 
* 

### Video Demo

Full gameplay video is available in `docs/demo/gameplay.mp4`

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
| `P` or `ESC` | Pause game |
| `R` | Restart game |
| `Q` | Quit to menu |

### Gameplay Guide

1.  **Start the game:** Click "New Game" from the main menu.
2.  **Control the paddle:** Use arrow keys or A/D to move left and right.
3.  **Launch the ball:** Press `SPACE` to launch the ball from the paddle.
4.  **Destroy bricks:** Bounce the ball to hit and destroy bricks.
5.  **Collect power-ups:** Catch falling power-ups for special abilities.
6.  **Avoid losing the ball:** Keep the ball from falling below the paddle.
7.  **Complete the level:** Destroy all destructible bricks to advance.

### Power-ups

| Icon | Name | Effect |
| :--- | :--- | :--- |
| 🟦 | **Expand Paddle** | Increases paddle width for 10 seconds |
| 🟥 | **Shrink Paddle** | Decreases paddle width for 10 seconds |
| ⚡ | **Fast Ball** | Increases ball speed by 30% |
| 🐌 | **Slow Ball** | Decreases ball speed by 30% |
| 🎯 | **Multi Ball** | Spawns 2 additional balls |
| 🔫 | **Laser Gun** | Shoot lasers to destroy bricks for 15 seconds |
| 🧲 | **Magnet** | Ball sticks to paddle; launch with `SPACE` |
| 🛡️ | **Shield** | Protects from losing one life |
| 🔥 | **Fire Ball** | Ball passes through bricks for 12 seconds |

### Scoring System

* **Normal Brick:** 100 points
* **Strong Brick:** 300 points
* **Explosive Brick:** 500 points + nearby bricks
* **Power-up Collection:** 50 points
* **Combo Multiplier:** x2, x3, x4... for consecutive hits

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
    git clone [https://github.com/hdungz/Arkanoid](https://github.com/hdungz/Arkanoid)
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

* **Singleton Pattern:** Used for `GameManager`, `AudioManager`, etc. to ensure a single instance.
* **Factory Method Pattern:** Used to create different types of bricks and power-ups.
* **Strategy Pattern:** Used to define different ball movement or AI behaviors.
* **Observer Pattern:** Used to notify the UI (e.g., score, lives) of changes in the game state.
* **State Pattern:** Used to manage different game states (e.g., Main Menu, Playing, Paused, Game Over).

### Multithreading Model

The game utilizes multiple concurrent threads to ensure a smooth, lag-free experience:

* **Game Loop Thread:** The "heart" of the game, updating logic (physics, collisions) 60 times per second (60 FPS).
* **Rendering Thread:** The "artist" of the game, dedicated to drawing graphics on the screen (The main JavaFX UI Thread).
* **Audio Thread Pool:** Handles sound effects and music asynchronously, preventing audio loading from freezing the game.
* **I/O Thread:** Manages slow tasks like Saving/Loading game data without blocking the UI.
* **Asset Loading Thread:** Pre-loads resources (images, level maps) for the next level in the background, allowing for seamless level transitions.

### UML Diagrams

* **Class Diagram:** Detailed class diagrams are available in the `/docs/uml/` directory.

---

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
