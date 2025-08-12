# Student-Management-System
# Marksheet Generator Java Project

## Overview
This is a Java-based Marksheet Generator application that allows students and teachers to manage student academic records, calculate SPI/CGPA, and generate marksheets. The application uses a MySQL database for persistent storage and provides a console-based interface for both students and teachers.

## Features
- **Student Module:**
  - Insert new student details
  - Update student details
  - Search student details
  - View marksheet
  - Display SPI & CGPA in descending order
  - Exit
- **Teacher Module:**
  - View all students
  - View ranked students by CGPA
  - Generate marksheets for students
  - Update student details
  - Delete student from database
  - Exit
- **Database:**
  - Uses MySQL for storing student records and rankings
  - Tables: `student`, `student_ranking`

## Technologies Used
- Java (JDBC)
- MySQL
- Console-based UI

## How to Run
1. **Clone the repository** (see below for GitHub instructions)
2. **Set up MySQL database:**
   - Create a database named `marksheet`.
   - Update the database connection string in `App.java` if needed.
3. **Compile the Java code:**
   - Open terminal in the `src` directory.
   - Run: `javac App.java`
4. **Run the application:**
   - Run: `java App`
5. **Follow the on-screen instructions** to use the application as a student or teacher.

## Database Schema
- `student` table: Stores enrollment, semester, branch, name, SPI, and CGPA.
- `student_ranking` table: Stores rank, enrollment, name, and CGPA.

## Project Structure
```
project-root/
│
├── src/
│   └── App.java


## Notes
- Make sure MySQL is running and accessible.
- Update the database credentials in `App.java` as needed.
- For any issues, check the stack trace printed in the console.

---

