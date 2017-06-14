package com.richfit.common_lib.lib_base_sdk.base_head;


import com.richfit.common_lib.lib_mvp.BaseView;

/**
 * 所有抬头页面的View层接口
 * Created by monday on 2017/3/18.
 */

public interface IBaseHeadView extends BaseView {
    /**
     * 上传数据成功后，清空抬头所有的信息
     */
    void clearAllUIAfterSubmitSuccess();

    /**
     * 修改离线系统的抬头数据失败
     * @param message
     */
    void uploadEditedHeadDataFail(String message);
    /**
     * 修改离线系统的抬头数据成功
     */
    void uploadEditedHeadComplete();

}
