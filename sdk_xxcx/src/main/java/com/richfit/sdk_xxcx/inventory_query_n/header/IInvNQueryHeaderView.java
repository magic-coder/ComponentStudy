package com.richfit.sdk_xxcx.inventory_query_n.header;


import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/5/25.
 */

public interface IInvNQueryHeaderView extends BaseView {
    void showWorks(List<WorkEntity> works);

    void loadWorksFail(String message);

    void loadWorksComplete();

    void showInvs(List<InvEntity> recInvs);

    void loadInvsFail(String message);

    void loadInvsComplete();

    void loadDictionaryDataSuccess(Map<String,List<SimpleEntity>> data);
    void loadDictionaryDataFail(String message);

}
