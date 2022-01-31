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

package com.wzt.aurora.spider.handle;

import com.wzt.aurora.spider.data.*;
import com.wzt.aurora.spider.utils.Utils;
import com.wzt.aurora.spider.net.RoomRequest;
import com.wzt.aurora.spider.utils.Value;

import java.util.EnumSet;
import java.util.HashMap;

import static com.wzt.aurora.spider.utils.Value.RoomHandleValue.*;

/**
 * <h1>RoomHandle-用于进行必须数据的读取和占用教室情况的查询</h1>
 * <p>继承自Handle抽象类，实现了Value.RoomHandleValue接口以调用其中的已定义常量</p>
 *
 * @see Handle
 * @see Value.RoomHandleValue
 */
public class RoomHandle extends Handle {
    /**
     * <h3>请求数据</h3>
     * <p>具体请求数据请翻阅Value.RoomHandleValue，当需要获得多个数据时，请将不同参数使用|分开</p>
     * <p>例：如需获得逸夫楼教室占用情况和时间数据，请传入参数 YI_FU_OCCUPY | DATE_DATA</p>
     *
     * @see Value.RoomHandleValue
     */
    private int requestData = 0;
    /**
     * <h3>用于进行服务器的数据请求</h3>
     * <p>虽然用户可以通过RoomRequest进行手动请求，但这是一种强烈不建议的行为，
     * 建议用户直接使用Handle的形式来获取数据</p>
     */
    private RoomRequest request;

    /**
     * RoomHandle的构造方法，通过传入请求数据来返回指定数据
     * <br>
     * 对于requestData参数，要求必须传入Value.RoomHandleValue中的参数，参数及意义，数据类型如下：
     * <table>
     *   <caption>requestData参数</caption>
     *   <tr>
     *       <th>参数</th>
     *       <th>|</th>
     *       <th>意义</th>
     *       <th>|</th>
     *       <th>数据类型</th>
     *   </tr>
     *   <tbody>
     *       <tr>
     *           <td>HE_SHI_LI_OCCUPY</td>
     *           <td>|</td>
     *           <td>何世礼教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>YI_FU_OCCUPY</td>
     *           <td>|</td>
     *           <td>逸夫楼教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>DA_CHENG_OCCUPY</td>
     *           <td>|</td>
     *           <td>大成教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>CAI_KUANG_OCCUPY</td>
     *           <td>|</td>
     *           <td>采矿馆教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>JI_DIAN_OCCUPY</td>
     *           <td>|</td>
     *           <td>机电馆教室占用情况</td>
     *           <td>|</td>
     *           <td>RoomData</td>
     *       </tr>
     *       <tr>
     *           <td>DATE_DATA</td>
     *           <td>|</td>
     *           <td>需要的日期数据</td>
     *           <td>|</td>
     *           <td>DateData</td>
     *       </tr>
     *       <tr>
     *           <td>SEMESTER_DATA</td>
     *           <td>|</td>
     *           <td>学期数据</td>
     *           <td>|</td>
     *           <td>SemesterData</td>
     *       </tr>
     *       <tr>
     *           <td>SCHOOL_SCHEDULE</td>
     *           <td>|</td>
     *           <td>校历数据</td>
     *           <td>|</td>
     *           <td>SchoolScheduleData</td>
     *       </tr>
     *       <tr>
     *           <td>HE_SHI_LI_LIST</td>
     *           <td>|</td>
     *           <td>何世礼可用教室列表</td>
     *           <td>|</td>
     *           <td>RoomListData</td>
     *       </tr>
     *       <tr>
     *           <td>YI_FU_LIST</td>
     *           <td>|</td>
     *           <td>逸夫楼可用教室列表</td>
     *           <td>|</td>
     *           <td>RoomListData</td>
     *       </tr>
     *       <tr>
     *           <td>DA_CHENG_LIST</td>
     *           <td>|</td>
     *           <td>大成教学馆可用教室列表</td>
     *           <td>|</td>
     *           <td>RoomListData</td>
     *       </tr>
     *       <tr>
     *           <td>CAI_KUANG_LIST</td>
     *           <td>|</td>
     *           <td>采矿馆可用教室列表</td>
     *           <td>|</td>
     *           <td>RoomListData</td>
     *       </tr>
     *       <tr>
     *           <td>JI_DIAN_LIST</td>
     *           <td>|</td>
     *           <td>机电馆可用教室列表</td>
     *           <td>|</td>
     *           <td>RoomListData</td>
     *       </tr>
     *   </tbody>
     * </table>
     * <br>
     *
     * @param requestData 请求数据
     */
    public RoomHandle(EnumSet<Value.RoomHandleValue> requestData) {
        for (Value.RoomHandleValue roomHandleValue : requestData) {
            this.requestData = this.requestData | roomHandleValue.getId();
        }
        request = new RoomRequest();
    }

