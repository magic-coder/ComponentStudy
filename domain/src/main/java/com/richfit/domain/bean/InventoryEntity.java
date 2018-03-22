package com.richfit.domain.bean;

/**
 * 库存信息实体类
 * Created by monday on 2016/5/28.
 */
public class InventoryEntity extends TreeNode{
    public String checkLineId;
    public boolean isChecked;
    public String rowId;
    public String id;
    public String transId;
    public String storageNum;
    public String workId;
    public String workCode;
    public String invId;
    public String invCode;
    public String invName;
    public String location;
    public String batchFlag;
    public String materialId;
    public String materialNum;
    public String materialDesc;
    public String materialGroup;
    public String materialUnit;
    public String lineNum;
    public String unit;
    public String invQuantity;
    public String quantity;
    public String supplierId;
    public String supplierNum;
    public String totalQuantity;
    public String workName;
    //组合仓位值
    public String locationCombine;
    //仓储类型
    public String locationType;
    /**
     * 盘点凭证
     */
    public String checkVisa;
    /**
     * 库存类型
     */
    public String inventoryType;
    /**
     * 特殊库存标识
     */
    public String specialInvFlag;
    /**
     * 特殊库存编号
     */
    public String specialInvNum;
    public String newFlag;//新增库存标识
    public String invType;
    public String userId;
    public String checkId;
    public String invFlag;
    public String projectNum;
    //副计量单位库存数量
    public String invQuantityCustom;
    //副计量单位
    public String unitCustom;
    public String quantityCustom;
    public String creationDate;
    public String duration;
    public String year;
    public String totalQuantityCustom;

    /*报检单*/
    public String contractNum;
    public String inspectionDate;
    public String inspectionNum;
    public String planNum;
    public String remark;
    public String supplierDesc;
    public String arrivalDate;
    public String materialState;
    public String suggestBatch;
    public String suggestLocation;
    public String actLocation;

    @Override
    public String toString() {
        return "InventoryEntity{" +
                "checkLineId='" + checkLineId + '\'' +
                ", isChecked=" + isChecked +
                ", rowId='" + rowId + '\'' +
                ", id='" + id + '\'' +
                ", newFlag='" + newFlag + '\'' +
                ", transId='" + transId + '\'' +
                ", storageNum='" + storageNum + '\'' +
                ", workId='" + workId + '\'' +
                ", workCode='" + workCode + '\'' +
                ", invId='" + invId + '\'' +
                ", invCode='" + invCode + '\'' +
                ", invName='" + invName + '\'' +
                ", location='" + location + '\'' +
                ", batchFlag='" + batchFlag + '\'' +
                ", materialId='" + materialId + '\'' +
                ", materialNum='" + materialNum + '\'' +
                ", materialDesc='" + materialDesc + '\'' +
                ", materialGroup='" + materialGroup + '\'' +
                ", materialUnit='" + materialUnit + '\'' +
                ", lineNum='" + lineNum + '\'' +
                ", unit='" + unit + '\'' +
                ", invQuantity='" + invQuantity + '\'' +
                ", quantity='" + quantity + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", supplierNum='" + supplierNum + '\'' +
                ", totalQuantity='" + totalQuantity + '\'' +
                ", workName='" + workName + '\'' +
                ", checkVisa='" + checkVisa + '\'' +
                ", inventoryType='" + inventoryType + '\'' +
                ", specialInvFlag='" + specialInvFlag + '\'' +
                ", specialInvNum='" + specialInvNum + '\'' +
                '}';
    }
}
