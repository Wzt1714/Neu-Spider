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

package com.wzt.aurora.spider.net;

import okhttp3.OkHttpClient;
import com.wzt.aurora.spider.utils.Value;

/**
 * <h1>RoomRequest-进行必要的数据请求</h1>
 * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
 *
 * @see com.wzt.aurora.spider.handle.NeuHandle
 * @see com.wzt.aurora.spider.handle.VpnHandle
 * @see com.wzt.aurora.spider.handle.RoomHandle
 */
public class RoomRequest {
    /**
     * <h3>构建必要数据的请求</h3>
     */
    private Client client = Client.getInstance(Value.ClientCodeValue.ROOM_CLIENT);
    /**
     * <h3>OkHttpClient对象</h3>
     */
    private OkHttpClient okHttpClient = client.getOkHttpClient();

    /**
     * 获得指定教学楼的教室占用信息的json数据
     * @param place 必须为以下字符串中的值：
     * <p>"教","逸","大成","采","机"</p>
     * @return json字符串
     */
    public String roomJson(String place) {
        return client.fastGet("http://117.50.172.22:8080/NeuAuroraWebApi/course?place=" + place);
    }

    /**
     * 获得时间信息的json数据
     * @return json字符串
     */
    public String dateJson() {
        return client.fastGet("http://117.50.172.22:8080/NeuAuroraWebApi/date");
    }

    /**
     * 获得学期信息的json数据
     * @return json字符串
     */
    public String semesterJson() {
        return client.fastGet("http://117.50.172.22:8080/NeuAuroraWebApi/semester");
    }
}
