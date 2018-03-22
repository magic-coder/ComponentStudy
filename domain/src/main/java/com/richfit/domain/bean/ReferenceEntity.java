package com.richfit.domain.bean;


import java.util.List;

/**
 * 单据信息
 */
public class ReferenceEntity {
    /*单据id，同时也是本地数据抬的id(主键)*/
    public String checkId;
    public String refCodeId;

    /*单据号*/
    public String checkNum;
    public String recordNum;
    /*验收抬头缓存标识*/
    public String tempFlag;
    /*盘点缓存*/
    public String checkFlag;
    /*缓存抬头id*/
    public String transId;
    public String transLineId;
    /*单据类型*/
    public String refType;
    /*移动类型*/
    public String moveType;
    //临时控制字段
    public String sapMoveType;
    /*业务类型*/
    public String bizType;
    /*工厂*/
    public String workId;
    public String workCode;
    public String workName;

    /*接收工厂*/
    public String recWorkId;
    public String recWorkCode;
    public String recWorkName;

    /*库存地点*/
    public String invId;
    public String invCode;
    public String invName;
    /*库存类型*/
    public String invType;

    /*接收库存地点*/
    public String recInvId;
    public String recInvCode;
    public String recInvName;

    /*创建人*/
    public String recordCreator;
    /*创建日期*/
    public String creationDate;
    /*更新人*/
    public String lastUpdatedBy;
    /*更新日期*/
    public String lastUpdateDate;
    /*采购号*/
    public String poNum;

    /*供应商*/
    public String supplierDesc;
    public String supplierNum;
    public String supplierId;

    /*过账日期*/
    public String voucherDate;

    /*项目编号*/
    public String projectNum;

    /*网络编号*/
    public String netNum;

    /*客户*/
    public String customer;

    /*成本中心*/
    public String costCenter;

    /*代管料出库原因*/
    public String dsReason;

    /*代管料出库类型*/
    public String asType;

    /*盘点状态*/
    public String status;

    public String storageNum;

    /*盘点总条数*/
    public int totalCount;
    /*验收类型*/
    public int inspectionType;
    /*行明细*/
    public List<RefDetailEntity> billDetailList;
    /*库存明细*/
    public List<InventoryEntity> checkList;

    public String checkLevel;
    public String specialFlag;

    public String location;
    public String materialNum;
    public String materialGroup;
    public String materialDesc;
    public String batchFlag;
    public String remark;
    public boolean qmFlag;
    public String userId;
    //是否应急标识
    public String invFlag;
    //是否项目移交物资
    public String specialInvFlag;
    //工单号
    public String jobNum;

    public int insDoor;
    public int insMaterial;
    public int insEquipe;
    public int insLocation;
    public int insSafe;
    public int insDocument;
    public int insOffice;
    public int insException;
    public int insPower;
    public int insLock;
    public String deliveryOrder;
    public String shopCondition;
    /*自定义辅助9*/
    public String zzzdy9;
    /*专项核算类别*/
    public String zzzxlb;
    /*专项核算内容*/
    public String zzzxnr;
    public String team;
    public String inspectionUnit;
    public String arrivalDate;
    public String inspectionDate;
    public String inspectionStandard;
    public String post;
    public String manufacture;
    //报检单位
    public String declaredUnit;
    public String glAccount;
    //供应商评价
    public String supplierEvaluation;
    public String locationType;
    public Object recordDate;
    public int insClean;
    public int insQuantity;
    public int insChemical;
    public int insFire;
    public int insTh;
    public int insHazardous;
    public int insFlammable;
    public int insEffective;
    public String insWeather;
    public String insTemperature;
    public String insHumidity;
    public String createdBy;
    public String quantityCustom;
    public String orderNum;
    public String projectNumDesc;
    public String costCenterDesc;
    public String businessScope;
    public String profitCenter;
    public String network;
    public String deliveryTo;
    public String reqCompany;
    public String sapMoveCause;
    public String glf;
    public String lyf;
    public String ckf;
    public String yfhj;
    public String moveCause;
    public String serialNum;
}
