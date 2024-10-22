import { ref } from "vue";

export const useTableRadio = () => {
	const currentRow = ref();
	const currentRowChange = (val: any) => {
		currentRow.value = val;
	};
	return {
		currentRow,
		currentRowChange
	};
};
