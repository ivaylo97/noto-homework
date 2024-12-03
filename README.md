# noto-homework

# Basic info 
Contains 2 services:
- mock-client
- transaction-processing-service

The former is used to simulate traffic to the latter. 
For sample curls on how to do this check the mock-client README.md

How to deploy: 
Build both applications with maven clean install so that they produce a jar file. 
Then run docker-compose up --build in order to start the services.
The command should start up a mongoDB and the 2 services.

For quick db access you can use the mongo.sh script in the root directory.