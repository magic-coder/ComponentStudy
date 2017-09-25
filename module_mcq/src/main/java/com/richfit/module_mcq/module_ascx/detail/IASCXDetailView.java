package com.richfit.module_mcq.module_ascx.detail;

import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailView;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;

/**
 * Created by monday on 2017/8/29.
 */

public interface IASCXDetailView extends IBaseDetailView<RefDetailEntity> {

    void loadRefListSuccess(List<ReferenceEntity> refs);
    void loadRefListFail(String message);
}
