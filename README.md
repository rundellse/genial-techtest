# Genial Tech Test
Application hosting an API which redacts words from a passed string.

## Running Instructions
Instructions provided as for a Windows environment. *nic environments may require some small changes.

### Requirements
* Java 17
* Git

### Configuration
There are two key properties for this application, one custom: 
 - 'genial.techtest.redacted.wordlist - defining the list of words to be redacted.

One from spring: 
 - 'server.port' - defining the port on which the app is available.

These are set by default in the application's core application.properties as follows:
```
server.port=8080
genial.techtest.redacted.wordlist=Dog,Cat,Snake,Dolphin,Mammal
```
The values can be overridden by defining a custom properties file which can be passed at startup during stage 3 below.

### Running
1. Clone the content of the repository into a suitable folder  
```
  git clone https://github.com/rundellse/genial-techtest.git
  cd genial-techtest
```
2. Build the project using the mvnw maven wrapper
```
  mvnw clean package -DskipTests
```
3. Run the spring boot application using the mvnw maven wrapper. Passing the the location of a custom properties file if required.
```
mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.config.location=[path\to\custom\properties\file]\custom.properties"
```
4. The redact API should be testable with curl/Postman on the following URL:
```
http://localhost:[configured_port]/redact
```

### Logging
The content of the request body of any calls to the '/redact' API will be logged to the RedactRequest.log file.
