import dayjs from "dayjs";
import useFormat18n from "@/hooks/useFormat18n";

const useRecentDate: () => treeOption[] = () => {
	const todayDate = dayjs(new Date()).format("YYYY-MM-DD");
	const last7Date = dayjs(todayDate).subtract(7, "day").format("YYYY-MM-DD");
	const last30Date = dayjs(todayDate).subtract(30, "day").format("YYYY-MM-DD");
	const lastMonthStartDate = dayjs(todayDate).subtract(1, "month").startOf("month").format("YYYY-MM-DD");
	const lastMonthEndDate = dayjs(todayDate).subtract(1, "month").endOf("month").format("YYYY-MM-DD");
	const thisYearStartDate = dayjs(todayDate).startOf("year").format("YYYY-MM-DD");

	return [
		{ label: useFormat18n("system.today"), value: `${todayDate}~${todayDate}` },
		{ label: useFormat18n("system.last7Days"), value: `${last7Date}~${todayDate}` },
		{ label: useFormat18n("system.last30Days"), value: `${last30Date}~${todayDate}` },
		{ label: useFormat18n("system.lastMonth"), value: `${lastMonthStartDate}~${lastMonthEndDate}` },
		{ label: useFormat18n("system.thisYear"), value: `${thisYearStartDate}~${todayDate}` },
		{ label: useFormat18n("common.all"), value: `` }
	];
};

export default useRecentDate;
