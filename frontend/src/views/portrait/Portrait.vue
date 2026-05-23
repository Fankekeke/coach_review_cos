
<template>
  <div class="portrait-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">能力画像</h1>
          <p class="page-subtitle">基于多维度数据的综合能力评估</p>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <a-spin size="large" tip="加载中..." />
    </div>

    <!-- 内容区域 -->
    <div v-else-if="userData" class="content-wrapper">
      <!-- 综合得分卡片 -->
      <a-row :gutter="24">
        <a-col :xs="24" :md="8">
          <a-card :bordered="false" class="score-overview-card">
            <div class="score-circle">
              <svg class="progress-ring" width="200" height="200">
                <circle
                  class="progress-ring-bg"
                  stroke-width="16"
                  fill="transparent"
                  r="82"
                  cx="100"
                  cy="100"
                />
                <circle
                  class="progress-ring-circle"
                  stroke-width="16"
                  fill="transparent"
                  r="82"
                  cx="100"
                  cy="100"
                  :stroke-dasharray="circumference"
                  :stroke-dashoffset="circumference - (totalScore / 500) * circumference"
                  transform="rotate(-90 100 100)"
                />
              </svg>
              <div class="circle-inner">
                <div class="total-score">{{ totalScore }}</div>
                <div class="score-label">综合得分</div>
              </div>
            </div>

            <div class="score-level">
              <a-tag :color="getScoreLevelColor" size="large">{{ scoreLevel }}</a-tag>
            </div>

            <div class="score-comment">
              <p>{{ scoreComment }}</p>
            </div>

            <div class="radar-chart-container">
              <apexchart
                type="radar"
                height="350"
                :options="radarChartOptions"
                :series="radarChartSeries"
              ></apexchart>
            </div>
          </a-card>
        </a-col>

        <!-- 各维度得分 -->
        <a-col :xs="24" :md="16">
          <a-card :bordered="false" class="dimensions-card">
            <a-row :gutter="[16, 16]">
              <a-col :xs="24" :md="8" v-for="dimension in dimensions" :key="dimension.key">
                <div class="dimension-item">
                  <div class="dimension-header">
                    <span class="dimension-name">{{ dimension.name }}</span>
                    <span class="dimension-score" :style="{ color: getDimensionColor(userData[dimension.key]) }">
                      {{ userData[dimension.key] }}/100
                    </span>
                  </div>
                  <div class="dimension-progress">
                    <a-progress
                      :percent="userData[dimension.key]"
                      :stroke-color="getDimensionColor(userData[dimension.key])"
                      :show-info="false"
                      :stroke-width="8"
                    />
                    <span class="progress-percent">{{ userData[dimension.key] }}%</span>
                  </div>
                </div>
              </a-col>
              <a-col :span="24" style="margin-top: 95px;">
                <div class="bar-chart-container">
                  <apexchart
                    type="bar"
                    height="380"
                    :options="barChartOptions"
                    :series="barChartSeries"
                  ></apexchart>
                </div>
              </a-col>
            </a-row>
          </a-card>
        </a-col>
      </a-row>

      <a-row :gutter="24" style="margin-top: 24px;">
        <a-col :span="24">
          <a-card :bordered="false" class="trend-card">
            <div slot="title" class="card-header">
              <a-icon type="line-chart" style="margin-right: 8px;" />
              能力趋势分析
            </div>
            <div class="trend-chart-container">
              <apexchart
                type="line"
                height="250"
                :options="trendChartOptions"
                :series="trendChartSeries"
              ></apexchart>
            </div>
          </a-card>
        </a-col>
      </a-row>

      <!-- 详细信息 -->
      <a-row :gutter="24" style="margin-top: 24px;">
        <a-col :span="24">
          <a-card :bordered="false" class="info-card">
            <div slot="title" class="card-header">
              <a-icon type="user" style="margin-right: 8px;" />
              个人信息
            </div>
            <a-descriptions bordered :column="3">
              <a-descriptions-item label="姓名">
                {{ userData.name }}
              </a-descriptions-item>
              <a-descriptions-item label="性别">
                {{ userData.staffSex === 1 ? '男' : '女' }}
              </a-descriptions-item>
              <a-descriptions-item label="出生日期">
                {{ userData.birthDate }}
              </a-descriptions-item>
              <a-descriptions-item label="邮箱">
                {{ userData.email }}
              </a-descriptions-item>
              <a-descriptions-item label="电话">
                {{ userData.phone }}
              </a-descriptions-item>
              <a-descriptions-item label="擅长方向">
                <a-tag color="blue">{{ getTagNameById(userData.tagId) }}</a-tag>
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 空状态 -->
    <a-empty v-else description="暂无数据" style="margin-top: 100px;" />
  </div>
