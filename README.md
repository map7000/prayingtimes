# Prayer Times Bot

A multiservice Kubernetes application providing accurate prayer times based on user location and preferred calculation methods, with Telegram bot integration.

## Project Overview

This project consists of several interconnected microservices that work together to provide prayer time calculations and Qibla direction services through both REST API and Telegram bot interfaces. The application is designed to run on Kubernetes and is deployed using Helm.

## Architecture

![Architecture Diagram](docs/architecture.png)

The application is composed of the following microservices deployed as separate containers in Kubernetes:

## Modules

### prayingtimes-models
A Java package containing POJO (Plain Old Java Object) classes used across the application for API data transfer and internal processing.

mvn clean install -pl prayingtimes-models -am

### prayingtimes-timeskeeper
A Spring Boot application with REST API endpoints for calculating prayer times.

**Current Implementation**: Delegates calculations to the
[prayer-times-calculator](https://github.com/map7000/prayer-times-calculator) service. The provider is configured with
`prayer-times.api.base-url` (environment variable `PRAYER_TIMES_API_URL`, default
`http://localhost:3000`).

**Features**:
- RESTful API endpoints for prayer time calculations
- Support for multiple calculation methods
- Location-based time calculations with IANA timezone support
- Hanafi Asr calculation, matching the previous provider behaviour
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

Prayer times are calculated by the external
[prayer-times-calculator](https://github.com/map7000/prayer-times-calculator) service (Rust). The timeskeeper module
translates the public API request (IANA timezone, calculation method enum) into the provider's
format and exposes the result unchanged to clients.

The qibla calculator is not part of the running application yet; the provider already exposes a
qibla endpoint that can be wired in when needed.

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