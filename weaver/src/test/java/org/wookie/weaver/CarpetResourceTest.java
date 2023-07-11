package org.wookie.weaver;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

import io.restassured.http.ContentType;

@QuarkusTest
public class CarpetResourceTest {

    @InjectMock
    @RestClient
    WookieService mock;

    @BeforeEach
    public void setUp() {
        when(mock.getFur(any())).thenReturn(new Skein("brown"));
    }

    @Test
    public void testCarpetEndpointForBrownColor() {
        CarpetOrder order = new CarpetOrder("brown", 12);
        Carpet carpet = given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/carpet/order")
                .then()
                .statusCode(200)
                .extract().as(Carpet.class);

        assertEquals("brown", carpet.colour());
    }


}