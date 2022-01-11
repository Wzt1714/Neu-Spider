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

package com.wzt.aurora.spider.data;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>GpaData-用于存储个人的绩点信息</h1>
 * <p>定义了静态内部类ChildGpa用于存储单个课程的绩点数据，在GpaData中定义了ChildGpa列表和总平均绩点用于存储所有课程绩点数据和总绩点</p>
 * <p>此类与其内部类均实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see GpaData
 * @see ChildGpa
 */
public class GpaData implements AuroraData {
    /**
     * 将json格式的字符串转化为GpaData对象
     *
     * @param json 传入的json字符串
     * @return 返回的GpaData对象
     * @see GpaData
     */
    public static GpaData inverse(String json) {
        JSONObject object = JSONObject.fromObject(json);
        String gpaSum = object.getString("gpaSum");
        List<GpaData.ChildGpa> gpaList = new ArrayList<>();
        JSONArray array = object.getJSONArray("list");
        for (Object o : array) {
            String courseName = ((JSONObject) o).getString("courseName");
            String courseType = ((JSONObject) o).getString("courseType");
            String courseCredit = ((JSONObject) o).getString("courseCredit");
            String usualResults = ((JSONObject) o).getString("usualResults");
            String midTermResults = ((JSONObject) o).getString("midTermResults");
            String endTermResults = ((JSONObject) o).getString("endTermResults");
            String generalResults = ((JSONObject) o).getString("generalResults");
            String GPA = ((JSONObject) o).getString("GPA");
            ChildGpa childGpa = new ChildGpa(courseName, courseType, courseCredit, usualResults, midTermResults, endTermResults, generalResults, GPA);
            gpaList.add(childGpa);
        }
        return new GpaData(gpaSum, gpaList);
    }
    /**
     * <h1>ChildGpa-用于存储单门课程绩点数据</h1>
     * <p>同样实现了AuroraData接口，为GpaData的静态内部类</p>
     * <p>需要注意的是，尽管此内部类也实现了AuroraData接口，但是没有实现静态方法inverse()，将此方法的实现放于外部类，
     * 当需要进行json数据转化为GpaData数据时，只需要调用GpaData.inverse()即可</p>
     *
     * @see AuroraData
     * @see GpaData
     */
    public static class ChildGpa implements AuroraData {
        /**
         * <h3>课程名</h3>
         */
        private String courseName;
        /**
         * <h3>课程类型</h3>
         * <p>选修或者必修</p>
         */
        private String courseType;
        /**
         * <h3>课程所占学分</h3>
         */
        private String courseCredit;
        /**
         * <h3>平时成绩</h3>
         */
        private String usualResults;
        /**
         * <h3>期中成绩</h3>
         */
        private String midTermResults;
        /**
         * <h3>期末成绩</h3>
         */
        private String endTermResults;
        /**
         * <h3>总评成绩</h3>
         */
        private String generalResults;
        /**
         * <h3>单门绩点</h3>
         */
        private String GPA;

        @Override
        public String toJson() {
            JSONObject object = new JSONObject();
            object.put("courseName", courseName);
            object.put("courseType", courseType);
            object.put("courseCredit", courseCredit);
            object.put("usualResults", usualResults);
            object.put("midTermResults", midTermResults);
            object.put("endTermResults", endTermResults);
            object.put("generalResults", generalResults);
            object.put("GPA", GPA);
            return object.toString();
        }

        /**
         * ChildGpa构造器，用于创建单门绩点对象
         *
         * @param courseName 课程名
         * @param courseType 课程类型
         * @param courseCredit 课程学分
         * @param usualResults 平时成绩
         * @param midTermResults 期中成绩
         * @param endTermResults 期末成绩
         * @param generalResults 总评成绩
         * @param GPA 单门绩点
         */
        public ChildGpa(String courseName, String courseType, String courseCredit, String usualResults, String midTermResults, String endTermResults, String generalResults, String GPA) {
            this.courseName = courseName;
            this.courseType = courseType;
            this.courseCredit = courseCredit;
            this.usualResults = usualResults;
            this.midTermResults = midTermResults;
            this.endTermResults = endTermResults;
            this.generalResults = generalResults;
            this.GPA = GPA;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getCourseType() {
            return courseType;
        }

        public String getCourseCredit() {
            return courseCredit;
        }

        public String getUsualResults() {
            return usualResults;
        }

        public String getMidTermResults() {
            return midTermResults;
        }

        public String getEndTermResults() {
            return endTermResults;
        }

        public String getGeneralResults() {
            return generalResults;
        }

        public String getGPA() {
            return GPA;
        }
    }

    /**
     * <h3>总平均绩点</h3>
     */
    private String gpaSum;
    /**
     * <h3>课程绩点列表</h3>
     */
    private List<ChildGpa> gpaList = new ArrayList<>();

    /**
     * GpaData构造器，用于创建绩点对象
     * @param gpaSum 总平均绩点
     * @param gpaList 单门绩点列表
     */
    public GpaData(String gpaSum, List<ChildGpa> gpaList) {
        this.gpaSum = gpaSum;
        this.gpaList = gpaList;
    }

    public String getGpaSum() {
        return gpaSum;
    }

    public List<ChildGpa> getGpaList() {
        return gpaList;
    }

    @Override
    public String toJson() {
        JSONObject object = new JSONObject();
        object.put("gpaSum", gpaSum);
        JSONArray array = new JSONArray();
        for (ChildGpa gpa : gpaList) {
            array.add(gpa.toJson());
        }
        object.put("list", array);
        return object.toString();
    }
}
