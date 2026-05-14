<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="用户名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.userName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="题库名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.bankName"/>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <!--        <a-button type="primary" ghost @click="add">新增</a-button>-->
        <a-button @click="batchDelete">删除</a-button>
        <!--        <a-button @click="batchDelete1">删除</a-button>-->
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="titleShow" slot-scope="text, record">
          <template>
            <a-badge status="processing" v-if="record.rackUp === 1"/>
            <a-badge status="error" v-if="record.rackUp === 0"/>
            <a-tooltip>
              <template slot="title">
                {{ record.title }}
              </template>
              {{ record.title.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="contentShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.content }}
              </template>
              {{ record.content.slice(0, 30) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon
            type="eye"
            theme="twoTone"
            twoToneColor="#52c41a"
            @click="viewDetail(record)"
            title="查看详情"            style="margin-right: 12px;"
          ></a-icon>
        </template>
      </a-table>
    </div>
    <!-- 答题详情 Modal -->
    <a-modal
      v-model="detailModal.visible"
      title="答题详情"
      width="900px"
      :footer="null"
      @cancel="handleDetailClose"
      class="detail-modal"
    >
      <div class="detail-header" v-if="detailModal.data">
        <a-row :gutter="16">
          <a-col :span="12">
            <div class="info-item">
              <a-icon type="user" style="color: #1890ff;" />
              <strong>用户：</strong>{{ detailModal.data.userName || '- -' }}
            </div>
          </a-col>
          <a-col :span="12">
            <div class="info-item">
              <a-icon type="book" style="color: #722ed1;" />
              <strong>题库：</strong>{{ detailModal.data.bankName || '- -' }}
            </div>
          </a-col>
        </a-row>
        <a-row :gutter="16" style="margin-top: 12px;">
          <a-col :span="8">
            <div class="score-info">
              <a-statistic
                title="得分"
                :value="detailModal.data.totalScore"
                :value-style="{ color: '#3f8600' }"
                suffix="分"
              />
            </div>
          </a-col>
          <a-col :span="8">
            <div class="score-info">
              <a-statistic
                title="总分"
                :value="detailModal.data.score"
                :value-style="{ color: '#1890ff' }"
                suffix="分"
              />
            </div>
          </a-col>
          <a-col :span="8">
            <div class="score-info">
              <a-statistic
                title="正确率"
                :value="calculateAccuracy(detailModal.data)"
                :value-style="{ color: '#cf1322' }"
                suffix="%"
              />
            </div>
          </a-col>
        </a-row>
      </div>

      <a-divider>答题明细</a-divider>

      <div class="answer-detail-list" v-if="detailModal.data && detailModal.data.answerDetail">
        <div
          v-for="(item, index) in parseAnswerDetail(detailModal.data.answerDetail)"
          :key="index"
          class="answer-item"
          :class="item.isCorrect ? 'correct' : 'wrong'"
        >
          <div class="answer-item-header">
            <div class="question-info">
              <a-tag :color="getQuestionTypeColor(item.questionType)" class="type-tag">
                {{ item.questionType }}
              </a-tag>
              <span class="question-title">第{{ index + 1 }}题</span>
              <a-tag :color="item.isCorrect ? 'success' : 'error'" class="result-tag">
                <a-icon :type="item.isCorrect ? 'check-circle' : 'close-circle'" />
                {{ item.isCorrect ? '正确' : '错误' }}
              </a-tag>
            </div>
            <div class="question-score">
              <a-tag color="orange">
                <a-icon type="star" theme="filled" /> {{ item.score }}分
              </a-tag>
            </div>
          </div>

          <div class="question-content">
            <p class="question-desc">{{ item.questionTitle }}</p>
          </div>

          <div class="answer-options">
            <div class="option-section">
              <div class="section-label correct-label">
                <a-icon type="check-circle" theme="filled" style="color: #52c41a;" />
                <strong>正确答案：</strong>
              </div>
              <div class="answer-tags">
                <a-tag
                  v-for="(ans, aIndex) in item.correctAnswer"
                  :key="aIndex"
                  color="green"
                  class="answer-tag"
                >
                  {{ getOptionLabel(aIndex) }}. {{ ans }}
                </a-tag>
              </div>
            </div>

            <div class="option-section">
              <div class="section-label user-label">
                <a-icon type="user" style="color: #1890ff;" />
                <strong>用户答案：</strong>
              </div>
              <div class="answer-tags">
                <a-tag
                  v-for="(uAns, uIndex) in (Array.isArray(item.userAnswer) ? item.userAnswer : [item.userAnswer])"
                  :key="uIndex"
                  :color="item.isCorrect ? 'green' : 'red'"
                  class="answer-tag"
                >
                  {{ getOptionLabel(getOptionIndex(item.options, uAns)) }}. {{ uAns }}
                </a-tag>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="empty-state" v-else-if="detailModal.data && !detailModal.data.answerDetail">
        <a-empty description="暂无答题详情数据" />
      </div>
    </a-modal>
    <bulletin-add
      v-if="bulletinAdd.visiable"
      @close="handleBulletinAddClose"
      @success="handleBulletinAddSuccess"
      :bulletinAddVisiable="bulletinAdd.visiable">
    </bulletin-add>
    <bulletin-edit
      ref="bulletinEdit"
      @close="handleBulletinEditClose"
      @success="handleBulletinEditSuccess"
      :bulletinEditVisiable="bulletinEdit.visiable">
    </bulletin-edit>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import BulletinAdd from './AnswerAdd.vue'
import BulletinEdit from './AnswerEdit.vue'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'Bulletin',
  components: {BulletinAdd, BulletinEdit, RangeDate},
  data () {
    return {
      advanced: false,
      bulletinAdd: {
        visiable: false
      },
      bulletinEdit: {
        visiable: false
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: [],
      detailModal: {
        visible: false,
        data: {}
      }
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '用户',
        dataIndex: 'userName',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        },
        ellipsis: true
      }, {
        title: '头像',
        dataIndex: 'userImages',
        customRender: (text, record, index) => {
          if (!record.userImages) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.userImages } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.userImages } />
          </a-popover>
        }
      }, {
        title: '题库名称',
        dataIndex: 'bankName',
        ellipsis: true
      }, {
        title: '得分/总分',
        dataIndex: 'totalScore',
        customRender: (text, row, index) => {
          if (text !== null && row.score !== null) {
            return `${text} / ${row.score}分`
          } else {
            return '- -'
          }
        },
        ellipsis: true
      }, {
        title: '答题时间',
        dataIndex: 'createDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        },
        ellipsis: true
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    calculateAccuracy (data) {
      if (!data.answerDetail) return 0
      try {
        const details = JSON.parse(data.answerDetail)
        if (!details || details.length === 0) return 0
        const correctCount = details.filter(d => d.isCorrect).length
        return Math.round((correctCount / details.length) * 100)
      } catch (e) {
        return 0
      }
    },

    parseAnswerDetail (answerDetailStr) {
      if (!answerDetailStr) return []
      try {
        return JSON.parse(answerDetailStr)
      } catch (e) {
        console.error('解析答题详情失败:', e)
        return []
      }
    },

    getQuestionTypeColor (type) {
      const colors = {
        '单选题': 'blue',
        '多选题': 'purple',
        '判断题': 'green'
      }
      return colors[type] || 'default'
    },

    getOptionLabel (index) {
      const labels = ['A', 'B', 'C', 'D', 'E', 'F']
      return labels[index] || `${index + 1}`
    },

    getOptionIndex (options, answer) {
      if (!options || !answer) return 0
      return options.indexOf(answer)
    },

    viewDetail (record) {
      this.detailModal.data = { ...record }
      this.detailModal.visible = true
    },

    showDetail (record) {
      this.detailModal.data = { ...record }
      this.detailModal.visible = true
    },
    handleDetailClose () {
      this.detailModal.visible = false
      this.detailModal.data = {}
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.bulletinAdd.visiable = true
    },
    handleBulletinAddClose () {
      this.bulletinAdd.visiable = false
    },
    handleBulletinAddSuccess () {
      this.bulletinAdd.visiable = false
      this.$message.success('新增答题记录成功')
      this.search()
    },
    edit (record) {
      this.$refs.bulletinEdit.setFormValues(record)
      this.bulletinEdit.visiable = true
    },
    handleBulletinEditClose () {
      this.bulletinEdit.visiable = false
    },
    handleBulletinEditSuccess () {
      this.bulletinEdit.visiable = false
      this.$message.success('修改答题记录成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete1 () {
      this.$get('/business/supplier-info/batchEditSupplierName').then((r) => {
      })
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/business/answer-record/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      params.userId = this.currentUser.userId
      this.$get('/business/answer-record/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
<style scoped lang="less">.detail-modal {
  .detail-header {
    background: #fafafa;
    padding: 16px;
    border-radius: 8px;
    margin-bottom: 16px;

    .info-item {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 14px;
      color: #333;

      strong {
        color: #666;
      }
    }

    .score-info {
      text-align: center;

      /deep/ .ant-statistic-title {
        font-size: 12px;
      }

      /deep/ .ant-statistic-content {
        font-size: 18px;
        font-weight: 600;
      }
    }
  }

  .answer-detail-list {
    max-height: 500px;
    overflow-y: auto;

    .answer-item {
      padding: 16px;
      margin-bottom: 16px;
      background: #fafafa;
      border-radius: 8px;
      border-left: 4px solid #d9d9d9;
      transition: all 0.3s;

      &.correct {
        border-left-color: #52c41a;
        background: linear-gradient(135deg, #f6ffed 0%, #fafafa 100%);
      }

      &.wrong {
        border-left-color: #ff4d4f;
        background: linear-gradient(135deg, #fff1f0 0%, #fafafa 100%);
      }

      &:hover {
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        transform: translateX(4px);
      }

      .answer-item-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        .question-info {
          display: flex;
          align-items: center;
          gap: 8px;

          .type-tag {
            font-size: 12px;
            font-weight: 600;
          }

          .question-title {
            font-size: 14px;
            font-weight: 600;
            color: #333;
          }

          .result-tag {
            font-size: 12px;
            font-weight: 600;
          }
        }

        .question-score {
          .ant-tag {
            font-size: 13px;
            font-weight: 600;
          }
        }
      }

      .question-content {
        margin-bottom: 16px;

        .question-desc {
          margin: 0;
          padding: 12px;
          background: white;
          border-radius: 6px;
          line-height: 1.8;
          color: #333;
          font-size: 14px;
          border: 1px solid #e8e8e8;
        }
      }

      .answer-options {
        .option-section {
          margin-bottom: 12px;

          .section-label {
            display: flex;
            align-items: center;
            gap: 6px;
            margin-bottom: 8px;
            font-size: 14px;

            &.correct-label {
              color: #52c41a;
            }

            &.user-label {
              color: #1890ff;
            }

            strong {
              font-size: 14px;
            }
          }

          .answer-tags {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
            padding-left: 28px;

            .answer-tag {
              margin: 0;
              font-size: 13px;
              padding: 4px 12px;
            }
          }
        }
      }
    }
  }

  .empty-state {
    padding: 60px 0;
    text-align: center;
  }
}
</style>
