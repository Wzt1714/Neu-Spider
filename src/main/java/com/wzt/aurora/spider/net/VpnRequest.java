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
import okhttp3.*;

import java.io.IOException;
import java.util.regex.Matcher;

/**
 * <h1>VpnRequest-进行非校园网环境下的数据请求</h1>
 * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
 *
 * @see com.wzt.aurora.spider.handle.NeuHandle
 * @see com.wzt.aurora.spider.handle.VpnHandle
 * @see com.wzt.aurora.spider.handle.RoomHandle
 */
public class VpnRequest {
    /**
     * <h3>学号</h3>
     */
    private String id;
    /**
     * <h3>密码</h3>
     */
    private String pass;
    /**
     * <h3>学期id</h3>
     */
    private String semesterId;
    /**
     * <h3>是否登录</h3>
     */
    private boolean isLogin = false;
    /**
     * <h3>构建非校园网下Vpn的请求</h3>
     */
    private Client client = Client.getInstance(Value.ClientCodeValue.VPN_E_ONE_CLIENT);
    /**
     * <h3>获得OkHttpClient对象</h3>
     */
    private OkHttpClient okHttpClient = client.getOkHttpClient();

    public boolean isLogin() {
        return isLogin;
    }

    /**
     * VpnRequest构造器，创建VpnRequest对象并登录WebVpn
     *
     * @param id         学号
     * @param pass       密码
     * @param semesterId 学期id
     */
    public VpnRequest(String id, String pass, String semesterId) {
        this.id = id;
        this.pass = pass;
        this.semesterId = semesterId;
        login();
    }

