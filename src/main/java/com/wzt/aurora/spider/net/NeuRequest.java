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
 * <h1>NeuRequest-进行校园网环境下的数据请求</h1>
 * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
 *
 * @see com.wzt.aurora.spider.handle.NeuHandle
 * @see com.wzt.aurora.spider.handle.VpnHandle
 * @see com.wzt.aurora.spider.handle.RoomHandle
 */
public class NeuRequest {
    /**
     * <h1>EOneRequest-进行校园网环境下对一网通网页的请求</h1>
     * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
     *
     * @see com.wzt.aurora.spider.handle.NeuHandle
     * @see com.wzt.aurora.spider.handle.VpnHandle
     * @see com.wzt.aurora.spider.handle.RoomHandle
     */
    public static class EOneRequest {
        /**
         * <h3>学号</h3>
         */
        private String id;
        /**
         * <h3>密码</h3>
         */
        private String pass;
        /**
         * <h3>是否登录</h3>
         */
        private boolean isLogin = false;
        /**
         * <h3>构建校园网下一网通的请求</h3>
         */
        private Client client = Client.getInstance(Value.ClientCodeValue.NEU_E_ONE_CLIENT);
        /**
         * <h3>获得OkHttpClient对象</h3>
         */
        private OkHttpClient okHttpClient = client.getOkHttpClient();

        /**
         * EOneRequest构造器，创建对象并登录一网通
         *
         * @param id   学号
         * @param pass 密码
         */
        public EOneRequest(String id, String pass) {
            this.id = id;
            this.pass = pass;
            login();
        }

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
            Request request = Utils.ClientUtils.fastBuildRequest("https://portal.neu.edu.cn/tp_up/up/subgroup/gotoLibirarySystem");
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
            Matcher matcher = Value.PatternValue.BORROW_BOOK_URL.matcher(html);
            if (matcher.find()) {
                html = client.fastGet(matcher.group(1));
            }
            return html;
        }

        /**
         * 获得学生信息的json数据
         *
         * @return json字符串
         */
        public String infoJson() {
            return client.fastGet("https://portal.neu.edu.cn/desktop/api/user/info");
        }

        /**
         * 获得校园网信息的json数据
         *
         * @return json字符串
         */
        public String networkJson() {
            return client.fastGet("https://portal.neu.edu.cn/desktop/api/personal/campusInfo/network");
        }

        /**
         * 获得校园卡信息的json数据
         *
         * @return json字符串
         */
        public String cardJson() {
            return client.fastGet("https://portal.neu.edu.cn/desktop/api/personal/campusInfo/card");
        }

        /**
         * 进行登录操作
         */
        private void login() {
            String location = "";
            String loginHtml = "";

            client.fastGet("https://portal.neu.edu.cn/desktop/api/user");

            Request request = Utils.ClientUtils.fastBuildRequest("https://portal.neu.edu.cn/desktop/api/sso/login");
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }

            loginHtml = client.fastGet(location);

            RequestBody loginBody = Utils.ClientUtils.fastBuildLoginBody(loginHtml, id, pass);
            String jsessionid_tpass = "";
            for (Cookie cookie : Cookies.NeuEOneCookies.getInstance().getCookieStore().get("pass.neu.edu.cn")) {
                if (cookie.name().equals("jsessionid_tpass"))
                    jsessionid_tpass = cookie.value();
            }
            request = Utils.ClientUtils.fastBuildRequest("https://pass.neu.edu.cn/tpass/login;jsessionid_tpass=" + jsessionid_tpass + "?service=https%3A%2F%2Fportal.neu.edu.cn%2Fdesktop%2Fapi%2Fsso%2Flogin", loginBody);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                if (response.code() >= 300 && response.code() <= 400) {
                    location = response.header("Location");
                    isLogin = true;
                } else {
                    isLogin = false;
                    return;
                }
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
     * <h1>DeanRequest-进行校园网环境下对教务处网页的请求</h1>
     * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
     *
     * @see com.wzt.aurora.spider.handle.NeuHandle
     * @see com.wzt.aurora.spider.handle.VpnHandle
     * @see com.wzt.aurora.spider.handle.RoomHandle
     */
    public static class DeanRequest {
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
         * <h3>构建校园网下进行教务处访问的请求</h3>
         */
        private Client client = Client.getInstance(Value.ClientCodeValue.NEU_DEAN_CLIENT);
        /**
         * <h3>获得OkHttpClient请求对象</h3>
         */
        private OkHttpClient okHttpClient = client.getOkHttpClient();

