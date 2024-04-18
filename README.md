# Encoder API application

This repository contains a simple REST API application that provides encoder: parsing text with emails and phone numbers.

## Table of Contents

- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)

## Introduction

This is a basic REST API application built using [Spring Boot](https://spring.io/projects/spring-boot) framework and [Maven](https://maven.apache.org). The application allows users to get parsed text by making HTTP requests to predefined endpoints.

## Technologies Used

- [Spring Boot](https://spring.io/projects/spring-boot): Web framework for building the REST API.

## Getting Started

### Prerequisites

Make sure you have the following installed:

- Java (version 17 or higher)
- Maven

### Installation

1. Clone the repository:

    
    git clone https://github.com/kuzzne4ik/EncoderLab
    

2. Build the project:

    
    mvn clean install
    
The application will start on http://localhost:8080.

## Usage

### Endpoints

- Get filtered text:
  
  
  GET /api/encoder/filtered?text=YOUR_TEXT

- Get emails from text:
  
  
  GET /api/encoder/emails?text=YOUR_TEXT

- Get phone numbers from text:
  
  
  GET /api/encoder/phonenumbers?text=YOUR_TEXT
    