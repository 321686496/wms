import i18n from "@/languages";
import { Format18N } from "./interface";

/**
 *
 * @param msg 来源文本 可以是字符串或者字符串数组
 * @param segment 中间分隔符，默认为空串
 * @param reverse 是否翻转单词，默认否
 * @returns 格式化多语言文本
 */

const useFormat18n = (msg: Format18N.originType, segment: string = "", reverse: boolean = false): string => {
	const { t } = i18n.global;
	if (Array.isArray(msg)) {
		let resultArr = msg.map((str: string) => t(str));
		let resultStr = reverse ? resultArr.reverse().join(segment) : resultArr.join(segment);
		return resultStr;
	} else {
		return t(msg);
	}
};

export default useFormat18n;
