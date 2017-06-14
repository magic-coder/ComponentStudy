package com.richfit.common_lib.lib_interface;

/**
 * Created by monday on 2017/3/18.
 */

public interface IOnItemMove<T> {
    /**
     * 明细界面子节点删除
     * @param node
     * @param position
     */
    void deleteNode(T node, int position);

    /**
     * 明细界面子节点编辑(修改)
     * @param node
     * @param position
     */
    void editNode(T node, int position);
}
