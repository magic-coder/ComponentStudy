package com.richfit.sdk_wzck.base_dsn_head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;
import com.richfit.domain.bean.WorkEntity;

import java.util.List;

/**
 * Created by monday on 2017/3/27.
 */

public interface IDSNHeadView extends IBaseHeadView {
    void showWorks(List<WorkEntity> works);
    void loadWorksFail(String message);

    void showAutoCompleteList(List<String> suppliers);
    void loadAutoCompleteFail(String message);

    void deleteCacheSuccess(String message);
    void deleteCacheFail(String message);
}
