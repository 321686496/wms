package com.hongan.template.base.entity;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: BaseError
 */
public enum BaseError {

    /*
     * 服务器内部错误
     */
    ServerError(50001, "InternalServerError", "系统错误"),
    DBError(50002, "DBError", "数据库错误"),
    IOFileError(50003, "IOFileError", "IO文件异常"),
    IOHttpStreamError(50004, "IOHttpStreamError", "IOHttp流错误"),
    JSONError(50005, "JSONError", "JSON序列化失败"),
    WxAPIError(20088, "WxAPIError", "微信接口异常"),
    FormatError("FormatError", "格式化失败"),
    LoginUserTypeError(20088, "LoginUserTypeError", "登陆用户类型异常"),

    /*
     * 用户请求错误
     */
    BadRequest(40001, "BadRequest", "错误的请求"),
    AuthError(40100, "AuthError", "未授权登陆"),
    AccessForbidden(40300, "AccessForbidden", "无访问权限"),
    NotFound(40400, "NotFound", "找不到文件"),


    /**
     * 用户操作异常
     */
    MaterialTypeError("MaterTypeError", "商品类别不存在"),
    PhoneFormatError("PhoneFormatError", "手机号格式错误"),
    PasswordFormatError("PasswordFormatError", "密码格式错误"),
    ImgCaptchaError("ImgCaptchaError", "图片验证码错误"),
    PhoneCaptchaError("PhoneCaptchaError", "手机验证码错误"),
    PasswordError("PasswordError", "密码错误"),
    SendPhoneCaptchaError("SendPhoneCaptchaError", "手机验证码发送失败"),
    DefaultRoleProhibitDelete("DefaultRoleProhibitDelete", "默认角色禁止删除"),
    RoleExistUserProhibitDelete("RoleExistUserProhibitDelete", "角色下存在用户,禁止删除"),
    DefaultDeptProhibitDelete("DefaultDeptProhibitDelete", "默认部门禁止删除"),
    DefaultAdminProhibitDelete("DefaultAdminProhibitDelete", "默认管理员禁止删除"),
    AdminNotExist("AdminNotExist", "账号信息不存在"),
    AdminIsLock("AdminIsLock", "账号已被禁用"),
    DataNotExist("DataNotExist", "数据不存在"),
    PhoneIsRegistered("PhoneIsRegistered", "该手机号已注册"),
    TwoPasswordNotMatch("TwoPasswordNotMatch", "两次密码不一致"),
    PhoneIsRegisteredPleaseLogoff("PhoneIsRegisteredPleaseLogoff", "该手机号已注册，请先注销"),
    PleaseInputPhone("PleaseInputPhone", "请填写手机号"),
    PleaseSelectRole("PleaseSelectRole", "请选择所属角色"),
    PleaseSelectDept("PleaseSelectDept", "请选择所属部门"),
    PleaseInputName("PleaseInputName", "请填写名称"),
    RoleKeyRepeat("RoleKeyRepeat", "角色关键字重复"),
    DeptExistUserProhibitDelete("DeptExistUserProhibitDelete", "部门下存在用户,禁止删除"),
    PleaseSelectParentDept("PleaseSelectParentDept", "请选择上级部门"),
    NameRepeat("NameRepeat", "名称重复"),
    ProhibitSettingSuperRole("ProhibitSettingSuperRole", "禁止配置超级管理员权限"),
    ImgFileFormatError("ImgFileFormatError", "图片文件格式错误,仅支持jpg/jpeg/png格式图片"),
    VideoFileFormatError("VideoFileFormatError", "视频文件格式错误,仅支持mp4格式视频"),
    FileImportFormatError("FileImportFormatError", "文件导入格式错误,仅支持xls格式文件"),
    CodeRepeat("CodeRepeat", "编码重复"),
    PleaseUploadImportTemplate("PleaseUploadImportTemplate", "请上传导入模板"),
    ParentCodeExist("ParentCodeExist", "上级编码不存在"),
    OnlySupportsThreeLevelCategory("OnlySupportsThreeLevelCategory", "仅支持三级分类"),
    LeaderNameRepeat("LeaderNameRepeat", "负责人名称重复"),
    PleaseSelectStock("PleaseSelectStock", "请选择仓库"),
    SpecRepeat("SpecRepeat", "规格重复"),
    PleaseInputSpec("PleaseInputSpec", "请填写规格"),
    CategoryUsedProhibitDelete("CategoryUsedProhibitDelete", "分类正在被使用,禁止删除"),
    BrandUsedProhibitDelete("BrandUsedProhibitDelete", "品牌正在被使用,禁止删除"),
    LocationUsedProhibitDelete("LocationUsedProhibitDelete", "库位正在被使用,禁止删除"),
    StockUsedProhibitDelete("StockUsedProhibitDelete", "仓库正在被使用,禁止删除"),
    ParentNameExist("ParentNameExist", "上级名称不存在"),
    PleaseInputValue("PleaseInputValue", "请填写值"),
    OnlyEnableBatchNumberOrEnableSerialNumber("OnlyEnableBatchNumberOrEnableSerialNumber", "仅能启用批次号或启用序列号"),
    MainUnitProhibitDisabled("MainUnitProhibitDisabled", "主单位禁止禁用"),
    MainUnitProhibitDelete("MainUnitProhibitDelete", "主单位禁止删除"),
    BillDataNotExist("BillDataNotExist", "单据数据不存在"),
    BillItemCannotEmpty("BillItemCannotEmpty", "单据明细不能为空"),
    PleaseSelectUnit("PleaseSelectUnit", "请选择单位"),
    PleaseSelectMaterial("PleaseSelectMaterial", "请选择物料"),
    PleaseInputSerialNumber("PleaseInputSerialNumber", "请填写序列号"),
    PleaseSelectSerialNumber("PleaseSelectSerialNumber", "请选择序列号"),
    PleaseInputBatchNumber("PleaseInputBatchNumber", "请填写批次号"),
    PleaseSelectBatchNumber("PleaseSelectBatchNumber", "请选择批次号"),
    NumberInconsistentWithSerialNumber("NumberInconsistentWithSerialNumber", "数量与序列号不一致"),
    SerialNumberRepeat("SerialNumberRepeat", "序列号重复"),
    BatchNumberNumberRepeat("BatchNumberNumberRepeat", "批次号重复"),
    FileFormatError("FileFormatError", "文件格式错误,仅支持jpg/jpeg/png/xls/xlsx/pdf/doc/docx格式文件"),
    MaterialHasStockProhibitDelete("MaterialHasStockProhibitDelete", "物料存在库存禁止删除"),
    MaterialSkuHasStockProhibitDelete("MaterialSkuHasStockProhibitDelete", "物料规格存在库存禁止删除"),
    SerialNumberNotExist("SerialNumberNotExist", "序列号不存在"),
    SerialNumberUsed("SerialNumberUsed", "序列号已被使用"),
    BatchNumberNotExist("BatchNumberNotExist", "批次号不存在"),
    BatchNumberUsed("BatchNumberUsed", "批次号已被使用"),
    BatchNumberInsufficientInventory("BatchNumberInsufficientInventory", "批次库存不足"),
    InsufficientInventory("InsufficientInventory", "库存不足"),
    MaterialSkuCodeNotExist("MaterialSkuCodeNotExist", "物料Sku编码不存在"),
    CurrentStatusBillProhibitSave("CurrentStatusBillProhibitSave", "当前单据状态禁止保存"),
    CurrentStatusBillProhibitAudit("CurrentStatusBillProhibitAudit", "当前单据状态禁止审核"),
    CurrentStatusBillProhibitAbandonAudit("CurrentStatusBillProhibitAbandonAudit", "当前单据状态禁止弃审"),
    CurrentStatusBillProhibitDelete("CurrentStatusBillProhibitDelete", "当前单据状态禁止删除"),
    PleaseInputAuditReason("PleaseInputAuditReason", "请填写审核原因"),
    ExistNegativeInventoryProhibitClose("ExistNegativeInventoryProhibitClose", "存在负库存禁止关闭"),

