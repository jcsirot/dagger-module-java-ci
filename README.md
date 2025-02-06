# A sample CI workflow using a Java Dagger module

This application is a simple echo server that listens on port 8080 and returns the request path as the response.

## Install Dagger

See https://docs.dagger.io/install to install Dagger.

## Run the CI module

### List the CI tasks

```bash
$ dagger functions
```
### Run the unit tests

```bash
$ dagger call test --source=.
```

### Build and Package the application

```bash
$ dagger call build --source=.
```

### Run the service on your local workstation

```bash
$ dagger call dev --source=. up
...
$ curl http://localhost:8080/hello-world/
Echo: /hello-world/
```

### Publish the service image to ttl.sh registry
```bash
$ dagger call publish --source=.
...
ttl.sh/echo-server:1h@sha256:3e85eb...
...
$ # Test the published image
$ docker run --rm -p 8080:8080 --pull=always ttl.sh/echo-server:1h
```
