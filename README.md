# VitaPick Backend

VitaPick은 건강기능식품 쇼핑몰과 관리자 페이지를 함께 구현한 팀 프로젝트입니다.  
이 저장소는 VitaPick의 Backend 저장소이며, 저는 관리자 페이지에서 사용하는 Spring Boot API, 권한 검증, 운영 데이터 처리 기능을 담당했습니다.

관리자 기능은 단순 조회 API보다 운영 데이터를 정확하게 조회하고, 권한이 있는 사용자만 접근할 수 있도록 처리하는 흐름이 중요했습니다.  
대시보드에서는 매출, 회원, 주문, 문의 데이터를 집계했고, 회원·상품·주문·리뷰·고객센터 관리 화면에서 필요한 관리자 API를 구현했습니다.

<br />

## 프로젝트 개요

| 항목 | 내용 |
|---|---|
| 프로젝트명 | VitaPick |
| 개발 기간 | 2026.04 - 2026.07 |
| 참여 인원 | 4명 |
| 담당 역할 | 관리자 페이지 풀스택 개발 |
| BE 담당 범위 | 관리자 API 구현, 권한 검증, 운영 데이터 처리, 엑셀 리포트 다운로드 |

<br />

## 기술 스택

| 분류 | 기술 |
|---|---|
| Backend | Java, Spring Boot, Spring Security, Spring Data JPA, REST API |
| Database | MySQL |
| Build | Gradle |
| Deployment | AWS EC2, Docker, Nginx, GitHub Actions |
| Version Control | Git, GitHub Desktop |
| Tool | DBeaver |

<br />

## 담당 기능 요약

- 관리자 주요 화면 6개 영역에 필요한 API 구현
- Spring Security 기반 관리자 권한 검증 보완
- 매출, 회원, 주문, 문의 대시보드 지표 조회
- 운영 데이터 엑셀 리포트 다운로드 구현
- 회원, 상품, 주문, 리뷰, 고객센터 관리 API 구현
- AWS EC2, Docker, Nginx, GitHub Actions 기반 배포 과정 참여

<br />

## 관리자 API 구성

| 영역 | 주요 기능 |
|---|---|
| 관리자 권한 확인 | 관리자 페이지 접근 가능 여부 확인 |
| 대시보드 | 매출, 주문, 회원, 문의 지표 조회 |
| 회원 관리 | 회원 목록, 상세 정보, 주문 내역 조회 |
| 상품 관리 | 상품 목록, 상세 조회, 상품 정보 수정 |
| 주문 관리 | 결제완료 주문 목록 조회 |
| 리뷰 관리 | 리뷰 목록, 상세 조회, 관리자 답글 등록 및 삭제 |
| 고객센터 관리 | 공지글, FAQ, 1:1 문의 답변 처리 |
| 엑셀 리포트 | 관리자 운영 데이터 엑셀 파일 다운로드 |

<br />

## 핵심 구현 내용

### 1. 관리자 권한 검증

관리자 페이지 접근은 Frontend에 저장된 role 값만으로 판단하지 않도록 수정했습니다.

Frontend의 sessionStorage 값은 사용자가 직접 변경할 수 있기 때문에, 관리자 URL 진입 시 Backend 권한 확인 API를 호출하도록 구성했습니다.  
Spring Security에서 관리자 권한을 확인하고, 권한이 없는 사용자는 관리자 API에 접근하지 못하도록 처리했습니다.

```text
관리자 페이지 진입
→ Backend 관리자 권한 확인 API 호출
→ Spring Security 권한 검증
→ ADMIN 권한이면 관리자 화면 접근 허용
→ 권한 없음 또는 인증 실패 시 접근 차단
```

이 흐름을 통해 관리자 접근 판단 기준을 Frontend 상태값이 아니라 Backend 검증 결과로 옮겼습니다.

<br />

### 2. 관리자 대시보드 데이터 집계

대시보드는 운영자가 서비스 상태를 빠르게 확인할 수 있도록 필요한 데이터를 집계하는 API로 구현했습니다.

결제완료 주문을 기준으로 매출을 계산했고, 월별 회원 수와 주문 수를 조회해 운영 흐름을 볼 수 있도록 구성했습니다.  
상품별 매출 TOP5와 문의 처리 현황도 함께 응답해 Frontend에서 카드, 그래프, 목록 형태로 표시할 수 있게 했습니다.

```text
관리자 대시보드 요청
→ 결제완료 주문 기준 매출 조회
→ 월별 회원·주문 데이터 조회
→ 상품별 매출 TOP5 조회
→ 문의 대기·처리율 데이터 조회
→ 대시보드 응답 DTO로 반환
```

<br />

### 3. 관리자 관리 기능 API

관리자 페이지에서 사용하는 회원, 상품, 주문, 리뷰, 고객센터 API를 구현했습니다.

회원 관리에서는 회원 기본 정보와 주문 내역을 조회할 수 있게 했고, 상품 관리에서는 상품 상세 조회와 수정 흐름을 연결했습니다.  
주문 관리는 결제완료 주문 기준으로 조회했고, 리뷰 관리에서는 관리자 답글 등록과 삭제를 처리했습니다.  
고객센터 관리에서는 공지글, FAQ 항목, 1:1 문의 답변 처리를 API로 제공했습니다.

<br />

### 4. 엑셀 리포트 다운로드

관리자 운영 데이터를 엑셀 파일로 내려받을 수 있는 API를 구현했습니다.

