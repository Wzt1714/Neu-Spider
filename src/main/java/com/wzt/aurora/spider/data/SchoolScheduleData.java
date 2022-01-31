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
import net.sf.json.JSONObject;

/**
 * <h1>SchoolScheduleData-用于存储校历信息</h1>
 * <p>此类实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 */
public class SchoolScheduleData implements AuroraData {
    /**
     * <h3>存储校历图片的二进制数据</h3>
     */
    private byte[] image;

    /**
     * SchoolScheduleData构造器，用以生成SchoolScheduleData
     *
     * @param image 二进制数据
     */
    public SchoolScheduleData(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public String toJson() {
        JSONObject object = new JSONObject();
        object.put("image", Utils.StandardUtils.byte2Base64StringFun(image));
        return object.toString();
    }

    /**
     * 将json字符串转化为SchoolScheduleData数据
     *
     * @param json json字符串
     * @return SchoolScheduleData对象
     */
    public static SchoolScheduleData inverse(String json) {
        try {
            JSONObject object = JSONObject.fromObject(json);
            return new SchoolScheduleData(Utils.StandardUtils.base64String2ByteFun(object.getString("image")));
        } catch (Exception e) {
            return null;
        }
    }
}
