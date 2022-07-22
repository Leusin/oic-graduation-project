깃허브 생성 축하!!


# oic-android
+ [스플래시 스크린 생성](#스플래시-스크린)
+ [로그인](#로그인)

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

1. theme.xml 에서 기존 액션 바 제거 
```xml
<style name="AppTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar">
...
</style>
```

2. toolbar 를 레이아웃에 추가

## 로그인
https://latte-is-horse.tistory.com/283

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
    //Multidex
    implementation 'androidx.multidex:multidex:2.0.1'
}
```
MultiDex 는 앱을 빌드 할 때 "Cannot fit requested classes in a single dex file" 오류를 막기위함.

```kotlin
FirebaseAuthApplication.auth.signInWithEmailAndPassword(username.text.toString(), password.text.toString()).addOnCompleteListener(this) { task ->
                username.text.clear(); password.text.clear()
                if (task.isSuccessful) {
                    if (FirebaseAuthApplication.checkAuth()) { FirebaseAuthApplication.email = username.text.toString(); ChangeVisibility("login")}
                    else { Toast.makeText(baseContext, "전송된 메일로 이메일 인증이 되지 않았습니다.", Toast.LENGTH_SHORT).show() }
                }
                else {
                    Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
```