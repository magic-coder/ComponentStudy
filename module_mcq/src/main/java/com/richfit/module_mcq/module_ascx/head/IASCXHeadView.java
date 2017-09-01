package com.richfit.module_mcq.module_ascx.head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;

/**
 * Created by monday on 2017/8/28.
 */

public interface IASCXHeadView extends IBaseHeadView {
    void loadRefListSuccess(List<ReferenceEntity> refs);
    void loadRefListFail(String message);
}
