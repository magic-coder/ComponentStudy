package com.richfit.module_mcq.module_dscx.head;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/8/28.
 */

public class DSCXHeadPresenterImp extends BaseHeadPresenterImp<IDSCXHeadView>
        implements IDSCXHeadPresenter {

    public DSCXHeadPresenterImp(Context context) {
        super(context);
    }

}
