<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="题库编号"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.code"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="题库名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.name"/>
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
        <a-button type="primary" ghost @click="add">新增</a-button>
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
        <template slot="code" slot-scope="text">
          <a-tooltip v-if="text">
            <template slot="title">{{ text }}</template>
            <span class="table-text-ellipsis">{{ text }}</span>
          </a-tooltip>
          <span v-else>- -</span>
        </template>

        <template slot="name" slot-scope="text">
          <a-tooltip v-if="text">
            <template slot="title">{{ text }}</template>
            <span class="table-text-ellipsis">{{ text }}</span>
          </a-tooltip>
          <span v-else>- -</span>
        </template>

        <template slot="image" slot-scope="text, record">
          <a-popover v-if="record.image">
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" :src="'http://127.0.0.1:9527/imagesWeb/' + record.image" />
            </template>
            <a-avatar shape="square" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/' + record.image" />
          </a-popover>
          <a-avatar v-else shape="square" icon="user" />
        </template>

        <template slot="collectionCount" slot-scope="text">
          <a-badge v-if="text !== null && text !== undefined" :count="text" :numberStyle="{ backgroundColor: '#1890ff' }" />
          <span v-else style="color: #999;">0</span>
        </template>

        <template slot="questionTypes" slot-scope="text, record">
          <div class="question-type-tags">
            <a-tag color="blue" v-if="record.singleCount > 0">
              单选 {{ record.singleCount }}
            </a-tag>
            <a-tag color="green" v-if="record.multipleCount > 0">
              多选 {{ record.multipleCount }}
            </a-tag>
            <a-tag color="orange" v-if="record.judgeCount > 0">
              判断 {{ record.judgeCount }}
            </a-tag>
            <span v-if="!record.singleCount && !record.multipleCount && !record.judgeCount" style="color: #999;">-</span>
          </div>
        </template>

        <template slot="createDate" slot-scope="text">
          <span v-if="text">{{ text }}</span>
          <span v-else style="color: #999;">- -</span>
        </template>

        <template slot="operation" slot-scope="text, record">
          <a-icon type="eye" theme="twoTone" twoToneColor="#1890ff" @click="showDetail(record)" title="查看详情" style="margin-right: 10px;"></a-icon>
        </template>
      </a-table>
    </div>
    <!-- 详情 Modal -->
    <a-modal
      v-model="detailModal.visible"
      :title="detailModal.title"
      :width="1200"
      :footer="null"
      @cancel="handleDetailClose"
      centered
      :bodyStyle="{ padding: '24px' }"
      wrapClassName="bank-detail-modal">

      <div class="bank-detail-container" v-if="detailModal.loaded">
        <!-- 题库基本信息 -->
        <div class="bank-header">
          <div class="bank-cover">
            <a-avatar v-if="detailModal.data.bank && detailModal.data.bank.image" shape="square" size="120" icon="book" :src="'http://127.0.0.1:9527/imagesWeb/' + detailModal.data.bank.image" />
            <a-avatar v-else shape="square" size="120" icon="book" style="background-color: #1890ff;" />
          </div>
          <div class="bank-info">
            <h2 class="bank-title">{{ (detailModal.data.bank && detailModal.data.bank.name) || '-' }}</h2>
            <div class="bank-meta">
              <a-tag color="blue">{{ (detailModal.data.bank && detailModal.data.bank.code) || '-' }}</a-tag>
              <a-tag><a-icon type="clock-circle" /> {{ (detailModal.data.bank && detailModal.data.bank.createDate) || '-' }}</a-tag>
            </div>
            <div class="bank-stats">
              <a-statistic title="总题数" :value="(detailModal.data.bank && detailModal.data.bank.collectionCount) || 0" :value-style="{ fontSize: '20px', fontWeight: 'bold' }" />
              <a-divider type="vertical" />
              <a-statistic title="单选题" :value="(detailModal.data.bank && detailModal.data.bank.singleCount) || 0" :value-style="{ fontSize: '20px', fontWeight: 'bold', color: '#1890ff' }" />
              <a-divider type="vertical" />
              <a-statistic title="多选题" :value="(detailModal.data.bank && detailModal.data.bank.multipleCount) || 0" :value-style="{ fontSize: '20px', fontWeight: 'bold', color: '#52c41a' }" />
