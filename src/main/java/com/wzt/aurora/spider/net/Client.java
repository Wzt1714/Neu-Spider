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
 * <h1>Client-实现OkHttpClient的创建和封装</h1>
 * <p>使用单例模式</p>
 * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
 *
 * @see com.wzt.aurora.spider.handle.NeuHandle
 * @see com.wzt.aurora.spider.handle.VpnHandle
 * @see com.wzt.aurora.spider.handle.RoomHandle
 * @see com.wzt.aurora.spider.handle.AutoHandle
 */
public class Client {
    /**
     * <h3>使用懒汉式创建</h3>
     */
    private static Client client = null;
    /**
     * <h3>OkHttpClient对象</h3>
     */
    private OkHttpClient okHttpClient = null;
    /**
     * <h3>何种请求对象</h3>
     */
    private Value.ClientCode typeCode = Value.ClientCode.NULL_CLIENT;

    /**
     * Client的构造函数，通过指定参数创建对象,设置为private禁止外部调用
     * <p>参数必须为Value.ClientCodeValue里的值</p>
     *
     * @param typeCode 参数
     * @see Value.ClientCode
     */
    private Client(Value.ClientCode typeCode) {
        this.typeCode = typeCode;
        switch (typeCode) {
            case NEU_E_ONE_CLIENT:
                okHttpClient = new OkHttpClient.Builder()
                        .followRedirects(false)
                        .cookieJar(Cookies.NeuEOneCookies.getInstance())
                        .build();
                break;
            case NEU_DEAN_CLIENT:
                okHttpClient = new OkHttpClient.Builder()
                        .followRedirects(false)
                        .cookieJar(Cookies.NeuDeanCookies.getInstance())
                        .build();
                break;
            case ROOM_CLIENT:
            case NET_ENVIRONMENT_CLIENT:
                okHttpClient = new OkHttpClient.Builder()
                        .followRedirects(false)
                        .build();
                break;
            case VPN_E_ONE_CLIENT:
                okHttpClient = new OkHttpClient.Builder()
                        .followRedirects(false)
                        .cookieJar(Cookies.VpnCookies.getInstance())
                        .build();
                break;
        }
    }

    /**
     * 获取Client实例的方法
     * <p>每次调用都会按照参数重新创建对象</p>
     * <p>参数必须为Value.ClientCodeValue里的值</p>
     *
     * @param code 参数
     * @return Client实例
     * @see Value.ClientCode
     */
    public static Client getInstance(Value.ClientCode code) {
        client = new Client(code);
        return client;
    }

    /**
     * 获取Client实例的方法
     * <p>与有参方法不同的是，此方法会获得上一个创建的Client对象</p>
     *
     * @return Client实例
     */
    public static Client getInstance() {
        return client;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 进行快速GET请求
     *
     * @param url 请求url
     * @return 请求体字符串
     */
    public String fastGet(String url) {
        Request request = new Request.Builder()
                .url(url)
                .headers(Value.RequestHeadsValue.DEFAULT_HEADER)
                .get()
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            Utils.ClientUtils.printClientData(response);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
