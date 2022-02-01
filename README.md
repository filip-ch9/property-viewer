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
```- create free account on ->```https://www.geoapify.com/geocoding-api

### Getting started

Build application:\
```- git clone the repo, or download code as zip file```\
```- unzip if needed```\
```- go to app/services/LocatePropertyService.java```\
```- change {YOUR_API_KEY} with one you aquire from geopify ->```
```java
    HttpUrl url=Objects.requireNonNull(HttpUrl.parse("https://api.geoapify.com/v1/geocode/search"))
    .newBuilder()
    .addQueryParameter("text",property.getStreetNumber()+" "
    +property.getStreetName()+" "
    +property.getCity()+" "
    +property.getPostalCode()+" "
    +property.getCountry()+" ")
    .addQueryParameter("apiKey","{YOUR_API_KEY}")
    .build();
```   
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
