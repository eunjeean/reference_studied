# reference_studied
공부하여 개발한 참고자료
 - build > MainActivity > 기능별 버튼으로 구성

## Button 순서대로 정리
>### 01) Shell Execute
 - 터미널에서 작성하는 adb 기능을 코드로 구현
 - adb shell 이후 문장으로 요청하는 명령어를 입력하여 실행
 - 명령 수행시 App의 권한은 항상 User이기 때문에 su권한을 필요로 하는 명령은 수행할 수 없다는 점 유의 > manifest에 권한 추가하여 사용 가능한 부분도 있음
 - 환경에 따라 실행이 안될 수도 있음 >> 대부분 SecurityException: Permission Denial 생기는데 환경에 따라 적용할 수 있을듯

`동기화 : adb shell sync`<br>
`연결된 Device 목록 확인 : adb shell devices`

`데이터 지우기 Clear App data : adb shell pm clear --user [user id] [package name]` <br>
`캐시 지우기 Clear cache data : adb shell pm clear --user [user id] --cache-only [package name]`

`앱 비활성화 시키기 : adb shell pm disable-user --user 0 [package name]` <br>
`앱 다시 활성화 시키기 : adb shell pm enable --user 0 [package name]` <br>

`설치 : adb install –r –d - t APK명(경로)` <br>
`삭제 : adb shell pm uninstall [package name]` <br>
`앱 삭제 시키기 : adb shell pm uninstall --user 0 [package name]`

***

>### 02) Memory Size
 - 내외부 저장소의 메모리 용량 확인(전체 용량 / 사용 가능한 용량)
 - 단일 apk 또는 apk List의 용량 조회
 - 여유공간 확인 : fileSizes 대비 현재 저장소에 남은 용량
 - 용량 확인시 기본단위가 bytes로 되어있는데 이를 다른 단위로 변환하는 코드

***

>### 03) Digest Calculator
 - sha256 해시 암호화하기
 - 해시 암호화 한 뒤 Base64 인코딩 반환
 - 임의의 password와 salt 값을 지정하여 사용

***

>### 04) Digest Calculator N
 - N회차 반복 sha256 해시 암호화하기
 - 해시 암호화 한 뒤 Base64 인코딩 반환
 - 임의의 password와 salt 값을 지정하여 사용
 - 임의의 반복회차를 지정

***

>### 05) Date Format
 - Calendar date를 지정된 형식으로 포맷
 - 포맷 형식에 따라 출력되는 내용이 달라짐

`yyyy/MM/dd HH:mm:ss z = 2024/01/01 12:34:56 GMT+09:00` <br>
`yyyy/MM/dd HH:mm:ss Z = 2024/01/01 12:34:56 +0900` <br>
`yyyy/MM/dd HH:mm:ss 'Asia/Seoul' = 2024/01/01 12:34:56 Asia/Seoul` <br>
`yyyy/MM/dd HH:mm:ss 'KST' = 2024/01/01 12:34:56 KST`

***

>### 06) 기타 추가중...

***
