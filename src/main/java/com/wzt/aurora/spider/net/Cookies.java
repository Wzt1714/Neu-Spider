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

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import com.wzt.aurora.spider.utils.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <h1>Cookies-实现对请求Cookie的自动化管理</h1>
 * <p>使用单例模式</p>
 * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
 *
 * @see com.wzt.aurora.spider.handle.NeuHandle
 * @see com.wzt.aurora.spider.handle.VpnHandle
 * @see com.wzt.aurora.spider.handle.RoomHandle
 */
public class Cookies {
    /**
     * <h1>NeuEOneCookies-实现校园网环境下对一网通网站请求Cookie的自动管理</h1>
     * <p>实现CookieJar和CookieDestroy接口</p>
     * <p>使用单例模式</p>
     * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
     *
     * @see com.wzt.aurora.spider.handle.NeuHandle
     * @see com.wzt.aurora.spider.handle.VpnHandle
     * @see com.wzt.aurora.spider.handle.RoomHandle
     * @see CookieJar
     * @see CookieDestroy
     */
    public static class NeuEOneCookies implements CookieJar, CookieDestroy {
        /**
         * 将构造器设为private禁止外部访问
         */
        private NeuEOneCookies() {
        }

        /**
         * <h3>懒汉式</h3>
         */
        private static NeuEOneCookies cookies;

        /**
         * 获取实例
         *
         * @return NeuEOneCookies实例
         */
        public static NeuEOneCookies getInstance() {
            if (cookies == null) {
                cookies = new NeuEOneCookies();
            }
            return cookies;
        }

        /**
         * <h3>用于存储Cookie</h3>
         */
        private HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        public HashMap<String, List<Cookie>> getCookieStore() {
            return cookieStore;
        }

        /**
         * 请求时自动发送Cookie
         *
         * @param httpUrl httpUrl对象
         * @return 发送的Cookie
         */
        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
            if (Value.debug) {
                System.out.println("Send Cookies: ");
                System.out.println("Host = " + httpUrl.host());
                System.out.println("Cookies: ");
                if (cookieStore.get(httpUrl.host()) != null)
                    for (Cookie cookie : cookieStore.get(httpUrl.host())) {
                        System.out.println(cookie.name() + ": " + cookie.value());
                    }
                System.out.println();
            }
            if (null != httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            } else {
                return new ArrayList<Cookie>();
            }
        }

