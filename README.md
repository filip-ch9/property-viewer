# property-viewer-web-app 

## Overview

Simple web application, build using play framework with Java

### Example url

    http://localhost:9000

### Prerequisites

Software needed to start the application:\
```- jdk 1.8 or higher```\
```- sbt version 1.6.1 or higher```\
```- mysql database started on localhost, or workbench```\
```- create schema with name: property_viewer```\
```- [Optional] -> postman for testing the endpoints```\

### Getting started

Build application:\
```- git clone the repo, or download code as zip file```\
```- unzip if needed```\
```- import into favorable IDE, or open command line in project root folder```\
```- run command -> sbt run```\
```- go to  http://localhost:9000```

## Testing and Troubleshooting

To populate the database simply run ```dummyJsonData.json``` in Postman on ```http://localhost:9000/properties/addAll```. 
It will check their coordinates. If the information is referring to a building it will get the coordinates, otherwise it will
return empty or null coordinates. On the GUI you can do simple operations like see all properties, add new or delete them. 
I have marked every endpoint in routes with ```+nocsrf``` so you don't have to worry getting 
```strict-origin-when-cross-origin``` error. In the test folder there are two tests you can run separately. 
One is a Simple (JUnit) test that can call all parts of a play app, and the other one is an
Integration test, that involves starting up an application or a server.

## Coming soon
### Future improvements:
- Add user entity
- Relation between entities (User - Property)
- Authentication (login, register, logout), and security
- Integrate GUI into React.js
