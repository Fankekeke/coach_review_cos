<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="问题内容"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.content"/>
              </a-form-item>
            </a-col>
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
                label="状态"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-select v-model="queryParams.status" placeholder="请选择状态" allow-clear>
                  <a-select-option value="已回答">已回答</a-select-option>
                  <a-select-option value="进行中">进行中</a-select-option>
                </a-select>
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
        <template slot="operation" slot-scope="text, record">
          <a-icon type="eye" theme="twoTone" twoToneColor="#1890ff" @click="showDetail(record)" title="查看详情" style="margin-right: 10px;"></a-icon>
<!--          <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>-->
        </template>
      </a-table>
    </div>
    <!-- 详情 Modal -->
    <a-modal
      v-model="detailModal.visible"
      :title="detailModal.title"
      :width="900"
      :footer="null"
      @cancel="handleDetailClose"
      centered
      :bodyStyle="{ padding: '24px' }">
      <div class="detail-container">
        <div class="detail-header">
          <div class="header-avatar">
            <a-avatar v-if="detailModal.data.userImages" shape="circle" size="80" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/' + detailModal.data.userImages" />
            <a-avatar v-else shape="circle" size="80" icon="user" style="background-color: #1890ff;" />
          </div>
          <div class="header-info">
            <div class="user-name">{{ detailModal.data.userName || '匿名用户' }}</div>
            <div class="question-status">
              <a-tag v-if="detailModal.data.status === '已回答'" color="success" style="font-size: 14px;">
                <a-icon type="check-circle" /> 已回答
              </a-tag>
              <a-tag v-else-if="detailModal.data.status === '进行中'" color="processing" style="font-size: 14px;">
                <a-icon type="loading" /> 进行中
              </a-tag>
              <span v-else style="color: #999;">- -</span>
            </div>
          </div>
        </div>

        <a-divider style="margin: 16px 0" />

        <div class="detail-content">
          <div class="content-section">
            <div class="section-title">
              <a-icon type="question-circle" style="color: #1890ff; margin-right: 8px;" />
              <span>问题内容</span>
            </div>
            <div class="section-body">
              <p class="text-content">{{ detailModal.data.content || '暂无内容' }}</p>
            </div>
          </div>

          <div class="content-section">
            <div class="section-title">
              <a-icon type="robot" style="color: #52c41a; margin-right: 8px;" />
              <span>AI 答疑</span>
            </div>
            <div class="section-body ai-answer">
              <p class="text-content">{{ detailModal.data.aiAnswer || detailModal.data.content || '暂无 AI 回答' }}</p>
            </div>
          </div>

          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">
                <a-icon type="tags" style="color: #722ed1;" />
                <span>关键词</span>
              </div>
              <div class="info-value">
                <a-tag color="blue">{{ detailModal.data.keyWord || '- -' }}</a-tag>
              </div>
            </div>

            <div class="info-item">
              <div class="info-label">
                <a-icon type="clock-circle" style="color: #faad14;" />
                <span>创建时间</span>
              </div>
              <div class="info-value">
                <span>{{ detailModal.data.createdAt || '- -' }}</span>
              </div>
            </div>
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
import BulletinAdd from './UserQuestionsAdd.vue'
import BulletinEdit from './UserQuestionsEdit.vue'
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
        title: '问题详情',
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
        title: '关键词',
        dataIndex: 'keyWord',
        ellipsis: true
      }, {
        title: '问题内容',
        dataIndex: 'content',
        ellipsis: true
      }, {
        title: 'AI答疑内容',
        dataIndex: 'aiAnswer',
        ellipsis: true
      }, {
        title: '用户名称',
        ellipsis: true,
        dataIndex: 'userName',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
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
        title: '状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case '已回答':
              return <a-tag type="success">已回答</a-tag>
            case '进行中':
              return <a-tag type="warning">进行中</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '创建时间',
        dataIndex: 'createdAt',
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
      this.$message.success('新增AI答疑成功')
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
      this.$message.success('修改AI答疑成功')
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
          that.$delete('/business/user-questions/' + ids).then(() => {
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
      if (params.status === undefined) {
        delete params.status
      }
      params.userId = this.currentUser.userId
      this.$get('/business/user-questions/page', {
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
.detail-container {
  .detail-header {
    display: flex;
    align-items: center;
    padding: 16px;
    background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
    border-radius: 8px;
    margin-bottom: 16px;

    .header-avatar {
      margin-right: 24px;

      .ant-avatar {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }
    }

    .header-info {
      flex: 1;

      .user-name {
        font-size: 20px;
        font-weight: 600;
        color: #262626;
        margin-bottom: 8px;
      }

      .question-status {
        display: flex;
        align-items: center;
      }
    }
  }

  .detail-content {
    .content-section {
      margin-bottom: 24px;

      .section-title {
        display: flex;
        align-items: center;
        font-size: 16px;
        font-weight: 600;
        color: #262626;
        margin-bottom: 12px;
        padding: 8px 12px;
        background: #f5f5f5;
        border-radius: 4px;
        border-left: 4px solid #1890ff;
      }

      .section-body {
        padding: 16px;
        background: #fafafa;
        border-radius: 6px;
        border: 1px solid #e8e8e8;
        min-height: 80px;

        &.ai-answer {
          background: #f6ffed;
          border-color: #b7eb8f;
          border-left: 4px solid #52c41a;
        }

        .text-content {
          margin: 0;
          line-height: 1.8;
          color: #262626;
          font-size: 14px;
          white-space: pre-wrap;
          word-break: break-all;
        }
      }
    }

    .info-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
      margin-top: 24px;

      .info-item {
        display: flex;
        flex-direction: column;
        padding: 16px;
        background: #f5f5f5;
        border-radius: 6px;
        border: 1px solid #e8e8e8;

        .info-label {
          display: flex;
          align-items: center;
          font-size: 13px;
          color: #8c8c8c;
          margin-bottom: 8px;
          font-weight: 500;

          span {
            margin-left: 6px;
          }
        }

        .info-value {
          font-size: 15px;
          color: #262626;
          font-weight: 500;
        }
      }
    }
  }
}
</style>
