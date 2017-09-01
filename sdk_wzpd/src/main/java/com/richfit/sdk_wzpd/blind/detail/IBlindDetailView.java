package com.richfit.sdk_wzpd.blind.detail;


import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;

/**
 * Created by monday on 2016/12/6.
 */

public interface IBlindDetailView extends BaseView {

    /**
     * 加载整单缓存数据成功后显示明细
     *
     * @param refData
     * @param pageNum
     */
    void showCheckInfo(ReferenceEntity refData, int pageNum);

    void loadCheckInfoFail(String message);


    /**
     * 删除子节点成功
     *
     * @param position：节点在明细列表的位置
     */
    void deleteNodeSuccess(int position);

    /**
     * 删除子节点失败
     *
     * @param message
     */
    void deleteNodeFail(String message);

    void transferCheckDataSuccess();

    void showTransferedNum(String transNum);

    void transferCheckDataFail(String message);

    void setTransFlagFail(String message);

    void setTransFlagsComplete();

    /**
     * 显示库存明细
     * @param allNodes
     */
    void showNodes(List<InventoryEntity> allNodes);

}
