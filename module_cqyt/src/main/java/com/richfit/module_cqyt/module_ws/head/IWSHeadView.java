package com.richfit.module_cqyt.module_ws.head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;
import com.richfit.domain.bean.WorkEntity;

import java.util.List;

/**
 * Created by monday on 2017/9/21.
 */

public interface IWSHeadView extends IBaseHeadView {
    void showWorks(List<WorkEntity> works);

    void loadWorksFail(String message);
}
