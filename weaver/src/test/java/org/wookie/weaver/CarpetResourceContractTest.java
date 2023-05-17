package org.wookie.weaver;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.quarkus.test.junit.QuarkusTest;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.http.ContentType;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "wookie-tamer", port = "8096")
@QuarkusTest
public class CarpetResourceContractTest {

	@Pact(consumer = "weaver")
	public V4Pact requestingFurContract(PactDslWithProvider builder) {
		var headers = Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

		// Here we define our mock, which is also our expectations for the provider

		// This defines what the body of the request could look like;
		// we are generic and say it can be anything that meets the schema
		var furOrderBody = newJsonBody(body ->
			body
				.stringType("colour", "brown")
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

	@Pact(consumer = "weaver")
	public V4Pact requestingPinkFurContract(PactDslWithProvider builder) {
		var headers = Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

		// Here we define our mock, which is also our expectations for the provider

		// This defines what the body of the request could look like;
		// in this case we are saying the colour in the request MUST be pink
		var furOrderBody = newJsonBody(body ->
			body
				.stringValue("colour", "pink")
				.numberType("orderNumber")
		).build();

		return builder
			.uponReceiving("A request for pink wookie fur")
				.path("/fur/order")
				.headers(headers)
				.method(HttpMethod.POST)
				.body(furOrderBody)
			.willRespondWith()
				.status(Status.NOT_FOUND.getStatusCode())
			.toPact(V4Pact.class);
	}

	@Test
	@PactTestFor(pactMethod = "requestingFurContract")
	public void testCarpetEndpointForCarpet() {
		var order = new CarpetOrder("brown", 12);
		var carpet = given()
			.contentType(ContentType.JSON)
			.body(order)
			.when()
			.post("/carpet/order")
			.then()
			.statusCode(200)
			.extract().as(Carpet.class);

		assertEquals("brown", carpet.colour());
	}

	@Test
	@PactTestFor(pactMethod = "requestingPinkFurContract")
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
}