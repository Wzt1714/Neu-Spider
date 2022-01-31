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
 * <h1>CardData-用于存储校园卡（余额）信息</h1>
 * <p>此类实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 */
public class CardData implements AuroraData {
    /**
     * <h3>校园卡余额</h3>
     */
    private String balance;

    /**
     * CardData构造器，用于创建校园卡信息数据
     *
     * @param balance 校园卡余额
     */
    public CardData(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    @Override
    public String toJson() {
        JSONObject object = new JSONObject();
        object.put("balance", balance);
        return object.toString();
    }

    /**
     * 将json格式的字符串转化为CardData对象
     *
     * @param json 传入的json字符串
     * @return 返回的CardData对象
     * @see CardData
     */
    public static CardData inverse(String json) {
        try {
            JSONObject object = JSONObject.fromObject(json);
            String balance = object.getString("balance");
            return new CardData(balance);
        } catch (Exception e) {
            return null;
        }
    }
}
