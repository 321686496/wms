import { onUnmounted, ref } from "vue";

export type Callback = (...args: any[]) => void | Promise<void>;

export const useIntervalAsync = (callback: Callback, delay: number = 3000) => {
	const pageTimer = ref(0);
	// 运行任务
	const run = async () => {
		await Promise.resolve(callback());
		// 设置下一次任务
		pageTimer.value = window.setTimeout(run, delay);
	};

	const stop = () => {
		pageTimer.value && window.clearTimeout(pageTimer.value);
		pageTimer.value = 0;
	};

	// 刷新任务
	const refresh = () => {
		stop();
		run();
	};

	// 取消任务
	onUnmounted(() => {
		stop();
	});

	return {
		run,
		stop,
		refresh
	};
};
