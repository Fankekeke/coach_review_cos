
<template>
  <div class="page-container">
    <a-row :gutter="16" class="stats-row">
      <!-- 今日数据卡片 -->
      <a-col :span="6">
        <a-card :bordered="false" class="stat-card today-question">
          <a-statistic title="今日问题数" :value="adminHome.todayQuestionCount || 0" :value-style="{ color: '#1890ff' }">
            <template slot="prefix">
              <a-icon type="question-circle" theme="filled" style="color: #1890ff;" />
            </template>
          </a-statistic>
        </a-card>
      </a-col>

      <a-col :span="6">
        <a-card :bordered="false" class="stat-card today-user">
          <a-statistic title="今日新增用户" :value="adminHome.todayUserCount || 0" :value-style="{ color: '#52c41a' }">
            <template slot="prefix">
              <a-icon type="user-add" theme="filled" style="color: #52c41a;" />
            </template>
          </a-statistic>
        </a-card>
      </a-col>

      <a-col :span="6">
        <a-card :bordered="false" class="stat-card resolution-rate">
          <a-statistic title="解决率" :value="(adminHome.resolutionRate || 0) * 100" :precision="1" suffix="%" :value-style="{ color: '#722ed1' }">
            <template slot="prefix">
              <a-icon type="check-circle" theme="filled" style="color: #722ed1;" />
            </template>
          </a-statistic>
        </a-card>
      </a-col>

      <a-col :span="6">
        <a-card :bordered="false" class="stat-card pending-confirm">
          <a-statistic title="待确认回答" :value="adminHome.pendingConfirmCount || 0" :value-style="{ color: '#faad14' }">
            <template slot="prefix">
              <a-icon type="clock-circle" theme="filled" style="color: #faad14;" />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
    </a-row>

    <!-- 趋势图表 -->
    <a-row :gutter="16" class="chart-row">
      <a-col :span="16">
        <a-card title="近 7 日问答趋势" :bordered="false" class="trend-card">
          <div ref="trendChart" class="chart-container"></div>
        </a-card>
      </a-col>

      <!-- 负面反馈 -->
      <a-col :span="8">
        <a-card title="负面反馈 TOP" :bordered="false" class="feedback-card">
          <a-list
            item-layout="horizontal"
            :data-source="adminHome.topNegativeFeedbacks || []"
            class="feedback-list"
          >
            <a-list-item slot="renderItem" slot-scope="item">
              <div class="feedback-item">
                <div class="feedback-header">
                  <div class="feedback-rank">
                    <a-icon type="warning" theme="filled" />
                  </div>
                  <div class="feedback-info">
                    <div class="feedback-title">{{ item.title }}</div>
                    <div class="feedback-date">{{ item.feedbackDate }}</div>
                  </div>
                </div>
                <div class="feedback-body">
                  <a-tag color="red" class="feedback-tag">
                    <a-icon type="exclamation-circle" /> 负面反馈
                  </a-tag>
                  <div class="feedback-text">{{ item.feedback }}</div>
                </div>
              </div>
            </a-list-item>

            <div v-if="!adminHome.topNegativeFeedbacks || adminHome.topNegativeFeedbacks.length === 0" class="empty-feedback">
              <a-empty description="暂无负面反馈" :image="simpleImage">
                <template #image>
                  <a-icon type="smile" theme="filled" style="font-size: 64px; color: #52c41a;" />
                </template>
              </a-empty>
            </div>
          </a-list>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>import { Empty } from 'ant-design-vue'

export default {
  name: 'home',
  data () {
    return {
      adminHome: {
        todayQuestionCount: 0,
        todayUserCount: 0,
        resolutionRate: 0,
        pendingConfirmCount: 0,
        topNegativeFeedbacks: [],
        last7DaysTrend: []
      },
      simpleImage: Empty.PRESENTED_IMAGE_SIMPLE
    }
  },
  mounted () {
    this.queryAdminHome()
  },
  methods: {
    queryAdminHome () {
      this.$get('/business/web/adminHome').then((r) => {
        if (r.data.code === 0 && r.data.data) {
          this.adminHome = r.data.data
          this.$nextTick(() => {
            this.renderTrendChart()
          })
        }
      }).catch((r) => {
        console.error(r)
        this.$message.error('获取首页信息失败')
      })
    },
    renderTrendChart () {
      const chartData = this.adminHome.last7DaysTrend || []
      if (chartData.length === 0) return

      const dates = chartData.map(item => item.date.substring(5))
      const questionCounts = chartData.map(item => item.questionCount)
      const answerCounts = chartData.map(item => item.answerCount)

      const options = {
        series: [
          {
            name: '问题数',
            data: questionCounts,
            color: '#1890ff'
          },
          {
            name: '回答数',
            data: answerCounts,
            color: '#52c41a'
          }
        ],
        chart: {
          type: 'bar',
          height: 300,
          toolbar: {
            show: false
          },
          animations: {
            enabled: true,
            easing: 'easeinout',
            speed: 800
          }
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '60%',
            endingShape: 'rounded',
            borderRadius: 4
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: dates,
          labels: {
            style: {
              fontSize: '12px',
              fontFamily: 'Helvetica, Arial, sans-serif',
              fontWeight: 500,
              colors: '#666'
            }
          },
          axisBorder: {
            show: false
          },
          axisTicks: {
            show: false
          }
        },
        yaxis: {
          title: {
            text: '数量',
            style: {
              fontSize: '12px',
              fontWeight: 500,
              color: '#666'
            }
          },
          labels: {
            style: {
              fontSize: '12px',
              fontFamily: 'Helvetica, Arial, sans-serif',
              fontWeight: 500,
              colors: '#666'
            }
          }
        },
        fill: {
          opacity: 1,
          type: 'gradient',
          gradient: {
            shade: 'light',
            type: 'vertical',
            shadeIntensity: 0.4,
            inverseColors: false,
            opacityFrom: 1,
            opacityTo: 0.8,
            stops: [0, 100]
          }
        },
        tooltip: {
          shared: true,
          intersect: false,
          y: {
            formatter: function (val) {
              return val + ' 条'
            }
          }
        },
        legend: {
          position: 'top',
          horizontalAlign: 'right',
          fontSize: '13px',
          fontWeight: 500,
          markers: {
            size: 8,
            radius: 4
          }
        },
        grid: {
          borderColor: '#e8e8e8',
          strokeDashArray: 4,
          xaxis: {
            lines: {
              show: true
            }
          },
          yaxis: {
            lines: {
              show: true
            }
          }
        },
        colors: ['#1890ff', '#52c41a']
      }

      if (this.chart) {
        this.chart.destroy()
      }

      this.chart = new ApexCharts(this.$refs.trendChart, options)
      this.chart.render()
    }
  },
  beforeDestroy () {
    if (this.chart) {
      this.chart.destroy()
    }
  }
}
</script>