대시보드, 회원, 상품, 주문, 리뷰, 고객센터 데이터를 각각 시트로 분리했고, 관리자가 화면에서 확인하는 운영 데이터를 파일로도 확인할 수 있도록 구성했습니다.  
Frontend에서는 이 API를 호출해 브라우저에서 엑셀 파일을 다운로드합니다.

```text
엑셀 다운로드 요청
→ 관리자 운영 데이터 조회
→ 시트별 데이터 구성
→ Excel 파일 생성
→ 파일 다운로드 응답 반환
```

<br />

### 5. 배포 및 오류 해결

Backend는 AWS EC2 서버에서 Docker 기반으로 배포했습니다.

Spring Boot 애플리케이션은 Docker 컨테이너로 실행했고, Nginx를 통해 Frontend 정적 파일과 Backend API 요청을 연결했습니다.  
GitHub Actions를 사용해 main 브랜치 변경 시 EC2 서버에서 재빌드와 재실행이 이루어지도록 구성했습니다.

배포 과정에서는 Nginx의 `proxy_pass` 설정 오류가 발생했고, 요청 경로와 location 설정을 수정해 문제를 해결했습니다.

<br />

## 주요 API

### 관리자 권한 확인

| Method | URL | 설명 |
|---|---|---|
| GET | `/api/admin/check` | 관리자 권한 확인 |

### 대시보드

| Method | URL | 설명 |
|---|---|---|
| GET | `/api/admin/dashboard/summary` | 대시보드 요약 데이터 조회 |
| GET | `/api/admin/dashboard/excel` | 운영 데이터 엑셀 다운로드 |

### 회원 관리

| Method | URL | 설명 |
|---|---|---|
| GET | `/api/admin/users` | 회원 목록 조회 |

### 상품 관리

| Method | URL | 설명 |
|---|---|---|
| GET | `/api/admin/products` | 상품 목록 조회 |
| GET | `/api/admin/products/{prdId}` | 상품 상세 조회 |
| PATCH | `/api/admin/products/{prdId}` | 상품 정보 수정 |

### 주문 관리

| Method | URL | 설명 |
|---|---|---|
| GET | `/api/admin/orders` | 주문 목록 조회 |

### 리뷰 관리

| Method | URL | 설명 |
|---|---|---|
| GET | `/api/admin/reviews` | 리뷰 목록 조회 |
| GET | `/api/admin/reviews/{reviewId}` | 리뷰 상세 조회 |
| PATCH | `/api/admin/reviews/{reviewId}/reply` | 리뷰 답글 등록 |
| DELETE | `/api/admin/reviews/{reviewId}/reply` | 리뷰 답글 삭제 |

### 고객센터 관리

| Method | URL | 설명 |
|---|---|---|
| GET | `/api/admin/cscenter/notices` | 공지글 목록 조회 |
| POST | `/api/admin/cscenter/notices` | 공지글 등록 |
| PATCH | `/api/admin/cscenter/notices/{ntcId}` | 공지글 수정 |
| DELETE | `/api/admin/cscenter/notices/{ntcId}` | 공지글 삭제 |
| GET | `/api/admin/cscenter/faqs` | FAQ 항목 목록 조회 |
| POST | `/api/admin/cscenter/faqs` | FAQ 항목 등록 |
| PATCH | `/api/admin/cscenter/faqs/{faqId}` | FAQ 항목 수정 |
| DELETE | `/api/admin/cscenter/faqs/{faqId}` | FAQ 항목 삭제 |
| GET | `/api/admin/cscenter/inquiries` | 1:1 문의 목록 조회 |
| GET | `/api/admin/cscenter/inquiries/{inqId}` | 1:1 문의 상세 조회 |
| PATCH | `/api/admin/cscenter/inquiries/{inqId}/answer` | 1:1 문의 답변 처리 |

> 실제 Controller 경로와 다른 항목이 있으면 코드 기준으로 수정해야 합니다.

<br />

## 구현하면서 중점적으로 본 부분

관리자 기능은 화면에 데이터를 보여주는 것보다, 어떤 기준으로 데이터를 조회하고 어떤 권한으로 접근할 수 있는지가 중요했습니다.

매출은 결제완료 주문을 기준으로 집계했고, 대시보드 응답은 Frontend에서 바로 사용하기 좋도록 요약 데이터 형태로 구성했습니다.  
관리자 권한은 Frontend role 값이 아니라 Backend 검증 결과를 기준으로 처리했고, 엑셀 다운로드는 여러 관리 데이터를 시트 단위로 분리해 운영자가 확인하기 쉽게 만들었습니다.

배포 과정에서는 로컬에서 동작하던 코드가 서버 환경에서 바로 동작하지 않는 문제를 겪었습니다.  
Nginx 설정 오류를 수정하면서 Frontend 정적 파일, Backend API 요청, Docker 실행 환경이 어떻게 연결되는지 확인할 수 있었습니다.

<br />

## 관련 링크

| 구분 | URL |
|---|---|
| 배포 주소 | http://15.134.141.44/ |
| Frontend Repository | https://github.com/qkrwnstj1346/vitapick-FE |

> 배포 주소는 EC2 서버 상태에 따라 접속이 제한될 수 있습니다.

<br />

## 로컬 실행

Windows 기준:

```bash
gradlew.bat bootRun
```

macOS / Linux 기준:

```bash
./gradlew bootRun
```

<br />

## 빌드

Windows 기준:

```bash
gradlew.bat build
```

macOS / Linux 기준:

```bash
./gradlew build
```
