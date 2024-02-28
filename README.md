# Sunset and Sunrise API application

This repository contains a simple REST API application that provides sunset and sunrise information based on latitude, longitude and date.

## Table of Contents

- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Configuration](#configuration)

## Introduction

This is a basic REST API application built using [Spring Boot](https://spring.io/projects/spring-boot) framework and [Maven](https://maven.apache.org). The application allows users to get information about sunset and sunrise time information for a coordinates by making HTTP requests to predefined endpoints.

## Technologies Used

- [Spring Boot](https://spring.io/projects/spring-boot): Web framework for building the REST API.
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa): Data access framework for interacting with the database.
- [MySQL](https://www.mysql.com): Database for local and global use.
- [Sunrise Sunset API](https://api.sunrise-sunset.org/json): External API for sunrise and sunset time information.

## Getting Started

### Prerequisites

Make sure you have the following installed:

- Java (version 17 or higher)
- Maven
- MySQL server
- MySQL driver
- JPA

### Installation

1. Clone the repository:

    
    git clone https://github.com/Willygodx/SunsetSunriseAPI
    

2. Build the project:

    
    mvn clean install
    

3. Run the application:

    
    java -jar target/SunsetSunriseAPI-0.0.1-SNAPSHOT.jar
    

The application will start on http://localhost:8080.

## Usage

### Endpoints

- Get sunrise and sunset time by latitude, longitude and date:
  
  
  GET /sunset-sunrise/get-info?latitude=YOUR_LATITUDE&longitude=YOUR_LONGITUDE&date=YOUR_DATE
  

  Retrieves sunrise and sunset time information for the specified lat, lng and date.

  Example:
  
  GET /sunset-sunrise/get-info?latitude=51.508530&longitude=-0.125740&date=2024-02-19
  

### Configuration

The application uses the [Sunrise Sunset API](https://sunrise-sunset.org) to fetch data. You need to obtain an API URL (https://api.sunrise-sunset.org/json) and configure it in the application.properties file.


# application.properties

# Sunrise Sunset API URL
external.api.url=https://api.sunrise-sunset.org/json