package com.hcl.sa.utils.api;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.utils.bigfix.Credentials;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class ApiRequests {

    public RequestSpecification setBaseURIAndBasicAuthentication() {
        RestAssured.baseURI = ConsoleConsts.BIGFIX_SERVER_URI.text;
        RestAssured.useRelaxedHTTPSValidation();
        Credentials consoleCred = Credentials.valueOf(Credentials.CONSOLE.name());
        RequestSpecification bigfixCredentials = given().auth().preemptive().basic(consoleCred.getUsername(), consoleCred.getPassword());
        return bigfixCredentials;
    }

    public RequestSpecification setWasLibertyURIAndBasicAuthentication() {
        RestAssured.baseURI = CreatePlanConsts.WAS_LIBERTY_SERVER_URI.text;
        RestAssured.useRelaxedHTTPSValidation();
        Credentials consoleCred = Credentials.valueOf(Credentials.CONSOLE.name());
        RequestSpecification bigfixCredentials = given().auth().preemptive().basic(consoleCred.getUsername(), consoleCred.getPassword());
        return bigfixCredentials;
    }

    public RequestSpecification setSaRestURIAndBasicAuthentication() {
        RestAssured.baseURI = CreatePlanConsts.SA_REST_SERVER_URI.text;
        RestAssured.useRelaxedHTTPSValidation();
        Credentials consoleCred = Credentials.valueOf(Credentials.CONSOLE.name());
        RequestSpecification bigfixCredentials = given().auth().preemptive().basic(consoleCred.getUsername(), consoleCred.getPassword());
        return bigfixCredentials;
    }

    public Response GET(String uri) {
        Response response = checkForStatusCode(setBaseURIAndBasicAuthentication().get(uri));
        return response;
    }

    public Response GET(String parameter, String parameterValue, String uri) {
        Response response = checkForStatusCode(setBaseURIAndBasicAuthentication().and().pathParam(parameter, parameterValue).and().when().get(uri));
        return response;
    }

    public Response POST(String uri) {
        Response response = checkForStatusCode(setBaseURIAndBasicAuthentication().contentType(ContentType.JSON).when().post(uri));
        return response;
    }

    public Response POST(String param, String paramValue, String uri) {
        Response response = checkForStatusCode(setBaseURIAndBasicAuthentication().contentType(ContentType.JSON).and().
                pathParam(param, paramValue).and().when().post(uri));
        return response;
    }

    public Response POST(String body, String uri) {
        Response response = checkForStatusCode(setBaseURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).body(body).when().post(uri));
        return response;
    }

    public Response POST(String param, String paramValue, String body, String uri) {
        Response response = checkForStatusCode(setBaseURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY)
                .and().pathParam(param, paramValue).and().body(body).when().post(uri));
        return response;
    }

    public Response POST(RequestSpecification reqSpecs, String uri) {
        Response response = checkForStatusCode(reqSpecs.when().post(uri));
        return response;
    }

    public Response POST(RequestSpecification reqSpecs, String uri, String body) {
        Response response = checkForStatusCode(reqSpecs.and().body(body).when().post(uri));
        return response;
    }

    public Response checkForStatusCode(Response res) {
        return res.then().assertThat().statusCode(200).extract().response();
    }

    public Response DELETE(String param, String paramValue, String uri) {
        Response response = checkForStatusCode(setBaseURIAndBasicAuthentication().contentType(ContentType.JSON).and().
                pathParam(param, paramValue).and().when().delete(uri));
        return response;
    }

    public Response DELETE(RequestSpecification reqSpecs, String uri) {
        Response response = checkForStatusCode(reqSpecs.and().when().delete(uri));
        return response;
    }

    public Response GET(RequestSpecification reqSpecs, String uri) {
        Response response = checkForStatusCode(reqSpecs.when().get(uri));
        return response;
    }


    public RequestSpecification invalidSaAuthentication() {
        RestAssured.baseURI = CreatePlanConsts.SA_REST_SERVER_URI.text;;
        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification bigfixCredentials = given().auth().preemptive().basic("","");
        return bigfixCredentials;

    }
}