<style scoped lang="less">.page-container {
  padding: 24px;
  background: linear-gradient(135deg, #ffffff 0%, #f3f3f3 100%);
}

.stats-row {
  margin-bottom: 24px;

  .stat-card {
    border-radius: 3px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    transition: all 0.3s;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    }

    &.today-question {
      background: linear-gradient(135deg, #e6f7ff 0%, #bae7ff 100%);
    }

    &.today-user {
      background: linear-gradient(135deg, #f6ffed 0%, #d9f7be 100%);
    }

    &.resolution-rate {
      background: linear-gradient(135deg, #f9f0ff 0%, #efdbff 100%);
    }

    &.pending-confirm {
      background: linear-gradient(135deg, #fff7e6 0%, #ffe7ba 100%);
    }

    /deep/ .ant-statistic-title {
      font-size: 14px;
      color: rgba(0, 0, 0, 0.65);
    }

    /deep/ .ant-statistic-content {
      font-size: 28px;
      font-weight: 600;
    }
  }
}

.chart-row {
  .trend-card,
  .feedback-card {
    border-radius: 3px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
  }

  .chart-container {
    height: 300px;
    width: 100%;
  }

  .feedback-card {
    .feedback-list {
      max-height: 300px;
      overflow-y: auto;

      .feedback-content {
        flex: 1;

        .feedback-text {
          color: #333;
          font-size: 14px;
          line-height: 1.6;
          margin-bottom: 8px;
        }

        .feedback-date {
          color: #999;
          font-size: 12px;
        }
      }

      .empty-feedback {
        padding: 40px 0;
      }
    }
  }
}

.feedback-card {
  /deep/ .ant-card-head {
    background: linear-gradient(135deg, #fff1f0 0%, #ffccc7 100%);
    border-bottom: 2px solid #ffccc7;
    padding: 16px 20px;

    /deep/ .ant-card-head-title {
      font-size: 16px;
      font-weight: 600;
      color: #cf1322;
    }
  }

  .feedback-list {
    max-height: 350px;
    overflow-y: auto;
    padding: 8px;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: #f5f5f5;
      border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb {
      background: #d9d9d9;
      border-radius: 3px;

      &:hover {
        background: #bfbfbf;
      }
    }

    .feedback-item {
      margin-bottom: 12px;
      padding: 16px;
      background: linear-gradient(135deg, #ffffff 0%, #fafafa 100%);
      border-radius: 3px;
      border-left: 4px solid #ff4d4f;
      box-shadow: 0 2px 8px rgba(255, 77, 79, 0.08);
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

      &:hover {
        transform: translateX(4px);
        box-shadow: 0 4px 16px rgba(255, 77, 79, 0.15);
        background: linear-gradient(135deg, #fff1f0 0%, #ffffff 100%);
      }

      .feedback-header {
        display: flex;
        align-items: center;
        margin-bottom: 12px;

        .feedback-rank {
          width: 36px;
          height: 36px;
          border-radius: 50%;
          background: linear-gradient(135deg, #ff4d4f 0%, #cf1322 100%);
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          box-shadow: 0 2px 8px rgba(255, 77, 79, 0.3);

          i {
            color: white;
            font-size: 18px;
          }
        }

        .feedback-info {
          flex: 1;

          .feedback-title {
            font-size: 14px;
            font-weight: 600;
            color: #333;
            margin-bottom: 6px;
            line-height: 1.5;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
          }

          .feedback-date {
            font-size: 12px;
            color: #999;
            font-weight: 400;
          }
        }
      }

      .feedback-body {
        .feedback-tag {
          margin-bottom: 8px;
          font-size: 12px;
          padding: 2px 8px;
          border-radius: 4px;
          border: none;
          background: linear-gradient(135deg, #ffccc7 0%, #ff7875 100%);
          color: white;
          font-weight: 500;

          i {
            margin-right: 4px;
          }
        }

        .feedback-text {
          color: #666;
          font-size: 13px;
          line-height: 1.7;
          padding: 10px;
          background: #f5f5f5;
          border-radius: 4px;
          border-left: 3px solid #ffccc7;
        }
      }
    }

    .empty-feedback {
      padding: 50px 0;
      text-align: center;

      /deep/ .ant-empty-description {
        color: #999;
        font-size: 14px;
      }
    }
  }
}
</style>
