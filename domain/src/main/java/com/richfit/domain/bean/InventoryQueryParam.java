package com.richfit.domain.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 库存查询额外参数
 * Created by monday on 2017/6/19.
 */

public class InventoryQueryParam {
    public String queryType;
    public String invType;
    public Map<String,Object> extraMap;

    public void reset() {
        queryType = null;
        invType = null;
        extraMap = null;
    }
}
