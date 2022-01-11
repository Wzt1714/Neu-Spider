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
 * <h1>SemesterData-用于存储学期信息</h1>
 * <p>定义了静态内部类ChildSemester用于存储单个学期数据，在SemesterData中定义了ChildSemester列表用于存储所有学期数据</p>
 * <p>此类与其内部类均实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 * @see ChildSemester
 */
public class SemesterData implements AuroraData {
    /**
     * 将json格式的字符串转化为BookData对象
     *
     * @param json 传入的json字符串
     * @return 返回的SemesterData对象
     * @see SemesterData
     */
    public static SemesterData inverse(String json) {
        JSONArray array = JSONArray.fromObject(json);
        List<ChildSemester> semesterList = new ArrayList<>();
        for (Object o : array) {
            String semesterName = ((JSONObject) o).getString("semesterName");
            String semesterId = ((JSONObject) o).getString("semesterId");
            ChildSemester childSemester = new ChildSemester(semesterName, semesterId);
            semesterList.add(childSemester);
        }
        return new SemesterData(semesterList);
    }
    /**
     * <h1>ChildSemester-用于存储单个学期数据</h1>
     * <p>同样实现了AuroraData接口，为SemesterData的静态内部类</p>
     * <p>需要注意的是，尽管此内部类也实现了AuroraData接口，但是没有实现静态方法inverse()，将此方法的实现放于外部类，
     * 当需要进行json数据转化为SemesterData数据时，只需要调用BookData.inverse()即可</p>
     *
     * @see AuroraData
     * @see SemesterData
     */
    public static class ChildSemester implements AuroraData {
        /**
         * <h3>学期名</h3>
         */
        private String semesterName;
        /**
         * <h3>学期id</h3>
         */
        private String semesterId;

        /**
         * ChildSemester构造器，用于创建单个学期数据
         *
         * @param semesterName 学期名
         * @param semesterId 学期id
         */
        public ChildSemester(String semesterName, String semesterId) {
            this.semesterName = semesterName;
            this.semesterId = semesterId;
        }

        public String getSemesterName() {
            return semesterName;
        }

        public String getSemesterId() {
            return semesterId;
        }

        @Override
        public String toJson() {
            JSONObject object = new JSONObject();
            object.put("semesterName", semesterName);
            object.put("semesterId", semesterId);
            return object.toString();
        }
    }

    /**
     * <h3>学期数据列表</h3>
     */
    private List<ChildSemester> semesterList = new ArrayList<>();

    /**
     * SemesterData构造器，用于创建SemesterData对象
     *
     * @param semesterList 学期数据列表
     */
    public SemesterData(List<ChildSemester> semesterList) {
        this.semesterList = semesterList;
    }

    public List<ChildSemester> getSemesterList() {
        return semesterList;
    }

    @Override
    public String toJson() {
        JSONArray array = new JSONArray();
        for (ChildSemester semester : semesterList) {
            array.add(semester.toJson());
        }
        return array.toString();
    }
}
