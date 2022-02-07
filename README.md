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