# ETCHAPEIDA

## 🫵 주제 선정 이유

🌏 **문화적 가치**

기존 왓챠피디아도 일부 도서 데이터를 다루고 있지만 영상 콘텐츠의 부가 요소 수준에 머물러 있기에, ‘도서’를 핵심 주제이자 메인 카테고리로 확장해 독서 문화의 가치를 적극적으로 반영하는 플랫폼을 만들고자 했다.

🎯 **개인 취향 분석에 유용**

사용자가 클릭한 책 데이터를 저장하고 이를 기반으로 AI가 유사한 책을 추천하는 기능을 제공한다. 이를 통해 개인의 독서 취향을 반영한 맞춤형 추천이 가능하다.

📈 **사회적 수요 증가**

자기계발, 에세이, 웹소설 등 독서 트렌드가 다양화되고 있으며, 특히 MZ세대는 책에 대한 감상과 의견을 공유하는 것을 즐기기 때문에 이를 지원하는 플랫폼의 필요성이 높아지고 있다.

## 📅 프로젝트 기간

- 2025년 9월 9일 ~ 9월 26일 (약 3주)

## 🗄️ 프로젝트 주소

**API 명세 URL**: [https://www.notion.so/API-2440ad788074807888cbc14d4f2d7dd7](https://www.notion.so/ETCHAPEDIA-API-27e0ad78807481a69d13c57cd2133115?pvs=21)

**Server(AWS) URL**: http://43.200.215.21:9091/home

## 👨‍💻 사용된 기술

### 🔙 Backend (서버 및 데이터베이스)

Java 17 / Spring Boot + JPA / Spring Security / Thymeleaf / Oracle 11gR2 / STOMP

### 🎨 Frontend (클라이언트 및 UI/UX)

HTML / CSS / JavaScript / jQuery / Fetch API / WebSocket(실시간 알림)

### 🌐 외부 API

- Kakao Login API (로그인), Kakao Pay API (결제), SOLAPI API (인증코드 발송)
- Naver Book API (도서 정보), 정보나루 API (인기 도서 정보)
- ChatGPT API, ETRI 기술체험 플랫폼 API (위키백과 QA API)

### 🧰 개발 및 협업 도구

STS / Git / GitHub / SQL Developer

## 🎢 성장점

### 🦄 **서수련**

> 프로젝트를 진행하며 기술적 역량과 문제 해결 능력을 성장시킬 수 있었다. ChatGPT API를 활용해 프롬프트를 설계하고 원하는 형태로 데이터를 다듬는 과정을 직접 경험하며 Spring Boot와 JPA, 그리고  Spring Security 등 새로운 기술을 다루는 능력을 키웠다. 
특히 이번 프로젝트에서 JPA와 Spring Security, GPT API를 처음 사용했지만 여러 시행착오를 거치며 점차 익숙해졌고, 이제는 이를 활용해 필요한 기능을 직접 구현할 수 있을 만큼 다룰 수 있게 되었다.
> 

### 🍀 이경미

> 1차 프로젝트에서 사용하지 않았던 다양한 외부 API와 AI 기능을 새롭게 활용해보았습니다.
> Spring Boot/JPA 기반으로 프로젝트를 진행하면서 짧은 기간 내에 새로운 기술을 익히고 적용해야 했기 때문에 어려움도 있었지만, 반복적인 학습과 직접 구현 과정을 통해 프로젝트를 완성할 수 있었습니다.
> AI 추천 도서 API, Naver Book API, Kakao Pay, Kakao Login, WebSocket 등 여러 기능을 연동하면서 개발 역량을 한 단계 성장시켰습니다.
> Git 연동에 익숙해졌고, 이 과정에서 문제 해결 능력과 협업 역량을 동시에 키울 수 있었습니다.
> 짧은 기간 동안 진행된 프로젝트라 시작할 때는 부담도 있었지만, 성공적으로 마무리하면서 큰 성취감을 느꼈고, 무엇보다 개발 과정 자체가 즐겁고 의미 있는 경험이 되었습니다.

## 📑 주요기능

### 👤 회원 관리

- 일반 회원가입 / 로그인 (SOLAPI API)
- 소셜 로그인 (Kakao Login)

### 📚 도서 서비스 (Naver Book API, 정보나루 API)

- 도서 검색 (위키백과 QA API)
- 도서 상세 화면
- 도서 “관심없어요” 기능
- 도서 코멘트 / 댓글 작성

### 🛒 쇼핑 서비스

- 도서 장바구니
- 도서 구입 (Kakao Pay API)

### 🏠 사용자 서비스

- 마이페이지
- 도서 AI 추천 (ChatGPT API)
- 인기 급상승 도서 리스트 (정보나루 API)

## 👩‍💻 **구현 기능 (담당: 서수련)**

1. 도서 AI 추천 `(ChatGPT API)`
    - 클릭한 책 기반 chatGPT 추천 기능
2. 로그인 / 소셜 로그인 `(Kakao Login, SOLAPI API)`
3. 홈화면 `(Naver Book API, 정보나루 API, ETRI 위키백과 QA API)`
4. 인기 급상승 도서 리스트 `(정보나루 API)`
5. 도서 댓글 기능
6. 마이페이지

## 👩‍💻 **주요 구현 기능 (담당: 이경미)**

1. 회원가입 (이메일 중복 체크)
2. 도서 상세 화면 (Naver Book API, 정보나루 API)
    - 사용자가 특정 도서를 추가/삭제 할 수 있는 기능(관심없어요)
    - 마이페이지에서 관심없어요 도서 목록 관리 기능
3. 도서 코멘트 기능 (WebSocket, Ajax)
4. 도서 장바구니 및 결제 (Kakao Pay API)
5. 도서 검색 기능 (ETRI API)
-ETRI 기술체험 플랫폼 위키백과 QA API 활용
