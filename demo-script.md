# Carpet Shop Demo notes 

## Pre-demo prep

- Print these instructions!
- Run `./prep-demo` script, which will delete the contract tests and initialise the podman container environment. It also clears the database if the architecture recorder is running.

### Machine tidy
- Pause any backup software
- Turn on Mac Focus
- Quit email and other messaging tools

### Display
- Make terminal and IDE fonts huge
- Make browser font huge
- Reduce screen resolution to 1920 x 1080

### Environment setup

- Sort out web conference green screen if necessary
- Get an iPad with a timer running

### IDE setup 

Open three IDEs, one for [`carpet-shopper`](carpet-shopper), [`knitter`](knitter), and [`wookie-tamer`](wookie-tamer). 

Open a terminal within each IDE (or three OS terminals). 

### Services setup 

1. If this is the first time you are running this, build/install the [`observer extension`](observer-extension):
    ```shell
    cd observer-extension
    ./mvnw install
    ```
2. Start the [architecture recorder](architecture-recorder):
    ```shell
    cd architecture-recorder
    quarkus dev
    ```

It may be useful to clear all architecture information, or just the historical interactions. 

```shell
 curl -i -X POST http://localhost:8088/recorder/clearall
```
OR
```shell
 curl -i -X POST http://localhost:8088/recorder/clearinteractions
```

## The demo 
1. Start the [`carpet-shopper`](carpet-shopper) service with `quarkus dev --clean`.
2. Visit http://localhost:8080. The app has a React front end and a Quarkus back end, stitched together and bridged by [Quarkus Quinoa](https://quarkiverse.github.io/quarkiverse-docs/quarkus-quinoa/dev/index.html).
3. Try and do an order. Nothing will happen; there are no other services.
4. Start the [`knitter`](knitter) service (`quarkus dev --clean`).
5. Try and do an order. Nothing will happen; we need Wookie fur.
6. Start the [`wookie-tamer`](wookie-tamer) service (`wookie-tamer/start-wookie-tamer-with-pact.sh`).
7. Do an order for a brown carpet. It should succeed, and an order should appear.
8. We've had to do quite a lot of starting of services, just to see if our app works... and this is a trivial application. Microservices are hard!
9. Show the tests. Because of course there are tests, we're responsible developers. The tests should have also automatically been running in the background of each app via continuous testing.
    - Run the back-end tests in IntelliJ
10. Refactor the [`Skein` record in the `wookie-tamer` app](wookie-tamer/src/main/java/org/wookie/tamer/Skein.java).
    - Colour is a British spelling. To refactor, `shift-f6` on `colour` variable, change it to ‘color’. Getters will update too. 
11. Sense check. Run the Java tests (all working), all working in all services (& continuous testing should have picked up changes as well & re-tested). 
12. Visit the web page again. It's all going to work, right, because the tests all worked?
13. Shouldn't the unit tests have caught this? Look at [`FurResourceTest`](wookie-tamer/src/test/java/org/wookie/tamer/FurResourceTest.java).
    - Because we're using the object model, our IDE automatically refactored `colour()` to `color()`.
    - We could have done more hard-coding in the tests to do json-path type expressions, but that's kind of icky, and the IDE might have refactored the hard-coded strings, too.
    - If we were to lock the hard-coded strings and say they can't be changed ... well, that's basically a contract. But it's only on one side, with no linkage to the other side, so it's pretty manual and error-prone.

## The first contract test 
### Consumer
1. How can we fix this? The app is broken, but the tests are all green. This is where contract tests give that extra validation to allow us to confirm assumptions. 
    - Pact is consumer-driven contract testing, so we start with the consumer. It's a bit like a TDD principle, start with the expectations.
2. Open the [`knitter`](knitter) project in a terminal (or switch to it if you already opened it).
3. In a terminal, run `quarkus extension add quarkus-pact-consumer`
4. Add the tests
   1. If you're in a hurry, use the git history to recreate the contract tests. Rollback any [`pom.xml`](knitter/pom.xml) and [`CarpetResourceContractTest`](knitter/src/test/java/org/wookie/knitter/CarpetResourceContractTest.java) changes from the git history.
   2. Otherwise, copy the [`CarpetResourceTest`](knitter/src/test/java/org/wookie/knitter/CarpetResourceTest.java) and use it as the starting point.
       - The test method stays exactly the same, because we're trying to validate the behaviour of *our* knitter code.
   3. The mocking logic is a bit different. [`CarpetResourceTest`](knitter/src/test/java/org/wookie/knitter/CarpetResourceTest.java) is mocking the entire call to the [`wookie-tamer`](wookie-tamer). Instead, we want it to actually make a call to the Pact mock server.
       - Delete the mock injection of the `WookieService` and the `BeforeEach` method (`setUp()`)
       - *pact-tab* for the following live template:
      
      ```java
      @Pact(consumer = "knitter")
      public V4Pact requestingFurContract(PactDslWithProvider builder) {
        var headers = Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
      
        // Here we define our mock, which is also our expectations for the provider
      
        // This defines what the body of the request could look like
        // we are generic and say it can be anything that meets the schema
        var furOrderBody = newJsonBody(body ->
          body
            .stringType("colour")
            .numberType("orderNumber")
        ).build();
      
        // And then define what the response from the mock should look like, which becomes part of the contract
        var furBody = newJsonBody(body -> body.stringValue("colour", "brown")).build();
      
        return builder
          .uponReceiving("A request for wookie fur")
            .path("/fur/order")
            .headers(headers)
            .method(HttpMethod.POST)
            .body(furOrderBody)
          .willRespondWith()
            .status(Status.OK.getStatusCode())
            .headers(headers)
            .body(furBody)
          .toPact(V4Pact.class);
      }
      ```
