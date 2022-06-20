# Distributed Systems Manager
Big data and distributed systems university project<br>
Radosław Bodus, Hubert Radom

## How to run

### Cassandra cluster (docker)

`docker run --name cass1 -p 9042:9042 -e CASSANDRA_CLUSTER_NAME=ClusterName -e HEAP_NEWSIZE=1M -e MAX_HEAP_SIZE=256M -d cassandra:latest`

`docker run --name cass2 -e CASSANDRA_SEEDS="$(docker inspect --format='{{.NetworkSettings.IPAddress}}' cass1)" -e CASSANDRA_CLUSTER_NAME=ClusterName -e HEAP_NEWSIZE=1M -e MAX_HEAP_SIZE=256M -d cassandra:latest`

`docker run --name cass3 -e CASSANDRA_SEEDS="$(docker inspect --format='{{.NetworkSettings.IPAddress}}' cass1)" -e CASSANDRA_CLUSTER_NAME=ClusterName -e HEAP_NEWSIZE=1M -e MAX_HEAP_SIZE=256M -d cassandra:latest`

### Backend (java)

go to backend directory: `cd backend`

run spring maven `mvn spring-boot:run`

### Frontend (typescript)

go to frontend directory: `cd frontend`

run typescript app `npm start`

### Tests (python)

go to tests directory `cd tests`

run python script `python stress.py`

## Project description

The goal of our project was to create a distributed system for managing distributed systems. The user can create clusters and give access to them to other users. Cluster consists of an unlimited number of nodes. Each user can disable or enable a particular node or an entire cluster. Each user can also ping the node to save its status (ON / OFF) along with timestamp and information about the user in the log history.

### Database scheme

`database-scheme.pdf`

## Tech stack and modules description

### Backend

We use Java Spring with Cassandra client. Cassandra cluster is run using three docker containers. All endpoint handling logic is in the `backend/*/controller/Controller.java` file. In `backend/*/model/*` we can find declarations of all models, so items that are stored in tables (see database scheme). In `backend/*/repository/*` we can find repositories (tables) declarations with custom queries.

### Frontend

We use Typescript and React. All logic is in the `frontend/src/App.tsx` file. 

### Tests

Tests are written in Python. To make asynchronous endpoint calls we use `asyncio` library.
We performed multiple times three stress tests when running app with three-node cassandra cluster.
1. The same user is trying to create logs for the same node 10000 times.
2. Different users are trying to create logs for different nodes 10000 times (together).
3. One user is trying to switch the whole cluster OFF when in the same time the other user tries to switch it ON. To make the result consistent (all nodes ON or all nodes OFF) we implemented custom logic to handle it. After making requests, users check in what status the most of nodes are currently in. If most of them are ON, users switch all on, otherwise OFF.

Estimated time for test 1 and 2 is 4 seconds on average, when for test 3 it is 1.3 second.

## Problems

Turning on three-node Cassandra cluster turned out to be a problem for us when testing the application. While the next nodes were starting up, the previous ones were dying. The solution, however, turned out to be very simple. A laptop with 8GB of RAM needed to limit the memory that docker containers had at their disposal to 256MB.

The very beginning was also a bit difficult, i.e. getting to know the environment in which we work. Our little knowledge of Java didn't make it easy for us to manage dependency in pom.xml :)
