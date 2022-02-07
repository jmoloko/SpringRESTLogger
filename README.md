[![CircleCI](https://circleci.com/gh/jmoloko/SpringRESTLogger/tree/circleci-project-setup.svg?style=svg)](https://circleci.com/gh/jmoloko/SpringRESTLogger/tree/circleci-project-setup)
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

**Technology:** _Java, MySQL, Spring (Ioc, Data, Security), AWS SDK, MySQL, Circleci, Docker, Junit, Mockito, Gradle._

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
> 