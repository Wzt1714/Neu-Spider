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
import java.util.function.Consumer;

/**
 * <h1>RoomListData-用于存储可用教室信息</h1>
 * <p>此类实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 */
public class RoomListData implements AuroraData {
    /**
     * <h3>可用教室列表</h3>
     * <p>每一元素存储教室编号</p>
     */
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    /**
     * RoomListData构造器，用于创建RoomListData数据
     *
     * @param list 可用教室列表
     */
    public RoomListData(List<String> list) {
        this.list = list;
    }

    @Override
    public String toJson() {
        JSONArray array = new JSONArray();
        for (String code : list) {
            JSONObject object = new JSONObject();
            object.put("code", code);
            array.add(object);
        }
        return array.toString();
    }

    /**
     * 将json字符串转换为RoomListData数据
     *
     * @param json json字符串
     * @return RoomListData对象
     */
    public static RoomListData inverse(String json) {
        try {
            ArrayList<String> list = new ArrayList<>();
            JSONArray array = JSONArray.fromObject(json);
            array.forEach(o -> {
                list.add(((JSONObject) o).getString("code"));
            });
            return new RoomListData(list);
        } catch (Exception e) {
            return null;
        }
    }
}