        /**
         * 响应后自动保存Cookie
         *
         * @param httpUrl httpUrl实例
         * @param list    Cookie列表
         */
        @Override
        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
            cookieStore.put(httpUrl.host(), list);
            if (Value.debug) {
                System.out.println("Save Cookies: ");
                System.out.println("Host = " + httpUrl.host());
                System.out.println("Cookies: ");
                for (Cookie cookie : list) {
                    System.out.println(cookie.name() + ": " + cookie.value());
                }
                System.out.println();
            }
        }

        @Override
        public void destroy() {
            cookieStore.clear();
        }
    }

    /**
     * <h1>NeuEOneCookies-实现校园网环境下对教务处网站请求Cookie的自动管理</h1>
     * <p>实现CookieJar和CookieDestroy接口</p>
     * <p>使用单例模式</p>
     * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
     *
     * @see com.wzt.aurora.spider.handle.NeuHandle
     * @see com.wzt.aurora.spider.handle.VpnHandle
     * @see com.wzt.aurora.spider.handle.RoomHandle
     * @see CookieJar
     * @see CookieDestroy
     */
    public static class NeuDeanCookies implements CookieJar, CookieDestroy {
        /**
         * 将构造器设为private，禁止外部调用
         */
        private NeuDeanCookies() {
        }

        /**
         * <h3>懒汉式</h3>
         */
        public static NeuDeanCookies neuDeanCookies;

        /**
         * 获取NeuDeanCookies实例
         *
         * @return NeuDeanCookies实例
         */
        public static NeuDeanCookies getInstance() {
            if (neuDeanCookies == null) {
                neuDeanCookies = new NeuDeanCookies();
            }
            return neuDeanCookies;
        }

        /**
         * <h3>用于存储Cookie对象</h3>
         */
        private HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        public HashMap<String, List<Cookie>> getCookieStore() {
            return cookieStore;
        }

        /**
         * 请求时自动发送Cookie
         *
         * @param httpUrl httpUrl对象
         * @return 发送的Cookie
         */
        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
            if (Value.debug) {
                System.out.println("Send Cookies: ");
                System.out.println("Host = " + httpUrl.host());
                System.out.println("Cookies: ");
                if (cookieStore.get(httpUrl.host()) != null)
                    for (Cookie cookie : cookieStore.get(httpUrl.host())) {
                        System.out.println(cookie.name() + ": " + cookie.value());
                    }
                System.out.println();
            }
            if (null != httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            } else {
                return new ArrayList<Cookie>();
            }
        }

        /**
         * 响应后自动保存Cookie
         *
         * @param httpUrl httpUrl实例
         * @param list    Cookie列表
         */
        @Override
        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
            if (httpUrl.host().equals("219.216.96.4")) {
                if (list.size() == 3)
                    cookieStore.put(httpUrl.host(), list);
            } else {
                cookieStore.put(httpUrl.host(), list);
            }
            if (Value.debug) {
                System.out.println("Save Cookies: ");
                System.out.println("Host = " + httpUrl.host());
                System.out.println("Cookies: ");
                for (Cookie cookie : list) {
                    System.out.println(cookie.name() + ": " + cookie.value());
                }
                System.out.println();
            }
        }

        @Override
        public void destroy() {
            cookieStore.clear();
        }
    }

    /**
     * <h1>NeuEOneCookies-实现非校园网环境下对WebVpn网站请求Cookie的自动管理</h1>
     * <p>实现CookieJar和CookieDestroy接口</p>
     * <p>使用单例模式</p>
     * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
     *
     * @see com.wzt.aurora.spider.handle.NeuHandle
     * @see com.wzt.aurora.spider.handle.VpnHandle
     * @see com.wzt.aurora.spider.handle.RoomHandle
     * @see CookieJar
     * @see CookieDestroy
     */
    public static class VpnCookies implements CookieJar, CookieDestroy {
        /**
         * <h3>用于保存登录后WebVpn的同用Cookie</h3>
         */
        private Cookie cookie;

        /**
         * 将构造器设为private，禁止外部调用
         */
        private VpnCookies() {
        }

        /**
         * <h3>懒汉式</h3>
         */
        private static VpnCookies vpnCookies;

        /**
         * 获取VpnCookies实例
         *
         * @return VpnCookies实例
         */
        public static VpnCookies getInstance() {
            if (vpnCookies == null) {
                vpnCookies = new VpnCookies();
            }
            return vpnCookies;
        }

        /**
         * <h3>用于存储Cookie对象</h3>
         */
        private HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        public HashMap<String, List<Cookie>> getCookieStore() {
            return cookieStore;
        }

        /**
         * 请求时自动发送Cookie
         *
         * @param httpUrl httpUrl对象
         * @return 发送的Cookie
         */
        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
            if (Value.debug) {
                System.out.println("Send Cookies: ");
                System.out.println("Host = " + httpUrl.host());
                System.out.println("Cookies: ");
                if (cookieStore.get(httpUrl.host()) != null)
                    for (Cookie cookie : cookieStore.get(httpUrl.host())) {
                        System.out.println(cookie.name() + ": " + cookie.value());
                    }
                System.out.println();
            }
            if (cookie == null) {
                if (null != httpUrl) {
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                } else {
                    return new ArrayList<Cookie>();
                }
            } else {
                if (null != httpUrl) {
                    ArrayList<Cookie> cookieArrayList = new ArrayList<>();
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    if (cookies != null) {
                        cookieArrayList.addAll(cookies);
                    }
                    cookieArrayList.add(cookie);
                    return cookieArrayList;
                } else {
                    ArrayList<Cookie> c = new ArrayList<Cookie>();
                    c.add(cookie);
                    return c;
                }
            }
        }

        /**
         * 响应后自动保存Cookie
         *
         * @param httpUrl httpUrl实例
         * @param list    Cookie列表
         */
        @Override
        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
            cookieStore.put(httpUrl.host(), list);
            for (Cookie cookie : list) {
                if (cookie.name().equals("wengine_vpn_ticketwebvpn_neu_edu_cn"))
                    this.cookie = cookie;
            }
            if (Value.debug) {
                System.out.println("Save Cookies: ");
                System.out.println("Host = " + httpUrl.host());
                System.out.println("Cookies: ");
                for (Cookie cookie : list) {
                    System.out.println(cookie.name() + ": " + cookie.value());
                }
                System.out.println();
            }
        }

        @Override
        public void destroy() {
            cookieStore.clear();
            cookie = null;
        }
    }

    /**
     * <h1>CookieDestroy-用于销毁Cookie的接口</h1>
     * <p>实现该接口中的方法销毁Cookie数据</p>
     * <p>尽管可以通过调用com.wzt.aurora.spider.net包中的类和方法进行自定义爬虫，但不建议这么做，可能会引起无法预测的结果，使用爬虫请调用Handle</p>
     *
     * @see com.wzt.aurora.spider.handle.NeuHandle
     * @see com.wzt.aurora.spider.handle.VpnHandle
     * @see com.wzt.aurora.spider.handle.RoomHandle
     */
    private interface CookieDestroy {
        public void destroy();
    }
}
