package com.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class PetStoreTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Config.BASE_URI;
    }

    @Test
    public void testFindPetsByStatusAvailable() {
        checkPetsStatus("available", Config.STATUS_OK);
    }

    @Test
    public void testFindPetsByStatusPending() {
        checkPetsStatus("pending", Config.STATUS_OK);
    }

    @Test
    public void testFindPetsByStatusSold() {
        checkPetsStatus("sold", Config.STATUS_OK);
    }

    @Test
    public void testFindPetsByInvalidStatus() {
        checkPetsStatus("unknown", Config.STATUS_BAD_REQUEST); // Invalid status
    }

    @Test
    public void testFindPetsByEmptyStatus() {
        Response response = RestUtils.getPetsWithEmptyStatus();
        validateErrorResponse(response, Config.STATUS_BAD_REQUEST, "invalid status value");
    }

    @Test
    public void testFindPetsByNoStatusParameter() {
        Response response = RestUtils.getPetsWithNoStatusParameter();
        validateErrorResponse(response, Config.STATUS_BAD_REQUEST, "missing status value");
    }

    private void checkPetsStatus(String status, int expectedStatusCode) {
        Response response = RestUtils.getPetsByStatus(status);
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode);

        if (expectedStatusCode == Config.STATUS_OK) {
            List<String> statuses = response.jsonPath().getList("status");
            for (String petStatus : statuses) {
                Assert.assertEquals(petStatus, status, "Pet status mismatch!");
            }
        }
    }

    private void validateErrorResponse(Response response, int expectedStatusCode, String expectedMessage) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode);
        Assert.assertTrue(response.getBody().asString().contains(expectedMessage));
    }
}
