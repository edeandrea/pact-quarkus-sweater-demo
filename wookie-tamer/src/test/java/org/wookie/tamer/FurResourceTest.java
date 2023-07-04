package org.wookie.tamer;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkus.test.junit.QuarkusTest;

import io.restassured.http.ContentType;

@QuarkusTest
public class FurResourceTest {

    @ParameterizedTest
    @ValueSource(strings = { "white", "black" })
    public void furEndpoint(String colour) {
        Order order = new Order(colour, 2);
        Skein skein = given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/fur/order")
                .then()
                .statusCode(200)
                .extract().as(Skein.class);

        assertEquals(skein.colour(), colour);
    }
}