# CodeTropics-Java-Agent-Timemachine

**Forked from [Hapi/Java-Agent-Timemachine](https://github.com/Hapi/Java-Agent-Timemachine)**

This fork brings the original TimeMachine Java Agent up to date with:

- **Java 17+ compatibility**
- **ASM 9.x support** (migrated from ASM 3.x)
- Bug fixes, test improvements, and modern Maven configuration

---

## What is TimeMachine?

_TimeMachine_ is a Java agent that enables JVM-based applications to simulate different system times at runtime—**without modifying the actual system clock**.

It’s useful for:
- Testing time-sensitive business logic
- Simulating end-of-month, leap years, etc.
- Batch processing, financial or legacy systems
- Any scenario where you need to "travel in time" inside your Java app

---

## 🚀 Main changes in this fork

- **Modernized codebase**: Now uses Java 17 language features and ASM 9 APIs
- **Cleaner Maven build**: Updated dependencies, plugin versions, and improved reliability on modern Java
- **Time handling fixes**: Absolute and relative time shifts work with correct calendar and timezone logic
- **Improved test suite**: Ported to JUnit 4.13, cleaned up old test cases

---

## 📦 Requirements

- Java 17 or later (tested with OpenJDK 17+)
- ASM 9.x or newer (see [ASM project](https://asm.ow2.org/))
- No external dependencies at runtime

---

## 🛠️ Build with Maven:

Standard build (skip Javadoc generation)
```sh
mvn clean package -Dmaven.javadoc.skip=true
```

(Recommended) Skip both tests and Javadoc for a faster build:
```sh
mvn clean package -DskipTests -Dmaven.javadoc.skip=true
```
