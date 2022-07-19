깃허브 생성 축하!!


# OIC
+ [스플래시 스크린 생성](#스플래시-스크린)


## 스플래시 스크린
https://developer.android.com/guide/topics/ui/splash-screen/migrate

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