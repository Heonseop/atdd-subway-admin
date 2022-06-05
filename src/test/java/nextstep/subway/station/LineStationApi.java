package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.dto.LineStationRequest;
import org.springframework.http.MediaType;

public class LineStationApi {
    public static ExtractableResponse<Response> 지하철구간_생성(Long lineId, LineStationRequest lineStationRequest) {
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(lineStationRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/line/" + lineId + "/section")
                        .then().log().all()
                        .extract();

        return response;
    }
}