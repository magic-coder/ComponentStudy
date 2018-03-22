package com.richfit.module_cqyt.module_inv;

import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.RefDetailEntity;

import java.util.List;

/**
 * Created by monday on 2018/3/19.
 */

public interface ICQDSInvYView extends BaseView {
    /**
     * 读取单据数据
     *
     * @param datas:明细数据
     */
    void getReferenceSuccess(List<RefDetailEntity> datas);

    void loadReferenceComplete();

    void getReferenceFail(String message);

}
