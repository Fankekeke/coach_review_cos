
<template>
  <div class="page-container">
    <a-row :gutter="16" class="content-wrapper">
      <a-col :span="24">
        <a-card
          :loading="loading"
          :bordered="false"
          class="bank-card"
          :bodyStyle="{ padding: '24px' }"
        >
          <div slot="title" class="card-header">
            <a-icon type="book" theme="filled" style="color: #1890ff; font-size: 24px;" />
            <span class="header-title">在线题库</span>
            <a-divider type="vertical" style="height: 24px; margin: 0 12px;" />
            <span class="header-subtitle">选择题库开始答题</span>
          </div>

          <div class="bank-list">
            <a-row :gutter="24">
              <a-col
                v-for="(bank, index) in bankList"
                :key="bank.id"
                :xs="24"
                :sm="12"
                :md="8"
                :lg="8"
                :xl="6"
                class="bank-col"
              >
                <a-card
                  hoverable
                  class="bank-item"
                  @click="startPractice(bank)"
                  :bodyStyle="{ padding: '16px' }"
                >
                  <div slot="cover" class="bank-cover">
                    <img
                      :src="'http://127.0.0.1:9527/imagesWeb/' + bank.image"
                      :alt="bank.name"
                      class="bank-image"
                      @error="handleImageError"
                    />
                    <div class="bank-overlay">
                      <a-icon type="rocket" theme="filled" style="font-size: 48px; color: #fff;" />
                    </div>
                  </div>

                  <div class="bank-info">
                    <h3 class="bank-name" :title="bank.name">{{ bank.name }}</h3>
                    <p class="bank-code">编号：{{ bank.code }}</p>

                    <div class="bank-stats">
                      <div class="stat-item">
                        <a-icon type="bars" style="color: #1890ff;" />
                        <span class="stat-label">总题数</span>
                        <span class="stat-value">{{ bank.questionCount.total }}</span>
                      </div>
                      <div class="stat-item" v-if="bank.questionCount.typeCount">
                        <a-icon type="check-circle" theme="filled" style="color: #52c41a;" />
                        <span class="stat-label">单选题</span>
                        <span class="stat-value">{{ bank.questionCount.typeCount['单选题'] || 0 }}</span>
                      </div>
                      <div class="stat-item" v-if="bank.questionCount.typeCount">
                        <a-icon type="check-square" theme="filled" style="color: #722ed1;" />
                        <span class="stat-label">多选题</span>
                        <span class="stat-value">{{ bank.questionCount.typeCount['多选题'] || 0 }}</span>
                      </div>
                    </div>

                    <a-button
                      type="primary"
                      block
                      size="large"
                      class="start-btn"
                      @click.stop="startPractice(bank)"
                    >
                      <a-icon type="play-circle" /> 开始答题
                    </a-button>
                  </div>
                </a-card>
              </a-col>
            </a-row>
          </div>

          <!-- 空状态 -->
          <a-empty
            v-if="!loading && bankList.length === 0"
            description="暂无可用题库"
            class="empty-state"
          >
            <a-button type="primary" @click="queryBankList">
              <a-icon type="reload" /> 刷新
            </a-button>
          </a-empty>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>export default {
  name: 'Bank',
  data () {
    return {
      loading: false,
      bankList: []
    }
  },
  mounted () {
    this.queryBankList()
  },
  methods: {
    queryBankList () {
      this.loading = true
      this.$get('/business/question-bank/queryBankList').then((r) => {
        console.log(r)
        if (r.data.code === 0 && r.data.data) {
          this.bankList = r.data.data
        }
      }).catch((error) => {
        console.error('获取题库列表失败:', error)
        this.$message.error('加载题库失败，请稍后重试')
      }).finally(() => {
        this.loading = false
      })
    },

    handleImageError (e) {
      e.target.src = 'https://via.placeholder.com/300x200?text=No+Image'
    },

    startPractice (bank) {
      this.$message.success(`即将开始：${bank.name}`)
      // TODO: 跳转到答题页面或打开答题弹窗
      this.$router.push({
        path: '/practice',
        query: { bankId: bank.id, bankName: bank.name }
      })
    }
  }
}
</script>

<style scoped lang="less">.page-container {
  padding: 24px;
  background: linear-gradient(135deg, #FFFFFF 0%, #f6f6f6 100%);
  min-height: calc(100vh - 64px);
}

.content-wrapper {
  .bank-card {
    border-radius: 16px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);

    .card-header {
      display: flex;
      align-items: center;
      padding: 16px 0;
      border-bottom: 2px solid #f0f0f0;
      margin-bottom: 24px;

      .header-title {
        font-size: 24px;
        font-weight: bold;
        color: #333;
        margin-left: 12px;
      }

      .header-subtitle {
        font-size: 14px;
        color: #999;
        font-weight: normal;
      }
    }

    .bank-list {
      .bank-col {
        margin-bottom: 24px;
        transition: transform 0.3s;

        &:hover {
          transform: translateY(-8px);
        }

        .bank-item {
          border-radius: 12px;
          overflow: hidden;
          box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
          transition: all 0.3s;
          cursor: pointer;

          &:hover {
            box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);

            .bank-cover {
              .bank-overlay {
                opacity: 1;
              }
            }
          }

          .bank-cover {
            position: relative;
            height: 200px;
            overflow: hidden;

            .bank-image {
              width: 100%;
              height: 100%;
              object-fit: cover;
              transition: transform 0.3s;
            }

            .bank-overlay {
              position: absolute;
              top: 0;
              left: 0;
              right: 0;
              bottom: 0;
              background: linear-gradient(135deg, #FFFFFF 0%, #f6f6f6 100%);
              display: flex;
              align-items: center;
              justify-content: center;
              opacity: 0;
              transition: opacity 0.3s;
            }
          }

          .bank-info {
            padding: 16px;

            .bank-name {
              font-size: 16px;
              font-weight: 600;
              color: #333;
              margin: 0 0 8px 0;
              overflow: hidden;
              text-overflow: ellipsis;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
              line-height: 1.5;
              min-height: 48px;
            }

            .bank-code {
              font-size: 12px;
              color: #999;
              margin: 0 0 16px 0;
              font-family: monospace;
              background: #fafafa;
              padding: 4px 8px;
              border-radius: 4px;
              display: inline-block;
            }

            .bank-stats {
              display: flex;
              gap: 12px;
              margin-bottom: 16px;

              .stat-item {
                flex: 1;
                display: flex;
                flex-direction: column;
                align-items: center;
                padding: 8px;
                background: #fafafa;
                border-radius: 8px;
                transition: all 0.3s;

                &:hover {
                  background: #f0f0f0;
                  transform: scale(1.05);
                }

                .stat-label {
                  font-size: 12px;
                  color: #666;
                  margin-top: 4px;
                }

                .stat-value {
                  font-size: 18px;
                  font-weight: bold;
                  color: #333;
                  margin-top: 2px;
                }
              }
            }

            .start-btn {
              height: 44px;
              font-size: 16px;
              border-radius: 8px;
              background: linear-gradient(135deg, #2dc230 0%, #289d17 100%);
              border: none;
              transition: all 0.3s;

              &:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
              }
            }
          }
        }
      }
    }

    .empty-state {
      padding: 60px 0;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .page-container {
    padding: 16px;
  }

  .content-wrapper {
    .bank-card {
      .card-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 8px;
      }
    }
  }
}
</style>