    /**
     * 登录操作
     */
    private void login() {
        String location = "";
        String loginHtml = "";

        Request request = Utils.ClientUtils.fastBuildRequest("https://webvpn.neu.edu.cn/");
        try (Response response = okHttpClient.newCall(request).execute()) {
            Utils.ClientUtils.printClientData(response);
            location = response.header("Location");
        } catch (IOException e) {
            e.printStackTrace();
        }

        request = Utils.ClientUtils.fastBuildRequest(location);
        try (Response response = okHttpClient.newCall(request).execute()) {
            Utils.ClientUtils.printClientData(response);
            location = response.header("Location");
        } catch (IOException e) {
            e.printStackTrace();
        }

        loginHtml = client.fastGet(location);
        RequestBody body = Utils.ClientUtils.fastBuildLoginBody(loginHtml, id, pass);
        String jsessionid_tpass = "";
        for (Cookie cookie : Cookies.VpnCookies.getInstance().getCookieStore().get("pass.neu.edu.cn")) {
            if (cookie.name().equals("jsessionid_tpass"))
                jsessionid_tpass = cookie.value();
        }
        request = Utils.ClientUtils.fastBuildRequest("https://pass.neu.edu.cn/tpass/login;jsessionid_tpass=" + jsessionid_tpass + "?service=https%3A%2F%2Fwebvpn.neu.edu.cn%2Flogin%3Fcas_login%3Dtrue", body);
        try (Response response = okHttpClient.newCall(request).execute()) {
            Utils.ClientUtils.printClientData(response);
            if (response.code() >= 300 && response.code() <= 400)
                isLogin = true;
            else {
                isLogin = false;
                return;
            }
            location = response.header("Location");
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.fastGet(location);

//        client.fastGet("https://webvpn.neu.edu.cn/");
    }

    /**
     * <h1>EOneRequest-进行非校园网环境下对一网通网页的请求</h1>
     * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
     *
     * @see com.wzt.aurora.spider.handle.NeuHandle
     * @see com.wzt.aurora.spider.handle.VpnHandle
     * @see com.wzt.aurora.spider.handle.RoomHandle
     */
    public class EOneRequest {
        /**
         * <h3>一网通是否登录</h3>
         */
        private boolean isLogin = false;

        public boolean isLogin() {
            return isLogin;
        }

        /**
         * 获取图书借阅页面的html文件
         *
         * @return html字符串
         */
        public String libraryHtml() {
            String location = "";
            Request request = Utils.ClientUtils.fastBuildRequest("https://webvpn.neu.edu.cn/https/77726476706e69737468656265737421e0f85388263c265e7b1dc7a99c406d369a/tp_up/up/subgroup/gotoLibirarySystem");
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = Utils.ClientUtils.fastBuildRequest(location);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = Utils.ClientUtils.fastBuildRequest(location);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = Utils.ClientUtils.fastBuildRequest(location);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            String html = client.fastGet(location);
            Matcher matcher = Value.PatternValue.BORROW_BOOK_URL_VPN.matcher(html);
            if (matcher.find()) {
                html = client.fastGet(matcher.group(1).replace("&amp;", "&"));
            }
            return html;
        }

        /**
         * 获得学生信息的json数据
         *
         * @return json字符串
         */
        public String infoJson() {
            return client.fastGet("https://webvpn.neu.edu.cn/https/77726476706e69737468656265737421e0f85388263c265e7b1dc7a99c406d369a/desktop/api/user/info?vpn-12-o2-portal.neu.edu.cn");
        }

        /**
         * 获得校园网信息的json数据
         *
         * @return json字符串
         */
        public String networkJson() {
            return client.fastGet("https://webvpn.neu.edu.cn/https/77726476706e69737468656265737421e0f85388263c265e7b1dc7a99c406d369a/desktop/api/personal/campusInfo/network?vpn-12-o2-portal.neu.edu.cn");
        }

        /**
         * 获得校园卡信息的json数据
         *
         * @return json字符串
         */
        public String cardJson() {
            return client.fastGet("https://webvpn.neu.edu.cn/https/77726476706e69737468656265737421e0f85388263c265e7b1dc7a99c406d369a/desktop/api/personal/campusInfo/card?vpn-12-o2-portal.neu.edu.cn");
        }

        /**
         * 进行注销一网通操作
         */
        public void logout() {
            String location = "";
            Request request = Utils.ClientUtils.fastBuildRequest("https://webvpn.neu.edu.cn/https/77726476706e69737468656265737421f1e25594693e6d45300d8db9d6562d/auth/realms/portal/protocol/openid-connect/logout?redirect_uri=https%3A%2F%2Fpass.neu.edu.cn%2Ftpass%2Flogout%3Fservice%3Dhttps%253A%252F%252Fportal.neu.edu.cn%252Ftp_up%252F");
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            client.fastGet(location);
        }

        /**
         * 进行登录一网通操作
         */
        public void login() {
            String location = "";
            String loginHtml = "";

            Request request = Utils.ClientUtils.fastBuildRequest("https://webvpn.neu.edu.cn/https/77726476706e69737468656265737421e0f85388263c265e7b1dc7a99c406d369a/desktop/api/sso/login");
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            loginHtml = client.fastGet(location);
            String jsessionid_tpass = client.fastGet("https://webvpn.neu.edu.cn/wengine-vpn/cookie?method=get&host=portal.neu.edu.cn&scheme=https&path=/desktop/&vpn_timestamp=" + System.currentTimeMillis()).split("SESSION=")[1];

            RequestBody body = Utils.ClientUtils.fastBuildLoginBody(loginHtml, id, pass);
            request = Utils.ClientUtils.fastBuildRequest("https://webvpn.neu.edu.cn/https/77726476706e69737468656265737421e0f6528f693e6d45300d8db9d6562d/tpass/login;jsessionid_tpass=" + jsessionid_tpass + "?service=https%3A%2F%2Fportal.neu.edu.cn%2Fdesktop%2Fapi%2Fsso%2Flogin", body);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                if (response.code() >= 300 && response.code() <= 400) {
                    isLogin = true;
                } else {
                    isLogin = false;
                    return;
                }
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = Utils.ClientUtils.fastBuildRequest(location);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = Utils.ClientUtils.fastBuildRequest(location);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <h1>DeanRequest-进行非校园网环境下对教务处网页的请求</h1>
     * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
     *
     * @see com.wzt.aurora.spider.handle.NeuHandle
     * @see com.wzt.aurora.spider.handle.VpnHandle
     * @see com.wzt.aurora.spider.handle.RoomHandle
     */
    public class DeanRequest {
        /**
         * <h3>教务处是否登录</h3>
         */
        private boolean isLogin = false;

        public boolean isLogin() {
            return isLogin;
        }

        /**
         * 教务处登录操作
         */
        public void login() {
            String location = "";
            String loginHtml = client.fastGet("https://webvpn.neu.edu.cn/https/77726476706e69737468656265737421e0f6528f693e6d45300d8db9d6562d/tpass/login?service=http%3A%2F%2F219.216.96.4%2Feams%2FhomeExt.action");
            String jsessionid_tpass = client.fastGet("https://webvpn.neu.edu.cn/wengine-vpn/cookie?method=get&host=pass.neu.edu.cn&scheme=https&path=/tpass/login&vpn_timestamp=" + System.currentTimeMillis()).split("jsessionid_tpass=")[1];

            RequestBody body = Utils.ClientUtils.fastBuildLoginBody(loginHtml, id, pass);
            Request request = Utils.ClientUtils.fastBuildRequest("https://webvpn.neu.edu.cn/https/77726476706e69737468656265737421e0f6528f693e6d45300d8db9d6562d/tpass/login?service=http%3A%2F%2F219.216.96.4%2Feams%2FhomeExt.action&jsessionid_tpass=" + jsessionid_tpass, body);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                if (response.code() >= 300 && response.code() <= 400) {
                    isLogin = true;
                } else {
                    isLogin = false;
                    return;
                }
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = Utils.ClientUtils.fastBuildRequest(location);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            client.fastGet(location);
        }

        /**
         * 教务处注销操作
         */
        public void logout() {
            String location = "";
            Request request = Utils.ClientUtils.fastBuildRequest("https://webvpn.neu.edu.cn/http/77726476706e69737468656265737421a2a618d275613e1e275ec7f8/eams/logout.action");
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.fastGet(location);
        }

        /**
         * 获得课表信息的html文件
         *
         * @return html字符串
         */
        public String courseTableHtml() {
            return client.fastGet("https://webvpn.neu.edu.cn/http/77726476706e69737468656265737421a2a618d275613e1e275ec7f8/eams/courseTableForStd!courseTable.action?vpn-12-o1-219.216.96.4&ignoreHead=1&showPrintAndExport=1&setting.kind=std&semester.id=" + semesterId + "&ids=" + (Integer.valueOf(id) - 20127006));
        }

        /**
         * 获得绩点信息的html文件
         *
         * @return html字符串
         */
        public String gpaHtml() {
            long time = System.currentTimeMillis();
            return client.fastGet("https://webvpn.neu.edu.cn/http/77726476706e69737468656265737421a2a618d275613e1e275ec7f8/eams/teach/grade/course/person!search.action?vpn-12-o1-219.216.96.4&semesterId=" + semesterId + "&projectType=&_=" + time);
        }

        /**
         * 获得考试信息的首页html文件
         *
         * @return html字符串
         */
        public String examListHtml() {
            long time = System.currentTimeMillis();
            return client.fastGet("https://webvpn.neu.edu.cn/http/77726476706e69737468656265737421a2a618d275613e1e275ec7f8/eams/stdExamTable.action?vpn-12-o1-219.216.96.4&_=" + time + "&semester.id=" + semesterId);
        }

        /**
         * 获得指定考试id的考试信息html文件
         *
         * @param examId 考试id
         * @return html字符串
         */
        public String examHtml(String examId) {
            long time = System.currentTimeMillis();
            return client.fastGet("https://webvpn.neu.edu.cn/http/77726476706e69737468656265737421a2a618d275613e1e275ec7f8/eams/stdExamTable!examTable.action?vpn-12-o1-219.216.96.4&examBatch.id=" + examId + "&_=" + time);
        }
    }
}
