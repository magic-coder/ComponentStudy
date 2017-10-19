package com.richfit.sdk_wzrs.base_rsn_head;


import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/3/2.
 */

public interface IRSNHeaderView extends IBaseHeadView {
    void showWorks(List<WorkEntity> works);

    void loadWorksFail(String message);

    void showAutoCompleteList(Map<String,List<SimpleEntity>> data);

    void loadAutoCompleteFail(String message);

    void deleteCacheSuccess(String message);

    void deleteCacheFail(String message);
}
