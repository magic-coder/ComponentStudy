package com.richfit.module_qysh.module_ys.detail;


import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailPresenter;
import com.richfit.domain.bean.ReferenceEntity;

/**
 * Created by monday on 2016/11/24.
 */

public interface IAODetailPresenter extends IBaseDetailPresenter<IAODetailView> {

    /**
     * 获取单据数据。注意由于验收只在一张单子上做业务
     *
     * @param refNum：单号
     * @param refType:单据类型
     * @param bizType：业务类型
     * @param moveType:移动类型
     * @param userId：用户loginId
     */
    void getReference(ReferenceEntity refData, String refNum, String refType,
                      String bizType, String moveType,
                      String refLineId, String userId);

    /**
     * 父节点删除
     *
     * @param lineDeleteFlag:是否删除整行(该字段仅仅是针对具有父子节点结构的明细有作用)。如果Y那么一次性删除该福及诶单 下的所有子节点，如果是N那么仅仅删除一个子节点
     * @param refNum：单号
     * @param refLineNum：行号
     * @param refLineId：行id
     * @param refType：单据类型
     * @param userId：用户id
     */
    void deleteNode(String lineDeleteFlag, String refNum, String refLineNum, String refLineId,
                    String refType, String bizType, String userId, int position,
                    String companyCode);




}
