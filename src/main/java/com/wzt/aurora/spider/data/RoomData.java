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

import com.wzt.aurora.spider.utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>RoomData-用于存储教室占用信息</h1>
 * <p>定义了静态内部类ChildRoom用于存储单间教室数据，在RoomData中定义了ChildRoom列表用于存储所有教室数据</p>
 * <p>此类与其内部类均实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 * @see ChildRoom
 */
public class RoomData implements AuroraData {
    /**
     * 将json格式的字符串转化为RoomData对象
     *
     * @param json 传入的json字符串
     * @return 返回的BookData对象
     * @see RoomData
     */
    public static RoomData inverse(String json) {
        List<ChildRoom> roomList = new ArrayList<>();
        JSONArray array = JSONArray.fromObject(json);
        for (Object o : array) {
            String courseName = ((JSONObject) o).getString("courseName");
            String courseClass = ((JSONObject) o).getString("courseClass");
            int[] courseWeek = Utils.StandardUtils.str2array(((JSONObject) o).getString("courseWeek"));
            String courseDay = ((JSONObject) o).getString("courseDay");
            int[] courseTime = Utils.StandardUtils.str2array(((JSONObject) o).getString("courseTime"));
            String courseTeacher = ((JSONObject) o).getString("courseTeacher");
            String coursePlace = ((JSONObject) o).getString("coursePlace");
            ChildRoom room = new ChildRoom(courseName, courseClass, courseWeek, courseDay, courseTime, courseTeacher, coursePlace);
            roomList.add(room);
        }
        return new RoomData(roomList);
    }

    /**
     * <h1>ChildRoom-用于存储单间教室数据</h1>
     * <p>同样实现了AuroraData接口，为RoomData的静态内部类</p>
     * <p>需要注意的是，尽管此内部类也实现了AuroraData接口，但是没有实现静态方法inverse()，将此方法的实现放于外部类，
     * 当需要进行json数据转化为RoomData数据时，只需要调用RoomData.inverse()即可</p>
     *
     * @see AuroraData
     * @see RoomData
     */
    public static class ChildRoom implements AuroraData {
        /**
         * <h3>课程名</h3>
         */
        private String courseName;
        /**
         * <h3>上课班级</h3>
         */
        private String courseClass;
        /**
         * <h3>上课周</h3>
         * <p>int数组索引为i的值代表第i+1周的数据，1为有课，0为无课</p>
         */
        private int[] courseWeek;
        /**
         * <h3>上课天</h3>
         * <p>在这里指的是星期几</p>
         */
        private String courseDay;
        /**
         * <h3>上课节次</h3>
         * <p>int数组索引为i的值代表第i+1节的数据，1为有课，0为无课</p>
         */
        private int[] courseTime;
        /**
         * <h3>授课教师</h3>
         */
        private String courseTeacher;
        /**
         * <h3>上课地点</h3>
         */
        private String coursePlace;

        @Override
        public String toJson() {
            JSONObject object = new JSONObject();
            object.put("courseName", courseName);
            object.put("courseClass", courseClass);
            object.put("courseWeek", Utils.StandardUtils.array2str(courseWeek));
            object.put("courseDay", courseDay);
            object.put("courseTime", Utils.StandardUtils.array2str(courseTime));
            object.put("courseTeacher", courseTeacher);
            object.put("coursePlace", coursePlace);
            return object.toString();
        }

        /**
         * ChildRoom构造器，用于创建单间教室数据
         *
         * @param courseName    课程名
         * @param courseClass   上课班级
         * @param courseWeek    上课周
         * @param courseDay     上课天
         * @param courseTime    上课节次
         * @param courseTeacher 授课教师
         * @param coursePlace   上课地点
         */
        public ChildRoom(String courseName, String courseClass, int[] courseWeek, String courseDay, int[] courseTime, String courseTeacher, String coursePlace) {
            this.courseClass = courseClass;
            this.courseName = courseName;
            this.courseWeek = courseWeek;
            this.courseDay = courseDay;
            this.courseTime = courseTime;
            this.coursePlace = coursePlace;
            this.courseTeacher = courseTeacher;
        }


        public String getCourseName() {
            return courseName;
        }

        public int[] getCourseTime() {
            return courseTime;
        }

        public int[] getCourseWeek() {
            return courseWeek;
        }

        public String getCourseClass() {
            return courseClass;
        }

        public String getCourseDay() {
            return courseDay;
        }

        public String getCoursePlace() {
            return coursePlace;
        }

        public String getCourseTeacher() {
            return courseTeacher;
        }
    }

    /**
     * <h3>所有教室数据</h3>
     */
    private List<ChildRoom> childRoomList = new ArrayList<>();

    /**
     * RoomData构造器，用于创建所有教室上课数据
     *
     * @param childRoomList 单间教室数据列表
     */
    public RoomData(List<ChildRoom> childRoomList) {
        this.childRoomList = childRoomList;
    }

    public List<ChildRoom> getChildRoomList() {
        return childRoomList;
    }

    @Override
    public String toJson() {
        JSONArray array = new JSONArray();
        for (ChildRoom childRoom : childRoomList) {
            array.add(childRoom.toJson());
        }
        return array.toString();
    }

}
