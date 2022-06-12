package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.base.BaseUnitTest;
import nextstep.subway.dto.LineResponse;
import nextstep.subway.dto.SectionRequest;
import nextstep.subway.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Collectors;

import static nextstep.subway.Line.LineAcceptanceTest.지하철_노선_생성_신분당선;
import static nextstep.subway.station.StationAcceptanceTest.createStation;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest extends BaseUnitTest {

    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 판교역;
    private StationResponse 신림역;
    private StationResponse 삼성역;
    private StationResponse 강남구청역;
    private StationResponse 미금역;
    private LineResponse 신분당선;

    @BeforeEach
    void given() {
        //given
        //지하철역 등록
        지하철역_등록();

        //given
        //노선 등록
        신분당선 = 지하철_노선_생성_신분당선(강남역, 광교역, 10L).as(LineResponse.class);
    }

    @DisplayName("역과 역 사이에 새로운 구간을 등록한다. (상행역이 같은 경우)")
    @Test
    void 구간_등록_테스트_동일_상행역() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        SectionRequest sectionRequest = SectionRequest.of(강남역.getId(), 판교역.getId(), 1L);
        ExtractableResponse<Response> createResponse = 지하철_구간_등록(신분당선.getId(), sectionRequest);

        // then
        // 지하철_노선_조회
        ExtractableResponse<Response> response = 지하철_노선_조회(신분당선.getId());
        LineResponse lineResponse = getLineResponse(response);

        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineResponse.getSections().size()).isEqualTo(2),
                () -> assertThat(lineResponse.getSections().stream()
                        .filter(sectionResponse -> sectionResponse.getUpStationId().equals(강남역.getId()))
                        .findFirst().get().getDistance()).isEqualTo(1),
                () -> assertThat(lineResponse.getSections().stream()
                        .filter(sectionResponse -> sectionResponse.getUpStationId().equals(판교역.getId()))
                        .findFirst().get().getDistance()).isEqualTo(9)
        );
    }

    @DisplayName("역과 역 사이에 새로운 구간을 등록한다. (하행역이 같은 경우)")
    @Test
    void 구간_등록_테스트_동일_하행역() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        SectionRequest sectionRequest = SectionRequest.of(판교역.getId(), 광교역.getId(), 9L);
        ExtractableResponse<Response> createResponse = 지하철_구간_등록(신분당선.getId(), sectionRequest);

        // then
        // 지하철_노선_조회
        ExtractableResponse<Response> response = 지하철_노선_조회(신분당선.getId());
        LineResponse lineResponse = getLineResponse(response);

        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(lineResponse.getSections().size()).isEqualTo(2),
                () -> assertThat(lineResponse.getSections().stream()
                        .filter(sectionResponse -> sectionResponse.getUpStationId().equals(강남역.getId()))
                        .findFirst().get().getDistance()).isEqualTo(1),
                () -> assertThat(lineResponse.getSections().stream()
                        .filter(sectionResponse -> sectionResponse.getUpStationId().equals(판교역.getId()))
                        .findFirst().get().getDistance()).isEqualTo(9)
        );
    }

    @DisplayName("새로운 역을 상행 종점으로 등록할 경우")
    @Test
    void 상행_종점_등록() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        SectionRequest sectionRequest = SectionRequest.of(강남구청역.getId(), 강남역.getId(), 9L);
        ExtractableResponse<Response> createResponse = 지하철_구간_등록(신분당선.getId(), sectionRequest);

        // then
        // 지하철_노선_조회
        ExtractableResponse<Response> response = 지하철_노선_조회(신분당선.getId());
        LineResponse lineResponse = getLineResponse(response);

        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineResponse.getStations().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()).contains(강남구청역.getName())).isTrue(),
                () -> assertThat(lineResponse.getStations().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()).contains(강남역.getName())).isFalse()
        );
    }

    @DisplayName("새로운 역을 하행 종점으로 등록할 경우")
    @Test
    void 하행_종점_등록() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        SectionRequest sectionRequest = SectionRequest.of(광교역.getId(), 미금역.getId(), 9L);
        ExtractableResponse<Response> createResponse = 지하철_구간_등록(신분당선.getId(), sectionRequest);

        // then
        // 지하철_노선_조회
        ExtractableResponse<Response> response = 지하철_노선_조회(신분당선.getId());
        LineResponse lineResponse = getLineResponse(response);

        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineResponse.getStations().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()).contains(미금역.getName())).isTrue(),
                () -> assertThat(lineResponse.getStations().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()).contains(광교역.getName())).isFalse()
        );
    }

    @DisplayName("역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없다.")
    @Test
    void 예외_기존역보다_길이가_큰_경우() {
        // when
        // 지하철_노선에_지하철역_등록_요청
        SectionRequest sectionRequest = SectionRequest.of(판교역.getId(), 광교역.getId(), 10L);
        ExtractableResponse<Response> createResponse = 지하철_구간_등록(신분당선.getId(), sectionRequest);

        // then
        // 에러 확인
        Assertions.assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없다.")
    @Test
    void 예외_동일한_상행역_하행역_등록() {
        // given
        구간_등록_테스트_동일_상행역();

        // when
        // 동일한 구간 등록
        SectionRequest sectionRequest = SectionRequest.of(강남역.getId(), 판교역.getId(), 1L);
        ExtractableResponse<Response> response = 지하철_구간_등록(신분당선.getId(), sectionRequest);

        // then
        // 에러 확인
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없다.")
    @Test
    void 예외_상행역_하행역_미포함() {
        // given
        구간_등록_테스트_동일_상행역();

        // when
        // 상행역, 하행역 포함하지 않고 구간 등록
        SectionRequest sectionRequest = SectionRequest.of(신림역.getId(), 삼성역.getId(), 1L);
        ExtractableResponse<Response> response = 지하철_구간_등록(신분당선.getId(), sectionRequest);

        // then
        // 에러 확인
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_구간_등록(Long id, SectionRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + id + "/sections")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public LineResponse getLineResponse(ExtractableResponse<Response> response) {
        return response.as(LineResponse.class);
    }

    private void 지하철역_등록() {
        강남역 = createStation("강남역").as(StationResponse.class);
        광교역 = createStation("광교역").as(StationResponse.class);
        판교역 = createStation("판교역").as(StationResponse.class);
        신림역 = createStation("신림역").as(StationResponse.class);
        삼성역 = createStation("삼성역").as(StationResponse.class);
        미금역 = createStation("미금역").as(StationResponse.class);
        강남구청역 = createStation("강남구청역").as(StationResponse.class);
    }
}
