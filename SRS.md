Brendan Goldberg
<p align='center'>Software Requirements Specification Document</p>

## 1. Purpose
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The goal of this project is to create a library web application.
## 2. Scope
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The application name is “Simple Library”. The main focus of the application is to allow users to find information about different books. Users will also be able to add book information to the application. This target users of this application are people that like reading books and want to find new material to read.
## 3. Product Functions
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Users will be able to search for different books that are stored in the application. They will also be able to add their own entries into the application if the entry is not already stored.
## 4. Backend Functionality
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The application will persist a local MySQL DB. The application will be running on Apache Tomcat using Maven to build .war files for better version control. Java Servlets will be used for the application logic of displaying information to the user as well as storing information on a user’s request.
### Endpoints
Author Requests:
```
GET /authors
GET /authors/{authorID}
GET /authors/{authorID}/books
GET /authors/{authorID}/books/{bookID}

POST /authors
POST /authors/{authorID}/books

PUT /authors/{authorID}
PUT /authors/{authorID}/book/{bookID}

DELETE /authors/{authorID}
DELETE /authors/{authorID}/books/{bookID}
```
Book Requests:
```
GET /books
GET /books/{bookID}

PUT /books/{bookID}

DELETE /books/{booksID}
```
## 5. Backend Frontend Interaction
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The application will have different endpoints that users can access to either read or write information. These endpoints will correspond to some Java Servlet that will handle the protocols for their respective endpoints such as GET, POST, PUT and DELETE requests.