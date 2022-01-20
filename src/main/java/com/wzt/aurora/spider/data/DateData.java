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
 * <h1>DateData-用于存储当前时间的时间数据</h1>
 * <p>此类实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 */
public class DateData implements AuroraData {
    /**
     * <h3>本学期开始年</h3>
     */
    private int startYear;
    /**
     * <h3>本学期开始月</h3>
     */
    private int startMonth;
    /**
     * <h3>本学期开始日</h3>
     */
    private int startDay;
    /**
     * <h3>当前周数</h3>
     */
    private int nowWeekNum;
    /**
     * <h3>当前星期几</h3>
     */
    private String nowDayNum;
    /**
     * <h3>当前学期id</h3>
     */
    private int nowSemesterId;

    /**
     * 将json格式的字符串转化为BookData对象
     *
     * @param json 传入的json字符串
     * @return 返回的DateData对象
     * @see DateData
     */
    public static DateData inverse(String json) {
        JSONObject object = JSONObject.fromObject(json);
        int startYear = object.getInt("startYear");
        int startMonth = object.getInt("startMonth");
        int startDay = object.getInt("startDay");
        int nowWeekNum = object.getInt("nowWeekNum");
        String nowDayNum = object.getString("nowDayNum");
        int nowSemesterId = object.getInt("nowSemesterId");
        return new DateData(startYear, startMonth, startDay, nowWeekNum, nowDayNum, nowSemesterId);
    }

    @Override
    public String toJson() {
        JSONObject object = new JSONObject();
        object.put("startYear", startYear);
        object.put("startMonth", startMonth);
        object.put("startDay", startDay);
        object.put("nowWeekNum", nowWeekNum);
        object.put("nowDayNum", nowDayNum);
        object.put("nowSemesterId", nowSemesterId);
        return object.toString();
    }

    /**
     * DateData构造器，用于创建日期数据
     *
     * @param startYear     学期开始年
     * @param startMonth    学期开始月
     * @param startDay      学期开始天
     * @param nowWeekNum    当前周数
     * @param nowDayNum     当前星期几
     * @param nowSemesterId 当前学期id
     */
    public DateData(int startYear, int startMonth, int startDay, int nowWeekNum, String nowDayNum, int nowSemesterId) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.nowWeekNum = nowWeekNum;
        this.nowDayNum = nowDayNum;
        this.nowSemesterId = nowSemesterId;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getNowWeekNum() {
        return nowWeekNum;
    }

    public String getNowDayNum() {
        return nowDayNum;
    }

    public int getNowSemesterId() {
        return nowSemesterId;
    }
}
