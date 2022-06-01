# 지하철 노선도 관리
## 도메인
지하철 역(station)
- 지하철 역 속성
- 이름(name)  

지하철 구간(section)
- 지하철 (상행 방향)역과 (하행 방향)역 사이의 연결 정보
- 지하철 구간 속성
    - 길이(distance)  
    
지하철 노선(line)
- 지하철 구간의 모음으로 구간에 포함된 지하철 역의 연결 정보
- 지하철 노선 속성
    - 노선 이름(name)
    - 노선 색(color)
    
## 기능 요구사항
- 지하철역 인수 테스트를 완성하세요.
- [x] 지하철역 목록 조회 인수 테스트 작성하기
- [x] 지하철역 삭제 인수 테스트 작성하기
- 인수 조건을 기반으로 지하철 노선 관리 기능을 구현하세요.
- [x] 지하철 노선 생성
- [x] 지하철 노선 목록 조회
- [x] 지하철 노선 조회
- [x] 지하철 노선 수정
- [x] 지하철 노선 삭제
- 지하철 구간 추가 기능을 구현하세요.
- [x] 인수 조건을 작성하세요.
- [x] 인수 조건을 검증하는 인수 테스트를 작성하세요.
- [x] 예외 케이스에 대한 검증도 포함하세요.
- 지하철 구간 제거 기능을 구현하세요.  
- [x] 인수 조건을 작성하세요.
- [x] 인수 조건을 검증하는 인수 테스트를 작성하세요.
- [x] 예외 케이스에 대한 검증도 포함하세요.

## 프로그래밍 요구사항
- 아래의 순서로 기능을 구현하세요.
    - 요구사항을 기반으로 인수 조건 작성
    - 인수 조건을 검증하는 인수 테스트 작성
    - 인수 테스트를 충족하는 기능 구현
- 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
- 인수 테스트의 결과가 다른 인수 테스트에 영향을 끼치지 않도록 인수 테스트를 서로 격리 시키세요.
- 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링 하세요.

## 인수 조건
- [x] Feature: 지하철역 생성하기  
    Scenario: 지하철역 생성    
    When: 사용자는 지하철역 생성을 요청한다.   
    Then: 지하철 역이 생성된다.  
    Then: 사용자는 지하철역 목록 조회 시 생성한 역을 찾을 수 있다.

- [x] Feature: 지하철역 생성 실패  
    Scenario: 같은 지하철역 생성  
    Given: 지하철역이 생성되어 있다.  
    When: 사용자는 같은 이름의 지하철역 생성을 요청한다.      
    Then: 지하철 역이 생성되지 않는다.  
    
- [x] Feature: 지하철역 목록 조회하기  
    Scenario: 지하철역 목록 조회  
    Given: 2개의 지하철역이 생성되어 있다.  
    When: 사용자는 지하철역 목록을 요청한다.  
    Then: 사용자는 2개의 지하철역 목록을 응답받는다.
    
- [x] Feature: 지하철역 삭제하기  
    Scenario: 지하철역 삭제  
    Given: 지하철역이 생성되어 있다.  
    When: 사용자는 지하철역 삭제를 요청한다.  
    Then: 사용자는 지하철역 목록 조회 시 생성한 역을 찾을 수 없다.
    
- [x] Feature: 지하철 노선 생성하기  
    Scenario: 지하철 노선 생성    
    When: 사용자는 지하철 노선 생성을 요청한다.  
    Then: 지하철 노선이 생성된다.  

- [X] Feature: 지하철 노선 목록 조회하기  
    Scenario: 지하철 노선 목록 조회  
    Given: 2개의 지하철 노선이 생성되어 있다.  
    When: 사용자는 지하철 노선 목록 조회를 요청한다.  
    Then: 사용자는 2개의 지하철 노선 목록을 응답받는다.  

- [x] Feature: 지하철 노선 조회하기  
    Scenario: 지하철 노선 조회  
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 생성한 지하철 노선 조회를 요청한다.  
    Then: 사용자는 생성한 지하철 노선의 정보를 응답받는다.  
    
