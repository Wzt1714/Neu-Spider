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

import com.wzt.aurora.spider.data.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * <h1>Utils-工具类</h1>
 * <p>数据转化工具、网络请求工具、常用工具</p>
 */
public class Utils {
    /**
     * <h1>DataTransformUtils-数据转化工具集</h1>
     */
    public static class DataTransformUtils {
        /**
         * 将获得的json数据转化为StudentData对象
         *
         * @param json json数据
         * @return StudentData对象
         */
        public static StudentData json2student(String json) {
            JSONObject object = JSONObject.fromObject(json);
            return new StudentData(object.getString("RYLX"), object.getString("ZZJG"), object.getString("XM"));
        }

        /**
         * 将获得的json数据转化为CardData对象
         *
         * @param json json数据
         * @return CardData对象
         */
        public static CardData json2card(String json) {
            JSONObject object = JSONObject.fromObject(json);
            return new CardData(object.getString("balance"));
        }

        /**
         * 将获得的json数据转化为NetData对象
         *
         * @param json json数据
         * @return NetData对象
         */
        public static NetData json2net(String json) {
            JSONObject object = JSONObject.fromObject(json);
            return new NetData(object.getString("balance"), object.getString("usedData"));
        }

        /**
         * 将获得的html数据转化为CourseData对象
         *
         * @param html html数据
         * @return CourseData对象
         */
        public static CourseData html2course(String html) {
            List<CourseData.ChildCourse> courseList = new ArrayList<>();
            Matcher findData = Value.PatternValue.COURSE_SUM.matcher(html);
            while (findData.find()) {
                String teacherName = "";
                String teacherJson = findData.group(1);
                List<JSONObject> array = JSONArray.fromObject(teacherJson);
                for (JSONObject object : array) {
                    teacherName += object.getString("name") + ",";
                }
                teacherName = teacherName.substring(0, teacherName.length() - 1);

                String courseName = findData.group(2);

                String coursePlace = findData.group(3);

                String weekData = findData.group(4);

                String dayTimeString = findData.group(5);

                Matcher findDayTime = Value.PatternValue.COURSE_DAY_AND_TIME.matcher(dayTimeString);

                List<Pair<String, String>> dayTimeList = new ArrayList<>();

                while (findDayTime.find()) {
                    Pair<String, String> pair = new Pair<>(findDayTime.group(1), findDayTime.group(2));
                    dayTimeList.add(pair);
                }

                String dayData = Value.dayMap.get(dayTimeList.get(0).getFirst());

                StringBuilder builder = new StringBuilder("000000000000");

                for (Pair<String, String> pair : dayTimeList) {
                    int index = Integer.valueOf(pair.getSecond());
                    builder.replace(index, index + 1, "1");
                }

                String timeData = builder.toString();
                CourseData.ChildCourse childCourse = new CourseData.ChildCourse(courseName, coursePlace, teacherName, timeData, dayData, weekData);
                courseList.add(childCourse);
            }
            return new CourseData(courseList);
        }

        /**
         * 将获得的html数据转化为GpaData对象
         *
         * @param html html数据
         * @return GpaData对象
         */
        public static GpaData html2gpa(String html) {
            Matcher gpaSum = Value.PatternValue.GPA_SUM.matcher(html);
            String sum = "";
            if (gpaSum.find()) {
                sum = gpaSum.group(1);
            }

            Document document = Jsoup.parse(html);
            List<GpaData.ChildGpa> gpaList = new ArrayList<>();
            ArrayList<Element> table = document.selectFirst("tbody").select("tr");
            for (Element tr : table) {
                String courseName = "";
                String courseType = "";
                String courseCredit = "";
                String usualResults = "";
                String midTermResults = "";
                String endTermResults = "";
                String generalResults = "";
                String GPA = "";
                ArrayList<Element> td = tr.select("td");
                if (td.size() == 14) {
                    courseName = td.get(3).text();
                    courseType = td.get(5).text();
                    courseCredit = td.get(6).text();
                    usualResults = td.get(7).text();
                    midTermResults = td.get(8).text();
                    endTermResults = td.get(9).text();
                    generalResults = td.get(10).text();
                    GPA = td.get(12).text();
                } else if (td.size() == 12) {
                    courseName = td.get(3).text();
                    courseType = td.get(5).text();
                    courseCredit = td.get(6).text();
                    usualResults = "无";
                    midTermResults = "无";
                    endTermResults = td.get(7).text();
                    generalResults = td.get(8).text();
                    GPA = td.get(10).text();
                }
                GpaData.ChildGpa childGpa = new GpaData.ChildGpa(courseName, courseType, courseCredit, usualResults, midTermResults, endTermResults, generalResults, GPA);
                gpaList.add(childGpa);
            }
            return new GpaData(sum, gpaList);
        }

