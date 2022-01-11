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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>BookData-用于存储个人的借阅图书信息</h1>
 * <p>定义了静态内部类ChildBook用于存储单个图书数据，在BookData中定义了ChildBook列表用于存储所有书籍数据</p>
 * <p>此类与其内部类均实现了AuroraData接口以完成数据的json化和反json化</p>
 *
 * @see AuroraData
 * @see ChildBook
 */
public class BookData implements AuroraData {
    /**
     * <h1>ChildBook-用于存储单本图书数据</h1>
     * <p>同样实现了AuroraData接口，为BookData的静态内部类</p>
     * <p>需要注意的是，尽管此内部类也实现了AuroraData接口，但是没有实现静态方法inverse()，将此方法的实现放于外部类，
     * 当需要进行json数据转化为BookData数据时，只需要调用BookData.inverse()即可</p>
     *
     * @see AuroraData
     * @see BookData
     */
    public static class ChildBook implements AuroraData {
        @Override
        public String toJson() {
            JSONObject object = new JSONObject();
            object.put("author", author);
            object.put("bookName", bookName);
            object.put("publishYear", publishYear);
            object.put("bookId", bookId);
            object.put("returnDate", returnDate);
            object.put("impose", impose);
            object.put("bookPlace", bookPlace);
            return object.toString();
        }

        /**
         * <h3>图书作者</h3>
         */
        private String author;
        /**
         * <h3>图书名</h3>
         */
        private String bookName;
        /**
         * <h3>出版年份</h3>
         */
        private String publishYear;
        /**
         * <h3>图书索书号</h3>
         */
        private String bookId;
        /**
         * <h3>学校图书馆规定的需要归还的日期</h3>
         */
        private String returnDate;
        /**
         * <h3>负债，一般出现于图书损坏或丢失</h3>
         */
        private String impose;
        /**
         * <h3>图书所在位置</h3>
         */
        private String bookPlace;

        /**
         * ChildBook构造器，用于创建单本图书数据
         *
         * @param author      图书作者
         * @param bookName    图书名
         * @param publishYear 出版年份
         * @param bookId      索书号
         * @param returnDate  还书日期
         * @param impose      赔款
         * @param bookPlace   图书所在地
         */
        public ChildBook(String author, String bookName, String publishYear, String bookId, String returnDate, String impose, String bookPlace) {
            this.author = author;
            this.bookName = bookName;
            this.publishYear = publishYear;
            this.bookId = bookId;
            this.returnDate = returnDate;
            this.impose = impose;
            this.bookPlace = bookPlace;
        }

        public String getAuthor() {
            return author;
        }

        public String getBookName() {
            return bookName;
        }

        public String getPublishYear() {
            return publishYear;
        }

        public String getBookId() {
            return bookId;
        }

        public String getReturnDate() {
            return returnDate;
        }

        public String getImpose() {
            return impose;
        }

        public String getBookPlace() {
            return bookPlace;
        }
    }

    /**
     * <h3>用于存储所有图书数据</h3>
     */
    private List<ChildBook> bookList = new ArrayList<>();

    /**
     * BookData构造器，用于创建个人所有图书数据
     *
     * @param bookList 传入的图书列表
     * @see ChildBook
     */
    public BookData(List<ChildBook> bookList) {
        this.bookList = bookList;
    }

    public List<ChildBook> getBookList() {
        return bookList;
    }

    @Override
    public String toJson() {
        JSONArray array = new JSONArray();
        for (ChildBook book : bookList) {
            array.add(book.toJson());
        }
        return array.toString();
    }

    /**
     * 将json格式的字符串转化为BookData对象
     *
     * @param json 传入的json字符串
     * @return 返回的BookData对象
     * @see BookData
     */
    public static BookData inverse(String json) {
        JSONArray array = JSONArray.fromObject(json);
        List<BookData.ChildBook> bookList = new ArrayList<>();
        for (Object o : array) {
            String author = ((JSONObject) o).getString("author");
            String bookName = ((JSONObject) o).getString("bookName");
            String publishYear = ((JSONObject) o).getString("publishYear");
            String bookId = ((JSONObject) o).getString("bookId");
            String returnDate = ((JSONObject) o).getString("returnDate");
            String impose = ((JSONObject) o).getString("impose");
            String bookPlace = ((JSONObject) o).getString("bookPlace");
            BookData.ChildBook childBook = new ChildBook(author, bookName, publishYear, bookId, returnDate, impose, bookPlace);
            bookList.add(childBook);
        }
        return new BookData(bookList);
    }
}
