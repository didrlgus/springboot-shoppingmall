# :shopping_cart: 스프링 부트로 만드는 쇼핑몰 프로젝트

## :clipboard: 개발환경
* IntelliJ
* Postman
* HeidiSql
* Sourcetree
* GitHub

## :clipboard: 사용 기술
### 백엔드
#### Spring boot
* JAVA 8
* Spring MVC
* Spring Boot Security
* Spring Boot Actuator
* Spring Data JPA
* Spring Boot Batch
* Spring AOP
* spring cloud stream

#### Build tool
* Gradle

#### Database
* Mysql
* Redis

#### AWS
* EC2
* S3

#### Message Queue
* Kafka

#### CI
* Travis CI

### 프론트엔드
* Javascript
* Thymeleaf
* jQuery

## :clipboard: 주요 키워드
* REST API
* 시큐리티
* 배치
* 스케줄링
* HTTP 통신
* JPA
* 페이징
* 트랜잭션
* 예외처리
* Git 버전관리
* AWS EC2 배포
* Message Queue
* Event Driven Architecture

## :clipboard: 성능 테스팅 도구
* K6
* Grafana
* InfluxDB

## :clipboard: 로그 분석 도구
* ELK Stack
  * Elasticsearch, Logstash, Kibana, Filebeats

## :factory: 시스템 구조
<img src="https://user-images.githubusercontent.com/40568894/136735965-4b807c84-02b4-4410-8bb1-771840a04102.jpeg" width="80%" height="80%">

## :link: API 서버 (Producer)
* [API 서버](https://github.com/didrlgus/springboot-shoppingmall/tree/master/app/api-server) 

## :link: order 서버 (Consumer)
* [order 서버](https://github.com/didrlgus/springboot-shoppingmall/tree/master/app/order-server)

## :link: product-purchase-count 서버 (Consumer)
* [product-purchase-count 서버](https://github.com/didrlgus/springboot-shoppingmall/tree/master/app/product-purchase-count-server) 

## :link: mail 서버 (Consumer)
* [mail 서버](https://github.com/didrlgus/springboot-shoppingmall/tree/master/app/mail-server)

## :link: product-purchase-count-batch 서버
* [product-purchase-count-batch 서버](https://github.com/didrlgus/springboot-shoppingmall/tree/master/app/product-purchase-count-batch)

## :link: redis-update-batch 서버
* [redis-update-batch 서버](https://github.com/didrlgus/springboot-shoppingmall/tree/master/app/batch-server)

## :link: 공통 모듈
* app 공통 모듈
  * [app 공통 모듈](https://github.com/didrlgus/springboot-shoppingmall/tree/master/app/common)

* lib 공통 모듈
  * [redis 공통 모듈](https://github.com/didrlgus/springboot-shoppingmall/tree/master/lib/redis)
  * [kafka 공통 모듈](https://github.com/didrlgus/springboot-shoppingmall/tree/master/lib/kafka)

## :link: redis update 권한 서버
* [redis update 권한 서버 Repository](https://github.com/didrlgus/redis-update-server)

## :link: Trouble Shooting
* [trouble shooting 기록](https://github.com/didrlgus/springboot-shoppingmall/issues?q=is%3Aissue+is%3Aopen+label%3Atroubleshooting)

## :link: 성능 테스트
* [초기 성능 테스트 결과](https://github.com/didrlgus/springboot-shoppingmall/issues/5)
* [메인화면 API 캐시 적용 전, 후 성능 테스트 비교 결과](https://github.com/didrlgus/springboot-shoppingmall/issues/21)
* [서버를 2대 돌리면 얼마나 더 많은 트래픽을 감당할 수 있을까?](https://github.com/didrlgus/springboot-shoppingmall/issues/46)

## :link: ERD 설계
* [shopping mall ERD](https://github.com/didrlgus/springboot-shoppingmall/issues/1)

## :link: Rest API 문서
* [shopping mall API 문서](https://github.com/didrlgus/springboot-shoppingmall/issues/58)
