# todobin

### Starting a local Postgres instance using Docker

```docker run --name todobin-postgres -p 5432:5432 -e POSTGRES_PASSWORD=((password)) -d postgres```

### Killing and Removing a Docker container
1. Execute `docker ps` to identify postgres container's `CONTAINER ID`
2. Execute `docker kill CONTAINER_ID` and `docker rm CONTAINER_ID`

### Connect to Postgres DB via psql

1. Execute `docker ps` to identify postgres container's `CONTAINER ID`
2. Execute `docker exec -it CONTAINER_ID bash` to get a bash shell in the container
3. Execute `psql -U postgres`

### Useful psql commands
`/list` - lists all databases