    PleaseSelectSupplier("PleaseSelectSupplier", "请选择供应商"),
    PleaseSelectCustomer("PleaseSelectCustomer", "请选择客户"),
    LocationExistInventoryProhibitClose("LocationExistInventoryProhibitClose", "库位存在库存禁止关闭"),
    CurrentStatusBillProhibitPushDown("CurrentStatusBillProhibitPushDown", "当前单据状态禁止下推"),
    CurrentStatusBillProhibitMerge("CurrentStatusBillProhibitMerge", "当前单据状态禁止合并"),
    NumberExceedsSourceBillNumber("NumberExceedsSourceBillNumber", "数量超出原单据数量"),
    NumberValueError("NumberValueError", "数量取值错误"),
    SourcePurchaseInstockNotExistSerialNumber("SourcePurchaseInstockNotExistSerialNumber", "原始采购入库单不存在该序列号"),
    SourcePurchaseInstockNotExistBatchNumber("SourcePurchaseInstockNotExistBatchNumber", "原始采购入库单不存在该批次号"),
    SerialNumberOutstock("SerialNumberOutstock", "序列号已出库"),
    SerialNumberNotOutstock("SerialNumberNotOutstock", "序列号未出库"),
    BillPaymentProhibitAbandonAudit("BillPaymentProhibitAbandonAudit", "单据已付款禁止弃审"),
    BillReceiptProhibitAbandonAudit("BillPaymentProhibitAbandonAudit", "单据已收款禁止弃审"),
    PaymentRecordDataNotExist("PaymentRecordDataNotExist", "付款明细数据不存在"),
    ReceiptRecordDataNotExist("ReceiptRecordDataNotExist", "收款明细数据不存在"),
    PleaseSelectPaytype("PleaseSelectPaytype", "请选择付款方式"),
    PleaseInputPrinterSn("PleaseInputPrinterSn", "请填写打印机编号"),
    PleaseInputPrinterKey("PleaseInputPrinterKey", "请填写打印机密钥"),
    FeieyunAPIError("FeieyunAPIError", "飞鹅云接口异常"),
    PrinterSnError("PrinterSnError", "打印机编号错误"),
    PrinterSnKeyError("PrinterSnKeyError", "打印机编号和KEY不正确"),
    PrinterHasBeenAdded("PrinterHasBeenAdded", "打印机已被添加过"),
    DeleteError("DeleteError", "删除失败"),
    PrinterIsDisabled("PrinterIsDisabled", "该打印机已禁用"),
    PleaseCheckPrinterIsNormal("PleaseCheckPrinterIsNormal", "请检查打印机是否正常"),
    PleaseSelectSourceBill("PleaseSelectSourceBill", "请选择上游单据"),
    SerialNumberIsBoxed("SerialNumberIsBoxed", "序列号已装箱"),
    BillAmountIsAllSettlement("BillAmountIsAllSettlement", "单据金额已全部结清"),
    SourceBillTypeError("SourceBillTypeError", "上游单据类型异常"),
    SerialNumberIsNotBoxed("SerialNumberIsNotBoxed", "序列号未装箱"),
    PleaseInputStockName("PleaseInputStockName", "请填写仓库名称"),
    StockNotExist("StockNotExist", "仓库不存在"),
    ParentCannotIsSelf("ParentCannotIsSelf", "父类不能为自己"),
    PleaseInputMaterialName("PleaseInputMaterialName", "请填写物料名称"),
    MaterialCategoryNotExist("MaterialCategoryNotExist", "物料分类不存在"),
    MaterialBrandNotExist("MaterialBrandNotExist", "物料品牌不存在"),
    PleaseInputSkuSpec("PleaseInputSkuSpec", "请填写SKU规格"),
    PleaseInputMaterialUnit("PleaseInputMaterialUnit", "请填写物料单位"),
    PayAmountError("PayAmountError", "付款金额错误"),
    PayAmountExceeds("PayAmountExceeds", "付款金额超出"),
    ReceiptAmountError("ReceiptAmountError", "收款金额错误"),
    ReceiptAmountExceeds("ReceiptAmountExceeds", "收款金额超出"),
    PrepaidAmountExceedsAvailableBalance("PrepaidAmountExceedsAvailableBalance", "预付金额超出可用余额"),
    PrepaidReceiptAmountExceedsAvailableBalance("PrepaidReceiptAmountExceedsAvailableBalance", "预收金额超出可用余额"),
    PrepaidAmountExceedsPayAmount("PrepaidAmountExceedsAvailableBalance", "预付金额超出付款金额"),
    PrepaidReceiptAmountExceedsReceiptAmount("PrepaidReceiptAmountExceedsReceiptAmount", "预收金额超出收款金额"),
    PayAmountExceedsUnpayAmount("PayAmountExceedsUnpayAmount", "付款金额超出未付金额"),
    ReceiptAmountExceedsUnpayAmount("PayAmountExceedsUnpayAmount", "收款金额超出未收金额"),
    PaymentRecordIsAllSettlement("PaymentRecordIsAllSettlement", "应付事项已结清"),
    ReceiptRecordIsAllSettlement("ReceiptRecordIsAllSettlement", "应收事项已结清"),
    AdvancePaymentAmountError("AdvancePaymentAmountError", "预付金额错误"),
    AdvanceReceiptAmountError("AdvanceReceiptAmountError", "预收金额错误"),
    PleaseSelectParentMaterial("PleaseSelectParentMaterial", "请选择父件"),
    PleaseSelectParentMaterialUnit("PleaseSelectParentMaterialUnit", "请选择父件单位"),
    ParentMaterialNumberError("ParentMaterialNumberError", "父件数量错误"),
    PleaseSelectChildrenMaterial("PleaseSelectChildrenMaterial", "请选择子件"),
    PleaseSelectChildrenMaterialUnit("PleaseSelectChildrenMaterialUnit", "请选择子件单位"),
    ChildrenMaterialNumberError("ChildrenMaterialNumberError", "子件数量错误"),
    DefaultDataProhibitDelete("DefaultDataProhibitDelete", "默认数据禁止删除"),
    PleaseInputBomVersion("PleaseInputBomVersion", "请填写Bom版本号"),
    MaterialBomVersionRepeat("MaterialBomVersionRepeat", "同一物料Bom版本号重复"),
    PleaseRefundType("PleaseRefundType", "请选择退换类型"),
    OnlyRefundBillNoNeedPush("OnlyRefundBillNoNeedPush", "仅退款单据无需下推"),
    SourceBillNotAudit("SourceBillNotAudit", "来源单据未审核"),
    OutstockListNotExistMaterialSkuCode("OutstockListNotExistMaterialSkuCode", "出库列表中未存在该物料编码"),
    MaterialAllInBox("MaterialAllInBox", "该物料已全部装箱"),
    PleaseSelectMrpOperationBill("PleaseSelectMrpOperationBill", "请选择MRP运算单据"),
    PleaseSelectOperationScheme("PleaseSelectOperationScheme", "请选择MRP运算方案"),
    PleaseSelectPurchaseDate("PleaseSelectPurchaseDate", "请选择采购日期"),
    DataIsUsedProhibitDelete("DataIsUsedProhibitDelete", "数据正在被使用,禁止删除"),
    ExecutionSortError("ExecutionSortError", "加工顺序错误"),
    MaterialSkuHasBomProhibitDelete("MaterialSkuHasBomProhibitDelete", "物料规格正在Bom中使用，禁止删除"),
    PleaseSelectProductionDept("PleaseSelectProductionDept", "请选择生产部门"),
    PleaseSelectBomVersion("PleaseSelectBomVersion", "请选择Bom版本"),
    PleaseSelectWorkmanship("PleaseSelectWorkmanship", "请选择工艺路线"),
    PleaseSelectProductionAdmin("PleaseSelectProductionAdmin", "请选择生产工人"),
    NumberExceedsClaimableNumber("NumberExceedsClaimableNumber", "数量超出可认领数量"),
    BillItemIsAllPick("BillItemIsAllPick", "单据已全部领料"),
    BillItemIsAllReturn("BillItemIsAllReturn", "单据已全部还料"),
    BillItemIsAllReport("BillItemIsAllReport", "单据已全部报工"),
    NumberExceedsReportNumber("NumberExceedsReportNumber", "数量超出可上报数量"),
    InspectionNumberMismatch("InspectionNumberMismatch", "质检数量不匹配"),
    CurrentBillNotNeedInspection("CurrentBillNotNeedInspection", "当前单据无需质检"),
    CurrentBillNeedInspection("CurrentBillNeedInspection", "当前单据需要质检"),
    BillItemIsNotInspectionData("BillItemIsNotInspectionData", "单据无可质检数据"),
    InspectionNumberExceeds("InspectionNumberExceeds", "质检数量超出"),
    ExistChildrenBillProhibitAbandonAudit("ExistChildrenBillProhibitAbandonAudit", "存在下游单据禁止弃审"),
    ExistChildrenBillProhibitAbandonDelete("ExistChildrenBillProhibitAbandonDelete", "存在下游单据禁止删除"),
    BillItemIsNotInstockData("BillItemIsNotInstockData", "单据无可入库数据"),
    NumberExceedsInstockNumber("NumberExceedsInstockNumber", "数量超出可入库数量"),
    CurrentStatusBillProhibitManualCompletion("CurrentStatusBillProhibitManualCompletion", "当前单据状态禁止手动完结"),
    CurrentBillTypeProhibitPushDown("CurrentBillTypeProhibitPushDown", "当前单据类型禁止下推"),
    UnqualifiedProductionNumberExceeds("UnqualifiedProductionNumberExceeds", "工废再生产数量超出"),
    BillItemIsNotPushData("BillItemIsNotPushData", "单据无可下推数据"),
    SourceOutsourceInstockNotExistSerialNumber("SourceOutsourceInstockNotExistSerialNumber", "原始委外入库单不存在该序列号"),
    SourceOutsourceInstockNotExistBatchNumber("SourceOutsourceInstockNotExistBatchNumber", "原始委外入库单不存在该批次号"),
    PleaseSelectProductionStartDate("PleaseSelectProductionStartDate", "请选择开工日期"),
    PleaseSelectOutstockStock("PleaseSelectOutstockStock", "请选择出库仓库"),
    PleaseSelectInstockStock("PleaseSelectInstockStock", "请选择入库仓库"),
    TransferInAndOutStockCannotSame("TransferInAndOutStockCannotSame", "调入仓库与调出仓库不能相同"),
    ExistMaterialIsInventorying("ExistMaterialIsInventorying", "存在正在盘点的物料"),
    TransferLocationCannotSame("TransferLocationCannotSame", "调整库位不能相同"),
    ConsumablesProhibitEnableBatchNumberOrEnableSerialNumber("ConsumablesProhibitEnableBatchNumberOrEnableSerialNumber", "耗材禁止启用批次号或启用序列号"),
    PartialProcedureNotCompletedPleaseCompleteBeforeAssignOrReceive("PartialProcedureNotCompletedPleaseCompleteBeforeAssignOrReceive", "部分工序未完成,请先完成后再分配或领取"),
    ProductionProcedureWorkIsEmpty("ProductionProcedureWorkIsEmpty", "暂无工序执行单"),
    CurrentProductionProcedureIsNotWorkData("CurrentProductionProcedureIsNotWorkData", "当前工序暂无执行数据"),
    PleaseSelectAdmin("PleaseSelectAdmin", "请选择员工"),
    PleaseSelectSalaryProject("PleaseSelectSalaryProject", "请选择薪资项目"),
    RecordIsWriteOffProhibitAbandonAudit("RecordIsWriteOffProhibitAbandonAudit", "记录已发放,禁止弃审"),
    ProcedureWorkSalaryIsVerifyPass("ProcedureWorkSalaryIsVerifyPass", "工序工资已审核"),
    ProcedureWorkSalaryIsVerifyPassProhibitAbandonAudit("ProcedureWorkSalaryIsVerifyPassProhibitAbandonAudit", "工序工资已审核,禁止弃审"),
    ProcedureWorkSalaryIsNotVerifyPassProhibitAbandonAudit("ProcedureWorkSalaryIsNotVerifyPass", "工序工资未审核,禁止弃审"),
    ProcedureWorkSalaryIsWriteOffProhibitAbandonAudit("ProcedureWorkSalaryIsWriteOffProhibitAbandonAudit", "工序工资已发放,禁止弃审"),
    SalaryAddRecordIsWriteOff("SalaryAddRecordIsWriteOff", "薪资增项记录已发放"),
    SalaryReduceRecordIsWriteOff("SalaryReduceRecordIsWriteOff", "薪资减项记录已发放"),
    WagesCountRecordIsWriteOff("WagesCountRecordIsWriteOff", "计件工资记录已发放"),
    WagesTimeRecordIsWriteOff("WagesTimeRecordIsWriteOff", "计时工资记录已发放"),
    PleaseSelectAttendanceCalendar("PleaseSelectAttendanceCalendar", "请选择工作日历"),
    PleaseInputAttendanceClass("PleaseInputAttendanceClass", "请填写工作班次"),
    PleaseSelectAttendanceClockAddress("PleaseSelectAttendanceClockAddress", "请选择考勤打卡地点"),
    DeptHasConfiguredAttendanceRules("DeptHasConfiguredAttendanceRules", "部门已配置考勤规则"),
    AdminHasConfiguredAttendanceRules("AdminHasConfiguredAttendanceRules", "员工已配置考勤规则"),
    NotAttendanceRules("NotAttendanceRules", "暂无考勤规则"),
    NotInClockAddressRange("NotInClockAddressRange", "未在打卡范围"),
    WorkingTimeIsBeforeOffTime("WorkingTimeIsBeforeOffTime", "下班时间不能早于上班时间"),
    PleaseArrangeShiftsInChronologicalOrderTimeCannotOverlap("PleaseArrangeShiftsInChronologicalOrderTimeCannotOverlap", "请按时间顺序排班，时间不能重叠"),
    NotClockAttendanceClass("NotClockAttendanceClass", "无可打卡班次"),
    NotReplacementClock("NotReplacementClock", "无可补卡记录"),
    OnlyAbsenteeismCanReplacementClock("OnlyAbsenteeismCanReplacementClock", "仅旷工状态可补卡"),
    ProhibitReplacementClock("ProhibitReplacementClock", "禁止补卡"),
    ExceedingReplacementClockTime("ExceedingReplacementClockTime", "超出可补卡时间"),
    CurrentMonthNotReplacementClockCount("CurrentMonthNotReplacementClockCount", "本月无可补卡次数"),
    RetainsSmallDigitOnlyIncreaseCannotReduce("RetainsSmallDigitOnlyIncreaseCannotReduce", "保留小数位只能增加不能减少"),
    SmallDigitRetainsMaxValueIs6("SmallDigitRetainsMaxValueIs6", "小数位最多保留6位"),
    ExistMaterialUseBatchNumberProhibitClose("ExistMaterialUseBatchNumberProhibitClose", "存在使用批次号的物料，禁止关闭"),
    ExistMaterialUseSerialNumberProhibitClose("ExistMaterialUseSerialNumberProhibitClose", "存在使用序列号的物料，禁止关闭"),
    DeductionAmountExceedsUnpayAmount("DeductionAmountExceedsUnpayAmount", "扣除金额超出未付金额"),
    DeductionAmountExceedsUnReceiptAmount("DeductionAmountExceedsUnReceiptAmount", "扣除金额超出未收金额"),
    AmountLessThenZeroCannotDeductionAmount("AmountLessThenZeroCannotDeductionAmount", "金额小于0无法进行扣款"),
    PleaseSelectBillTypes("PleaseSelectBillTypes", "请选择单据类型"),
    PleaseSelectBillStatus("PleaseSelectBillStatus", "请选择单据状态"),
    BillNotAudit("BillNotAudit", "单据未审核"),
    TaskIsNotAudit("TaskIsNotAudit", "任务未审核"),
    TaskIsNotStart("TaskIsNotStart", "任务未开始"),
    OnlyInProgressTaskCanTerminated("OnlyInProgressTaskCanTerminated", "仅进行中的任务可以终止"),
    OnlyInProgressTaskCanCompleted("OnlyInProgressTaskCanCompleted", "仅进行中的任务可以完结"),
    PleaseConfirmDelivery("PleaseConfirmDelivery", "请先确认配送"),
    PleaseConfirmArrived("PleaseConfirmArrived", "请先确认送达"),
    PleaseInputTitle("PleaseInputTitle", "请填写标题"),
    PleaseSelectReceivePerson("PleaseSelectReceivePerson", "请选择接收人"),
    OnlySendPersonCanDeleteMessage("OnlySendPersonCanDeleteMessage", "仅发送者可以删除消息"),
    TheSenderAndReceiverCannotBeSame("TheSenderAndReceiverCannotBeSame", "消息发送人和接收人不能相同"),
    AtLeastKeepOneDefaultData("AtLeastKeepOneDefaultData", "至少保留一条默认数据"),
    PleaseSelectMaintenanceEquipment("PleaseSelectMaintenanceEquipment", "请选择维修设备"),
    BillIsMrpOperationProhibitDelete("BillIsMrpOperationProhibitDelete", "单据已MRP运算禁止删除"),
    BillIsMrpOperationProhibitAbandonAudit("BillIsMrpOperationProhibitAbandonAudit", "单据已MRP运算禁止弃审"),
    MaterialNotExist("MaterialNotExist", "物料数据不存在"),
    MaterialSkuNotExist("MaterialSkuNotExist", "物料Sku数据不存在"),
    ProductionOrderNotSelectCostAccountingRule("ProductionOrderNotSelectCostAccountingRule", "生产订单未选择成本核算规则"),
    ProductionOrderHasCostAccounting("ProductionOrderHasCostAccounting", "生产订单已进行成本核算"),
    PleaseSelectParentDistrict("PleaseSelectParentDistrict", "请选择上级行政区"),
    SupportsUpToFourLevelsOfDistrict("SupportsUpToFourLevelsOfDistrict", "最多支持四级行政区"),
    TenantIsDisabled("TenantIsDisabled", "该租户已被已禁用"),
    TransferAmountError("TransferAmountError", "转账金额错误"),
    PleaseSelectVerifyPersonMessage("PleaseSelectVerifyPersonMessage", "请选择审核人信息"),
    BillVerifyRuleRepeat("BillVerifyRuleRepeat", "单据审核规则重复"),
    VerifyRuleSaveError("VerifyRuleSaveError", "审核规则保存失败"),
    BillVerifyError("BillVerifyError", "单据审核失败"),
    YouDoNotHaveVerifyPermissions("YouDoNotHaveVerifyPermissions", "您当前没有审核权限"),
    YouDoNotHaveAbandonVerifyPermissions("YouDoNotHaveAbandonVerifyPermissions", "您当前没有弃审权限"),
    YouHaveCompletedTheBillVerify("YouHaveCompletedTheBillVerify", "您已完成单据审核"),
    YouHaveCompletedTheBillAbandonVerify("YouHaveCompletedTheBillAbandonVerify", "您已完成单据弃审"),
    PleaseWaitOtherPersonCompleteBillVerify("PleaseWaitOtherPersonCompleteBillVerify", "请等待其他人员单据审核完成"),
    PleaseWaitOtherPersonCompleteBillAbandonVerify("PleaseWaitOtherPersonCompleteBillAbandonVerify", "请等待其他人员单据弃审完成"),
    StockExistInventoryProhibitDelete("StockExistInventoryProhibitDelete", "仓库存在库存禁止删除"),
    LocationExistInventoryProhibitDelete("LocationExistInventoryProhibitDelete", "库位存在库存禁止删除"),
    ProductionConductedCostAccountingProhibitAbandonAudit("ProductionConductedCostAccountingProhibitAbandonAudit", "生产订单已进行成本核算禁止弃审"),
    PictureSearchCountInsufficient("PictureSearchCountInsufficient", "以图识物次数不足"),
    BillItemIsNotPushDataOrMaterialsWithoutPurchaseAttributes("BillItemIsNotPushDataOrMaterialsWithoutPurchaseAttributes", "单据无可下推数据或物料无采购属性"),
    BillItemIsNotPushDataOrMaterialsWithoutProductionAttributes("BillItemIsNotPushDataOrMaterialsWithoutProductionAttributes", "单据无可下推数据或物料无生产属性"),
    BillItemIsNotPushDataOrMaterialsWithoutOutsourceAttributes("BillItemIsNotPushDataOrMaterialsWithoutOutsourceAttributes", "单据无可下推数据或物料无委外属性"),
    OnlyBusinessTypeIsEntrustMachiningBillCanPushEntrustMaterialInstockBill("OnlyBusinessTypeIsEntrustMachiningBillCanPushEntrustMaterialInstockBill", "仅业务类型为委受托加工的单据可以下推受托入库单"),
    PleaseSelectCostAccountingRule("PleaseSelectCostAccountingRule", "请选择成本核算规则"),
    InvoiceAmountExceeded("InvoiceAmountExceeded", "开票金额超出"),
    InvoiceNumberExceeded("InvoiceNumberExceeded", "开票数量超出"),
    OnlyRefundTypeCanPushSaleInvoice("OnlyRefundTypeCanPushSaleInvoice", "仅仅退款类型可以下推销项发票"),
    OnlyReturnsRefundsTypeCanPushSaleInvoice("OnlyReturnsRefundsTypeCanPushSaleInvoice", "仅退货退款类型可以下推销项发票"),
    OnlyRefundTypeCanPushPurchaseInvoice("OnlyRefundTypeCanPushPurchaseInvoice", "仅仅退款类型可以下推进项发票"),
    OnlyReturnsRefundsTypeCanPushPurchaseInvoice("OnlyReturnsRefundsTypeCanPushPurchaseInvoice", "仅退货退款类型可以下推进项发票"),
    MaterialNotHasPurchaseInspectionReportTemplate("MaterialNotHasPurchaseInspectionReportTemplate", "物料没有来料质检报告模板"),
    ExistUnAuditPurchaseInspectionReport("ExistUnAuditPurchaseInspectionReport", "存在未审核的质检报告"),
    OnlyCreatedAdminCanOperate("OnlyCreatedAdminCanOperate", "仅创建者可以操作"),
    ExistPurchaseInspectionReportProhibitDelete("ExistPurchaseInspectionReportProhibitDelete", "存在质检报告数据禁止删除"),
    OnlyInitAdminCanClearData("OnlyInitAdminCanClearData", "仅初始管理员可以清除数据"),
    OnlyInitAdminCanLogout("OnlyInitAdminCanLogout", "仅初始管理员可以执行注销操作"),
    MaterialNotHasInspectionSaleReportTemplate("MaterialNotHasInspectionSaleReportTemplate", "物料没有出货质检报告模板"),
    MaterialNotHasInspectionFirstReportTemplate("MaterialNotHasInspectionFirstReportTemplate", "物料没有首件质检报告模板"),
    BillCustomerInconsistent("BillCustomerInconsistent", "单据客户不一致"),
    BillSupplierInconsistent("BillSupplierInconsistent", "单据供应商不一致"),
    BillBusinessTypeInconsistent("BillBusinessTypeInconsistent", "单据业务类型不一致"),
    PartBillOpenStockLockProhibitMergeOustock("PartBillOpenStockLockProhibitMergeOustock", "部分单据开启了库存锁定禁止合并出库"),
    PleaseSelectMultipleLendMerge("PleaseSelectMultipleLendMerge", "请选择多个借出单合并"),
    StockLendNotExistSerialNumber("StockLendNotExistSerialNumber", "借出单不存在该序列号"),
    StockLendNotExistBatchNumber("StockLendNotExistBatchNumber", "借出单不存在该批次号"),
    TheSerialNumberIsReturn("TheSerialNumberIsReturn", "该序列号已归还"),
    BillItemIsAllMrpOperation("BillItemIsAllMrpOperation", "单据明细已全部MRP运算"),
    NotDefaultOperationScheme("NotDefaultOperationScheme", "无默认MRP运算方案"),
    MaterialHasNotDefaultBom("MaterialHasNotDefaultBom", "物料无默认Bom"),
    ItemIsPushedBillProhibitConversion("ItemIsPushedBillProhibitConversion", "明细已下推单据,禁止转换"),
    ProductionCompletedProhibitAbandonAudit("ProductionCompletedProhibitAbandonAudit", "生产已完工,禁止弃审"),
    LeastSelectOneData("LeastSelectOneData", "最少选择一条数据"),
    AllMaterialsNeedPickOutstockBeforeProductionCompletion("AllMaterialsNeedPickOutstockBeforeProductionCompletion", "完工入库前需要原材料全部领料出库"),
    InspectionResultErrorProhibitSubmit("InspectionResultErrorProhibitSubmit", "质检结果错误禁止提交"),
    OnlyExchangeBillCanPushPurchaseRefundInstock("OnlyExchangeBillCanPushPurchaseRefundInstock", "仅换货类型可下推采购换货入库"),
    OnlyExchangeBillCanPushSaleRefundOutstock("OnlyExchangeBillCanPushSaleRefundOutstock", "仅换货类型可下推销售换货出库"),
    OnlyExchangeBillCanPushOutsourceRefundOutstock("OnlyExchangeBillCanPushOutsourceRefundOutstock", "仅换货类型可下推委外换货入库"),
    PleaseSelectWasteStock("PleaseSelectWasteStock", "请选择废品仓"),
    ProductionOrderCompleted("ProductionOrderCompleted", "生产订单已完工"),
    BillCodeRepeat("BillCodeRepeat", "单据编号重复"),
    CategoryIsUsedByProduct("CategoryIsUsedByProduct", "该分类正在被使用"),
    WithdrawalAmountError("WithdrawalAmountError", "提现金额错误"),
    AvailableBalanceInsufficient("AvailableBalanceInsufficient", "可用余额不足"),
    WithdrawableBalanceInsufficient("WithdrawableBalanceInsufficient", "可提现余额不足"),
    PleaseInputEvaluate("PleaseInputEvaluate", "请输入评论内容"),
    PleaseInputLotteryProbability("PleaseInputLotteryProbability", "请填写抽奖概率"),
    LotteryProbabilityRangeError("LotteryProbabilityRangeError", "抽奖概率需要在0%~100%之间"),
    PleaseSelectLotteryPrizeGoods("PleaseSelectLotteryPrizeGoods", "请选择抽奖奖品商品"),
    PleaseInputLotteryPrizeAmount("PleaseInputLotteryPrizeAmount", "请选择抽奖奖品金额"),
    PleaseSelectStartTime("PleaseSelectStartTime", "请选择开始时间"),
    PleaseSelectEndTime("PleaseSelectEndTime", "请选择结束时间"),
    StartTimeIsAfterEndTime("StartTimeIsAfterEndTime", "开始时间晚于结束时间"),
    LotteryIsDisable("LotteryIsDisable", "抽奖活动已下架！"),
    LotteryUnStart("LotteryUnStart", "抽奖活动未开始！"),
    LotteryIsEnd("LotteryIsEnd", "抽奖活动已结束！"),
    YouHaveParticipatedLottery("YouHaveParticipatedLottery", "您已参与过该抽奖活动！"),


    DBNotFoundUser(20031, "DBNotFoundUser"),

    DBExist(20150, "DBExist"),


    DBExistEmail(20151, "DBExistEmail"),

    DBExistPhone(20152, "DBExistPhone"),

    DBExistName(20153, "DBExistName"),

    DBDeleteError(20154, "DBDeleteError"),

    DBSaveError(20155, "DBSaveError"),

    ErrorReceiveStatus(20062, "ErrorReceiveStatus"),

    ;

    private int status;
    private String message;
    private String remark;

    BaseError(int status, String message, String remark) {
        this.status = status;
        this.message = message;
        this.remark = remark;
    }

    BaseError(int status, String message) {
        this.status = status;
        this.message = message;
        this.remark = "";
    }

    BaseError(String message) {
        this.status = 20088;
        this.remark = "";
        this.message = message;
    }

    BaseError(String message, String remark) {
        this.status = 20088;
        this.remark = remark;
        this.message = message;
    }


    public static BaseError checkFromMessage(String message) {
        for (BaseError error : BaseError.values()) {
            if (error.getMessage().equals(message)) {
                return error;
            }
        }
        return BaseError.BadRequest;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
