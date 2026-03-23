# Planinska Vikendica – PIA Project 2024/2025

Full-stack web application for **booking and managing mountain cottages** (*planinske vikendice*), built using **Angular**, **Spring Boot**, and **MySQL**.  
Developed as part of the *Programiranje internet aplikacija (PIA)* course, academic year 2024–2025.
Details about the application can be found in the .pdf [document](PIA_2425_projekat.pdf).

---

## Overview

The project simulates a real booking platform for rural or mountain cottages.  
It supports three main user roles:

- **Administrator** – user management and global statistics  
- **Turista (Guest)** – registration, login, searching, and making reservations  
- **Vlasnik (Owner)** – adding cottages, managing bookings and revenue stats  

All data are persisted in MySQL, with structured access via DAO repositories and controllers.

---

## Architecture

**Backend:** Java (Spring Boot)  
**Frontend:** Angular (TypeScript, HTML, CSS)  
**Database:** MySQL 8 (+ Workbench import script)

---

## Prerequisites

| Tool | Version |
|------|----------|
| Node.js | ≥ 16 |
| Angular CLI | ≥ 13 |
| Java JDK | ≥ 17 |
| Maven | ≥ 3.8 |
| MySQL Server | ≥ 8.0 |
| MySQL Workbench | optional, for database import |

---

## Backend Setup

## Database Connection (Spring Configuration)

1. The backend connects to the local MySQL instance using a manually defined `DataSource` bean inside the class:

**File:** `backend/src/main/java/com/example/backend/db/DB.java`

```java
@Configuration
public class DB {
    @Bean
    public static DataSource source(){
        DriverManagerDataSource dmds = new DriverManagerDataSource();
        dmds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dmds.setUrl("jdbc:mysql://localhost:3306/planinskavikendica");
        dmds.setUsername("root");
        dmds.setPassword("");

        return dmds;
    }
}
```
Set username and password according to your MySQL Workbench username and password.

2. Run backend

## Database Setup

1. Start MySQL Server.
2. Import the SQL script baza.sql in MySQL Workbench and execute it.
3. Refresh the schemas.

The script fills the database with testing data.

## Frontend Setup

1. Navigate to frontend: cd frontend
2. Install dependencies: npm install
3. Run angular dev server: ng serve

## Open the web app

1. Open the browser and go to localhost:4200.



