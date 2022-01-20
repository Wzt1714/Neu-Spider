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
 * <h1>ExamData-用于存储个人的考试信息</h1>
 * <p>定义了静态内部类ChildExam用于存储单场考试数据，在ExamData中定义了ChildExam列表用于存储所有考试数据</p>
 * <p>此类与其内部类均实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 * @see ChildExam
 */
public class ExamData implements AuroraData {
    /**
     * 将json格式的字符串转化为ExamData对象
     *
     * @param json 传入的json字符串
     * @return 返回的ExamData对象
     * @see ExamData
     */
    public static ExamData inverse(String json) {
        JSONArray array = JSONArray.fromObject(json);
        List<ExamData.ChildExam> examList = new ArrayList<>();
        for (Object o : array) {
            String courseName = ((JSONObject) o).getString("courseName");
            String examType = ((JSONObject) o).getString("examType");
            String examDate = ((JSONObject) o).getString("examDate");
            String examTime = ((JSONObject) o).getString("examTime");
            String examPlace = ((JSONObject) o).getString("examPlace");
            String examSeatId = ((JSONObject) o).getString("examSeatId");
            String examStatus = ((JSONObject) o).getString("examStatus");
            ChildExam exam = new ChildExam(courseName, examType, examDate, examTime, examPlace, examSeatId, examStatus);
            examList.add(exam);
        }
        return new ExamData(examList);
    }

    /**
     * <h1>ChildExam-用于存储单场考试数据</h1>
     * <p>同样实现了AuroraData接口，为ExamData的静态内部类</p>
     * <p>需要注意的是，尽管此内部类也实现了AuroraData接口，但是没有实现静态方法inverse()，将此方法的实现放于外部类，
     * 当需要进行json数据转化为ExamData数据时，只需要调用ExamData.inverse()即可</p>
     *
     * @see AuroraData
     * @see ExamData
     */
    public static class ChildExam implements AuroraData {
        /**
         * <h3>课程名</h3>
         */
        private String courseName;
        /**
         * <h3>考试类型</h3>
         * <p>例：期末考试</p>
         */
        private String examType;
        /**
         * <h3>考试日期</h3>
         * <p>例：2021-12-26</p>
         */
        private String examDate;
        /**
         * <h3>考试时间</h3>
         * <p>例：09:30~10:10</p>
         */
        private String examTime;
        /**
         * <h3>考试地点</h3>
         */
        private String examPlace;
        /**
         * <h3>座位号</h3>
         */
        private String examSeatId;
        /**
         * <h3>考试状态</h3>
         * <p>一般均为’正常‘</p>
         */
        private String examStatus;

        @Override
        public String toJson() {
            JSONObject object = new JSONObject();
            object.put("courseName", courseName);
            object.put("examType", examType);
            object.put("examDate", examDate);
            object.put("examTime", examTime);
            object.put("examPlace", examPlace);
            object.put("examSeatId", examSeatId);
            object.put("examStatus", examStatus);
            return object.toString();
        }

        /**
         * ChildExam构造器，用于创建单场考试数据
         *
         * @param courseName 课程名
         * @param examType   考试类型
         * @param examDate   考试日期
         * @param examTime   考试时间
         * @param examPlace  考试地点
         * @param examSeatId 考试座位号
         * @param examStatus 考试状态
         */
        public ChildExam(String courseName, String examType, String examDate, String examTime, String examPlace, String examSeatId, String examStatus) {
            this.courseName = courseName;
            this.examType = examType;
            this.examDate = examDate;
            this.examTime = examTime;
            this.examPlace = examPlace;
            this.examSeatId = examSeatId;
            this.examStatus = examStatus;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getExamType() {
            return examType;
        }

        public String getExamDate() {
            return examDate;
        }

        public String getExamTime() {
            return examTime;
        }

        public String getExamPlace() {
            return examPlace;
        }

        public String getExamSeatId() {
            return examSeatId;
        }

        public String getExamStatus() {
            return examStatus;
        }


    }

    /**
     * <h3>用于存储所有考试数据</h3>
     */
    private List<ChildExam> examList = new ArrayList<>();

    /**
     * ExamData构造器，用于创建所有考试数据
     *
     * @param examData 传入的考试列表
     */
    public ExamData(List<ChildExam> examData) {
        this.examList = examData;
    }

    public List<ChildExam> getExamList() {
        return examList;
    }

    @Override
    public String toJson() {
        JSONArray array = new JSONArray();
        for (ChildExam exam : examList) {
            array.add(exam.toJson());
        }
        return array.toString();
    }
}
