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

import com.wzt.aurora.spider.utils.Utils;
import com.wzt.aurora.spider.utils.Value;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * <h1>NetEnvironmentRequest-进行网络环境查询的数据请求</h1>
 * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
 *
 * @see com.wzt.aurora.spider.handle.NeuHandle
 * @see com.wzt.aurora.spider.handle.VpnHandle
 * @see com.wzt.aurora.spider.handle.RoomHandle
 * @see com.wzt.aurora.spider.handle.AutoHandle
 */
public class NetEnvironmentRequest {
    /**
     * <h3>构建必要数据的请求</h3>
     */
    private Client client = Client.getInstance(Value.ClientCode.ROOM_CLIENT);
    /**
     * <h3>OkHttpClient对象</h3>
     */
    private OkHttpClient okHttpClient = client.getOkHttpClient();
    /**
     * 查询网络请求是否为校园网
     */
    public Boolean isNEUNet(){
        Request request = Utils.ClientUtils.fastBuildRequest("https://webvpn.neu.edu.cn/");
        try (Response response = okHttpClient.newCall(request).execute()){
            if (response.code() == 302)
                return false;
            else if (response.code() == 403)
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
