# mock-client

## General info
The purpose for this application is to provide the developer with a way to trigger
a burst of/distributed over time requests to the transaction processing service.

The way this is done is by http requests to the mock client, which based on the parameters they take
(for example: number of requests, time between requests, countries) will result in a request(s) being
sent to the transaction processing service.

##Sample curl request:

### Trigger 10 requests with 5 seconds between each request(default country is used: BG)
```
curl -X GET http://localhost:8080/mock-client/api/trigger?intervalBetweenRequests=5000&numberOfRequests=10
```

### Trigger one request for each of the countries US, UK, DE with 5 seconds between each request.
```
curl -X GET http://localhost:8080/mock-client/api/trigger?intervalBetweenRequests=5000&countries=US&countries=UK&countries=DE
```
