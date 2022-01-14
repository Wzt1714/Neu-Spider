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

package com.wzt.aurora.spider.utils;

import okhttp3.Headers;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * <h1>Value-存放一些常量</h1>
 */
public class Value {
    /**
     * <h3>是否为debug模式</h3>
     * <p>debug模式会输出一些调试信息</p>
     */
    public static boolean debug = false;
    /**
     * <h3>星期几字符串和整数的映射</h3>
     */
    public static final HashMap<String, String> dayMap = new HashMap<>();

    /**
     * 初始化映射
     */
    static {
        dayMap.put("0", "星期一");
        dayMap.put("1", "星期二");
        dayMap.put("2", "星期三");
        dayMap.put("3", "星期四");
        dayMap.put("4", "星期五");
        dayMap.put("5", "星期六");
        dayMap.put("6", "星期日");
    }
    /**
     * <h1>ClientCode-标记请求值</h1>
     */
    public enum ClientCode{
        /**
         * <h3>无请求的情况</h3>
         */
        NULL_CLIENT,
        /**
         * <h3>校园网下请求一网通</h3>
         */
        NEU_E_ONE_CLIENT,
        /**
         * <h3>校园网下请求教务处</h3>
         */
        NEU_DEAN_CLIENT,
        /**
         * <h3>请求一些常用数据和教室占用情况</h3>
         */
        ROOM_CLIENT,
        /**
         * <h3>非校园网情况下的请求</h3>
         */
        VPN_E_ONE_CLIENT,
        /**
         * <h3>查询网络环境的请求</h3>
         */
        NET_ENVIRONMENT_CLIENT;
    }

    /**
     * <h1>RequestHeadsValue-请求头数据</h1>
     */
    public static class RequestHeadsValue {
        /**
         * <h3>请求头</h3>
         */
        public final static Headers DEFAULT_HEADER = new Headers.Builder()
                .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                .build();
    }

    /**
     * <h1>PatternValue-使用正则表达式的值</h1>
     */
    public static class PatternValue {
        /**
         * <h3>匹配登录页面的lt参数</h3>
         */
        public final static Pattern LT = Pattern.compile("<input type=\"hidden\" id=\"lt\" name=\"lt\" value=\"(.*?)\" />");
        /**
         * <h3>匹配登录页面的execution参数</h3>
         */
        public final static Pattern EXECUTION = Pattern.compile("<input type=\"hidden\" name=\"execution\" value=\"(.*?)\" />");
        /**
         * <h3>匹配校园网下前往借阅书籍页面的url</h3>
         */
        public final static Pattern BORROW_BOOK_URL = Pattern.compile("<td class=td2 align=left width=35%>\\s*外借\\s*</td>\\s*<td class=td1><a href=\\\"javascript:replacePage\\('(.*?)'\\);\\\">0\\s*</a></td>");
        /**
         * <h3>大致匹配到课表的所有课程信息</h3>
         */
        public final static Pattern COURSE_SUM = Pattern.compile("var actTeachers = (.*?);[\\s|\\S]*?activity = new TaskActivity\\(actTeacherId\\.join\\(','\\),actTeacherName\\.join\\(','\\),\\\"[\\s|\\S]*?\\\",\\\"(.*?)\\([\\s|\\S]*?\\)\\\",\\\"[\\s|\\S]*?\\\",\\\"(.*?)\\\",\\\"0(.*?)\\\",null,null,assistantName,\\\"\\\",\\\"\\\"\\);\\s*([index =[0-9]+\\*unitCount\\+[0-9]+;\\s*table0\\.activities\\[index\\]\\[table0\\.activities\\[index\\]\\.length\\]=activity;\\s*]+)\n");
        /**
         * <h3>进一步匹配课表里的时间和天数信息</h3>
         */
        public final static Pattern COURSE_DAY_AND_TIME = Pattern.compile("index =(.*?)\\*unitCount\\+(.*?);");
        /**
         * <h3>匹配总平均绩点</h3>
         */
        public final static Pattern GPA_SUM = Pattern.compile("<div>总平均绩点：(.*?)</div>");
        /**
         * <h3>匹配非校园网下前往借阅书籍界面的url</h3>
         */
        public final static Pattern BORROW_BOOK_URL_VPN = Pattern.compile("<td class=td2 align=left width=35%>\\s*外借\\s*</td>\\s*<td class=td1><a href=\\\"javascript:this\\.top\\.vpn_inject_scripts_window\\(this\\);vpn_eval\\(\\(function \\(\\) \\{ replacePage\\(&#39;(.*?)&#39;\\); \\}\\)\\.toString\\(\\)\\.slice\\(14, -2\\)\\)\\\">0\\s*</a></td>");
    }

