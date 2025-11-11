# FoodShare - Complete Setup Guide

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Installation Methods](#installation-methods)
3. [Database Setup](#database-setup)
4. [Project Configuration](#project-configuration)
5. [Running the Application](#running-the-application)
6. [Troubleshooting](#troubleshooting)
7. [Demo Accounts](#demo-accounts)

---

## 🔧 Prerequisites

Before you begin, ensure you have the following installed on your system:

### Required Software
- **Java Development Kit (JDK) 17 or higher**
- **Apache Maven 3.6+** (for building the project)
- **PostgreSQL Database** (or use Supabase cloud database)
- **Git** (for cloning the repository)

### Optional Tools
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Postman** (for API testing)

---

## 📦 Installation Methods

### Method 1: Windows Installation (Recommended for Windows Users)

#### Step 1: Install Java JDK

**Option A: Using Chocolatey (Easiest)**
```powershell
# Install Chocolatey if not already installed
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install JDK 17
choco install openjdk17 -y
```

**Option B: Manual Installation**
1. Download JDK 17 from [Oracle](https://www.oracle.com/java/technologies/downloads/#java17) or [Adoptium](https://adoptium.net/)
2. Run the installer
3. Set `JAVA_HOME` environment variable:
   ```powershell
   # Open PowerShell as Administrator
   [System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-17', [System.EnvironmentVariableTarget]::Machine)
   ```
4. Add to PATH:
   ```powershell
   $env:Path += ";$env:JAVA_HOME\bin"
   ```

**Verify Installation:**
```powershell
java -version
# Should show: java version "17.x.x" or higher
```

#### Step 2: Install Apache Maven

**Option A: Using Chocolatey**
```powershell
choco install maven -y
```

**Option B: Manual Installation**
1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Set `MAVEN_HOME`:
   ```powershell
   [System.Environment]::SetEnvironmentVariable('MAVEN_HOME', 'C:\Program Files\Apache\maven', [System.EnvironmentVariableTarget]::Machine)
   ```
4. Add to PATH:
   ```powershell
   $env:Path += ";$env:MAVEN_HOME\bin"
   ```

**Verify Installation:**
```powershell
mvn -version
# Should show: Apache Maven 3.x.x
```

#### Step 3: Install PostgreSQL (Optional - if not using Supabase)

**Option A: Using Chocolatey**
```powershell
choco install postgresql -y
```

**Option B: Manual Installation**
1. Download from [PostgreSQL Official Site](https://www.postgresql.org/download/windows/)
2. Run installer and set password for `postgres` user
3. Default port: 5432

---

### Method 2: macOS Installation

#### Step 1: Install Homebrew (if not installed)
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

#### Step 2: Install Java JDK
```bash
brew install openjdk@17

# Link it to system Java
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk

# Set JAVA_HOME in ~/.zshrc or ~/.bash_profile
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc
```

#### Step 3: Install Maven
```bash
brew install maven
```

#### Step 4: Install PostgreSQL (Optional)
```bash
brew install postgresql@14
brew services start postgresql@14
```

**Verify Installations:**
```bash
java -version
mvn -version
psql --version
```

---

### Method 3: Linux (Ubuntu/Debian) Installation

#### Step 1: Update Package Lists
```bash
sudo apt update
```

#### Step 2: Install Java JDK
```bash
sudo apt install openjdk-17-jdk -y

# Set JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

#### Step 3: Install Maven
```bash
sudo apt install maven -y
```

#### Step 4: Install PostgreSQL (Optional)
```bash
sudo apt install postgresql postgresql-contrib -y
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

**Verify Installations:**
```bash
java -version
mvn -version
psql --version
```

---

## 🗄️ Database Setup

### Option 1: Using Supabase (Recommended - No Local Installation Required)

The project is already configured to use Supabase cloud database. **No additional setup needed** - the database is already set up and running!

**Database Details:**
- **Host**: aws-0-ap-south-1.pooler.supabase.com
- **Port**: 5432
- **Database**: postgres
- **Schema**: All tables are already created
- **Sample Data**: Pre-populated with demo users and donations

### Option 2: Local PostgreSQL Setup

If you prefer to use a local PostgreSQL database:

#### Step 1: Create Database
```bash
# Login to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE foodshare;

# Exit
\q
```

#### Step 2: Update Configuration
Edit `src/main/resources/application.properties`:

```properties
# Replace Supabase configuration with local database
spring.datasource.url=jdbc:postgresql://localhost:5432/foodshare
spring.datasource.username=postgres
spring.datasource.password=your_postgres_password
```

#### Step 3: Initialize Database
The application will automatically create tables on first run. To manually run SQL scripts:

```bash
psql -U postgres -d foodshare -f src/main/resources/init.sql
psql -U postgres -d foodshare -f src/main/resources/data.sql
```

---

## ⚙️ Project Configuration

### Step 1: Clone the Repository

```bash
# Using HTTPS
git clone https://github.com/yourusername/FoodShare.git
cd FoodShare

# OR using SSH
git clone git@github.com:yourusername/FoodShare.git
cd FoodShare
```

### Step 2: Verify Configuration File

Check `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8083

# Database Configuration (Supabase - Already configured)
spring.datasource.url=jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:5432/postgres
spring.datasource.username=postgres.xxxxxxxxxxx
spring.datasource.password=your_password_here

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging
logging.level.com.example.foodshare=DEBUG
```

### Step 3: Install Project Dependencies

```bash
# Clean and install dependencies
mvn clean install

# OR skip tests for faster installation
mvn clean install -DskipTests
```

---

## 🚀 Running the Application

### Method 1: Using Maven (Recommended)

```bash
# Development mode with auto-reload
mvn spring-boot:run
```

### Method 2: Using JAR File

```bash
# Build the JAR file
mvn clean package -DskipTests

# Run the JAR
java -jar target/foodshare-1.0.0.jar
```

### Method 3: Using IDE

#### IntelliJ IDEA
1. Open project folder in IntelliJ
2. Wait for Maven to sync dependencies
3. Right-click on `FoodShareApplication.java`
4. Select "Run 'FoodShareApplication'"

#### Eclipse
1. Import as Maven project
2. Right-click on project → Run As → Spring Boot App

#### VS Code
1. Install Java Extension Pack
2. Open project folder
3. Press `F5` or use Run menu

### Verify Application is Running

Once started, you should see:
```
Started FoodShareApplication in X.XXX seconds (process running for X.XXX)
Tomcat started on port 8083 (http) with context path ''
```

**Access the application:**
- **URL**: http://localhost:8083
- **Login Page**: http://localhost:8083/login
- **Admin Dashboard**: http://localhost:8083/admin/dashboard

---

## 🔍 Troubleshooting

### Issue 1: "Port 8083 already in use"

**Solution A - Kill the process (Windows):**
```powershell
$process = Get-NetTCPConnection -LocalPort 8083 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique
if ($process) { Stop-Process -Id $process -Force }
```

**Solution B - Kill the process (Mac/Linux):**
```bash
# Find process on port 8083
lsof -i :8083

# Kill the process (replace PID with actual process ID)
kill -9 PID
```

**Solution C - Change port:**
Edit `application.properties`:
```properties
server.port=8080
```

---

### Issue 2: "JAVA_HOME not set"

**Windows:**
```powershell
# Set JAVA_HOME
[System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-17', [System.EnvironmentVariableTarget]::Machine)

# Restart terminal and verify
echo $env:JAVA_HOME
```

**Mac/Linux:**
```bash
# Add to ~/.bashrc or ~/.zshrc
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Reload
source ~/.bashrc
```

---

### Issue 3: "Database Connection Failed"

**Check 1: Verify database is running**
```bash
# For PostgreSQL
sudo systemctl status postgresql  # Linux
brew services list  # Mac
```

**Check 2: Test connection manually**
```bash
psql -h aws-0-ap-south-1.pooler.supabase.com -U postgres.xxxxxxxxxxx -d postgres
```

**Check 3: Verify credentials in `application.properties`**
- Ensure password is correct
- Check if database URL is accessible
- Verify username format

---

### Issue 4: "Maven Build Failed"

**Solution A - Clear Maven cache:**
```bash
# Windows
rmdir /s /q %USERPROFILE%\.m2\repository
mvn clean install

# Mac/Linux
rm -rf ~/.m2/repository
mvn clean install
```

**Solution B - Use Maven wrapper:**
```bash
# Windows
.\mvnw.cmd clean install

# Mac/Linux
./mvnw clean install
```

**Solution C - Update Maven:**
```bash
# Check version
mvn -version

# Update to latest (using package manager)
choco upgrade maven  # Windows
brew upgrade maven   # Mac
sudo apt upgrade maven  # Linux
```

---

### Issue 5: "Template Parsing Error"

**Clear target folder and rebuild:**
```bash
mvn clean
mvn compile
mvn spring-boot:run
```

---

### Issue 6: "Out of Memory Error"

**Increase JVM heap size:**
```bash
# Set environment variable before running
set MAVEN_OPTS=-Xmx1024m  # Windows
export MAVEN_OPTS=-Xmx1024m  # Mac/Linux

# OR run with Java options
java -Xmx1024m -jar target/foodshare-1.0.0.jar
```

---

### Issue 7: "Dependencies Download Failed"

**Check internet connection and try alternate Maven repository:**

Edit `pom.xml`, add before `<dependencies>`:
```xml
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo.maven.apache.org/maven2</url>
    </repository>
    <repository>
        <id>spring</id>
        <url>https://repo.spring.io/release</url>
    </repository>
</repositories>
```

Then run:
```bash
mvn clean install -U
```

---

## 👥 Demo Accounts

The application comes with pre-configured demo accounts:

### Admin Account
- **Email**: admin@gmail.com
- **Password**: password123
- **Access**: Full system administration, user management, reports

### Donor Account
- **Email**: donor@gmail.com
- **Password**: password123
- **Access**: Create donations, manage own donations, analytics

### Receiver Account
- **Email**: receiver@gmail.com
- **Password**: password123
- **Access**: Browse available donations, claim food, view history

---

## 🎯 Quick Start Commands

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/FoodShare.git
cd FoodShare

# 2. Install dependencies
mvn clean install -DskipTests

# 3. Run the application
mvn spring-boot:run

# 4. Open browser
# Navigate to: http://localhost:8083
```

---

## 📝 Additional Configuration

### Email Configuration (Optional)

To enable email notifications, add to `application.properties`:

```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### File Upload Directory (Optional)

```properties
# Custom upload directory
file.upload-dir=./uploads
```

---

## 🔒 Security Notes

1. **Change default passwords** before deploying to production
2. **Update database credentials** in `application.properties`
3. **Enable HTTPS** for production deployment
4. **Use environment variables** for sensitive data:

```bash
# Set as environment variables
export DB_PASSWORD=your_secure_password
export JWT_SECRET=your_jwt_secret
```

Then reference in `application.properties`:
```properties
spring.datasource.password=${DB_PASSWORD}
```

---

## 📚 API Documentation

Once the application is running, access Swagger UI (if configured):
- **URL**: http://localhost:8083/swagger-ui.html

---

## 🐛 Getting Help

If you encounter issues:

1. **Check logs**: Look at console output for error messages
2. **Enable debug logging**: Set `logging.level.root=DEBUG` in `application.properties`
3. **Clear cache**: Delete `target` folder and rebuild
4. **Check dependencies**: Run `mvn dependency:tree`
5. **Verify Java version**: Ensure JDK 17+ is being used

---

## 🎉 Success Checklist

- [✓] Java JDK 17+ installed
- [✓] Maven 3.6+ installed
- [✓] Database accessible (Supabase or local PostgreSQL)
- [✓] Dependencies downloaded successfully
- [✓] Application starts without errors
- [✓] Can access http://localhost:8083
- [✓] Can login with demo accounts

---

## 📧 Support

For additional help:
- Create an issue on GitHub
- Check documentation in `/docs` folder
- Review application logs in console

---

**Congratulations! Your FoodShare application is now running! 🎊**
