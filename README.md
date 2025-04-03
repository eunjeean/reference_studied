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

>### 06) Log Filter
 - 현재 출력된 전체로그를 조회 > 특정키워드 필터링 후 저장

 - Manifest 권한 설정
   [로그 데이터를 읽기 위한 권한] 권한 주지 않으면 현재 package에서 출력된 내용만 저장됨<br>
   `<uses-permission android:name="android.permission.READ_LOGS"/>`
 
   [외부 저장소 접근 권한]<br>
   `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />`<br>
   `<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />`<br>

>#### adb 호출방법(경로에 따라 권한이 필요함) -----------------------------------------------------<br>
 [실시간 로그 저장 > 계속 덮어쓰면서 백업됨(과부하의 원인)]<br>
 `adb logcat > /(파일경로)/testLog.txt` 또는 `adb logcat -f /(파일경로)/testLog.txt`

 [현재 출력된 로그만 저장]<br>
 `adb logcat -d > \(파일경로)\testLog.txt` 또는 `adb logcat -d -f \(파일경로)\testLog.txt`

 [현재 출력된 로그의 특정 키워드 필터링하여 저장]<br>
 `adb logcat -d | grep "keyword" > \(파일경로)\testLog.txt`<br>
 `adb logcat -d | find "keyword" > \(파일경로)\testLog.txt`<br>
 `adb logcat -d | findstr "keyword" > \(파일경로)\testLog.txt`<br>
 또는<br>
 `adb logcat -d | grep "keyword" -f \(파일경로)\testLog.txt`<br>
 `adb logcat -d | find "keyword" -f \(파일경로)\testLog.txt`<br>
 `adb logcat -d | findstr "keyword" -f \(파일경로)\testLog.txt`

 기타 >><br>
 [버퍼에 저장된 모든 로그를 지우는 명령어(이전 로그를 삭제해 새 로그만 기록되도록 설정)]<br>
 `adb logcat -c > /(파일경로)/testLog.txt`
   
***

>### 07) NetworkStateMonitor
 - 네트워크 상태 모니터링
 - ConnectivityManager.CONNECTIVITY_ACTION은 API 28(Android 9.0)부터 deprecated 되었으므로 NetworkCallback 사용을 고려하는 것이 좋음 + OS 버전에 따라 처리되도록 2가지 방법 모두 반영
 - 이전에는 네트워크 상태 변경을 감지하기 위해 브로드캐스트 리시버를 사용하여 이 액션을 수신했지만, API 26(Android 8.0)부터는 암시적 브로드캐스트에 대한 정적 리시버 등록이 제한
 - 최신 버전에서는 registerNetworkCallback 또는 registerDefaultNetworkCallback과 같은 메서드를 사용하여 네트워크 상태 변경을 비동기적으로 처리하는 것이 권장
 - registerNetworkCallback() : 특정 네트워크 요청에 대한 변경 사항을 감지할 때 사용, Wi-Fi나 특정 VPN 연결 같은 특정 네트워크 유형에 대한 변경을 감지 가능
 -  현재사용코드 >> registerDefaultNetworkCallback() : 기기의 기본 네트워크(현재 활성화된 네트워크) 상태가 바뀔 때 호출됨, 특정 네트워크 유형을 지정하지 않고, 모든 네트워크 변경을 감지

***

>### 08) AutoLogin

####방법1) AccountManager 사용하여 계정 등록
- Android에서 계정을 관리하는 시스템 서비스로, 사용자의 로그인 정보를 중앙에서 관리
- 안드로이드 내에 계정을 등록함(설정 - 계정 및 프로필 - 해당앱에 대한 계정 등록확인됨)
- AccountManager.addAccountExplicitly()를 사용하여 계정을 저장

