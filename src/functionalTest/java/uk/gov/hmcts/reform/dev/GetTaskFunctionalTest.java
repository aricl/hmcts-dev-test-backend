package uk.gov.hmcts.reform.dev;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GetTaskFunctionalTest {

    @Value("${TEST_URL:http://localhost:8080}")
    private String testUrl;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = testUrl;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    void getTaskSuccessfully() {
        String dueDate = LocalDateTime.now().plusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Map<String, Object> payload = Map.of(
            "title", "Eat B vitamins",
            "description", "I can't eat them for you",
            "status", "open",
            "due_date", dueDate
        );

        Response createResponse = given()
            .contentType(ContentType.JSON)
            .body(payload)
            .when()
            .post("/task")
            .then()
            .extract().response();

        Long id = createResponse.jsonPath().getLong("id");

        Response getResponse = given()
            .contentType(ContentType.JSON)
            .body(payload)
            .when()
            .get("/task/" + id)
            .then()
            .extract().response();

        Assertions.assertEquals(id, getResponse.jsonPath().getLong("id"));
        Assertions.assertEquals(payload.get("title"), getResponse.jsonPath().getString("title"));
    }
}
