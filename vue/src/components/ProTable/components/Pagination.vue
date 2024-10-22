<template>
	<!-- 分页组件 -->
	<el-pagination
		small
		:current-page="pageable.current"
		:page-size="pageable.pageSize"
		:page-sizes="[10, 25, 50, 100, 200]"
		:background="true"
		layout="total, sizes, prev, pager, next"
		:total="pageable.total"
		@size-change="handleSizeChange"
		@current-change="handleCurrentChange"
	></el-pagination>
</template>

<script setup lang="ts" name="Pagination">
interface Pageable {
	current: number;
	pageSize: number;
	total: number;
}

interface PaginationProps {
	pageable: Pageable;
	handleSizeChange: (size: number) => void;
	handleCurrentChange: (currentPage: number) => void;
}

defineProps<PaginationProps>();
</script>
