# Campus Course & Records Manager (CCRM) 

A console-based academic management system built in **Java SE 17** that handles student records, course catalogs, enrollments, and grade tracking, with full file-based persistence and automatic backups.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack & Libraries](#tech-stack--libraries)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Building the Project](#building-the-project)
  - [Running the Application](#running-the-application)
- [Usage Guide](#usage-guide)
- [Design Patterns & Architecture](#design-patterns--architecture)
- [Data Persistence & Backups](#data-persistence--backups)
- [Custom Exceptions](#custom-exceptions)
- [Future Improvements](#future-improvements)
- [License](#license)

## Overview

CCRM is a fully menu-driven Java application that digitises core administrative workflows at a university or college. It enforces real business rules, preventing duplicate enrollments, capping credit loads, and validating emails, and stores all data in human-readable flat files that are automatically backed up before every write.

The project was built as a demonstration of idiomatic, modern Java: OOP design with abstract classes and interfaces, functional-style data processing with Streams, safe construction with the Builder pattern, and robust I/O with NIO.2.

## Features

### Student Management
- Add, view, search, update, and delete student records
- Email validation via regex (`java.util.regex.Pattern`)
- Auto-incremented, thread-safe student IDs (`AtomicInteger`)

### Course Management
- Create courses using a fluent Builder API
- Set credit hours, seat capacity, and semester
- List all courses or filter by available seats / semester

### Enrollment
- Enroll a student in a course with full business rule enforcement
- Prevents duplicate enrollments (`DuplicateEnrollmentException`)
- Blocks enrollment when credit limit is exceeded (`MaxCreditLimitExceededException`)
- Respects per-course seat capacity

### Grade & GPA Tracking
- Record letter grades (`A+`, `A`, `B+`, ... `F`) per enrollment
- Compute cumulative GPA automatically
- Determine academic standing (Good Standing, Probation, etc.)

### Reporting
- List students by academic standing
- View courses with remaining seats
- Generate enrollment summaries per student or course

### Persistence & Backups
- All data saved to plain-text files in `data/`
- Timestamped automatic backups created in `backup/` before every write operation

## Tech Stack & Libraries

| Category | Details |
|---|---|
| **Language** | Java SE 17 |
| **Collections** | `List`, `ArrayList`, `Optional`, `Objects`, `Comparator` |
| **Streams** | `Stream`, `Collectors`, lambdas, `Predicate`, `Consumer`, `Function` |
| **Date / Time** | `LocalDate`, `LocalDateTime`, `DateTimeFormatter` |
| **File I/O** | `java.nio.file` — `Path`, `Files`, `Paths`, `StandardCopyOption` |
| **Concurrency** | `AtomicInteger` for thread-safe ID generation |
| **Regex** | `java.util.regex.Pattern` for email validation |
| **Control Flow** | `switch`, `do-while`, `for`, enhanced `for`, `break`, `continue` |
| **Assertions** | `assert` keyword for invariant checking |

## Project Structure

```
Campus_Course_And_Records_Manager/
├── src/                    # Source code (package: edu.ccrm)
│   └── edu/
│       └── ccrm/           # All Java classes
├── data/                   # Persistent data files (students, courses, enrollments)
├── backup/                 # Timestamped backup folders (e.g., backup_20260330_233121/)
├── bin/                    # Compiled .class files
├── Screenshots/            # Demo images
├── REQUIREMENTS.md         # Complete technical specification
├── .vscode/                # VS Code settings & recommendations
├── .gitattributes
└── README.md               # ← You are here
```

## Getting Started

### Prerequisites

- **Java SE 17** or higher
  ```bash
  java -version   # should print: openjdk 17.x.x or similar
  ```
- Any IDE (VS Code with Extension Pack for Java, IntelliJ IDEA, Eclipse) or just a terminal.

### Building the Project

**From the terminal (javac):**
```bash
# From the project root
javac -d bin src/**/*.java
```

**Using VS Code:**  
Open the folder in VS Code. With the Java Extension Pack installed, press `F5` or click **Run** on `Main.java`.

**Using IntelliJ IDEA:**  
`File > Open` the project root, mark `src/` as Sources Root, then run `Main.java`.

### Running the Application

```bash
# After compiling with javac
java -cp bin Main
```

The application will create the `data/` directory automatically on first run.

## Usage Guide

Once launched, you will see the main menu:

```
===== Campus Course & Records Manager =====
1. Student Management
2. Course Management
3. Enrollment Management
4. Grade Management
5. Reports
0. Exit
```

Navigate by entering the number for any option. Sub-menus follow the same pattern.

**Example flow to enroll a student:**
1. Select `1 > Add Student`, enter name and email.
2. Select `2 > Add Course`, enter course code, title, credits, and max seats.
3. Select `3 > Enroll Student`, enter the student ID and course code.
4. Select `4 > Record Grade` after the semester to record the outcome.
5. Select `5 > Reports > Student GPA` to view the computed GPA.

## Design Patterns & Architecture

### Layered Architecture (MVC-inspired)

| Layer | Classes |
|---|---|
| **Model** | `Person`, `Student`, `Course`, `Enrollment`, `Grade`, `Semester`, `CourseCode` |
| **Service** | `StudentManager`, `CourseManager`, `EnrollmentManager` |
| **Config** | `AppConfig` (Singleton) |
| **View / Controller** | `Main` (menu handlers) |

### Builder Pattern `Course`

Courses have many optional attributes (capacity, semester, description). The `Course.Builder` inner class prevents partially-initialised objects and keeps call sites readable:

```java
Course cs101 = new Course.Builder("CS101", "Introduction to Programming")
    .credits(3)
    .maxSeats(40)
    .semester(Semester.FALL_2025)
    .build();
```

### Singleton `AppConfig`

All configuration (data directory, backup path, max credits per semester) lives in one place:

```java
int limit = AppConfig.getInstance().getMaxCreditsPerSemester();
```

### Immutable Value Type `CourseCode`

`CourseCode` is a `final` class with all fields `final`. A course's identity cannot change after creation, making it safe to use as a map key or in equality checks without defensive copies.

### Enum with Behaviour `Grade`

```java
public enum Grade {
    A_PLUS(4.0), A(4.0), B_PLUS(3.5), B(3.0), /* ... */ F(0.0);
    private final double points;
    Grade(double points) { this.points = points; }
    public double getPoints() { return points; }
}
```

## Data Persistence & Backups

Data is stored as plain text in the `data/` directory. Before every write, the current state is copied to a timestamped folder:

```
backup/
└── backup_20260330_233121/
    ├── students.txt
    ├── courses.txt
    └── enrollments.txt
```

This is handled via `java.nio.file.Files.copy` with `StandardCopyOption.REPLACE_EXISTING`. To restore a backup, copy the contents of any timestamped folder back into `data/`.

## Custom Exceptions

| Exception | Thrown when |
|---|---|
| `DuplicateEnrollmentException` | A student attempts to enroll in the same course in the same semester more than once |
| `MaxCreditLimitExceededException` | Adding a course would push the student's credit load past the configured semester maximum |

Both exceptions are checked, meaning the compiler enforces that callers handle them explicitly.

## Future Improvements

- [ ] **Unit tests** — JUnit 5 test suite for manager classes and business rule enforcement
- [ ] **Database backend** — replace flat files with SQLite via JDBC for referential integrity
- [ ] **CSV / PDF export** — allow reports to be saved as downloadable files
- [ ] **Logging** — replace `System.out` status messages with SLF4J + Logback
- [ ] **Instructor entity** — extend the domain model to assign instructors to courses
- [ ] **Prerequisites** — enforce course prerequisite chains at enrollment time

Built with Java SE 17 by [Chetan Prakash](https://github.com/ChetanDixit-0717)
