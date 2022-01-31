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
 * <h1>CourseData-用于存储个人的课表信息</h1>
 * <p>定义了静态内部类ChildCourse用于存储单个课程数据，在CourseData中定义了ChildCourse列表用于存储所有书籍数据</p>
 * <p>此类与其内部类均实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 * @see ChildCourse
 */
public class CourseData implements AuroraData {
    /**
     * <h3>用于存储所有课程数据</h3>
     */
    public List<ChildCourse> courseList = new ArrayList<>();

    /**
     * CourseData构造器，用于创建所有课程信息数据
     *
     * @param courseList 传入的课程列表
     * @see ChildCourse
     */
    public CourseData(List<ChildCourse> courseList) {
        this.courseList = courseList;
    }

    public List<ChildCourse> getCourseList() {
        return courseList;
    }

    @Override
    public String toJson() {
        JSONArray array = new JSONArray();
        for (ChildCourse childCourse : courseList) {
            array.add(childCourse.toJson());
        }
        return array.toString();
    }

    /**
     * 将json格式的字符串转化为CourseData对象
     *
     * @param json 传入的json字符串
     * @return 返回的CourseData对象
     * @see CourseData
     */
    public static CourseData inverse(String json) {
        try {
            JSONArray array = JSONArray.fromObject(json);
            List<CourseData.ChildCourse> courseList = new ArrayList<>();
            for (Object o : array) {
                String courseName = ((JSONObject) o).getString("courseName");
                String coursePlace = ((JSONObject) o).getString("coursePlace");
                String teacherName = ((JSONObject) o).getString("teacherName");
                String timeData = ((JSONObject) o).getString("timeData");
                String dayData = ((JSONObject) o).getString("dayData");
                String weekData = ((JSONObject) o).getString("weekData");
                CourseData.ChildCourse childCourse = new ChildCourse(courseName, coursePlace, teacherName, timeData, dayData, weekData);
                courseList.add(childCourse);
            }
            return new CourseData(courseList);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <h1>ChildCourse-用于存储单个课程数据</h1>
     * <p>同样实现了AuroraData接口，为CourseData的静态内部类</p>
     * <p>需要注意的是，尽管此内部类也实现了AuroraData接口，但是没有实现静态方法inverse()，将此方法的实现放于外部类，
     * 当需要进行json数据转化为CourseData数据时，只需要调用CourseData.inverse()即可</p>
     *
     * @see AuroraData
     * @see CourseData
     */
    public static class ChildCourse implements AuroraData {
        /**
         * <h3>课程名</h3>
         */
        private String courseName;
        /**
         * <h3>上课地点</h3>
         */
        private String coursePlace;
        /**
         * <h3>教师名</h3>
         */
        private String teacherName;
        /**
         * <h3>上课节次数据</h3>
         * <p>由长度为12的字符串存储，第几节有课则将第几位设为1，否则为0</p>
         * <p>例：第3、4节有课，其他时间段均无课，则有：</p>
         * <p>001100000000</p>
         */
        private String timeData;
        /**
         * <h3>课程位于星期几</h3>
         * <p>直接由中文存储</p>
         * <p>例：星期一有课，则有：</p>
         * <p>星期一</p>
         */
        private String dayData;
        /**
         * <h3>上课周数据</h3>
         * <p>由长度为52的字符串存储，第几周有课则将第几位设为1，否则为0</p>
         * <p>例：第10、14、16、17周有课，其他周无课，则有：</p>
         * <p>0000000001000101100000000000000000000000000000000000</p>
         */
        private String weekData;

        /**
         * ChildCourse构造器，用于创建单个课程数据
         *
         * @param courseName  课程名
         * @param coursePlace 上课地点
         * @param teacherName 教师名
         * @param timeData    节次数据
         * @param dayData     星期几
         * @param weekData    上课周数据
         */
        public ChildCourse(String courseName, String coursePlace, String teacherName, String timeData, String dayData, String weekData) {
            this.courseName = courseName;
            this.coursePlace = coursePlace;
            this.teacherName = teacherName;
            this.timeData = timeData;
            this.dayData = dayData;
            this.weekData = weekData;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getCoursePlace() {
            return coursePlace;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public String getTimeData() {
            return timeData;
        }

        public String getDayData() {
            return dayData;
        }

        public String getWeekData() {
            return weekData;
        }

        @Override
        public String toJson() {
            JSONObject object = new JSONObject();
            object.put("courseName", courseName);
            object.put("coursePlace", coursePlace);
            object.put("teacherName", teacherName);
            object.put("timeData", timeData);
            object.put("dayData", dayData);
            object.put("weekData", weekData);
            return object.toString();
        }
    }
}
