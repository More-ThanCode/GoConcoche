# GoConcoche
## Intro
Welcome to the backend API GoConCoche, a web app designed to let user register and rent out their 
car (or fleet of cars) or rent a car.
The system enables users to search for, book, and manage car-sharing reservations at specific 
locations. It allows car owners to manage their rental offers and it provides administrators with tools 
to oversee vehicle fleets and booking rules. It also includes advanced features such as secure 
authentication using JWT. The project is implemented using Java 21, Maven, and MySQL, with a strong 
focus on modularity, security, and efficiency. A key constraint for this API is that the ability to 
search for available vehicles is protected and available only for registered users, as per customer 
request, therefore prioritizing a secure, registered-user-only experience from the very first step.

## Database Schema
##### Main Entities:
- **registered_users**: User accounts with authentication details
- **roles**: User roles (OWNER, RENTER, ADMIN)
- **owner_profiles**: Owner-specific profile information
- **renter_profiles**: Renter-specific profile with driver's license data
- **vehicles**: Vehicle information (brand, model, fuel type, etc.)
- **locations**: Geographic locations for rental offers
- **vehicle_rental_offers**: Published rental offers with pricing and availability
- **rental_offer_slots**: Time slots for granular availability management
- **vehicle_reservations**: Booking records (in development)

