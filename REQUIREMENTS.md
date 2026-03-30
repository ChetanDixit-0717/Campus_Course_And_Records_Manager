# Technical Requirements & Libraries Used

This document outlines the core Java libraries, APIs, and language features utilized in the **Campus Course & Records Manager (CCRM)** project.

---

## Java Version

- **Java SE 17** (built and tested)

---

## Control Flow

- `switch` statements
- `do-while`, `for`, and enhanced `for` loops
- `if-else` conditionals
- `break` and `continue` statements

---

## Assertions

- `assert` keyword for invariant checking

---

## Regular Expressions

- `java.util.regex.Pattern` (email validation)

---

## Concurrency

- `java.util.concurrent.atomic.AtomicInteger` (thread-safe counters)

---

## Collections & Core APIs

- `java.util.List`
- `java.util.ArrayList`
- `java.util.Optional`
- `java.util.Objects`
- `java.util.Scanner`
- `java.util.Comparator`

---

## Date/Time API

- `java.time.LocalDate`
- `java.time.LocalDateTime`
- `java.time.format.DateTimeFormatter`

---

## Streams & Functional Programming

- `java.util.stream.Stream`
- `java.util.stream.Collectors`
- Lambda expressions (`->`) for filtering, mapping, forEach
- Functional interfaces: `Predicate`, `Consumer`, `Function`

---

## Object-Oriented Programming (OOP) & Design

- **Classes & Objects**
  - `abstract class` (`Person`)
  - `enum` with constructors and fields (`Grade`, `Semester`)
  - `final` immutable class (`CourseCode`)
- **Inheritance**
  - `extends` keyword for subclassing
- **Interfaces**
  - `interface` keyword (`Persistable`)
- **Nested Classes**
  - `static class` for Builder pattern (`Course.Builder`)
- **Design Patterns**
  - Singleton (`AppConfig`)
  - Builder (`Course`)

---

## File I/O & Exception Handling

- **NIO.2 (New I/O)**
  - `java.nio.file.Path`
  - `java.nio.file.Files`
  - `java.nio.file.Paths`
  - `java.nio.file.StandardCopyOption`
- **Exception Handling**
  - `try-catch` blocks
  - `throw` and `throws` keywords
  - Custom exceptions (`DuplicateEnrollmentException`, `MaxCreditLimitExceededException`)