</template>

<script>import { mapState } from 'vuex'

export default {
  name: 'Portrait',
  data () {
    return {
      loading: false,
      userData: null,
      tagList: [],
      scoreTrendData: [],
      dimensions: [
        {
          key: 'professionalScore',
          name: '专业技能',
          color: '#52c41a'
        },
        {
          key: 'communicationScore',
          name: '沟通能力',
          color: '#faad14'
        },
        {
          key: 'teamworkScore',
          name: '团队协作',
          color: '#52c41a'
        },
        {
          key: 'emergencyScore',
          name: '应急处理',
          color: '#faad14'
        },
        {
          key: 'learningScore',
          name: '学习能力',
          color: '#52c41a'
        }
      ]
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    totalScore () {
      if (!this.userData) return 0
      const scores = [
        this.userData.professionalScore || 0,
        this.userData.communicationScore || 0,
        this.userData.teamworkScore || 0,
        this.userData.emergencyScore || 0,
        this.userData.learningScore || 0
      ]
      return Math.round(scores.reduce((sum, score) => sum + score, 0))
    },
    totalScoreDegree () {
      return (this.totalScore / 500) * 360
    },
    circumference () {
      return 2 * Math.PI * 82
    },
    scoreLevel () {
      if (this.totalScore >= 450) return '优秀'
      if (this.totalScore >= 350) return '良好'
      if (this.totalScore >= 250) return '合格'
      return '待提升'
    },
    getScoreLevelColor () {
      if (this.totalScore >= 450) return 'green'
      if (this.totalScore >= 350) return 'blue'
      if (this.totalScore >= 250) return 'orange'
      return 'red'
    },
    scoreComment () {
      if (this.totalScore >= 450) return '您的综合能力表现优秀，继续保持！'
      if (this.totalScore >= 350) return '您的综合能力表现良好，还有提升空间。'
      if (this.totalScore >= 250) return '您的综合能力基本合格，需要加强某些维度。'
      return '您的综合能力有待提升，建议重点关注薄弱环节。'
    },
    barChartOptions () {
      return {
        chart: {
          toolbar: {
            show: false
          },
          animations: {
            enabled: true,
            easing: 'easeinout',
            speed: 800,
            animateGradually: {
              enabled: true,
              delay: 150
            }
          }
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '50%',
            borderRadius: 4,
            distributed: true
          }
        },
        dataLabels: {
          enabled: true,
          formatter: function (val) {
            return val + '分'
          },
          style: {
            fontSize: '12px',
            fontWeight: 600
          },
          offsetY: -20
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: this.dimensions.map(d => d.name),
          labels: {
            style: {
              fontSize: '13px',
              fontWeight: 500,
              colors: '#595959'
            }
          },
          axisBorder: {
            show: true,
            color: '#e8e8e8'
          },
          axisTicks: {
            show: true,
            color: '#e8e8e8'
          }
        },
        yaxis: {
          title: {
            text: '得分',
            style: {
              fontSize: '14px',
              fontWeight: 600,
              color: '#595959'
            }
          },
          labels: {
            formatter: function (val) {
              return val + '分'
            },
            style: {
              fontSize: '12px',
              colors: '#8c8c8c'
            }
          },
          max: 100,
          min: 0,
          tickAmount: 5
        },
        grid: {
          borderColor: '#f0f0f0',
          strokeDashArray: 4,
          yaxis: {
            lines: {
              show: true
            }
          }
        },
        tooltip: {
          theme: 'light',
          y: {
            formatter: function (val) {
              return val + ' 分'
            }
          }
        },
        legend: {
          show: false
        },
        fill: {
          opacity: 1,
          gradient: {
            shade: 'light',
            type: 'vertical',
            shadeIntensity: 0.3,
            opacityFrom: 1,
            opacityTo: 0.8,
            stops: [0, 100]
          }
        }
      }
    },
    barChartSeries () {
      if (!this.userData) return []
      return [{
        name: '能力评分',
        data: [
          this.userData.professionalScore || 0,
          this.userData.communicationScore || 0,
          this.userData.teamworkScore || 0,
          this.userData.emergencyScore || 0,
          this.userData.learningScore || 0
        ]
      }]
    },
    trendChartOptions () {
      return {
        chart: {
          toolbar: {
            show: true,
            tools: {
              download: true,
              selection: true,
              zoom: true,
              zoomin: true,
              zoomout: true,
              pan: true,
              reset: true
            }
          },
          animations: {
            enabled: true,
            easing: 'easeinout',
            speed: 800
          }
        },
        colors: ['#52c41a', '#1890ff', '#722ed1', '#faad14', '#13c2c2'],
        stroke: {
          width: 3,
          curve: 'smooth'
        },
        markers: {
          size: 5,
          strokeWidth: 2,
          hover: {
            size: 7,
            sizeOffset: 3
          }
        },
        xaxis: {
          type: 'datetime',
          labels: {
            datetimeFormatter: {
              year: 'yyyy',
              month: "yyyy年MM月",
              day: 'MM月dd日',
              hour: 'HH:mm'
            },
            style: {
              fontSize: '12px',
              colors: '#8c8c8c'
            }
          },
          axisBorder: {
            show: true,
            color: '#e8e8e8'
          },
          axisTicks: {
            show: true,
            color: '#e8e8e8'
          }
        },
        yaxis: {
          title: {
            text: '得分',
            style: {
              fontSize: '14px',
              fontWeight: 600,
              color: '#595959'
            }
          },
          labels: {
            formatter: function (val) {
              return val + '分'
            },
            style: {
              fontSize: '12px',
              colors: '#8c8c8c'
            }
          },
          max: 100,
          min: 0,
          tickAmount: 5
        },
        grid: {
          borderColor: '#f0f0f0',
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
        tooltip: {
          theme: 'light',
          shared: true,
          intersect: false,
          x: {
            format: 'yyyy年MM月dd日 HH:mm'
          },
          y: {
            formatter: function (val) {
              return val + ' 分'
            }
          }
        },
        legend: {
          position: 'top',
          horizontalAlign: 'center',
          fontSize: '13px',
          fontWeight: 500,
          markers: {
            width: 12,
            height: 12,
            strokeWidth: 0,
            radius: 6
          },
          itemMargin: {
            horizontal: 10,
            vertical: 5
          }
        },
        dataLabels: {
          enabled: false
        }
      }
    },
    trendChartSeries () {
      if (!this.scoreTrendData || this.scoreTrendData.length === 0) return []

      const dimensionColors = {
        '专业技能': '#52c41a',
        '沟通能力': '#1890ff',
        '团队协作': '#722ed1',
        '应急处理': '#faad14',
        '学习能力': '#13c2c2'
      }

      return this.scoreTrendData.map(dimension => {
        const validScores = dimension.scores
          .filter(s => s.finalScore > 0)
          .map(s => ({
            x: new Date(s.createDate).getTime(),
            y: s.finalScore
          }))

        return {
          name: dimension.dimension,
          data: validScores,
          color: dimensionColors[dimension.dimension] || '#1890ff'
        }
      }).filter(series => series.data.length > 0)
    },
    radarChartOptions () {
      return {
        chart: {
          toolbar: {
            show: false
          },
          animations: {
            enabled: true,
            easing: 'easeinout',
            speed: 800
          }
        },
        colors: ['#1890ff'],
        stroke: {
          width: 2,
          colors: ['#1890ff']
        },
        fill: {
          opacity: 0.2,
          colors: ['#1890ff']
        },
        markers: {
          size: 5,
          colors: ['#1890ff'],
          strokeWidth: 2,
          strokeColors: '#fff',
          hover: {
            size: 7
          }
        },
        xaxis: {
          categories: this.dimensions.map(d => d.name),
          labels: {
            style: {
              fontSize: '13px',
              fontWeight: 500,
              colors: '#595959'
            }
          }
        },
        yaxis: {
          show: false,
          max: 100,
          min: 0,
          tickAmount: 5
        },
        plotOptions: {
          radar: {
            size: 120,
            polygons: {
              strokeColors: '#e8e8e8',
              fill: {
                colors: ['#fafafa', '#fff']
              }
            }
          }
        },
        tooltip: {
          enabled: true,
          theme: 'light',
          y: {
            formatter: function (val) {
              return val + ' 分'
            }
          }
        },
        legend: {
          show: false
        }
      }
    },
    radarChartSeries () {
      if (!this.userData) return []
      return [{
        name: '能力评分',
        data: [
          this.userData.professionalScore || 0,
          this.userData.communicationScore || 0,
          this.userData.teamworkScore || 0,
          this.userData.emergencyScore || 0,
          this.userData.learningScore || 0
        ]
      }]
    }
  },
  mounted () {
    this.queryScoreTrendByUser()
    this.loadPortraitData()
    this.loadTagList()
  },
  methods: {
    queryScoreTrendByUser () {
      this.$get('/business/user-ability-score/queryScoreTrendByUser', {
        userId: this.currentUser.userId
      }).then((r) => {
        this.scoreTrendData = r.data.data
      })
    },
    loadPortraitData () {
      this.loading = true
      this.$get('/business/staff-info/queryStaffBySysUserId', {
        sysUserId: this.currentUser.userId
      }).then((r) => {
        if (r.data.code === 0 && r.data.data) {
          this.userData = r.data.data
        } else {
          this.$message.error(r.data.msg || '获取数据失败')
        }
      }).catch((error) => {
        console.error('获取画像数据错误:', error)
        this.$message.error('网络错误，请稍后重试')
      }).finally(() => {
        this.loading = false
      })
    },
    loadTagList () {
      this.$get('/business/tag-info/list').then((r) => {
        if (r.data.code === 0) {
          this.tagList = r.data.data
        }
      })
    },
    getTagNameById (tagId) {
      if (!tagId) return '未设置'
      const tag = this.tagList.find(t => t.id === tagId)
      return tag ? tag.name : '未知'
    },
    exportReport () {
      this.$message.info('导出功能开发中...')
    },
    getDimensionColor (score) {
      if (!score && score !== 0) return '#d9d9d9'
      if (score >= 90) return '#52c41a' // 绿色 - 优秀
      if (score >= 70) return '#1890ff' // 蓝色 - 良好
      if (score >= 60) return '#faad14' // 橙色 - 合格
      return '#ff4d4f' // 红色 - 待提升
    }
  }
}
</script>

<style scoped lang="less">
/deep/ .ant-progress-bg {
  border-radius: 0px;
}

/deep/ .ant-progress-inner {
  border-radius: 0px;
}

.portrait-container {
  padding: 24px;
  background: #f5f7fa;
  min-height: calc(100vh - 64px);
}

.page-header {
  margin-bottom: 24px;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-left {
      .page-title {
        font-size: 28px;
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
  }
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.content-wrapper {
  // 综合得分卡片
  .score-overview-card {
    border-radius: 2px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    text-align: center;
    padding: 32px 24px;

    .score-circle {
      position: relative;
      width: 200px;
      height: 200px;
      margin: 0 auto 24px;

      .progress-ring {
        width: 100%;
        height: 100%;

        .progress-ring-bg {
          stroke: #f0f0f0;
        }

        .progress-ring-circle {
          stroke: #52c41a;
          stroke-linecap: round;
          transition: stroke-dashoffset 1s ease;
        }
      }

      .circle-inner {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        text-align: center;

        .total-score {
          font-size: 48px;
          font-weight: bold;
          color: #262626;
          line-height: 1;
        }

        .score-label {
          font-size: 14px;
          color: #8c8c8c;
          margin-top: 8px;
        }
      }
    }

    .score-level {
      margin-bottom: 16px;
    }

    .score-comment {
      p {
        font-size: 14px;
        color: #595959;
        line-height: 1.6;
        margin: 0;
      }
    }
  }

  // 维度卡片
  .dimensions-card {
    border-radius: 2px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

    .dimension-item {
      padding: 16px;
      background: #fafafa;
      border-radius: 2px;
      transition: all 0.3s;

      &:hover {
        background: #f0f0f0;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }

      .dimension-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        .dimension-name {
          font-size: 15px;
          font-weight: 500;
          color: #262626;
        }

        .dimension-score {
          font-size: 18px;
          font-weight: 600;
        }
      }

      .dimension-progress {
        display: flex;
        align-items: center;
        gap: 12px;

        ::v-deep .ant-progress {
          flex: 1;
        }

        .progress-percent {
          font-size: 14px;
          color: #595959;
          min-width: 40px;
          text-align: right;
        }
      }
    }
  }

  // 信息卡片
  .info-card {
    border-radius: 2px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

    .card-header {
      display: flex;
      align-items: center;
      font-size: 16px;
      font-weight: 600;
      color: #262626;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .portrait-container {
    padding: 16px;
  }

  .page-header {
    .header-content {
      flex-direction: column;
      align-items: flex-start;
      gap: 16px;

      .header-right {
        width: 100%;
      }
    }
  }

  .content-wrapper {
    .score-overview-card {
      margin-bottom: 16px;
    }
  }
}
</style>
