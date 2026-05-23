
<template>
  <div class="user-profile-container" style="width: 100%">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">方向分布统计</h1>
      <p class="page-subtitle">展示各方向人员分布情况及占比</p>
    </div>

    <!-- 加载状态 -->
    <a-spin v-if="loading" size="large" tip="加载中..." class="loading-wrapper" />

    <!-- 数据内容 -->
    <div v-else class="profile-content">
      <!-- 总体统计卡片 -->
      <a-row :gutter="24" class="summary-cards">
        <a-col :span="8">
          <a-card :bordered="false" class="stat-card">
            <div class="stat-icon" style="background: linear-gradient(135deg, #0e66c0 0%, #0e66c0 100%);">
              <a-icon type="team" />
            </div>
            <div class="stat-info">
              <div class="stat-label">总人数</div>
              <div class="stat-value">{{ profileData.totalStaff }}</div>
            </div>
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card :bordered="false" class="stat-card">
            <div class="stat-icon" style="background: linear-gradient(135deg, #0e66c0 0%, #0e66c0 100%);">
              <a-icon type="tag" />
            </div>
            <div class="stat-info">
              <div class="stat-label">已分配方向人数</div>
              <div class="stat-value">{{ profileData.totalStaffWithTag }}</div>
            </div>
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card :bordered="false" class="stat-card">
            <div class="stat-icon" style="background: linear-gradient(135deg, #0e66c0 0%, #0e66c0 100%);">
              <a-icon type="percentage" />
            </div>
            <div class="stat-info">
              <div class="stat-label">方向覆盖率</div>
              <div class="stat-value">{{ coverageRate }}%</div>
            </div>
          </a-card>
        </a-col>
      </a-row>

      <!-- 图表区域 -->
      <a-row :gutter="24">
        <!-- 饼图 - 方向占比 -->
        <a-col :xs="24" :md="12">
          <a-card :bordered="false" class="chart-card">
            <div slot="title" class="card-title">
              <a-icon type="pie-chart" style="margin-right: 8px; color: #1890ff;" />
              方向占比分布
            </div>
            <apexchart
              v-if="pieChartSeries.length > 0"
              type="pie"
              height="350"
              :options="pieChartOptions"
              :series="pieChartSeries"
            />
            <a-empty v-else description="暂无数据" />
          </a-card>
        </a-col>

        <!-- 柱状图 - 各方向人数 -->
        <a-col :xs="24" :md="12">
          <a-card :bordered="false" class="chart-card">
            <div slot="title" class="card-title">
              <a-icon type="bar-chart" style="margin-right: 8px; color: #52c41a;" />
              各方向人数统计
            </div>
            <apexchart
              v-if="barChartSeries.length > 0"
              type="bar"
              height="300"
              :options="barChartOptions"
              :series="barChartSeries"
            />
            <a-empty v-else description="暂无数据" />
          </a-card>
        </a-col>
      </a-row>

      <!-- 详细数据表格 -->
      <a-card :bordered="false" class="table-card">
        <div slot="title" class="card-title">
          <a-icon type="table" style="margin-right: 8px; color: #faad14;" />
          方向分布详情
        </div>
        <a-table
          :columns="columns"
          :data-source="validDirections"
          :pagination="false"
          row-key="tagId"
          size="middle"
        >
          <template slot="tagName" slot-scope="text, record">
            <a-tag :color="getTagColor(record.tagName)">
              {{ text }}
            </a-tag>
          </template>
          <template slot="count" slot-scope="text">
            <span class="count-value">{{ text }}人</span>
          </template>
          <template slot="percentage" slot-scope="text">
            <a-progress
              :percent="text"
              :stroke-color="getProgressColor(text)"
              :format="percent => percent.toFixed(1) + '%'"
            />
          </template>
        </a-table>
      </a-card>
    </div>
  </div>
</template>

