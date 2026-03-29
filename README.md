# Prayer Times Bot

A multiservice Kubernetes application providing accurate prayer times based on user location and preferred calculation methods, with Telegram bot integration.

## Project Overview

This project consists of several interconnected microservices that work together to provide prayer time calculations and Qibla direction services through both REST API and Telegram bot interfaces. The application is designed to run on Kubernetes and is deployed using Helm.

## Architecture

![Architecture Diagram](docs/architecture.png)

The application is composed of the following microservices deployed as separate containers in Kubernetes:

## Modules

### prayingtimes-time-calculator
**Status**: Under development (currently produces wrong results)

A pure Java core library for calculating prayer times. This will serve as the foundation for accurate prayer time calculations once development is complete.

### prayingtimes-qibla-calculator
**Status**: Under development (currently produces wrong results)

A pure Java core library for calculating Qibla direction. This module will provide accurate Qibla direction calculations when completed.

### prayingtimes-models
A Java package containing POJO (Plain Old Java Object) classes used across the application for API data transfer and internal processing.

mvn clean install -pl prayingtimes-models -am

### prayingtimes-timeskeeper
A Spring Boot application with REST API endpoints for calculating prayer times.

**Current Implementation**: Uses the AlAdhan public internet API as a temporary solution while the native calculation library is under development.

**Features**:
- RESTful API endpoints for prayer time calculations
- Support for multiple calculation methods
- Location-based time calculations
- Integration point for the native calculation library (when ready)
- Health checks and metrics endpoints for Kubernetes

### prayingtimes-telegrambot
A simple Telegram bot that provides prayer time information to users.

**Features**:
- User information storage in database
- Location-based prayer time queries
- Support for user preference management (calculation methods, etc.)
- Integration with prayingtimes-timeskeeper for data retrieval
- Webhook or long-polling configuration for Kubernetes deployment

## Kubernetes Deployment

This application is designed to run on Kubernetes and is deployed using Helm charts.

### Prerequisites

- Kubernetes cluster
- Helm
- PostgreSQL database
- Telegram Bot Token


## Current Development Status

⚠️ **Note**: The core calculation libraries (time-calculator and qibla-calculator) are currently under development and produce incorrect results. The system currently relies on the AlAdhan API through the timeskeeper module for accurate prayer times.

## Monitoring and Logging

The application includes built-in support for:
- Prometheus metrics endpoints
- Health checks (/actuator/health)
- Structured JSON logging
- Kubernetes liveness and readiness probes

## CI/CD Pipeline

The project includes GitHub Actions workflows for:
- Building Docker images on push to main branch
- Running unit and integration tests
- Scanning for vulnerabilities
- Deploying to Kubernetes clusters using Helm

## Contributing

As this project is under active development, contributions are welcome. Please note that the calculation modules are currently producing incorrect results and need further development.

## License

http://www.apache.org/licenses/LICENSE-2.0

## Support

For support or questions regarding this project, please create an issue or contact the development team.