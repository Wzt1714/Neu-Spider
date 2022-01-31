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

import net.sf.json.JSONObject;

/**
 * <h1>StudentData-用于存储个人信息</h1>
 * <p>此类实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 */
public class StudentData implements AuroraData {
    /**
     * 将json格式的字符串转化为StudentData对象
     *
     * @param json 传入的json字符串
     * @return 返回的StudentData对象
     * @see StudentData
     */
    public static StudentData inverse(String json) {
        try {
            JSONObject object = JSONObject.fromObject(json);
            String academicDegree = object.getString("academicDegree");
            String college = object.getString("college");
            String name = object.getString("name");
            return new StudentData(academicDegree, college, name);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <h3>学位</h3>
     * <p>例：本科生</p>
     */
    private String academicDegree;
    /**
     * <h3>学院名</h3>
     */
    private String college;
    /**
     * <h3>学生姓名</h3>
     */
    private String name;

    /**
     * StudentData构造器，用于创建学生数据
     *
     * @param academicDegree 学位
     * @param college        学院
     * @param name           姓名
     */
    public StudentData(String academicDegree, String college, String name) {
        this.academicDegree = academicDegree;
        this.college = college;
        this.name = name;
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public String getCollege() {
        return college;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toJson() {
        JSONObject object = new JSONObject();
        object.put("academicDegree", academicDegree);
        object.put("college", college);
        object.put("name", name);
        return object.toString();
    }
}
