
Super duper easy way to release your Android and other artifacts to bintray.


## 介绍

此项目基于[Github Novoda](https://github.com/novoda/bintray-release)修改而来。
改进点：
在publish内新增archives参数，可上传指定的jar包或者aar包。
这样我们便可以很容易上传自己生成的jar，比如混淆过的。

## 新增依赖

*1.根目录下的build.gradle中加入上传开源库的依赖：*
classpath 'com.aaron.gradle:bintray-release:1.0.0'
*2.library的moudel中加入 apply*
apply plugin: 'com.aaron.gradle.bintray-release'


## 简单使用

利用 `publish` 闭包完成相应设置:

```groovy
publish {
    userOrg = 'novoda'
    groupId = 'com.novoda'
    artifactId = 'bintray-release'
    publishVersion = '0.3.4'
    archives file('build/outputs/test.jar') //指定将要上传的jar包，如果不写则默认上传系统生成的jar和aar
    desc = 'Oh hi, this is a nice description for a project, right?'
    website = 'https://github.com/snailflying/bintray-release'
}
```

最后，利用 task `bintrayUpload` 完成上传 (确保已提前在指定位置生成了相应jar包!):

```bash
$ ./gradlew bintrayUpload -PbintrayUser=BINTRAY_USERNAME -PbintrayKey=BINTRAY_KEY -PdryRun=false
```
## 常见错误排除
1.如果你开源库中有中文注释在moudel的build.gradle加入格式
allprojects {
    repositories {
        jcenter()
    }
    //加上这些
    tasks.withType(Javadoc) {
        options{ encoding "UTF-8"
            charSet 'UTF-8'
            links "http://docs.oracle.com/javase/7/docs/api"
        }
    }
}
根目录下的build.gradle中增加
tasks.getByPath(":library模块:releaseAndroidJavadocs").enabled = false

2.忽略错误信息：moudel的build.gradle
```groovy
android {
 lintOptions {
        abortOnError false
    }
}
```

