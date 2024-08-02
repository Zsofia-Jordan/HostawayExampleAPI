package com.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestUtils {
    
    public static Response getPetsByStatus(String status) {
        RequestSpecification request = RestAssured.given();
        return request.queryParam("status", status).get("/pet/findByStatus");
    }

    public static Response getPetsWithNoStatusParameter() {
        return RestAssured.given().get("/pet/findByStatus");
    }

    public static Response getPetsWithEmptyStatus() {
        return RestAssured.given().queryParam("status", "").get("/pet/findByStatus");
    }
}
