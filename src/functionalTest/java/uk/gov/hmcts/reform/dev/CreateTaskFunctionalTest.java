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
class CreateTaskFunctionalTest {

    @Value("${TEST_URL:http://localhost:8080}")
    private String testUrl;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = testUrl;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    void createTaskSuccessfully() {
        // Build task payload as a Map (could use a POJO and ObjectMapper as well)
        String dueDate = LocalDateTime.now().plusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Map<String, Object> payload = Map.of(
            "title", "Functional Test Task",
            "description", "Check creation of task via functional test",
            "status", "OPEN",
            "due_date", dueDate
        );

        Response response = given()
            .contentType(ContentType.JSON)
            .body(payload)
            .when()
            .post("/task")
            .then()
            .extract().response();

        Assertions.assertEquals(
            201,
            response.statusCode(),
            "Expected 201 Created"
        );

        Assertions.assertNotNull(
            response.jsonPath().get("id"),
            "Task ID should be present"
        );
        Assertions.assertEquals(
            payload.get("title"),
            response.jsonPath().getString("title")
        );
    }
}
