깃허브 생성 축하!!


# oic-android
+ [스플래시 스크린 생성](#스플래시-스크린)
+ [로그인](#로그인)
+ [카메라](#카메라)
+ [검색](#검색)

## 스플래시 스크린
https://developer.android.com/guide/topics/ui/splash-screen/migrate
https://velog.io/@pish11010/Android-Splash-Screen-%EA%B5%AC%ED%98%84

1. build.gradle(app)
```kotlin
implementation 'androidx.core:core-splashscreen:1.0.0-beta02'
```

2. themes.xml
```xml
<style name="Theme.App.Starting" parent="Theme.SplashScreen">
   <item name="windowSplashScreenBackground">@color/...</item>
   <item name="windowSplashScreenAnimatedIcon">@drawable/...</item>
   <item name="windowSplashScreenAnimationDuration">200</item>
    <!-- 테마 추가 (필수) -->
   <item name="postSplashScreenTheme">@style/Theme.App</item>
</style>
```

3. AndroidManifest.xml
```kotlin
<application android:theme="@style/Theme.App.Starting">
  <!-- or -->
    <activity android:theme="@style/Theme.App.Starting">
```

4. MainActivity.kt
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
       // Handle the splash screen transition.
       val splashScreen = installSplashScreen()

       super.onCreate(savedInstanceState)
    ...
```

> ~~HomeActivity 대신 LoginActivity 를 시작화면으로 대체할 시 exception 오류 발생.~~
> ~~Android 12 제공하는 Aip 대신 스플래시 Activity 를 따로 만듬~~


## 커스텀 액션 바
https://developer.android.com/guide/navigation/navigation-ui?hl=ko
[Kotiln 안드로이트 커스텀 액션바toolbar 완벽 가이드](https://show-me-the-money.tistory.com/entry/Kotiln-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BB%A4%EC%8A%A4%ED%85%80-%EC%95%A1%EC%85%98%EB%B0%94toolbar-%EC%99%84%EB%B2%BD-%EA%B0%80%EC%9D%B4%EB%93%9C)

### theme.xml 에서 기존 액션 바 제거 
```xml
<style name="AppTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar">
...
</style>
```


## 로그인
https://latte-is-horse.tistory.com/283
https://bada744.tistory.com/161

`build.gradle(app)`
```kotlin
android {
    ...
    defaultConfig {
        ...
        multiDexEnabled true
    }
    ...
}
dependencies {
    ...
    // Firebase
    implementation 'com.google.firebase:firebase-bom:30.3.0'
    implementation 'com.google.android.gms:play-services-auth:20.2.0'
    implementation 'com.google.firebase:firebase-auth-ktx:21.0.6'
    apply plugin: 'com.google.gms.google-services'
    //Multidex
    implementation 'androidx.multidex:multidex:2.0.1'
}
```
MultiDex 는 앱을 빌드 할 때 "Cannot fit requested classes in a single dex file" 오류를 막기위함. <br>
`apply plugin: 'com.google.gms.google-services'` 가 있어야 파이어베이즈 인증 관련 오류가 없어짐

### 해시키 확인 방법

SHA-1: 우측 상단 Gralde > android > signingReport

### gradle 에 카카오톡 로그인 모듈
`implementation "com.kakao.sdk:v2-user:2.3.0"`~~를 추가하면 구글 로그인 코드에 오류 발생~~
> ~~Unresolved reference: default_web_client_id~~

∴ 카카오톡 로그인은 주요기능 구현 후에 추가

https://chetan-garg36.medium.com/resolve-default-web-client-id-please-a37508e3f88b
```xml
    <!-- string.xml -->
    <string name="default_web_client_id" translatable="false">924806410873-h6p3e9gm2a6neo7kcqgtam73dl88pkdu.apps.googleusercontent.com</string>
```

## 카메라
https://developer.android.com/training/camerax/architecture?hl=ko
https://developer.android.com/codelabs/camerax-getting-started#0

### Gradle
```kotlin
dependencies {
  def camerax_version = "1.1.0-beta01"
  implementation "androidx.camera:camera-core:${camerax_version}"  
  implementation "androidx.camera:camera-camera2:${camerax_version}"  // camera2를 위한 코어 라이브러리
  implementation "androidx.camera:camera-lifecycle:${camerax_version}"  // 생명주기 라이브러리
  implementation "androidx.camera:camera-view:${camerax_version}"  // 뷰 클래스
}
```

### Fragemnt 에서 카메라 기능 구연
https://developer.android.com/codelabs/camerax-getting-started
https://rick38yip.medium.com/camerax-on-android-fragment-in-kotlin-with-imageanalyzer-9cb58f9182a8
https://wikidocs.net/101170

Fragment 의 생명주기
> onCreate > onCreateView > onViewCreate > [ onStart > onPause > onStop ] > onDestroyView > onDestroy

+ onCreateView
  + 뷰바인딩
+ onViewCreated
  + 카메라 권한요청
  + 카메라 버튼 리스너
  + 카메라 동작

### 권한 처리
https://stackoverflow.com/questions/40760625/how-to-check-permission-in-fragment

```xml
    <!-- 카메라 권한 -->
    <uses-feature android:name="android.hardware.camera.any" />
    <uses-permission android:name="android.permission.CAMERA" />
    <!-- 미디어 저장소 접근 권한-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
```

프래그먼트의 경우 권한 요청
```kotlin
requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS) 
```

### 촬영 이미지 뷰에 띄우기
https://itstudy-mary.tistory.com/361
https://github.com/littlemary1379/RandomColorChart/blob/master/app/src/main/java/com/mary/kotlinprojectstudy/camera/CameraActivity.kt

### 촬영 이미지 전송
https://no-dev-nk.tistory.com/81
1. 절대경로 uri 를 가지고 파일을 만든다.
2. (생략가능) 파일 이름을 정해준다.
3. RequestBody를 만들어 준다  mediatype는 이미지로..
4. 멀티파츠 바디를 만들어준다
5. 레트로핏 빌더 생성
6. 인터페이스를 레트로핏에 적용
7. 인터페이스에서 사용할 메소드를 호출하면서 실행

### 하단 네비게이션
https://www.youtube.com/watch?v=Chso6xrJ6aU&ab_channel=Stevdza-San

> ~~문제: 프래그먼트가 전환되지 않음~~ <br>
> `botton_nav_menu.xml` 과 `main_navigaion.xml` id 가 일치하지 않으면 프래그먼트가 연결되지 않음

### 툴바 업데이트
https://developer.android.com/reference/androidx/navigation/NavController#addOnDestinationChangedListener(androidx.navigation.NavController.OnDestinationChangedListener)
https://stackoverflow.com/questions/52511136/how-to-set-title-in-app-bar-with-navigation-architecture-component
https://youngest-programming.tistory.com/333

```kotlin
val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            title = when (destination.id) {
                R.id.navigation_search -> "oic"
                else -> "Default title"
            }

            if (destination.id == navigation_search) { binding.toolbarTitle.text = "단어장"; binding.searchView.visibility = View.VISIBLE; binding.orderSet.visibility = View.GONE }
            else if (destination.id == navigation_note) { binding.toolbarTitle.text = "단어장"; binding.searchView.visibility = View.GONE; binding.orderSet.visibility = View.VISIBLE}
            else if (destination.id == navigation_account) {binding.toolbarTitle.text = "내계정"; binding.searchView.visibility = View.GONE; binding.orderSet.visibility = View.GONE}
        }
```

프래그먼트의 목적지에 따라 툴바 UI 업데이트


## 검색 
### 인터페이스
https://developer.android.com/guide/topics/search/search-dialog?hl=ko
https://salix97.tistory.com/231
https://developer.android.com/guide/navigation/navigation-ui?hl=ko

검색 인터페이스 종류
+ `Search Dialog`
+ `SearchView` 위젯

검색 가능한 구성 XML 파일
+ 검색 대화상자 또는 위젯의 특정 UI 요소를 구성
+ _추천 검색어_ 및 _음성 검색_과 같은 기능의 작동방식 정의
+ 일반적인 이름으로 `searchable.xml`
```kotlin
 <?xml version="1.0" encoding="utf-8"?>
    <searchable xmlns:android="http://schemas.android.com/apk/res/android"
        android:label="@string/app_label"
        android:hint="@string/search_hint" >
    </searchable>
```
검색 가능 Activity
+ 사용자가 검색 대화상자 또는 위젯에서 검색을 실행하면 시스템에서 검색 가능 활동을 시작하고 `ACTION_SEARCH` 작업을 통해 `Intent` 에 포함하여 검색어를 전달
+ manifest의 <activity> 요소 내부에서 다음과 같이 설정
 + ACTION_SEARCH 인텐트를 허용하는 활동을 <intent-filter> 요소에서 선언
 + 사용할 검색 가능한 구성을 <meta-data> 요소에서 지정
```kotlin
<application ... >
<activity android:name=".SearchableActivity" >
<intent-filter>
<action android:name="android.intent.action.SEARCH" />
</intent-filter>
<meta-data android:name="android.app.searchable"
android:resource="@xml/searchable"/>
</activity>
...
</application>
```
### Retrofit
https://stackoverflow.com/questions/71443632/how-can-i-use-free-dictionary-api-to-get-the-top-definition-of-a-word
https://velog.io/@plz_no_anr/Android-REST-API
https://github.com/gokulakrishnan001/Dictionary
https://underdog11.tistory.com/entry/Kotlin-%EB%A0%88%ED%8A%B8%EB%A1%9C%ED%95%8F-%EC%82%AC%EC%9A%A9%EB%B2%95-Interface-retrofitInstance-constant%EB%A7%8C%EB%93%A4%EA%B8%B0-GET%EC%9D%98%EB%AF%B8-JSON-To-Kotlin-%ED%94%8C%EB%9F%AC%EA%B7%B8%EC%9D%B8-%EB%89%B4%EC%8A%A4%EC%95%B1-%EB%A7%8C%EB%93%A4%EA%B8%B0-2%ED%8E%B8

### 리사이클러 뷰
https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-kotlin-unit-2-pathway-3%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-training-recyclerview-scrollable-list#0
https://developer.android.com/codelabs/basic-android-kotlin-training-project-dogglers-app#0

+ 데이터 소스가 되는 클래스 만들기
  + DataSource
  + model
+ 항목의 레이아웃 구성 - `list_item.xml`
+ 어댑터 구현 - `ItemAdapter.kt`
+ `viewHolder` - recycle 뷰에 설정해야하는 뷰 바인딩
+ `onCreateViewHolder()` 에서 레이아웃 확장
+ `getItemCount()`
+ `onBindingViewHolder()` - 각 recycler 뷰에서 데이터 설정

### 리사이클러뷰에 클릭이벤트 추가
https://yunaaaas.tistory.com/57
https://yunaaaas.tistory.com/43

### putExtra 에러 문제 해결법
https://yuuj.tistory.com/211
https://yunaaaas.tistory.com/57?category=966969
>  에러가 나는 이유는 putExtra 의 정해진 형식(String,int 등)과 맞지 않게 우리가 만든 데이터 형태(클래스)를 보내기 때문입니다. 이를 위해서는 Parcelize라는 기술을 사용하면 쉽게 해결할 수 있습니다.

해결: Word.kt
```kotlin
data class Word(
    val img: Int,
    val word_eng: String,
    val word_kor: String
    ) : Serializable // <- 추가
```

## 커스텀 달력
https://github.com/iSoron/uhabits
https://androidexample365.com/a-calendar-to-integrate-calendar-with-custom-design-in-your-jetpack-compose-project/