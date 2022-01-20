## Neu-Aurora-Spider
### 🔥🔥🔥东北大学最新爬虫🔥🔥🔥
<br>

> 这是一个东北大学的爬虫项目，使用Java+OkHttp实现，可以用于Android和服务端
>
> 只需要极少的代码就可以进行爬虫操作，爬虫可以在学校教务处和一网通两个网页
>
> 工作，尽可能多的覆盖所有信息

<br>

注意：1.2版本之前有着无法查询借阅图书的bug，请使用1.2或1.2+版本，更新日志[点此](https://github.com/Wzt1714/Neu-Aurora-Spider/UPDATE.md) 查看

## 准备工作

 在开始使用爬虫之前，请加入所需依赖，如果你使用Gradle工具，请在build.gradle文件下加入下面内容：

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

将下载好的jar包加入项目或者将源代码src/main/java路径下的文件加入项目

## 开始

####  网络环境未知的情况（校园网或非校园网）

可以直接使用AutoHandle来进行数据获取：

```java
AutoHandle autoHandle = new AutoHandle("你的学号", "你的一网通密码", "学期id", requestData, ms->{
    Thread.sleep(ms)
});
```

请完善你的学号、一网通密码信息。第五个参数为不同平台下的延迟，ms参数单位为毫秒，定义此参数的目的是防止访问过快使得学校网站禁止获取，第四个参数requestData为需要获得的数据，可选如下内容：

| 参数         | 代表的意义   | 数据类型    |
| ------------ | ------------ | ----------- |
| STUDENT_DATA | 学生数据     | StudentData |
| COURSE_TABLE | 课程表数据   | CourseData  |
| GPA_DATA     | 绩点数据     | GpaData     |
| EXAM_DATA    | 考试数据     | ExamData    |
| CARD_DATA    | 校园卡数据   | CardData    |
| NET_DATA     | 校园网数据   | NetData     |
| LIBRARY_BOOK | 借阅书籍数据 | BookData    |

如果你想一次性获取多个数据，请使用 | 隔开，如：

```java
STUDENT_DATA | NET_DATA
```

学期id的获取请看[这儿](#提供的工具)

之后你就可以调用getData()方法获取数据：

```java
HashMap<Integer, AuroraData> data = autoHandle.getData();
```

因为所有的数据类都继承于AuroraData，所以调用该方法会返回这样的HashMap，获取指定数据可以这样使用：

```java
StudentData studentData = (StudentData) (data.get(STUDENT_DATA));
```

#### 网络环境已知的情况

确定你在校园网环境下时使用NeuHandle，确定你在非校园网环境下时使用VpnHandle：

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

参数意义与[上一节](#网络环境未知的情况（校园网或非校园网）)相同，不再赘述

#### 提供的工具

本项目提供了RoomHandle来获取其他必要信息：学期id，学期数据，时间数据，教室占用等：

```java
RoomHandle roomHandle = new RoomHandle(requestData);
```

requestData为需要获得的数据，可选内容如下：

| 参数             | 代表的意义         | 数据类型     |
| ---------------- | ------------------ | ------------ |
| HE_SHI_LI_OCCUPY | 何世礼教室占用情况 | RoomData     |
| YI_FU_OCCUPY     | 逸夫楼教室占用情况 | RoomData     |
| DA_CHENG_OCCUPY  | 大成教室占用情况   | RoomData     |
| CAI_KUANG_OCCUPY | 采矿馆教室占用情况 | RoomData     |
| JI_DIAN_OCCUPY   | 机电馆教室占用情况 | RoomData     |
| DATE_DATA        | 需要的日期数据     | DateData     |
| SEMESTER_DATA    | 学期数据           | SemesterData |

其中学期id包括于DateData对象中，如果你想一次性获取多个数据，请使用 | 隔开，如：

```java
HE_SHI_LI_OCCUPY | DATE_DATA
```

同上相同，使用getData()获取数据：

```java
HashMap<Integer, AuroraData> data = roomHandle.getData();
```

[点此](https://github.com/Wzt1714/Neu-Aurora-Spider/DEMO.md) 查看完整demo

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
，作者不咋上同性交友站，有问题或者建议可以加[QQ](tencent://AddContact/?fromId=45&fromSubId=1&subcmd=all&uin=1714233956&website=www.oicqzone.com) 来一起研究问题（小声逼逼：肯定没人加）
<br>
<br>
更新于2022.1.20

