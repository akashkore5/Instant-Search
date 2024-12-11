# Instant-Search

# Instant Search Application

This is a simple web application built with Spring Boot that allows users to search a large list of names. The application supports both prefix-based search using a Trie data structure and substring-based search using a HashSet. It also includes caching for faster search results on repeated queries.

## Features

- **Prefix-based search**: Uses a Trie for fast prefix-based lookups.
- **Substring-based search**: Uses a HashSet for case-insensitive substring matching.
- **Caching**: Search results are cached for subsequent identical queries to improve performance.
- **Response time display**: Shows how long the search took to execute in milliseconds.
- **Error handling**: Provides error messages for invalid queries (e.g., queries with fewer than 3 characters).

## Technologies Used

- **Spring Boot**: Framework for building the web application.
- **Thymeleaf**: Templating engine used for rendering the frontend.
- **Java**: Primary programming language for backend logic.
- **Trie data structure**: Efficiently stores and retrieves prefixes of names.
- **HashSet**: Used for substring-based search.

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven or Gradle for building the project

### Steps to run the application

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yourusername/instant-search.git
   cd instant-search
   
2. **Add the CSV file**:

Place a Names.csv file in the root directory of the project. This file should contain one name per line. This file will be read at the application's startup to populate the Trie and HashSet.

3. **Build and run the application**:

   Using Maven:
   ```bash./mvnw spring-boot:run

   Or using Gradle:
   ```bash./gradlew bootRun

4. **Access the application**:
   Once the application is running, open a web browser and go to:
   http://localhost:8080
   You'll see a search box where you can input your query.

## Endpoints
   GET /: Displays the main search page where users can enter their queries.
   GET /search: Handles the search logic. Accepts a query parameter q to perform the search. Returns the search results along with the response time.
   **How to Use**
      Enter a query in the search box and click the "Search" button.
      The results will be displayed below the search box, showing matching names and their rank based on the search criteria.
      The application supports both prefix-based search (starts with the query) and substring-based search (contains the query but not necessarily at the start).
      If a query is repeated, the search results will be retrieved from the cache for faster performance.