5. Finally, we need to add some extra annotations. *extend-tab* on the class declaration to add
```java
  @ExtendWith(PactConsumerTestExt.class)
  @PactTestFor(providerName = "wookie-tamer", port = "8096")
```
6. Show the [`CarpetResourceTest`](knitter/src/test/java/org/wookie/knitter/CarpetResourceTest.java) and then compare the two tests.
    - Explain the differences are because Pact acts both as a mock and a validator of all possible values.
7. Restart the tests. A json contract has appeared in [`knitter/target/pacts`](knitter/target/pacts).
8. The test should pass, we're the consumer, we made assumptions about how the provider should behave. But are those assumptions correct? Now is when we find out! 
9. Publish the Pact contract so it is available to the [`wookie-tamer` service](wookie-tamer) via 1 of the following 2 mechanisms:
    - If you are **NOT** using the Pact broker, run [`knitter/publish-contracts.sh`](knitter/publish-contracts.sh).
        - Normally this would be done by automatically checking it into source control or by using a pact broker.
    - If you **ARE** using the Pact broker, run [`knitter/publish-contracts-to-broker.sh`](knitter/publish-contracts-to-broker.sh).

### Provider (wookie-tamer)

1. Restore the [`FurResourceContractVerificationTests`](wookie-tamer/src/test/java/org/wookie/tamer/FurResourceContractVerificationTests.java) test from history. 
2. Add the pact provider dependency by running `quarkus extension add quarkus-pact-provider` (or by restoring [`wookie-tamer/pom.xml`](wookie-tamer/pom.xml) from history).
3. Run the Java tests, show the failure, explain how it could be fixed by negotiating a contract change or changing the source code.

## Carpet colour 

1. We were too vague in our contract. We actually said in the contract any colour would give a brown carpet. 
    - In [`CarpetResourceContractTest`](knitter/src/test/java/org/wookie/knitter/CarpetResourceContractTest.java), in the `requestingFurContract` contract method, change
    ```java
    var furOrderBody = newJsonBody(body ->
      body
        .stringType("colour")
        .numberType("orderNumber")
    ).build();
    ```
   
    to

    ```java
    var furOrderBody = newJsonBody(body ->
      body
        .stringValue("colour", "pink")
        .numberType("orderNumber")
    ).build();
    ```
   
The provider contract tests in `wookie-tamer` should still pass, but now the consumer tests in `knitter` are failing.

(Normally we would build up the tests, but to keep it simple, we will just change the test.)

2. Now publish the tests, and we have the failure. 
3. If you do want to add both tests, you can copy the existing pact and test methods, and change the colours in the copy. You will also need to add
```java
    @PactTestFor(pactMethod = "requestingPinkFurContract")
```
onto the test method. (By default, Pact will only stand up the first `@Pact` for the right provider.)

## Fallback and error handling

1. So we have a failing test, but what's the right fix? Fallback to brown isn't right, there should be some kind of error. 
2. We think we should have a `418`, not a brown carpet. `418` is `I'm a teapot`, maybe not the right code, but it's my code, so I can return what I want. Also, it keeps behaviour of the different services distinct. 
3. Look at [`NotFoundExceptionHandler`](knitter/src/main/java/org/wookie/knitter/NotFoundExceptionHandler.java), which turns `NotFoundException`s into `418`s. 
4. Update the tests to expect a `418`. 
   ```java
   @Test
   public void testCarpetEndpointForPinkCarpet() {
     var order = new CarpetOrder("pink", 16);
     given()
       .contentType(ContentType.JSON)
       .body(order)
       .when()
       .post("/carpet/order")
       .then()
       .statusCode(418);
   }
   ```
5. Update the implementation in [`CarpetResource`](knitter/src/main/java/org/wookie/knitter/CarpetResource.java) to wrap the invocation in a `try` and 
   ```java
   } 
   catch (Exception e) {
     throw new NotFoundException(order.getColour());
   }
   ```
6. The tests should pass. All good, except ... 
7. ... publish the tests, and they fail on the other side. 
8. Update the code to return `null` instead of brown as the fallback, and ... they should still fail.
9. In this case the right thing to do is to update the contract to return `204`, and update implementation to instead to a `null` check. The implementation would be to add a 
   ```java
   if (skein == null) {
   ```
check in `CarpetResource` and then throw an exception from the `null` check body.


