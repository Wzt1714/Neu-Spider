## 以下是两个完整的实例：

#### 对于手动进行校园网、WebVpn切换时（分别使用NeuHandle、VpnHandle）：

``````java
/*
 * 使用枚举 Value.RoomHandleValue 和 Value.NeuVpnHandle
 */
import static com.wzt.aurora.spider.utils.Value.NeuVpnHandleValue.*;
import static com.wzt.aurora.spider.utils.Value.RoomHandleValue.*;

public class Main {
    /*
     * 使用枚举 Value.RoomHandleValue 和 Value.NeuVpnHandle
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
         * SCHOOL_SCHEDULE				校历数据
         * HE_SHI_LI_LIST				何世礼可用教室列表
         * YI_FU_LIST					逸夫楼可用教室列表
         * DA_CHENG_LIST				大成教学馆可用教室列表
         * CAI_KUANG_LIST				采矿馆可用教室列表
         * JI_DIAN_LIST					机电馆可用教室列表
         * 在此处我们参数设为DATE_DATA，用于获得此时的学期id等数据，因为
         * 以下的操作需要学期id
         */
        RoomHandle roomHandle = new RoomHandle(EnumSet.of(DATE_DATA));
        /*
         * getData()方法返回一个HashMap，其中key为设置的枚举，va
         * lue为返回的数据对象，需要进行类型转化
         */
        DateData date = (DateData) roomHandle.getData().get(DATE_DATA);
        /*
         * 从对象中获取学期id并转化为字符串
         */
        String semester = date.getNowSemesterId() + "";
        /*
         * 因为进行爬虫操作时需要进行休眠操作（防止数据获取异常），由于不同
         * 平台休眠方法不同，所以由使用者自定义休眠操作（以PC端为例）
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
                    EnumSet.of(STUDENT_DATA, COURSE_TABLE, GPA_DATA, 
                    	EXAM_DATA, CARD_DATA, NET_DATA, LIBRARY_BOOK), sleep);
        else 
            handle = new NeuHandle(id, pass, semester,
                    EnumSet.of(STUDENT_DATA, COURSE_TABLE, GPA_DATA, 
                    	EXAM_DATA, CARD_DATA, NET_DATA, LIBRARY_BOOK), sleep);
        /*
         * 调用getData()方法获得数据，其中key为设置的参数字符串，va
         * lue为返回的数据对象，需要进行类型转化。
         */
        HashMap<Enum, AuroraData> dataHashMap = handle.getData();
        dataHashMap.forEach((e, data) -> {
            Utils.StandardUtils.dataPrint(data.toJson());
        });
    }
}
``````
#### 对于自动进行校园网、WebVpn切换时（使用AutoHandle）：

``````java
/*
 * 使用枚举 Value.RoomHandleValue 和 Value.NeuVpnHandle
 */
import static com.wzt.aurora.spider.utils.Value.NeuVpnHandleValue.*;
import static com.wzt.aurora.spider.utils.Value.RoomHandleValue.*;

public class Main {
    public static void main(String[] args) {
        /*
         * 这两个字符串设置为学号密码
         */
        String id = "你的学号", pass = "你的密码";
         /*
         * RoomHandle对象，可选参数有:
         * HE_SHI_LI_OCCUPY             何世礼教学馆今日教室占用情况
         * YI_FU_OCCUPY                 逸夫楼教学馆今日教室占用情况
         * DA_CHENG_OCCUPY              大成教学馆今日教室占用情况
         * CAI_KUANG_OCCUPY             采矿教学馆今日教室占用情况
         * JI_DIAN_OCCUPY               机电教学馆今日教室占用情况
         * DATE_DATA                    此时日期数据
         * SEMESTER_DATA                所有学期数据集
         * SCHOOL_SCHEDULE				校历数据
         * HE_SHI_LI_LIST				何世礼可用教室列表
         * YI_FU_LIST					逸夫楼可用教室列表
         * DA_CHENG_LIST				大成教学馆可用教室列表
         * CAI_KUANG_LIST				采矿馆可用教室列表
         * JI_DIAN_LIST					机电馆可用教室列表
         * 在此处我们参数设为DATE_DATA，用于获得此时的学期id等数据，因为
         * 以下的操作需要学期id
         */
        RoomHandle roomHandle = new RoomHandle(EnumSet.of(DATE_DATA));
        /*
         * getData()方法返回一个HashMap，其中key为设置的参数字符串，va
         * lue为返回的数据对象，需要进行类型转化
         */
        DateData date = (DateData) roomHandle.getData().get(DATE_DATA);
        /*
         * 从对象中获取学期id并转化为字符串
         */
        String semester = date.getNowSemesterId() + "";
        /*
         * 因为进行爬虫操作时需要进行休眠操作（防止数据获取异常），由于不同
         * 平台休眠方法不同，所以由使用者自定义休眠操作（以PC端为例）
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
         * 密码，学期id，要查询的数据，以及Sleep对象（函数式接口），可
         * 选参数有：
         * STUDENT_DATA                 学生个人信息数据
         * COURSE_TABLE                 本学期课表数据
         * SCORE_DATA                   本学期成绩数据
         * EXAM_DATA                    本学期考试数据
         * CARD_DATA                    校园卡余额数据
         * NET_DATA                     校园网余额数据
         * LIBRARY_BOOK                 图书馆借阅书籍数据
         */
        AutoHandle handle = new AutoHandle(id, pass, semester,
                    EnumSet.of(STUDENT_DATA, COURSE_TABLE, GPA_DATA, 
                    	EXAM_DATA, CARD_DATA, NET_DATA, LIBRARY_BOOK), sleep);
        HashMap<Enum, AuroraData> hashMap = handle.getData();
        hashMap.forEach(((e, auroraData) -> {
            Utils.StandardUtils.dataPrint(auroraData.toJson());
        }));
    }
}
``````