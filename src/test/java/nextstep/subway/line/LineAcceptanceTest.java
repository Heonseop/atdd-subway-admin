package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.dto.LineResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 인수 테스트")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "yellow");

        ExtractableResponse<Response> saveResponse =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(saveResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> getResponse = RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(convertToFiledName(getResponse, "name")).containsAnyOf("2호선");
        assertThat(convertToFiledName(getResponse, "color")).containsAnyOf("yellow");
    }

    /**
     * given 2개의 지하철 노선을 생성하고
     * when 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        지하철_노선_생성("2호선", "red");
        지하철_노선_생성("1호선", "blue");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(convertToDTO(response)).hasSize(2);
    }

    /**
     * 전달받은 지하철역 목록을 저장한다
     * @param name 노선 이름
     * @param color 노선 색상
     */
    private ExtractableResponse<Response> 지하철_노선_생성(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    /**
     * 지하철 노선 목록을 조회한다
     */
    private ExtractableResponse<Response> 지하철_노선_조회() {
        return RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private List<String> convertToFiledName(ExtractableResponse<Response> response, String field) {
        return response.jsonPath().getList(field, String.class);
    }

    private List<LineResponse> convertToDTO(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("", LineResponse.class);
    }

    private List<String> convertToFieldNames(ExtractableResponse<Response> response, String field) {
        return response.jsonPath().getList(field, String.class);
    }
}
