# currency-exchange-system

This project is a mock currency exchange system designed to demonstrate and test API integrations for real-time exchange rate retrieval. It features a modern, interactive frontend built with standard web technologies and a robust backend powered by Spring Boot.

## Features

- **Currency Conversion**: Allows users to input an amount and specify 'from' and 'to' currencies (e.g., USD to EUR).
- **Real-time Exchange Rate Display**: Fetches and displays the converted amount, original amount, applied exchange rate, and a timestamp for the conversion.
- **Client-side Validation**: Basic validation for the amount input to ensure it's a valid positive number.
- **Robust Error Handling**: Catches and displays API-related errors gracefully.
- **Dynamic Animated Background**: Features a subtle, blurry, and moving background effect, inspired by modern UI designs like Revolut, providing a premium visual experience.
- **Button Spam Prevention**: Implements a debouncing mechanism on the "Convert" button to prevent excessive API calls from rapid clicking, improving API stability and user experience.

## Technologies Used

### Frontend
- **HTML5**: Structure of the web application.
- **CSS3**: Styling and animations.
- **JavaScript (ES6+)**: Handles form submission, API requests using the Fetch API, DOM manipulation, and button debouncing logic.

### Backend
- **Java**: The core programming language for the backend.
- **Spring Boot**: Framework for building the RESTful API, providing a rapid development environment.
- **RESTful API**: Exposes an `/api/exchange` endpoint to handle currency conversion requests with `from`, `to`, and `amount` parameters.
- **Maven/Gradle**: Build automation tools (the Spring Initializr screenshot suggests Java 24 and either Gradle or Maven as options).

## Getting Started

To get this project up and running on your local machine, follow these steps:

### Prerequisites

- **Java Development Kit (JDK)**: Version 24 or compatible.
- **Maven or Gradle**: Installed on your system.
- **A modern web browser**: (e.g., Chrome, Firefox, Edge).

### Backend Setup

1. **Clone the repository**:

    ```bash
    git clone [Your Repository URL Here]
    cd currency-exchange-system
    ```

2. **Configure the API Access Key**:

    Open the file at:

    ```
    src/main/resources/application.properties
    ```

    Replace the following line:

    ```
    exchange.api.access-key=YOUR_ACCESS_KEY
    ```

    with your actual access key from [Fixer.io](https://fixer.io), like so:

    ```
    exchange.api.access-key=your_actual_access_key_here
    ```

    > **Note**: This project is integrated with **[Fixer.io](https://fixer.io)** (`http://data.fixer.io/api/`), a reliable foreign exchange rates API. Make sure your access key supports the endpoints used.

3. **Build and run the backend**:

    **Using Maven:**
    ```bash
    ./mvnw spring-boot:run
    ```

    **Using Gradle:**
    ```bash
    ./gradlew bootRun
    ```

    The backend will start at:

    ```
    http://localhost:8080
    ```
