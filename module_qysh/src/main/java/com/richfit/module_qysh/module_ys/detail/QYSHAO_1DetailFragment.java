package com.richfit.module_qysh.module_ys.detail;

import com.richfit.domain.bean.RefDetailEntity;

/**
 * 庆阳验收清单验收的数据明细，仅仅提供展示数据的功能
 * Created by monday on 2017/1/16.
 */

public class QYSHAO_1DetailFragment extends QYSHAODetailFragment {


    @Override
    public void deleteNode(RefDetailEntity node, int position) {
        showMessage("验收清单验收不能删除验收结果");
    }

    @Override
    public void editNode(RefDetailEntity node, int position) {
        showMessage("验收清单验收不能编辑验收结果");
    }



}