▶장단점
✅ 중앙 관리 : 계정 정보를 OS 차원에서 관리하여 보안성이 높고, 여러 앱에서 공유 가능
✅ 자동 동기화 지원 : Google 계정과 같은 방식으로 동기화 기능 제공
✅ 계정 복구 가능 : 사용자가 기기를 변경해도 계정을 쉽게 복구할 수 있음
❌ 제한된 사용성 : 앱에서만 사용하는 간단한 로그인 유지에는 다소 과함
❌ 비밀번호 저장 비권장 : Android 11(API 30)부터 보안상의 이유로 getPassword()가 deprecated됨

▶어디까지 로그인 정보가 유지될까?
- 유지불가 이유 : 앱삭제로 인하여 정보를 찾을 수 없음
1) 데이터 지우기 : 계정 유지
2) 업데이트 : 계정 유지
3) 앱삭제 후 설치 : 계정 유지 불가


####방법2) 암호화를 포함한 EncryptedSharedPreferences를 사용하여 저장 후 계정 사용
- SharedPreferences의 보안 강화 버전으로, 저장된 데이터를 암호화하여 보호하는 API
- MasterKey를 사용하여 데이터 암호화 후 저장하며, 내부적으로 AES-GCM을 사용
- 일반적인 SharedPreferences를 사용하면 계정정보가 노출되어 보안에 취약함에 따라 EncryptedSharedPreferences를 사용하여 pref를 암호화하는 방안으로 구현

▶장단점
✅ 간편한 구현 : 기존 SharedPreferences와 유사한 사용 방식
✅ 강력한 보안 : 자동 암호화를 제공하여 보안성이 뛰어남
✅ Backward Compatibility : API 23(Android 6.0) 이상에서 지원
❌ 속도 저하 : 일반 SharedPreferences보다 암호화 과정이 추가되어 속도가 느릴 수 있음
❌ 보안 강도가 키 저장 방식에 따라 다름 : MasterKey가 유출되면 데이터도 복호화될 가능성이 있음

▶어디까지 로그인 정보가 유지될까?
- 유지불가 이유 : 앱내에 저장된 pref가 삭제되었기 때문
1) 데이터 지우기 : 계정 유지 불가
2) 업데이트 : 계정 유지
3) 앱삭제 후 설치 : 계정 유지 불가

####방법3) KeyStore 사용하여 계정 조회
- Android KeyStore를 사용하여 민감한 데이터를 안전하게 저장하는 방식
- 키를 안전한 하드웨어(예: TEE, StrongBox)에 저장하여 직접 접근이 불가능하도록 보호
- 토큰 저장시 추천하나, 구현내용은 2번방법을 활용하여 EncryptedSharedPreferences 정보를 KeyStore로 저장함 > 보안을 높임

▶장단점
✅ 최고 수준의 보안 : OS 차원에서 키를 보호하며, Hardware-backed KeyStore를 지원하는 기기에서는 더 안전
✅ 비밀번호 직접 저장 X : 암호화 키만 저장하여, 토큰과 같은 민감한 데이터의 보안성이 높음
✅ 자동 삭제 기능 : 특정 상황(예: 기기 초기화)에서 자동 삭제됨
❌ 구현이 복잡함 : 다른 방법들보다 설정이 까다로움
❌ 백업 불가 : 키를 클라우드에 저장할 수 없어 기기 변경 시 계정 정보를 복구하기 어려움

▶어디까지 로그인 정보가 유지될까?
- 유지불가 이유 : 앱내에 저장된 pref가 삭제되었기 때문
1) 데이터 지우기 : 계정 유지 불가
2) 업데이트 : 계정 유지
3) 앱삭제 후 설치 : 계정 유지 불가


####구현하지 않았지만 이외 방법)
1) 서버 구축 후 저장하여 사용
- 서버에 리프레시 토큰 저장하여 앱 삭제 후에도 로그인 정보 복구

2) firebase, google 로그인 사용
- 안드로이드 백업 서비스로사용자가 앱을 삭제하고 재설치해도, 안드로이드가 자동으로 앱 데이터를 백업하고 복원할 수 있도록 설정
- 단, 이 방법은 기기나 Google 계정에 종속적이며 사용자 허용 필요

***

>### 00) 기타 추가중...

***
