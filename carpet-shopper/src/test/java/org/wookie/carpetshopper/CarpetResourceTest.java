package org.wookie.carpetshopper;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import io.restassured.http.ContentType;

@QuarkusTest
public class CarpetResourceTest {

    @Test
    public void carpetReturnsTheOrderNumber() {
        CarpetOrder order = new CarpetOrder("white", 67);
        Carpet carpet = given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/bff/order")
                .then()
                .statusCode(200)
                .extract().as(Carpet.class);

        assertEquals(67, carpet.orderNumber());
    }

    @Test
    public void carpetEndpointForWhiteCarpet() {
        CarpetOrder order = new CarpetOrder("white", 12);
        Carpet carpet = given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/bff/order")
                .then()
                .statusCode(200)
                .extract().as(Carpet.class);

        assertEquals("white", carpet.colour());
    }


}