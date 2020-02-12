# Nosto -Currency Converter exercise.

**Backend**
- Java 11
- Spring (Boot, MVC, Security, Data)
- H2 Database.

**Frontend**
- WebPack
- ReactJs

**Build**
- Maven

**Testing**
- Junit and Spring-test for unit testing.


## Application Details

The Currency Converter application has 3 screens -

- The Currency Converter app is built using Spring Boot and RectJs. 

- Using an in-memory relational H2 database to store the currencies and rates.

- All API requests return stored value in the memory database to avoid any rate limit from the 3rd party API.

- Scheduled tasks to update the value every 5 hours, but not really needed for this exercise.

- 100% Test Coverage for the Backend.

- Implemented CSRF check Between Frontend and Backend and also recommended security settings.

- Project ready to run from one command.

- Custom Error handling messages in the backend to have a unified message format.

- Validation both in Frontend and Backend.

- Server-Timing integration.

- All Frontend side is handled by Maven, no extra installs needed.


Running the application
---
**Prerequisites**

1. Java 11

4. Maven

**Build and Run**


2. Build the source code by running:   
		
		mvn clean install 
   from the root folder

3. To start the application using in-memory database (dev)
		
		mvn clean install spring-boot:run -Pdev

4. To start the application using in-memory database (prod)
		
		mvn clean install spring-boot:run -Pprod

5. After that you can access the application using the following URL:

        
        dev:  http://localhost:9090
        
        prod:  http://localhost:8080

6. To start ReactJs Frontend separately:
        
        npm start
        or
        yarn start
        
6.1. After that you can access the FrontEnd application using the following URL:      

 		http://localhost:3000

        
7. build prod ReactJs Frontend separately:
        
        npm run build
        or
        yarn build
        
