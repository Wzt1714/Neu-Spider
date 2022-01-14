# Neu-Aurora-Spider
## 🔥🔥🔥东北大学最新爬虫🔥🔥🔥
<br>
如你所见，这是一个东北大学的爬虫项目，在浏览了许许多多关于东北大学的爬虫项目之后，并没有发现一个满足我的需求的，于是自己动手实现了一个。
需知：此项目为了兼容Android开发，使用了Java+Okhttp的方案进行开发，并且也提供了json与数据互相转化的方法。
<br>

-----

请下载后将com.wzt.aurora.spider包放入你的项目，然后你就可以修改源代码以适应你的要求；或者直接将jar包下载后导入

-----

## 快速开始
在使用jar包或源代码之前请先添加依赖，如果使用Gradle构建时，添加如下依赖：
``````gradle
dependencies {
    dependencies {
        implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.0"))
        implementation("com.squareup.okhttp3:okhttp")
        implementation("com.squareup.okhttp3:logging-interceptor")
    }
    implementation group: 'org.jsoup', name: 'jsoup', version: '1.13.1'
    implementation 'net.sf.json-lib:json-lib:2.4:jdk15'
}
``````
成功添加依赖后你就可以直接访问数据了，如果在校园网环境下，请使用
``````Java
NeuHandle handle = new NeuHandle("你的学号", "你的密码", "本学期学期id", STUDENT_DATA | COURSE_TABLE | GPA_DATA |EXAM_DATA | CARD_DATA | NET_DATA | LIBRARY_BOOK, ms->{
    /*
     * 在此处实现延迟的逻辑，以下的例子为PC端，如果在Android端，
     * 请调用Android的延迟方法（TimerTask 、Android的Handle
     * 等）
     */
    try {
        Thread.sleep(ms);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
});
``````
在非校园网环境下，请使用
``````Java
VpnHandle handle = new VpnHandle("你的学号", "你的密码", "本学期学期id", STUDENT_DATA | COURSE_TABLE | GPA_DATA |EXAM_DATA | CARD_DATA | NET_DATA | LIBRARY_BOOK, ms->{
    /*
     * 在此处实现延迟的逻辑，以下的例子为PC端，如果在Android端，
     * 请调用Android的延迟方法（TimerTask 、Android的Handle
     * 等）
     */
    try {
        Thread.sleep(ms);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
});
``````
在这之后，请调用getData()方法获得数据
``````Java
HashMap<String, AuroraData> data = handle.getData();
``````
NeuHandle和VpnHandle中使用的参数“学期id”是通过RoomHandle获得的，快速获得当前学期id请使用如下方法：
``````Java
DateData date = (DateData) new RoomHandle(DATE_DATA).getData().get("DATE_DATA");
String semester = date.getNowSemesterId() + "";
``````
以下是两个完整的实例：
- 对于手动进行校园网、WebVpn切换时：
``````Java
public class Main implements Value.RoomHandleValue, Value.NeuVpnHandleValue {
    /*
     * 调用接口 Value.RoomHandleValue 和 Value.NeuVpnHandle
     * 定义的一些常量，当然你也可以使用静态导入的方法调用他们：
     * import static com.wzt.aurora.spider.utils.Value.NeuVpnHandleValue.*;
     * import static com.wzt.aurora.spider.utils.Value.RoomHandleValue.*;
     */
    public static void main(String[] args) throws IOException {
        /*
         * 选择是否使用Vpn，使用Vpn时请保证网络环境为非学校内网环境（非
         * 校园网），不使用Vpn时请保证网络环境为学校内网环境（校园网）
         */
        boolean useVpn = true;
        /*
         * 这两个字符串设置为学号密码
         */
        String id = "你的学号", pass = "你的一网通密码";
        /*
         * RoomHandle对象，构造器参数为多个时请使用 | 分割，可选参数有:
         * HE_SHI_LI_OCCUPY             何世礼教学馆今日教室占用情况
         * YI_FU_OCCUPY                 逸夫楼教学馆今日教室占用情况
         * DA_CHENG_OCCUPY              大成教学馆今日教室占用情况
         * CAI_KUANG_OCCUPY             采矿教学馆今日教室占用情况
         * JI_DIAN_OCCUPY               机电教学馆今日教室占用情况
         * DATE_DATA                    此时日期数据
         * SEMESTER_DATA                所有学期数据集
         * 在此处我们参数设为DATE_DATA，用于获得此时的学期id等数据，因为
         * 以下的操作需要学期id
         */
        RoomHandle roomHandle = new RoomHandle(DATE_DATA);
        /*
         * getData()方法返回一个HashMap，其中key为设置的参数字符串，va
         * lue为返回的数据对象，需要进行类型转化
         */
        DateData date = (DateData) roomHandle.getData().get("DATE_DATA");
        /*
         * 从对象中获取学期id并转化为字符串
         */
        String semester = date.getNowSemesterId() + "";
        /*
         * 因为进行爬虫操作时需要进行休眠操作（防止数据获取异常），由于不同
         * 平台休眠方法不同，所以由使用者自定义休眠操作（以电脑端为例）
         */
        Sleep sleep = ms -> {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Handle handle;
        if (useVpn)
        /*
         * 如果使用Vpn，则实例化VpnHandle对象，五个参数分别为学号，一网通
         * 密码，学期id，要查询的数据，以及Sleep对象（函数式接口），其中要
         * 查询的数据参数为多个时请使用 | 分割，可选参数有：
         * STUDENT_DATA                 学生个人信息数据
         * COURSE_TABLE                 本学期课表数据
         * SCORE_DATA                   本学期成绩数据
         * EXAM_DATA                    本学期考试数据
         * CARD_DATA                    校园卡余额数据
         * NET_DATA                     校园网余额数据
         * LIBRARY_BOOK                 图书馆借阅书籍数据
         * 此参数与NeuHandle相同，以下不在赘述
         */
            handle = new VpnHandle(id, pass, semester,
                    STUDENT_DATA | COURSE_TABLE | GPA_DATA |
                            EXAM_DATA | CARD_DATA | NET_DATA | LIBRARY_BOOK, sleep);
        else 
            handle = new NeuHandle(id, pass, semester,
                    STUDENT_DATA | COURSE_TABLE | GPA_DATA |
                            EXAM_DATA | CARD_DATA | NET_DATA | LIBRARY_BOOK, sleep);
        /*
         * 调用getData()方法获得数据，其中key为设置的参数字符串，va
         * lue为返回的数据对象，需要进行类型转化。
         */
        HashMap<String, AuroraData> dataHashMap = handle.getData();
        dataHashMap.forEach((string, data) -> {
            System.out.println(string + ": " + data.toJson());
        });
    }
}
``````
- 对于自动进行校园网、WebVpn切换时：
``````Java
public class Main implements Value.RoomHandleValue, Value.NeuVpnHandleValue{
    public static void main(String[] args) {
        /*
         * 这两个字符串设置为学号密码
         */
        String id = "20194148", pass = "wangzetong1115";
        /*
         * RoomHandle对象，构造器参数为多个时请使用 | 分割，可选参数有:
         * HE_SHI_LI_OCCUPY             何世礼教学馆今日教室占用情况
         * YI_FU_OCCUPY                 逸夫楼教学馆今日教室占用情况
         * DA_CHENG_OCCUPY              大成教学馆今日教室占用情况
         * CAI_KUANG_OCCUPY             采矿教学馆今日教室占用情况
         * JI_DIAN_OCCUPY               机电教学馆今日教室占用情况
         * DATE_DATA                    此时日期数据
         * SEMESTER_DATA                所有学期数据集
         * 在此处我们参数设为DATE_DATA，用于获得此时的学期id等数据，因为
         * 以下的操作需要学期id
         */
        RoomHandle roomHandle = new RoomHandle(DATE_DATA);
        /*
         * getData()方法返回一个HashMap，其中key为设置的参数字符串，va
         * lue为返回的数据对象，需要进行类型转化
         */
        DateData date = (DateData) roomHandle.getData().get("DATE_DATA");
        /*
         * 从对象中获取学期id并转化为字符串
         */
        String semester = date.getNowSemesterId() + "";
        /*
         * 因为进行爬虫操作时需要进行休眠操作（防止数据获取异常），由于不同
         * 平台休眠方法不同，所以由使用者自定义休眠操作（以电脑端为例）
         */
        Sleep sleep = ms -> {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        /*
         * 判断当前网络环境，自动进行实例化，五个参数分别为学号，一网通
         * 密码，学期id，要查询的数据，以及Sleep对象（函数式接口），其
         * 中要查询的数据参数为多个时请使用 | 分割，可选参数有：
         * STUDENT_DATA                 学生个人信息数据
         * COURSE_TABLE                 本学期课表数据
         * SCORE_DATA                   本学期成绩数据
         * EXAM_DATA                    本学期考试数据
         * CARD_DATA                    校园卡余额数据
         * NET_DATA                     校园网余额数据
         * LIBRARY_BOOK                 图书馆借阅书籍数据
         */
        AutoHandle handle = new AutoHandle(id, pass, semester,
                STUDENT_DATA | COURSE_TABLE | GPA_DATA |
                        EXAM_DATA | CARD_DATA | NET_DATA | LIBRARY_BOOK, sleep);
        HashMap<String, AuroraData> hashMap = handle.getData();
        hashMap.forEach(((str, auroraData) -> {
            Utils.StandardUtils.dataPrint(str + ": " + auroraData.toJson());
        }));
    }
}
``````
## 文档和源码
文档由JavaDoc工具直接生成，文档和源码、jar包下载请[点击此处](https://github.com/Wzt1714/Neu-Aurora-Spider/releases/)

## License
### [MIT](https://github.com/Wzt1714/Neu-Aurora-Spider/blob/main/LICENSE.md)

## 放在最后
搞了三四天，总算把这玩意搞定了，这个项目完美兼容Android端，爬取学校的最新一网通网站，所以也和最新的校园网收费规则一致，目前已知的最新的爬虫了
，作者不咋上同性交友站，有问题或者建议可以加QQ：1714233956来一起研究问题（小声逼逼：肯定没人加）
<br>
<br>
更新于2022.1.12
