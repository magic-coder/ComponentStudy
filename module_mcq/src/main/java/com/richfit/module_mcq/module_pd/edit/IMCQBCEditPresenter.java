package com.richfit.module_mcq.module_pd.edit;

import com.richfit.common_lib.lib_base_sdk.base_edit.IBaseEditPresenter;
import com.richfit.domain.bean.ResultEntity;

/**
 * Created by monday on 2017/8/30.
 */

public interface IMCQBCEditPresenter extends IBaseEditPresenter<IMCQBCEditView>{
    void uploadCheckDataSingle(ResultEntity result);
}