<!--              <a-divider type="vertical" />-->
<!--              <a-statistic title="判断题" :value="(detailModal.data.bank && detailModal.data.bank.judgeCount) || 0" :value-style="{ fontSize: '20px', fontWeight: 'bold', color: '#faad14' }" />-->
            </div>
          </div>
        </div>

        <a-divider style="margin: 24px 0" />

        <!-- 题目列表 -->
        <div class="questions-section">
          <h3 class="section-title">
            <a-icon type="file-text" style="color: #1890ff; margin-right: 8px;" />
            <span>题目列表 ({{ (detailModal.collection && detailModal.collection.length) || 0 }})</span>
          </h3>

          <div class="questions-list">
            <div
              v-for="(question, index) in (detailModal.collection || [])"
              :key="question.id"
              class="question-card"
              :class="'type-' + getQuestionTypeClass(question.questionType)">

              <div class="question-header">
                <div class="question-number">
                  <span>{{ index + 1 }}</span>
                </div>
                <div class="question-meta">
                  <div class="question-info">
                    <a-tag :color="getQuestionTypeColor(question.questionType)">
                      {{ formatQuestionType(question.questionType) }}
                    </a-tag>
                    <a-tag color="purple">{{ question.score }}分</a-tag>
                    <a-tag v-if="question.tags">{{ question.tags }}</a-tag>
                  </div>
                  <div class="question-time">
                    <a-icon type="clock-circle" />
                    <span>{{ question.creationTime || '-' }}</span>
                  </div>
                </div>
              </div>

              <div class="question-content">
                <p class="question-desc">{{ question.desc || '无题目内容' }}</p>
              </div>

              <div class="question-options">
                <div
                  v-for="(option, optIndex) in (question.collectionOptionsList || [])"
                  :key="option.id"
                  class="option-item"
                  :class="{ 'correct': option.isCorrect === 1 }">
                  <div class="option-label">{{ getOptionLabel(optIndex) }}</div>
                  <div class="option-content">{{ option.content }}</div>
                  <div class="option-mark" v-if="option.isCorrect === 1">
                    <a-icon type="check-circle" theme="filled" />
                    <span>正确答案</span>
                  </div>
                </div>
              </div>
            </div>

            <a-empty v-if="!detailModal.collection || detailModal.collection.length === 0" description="暂无题目" />
          </div>
        </div>
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
import BulletinAdd from './BankAdd.vue'
import BulletinEdit from './BankEdit.vue'
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
        title: '题库详情',
        data: {},
        collection: [],
        loaded: false
      }
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '题库编号',
        dataIndex: 'code',
        ellipsis: true,
        scopedSlots: { customRender: 'code' },
        width: 180
      }, {
        title: '题库名称',
        dataIndex: 'name',
        ellipsis: true,
        scopedSlots: { customRender: 'name' },
        width: 250
      }, {
        title: '所属方向',
        dataIndex: 'tagName',
        ellipsis: true,
        width: 100
      }, {
        title: '维度',
        dataIndex: 'dimension',
        ellipsis: true,
        width: 100
      }, {
        title: '图册',
        dataIndex: 'image',
        ellipsis: true,
        scopedSlots: { customRender: 'image' },
        width: 100
      }, {
        title: '题目数量',
        dataIndex: 'collectionCount',
        ellipsis: true,
        scopedSlots: { customRender: 'collectionCount' },
        width: 100
      }, {
        title: '题型分布',
        ellipsis: true,
        scopedSlots: { customRender: 'questionTypes' },
        width: 180
      }, {
        title: '发布时间',
        dataIndex: 'createDate',
        ellipsis: true,
        scopedSlots: { customRender: 'createDate' },
        width: 180
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
        width: 120,
        fixed: 'right'
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    showDetail (record) {
      this.detailModal.visible = true
      this.detailModal.loaded = false
      this.queryBankDetail(record)
    },

    handleDetailClose () {
      this.detailModal.visible = false
      this.detailModal.data = {}
      this.detailModal.collection = []
      this.detailModal.loaded = false
    },

    // 获取题型颜色
    getQuestionTypeColor (type) {
      const map = {
        '单选题': 'blue',
        '多选题': 'green',
        '判断题': 'orange'
      }
      return map[type] || 'default'
    },

    // 格式化题型显示
    formatQuestionType (type) {
      return type || '未知题型'
    },

    // 获取题型样式类
    getQuestionTypeClass (type) {
      const map = {
        '单选题': 'single',
        '多选题': 'multiple',
        '判断题': 'judge'
      }
      return map[type] || ''
    },

    // 获取选项标签
    getOptionLabel (index) {
      return String.fromCharCode(65 + index)
    },

    queryBankDetail (record) {
      this.$get('/business/question-bank/queryBankDetail', {
        bandId: record.id
      }).then((r) => {
        this.detailModal.data = r.data
        this.detailModal.data.bank = record
        this.detailModal.collection = r.data.collection || []
        this.detailModal.loaded = true
      }).catch(() => {
        this.detailModal.loaded = true
      })
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
      this.$message.success('新增题库成功')
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
      this.$message.success('修改题库成功')
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
          that.$delete('/business/question-bank/' + ids).then(() => {
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
      this.$get('/business/question-bank/page', {
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

.table-text-ellipsis {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.question-type-tags {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.table-text-ellipsis {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.question-type-tags {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.bank-detail-modal {
  :deep(.ant-modal-body) {
    max-height: 80vh;
    overflow-y: auto;
  }
}

.bank-detail-container {
  .bank-header {
    display: flex;
    gap: 24px;
    padding: 20px;
    background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
    border-radius: 12px;

    .bank-cover {
      flex-shrink: 0;

      .ant-avatar {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }
    }

    .bank-info {
      flex: 1;

      .bank-title {
        font-size: 24px;
        font-weight: 600;
        color: #262626;
        margin: 0 0 12px 0;
      }

      .bank-meta {
        display: flex;
        gap: 8px;
        margin-bottom: 16px;
      }

      .bank-stats {
        display: flex;
        align-items: center;
        gap: 16px;
      }
    }
  }

  .questions-section {
    .section-title {
      font-size: 18px;
      font-weight: 600;
      color: #262626;
      margin-bottom: 20px;
      display: flex;
      align-items: center;
    }

    .questions-list {
      .question-card {
        background: #fff;
        border: 2px solid #e8e8e8;
        border-radius: 8px;
        margin-bottom: 16px;
        transition: all 0.3s;

        &:hover {
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        &.type-single {
          border-left: 4px solid #1890ff;
        }

        &.type-multiple {
          border-left: 4px solid #52c41a;
        }

        &.type-judge {
          border-left: 4px solid #faad14;
        }

        .question-header {
          display: flex;
          gap: 16px;
          padding: 16px 20px;
          background: #fafafa;
          border-radius: 8px 8px 0 0;
          border-bottom: 1px solid #e8e8e8;

          .question-number {
            width: 36px;
            height: 36px;
            line-height: 36px;
            text-align: center;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #fff;
            border-radius: 50%;
            font-weight: bold;
            font-size: 16px;
            flex-shrink: 0;
          }

          .question-meta {
            flex: 1;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .question-info {
              display: flex;
              gap: 8px;
              align-items: center;
              flex-wrap: wrap;
            }

            .question-time {
              color: #8c8c8c;
              font-size: 13px;
              display: flex;
              align-items: center;
              gap: 4px;
            }
          }
        }

        .question-content {
          padding: 20px;

          .question-desc {
            margin: 0;
            line-height: 1.8;
            color: #262626;
            font-size: 15px;
            white-space: pre-wrap;
          }
        }

        .question-options {
          padding: 0 20px 20px 20px;

          .option-item {
            display: flex;
            gap: 12px;
            padding: 12px 16px;
            margin-bottom: 8px;
            background: #fafafa;
            border-radius: 6px;
            border: 2px solid transparent;
            transition: all 0.3s;

            &:hover {
              background: #fff;
              box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
            }

            &.correct {
              background: #f6ffed;
              border-color: #b7eb8f;
            }

            .option-label {
              width: 28px;
              height: 28px;
              line-height: 28px;
              text-align: center;
              background: #1890ff;
              color: #fff;
              border-radius: 50%;
              font-weight: bold;
              font-size: 14px;
              flex-shrink: 0;
            }

            .option-content {
              flex: 1;
              line-height: 28px;
              color: #262626;
              font-size: 14px;
            }

            .option-mark {
              display: flex;
              align-items: center;
              gap: 6px;
              color: #52c41a;
              font-weight: 500;
              font-size: 13px;

              .anticon {
                font-size: 16px;
              }
            }
          }
        }
      }
    }
  }
}
</style>