[Go-Con-Coche-ERD.png](https://postimg.cc/2qdmpvJM)

## Guide for using this API
###### For owners
1. Register and login with OWNER role
2. Create an owner profile with profile image
3. Add your vehicles with details and images
4. Create rental offers specifying location, dates, and pricing
5. Manage your offers and respond to bookings

###### For renters
1. Register and login with RENTER role
2. Create a renter profile with driver's license information
3. Search for available vehicles using filters
4. View detailed offer information
5. Make reservations

###### For administrators
1. Login with ADMIN credentials
2. View and manage all users, profiles, and offers
3. Create and manage user roles
4. Delete inappropriate content
5. Monitor platform activity# Go Con Coche - Car Rental Platform

## Features:
##### User Authentication
- **Register**: Create a new account with username, email, password, and personal information (first name, last name, date of birth, phone number)
- **Login**: Secure JWT-based authentication system for existing users
- **Role-Based Access Control**: Users can have OWNER, RENTER, or ADMIN roles

##### User Roles:
###### -- Car Owners --
-**Create Owner Profile**: Set up a profile with a profile image (uploaded to Cloudinary)
- **List Vehicles**: Add cars to the platform with details including:
  -- Vehicle brand, model, and year
  -- License plate number
  -- Seater capacity (2 to 9+ seats)
  -- Fuel type (Gasoline, Diesel, Electric, Hybrid)
  -- Vehicle images (uploaded to Cloudinary)
- **Create Rental Offers**: Publish vehicles for rent with:
  -- Availability period (start and end date/time)
  -- Location details
  -- Hourly pricing
  -- Automatic 8-hour time slot generation
- **Manage Listings**: View, edit, or remove vehicle listings and rental offers
- **View My Offers**: Track all your active rental offers
- **Update profile**: Change profile image

###### -- Renters --
- **Create Renter Profile**: Set up a profile with driver's license information:
  -- License type (B, BE, C1, C1E, C, CE, D1, D1E, D, DE)
  -- License number
  -- License expiration date
  -- License image (uploaded to Cloudinary)

- **Search Vehicles**: Browse available rental offers with advanced filters:
  -- City/location
  -- Date and time range
  -- Vehicle model
  -- Minimum number of seats

- **View Offers**: See detailed information about available vehicles and pricing
- **Make Reservations**: Book vehicles for specified time periods (functionality in progress)
- **Update Profile**: Modify license information and profile image
- **View My Profile**: Access personal renter profile details

###### -- Administrators --
- **User Management**: View all registered users, owner profiles, and renter profiles
- **Role Management**: Create, update, and delete user roles
- **Profile Oversight**: Access and manage any owner or renter profile by ID
- **Content Moderation**: Delete inappropriate profiles or listings
- **Platform Analytics**: Monitor all rental offers and system activity


## Getting Started

##### Prerequisites
- Java 21 or higher
- SQL database
- Redis server
- Maven
- Cloudinary account (for image storage)


##### Installation

**Clone the Repository**
```
git clone https://github.com/More-ThanCode/GoConcoche.git
cd go-con-coche
```
**Configure environment variables**

**Build the project**
```
./mvnw clean install
```

**Run the application**
```
./mvn spring-boot:run
```

**Access the API documentation**
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

#### Authentication
- POST http://localhost:8080/api/auth/register - Register a new user
- POST http://localhost:8080/api/auth/login - Login and receive JWT token

#### Email Reset
- POST http://localhost:8080/api/auth/forgot-password - To receive Forgot Password Email
- POST http://localhost:8080/api/auth/reset-password - Resets user password using token from email
- POST http://localhost:8080/api/auth/validate-reset-token - Checks that password reset token is valid

#### Owner Profiles
- POST http://localhost:8080/api/api/owner-profiles - Create owner profile (OWNER role)
- GET http://localhost:8080/api/owner-profiles/profile - Get my owner profile
- GET http://localhost:8080/api/owner-profiles - Get all profiles (ADMIN only)
- GET http://localhost:8080/api/owner-profiles/{id} - Get profile by ID (ADMIN only)
- PUT http://localhost:8080/api/owner-profiles/profile - Update my profile
- DELETE http://localhost:8080/api/owner-profiles/me - Delete my profile
- DELETE http://localhost:8080/api/owner-profiles/{id} - Delete profile by ID (ADMIN only)

#### Renter Profiles
- POST http://localhost:8080/api/renter-profiles - Create renter profile (RENTER role)
- GET http://localhost:8080/api/renter-profiles/me - Get my renter profile
- GET http://localhost:8080/renter-profiles - Get all profiles (ADMIN only)
- GET http://localhost:8080/api/renter-profiles/{username} - Get profile by username
- GET http://localhost:8080/api/renter-profiles/id/{id} - Get profile by ID (ADMIN only)
- PUT http://localhost:8080/api/renter-profiles - Update my profile
- DELETE http://localhost:8080/api/renter-profiles/me - Delete my profile
- DELETE http://localhost:8080/api/renter-profiles/id/{id} - Delete profile by ID (ADMIN only)

#### Roles (Admin only)
- GET http://localhost:8080/api/registered-users - Get all registered users


#### Vehicle
- POST http://localhost:8080/api/vehicles - Create a vehicle (OWNER role)
- GET http://localhost:8080/api/vehicles/my - Get my vehicles
- PUT http://localhost:8080/api/vehicles/{id} - Update vehicle
- DELETE http://localhost:8080/api/vehicles/{id} - Delete vehicle


#### Rental offers
- POST http://localhost:8080/api//vehicle-rental-offers - Create rental offer (OWNER role)
- GET http://localhost:8080/api/vehicle-rental-offers/my-offers - Get my rental offers
- GET http://localhost:8080/api/vehicle-rental-offers/search/by-criteria - Search offers with filters
- DELETE http://localhost:8080/api/vehicle-rental-offers/{id} - Delete rental offer

- FILTER BY CITY: http://localhost:8080/api/vehicle-rental-offers/search/by-criteria?city=Valencia
- FILTER BY START: http://localhost:8080/api/vehicle-rental-offers/search/by-criteria?startDateTime=2025-10-05T09:00:00
- FILTER BY DATE RANGE: http://localhost:8080/api/vehicle-rental-offers/search/by-criteria?startDateTime=2025-10-05T09:00:00
- FILTER BY VEHICLE MODEL: http://localhost:8080/api/vehicle-rental-offers/search/by-criteria?model=renegade
- FILTER BY MINIMUM SEATS: http://localhost:8080/api/vehicle-rental-offers/search/by-criteria?seats=5
- FILTER BY ALL CRITERIA COMBINED:  http://localhost:8080/api/vehicle-rental-offers/search/by-criteria?city=Valencia&st[…]:00:00&endDateTime=2025-10-31T17:00:00&model=renegade&seats=5

### Reservation (of Rental offers)
- GET http://localhost:8080/api/reservations to get all reservations (only for ADMIN)
- GET http://localhost:8080/api/reservations/my to get own reservations
- GET http://localhost:8080/api/reservations/{id} to get reservation by ID
- POST http://localhost:8080/api/vehicle-reservations to create reservation (RENTER)
- PUT http://localhost:8080/api/reservations/{id} to update reservation date by ID
- DELETE http://localhost:8080/api/reservations/{id} to delete reservation by ID


## Key Features Implementation
#### **Image Upload with Cloudinary**
All profile and vehicle images are automatically uploaded to Cloudinary with:
- Automatic format optimization
- Secure URL generation
- Public ID tracking for deletion
- Default images for profiles when none provided

#### **Search Functionality**
Advanced search with JPA Specifications supporting:
- City-based location filtering
- Date/time availability checking
- Vehicle model filtering
- Minimum seat requirement
- Time slot availability validation

#### Time Slot Management
Rental offers automatically generate 8-hour time slots for granular availability tracking and booking management.

#### Security
- JWT-based authentication
- Role-based access control (RBAC)
- Password encryption with BCrypt
- CORS configuration for frontend integration
- Stateless session management

#### Technology Stack
#### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 21+
- **Security**: Spring Security with JWT authentication
- **Database**: PostgreSQL (JPA/Hibernate)
- **Caching**: Redis for session management
- **Image Storage**: Cloudinary integration for profile and vehicle images
- **API Documentation**: Swagger/OpenAPI 3.0
- **Build Tool**: Maven/Gradle

#### Frontend (Partial Implementation)
- **Framework**: React with Vite
- **Deployment**: GitHub Pages support

#### Infrastructure
- **CORS**: Configured for local development and production
- **Server Environments**:
  -- Local: http://localhost:8080
  --Production: http://go-con-coche.ff5.rael.io

##  Testing
This project includes unit tests and integration tests. 
- Unit Tests: Centered on individual components (example, service methods).
- Integration Tests: Use MockMvc to simulate HTTP requests and verify controller behaviour.

## 🔄 Workflow - Pipelines

The project uses **GitHub Actions** for continuous integration.

This project implements a robust CI/CD pipeline using GitHub Actions to automate testing, Docker image building, and release management.

### 🔧 Build Pipeline (build.yml)
[![Build Pipeline](https://github.com/More-ThanCode/GoConcoche/actions/workflows/build.yml/badge.svg)](https://github.com/More-ThanCode/GoConcoche/actions/workflows/build.yml)

**Trigger:** Automatically executes on every push to main branch

**Purpose:** Build and publish development images

**Activities:**

- Docker image building and optimization
- Pushing images to Docker Hub registry
- Tagging images with commit SHAs
- Generating build artifacts and reports

### 🎯 Release Pipeline (release.yml)
[![Release Pipeline](https://github.com/More-ThanCode/GoConcoche/actions/workflows/release.yml/badge.svg)](https://github.com/More-ThanCode/GoConcoche/actions/workflows/release.yml)

**Trigger:** Automatically activates when version tags are pushed (format: v*, e.g., v1.0.0)

**Purpose:** Production-ready deployments

### 🔍 Test Pipeline (test.yml)
[![Test Pipeline](https://github.com/More-ThanCode/GoConcoche/actions/workflows/test.yml/badge.svg)](https://github.com/More-ThanCode/GoConcoche/actions/workflows/test.yml)

**Trigger:** Automatically runs on every Pull Request targeting main

**Purpose:** Quality assurance before code merging

**Activities:**

- Runs comprehensive test suite in Docker containers
- Executes security scans and code analysis
- Generates test coverage reports
- Uses docker-compose-test.yml for test environment
- Automatic cleanup after execution


## Contributors

Morena Peralta Almada
    <a href="https://github.com/morenaperalta">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>

Nia Espinal
    <a href="https://github.com/niaofnarnia">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>

Sofía Santos
<a href="https://github.com/sofianutria">
    <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>

Anna Nepyivoda
    <a href="https://github.com/NepyAnna">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>

Mary Kenny
<a href="https://github.com/marykenny123">
    <picture>
        <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
        <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
        <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
    </picture>
</a>