package com.richfit.sdk_cwtz.head;


import com.richfit.common_lib.lib_mvp.IPresenter;

import java.util.Map;

/**
 * Created by monday on 2017/2/7.
 */

public interface ILAHeadPresenter extends IPresenter<ILAHeadView> {

    void getWorks(int flag);

    void getInvsByWorkId(String workId, int flag);

    void getStorageNum(String workId, String workCode, String invId, String invCode);

    void getAutoComList(String workCode, Map<String,Object> extraMap, String keyWord, int defaultItemNum, int flag, String bizType);

}