        /**
         * 将获得的html数据转化为List&lt;String&gt;对象
         *
         * @param html html数据
         * @return List&lt;String&gt;对象
         */
        public static List<String> html2examIdList(String html) {
            List<String> arrayList = new ArrayList<>();
            Document document = Jsoup.parse(html);
            ArrayList<Element> list = document.selectFirst("#examBatchId").select("option");
            for (Element element : list) {
                arrayList.add(element.attr("value"));
            }
            return arrayList;
        }

        /**
         * 将获得的html数据转化为List&lt;ExamData.ChildExam&gt;对象
         *
         * @param html html数据
         * @return List&lt;ExamData.ChildExam&gt;对象
         */
        public static List<ExamData.ChildExam> html2examList(String html) {
            List<ExamData.ChildExam> result = new ArrayList<>();
            Document document = Jsoup.parse(html);
            ArrayList<Element> tr = document.selectFirst("tbody").select("tr");
            if (tr != null) {
                for (Element element : tr) {
                    ArrayList<Element> td = element.select("td");
                    if (td.size() != 0) {
                        String courseName = td.get(1).text();
                        String examType = td.get(2).text();
                        String examDate = td.get(3).text();
                        String examTime = td.get(4).text();
                        String examPlace = td.get(5).selectFirst("a").text();
                        String examSeatId = td.get(6).text();
                        String examStatus = td.get(7).text();
                        ExamData.ChildExam childExam = new ExamData.ChildExam(courseName, examType, examDate, examTime, examPlace, examSeatId, examStatus);
                        result.add(childExam);
                    }
                }
            }
            return result;
        }

