# currency-exchange-system

Currency Exchange System
This project is a mock currency exchange system designed to demonstrate and test API integrations for real-time exchange rate retrieval. It features a modern, interactive frontend built with standard web technologies and a robust backend powered by Spring Boot.

Features
Currency Conversion: Allows users to input an amount and specify 'from' and 'to' currencies (e.g., USD to EUR).
Real-time Exchange Rate Display: Fetches and displays the converted amount, original amount, applied exchange rate, and a timestamp for the conversion.
Client-side Validation: Basic validation for the amount input to ensure it's a valid positive number.
Robust Error Handling: Catches and displays API-related errors gracefully.
Dynamic Animated Background: Features a subtle, blurry, and moving background effect, inspired by modern UI designs like Revolut, providing a premium visual experience.
Button Spam Prevention: Implements a debouncing mechanism on the "Convert" button to prevent excessive API calls from rapid clicking, improving API stability and user experience.
Technologies Used
Frontend
HTML5: Structure of the web application.
CSS3: Styling, including advanced features like linear-gradient, radial-gradient, filter: blur(), and @keyframes for animations.
JavaScript (ES6+): Handles form submission, API requests using the Fetch API, DOM manipulation, and button debouncing logic.
Backend
Java: The core programming language for the backend.
Spring Boot: Framework for building the RESTful API, providing a rapid development environment.
RESTful API: Exposes an /api/exchange endpoint to handle currency conversion requests with from, to, and amount parameters.
Maven/Gradle: Build automation tools (the Spring Initializr screenshot suggests Java 24 and either Gradle or Maven as options).
Getting Started
To get this project up and running on your local machine, follow these steps:

Prerequisites
Java Development Kit (JDK): Version 24 or compatible.
Maven or Gradle: Installed on your system.
A modern web browser: (e.g., Chrome, Firefox, Edge).
Backend Setup
Clone the repository:
Bash

git clone [Your Repository URL Here]
cd currency-exchange-system
Build and Run the Spring Boot application:
Using Maven:
Bash

./mvnw spring-boot:run
Using Gradle:
Bash

./gradlew bootRun
The backend API will start on http://localhost:8080 by default.
Frontend Access
The HTML, CSS, and JavaScript files are served as static resources by the Spring Boot application.

Once the Spring Boot application is running, open your web browser.
Navigate to:

http://localhost:8080/

You should see the currency exchange form with the dynamic background.

API Endpoint

The backend provides a single conversion endpoint:

GET /api/exchange?from={currencyCodeFrom}&to={currencyCodeTo}&amount={value}

Example:
http://localhost:8080/api/exchange?from=USD&to=EUR&amount=100

This endpoint will return a JSON response containing the conversion details, including the original amount, converted amount, exchange rate, and timestamp.

Project Structure (Simplified)
currency-exchange-system/
├── src/
│   ├── main/
│   │   ├── java/com/eduardo/currencyexchange/
│   │   │   ├── controller/
│   │   │   │   └── ExchangeController.java  # Handles API requests
│   │   │   ├── dto/
│   │   │   │   └── ExchangeResponse.java    # Data Transfer Object for API response
│   │   │   ├── service/
│   │   │   │   └── ExchangeService.java     # Business logic for exchange
│   │   │   └── CurrencyExchangeApplication.java # Main Spring Boot application
│   │   └── resources/
│   │       └── static/                  # Static web resources
│   │           ├── index.html           # Main frontend page
│   │           ├── style.css            # Frontend styles, including animations
│   │           └── script.js            # Frontend logic and API calls
│   └── test/
├── pom.xml (or build.gradle)            # Project build configuration
└── README.md