- [x] Feature: 지하철 노선 수정하기  
    Scenario: 지하철 노선 수정   
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 생성한 지하철 노선 수정을 요청한다.  
    Then: 사용자는 지하철 노선 조회 시 수정된 정보를 응답받는다.
    
- [x] Feature: 지하철 노선 삭제하기  
    Scenario: 지하철 노선 삭제  
    Given: 지하철역이 생성되어 있다.  
    When: 사용자는 지하철 노선 삭제를 요청한다.  
    Then: 사용자는 지하철역 목록 조회 시 생성한 역을 찾을 수 없다.  

- [x] Feature: 역 사이에 새로운 지하철 역 추가하기  
    Scenario: 역 사이에 새로운 지하철 역 추가    
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 지하철 구간 추가를 요청한다.  
    Then: 역 사이에 새로운 역이 추가된다.
	
- [x] Feature: 상행 종점으로 새로운 지하철 역 추가하기  
    Scenario: 지하철 노선 하행 구간 추가  
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 하행 지하철 구간 추가를 요청한다.  
    Then: 하행 구간이 추가된다.
	
- [x] Feature: 하행 종점으로 새로운 지하철 역 추가하기   
    Scenario: 지하철 노선 상행 구간 추가  
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 상행 지하철 구간 추가를 요청한다.  
    Then: 상행 구간이 추가된다.
	
- [x] Feature: 기존 구간보다 길거나 같은 구간 추가하기  
    Scenario: 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없다.    
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 기존 노선 구간보다 긴 지하철 구간 추가를 요청한다.  
    Then: 구간 추가에 실패한다.  
	
- [x] Feature: 기존 노선에 등록된 상행역, 하행역 구간 추가하기  
    Scenario: 상행 역과 하행 역이 이미 노선에 모두 등록되어 있다면 추가할 수 없다.  
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 기존에 등록된 지하철 구간 추가를 요청한다.  
    Then: 구간 추가에 실패한다.  
	
- [x] Feature: 기존 노선에 없는 상행역, 하행역 구간 추가하기  
    Scenario: 상행 역과 하행 역 둘 중 하나도 포함되어 있지 않으면 추가할 수 없다.  
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 기존 노선에 없는 지하철 구간 추가를 요청한다.  
    Then: 구간 추가에 실패한다.  
    
- [x] Feature: 상행 종점 제거하기  
    Scenario: 상행 종점을 제거하면 다음 역이 상행 종점이 된다.   
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 지하철 노선의 상행 종점 제거를 요청한다.    
    Then: 상행 종점역이 삭제된다.   
	
- [x] Feature: 하행 종점 제거하기  
    Scenario: 하행 종점을 제거하면 이전 역이 하행 종점이 된다.   
    Given: 지하철노선이 생성되어 있다.  
    When: 사용자는 지하철 하행 종점 제거를 요청한다.  
    Then: 하행 종점역이 삭제된다.  
	
- [x] Feature: 중간역 제거하기  
    Scenario: 중간역을 제거하면 역이 재배치된다.  
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 지하철 노선의 중간역 제거를 요청한다.  
    Then: 중간역이 삭제된다.  
	
- [x] Feature: 기존 노선에 없는 역 제거하기  
    Scenario: 삭제하고자 하는 지하철 역이 기존 노선에 포함되어 있지 않으면 제거할 수 없다.  
    Given: 지하철 노선이 생성되어 있다.  
    When: 사용자는 기존 노선에 없는 지하철 역 제거를 요청한다.  
    Then: 구간 삭제에 실패한다.  
	
- [x] Feature: 구간이 하나인 노선의 지하철 역 제거하기  
    Scenario: 노선의 구간이 하나인 경우, 지하철 역을 제거할 수 없다.  
    Given: 구간이 1개인 지하철 노선이 생성되어 있다.  
    When: 사용자는 지하철 역 제거를 요청한다.  
    Then: 구간 삭제에 실패한다. 