        /**
         * DeanRequest构造器，创建DeanRequest对象并登录
         *
         * @param id         学号
         * @param pass       密码
         * @param semesterId 学期id
         */
        public DeanRequest(String id, String pass, String semesterId) {
            this.id = id;
            this.pass = pass;
            this.semesterId = semesterId;
            login();
        }

        public boolean isLogin() {
            return isLogin;
        }

        /**
         * 获得课表信息的html文件
         *
         * @return html字符串
         */
        public String courseTableHtml() {
            return client.fastGet("http://219.216.96.4/eams/courseTableForStd!courseTable.action?ignoreHead=1&showPrintAndExport=1&setting.kind=std&semester.id=" + semesterId + "&ids=" + (Integer.valueOf(id) - 20127006));
        }

        /**
         * 获得绩点信息的html文件
         *
         * @return html字符串
         */
        public String gpaHtml() {
            long time = System.currentTimeMillis();
            return client.fastGet("http://219.216.96.4/eams/teach/grade/course/person!search.action?semesterId=" + semesterId + "&projectType=&_=" + time);
        }

        /**
         * 获得考试信息的首页html文件
         *
         * @return html字符串
         */
        public String examListHtml() {
            long time = System.currentTimeMillis();
            return client.fastGet("http://219.216.96.4/eams/stdExamTable.action?semester.id=" + semesterId + "&_=" + time);
        }

        /**
         * 获得指定考试id的考试信息html文件
         *
         * @param examId 考试id
         * @return html字符串
         */
        public String examHtml(String examId) {
            long time = System.currentTimeMillis();
            return client.fastGet("http://219.216.96.4/eams/stdExamTable!examTable.action?examBatch.id=" + examId + "&_=" + time);
        }

        /**
         * 登录操作
         */
        private void login() {
            String location = "";
            String loginHtml = "";

            Request request = Utils.ClientUtils.fastBuildRequest("http://219.216.96.4/eams/homeExt.action?semester.id=" + semesterId);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                if (response.code() >= 300 && response.code() <= 400) {
                    isLogin = true;
                    location = response.header("Location");
                } else {
                    isLogin = false;
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            loginHtml = client.fastGet(location);

            RequestBody body = Utils.ClientUtils.fastBuildLoginBody(loginHtml, id, pass);
            String jsessionid_tpass = "", jsessionid = "";
            for (Cookie cookie : Cookies.NeuDeanCookies.getInstance().getCookieStore().get("pass.neu.edu.cn")) {
                if (cookie.name().equals("jsessionid_tpass"))
                    jsessionid_tpass = cookie.value();
                if (cookie.name().equals("JSESSIONID"))
                    jsessionid = cookie.value();
            }
            request = Utils.ClientUtils.fastBuildRequest("https://pass.neu.edu.cn/tpass/login;jsessionid_tpass=" + jsessionid_tpass + "?service=http%3A%2F%2F219.216.96.4%2Feams%2FhomeExt.action%3Bjsessionid%3D" + jsessionid, body);
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

            request = Utils.ClientUtils.fastBuildRequest(location);
            try (Response response = okHttpClient.newCall(request).execute()) {
                Utils.ClientUtils.printClientData(response);
                location = response.header("Location");
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.fastGet(location);
        }
    }
}
