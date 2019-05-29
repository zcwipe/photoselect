# photoselect
本地相册 图片选择 大图预览
# 如何使用

1 Add it in your root build.gradle at the end of repositories:
allprojects {
    repositories {
        jcenter()
        google()
        maven { url 'https://www.jitpack.io' }
    }
}

2 Add the dependency in your model build.gradle
 implementation 'com.github.zcwipe:photoselect:1.0'
 
3 startActivity(activity.this,PhotoSelectorActivity.class);
 
