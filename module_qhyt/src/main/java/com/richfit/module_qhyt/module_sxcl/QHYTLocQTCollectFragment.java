package com.richfit.module_qhyt.module_sxcl;

import com.richfit.module_qhyt.R;
import com.richfit.sdk_sxcl.basecollect.BaseLocQTCollectFragment;

/**
 * Created by monday on 2017/6/16.
 */

public class QHYTLocQTCollectFragment  extends BaseLocQTCollectFragment {

    @Override
    protected String getInvType() {
        return "1";
    }

    @Override
    protected String getInventoryQueryType() {
        return getString(R.string.inventoryQueryTypeSAPLocation);
    }
}
