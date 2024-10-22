<template>
	<div class="home card">
		<div class="not-container">
			<img src="https://p8-cdn.ponlay.com/mp-icon/404.png" class="not-img" alt="404" />
			<div class="not-detail">
				<h2>{{ $t("error.DataNotExist") }}</h2>
				<h4>{{ $t("menu.home_notfound") }}</h4>
				<el-button type="primary" @click="router.back()">{{ $t("common.back") }}</el-button>
			</div>
		</div>
	</div>
</template>
<script setup lang="ts" name="home_notfound">
import router from "@/routers";
</script>
<style lang="scss" scoped>
.home {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 100%;
	height: 100%;
}
.not-container {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 100%;
	height: 100%;
	.not-img {
		margin-right: 120px;
	}
	.not-detail {
		display: flex;
		flex-direction: column;
		h2,
		h4 {
			padding: 0;
			margin: 0;
		}
		h2 {
			font-size: 40px;
			color: var(--el-text-color-primary);
		}
		h4 {
			margin: 30px 0 20px;
			font-size: 19px;
			font-weight: normal;
			color: var(--el-text-color-regular);
		}
		.el-button {
			width: 100px;
		}
	}
}
</style>
