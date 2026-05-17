
<template>
  <div class="page-container"></div>
</template>

<script>import { Empty } from 'ant-design-vue'

export default {
  name: 'home',
  data () {
    return {
    }
  },
  mounted () {
    this.queryAdminHome()
  },
  methods: {
    queryAdminHome () {
      this.$get('/business/web/adminHome').then((r) => {
        if (r.data.code === 0 && r.data.data) {
        }
      }).catch((r) => {
        console.error(r)
        this.$message.error('获取首页信息失败')
      })
    }
  },
  beforeDestroy () {
    if (this.chart) {
      this.chart.destroy()
    }
  }
}
</script>

<style scoped lang="less">
</style>