    /**
     * <h1>NeuVpnHandleValue-对NeuHandle和VpnHandle常量定义的接口</h1>
     * <br>
     * <table>
     *   <caption>NeuVpnHandleValue</caption>
     *   <tr>
     *       <th>参数</th>
     *       <th>|</th>
     *       <th>意义</th>
     *       <th>|</th>
     *       <th>数据类型</th>
     *   </tr>
     *   <tbody>
     *       <tr>
     *           <td>STUDENT_DATA</td>
     *           <td>|</td>
     *           <td>学生数据</td>
     *           <td>|</td>
     *           <td>StudentData</td>
     *       </tr>
     *       <tr>
     *           <td>COURSE_TABLE</td>
     *           <td>|</td>
     *           <td>课程表数据</td>
     *           <td>|</td>
     *           <td>CourseData</td>
     *       </tr>
     *       <tr>
     *           <td>GPA_DATA</td>
     *           <td>|</td>
     *           <td>绩点数据</td>
     *           <td>|</td>
     *           <td>GpaData</td>
     *       </tr>
     *       <tr>
     *           <td>EXAM_DATA</td>
     *           <td>|</td>
     *           <td>考试数据</td>
     *           <td>|</td>
     *           <td>ExamData</td>
     *       </tr>
     *       <tr>
     *           <td>CARD_DATA</td>
     *           <td>|</td>
     *           <td>校园卡数据</td>
     *           <td>|</td>
     *           <td>CardData</td>
     *       </tr>
     *       <tr>
     *           <td>NET_DATA</td>
     *           <td>|</td>
     *           <td>校园网数据</td>
     *           <td>|</td>
     *           <td>NetData</td>
     *       </tr>
     *       <tr>
     *           <td>LIBRARY_BOOK</td>
     *           <td>|</td>
     *           <td>借阅书籍数据</td>
     *           <td>|</td>
     *           <td>BookData</td>
     *       </tr>
     *   </tbody>
     * </table>
     * <br>
     * <p>注意：返回数据类型指的是NeuHandle或VpnHandle调用getData后返回的数据类型</p>
     *
     * @see com.wzt.aurora.spider.handle.NeuHandle
     * @see com.wzt.aurora.spider.handle.VpnHandle
     */
    public interface NeuVpnHandleValue {
        /**
         * <h3>学生数据</h3>
         * <p>返回数据类型为StudentData</p>
         */
        int STUDENT_DATA = 0x0001;
        /**
         * <h3>课程表数据</h3>
         * <p>返回数据类型为CourseData</p>
         */
        int COURSE_TABLE = 0x0001 << 1;
        /**
         * <h3>绩点数据</h3>
         * <p>返回数据类型为GpaData</p>
         */
        int GPA_DATA = 0x0001 << 2;
        /**
         * <h3>考试数据</h3>
         * <p>返回数据类型为GpaData</p>
         */
        int EXAM_DATA = 0x0001 << 3;
        /**
         * <h3>校园卡数据</h3>
         * <p>返回数据类型为CardData</p>
         */
        int CARD_DATA = 0x0001 << 4;
        /**
         * <h3>校园网数据</h3>
         * <p>返回数据类型为NetData</p>
         */
        int NET_DATA = 0x0001 << 5;
        /**
         * <h3>借阅数据数据</h3>
         * <p>返回类型为BookData</p>
         */
        int LIBRARY_BOOK = 0x0001 << 6;
    }

    /**
     * <h1>RoomHandleValue-对RoomHandle常量定义的接口</h1>
     * <br>
     * <table>
     *   <caption>RoomHandleValue参数</caption>
     *   <tr>
     *       <th>参数</th>
     *       <th>|</th>
     *       <th>意义</th>
     *       <th>|</th>
     *       <th>数据类型</th>
     *   </tr>
     *   <tbody>
     *       <tr>
     *           <td>HE_SHI_LI_OCCUPY</td>
     *           <td>|</td>
     *           <td>何世礼教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>YI_FU_OCCUPY</td>
     *           <td>|</td>
     *           <td>逸夫楼教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>DA_CHENG_OCCUPY</td>
     *           <td>|</td>
     *           <td>大成教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>CAI_KUANG_OCCUPY</td>
     *           <td>|</td>
     *           <td>采矿馆教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>JI_DIAN_OCCUPY</td>
     *           <td>|</td>
     *           <td>机电馆教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>DATE_DATA</td>
     *           <td>|</td>
     *           <td>需要的日期数据</td>
     *           <td>|</td>
     *           <td>DateData</td>
     *       </tr>
     *       <tr>
     *           <td>SEMESTER_DATA</td>
     *           <td>|</td>
     *           <td>学期数据</td>
     *           <td>|</td>
     *           <td>SemesterData</td>
     *       </tr>
     *   </tbody>
     * </table>
     * <br>
     * <p>注意：返回数据类型指的是RoomHandle调用getData后返回的数据类型</p>
     *
     * @see com.wzt.aurora.spider.handle.RoomHandle
     */
    public interface RoomHandleValue {
        /**
         * <h3>何世礼教室占用情况</h3>
         * <p>返回类型为RoomData</p>
         */
        int HE_SHI_LI_OCCUPY = 0x0001;
        /**
         * <h3>逸夫楼教室占用情况</h3>
         * <p>返回类型为RoomData</p>
         */
        int YI_FU_OCCUPY = 0x0001 << 1;
        /**
         * <h3>大成教室占用情况</h3>
         * <p>返回类型为RoomData</p>
         */
        int DA_CHENG_OCCUPY = 0x0001 << 2;
        /**
         * <h3>采矿馆教室占用情况</h3>
         * <p>返回类型为RoomData</p>
         */
        int CAI_KUANG_OCCUPY = 0x0001 << 3;
        /**
         * <h3>机电馆教室占用情况</h3>
         * <p>返回类型为RoomData</p>
         */
        int JI_DIAN_OCCUPY = 0x0001 << 4;
        /**
         * <h3>需要的日期数据</h3>
         * <p>返回类型为DateData</p>
         */
        int DATE_DATA = 0x0001 << 5;
        /**
         * <h3>学期数据</h3>
         * <p>返回类型为SemesterData</p>
         */
        int SEMESTER_DATA = 0x0001 << 6;
    }
}
