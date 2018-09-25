package com.common.network;

import com.common.utils.GsonTools;
import com.common.utils.LogUtils;

/**
 * Created by seven
 * on 2018/5/17
 * email:seven2016s@163.com
 */

//public class ServerResultFunction implements io.reactivex.functions.Function<ResponseCustom, Object> {
public class ServerResultFunction implements io.reactivex.functions.Function<Object, Object> {
    @Override
    public Object apply(Object responseCustom) {
        LogUtils.jsonE("服务器返回结果===>" + GsonTools.createGsonString(responseCustom));

        return responseCustom;
    }
}
