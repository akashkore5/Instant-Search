# Instant-Search

# Instant Search Application

This is a simple web application built with Spring Boot that allows users to search a large list of names. The application supports both prefix-based search using a Trie data structure and substring-based search using a HashSet. It also includes caching for faster search results on repeated queries.

### Python Script to Generate Fake Names

    ```python
    from faker import Faker

    # Initialize Faker instance
    fake = Faker()

    # Generate 300,000 fake names
    names = [fake.name() for _ in range(300000)]

    # Write the names to a CSV file
    with open('Names.csv', 'w') as f:
        f.write("\n".join(names))

## Features

- **Prefix-based search**: Uses a Trie for fast prefix-based lookups.
- **Substring-based search**: Uses a HashSet for case-insensitive substring matching.
- **Caching**: Search results are cached for subsequent identical queries to improve performance.
- **Response time display**: Shows how long the search took to execute in milliseconds.
- **Error handling**: Provides error messages for invalid queries (e.g., queries with fewer than 3 characters).

## Demo
   **Initial Screen**
   <img width="1508" alt="image" src="https://github.com/user-attachments/assets/b3631a01-da1d-425d-ac91-4bd46dedc5ef" />

   **With Search Query**
   <img width="1512" alt="image" src="https://github.com/user-attachments/assets/63b236bd-a1e0-4f2a-b67d-26b8ec6e31f3" />

   **After Caching the query (Response Time Mapped to )**
   <img width="1512" alt="image" src="https://github.com/user-attachments/assets/6ba0123f-900a-46bc-b9da-e3bcb520fa26" />


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

- **GET /**: Displays the main search page where users can enter their queries.
- **GET /search**: Handles the search logic. Accepts a query parameter `q` to perform the search. Returns the search results along with the response time.

## How to Use

1. Enter a query in the search box and click the "Search" button.
2. The results will be displayed below the search box, showing matching names and their rank based on the search criteria.
3. The application supports both:
   - **Prefix-based search**: Finds names that start with the query.
   - **Substring-based search**: Finds names that contain the query but not necessarily at the start.
4. If a query is repeated, the search results will be retrieved from the cache for faster performance.

## Performance Considerations

- **Caching**: The application caches search results for identical queries, improving performance for repeated searches.
- **Trie for prefix search**: The Trie data structure ensures that prefix searches are efficient, reducing the time complexity of prefix lookups.

## Troubleshooting

- **Empty results**: Ensure the query is at least 3 characters long and that the CSV file is correctly formatted.
- **Error handling**: Queries with fewer than 3 characters will show an error message.

## Future Improvements

- **UI Enhancements**: Improve the user interface with better design and features such as auto-suggestions.
- **Advanced Search Algorithms**: Consider integrating more advanced search algorithms, such as fuzzy search or Levenshtein distance, to handle typos or approximate matches.



