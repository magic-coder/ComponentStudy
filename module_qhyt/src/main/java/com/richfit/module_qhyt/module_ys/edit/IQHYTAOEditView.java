package com.richfit.module_qhyt.module_ys.edit;

import com.richfit.common_lib.lib_base_sdk.base_edit.IBaseEditView;
import com.richfit.domain.bean.InvEntity;

import java.util.List;

/**
 * Created by monday on 2017/3/1.
 */

public interface IQHYTAOEditView extends IBaseEditView {

    void showInvs(List<InvEntity> invs);

    void loadInvsFail(String message);
}
