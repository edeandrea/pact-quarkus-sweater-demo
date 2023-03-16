package org.sheepy.knitter;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactDirectory;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jakarta.ws.rs.HttpMethod;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "farmer", port = "8096")
@PactDirectory("target/pacts")
@QuarkusTest
public class SweaterResourceContractTest {

    @Pact(provider = "farmer", consumer = "knitter")
    public V4Pact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Here we define our mock, which is also our expectations for the provider

        // This defines what the body of the request could look like; we are generic and say it can be anything that meets the schema
        DslPart woolOrderBody = new PactDslJsonBody()
                .stringType("colour")
                .numberType("orderNumber");

        String woolBody = "{\"colour\":\"white\"}\n";

        return builder
                .uponReceiving("post request")
                .path("/wool/order")
                .headers(headers)
                .method(HttpMethod.POST)
                .body(woolOrderBody)
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(woolBody)
                .toPact(V4Pact.class);
    }

    @Test
    public void testSweaterEndpointForWhiteSweater() {
        SweaterOrder order = new SweaterOrder("white", 12);
        Sweater sweater = given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/sweater/order")
                .then()
                .statusCode(200)
                .extract().as(Sweater.class);

        assertEquals("white", sweater.getColour());
    }


}