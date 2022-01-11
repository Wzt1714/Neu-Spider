## NeuAurora-Spider
#### 如你所见，这是一个东北大学的爬虫项目，在浏览了许许多多关于东北大学的爬虫项目之后，并没有发现一个满足我的需求的，于是自己动手实现了一个。
需知：此项目为了兼容Android开发，使用了Java+Okhttp的方案进行开发，并且也提供了json与数据互相转化的方法。

如需要修改源代码使用，请下载后将com.wzt.aurora.spider包放入你的项目，然后你就可以修改源代码以适应你的要求；<br>
如并无特殊功能实现，请直接下载jar包使用

### 快速开始
``````Java
public class Main implements Value.RoomHandleValue, Value.NeuVpnHandleValue {
    /**
     * 实现接口 Value.RoomHandleValue 和 Value.NeuVpnHandle
     * Value这两个接口只含有数据，不需要实现方法，实现的原因是因为
     * 方便使用定义的一些常量，当然你也可以使用静态导入的方法实现：
     * import static com.wzt.aurora.spider.utils.Value.NeuVpnHandleValue.*;
     * import static com.wzt.aurora.spider.utils.Value.RoomHandleValue.*;
     */
    public static void main(String[] args) throws IOException {
        /**
         * 选择是否使用Vpn，使用Vpn时请保证网络环境为非学校内网环境（非
         * 校园网），不使用Vpn时请保证网络环境为学校内网环境（校园网）
         */
        boolean useVpn = true;
        /**
         * 这两个字符串设置为学号密码
         */
        String id = "你的学号", pass = "你的一网通密码";
        /**
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
        /**
         * getData()方法返回一个HashMap，其中key为设置的参数字符串，va
         * lue为返回的数据对象，需要进行类型转化
         */
        DateData date = (DateData) roomHandle.getData().get("DATE_DATA");
        /**
         * 从对象中获取学期id并转化为字符串
         */
        String semester = date.getNowSemesterId() + "";
        /**
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
        /**
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
                    STUDENT_DATA | COURSE_TABLE | SCORE_DATA |
                            EXAM_DATA | CARD_DATA | NET_DATA | LIBRARY_BOOK, sleep);
        else {
            handle = new NeuHandle(id, pass, semester,
                    STUDENT_DATA | COURSE_TABLE | SCORE_DATA |
                            EXAM_DATA | CARD_DATA | NET_DATA | LIBRARY_BOOK, sleep);
            /**
             * 调用getData()方法获得数据，其中key为设置的参数字符串，va
             * lue为返回的数据对象，需要进行类型转化。
             */
            HashMap<String, AuroraData> dataHashMap = handle.getData();
            dataHashMap.forEach((string, data) -> {
                System.out.println(string + ": " + data.toJson());
            });
        }
    }
}
``````
### 数据格式


