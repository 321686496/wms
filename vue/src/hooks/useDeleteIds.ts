import { ref } from "vue";
/**
 * 处理删除ids
 */
const useDeleteIds = () => {
	const deleteIds = ref<number[]>([]);

	// 存id
	const pushId = (id: number) => {
		const has = deleteIds.value.find(v => v == id);
		if (!has) {
			deleteIds.value.push(id);
		}
	};

	// 清空
	const clearIds = () => {
		deleteIds.value = [];
	};
	return {
		deleteIds,
		clearIds,
		pushId
	};
};

export default useDeleteIds;
