package com.richfit.module_mcq.module_dscx.detail;

import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailView;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;

/**
 * Created by monday on 2017/9/6.
 */

public interface IDSCSDetailView  extends IBaseDetailView<RefDetailEntity> {

    void loadRefListSuccess(List<ReferenceEntity> refs);
    void loadRefListFail(String message);
}

