import { ElNotification } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";

/**
 * 批量处理结果消息弹窗
 */

const useBatchNotify = (param: Batch.BatchResultVo) => {
	// 1. 语言国际化处理(jsx内部处理不了国际化，所以在jsx外边处理)
	let totalHandle = useFormat18n("common.totalHandle");
	let item = useFormat18n("common.item");
	let success = useFormat18n("common.success");
	let fail = useFormat18n("common.fail");
	let errorRecords = useFormat18n("common.errorRecords");
	let rowNo = useFormat18n("common.rowNo");
	param.errList.forEach((errRow: Batch.errMsgRow) => {
		errRow.errorMsg = useFormat18n(`error.${errRow.errorMsg}`);
	});

	ElNotification({
		title: useFormat18n(["common.operate", "common.success"]),
		duration: 4500,
		type: "warning",
		customClass: "batch_notification",
		position: "bottom-right",
		message: (
			<div>
				<div>
					{totalHandle} {param.total} {item}，{success} {param.successCount} {item}，{fail} {param.errorCount} {item}
				</div>
				{param.errList.length ? <div style={{ marginBottom: "5px" }}>{errorRecords + ":"}</div> : null}
				{param.errList.map(errRow => (
					<div style={{ display: "flex", gap: "5px" }}>
						<div>
							{rowNo}:{errRow.rowNum}
						</div>
						<div>{errRow.data}</div>
						<div>{errRow.errorMsg}</div>
					</div>
				))}
			</div>
		)
	});
};

export default useBatchNotify;