    @Override
    public HashMap<Enum, AuroraData> getData() {
        HashMap<Enum, AuroraData> dataHashMap = new HashMap<>();
        if ((requestData & HE_SHI_LI_OCCUPY.getId()) == HE_SHI_LI_OCCUPY.getId()) {
            RoomData data = Utils.DataTransformUtils.json2room(request.roomJson("教"));
            dataHashMap.put(HE_SHI_LI_OCCUPY, data);
        }
        if ((requestData & YI_FU_OCCUPY.getId()) == YI_FU_OCCUPY.getId()) {
            RoomData data = Utils.DataTransformUtils.json2room(request.roomJson("逸"));
            dataHashMap.put(YI_FU_OCCUPY, data);
        }
        if ((requestData & DA_CHENG_OCCUPY.getId()) == DA_CHENG_OCCUPY.getId()) {
            RoomData data = Utils.DataTransformUtils.json2room(request.roomJson("大成"));
            dataHashMap.put(DA_CHENG_OCCUPY, data);
        }
        if ((requestData & CAI_KUANG_OCCUPY.getId()) == CAI_KUANG_OCCUPY.getId()) {
            RoomData data = Utils.DataTransformUtils.json2room(request.roomJson("采"));
            dataHashMap.put(CAI_KUANG_OCCUPY, data);
        }
        if ((requestData & JI_DIAN_OCCUPY.getId()) == JI_DIAN_OCCUPY.getId()) {
            RoomData data = Utils.DataTransformUtils.json2room(request.roomJson("机"));
            dataHashMap.put(JI_DIAN_OCCUPY, data);
        }
        if ((requestData & DATE_DATA.getId()) == DATE_DATA.getId()) {
            DateData data = Utils.DataTransformUtils.json2date(request.dateJson());
            dataHashMap.put(DATE_DATA, data);
        }
        if ((requestData & SEMESTER_DATA.getId()) == SEMESTER_DATA.getId()) {
            SemesterData data = Utils.DataTransformUtils.json2semester(request.semesterJson());
            dataHashMap.put(SEMESTER_DATA, data);
        }
        if ((requestData & SCHOOL_SCHEDULE.getId()) == SCHOOL_SCHEDULE.getId()) {
            SchoolScheduleData data = Utils.DataTransformUtils.byte2schedule(request.schedule());
            dataHashMap.put(SCHOOL_SCHEDULE, data);
        }
        if ((requestData & HE_SHI_LI_LIST.getId()) == HE_SHI_LI_LIST.getId()) {
            RoomListData data = Utils.DataTransformUtils.json2roomList(request.roomListJson("教"));
            dataHashMap.put(HE_SHI_LI_LIST, data);
        }
        if ((requestData & YI_FU_LIST.getId()) == YI_FU_LIST.getId()) {
            RoomListData data = Utils.DataTransformUtils.json2roomList(request.roomListJson("逸"));
            dataHashMap.put(YI_FU_LIST, data);
        }
        if ((requestData & DA_CHENG_LIST.getId()) == DA_CHENG_LIST.getId()) {
            RoomListData data = Utils.DataTransformUtils.json2roomList(request.roomListJson("大成"));
            dataHashMap.put(DA_CHENG_LIST, data);
        }
        if ((requestData & CAI_KUANG_LIST.getId()) == CAI_KUANG_LIST.getId()) {
            RoomListData data = Utils.DataTransformUtils.json2roomList(request.roomListJson("采"));
            dataHashMap.put(CAI_KUANG_LIST, data);
        }
        if ((requestData & JI_DIAN_LIST.getId()) == JI_DIAN_LIST.getId()) {
            RoomListData data = Utils.DataTransformUtils.json2roomList(request.roomListJson("机"));
            dataHashMap.put(JI_DIAN_LIST, data);
        }
        return dataHashMap;
    }
}
