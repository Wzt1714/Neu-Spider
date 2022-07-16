# Neu-Spider


### 🔥🔥🔥东北大学最新爬虫🔥🔥🔥


> 这是一个东北大学的爬虫项目，使用Java+OkHttp实现，可以用于Android和服务端
>
> 只需要极少的代码就可以进行爬虫操作，爬虫可以在学校教务处和一网通多个网页
>
> 工作，尽可能多的覆盖所有信息


注意：请使用最新版本！请使用最新版本！请使用最新版本！

低版本有未修复的bug，更新日志[点此](https://github.com/Wzt1714/Neu-Aurora-Spider/blob/main/UPDATE.md) 查看

## 准备工作


在开始使用爬虫之前，请加入所需依赖，如果你使用Gradle工具，请在`build.gradle`文件下加入下面内容：

```groovy
dependencies {
   // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation group: 'com.squareup.okhttp3', name: 'okhttp', version: '4.9.0'
    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    implementation group: 'org.jsoup', name: 'jsoup', version: '1.13.1'
    // https://mvnrepository.com/artifact/net.sf.json-lib/json-lib
    implementation 'net.sf.json-lib:json-lib:2.4:jdk15'
    其他依赖.....
}

```

如果你使用Maven工具，请加入如下依赖：

```xml
<!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.9.0</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.jsoup/jsoup -->
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.13.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/net.sf.json-lib/json-lib -->
<dependency>
    <groupId>net.sf.json-lib</groupId>
    <artifactId>json-lib</artifactId>
    <version>2.4</version>
</dependency>
其他依赖.....

```

完成上面步骤后，请[下载](https://github.com/Wzt1714/Neu-Aurora-Spider/tags) 最新jar包或源代码

将下载好的jar包加入项目或者将源代码`src/main/java`路径下的文件加入项目

## 开始


> 网络环境未知/已知指的是是否确定为校园网/非校园网环境

####  网络环境未知的情况

可以直接使用`AutoHandle`来进行数据获取，它会自动判断当前的网络环境来进行请求：

```java
AutoHandle autoHandle = new AutoHandle("你的学号", "你的一网通密码", "学期id", requestData, ms->{
    Thread.sleep(ms)
});
```

请完善你的学号、一网通密码信息。第五个参数为不同平台下的延迟，ms参数单位为毫秒，定义此参数的目的是防止访问过快使得学校网站禁止获取，第四个参数`requestData`为需要获得的数据，可选如下内容：

| 参数         | 代表的意义   | 数据类型    |
| ------------ | ------------ | ----------- |
| STUDENT_DATA | 学生数据     | StudentData |
| COURSE_TABLE | 课程表数据   | CourseData  |
| GPA_DATA     | 绩点数据     | GpaData     |
| EXAM_DATA    | 考试数据     | ExamData    |
| CARD_DATA    | 校园卡数据   | CardData    |
| NET_DATA     | 校园网数据   | NetData     |
| LIBRARY_BOOK | 借阅书籍数据 | BookData    |

请注意，以上的`参数`内容均存于`Value.NeuVpnHandleValue`枚举类中，如果你想使用`STUDENT_DATA`这样的形式而不是`Value.NeuVpnHandleValue.STUDENT_DATA`，请进行静态import：

```java
import static com.wzt.aurora.spider.utils.Value.NeuVpnHandleValue.*;
```

在最新版本中，将原本的二进制或运算更改为可读性更高的`EnumSet`结构，`requestData`参数请这样填写：

```java
//获取多个数据
EnumSet.of(STUDENT_DATA, NET_DATA)
//获取单个数据
EnumSet.of(STUDENT_DATA)
```

学期id的获取请看[这儿](#提供的工具)

之后你就可以调用`getData()`方法获取数据：

```java
HashMap<Enum, AuroraData> data = autoHandle.getData();
```

因为所有的数据类都继承于`AuroraData`，所以调用该方法会返回这样的HashMap，获取指定数据可以这样使用：

```java
//请记得强制类型转换
StudentData studentData = (StudentData) (data.get(STUDENT_DATA));
```

#### 网络环境已知的情况

确定你在校园网环境下时使用`NeuHandle`，确定你在非校园网环境下时使用`VpnHandle`，适用于网络环境已知且不变的时候，它们存在的意义是快于`AutoHandle`，因为省去了判断网络环境的过程，但实际上也没有快多少。。。而且失去了在不同网络环境的适应能力，有有利有弊：

> 注意：！！！有时调用VpnHandle也会无法请求，因为学校有时会将一些内网网站向外网开放，这我也管不了，自求多福

```java
Handle handle;
//是否处于非校园网环境
boolean useVpn = false;
//非校园网环境
if (useVpn)
    handle = new VpnHandle("你的学号", "你的一网通密码", "学期id", requestData, ms->{
    	Thread.sleep(ms)
	});
else
    handle = new NeuHandle("你的学号", "你的一网通密码", "学期id", requestData, ms->{
    	Thread.sleep(ms)
	});
```

参数意义与[上一节](#网络环境未知的情况)相同，不再赘述

#### 提供的工具

本项目提供了RoomHandle来获取其他必要信息：学期id，学期数据，时间数据，教室占用，可用教室列表，校历等：

```java
RoomHandle roomHandle = new RoomHandle(requestData);
```

`requestData`为需要获得的数据，可选内容如下：

| 参数             | 代表的意义             | 数据类型           |
| ---------------- | ---------------------- | ------------------ |
| HE_SHI_LI_OCCUPY | 何世礼教室占用情况     | RoomData           |
| YI_FU_OCCUPY     | 逸夫楼教室占用情况     | RoomData           |
| DA_CHENG_OCCUPY  | 大成教室占用情况       | RoomData           |
| CAI_KUANG_OCCUPY | 采矿馆教室占用情况     | RoomData           |
| JI_DIAN_OCCUPY   | 机电馆教室占用情况     | RoomData           |
| DATE_DATA        | 需要的日期数据         | DateData           |
| SEMESTER_DATA    | 学期数据               | SemesterData       |
| SCHOOL_SCHEDULE  | 校历数据               | SchoolScheduleData |
| HE_SHI_LI_LIST   | 何世礼可用教室列表     | RoomListData       |
| YI_FU_LIST       | 逸夫楼可用教室列表     | RoomListData       |
| DA_CHENG_LIST    | 大成教学馆可用教室列表 | RoomListData       |
| CAI_KUANG_LIST   | 采矿馆可用教室列表     | RoomListData       |
| JI_DIAN_LIST     | 机电馆可用教室列表     | RoomListData       |

请注意，以上的`参数`内容均存于`Value.RoomHandleValue`枚举类中，如果你想使用`DATE_DATA`这样的形式而不是`Value.RoomHandleValue.DATE_DATA`，请进行静态import：

```java
import static com.wzt.aurora.spider.utils.Value.RoomHandleValue.*;
```

在最新版本中，将原本的二进制或运算更改为可读性更高的`EnumSet`结构，`requestData`参数请这样填写：

```java
//获取多个数据
EnumSet.of(HE_SHI_LI_OCCUPY, SEMESTER_DATA)
//获取单个数据
EnumSet.of(HE_SHI_LI_OCCUPY)
```

同上相同，使用`getData()`获取数据：

```java
HashMap<Enum, AuroraData> data = roomHandle.getData();
```

[点此](https://github.com/Wzt1714/Neu-Aurora-Spider/blob/main/DEMO.md) 查看完整demo

## 更新问题

间断更新，作者苦逼大三狗，这个也是android端app `SmartNeu` 的核心爬虫，同性交友站的issue也会看，但不会那么频繁。有bug可以QQ或者邮箱联系我。不用阿帕奇的网络请求框架是因为不兼容android，而且后来发现okhttp确实好用。顺便吐槽一下，现在时间是2022/1/31，shabi学校WebVpn维护不开，OpenVpn也连不上去。。。

## License

### [MIT协议](https://github.com/Wzt1714/Neu-Aurora-Spider/blob/main/LICENSE.md)
``````
/**************************************************************************************************
 * The MIT License (MIT)                                                                          *
 *                                                                                                *
 * Copyright © 2022 WangZeTong<1714233956@qq.com>                                                 *
 *                                                                                                *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software  *
 * and associated documentation files (the “NeuAuroraSpider”), to deal in the Software without    *
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,     *
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the  *
 * Software is furnished to do so, subject to the following conditions:                           *
 *                                                                                                *
 * The above copyright notice and this permission notice shall be included in all copies or       *
 * substantial portions of the Software.                                                          *
 *                                                                                                *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR                     *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS               *
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS                    *
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,                      *
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN                *
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                     *
 **************************************************************************************************/
``````

## 放在最后
搞了三四天，总算把这玩意搞定了，这个项目完美兼容Android端，爬取学校的最新一网通网站，所以也和最新的校园网收费规则一致，目前已知的最新的爬虫了
，作者不咋上同性交友站，有问题或者建议可以加[QQ](tencent://AddContact/?fromId=45&fromSubId=1&subcmd=all&uin=1714233956&website=www.oicqzone.com) (1714233956)来一起研究问题（小声逼逼：肯定没人加）
<br>
<br>最后更新于2022.1.31

