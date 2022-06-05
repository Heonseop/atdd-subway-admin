package nextstep.subway.section;


import static nextstep.subway.line.LineAcceptanceTest.노선을_생성한다;
import static nextstep.subway.station.StationAcceptanceTest.지하철역을_생성_한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.dto.LineRequest;
import nextstep.subway.dto.LineResponse;
import nextstep.subway.dto.SectionRequest;
import nextstep.subway.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("노선 구간 관련 기능")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {
    @LocalServerPort
    int port;
    StationResponse 인천역;
    StationResponse 동인천역;
    LineResponse 호선_1;
    LineRequest 생성_요청한_1호선;

    //초기 거리
    private final static int INIT_DISTANCE = 10;


    @BeforeEach
    void setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }

        //given 지하철 역을 생성하고 지하철 노선을 생성한다.
        인천역 = 지하철역을_생성_한다("인천역").as(StationResponse.class);
        동인천역 = 지하철역을_생성_한다("동인천역").as(StationResponse.class);
        LineRequest 생성_요청한_1호선 = new LineRequest("1호선", "파랑색",INIT_DISTANCE, 인천역.getId(), 동인천역.getId());

        호선_1 = 노선을_생성한다(생성_요청한_1호선).as(LineResponse.class);

    }

    /**
     * given 지하철 역을 생성하고 지하철 노선을 생성한다.
     * when 노선의 구간을 조회한다.
     * then 구간의 정보를 얻는다.
     * */
    @Test
    @DisplayName("노선에 구간을 조회")
    void searchSection() {
        //when 노선의 구간을 조회한다.
        final JsonPath 노선의_구간을_조회 = 노선의_구간을_전부_조회(호선_1.getId()).jsonPath();

        //then 구간의 정보를 얻는다.
        assertAll(
                () -> assertThat(노선의_구간을_조회.getInt("distance[0]")).isEqualTo(10),
                () -> assertThat(노선의_구간을_조회.getString("upStation.name[0]")).isEqualTo("인천역"),
                () -> assertThat(노선의_구간을_조회.getString("downStation.name[0]")).isEqualTo("동인천역")
        );
    }


    /*
    * Given 추가할 노선을 생성한다.
    * when 노선을 추가 등록하면
    * then 노선이 등록된다.
     * then 기존의 구간은 거리가 신규 노선의 거리만큼 줄어든다
    */
    @Test
    @DisplayName("노선에 구간 등록")
    void addSection() {
        //Given 추가할 노선을 생성한다.
        StationResponse 도화역 = 지하철역을_생성_한다("도화역").as(StationResponse.class);

        //when 노선을 추가 등록하면
        SectionRequest 구간추가요청 = new SectionRequest(도화역.getId(), 동인천역.getId(), 3);
        final ExtractableResponse<Response> 노선의_구간을_추가한다 = 노선의_구간을_추가한다(호선_1.getId(), 구간추가요청);
        JsonPath 노선의_구간을_전부_조회 = 노선의_구간을_전부_조회(호선_1.getId()).jsonPath();

        assertAll(
                //then 노선구간이 등록된다.
                () -> assertThat(노선의_구간을_추가한다.response().statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(노선의_구간을_전부_조회
                        .getString("find{it -> it.upStation.name == '도화역'}.upStation.name")).isEqualTo("도화역"),
                //then 기존의 구간은 거리가 신규 노선의 거리만큼 줄어든다
                () -> assertThat(노선의_구간을_전부_조회
                        .getInt("find{it -> it.upStation.name == '인천역'}.distance.value")).isEqualTo(7)
        );


    }

    //given 지하철 역을 생성하고 지하철 노선을 추가한다.
    //when 기존 노선 보다 긴 노선을 등록 요청 한다.
    //then 등록이 되지 않는다.
    @Test
    @DisplayName("기존 노선보다 긴 노선 등록")
    void longAddSection() {
        //when 기존 노선 보다 긴 노선을 등록 요청 한다.
        StationResponse 도화역 = 지하철역을_생성_한다("도화역").as(StationResponse.class);
        SectionRequest 구간추가요청 = new SectionRequest(도화역.getId(), 동인천역.getId(), 13);
        final ExtractableResponse<Response> 거리가긴_구간을_추가한다 = 노선의_구간을_추가한다(호선_1.getId(), 구간추가요청);
        //then 등록이 되지 않는다.

        assertThat(거리가긴_구간을_추가한다.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    //given 지하철 역을 생성하고 지하철 노선을 추가한다.
    //when 기존 노선과 같은 노선을 등록한다
    //then 등록이 되지 않는다.
    @Test
    @DisplayName("이미 존재하는 구간을 노선 구간에 등록한다.")
    void addExistSection() {
        //when 기존 노선과 같은 노선을 등록한다.
        SectionRequest 이미_존재하는_노선 = new SectionRequest(인천역.getId(), 동인천역.getId(), 3);
        ExtractableResponse<Response> 이미_존재하는_노선을_추가한다 = 노선의_구간을_추가한다(호선_1.getId(), 이미_존재하는_노선);
        //then 등록이 되지 않는다.
        assertThat(이미_존재하는_노선을_추가한다.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("상행역과 하행역이 포함되지 않는 노선을 구간에 등록한다.")
    void noStationLine() {

        //given 지하철 역을 생성하고 지하철 노선을 추가한다.
        StationResponse 도화역 = 지하철역을_생성_한다("도화역").as(StationResponse.class);
        StationResponse 신도림 = 지하철역을_생성_한다("신도림").as(StationResponse.class);

        SectionRequest 상행역과_하행역이_포함되지_않는_노선 = new SectionRequest(신도림.getId(), 도화역.getId(), 3);
        ExtractableResponse<Response> 상행역과_하행역이_포함되지_않는_노선을_추가한다 = 노선의_구간을_추가한다(호선_1.getId(), 상행역과_하행역이_포함되지_않는_노선);
        //when 상행역과 하행역이 포함되지 않는 노선을 구간에 등록 요청한다.

        //then 등록이 되지 않는다.
        assertThat(상행역과_하행역이_포함되지_않는_노선을_추가한다.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    /*
    * given 지하철 역을 생성하고 지하철 노선을 추가한다.
    * when 상행역 노선의 새로운 역이며 하행역이 기존 상행종점인역인 구간을 등록 요청한다.
     * then 새로운 역이 상행 종점에 등록된다. - 이때는 거리를 신경쓰지 않는다.
    */
    @Test
    @DisplayName("새로운 역을 상행 종점에 등록한다.")
    void upStationAddSection() {
        StationResponse 도화역 = 지하철역을_생성_한다("도화역").as(StationResponse.class);
        //StationResponse 인천역 = 지하철역을_생성_한다("인천역").as(StationResponse.class);

        //when 상행역 노선의 새로운 역이며 하행역이 기존 상행종점인역인 구간을 등록 요청한다.
        SectionRequest 상행_종점_구간 = new SectionRequest(도화역.getId(), 인천역.getId(), 3);
        ExtractableResponse<Response> 상행_종점에_등록한다 = 노선의_구간을_추가한다(호선_1.getId(), 상행_종점_구간);

        //then 새로운 역이 상행 종점에 등록된다. - 이때는 거리를 신경쓰지 않는다.
        final JsonPath 노선의_구간을_조회 = 노선의_구간을_전부_조회(호선_1.getId()).jsonPath();
        assertAll(
                () -> assertThat(노선의_구간을_조회.getString("upStation[0].name")).isEqualTo("도화역"),
                () -> assertThat(노선의_구간을_조회.getInt("distance[0]")).isEqualTo(3)
        );
    }

    //given 지하철 역을 생성하고 지하철 노선을 추가한다.
    //when 상행역이 기존 하행 종점이며 하행역 노선의 새로운 역인 노선을 등록 요청한다.
    //then 새로운 역을 하행 종점에 등록된다..- 이때는 거리를 신경쓰지 않는다.
    @Test
    @DisplayName("새로운 역을 하행 종점에 등록한다.")
    void downStationAddSection() {
        StationResponse 도화역 = 지하철역을_생성_한다("도화역").as(StationResponse.class);
        //StationResponse 인천역 = 지하철역을_생성_한다("인천역").as(StationResponse.class);

        //when 상행역이 기존 하행 종점이며 하행역 노선의 새로운 역인 노선을 등록 요청한다.
        SectionRequest 하행_종점_구간 = new SectionRequest(동인천역.getId(), 도화역.getId(), 3);
        ExtractableResponse<Response> 하행_종점에_등록한다 = 노선의_구간을_추가한다(호선_1.getId(), 하행_종점_구간);

        //then 새로운 역이 상행 종점에 등록된다. - 이때는 거리를 신경쓰지 않는다.
        final JsonPath 노선의_구간을_조회 = 노선의_구간을_전부_조회(호선_1.getId()).jsonPath();
        assertAll(
                () -> assertThat(노선의_구간을_조회.getString("upStation[1].name")).isEqualTo("동인천역"),
                () -> assertThat(노선의_구간을_조회.getInt("distance[1]")).isEqualTo(3)
        );
    }


    //given 지하철 역을 생성하고 지하철 노선을 추가한다.
    //when 구간역이 제거하면
    //then 제거되지 않는다.
    @Test
    @DisplayName("구간이 하나인 역 제거")
    void oneSectionDelete() {

    }

    //given 다구간 호선을 생성 한다.
    //given 존재 하지 않은 역을 추가한다.
    //when 존재하지 않은 역을 제거 하면
    //then 제거되지 않는다.
    @Test
    @DisplayName("존재하지 않은 역 제거")
    void noExistStationDelete() {

    }


    //given 지하철 역을 생성하고 지하철 노선을 추가한다.
    //when 종점을 제거한다.
    //then 종점이 제거된다.
    //then 이전 역이 종점이 된다.
    @Test
    @DisplayName("종점역을 제거 한다.")
    void lastStationDelete() {

    }

    //given 지하철 역을 생성하고 지하철 노선을 추가한다.
    //when 종점을 제거한다.
    //then 종점이 제거된다.
    //then 이전 역이 종점이 된다.
    @Test
    @DisplayName("중간역을 제거 한다")
    void middleStationDelete() {

    }

    private static ExtractableResponse<Response> 노선의_구간을_추가한다(Long lineId, SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .body(sectionRequest)
                .pathParam("lineId", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/{lineId}/selections")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 노선의_구간을_전부_조회(Long lineId) {
        return RestAssured.given().log().all()
                .pathParam("lineId", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}/sections")
                .then().log().all()
                .extract();
    }


}
