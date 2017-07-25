package com.richfit.module_cqyt.module_vs;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadPresenterImp;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTPresenterImp extends BaseHeadPresenterImp<CQYTVSContract.View>
        implements CQYTVSContract.Presenter {

    CQYTVSContract.View mView;

    //已经在界面显示的单据数据
    ArrayList<ReferenceEntity> mDatas;

    ArrayList<RefDetailEntity> mTreeNodes;


    public CQYTPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void getReference(String refNum, String refType, String bizType, String moveType, String refLineId, String userId) {
        mView = getView();

        mRepository.getReference(refNum, refType, bizType, moveType, refLineId, userId)
                .filter(refData -> refData != null && refData.billDetailList != null && refData.billDetailList.size() > 0)
                .flatMap(data -> prepareAppendRedData(data))
                .map(data -> createNodesByRefData(data))
                .flatMap(nodes -> sortNodes(nodes))
                .map(nodes -> calculateQuantity(nodes))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<List<RefDetailEntity>>() {
                    @Override
                    public void onNext(List<RefDetailEntity> allNodes) {
                        if (mView != null) {
                            mView.showNodes(allNodes);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.showDataFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 准备追加数据
     *
     * @param data
     * @return
     */
    private Flowable<ReferenceEntity> prepareAppendRedData(ReferenceEntity data) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        for (ReferenceEntity item : mDatas) {
            if (data.recordNum.equals(item.recordNum)) {
                return Flowable.error(new Throwable("该单据已经统计完成!"));
            }
        }

        return Flowable.just(data);
    }

    /**
     * 在树形结构明细中追加一个父子节点或者一个子节点
     *
     * @param refData
     * @return
     */
    protected ArrayList<RefDetailEntity> createNodesByRefData(final ReferenceEntity refData) {
        final String recordNum = refData.recordNum;
        if (mDatas != null && mDatas.size() == 0) {
            //第一次统计,直接形成树形结构即可
            if (mTreeNodes == null) {
                mTreeNodes = new ArrayList<>();
            }
            List<RefDetailEntity> items = refData.billDetailList;
            ArrayList<RefDetailEntity> parentNodes = new ArrayList<>();
            //遍历明细，将物料信息作为父节点
            for (RefDetailEntity item : items) {
                item.recordNum = recordNum;
                parentNodes.add(item);
            }
            List<RefDetailEntity> result = appendTreeNode(parentNodes);
            mTreeNodes.addAll(result);
        } else {
            //清空所有已经显示的树的状态
            for (RefDetailEntity node : mTreeNodes) {
                if (node.getViewType() == Global.PARENT_NODE_HEADER_TYPE)
                    node.setChildren(new ArrayList<>());
            }

            //如果是追加,那么遍历要增加的明细数据
            List<RefDetailEntity> items = refData.billDetailList;
            for (RefDetailEntity item : items) {
                //查看树形结构是否已经存在该节点
                RefDetailEntity parentNode = findParentNode(item);
                if (parentNode == null) {
                    //如果不存在，那么将新的生成父子节点
                    ArrayList<RefDetailEntity> parentNodes = new ArrayList<>();
                    item.recordNum = recordNum;
                    parentNodes.add(item);
                    List<RefDetailEntity> result = appendTreeNode(parentNodes);
                    //将父子节点加入到显示列表
                    mTreeNodes.addAll(result);
                } else {
                    //新增一个子节点
                    RefDetailEntity childNode = new RefDetailEntity();
                    childNode.recordNum = recordNum;
                    //注意这里必须用最新的明细数据生成子节点
                    childNode.lineNum = item.lineNum;
                    childNode.orderQuantity = item.orderQuantity;
                    childNode.shkzg = item.shkzg;
                    addTreeInfo(parentNode, childNode, mTreeNodes);
                }
            }

        }
        //更新数据集合
        mDatas.add(refData);
        return mTreeNodes;
    }

    /**
     * 计算父节点的累计数量
     *
     * @param nodes
     */
    private List<RefDetailEntity> calculateQuantity(List<RefDetailEntity> nodes) {

        for (RefDetailEntity parentNode : nodes) {
            if (parentNode.getViewType() == Global.PARENT_NODE_HEADER_TYPE) {
                float quantityS = 0.F;
                float quantityH = 0.F;
                List<TreeNode> children = parentNode.getChildren();
                if (children == null || children.size() == 0) {
                    continue;
                }
                for (TreeNode child : children) {
                    if (child.getViewType() == Global.CHILD_NODE_HEADER_TYPE)
                        continue;
                    if (child instanceof RefDetailEntity) {
                        RefDetailEntity childNode = (RefDetailEntity) child;
                        if ("S".equals(childNode.shkzg)) {
                            //上架数量
                            quantityS += CommonUtil.convertToFloat(childNode.orderQuantity, 0.0F);
                        } else {
                            //下架数量
                            quantityH += CommonUtil.convertToFloat(childNode.orderQuantity, 0.0F);
                        }
                    }
                }
                parentNode.quantityS = String.valueOf(quantityS);
                parentNode.quantityH = String.valueOf(quantityH);
            }
        }
        return nodes;
    }

    /**
     * 针对任意一个父节点生成其子节点
     *
     * @param nodes:原始单据数据的明细
     * @return
     */
    private List<RefDetailEntity> appendTreeNode(final List<RefDetailEntity> nodes) {
        List<RefDetailEntity> result = new ArrayList<>();
        //CopyOnWriteArrayList采用了读些分离的技术
        CopyOnWriteArrayList<RefDetailEntity> parentNodes = new CopyOnWriteArrayList<>();

        //处理父节点一致的数据(物料id一致)
        for (RefDetailEntity data : nodes) {
            if (parentNodes.size() == 0) {
                RefDetailEntity newNode = new RefDetailEntity();
                newNode.materialId = data.materialId;
                newNode.materialNum = data.materialNum;
                newNode.materialDesc = data.materialDesc;
                newNode.materialGroup = data.materialGroup;
                newNode.shkzg = data.shkzg;
                parentNodes.add(newNode);
            } else {
                for (RefDetailEntity parentNode : parentNodes) {
                    if (parentNode.materialId.equals(data.materialId)) {
                        continue;
                    }
                    RefDetailEntity newNode = new RefDetailEntity();
                    newNode.materialId = data.materialId;
                    newNode.materialNum = data.materialNum;
                    newNode.materialDesc = data.materialDesc;
                    newNode.materialGroup = data.materialGroup;
                    newNode.shkzg = data.shkzg;
                    parentNodes.add(newNode);
                }
            }
        }
        //生成父节点
        addTreeInfo(parentNodes);
        result.addAll(parentNodes);
        //生成子节点
        List<RefDetailEntity> childNodes = new ArrayList<>();
        for (RefDetailEntity node : nodes) {
            RefDetailEntity childNode = new RefDetailEntity();
            childNode.recordNum = node.recordNum;
            childNode.lineNum = node.lineNum;
            childNode.orderQuantity = node.orderQuantity;
            childNode.shkzg = node.shkzg;
            childNodes.add(childNode);
        }

        for (RefDetailEntity parentNode : parentNodes) {
            for (RefDetailEntity childNode : childNodes) {
                addTreeInfo(parentNode, childNode, result);
            }
        }

        return result;
    }

    /**
     * 在已经显示的树中查找是否已经存在该父节点
     *
     * @param node
     * @return
     */
    private RefDetailEntity findParentNode(RefDetailEntity node) {
        for (RefDetailEntity parentNode : mTreeNodes) {
            if (node.materialId.equals(parentNode.materialId)) {
                return parentNode;
            }
        }
        return null;
    }


    //配置节点信息
    int treeId = 1;
    int pTreeId = 0;//注意id从1开始，pid从0开始是为了保证一级父节点没有父节点

    /**
     * 针对父子节点结构展示的数据，为父节点增加树状结构的信息
     *
     * @param details
     */
    @Override
    protected void addTreeInfo(final List<RefDetailEntity> details) {
        if (details == null || details.size() == 0)
            return;
        for (RefDetailEntity detail : details) {
            detail.setViewType(Global.PARENT_NODE_HEADER_TYPE);
            detail.setTreeId(treeId++);
            detail.setpTreeId(pTreeId);
            detail.setHasChild(false);
        }
    }

}
