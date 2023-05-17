This is the demo "script" that Eric uses on his machine using his IntelliJ Live Templates.

## Pre-demo prep
Follow all steps in the [`Pre-demo` prep in the demo-script](demo-script.md#pre-demo-prep).

## Demo 1
Follow all the steps in [`The demo` in the demo-script](demo-script.md#the-demo-).

## Demo 2
### Weaver Service
1. Open [`CarpetResourceTest`]
2. Remove the mocked `WookieService` and the `setUp` method
3. Place cursor above `@QuarkusTest`
    - Use the `pactConsumerAnnotations` live template to insert class annotations
4. Place cursor before the `testCarpetEndpointForBrownColor`
    - Use the `requestingFurContract` live template to insert the contract
5. Place cursor above `@Test` on the `testCarpetEndpointForBrownColor` method
    - Use the `pactTestForRequestingFurContract` live template to insert the `@PactTestFor` annotation
6. Show tests pass
7. Publish the contract
    - In the `weaver` terminal, run `./publish-contracts-to-broker.sh`
8. Show the broker (https://wookie-carpets.pactflow.io)

---

### Wookie Tamer Service
9. Create test class `FurResourceContractVerificationTests`
10. Highlight the entire class
    - Use the `furResourceContractVerificationTests` live template to insert the class body
    - May need to resolve some imports - use IntelliJ CMD+1 for quick fix help on each import
11. Tests should be red. Go to broker (https://wookie-carpets.pactflow.io) and show tests red
12. Open `Skein` record, highlight `color` in record constructor
    - `SHIFT+F6` to refactor/rename to `colour`
13. Tests should be green. Return to broker & see green there too

<div style="page-break-after: always"></div>

## Demo 3
### Weaver Service
1. Write the contract `when I ask for pink I get 404`. Test should fail.
    - In `CarpetResourceTest`:
        - Use `requestingPinkFurContract` live template to insert the contract
        - Use `testCarpetEndpointForPinkCarpet` live template to insert the test
2. Fix weaver to handle `404`. Test is green when done.
    - In `CarpetResource`, add try/catch
       ```java
       catch (WebApplicationException ex) {
         throw new NotFoundException(order.colour());
       }
       ```
3. Publish contract
    - In the `weaver` terminal, run `./publish-contracts-to-broker.sh 2.0`
4. Go to broker (https://wookie-carpets.pactflow.io) and show updated contract

---

### Wookie Tamer Service
5. `CMD-S` tp trigger continuous testing to find contract. Tests should be red. Show broker.
6. Fix wookie tamer to return `null` which will send a `204`
    - Open `FurResource`
    - Change `wookieColor = WookieColor.BROWN;` to `return null;`
7. Congratulate on a job well-done, except...why are those tests red?! I'm doing error handling. Show broker.
8. Debate about `204` vs `404` & which is better option. Making `404` is more code, `204` should be fine. Let's just adjust mock to match reality.

---

### Back to Weaver Service
9. In `CarpetResourceTest`, change `.status(Status.NOT_FOUND.getStatusCode())` to `.status(Status.NO_CONTENT.getStatusCode())`
    - Now my mock should match reality!
10. Publish contract
    - In the `weaver` terminal, run `./publish-contracts-to-broker.sh 3.0`
11. Ok, my mock now matches reality, but now both weaver & tamer tests are red? WTH?!?!
    - Changing expectation to match reality doesn't get things green because apparently weaver doesn't properly handle `204`.
    - Need to have a conversation about `204`/`404` between us