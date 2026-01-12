# Java 17 Setup (Required)

Your laptop is currently configured with **Java 8**:

- `JAVA_HOME = C:\Program Files\Java\jdk-1.8`
- `java -version` shows `1.8.0_431`

But this project requires **Java 17** (and Spring Boot 3.x also requires Java 17).

## Option 1 (Recommended): Install Eclipse Temurin JDK 17
1. Download and install **Eclipse Temurin 17 (JDK)**:
   - https://adoptium.net/temurin/releases/?version=17
2. During install, enable:
   - **Set JAVA_HOME**
   - **Add to PATH**
3. Restart IntelliJ and open a new terminal.

## Option 2: Point IntelliJ + Maven to JDK 17
IntelliJ:
- File → Project Structure → **Project SDK = 17**
- Settings → Build Tools → Maven → **JDK for importer = 17**

## Verify
In a new terminal:
- `java -version` should show `17.x`
- `./mvnw.cmd -v` should show `Java version: 17`

---

Once Java 17 is active, the backend and frontend can be generated and will run on localhost.

