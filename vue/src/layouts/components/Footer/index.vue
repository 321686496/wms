<template>
	<div class="footer flx-center">
		<a> {{ sysConfig.system_name }} </a>
		&nbsp;&nbsp;
		<a v-if="sysConfig.system_icp" href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">{{
			sysConfig.system_icp
		}}</a>
	</div>
</template>

<script setup lang="ts">
import useSysConfig from "@/hooks/useSysConfig";
const { sysConfig } = useSysConfig();
</script>

<style scoped lang="scss">
@import "./index.scss";
</style>
