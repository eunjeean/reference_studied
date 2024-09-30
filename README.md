# reference_studied
공부하여 개발한 참고자료
 - build > MainActivity > 기능별 버튼으로 구성

##Button 순서대로 정리
###01) Shell Execute
 - 터미널에서 작성하는 adb 기능을 코드로 구현
 - adb shell 이후 문장으로 요청하는 명령어를 입력하여 실행
 - 명령 수행시 App의 권한은 항상 User이기 때문에 su권한을 필요로 하는 명령은 수행할 수 없다는 점 유의 > manifest에 권한 추가하여 사용 가능한 부분도 있음
 - 환경에 따라 실행이 안될 수도 있음 >> 대부분 SecurityException: Permission Denial 생기는데 환경에 따라 적용할 수 있을듯

//        String cmd = "sync"; // 동기화
//        String cmd = "devices"; // 연결된 Device 목록 확인

//        String cmd = "pm clear --user 0 com.android.sample"; // 데이터 지우기 Clear App data : adb shell pm clear --user [user id] [package name]
//        String cmd = "pm clear --user 0 --cache-only com.android.sample"; // 캐시 지우기 Clear cache data : adb shell pm clear --user [user id] --cache-only [package name]

//        앱 사용/사용중지 기능 동작시 권한문제가 발생할 수 있음 > 그러면 동작하지 않음
//        String cmd = "pm disable-user --user 0 --cache-only com.android.sample"; // 앱 비활성화 시키기 : adb shell pm disable-user --user 0 [package name]
//        String cmd = "pm enable --user 0 com.android.sample"; // 앱 다시 활성화 시키기 : adb shell pm enable --user 0 [package name]

//        설치 : adb install –r –d - t APK명(경로)
//        삭제 : adb shell pm uninstall 패키지명
//        String cmd = "pm uninstall --user 0 com.android.sample"; // 앱 삭제 시키기 : adb shell pm uninstall --user 0 [package name]

***

###02)

###03)

###04)

###05)

###06)
