# CRUD API
### 첫 Spring boot 공부용.. 이것저것 막 해본 기록
### 회원가입, 로그인
### 게시글 작성, 수정, 삭제, 페이징, 검색, 카테고리 별 분류
### 게시글 좋아요, 게시글 조회수 기능
### 댓글 작성, 삭제, 댓글 페이징, 대댓글
### 비로그인 사용자와 로그인 사용자 분리(각각 ip와 userId 세션)

---
---


| **기능**                 | **설명**                                                 | **관련 기술/패턴**                      |
|------------------------|--------------------------------------------------------|-----------------------------------|
| **도메인 분리 및 통합**        | Board와 Comment를 각각 분리하여 관리하다 Aggregate Root 개념을 도입해 통합 시도 | Domain-Driven Design (DDD)        |
| **익명/로그인 사용자 처리**      | 비로그인 사용자는 닉네임과 패스워드 검증, 로그인 사용자는 세션 정보로 사용자 검증         | HttpSession, Validation           |
| **Template Method 패턴** | Abstract 클래스를 사용해 중복 코드 제거, Board와 Anonymous Service에서 재사용 | Template Method Pattern           |
| **QueryDSL 적용**        | findCommentsByBoard 메서드를 QueryDSL로 작성해 댓글 페이지네이션 구현    | QueryDSL                          |
| **Rate Limiting**      | Redis를 이용한 사용자 요청 제한 (IP 기반 또는 세션 기반)                  | Spring AOP, Redis, Rate Limiting  |
| **예외 처리 및 Validation** | @NotNull, @Size 등의 어노테이션으로 필수값 검증 및 Custom Exception 활용 | Bean Validation, Custom Exception |
| **Controller 분리**      | Board, Comment, Login, Signup 등 기능별로 컨트롤러를 분리          | MVC 패턴                            |
| **트랜잭션 관리**            | @Transactional을 통해 데이터의 일관성과 무결성 보장                    | Spring Transactional              |
| **캐시 데이터 관리**          | Redis Insight GUI를 통한 캐시 데이터 관리                        | Redis insight                     |
| **비동기 작업 batch 처리**    | 요청이 있을 때 마다 queue에 담고 batch 처리를 통해 일정시간마다 한번에 반영       | Redis Queue, Spring scheduler     |

---
---
### 학습 계획:
메모리 사용 최적화에 대한 고민

redis의 추가 기능 탐색

kafka와 websocket을 혼용한 실시간 채팅 기능

aws 강의 듣고 api 배포
