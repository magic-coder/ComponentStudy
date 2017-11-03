package com.richfit.module_qysh.module_as.asn.imp;

import android.content.Context;

import com.richfit.sdk_wzrk.base_asn_head.imp.ASNHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/11/2.
 */

public class QHSYASNHeadPresenterImp extends ASNHeadPresenterImp {

    public QHSYASNHeadPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getMoveTypeList(int flag) {
        mView = getView();
        ArrayList<String> moveTypeList = new ArrayList<>();
        moveTypeList.add("501K");
        if(mView != null) {
            mView.showMoveTypes(moveTypeList);
        }
    }
}
