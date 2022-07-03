[![Build Status](https://app.travis-ci.com/jmoloko/RESTServletApp.svg?branch=main)](https://app.travis-ci.com/jmoloko/RESTServletApp)
[![codecov](https://codecov.io/gh/jmoloko/SpringRESTLogger/branch/main/graph/badge.svg?token=SFA680ME0R)](https://codecov.io/gh/jmoloko/SpringRESTLogger)
## Spring REST application
Implemented by the REST API, which interacts with the file storage and provides the ability to access files and download history.

**Entities:**
* User ()
* EVENT (User User, File File)
* File ()

**Access levels:**
* Admin - full access to the application
* Moderator - add and delete files
* User - only reading all data except user

**Technology:** _Java, MySQL, Spring (Ioc, Data, Security), ~~AWS SDK~~, Travis, Docker, Junit, Mockito, Gradle._

**Run in Docker:**
```shell
~> git clone https://github.com/jmoloko/SpringRESTLogger.git
~> cd SpringRESTLogger
~> ./gradlew clean build 
~> docker-compose build
~> docker-compose up
```

**Endpoints:**
> **Start url - /api/v1**

> * **POST:** /api/v1/auth/registration - register new user
 ```json
{
  "name": "user",
  "email": "user@mail.com",
  "password": "user"
}
```
> * **POST:** /api/v1/auth/login - login user
```json
{
  "email": "user@mail.com",
  "password": "user"
}
```

> After login get jwt token. Use this token for each request for endpoints **/api/v1/user** 
> or **/api/v1/executive** depending on the _ROLE_.
> Header _Authorization_ - _token-number_

> * /api/v1/user - access level USER, MODERATOR, ADMIN
> * /api/v1/executive - access level MODERATOR, ADMIN

> * **GET:** /api/v1/user - show current user.
> * **GET:** /api/v1/user/files - show all files from current user.
> * **GET:** /api/v1/user/events - show all events from current user.
> * **GET:** /api/v1/user/files/{id} - show file by id from current user.
> * **GET:** /api/v1/user/files/{id}/download - download a current file by id.
> * **GET:** /api/v1/user/files/{id}/events - show all events from file by id.
> * **POST:** /api/v1/user/files - upload a new file in the file system.
> * **PUT:** /api/v1/user/files/{id} - rename a file in the file system and DB.
> * **DELETE:** /api/v1/user/files/{id} - delete a file in the file system and DB.

> * **GET:** /api/v1/executive/users - show all users.
> * **GET:** /api/v1/executive/users/{id} - show user by id.
> * **GET:** /api/v1/executive/users/{id}/events - show all events from user.
> * **GET:** /api/v1/executive/users/{id}/files - show all files from user.
> * **GET:** /api/v1/executive/users/{id}/files/{id} - show file by id from user.
> * **GET:** /api/v1/executive/users/{id}/files/{id}/download - download file by id.
> * **GET:** /api/v1/executive/users/{id}/files/{id}/events - all events from file by id.
> * **POST:** /api/v1/executive/users - create new user only from ADMIN.
> * **POST:** /api/v1/executive/users/{id} - update user, only from ADMIN.
> * **POST:** /api/v1/executive/users/{id}/files - upload a new file from current user.
> * **PUT:** /api/v1/executive/users/{id} - update current user.
> * **DELETE:** /api/v1/executive/users/{id} - delete user and here all files and events, only from ADMIN.
> * **DELETE:** /api/v1/executive/users/{id}/files/{id} - delete a file in the file system and DB.

> 