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

package com.wzt.aurora.spider.handle;

import com.wzt.aurora.spider.data.*;
import com.wzt.aurora.spider.net.Cookies;
import com.wzt.aurora.spider.utils.Utils;
import com.wzt.aurora.spider.exception.LoginException;
import com.wzt.aurora.spider.net.NeuRequest;
import com.wzt.aurora.spider.utils.Value;

import java.util.HashMap;
import java.util.List;

/**
 * <h1>NeuHandle-校园网环境下的数据获取器</h1>
 * <p>继承自Handle抽象类，实现了Value.NeuVpnHandleValue接口以调用其中的已定义常量</p>
 *
 * @see Handle
 * @see Value.NeuVpnHandleValue
 */
public class NeuHandle extends Handle implements Value.NeuVpnHandleValue {
    /**
     * <h3>学号</h3>
     */
    private String id;
    /**
     * <h3>密码</h3>
     */
    private String pass;
    /**
     * <h3>学期id</h3>
     * <p>在最初使用时，可能对这一参数感到困惑，学期id用于区分每个学期</p>
     * <p>可以通过指定RoomHandle参数为DATE_DATA获得当前时间数据，指定SEMESTER_DATA来获得学期id和具体学期的关系</p>
     * <p>
     * 快速获得当前学期id：
     * </p>
     * <pre>
     * {@code
     * RoomHandle roomHandle = new RoomHandle(DATE_DATA);
     * String semesterId = (DateData) roomHandle.getData().get("DATE_DATA").getNowSemesterId() + "";
     * }
     * </pre>
     *
     * @see DateData
     * @see SemesterData
     * @see Value.RoomHandleValue
     */
    private String semesterId;
    /**
     * <h3>请求数据</h3>
     * <p>具体请求数据请翻阅Value.NeuVpnHandleValue，当需要获得多个数据时，请将不同参数使用|分开</p>
     * <p>例：如需获得学生信息和考试信息，请传入参数 STUDENT_DATA | EXAM_DATA</p>
     *
     * @see Value.NeuVpnHandleValue
     */
    private int requestData;
    /**
     * <h3>用于进行一网通界面的数据请求</h3>
     * <p>虽然用户可以通过NeuRequest.EOneRequest进行手动请求，但这是一种强烈不建议的行为，
     * 建议用户直接使用Handle的形式来获取数据</p>
     */
    private NeuRequest.EOneRequest eOneRequest;
    /**
     * <h3>用于进行教务处界面的数据请求</h3>
     * <p>虽然用户可以通过NeuRequest.DeanRequest进行手动请求，但这是一种强烈不建议的行为，
     * 建议用户直接使用Handle的形式来获取数据</p>
     */
    private NeuRequest.DeanRequest deanRequest;
    /**
     * <h3>休眠方法</h3>
     * <p>学校网页的数据在请求过快时会禁止请求，由于各个平台的休眠方式不同，
     * 于是定义了函数式接口用于休眠</p>
     *
     * @see Sleep
     */
    private Sleep sleep;

    /**
     * NeuHandle的构造方法，通过传入请求数据来返回指定数据
     * <br>
     * 对于requestData参数，要求必须传入Value.NeuVpnHandleValue中的参数，参数及意义，数据类型如下：
     * <table>
     *   <caption>requestData参数</caption>
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
     * @param id          学号
     * @param pass        密码
     * @param semesterId  学期id
     * @param requestData 请求数据，多个数据请使用|隔开
     * @param sleep       函数式接口，需要用户自己实现指定平台的休眠方法
     */
    public NeuHandle(String id, String pass, String semesterId, int requestData, Sleep sleep) {
        this.id = id;
        this.pass = pass;
        this.semesterId = semesterId;
        this.requestData = requestData;
        this.sleep = sleep;
    }

