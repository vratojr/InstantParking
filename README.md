# Instant Parking

This is a simple application that allows you to find the nearest parking to you.

## How to run it

```./gradlew bootRun```

then go to 

```localhost:8080/api/parkings?lat=<val>&lng=<val>```
(The coordinates are ignored)

The response will be a list of parkings in JSON format.

## Naming convention

A *ParkingProvider* represents an API to call to obtain parking information around a given coordinate pair. The name is not my best choice, I could have called ParkingProviderApiConfiguration but I preferred to keep it simple since the domain model is simple.

## Global considerations and assumptions

1. Each city/location will probably have its own apis with specific url and format but they will hold all the required information.
2. It will be necessary to define how to map a given coordinate pair to a given ParkingProvider, here I used the bounding box of the ParkingProvider to define the area. 
3. The workflow requires a lot of external calls (db, api calls) so I opted for a non-blocking approach.

## Project structure

The project is structured in a "clean architecturish" style with a separation between the domain, application and infrastructure layers.

- The domain is pretty empty but it is there for the sake of the exercise.
- The application layer contains the business logic expressed under the form of use cases.
- The infrastructure layer contains the external dependencies (controllers, db, api clients).
- Unit tests have been added mainly to help my development rather that to ensure the quality of the code.

## Implementation notes

- I used the strategy pattern via the ParkingProviderApiClientFactory to get the correct client given a provider.
- I decided to return the Parking sorted by distance. And I decided to return ALL the parkings. But we should think whether to impose a cutoff based on distance/number of parkings or implement proper pagination.
- We could add the concept of 'score' for each parking that will be used to sort them if 'distance' is not the only parameter to consider.
- I decided to put the url of the api inside the ParkingProvider... It could be good for a first implementation that I had in my mind but now I fear it could be useless since, for each Provider, everything is hardcoded inside a specific ApiClient.

## What's missing

- Proper exception handling with a @ControllerAdvice to return a proper error message to the user.
- Metrics on the critic parts (the api/db calls) to check for bottlenecks.
- Proper configuration of the thread pool.
- We should add proper retry/circuit breaker to the apiclients and gracefully handle errors (for the user).
- The algo that researches ParkingProvider (and the PP structure) is just a stub. Probably we could leverage the direct use of GeoCoordinates in the db and in the code if this part becames a bottleneck.
- Parking could be a domain entity if we need to attach some buiness rules to it.
- Some more test case.

## Notes

In the method and variable names I always explicited the unit of measure since, by experience, I know that doing otherwise could lead to errors.

It took me 7h to complete this task. 