        /**
         * 将获得的html数据转化为BookData对象
         *
         * @param html html数据
         * @return BookData对象
         */
        public static BookData html2book(String html) {
            List<BookData.ChildBook> borrowBookData = new ArrayList<>();
            try {
                Document document = Jsoup.parse(html);
                Element table = document.select("body > center > center > table:nth-child(6) > tbody").first();
                if (table != null) {
                    ArrayList<Element> tr = table.select("tr");
                    tr.remove(0);
                    for (Element trElement : tr) {
                        ArrayList<Element> td = trElement.select("td");
                        td.remove(0);
                        td.remove(0);
                        td.remove(7);
                        td.remove(7);
                        BookData.ChildBook bookData = new BookData.ChildBook(td.get(0).html(), td.get(1).selectFirst("a").html(), td.get(2).html(), td.get(6).html(), td.get(3).html(), td.get(4).html(), td.get(5).html());
                        borrowBookData.add(bookData);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new BookData(borrowBookData);
        }

        /**
         * 将获得的json数据转化为RoomData对象
         *
         * @param json json数据
         * @return RoomData对象
         */
        public static RoomData json2room(String json) {
            List<RoomData.ChildRoom> roomList = new ArrayList<>();
            JSONArray array = JSONArray.fromObject(json);
            for (int i = 0; i < array.size(); i++) {
                String courseName = ((JSONObject) array.get(i)).getString("courseName");
                String courseClass = ((JSONObject) array.get(i)).getString("courseClass");
                String courseWeekTemp = ((JSONObject) array.get(i)).getString("courseWeek");
                String courseDay = ((JSONObject) array.get(i)).getString("courseDay");
                String courseTimeTemp = ((JSONObject) array.get(i)).getString("courseTime");
                String courseTeacher = ((JSONObject) array.get(i)).getString("courseTeacher");
                String coursePlace = ((JSONObject) array.get(i)).getString("coursePlace");
                int[] courseWeek = new int[courseWeekTemp.length()];
                for (int j = 0; j < courseWeekTemp.length(); j++) {
                    courseWeek[j] = courseWeekTemp.charAt(j);
                }
                int[] courseTime = new int[courseTimeTemp.length()];
                for (int j = 0; j < courseTimeTemp.length(); j++) {
                    courseTime[j] = courseTimeTemp.charAt(j);
                }
                RoomData.ChildRoom childRoom = new RoomData.ChildRoom(courseName, courseClass, courseWeek, courseDay, courseTime, courseTeacher, coursePlace);
                roomList.add(childRoom);
            }
            return new RoomData(roomList);
        }

        /**
         * 将获得的json数据转化为DateData对象
         *
         * @param json json数据
         * @return DateData对象
         */
        public static DateData json2date(String json) {
            JSONObject object = JSONObject.fromObject(json);
            int startYear = Integer.parseInt(object.getString("startYear"));
            int startMonth = Integer.parseInt(object.getString("startMonth"));
            int startDay = Integer.parseInt(object.getString("startDay"));
            int nowWeekNum = Integer.parseInt(object.getString("nowWeekNum"));
            String nowDayNum = object.getString("nowDayNum");
            int nowSemesterId = Integer.parseInt(object.getString("nowSemesterId"));
            return new DateData(startYear, startMonth, startDay, nowWeekNum, nowDayNum, nowSemesterId);
        }

        /**
         * 将获得的json数据转化为SemesterData对象
         *
         * @param json json数据
         * @return SemesterData对象
         */
        public static SemesterData json2semester(String json) {
            List<SemesterData.ChildSemester> semesterList = new ArrayList<>();
            JSONArray array = JSONArray.fromObject(json);
            for (Object o : array) {
                String semesterName = ((JSONObject) o).getString("semesterName");
                String semesterId = ((JSONObject) o).getString("semesterId");
                SemesterData.ChildSemester childSemester = new SemesterData.ChildSemester(semesterName, semesterId);
                semesterList.add(childSemester);
            }
            return new SemesterData(semesterList);
        }
    }

    /**
     * <h1>StandardUtils-常用工具集</h1>
     */
    public static class StandardUtils {
        /**
         * 格式化数据输出
         *
         * @param s 要输出的数据
         */
        public static void dataPrint(String s) {
            System.out.println("\033[1;94m" + s);
            System.out.print("\033[0;38m");
        }

        /**
         * 将数据输出至文件
         *
         * @param data 数据
         * @param path 文件路径
         */
        public static void printToFiles(String data, String path) {
            try {
                FileOutputStream outputStream = new FileOutputStream(path);
                outputStream.write(data.getBytes(StandardCharsets.UTF_8));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 整型数组转化字符串
         *
         * @param in 数字
         * @return 字符串
         */
        public static String array2str(int[] in) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < in.length; i++) {
                builder.append((in[i] + ""));
            }
            return builder.toString();
        }

        /**
         * 字符串转整型数组
         *
         * @param string 字符串
         * @return 整型数组
         */
        public static int[] str2array(String string) {
            int[] result = new int[string.length()];
            for (int i = 0; i < result.length; i++) {
                result[i] = Integer.valueOf(string.charAt(i));
            }
            return result;
        }
    }

    /**
     * <h1>ClientUtils-网络工具集</h1>
     */
    public static class ClientUtils {
        /**
         * 根据url快速构建请求
         *
         * @param url url
         * @return 构建好的请求
         */
        public static Request fastBuildRequest(String url) {
            Request request = new Request.Builder()
                    .url(url)
                    .headers(Value.RequestHeadsValue.DEFAULT_HEADER)
                    .get()
                    .build();
            return request;
        }

        /**
         * 根据url和数据体快速构建请求
         *
         * @param url  url
         * @param body 数据体
         * @return 构建好的请求
         */
        public static Request fastBuildRequest(String url, RequestBody body) {
            Request request = new Request.Builder()
                    .url(url)
                    .headers(Value.RequestHeadsValue.DEFAULT_HEADER)
                    .post(body)
                    .build();
            return request;
        }

        /**
         * 输出回复信息
         *
         * @param response 回复
         */
        public static void printClientData(Response response) {
            if (Value.debug) {
                System.out.println("Url = " + response.request().url());
                System.out.println("Method = " + response.request().method());
                System.out.println("Code = " + response.code());
                System.out.println("Location = " + response.headers("Location"));
                System.out.println();
            }
        }

        /**
         * 根据获得的登录页面快速构建数据体
         *
         * @param loginHtml html登录页面
         * @param stuId     学号
         * @param stuPass   密码
         * @return 数据体
         */
        public static RequestBody fastBuildLoginBody(String loginHtml, String stuId, String stuPass) {
            String lt = "", rsa = "", execution = "";
            Matcher ltGroup = Value.PatternValue.LT.matcher(loginHtml);
            Matcher executionGroup = Value.PatternValue.EXECUTION.matcher(loginHtml);
            if (ltGroup.find()) {
                lt = ltGroup.group(1);
                rsa = stuId + stuPass + lt;
            }
            if (executionGroup.find()) {
                execution = executionGroup.group(1);
            }
            RequestBody body = new FormBody.Builder()
                    .add("rsa", rsa)
                    .add("ul", String.valueOf(stuId.length()))
                    .add("pl", String.valueOf(stuPass.length()))
                    .add("lt", lt)
                    .add("execution", execution)
                    .add("_eventId", "submit")
                    .build();
            return body;
        }
    }
}