    @Override
    public HashMap<String, AuroraData> getData() {
        if ((requestData & (STUDENT_DATA | CARD_DATA | NET_DATA | LIBRARY_BOOK)) == 0)
            deanRequest = new NeuRequest.DeanRequest(id, pass, semesterId);
        else if ((requestData & (COURSE_TABLE | GPA_DATA | EXAM_DATA)) == 0)
            eOneRequest = new NeuRequest.EOneRequest(id, pass);
        else {
            eOneRequest = new NeuRequest.EOneRequest(id, pass);
            deanRequest = new NeuRequest.DeanRequest(id, pass, semesterId);
        }
        HashMap<String, AuroraData> dataHashMap = new HashMap<>();
        if (deanRequest != null) {
            if ((requestData & COURSE_TABLE) == COURSE_TABLE) {
                if (deanRequest.isLogin()) {
                    String html = deanRequest.courseTableHtml();
//                    Utils.StandardUtils.dataPrint(html);
//                    Utils.StandardUtils.printToFiles(html, "data\\course_table.html");
                    CourseData courseData = Utils.DataTransformUtils.html2course(html);
                    dataHashMap.put("COURSE_TABLE", courseData);
                } else
                    throw new LoginException("教务处");
            }
            if ((requestData & GPA_DATA) == GPA_DATA) {
                if (deanRequest.isLogin()) {
                    String html = deanRequest.gpaHtml();
//                    Utils.StandardUtils.dataPrint(html);
//                    Utils.StandardUtils.printToFiles(html, "data\\score_data.html");
                    GpaData gpaData = Utils.DataTransformUtils.html2gpa(html);
                    dataHashMap.put("SCORE_DATA", gpaData);
                } else
                    throw new LoginException("教务处");
            }
            if ((requestData & EXAM_DATA) == EXAM_DATA) {
                if (deanRequest.isLogin()) {
                    String html = deanRequest.examListHtml();
//                    Utils.StandardUtils.dataPrint(html);
//                    Utils.StandardUtils.printToFiles(html, "data\\exam_data.html");
                    List<String> idList = Utils.DataTransformUtils.html2examIdList(html);
                    idList.remove(0);
                    List<ExamData.ChildExam> childExams = Utils.DataTransformUtils.html2examList(html);
                    for (String id : idList) {
                        String idHtml = deanRequest.examHtml(id);
                        sleep.sleep(500);
                        childExams.addAll(Utils.DataTransformUtils.html2examList(idHtml));
                    }
                    ExamData examData = new ExamData(childExams);
                    dataHashMap.put("EXAM_DATA", examData);
                } else
                    throw new LoginException("教务处");
            }
        }
        Cookies.NeuDeanCookies.getInstance().destroy();
        if (eOneRequest != null) {
            if ((requestData & STUDENT_DATA) == STUDENT_DATA) {
                if (eOneRequest.isLogin()) {
                    String json = eOneRequest.infoJson();
//                    Utils.StandardUtils.dataPrint(json);
//                    Utils.StandardUtils.printToFiles(json, "data\\student_data.json");
                    StudentData studentData = Utils.DataTransformUtils.json2student(json);
                    dataHashMap.put("STUDENT_DATA", studentData);
                } else
                    throw new LoginException("一网通");
            }
            if ((requestData & NET_DATA) == NET_DATA) {
                if (eOneRequest.isLogin()) {
                    String json = eOneRequest.networkJson();
//                    Utils.StandardUtils.dataPrint(json);
//                    Utils.StandardUtils.printToFiles(json, "data\\net_data.json");
                    NetData netData = Utils.DataTransformUtils.json2net(json);
                    dataHashMap.put("NET_DATA", netData);
                } else
                    throw new LoginException("一网通");
            }
            if ((requestData & CARD_DATA) == CARD_DATA) {
                if (eOneRequest.isLogin()) {
                    String json = eOneRequest.cardJson();
//                    Utils.StandardUtils.dataPrint(json);
//                    Utils.StandardUtils.printToFiles(json, "data\\card_data.json");
                    CardData cardData = Utils.DataTransformUtils.json2card(json);
                    dataHashMap.put("CARD_DATA", cardData);
                } else
                    throw new LoginException("一网通");
            }
            if ((requestData & LIBRARY_BOOK) == LIBRARY_BOOK) {
                if (eOneRequest.isLogin()) {
                    String html = eOneRequest.libraryHtml();
//                    Utils.StandardUtils.dataPrint(html);
//                    Utils.StandardUtils.printToFiles(html, "data\\library_book.html");
                    BookData bookData = Utils.DataTransformUtils.html2book(html);
                    dataHashMap.put("LIBRARY_BOOK", bookData);
                } else
                    throw new LoginException("一网通");
            }
        }
        Cookies.NeuEOneCookies.getInstance().destroy();
        return dataHashMap;
    }

}
