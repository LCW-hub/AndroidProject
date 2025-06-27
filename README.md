# GPS 산책로 앱 (GuideFriend)

## 📱 프로젝트 개요
GPS 기반 산책로 탐색 및 정보 제공 Android 애플리케이션

## 🏗️ 프로젝트 구조
```
GPS/
├── app/                    # Android 앱 소스코드
│   ├── src/main/
│   │   ├── java/com/example/gps/
│   │   │   ├── api/       # API 클라이언트
│   │   │   ├── model/     # 데이터 모델
│   │   │   └── *.java     # 액티비티 및 프래그먼트
│   │   ├── res/           # 리소스 파일
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── server/                 # Node.js 백엔드 서버
│   ├── routes/            # API 라우트
│   ├── server.js          # 메인 서버 파일
│   └── package.json
└── README.md
```

## ✨ 주요 기능

### 🗺️ 지도 및 경로
- 네이버 지도 API 연동
- 실시간 GPS 위치 추적
- 산책로 경로 표시
- 위험 구역 및 편의시설 마커

### 📊 코스 정보
- 경로별 상세 정보 (소요시간, 걸음수, 거리)
- 애완동물 동반 가능 여부
- 소모 칼로리 및 수분 섭취량 계산
- 유동인구 정보

### 🚨 안전 기능
- SOS 긴급 상황 알림
- 주변 사용자에게 도움 요청
- 실시간 응답 시스템

### 🌤️ 날씨 및 추천
- OpenWeatherMap API 연동
- 실시간 날씨 정보
- 계절별 코스 추천
- 날씨 기반 경로 추천

### 🚌 교통 정보
- 대중교통 정보 제공
- 주변 정류장 및 지하철역
- 실시간 도착 정보

### 👥 커뮤니티
- 사용자 간 소통
- 포토스팟 추천
- 경로 리뷰 및 평가

## 🛠️ 기술 스택

### Android 앱
- **언어**: Java
- **지도**: 네이버 지도 SDK, T-Map API
- **네트워킹**: Retrofit2, OkHttp
- **이미지**: Glide
- **위치**: Google Play Services Location

### 백엔드 서버
- **언어**: Node.js
- **프레임워크**: Express.js
- **데이터베이스**: Firebase Firestore (옵션)
- **인증**: Firebase Auth (옵션)

### 외부 API
- **날씨**: OpenWeatherMap API
- **지도**: 네이버 지도 API, T-Map API

## 🚀 설치 및 실행

### Android 앱 실행
1. Android Studio 설치
2. 프로젝트 클론
```bash
git clone https://github.com/LCW-hub/GuideFreind.git
```
3. API 키 설정
   - `app/src/main/res/values/api_keys.xml` 파일 생성
   - 네이버 지도, T-Map, OpenWeatherMap API 키 입력
4. 앱 빌드 및 실행

### 서버 실행 (옵션)
1. Node.js 설치
2. 서버 디렉토리로 이동
```bash
cd server
npm install
```
3. 환경 변수 설정
```bash
cp env.example .env
# .env 파일에서 API 키 설정
```
4. 서버 실행
```bash
npm start
```

## 📋 API 키 설정

### 필수 API 키
1. **네이버 지도 API**
   - [네이버 클라우드 플랫폼](https://www.ncloud.com/)에서 발급
   - `app/src/main/res/values/api_keys.xml`에 설정

2. **T-Map API**
   - [T-Map 개발자 센터](https://developers.sktelecom.com/)에서 발급
   - `app/src/main/res/values/api_keys.xml`에 설정

3. **OpenWeatherMap API**
   - [OpenWeatherMap](https://openweathermap.org/api)에서 발급
   - `app/src/main/res/values/api_keys.xml`에 설정

### API 키 설정 예시
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="naver_map_client_id">YOUR_NAVER_CLIENT_ID</string>
    <string name="naver_map_client_secret">YOUR_NAVER_CLIENT_SECRET</string>
    <string name="tmap_api_key">YOUR_TMAP_API_KEY</string>
    <string name="openweather_api_key">YOUR_OPENWEATHER_API_KEY</string>
</resources>
```

## 👥 팀 정보
- **팀명**: 102 (일공이, ilgonge)
- **팀장**: 이채운
- **팀원**: 박희재, 옥진서, 이강호

## 📄 라이선스
이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 🤝 기여하기
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 문의
프로젝트 관련 문의사항이 있으시면 이슈를 생성해주세요. 