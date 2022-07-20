깃허브 생성 축하!!


# oic-android
+ [I. 스플래시 스크린 생성](#I.-스플래시-스크린)
+ [II. 로그인](#II.-로그인)

## I. 스플래시 스크린
https://developer.android.com/guide/topics/ui/splash-screen/migrate

i. build.gradle(app)
```kotlin
implementation 'androidx.core:core-splashscreen:1.0.0-beta02'
```

ii. themes.xml
```xml
<style name="Theme.App.Starting" parent="Theme.SplashScreen">
   <item name="windowSplashScreenBackground">@color/...</item>
   <item name="windowSplashScreenAnimatedIcon">@drawable/...</item>
   <item name="windowSplashScreenAnimationDuration">200</item>
    <!-- 테마 추가 (필수) -->
   <item name="postSplashScreenTheme">@style/Theme.App</item>
</style>
```

iii. AndroidManifest.xml
```kotlin
<application android:theme="@style/Theme.App.Starting">
  <!-- or -->
    <activity android:theme="@style/Theme.App.Starting">
```

vi. MainActivity.kt
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
       // Handle the splash screen transition.
       val splashScreen = installSplashScreen()

       super.onCreate(savedInstanceState)
    ...
```


## II. 로그인
https://latte-is-horse.tistory.com/283