<script>export default {
  name: 'UserProfile',
  data () {
    return {
      loading: false,
      profileData: {
        totalStaff: 0,
        totalStaffWithTag: 0,
        directionDistribution: []
      },
      columns: [
        {
          title: '方向名称',
          dataIndex: 'tagName',
          key: 'tagName',
          scopedSlots: { customRender: 'tagName' },
          width: '30%'
        },
        {
          title: '人数',
          dataIndex: 'count',
          key: 'count',
          scopedSlots: { customRender: 'count' },
          width: '25%',
          sorter: (a, b) => a.count - b.count
        },
        {
          title: '占比',
          dataIndex: 'percentage',
          key: 'percentage',
          scopedSlots: { customRender: 'percentage' },
          width: '45%',
          sorter: (a, b) => a.percentage - b.percentage
        }
      ]
    }
  },
  computed: {
    // 有效的方向（有数据的）
    validDirections () {
      return this.profileData.directionDistribution.filter(d => d.count > 0)
    },
    // 方向覆盖率
    coverageRate () {
      if (this.profileData.totalStaff === 0) return 0
      return ((this.profileData.totalStaffWithTag / this.profileData.totalStaff) * 100).toFixed(2)
    },
    // 饼图数据
    pieChartSeries () {
      return this.validDirections.map(d => d.count)
    },
    // 饼图配置
    pieChartOptions () {
      return {
        chart: {
          toolbar: {
            show: true,
            tools: {
              download: true,
              selection: false,
              zoom: false,
              zoomin: false,
              zoomout: false,
              pan: false,
              reset: false
            }
          }
        },
        labels: this.validDirections.map(d => d.tagName),
        colors: this.validDirections.map(d => this.getTagChartColor(d.tagName)),
        plotOptions: {
          pie: {
            donut: {
              labels: {
                show: true,
                total: {
                  show: true,
                  label: '总人数',
                  fontSize: '14px',
                  fontWeight: 'bold',
                  formatter: (w) => {
                    const total = w.globals.seriesTotals.reduce((a, b) => a + b, 0)
                    return total + '人'
                  }
                }
              }
            }
          }
        },
        dataLabels: {
          enabled: true,
          formatter: (val, opts) => {
            return opts.w.config.series[opts.seriesIndex] + '人 (' + val.toFixed(1) + '%)'
          },
          style: {
            fontSize: '13px',
            fontWeight: 'bold'
          }
        },
        legend: {
          position: 'bottom',
          offsetY: 10,
          fontSize: '13px',
          markers: {
            width: 12,
            height: 12,
            radius: 6
          }
        },
        tooltip: {
          y: {
            formatter: (val) => val + '人'
          }
        },
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 300
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      }
    },
    // 柱状图数据
    barChartSeries () {
      return [{
        name: '人数',
        data: this.validDirections.map(d => d.count)
      }]
    },
    // 柱状图配置
    barChartOptions () {
      return {
        chart: {
          toolbar: {
            show: true,
            tools: {
              download: true,
              selection: false,
              zoom: false,
              zoomin: false,
              zoomout: false,
              pan: false,
              reset: false
            }
          }
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '60%',
            endingShape: 'rounded',
            borderRadius: 6,
            distributed: true
          }
        },
        dataLabels: {
          enabled: true,
          formatter: (val) => val + '人',
          style: {
            fontSize: '13px',
            fontWeight: 'bold'
          }
        },
        xaxis: {
          categories: this.validDirections.map(d => d.tagName),
          labels: {
            rotate: -45,
            style: {
              fontSize: '13px',
              fontWeight: 'bold'
            }
          }
        },
        yaxis: {
          title: {
            text: '人数',
            style: {
              fontSize: '12px'
            }
          },
          tickAmount: 5,
          stepSize: 1
        },
        colors: this.validDirections.map(d => this.getTagChartColor(d.tagName)),
        fill: {
          opacity: 0.9
        },
        tooltip: {
          y: {
            formatter: (val) => val + '人'
          }
        },
        legend: {
          show: false
        },
        grid: {
          borderColor: '#f1f1f1',
          row: {
            colors: ['#f9f9f9', 'transparent'],
            opacity: 0.5
          }
        }
      }
    }
  },
  mounted () {
    this.queryUserProfile()
  },
  methods: {
    queryUserProfile () {
      this.loading = true
      this.$get('/business/web/queryUserProfile').then((r) => {
        this.profileData = r.data
      }).catch((err) => {
        console.error(err)
        this.$message.error('获取用户信息失败')
      }).finally(() => {
        this.loading = false
      })
    },
    getTagColor (tagName) {
      const colors = {
        '编程': 'blue',
        '教育': 'green',
        '水电维修': 'cyan',
        '挖掘机': 'orange',
        'TEM-4 / TEM-8': 'purple'
      }
      return colors[tagName] || 'default'
    },
    getTagChartColor (tagName) {
      const colors = {
        '编程': '#667eea',
        '教育': '#f093fb',
        '水电维修': '#4facfe',
        '挖掘机': '#43e97b',
        'TEM-4 / TEM-8': '#fa709a'
      }
      return colors[tagName] || '#667eea'
    },
    getProgressColor (percentage) {
      if (percentage >= 50) return '#52c41a'
      if (percentage >= 30) return '#1890ff'
      if (percentage >= 10) return '#faad14'
      return '#ff7a45'
    }
  }
}
</script>

<style scoped lang="less">.user-profile-container {
  .page-header {
    background: #ffffff;
    padding: 24px;
    margin-bottom: 24px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #262626;
      margin: 0 0 8px 0;
    }

    .page-subtitle {
      font-size: 14px;
      color: #8c8c8c;
      margin: 0;
    }
  }

  .loading-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 400px;
  }

  .profile-content {
    .summary-cards {
      margin-bottom: 24px;
    }

    .stat-card {
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
      transition: all 0.3s;

      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
      }

      ::v-deep .ant-card-body {
        display: flex;
        align-items: center;
        gap: 16px;
        padding: 20px;
      }

      .stat-icon {
        width: 45px;
        height: 45px;
        margin-bottom: 15px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28px;
        color: #fff;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }

      .stat-info {
        flex: 1;

        .stat-label {
          font-size: 13px;
          color: #8c8c8c;
          margin-bottom: 4px;
        }

        .stat-value {
          font-size: 24px;
          font-weight: 600;
          color: #262626;
        }
      }
    }

    .chart-card {
      margin-bottom: 24px;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

      .card-title {
        font-size: 16px;
        font-weight: 600;
        color: #262626;
        display: flex;
        align-items: center;
      }
    }

    .table-card {
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

      .card-title {
        font-size: 16px;
        font-weight: 600;
        color: #262626;
        display: flex;
        align-items: center;
      }

      .count-value {
        font-size: 14px;
        font-weight: 600;
        color: #262626;
      }

      ::v-deep .ant-progress-text {
        font-size: 13px;
        font-weight: 600;
      }
    }
  }
}
</style>
