# [UNOFFICIAL] JW Player Android SDK (version 3+) - ANDROIDX dependencies excluded from Exoplayer

This application contains an example implementation of the JW Player SDK for Android.

#### Usage instructions:

-	Clone the repository into your Android Studio workspace, `git clone git@github.com:jwplayer/jwplayer-sdk-android-demo.git`
-	git checkout v3_ads
- Open the `AndroidManifest.xml` file and replace `{YOUR_LICENSE_KEY}` with your license key

The demo application should now build and run. 

For more information on how to use our SDK refer to our developer guide:
[JW Android Support Documentation](https://developer.jwplayer.com/sdk/android/docs/developer-guide/)

#### build.gradle(app):
```
apply plugin: 'com.android.application'
android {
    compileSdkVersion 29
    buildToolsVersion = '29.0.3'

    defaultConfig {
        applicationId "com.jwplayer.opensourcedemo"
        minSdkVersion 16
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
    lintOptions {
        abortOnError false
        disable "GoogleAppIndexingWarning"
    }
    compileOptions {
        sourceCompatibility = '1.8'
        targetCompatibility = '1.8'
    }
}
ext {
    jwplayerVersion = '3.11.0'
    supportLibVersion = '28.0.0'
}
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    testImplementation 'junit:junit:4.12'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'

    // Android Classic Support Library
    implementation ("com.android.support:appcompat-v7:${supportLibVersion}"){
        force = true
    }
    implementation ("com.android.support:design:${supportLibVersion}"){
        force = true
    }
    implementation ('com.android.support:support-annotations:28.0.0'){
        force = true
    }
    implementation ('com.android.support:support-media-compat:28.0.0'){
        force = true
    }

    // JWPlayer SDK Library
    implementation ("com.longtailvideo.jwplayer:jwplayer-core:${jwplayerVersion}"){
        exclude group: 'androidx.annotation', module:'annotation'
        exclude group: 'androidx.media', module:'media'
    }

    implementation "com.longtailvideo.jwplayer:jwplayer-common:${jwplayerVersion}"
    implementation "com.longtailvideo.jwplayer:jwplayer-ima:${jwplayerVersion}"
    implementation "com.longtailvideo.jwplayer:jwplayer-chromecast:${jwplayerVersion}"
    implementation "com.longtailvideo.jwplayer:jwplayer-freewheel:${jwplayerVersion}"
}
```
