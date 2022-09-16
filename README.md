
# Tube Loader Server

## About

TubeLoader Server is the back end portion of the TubeLoader system. It acts as the data storage and downloader of the system. It's able to do this by using [yt-dlp](https://pypi.org/project/yt-dlp/) as an embedded dependency (which also requires a python runtime) used for downloading videos.

## Building

Building can be performed using the following command:

```
gradle clean build
```

## Running

The software can be ran with the following command:

```
gradle bootRun
```

Alternatively, If the JAR has been build it can be ran with:

```
java -jar app.jar
```

## Containerized

WARNING: When containerizing ensure that the program has been build before running any docker commands.

If containerization is required the following command can be ran to build the container image:

```
gradle clean build -x test
docker build -t tube_loader_server .
```

## Deploying

An example "docker-compose.yml" file has been provided to assist with deployment.Please follow the below steps in order to deploy the software correctly with the example "docker-compose.yml".

Build software:

```
gradle clean build -x test
```

Replace template values in "application.yml" and in ".db.env":

```
vim .db.env # Modify env vars as required (DO NOT USE DEFAULTS!)
cp src/main/resources/application.yml ./
vim application.yml # Modify app props as required (DO NOT USE DEFAULTS!)
```

Make config directory and copy "application.yml":

```
mkdir -p /opt/tube_loader/config
cp applicaiton.yml /opt/tube_loader/config/
```

Run docker compose:

```
docker compose up -d
```

## Testing

Unit tests can be ran with the following command:

```
gradle clean test
```

## Dependencies

The following software is required to launch and develop the application:

- Java 11
- [yt-dlp](https://pypi.org/project/yt-dlp/)

## License

[GPLv2](https://www.gnu.org/licenses/old-licenses/gpl-2.0.html)

