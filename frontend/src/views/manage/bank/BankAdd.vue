<template>
  <a-drawer
    placement="right"
    width="100%"
    :closable="false"
    :visible="show"
    destroyOnClose
    wrapClassName="aa"
    :getContainer="false"
  >
<!--    <div class="drawer-bootom-button">-->
<!--      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">-->
<!--        <a-button style="margin-right: .8rem">取消</a-button>-->
<!--      </a-popconfirm>-->
<!--      <a-button @click="handleSubmit" type="primary" :loading="loading">提交</a-button>-->
<!--    </div>-->
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="6">
          <div class="basic-info-card">
            <a-card title="基本信息" :bordered="false">
              <a-form-item label='题库标题' v-bind="formItemLayout">
                <a-input
                  v-decorator="['name', { rules: [{ required: true, message: '请输入名称!' }] }]"
                  placeholder="请输入题库标题"
                  size="large" />
              </a-form-item>

              <a-divider style="margin: 16px 0" />

              <a-form-item label='图册' v-bind="formItemLayout">
                <a-upload
                  name="avatar"
                  action="http://127.0.0.1:9527/file/fileUpload/"
                  list-type="picture-card"
                  :file-list="fileList"
                  @preview="handlePreview"
                  @change="picHandleChange"
                >
                  <div v-if="fileList.length < 8">
                    <a-icon type="plus" />
                    <div class="ant-upload-text">上传封面</div>
                  </div>
                </a-upload>
                <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                  <img alt="example" style="width: 100%" :src="previewImage" />
                </a-modal>
              </a-form-item>
            </a-card>

            <!-- 统计信息卡片 -->
            <a-card class="stats-card" :bordered="false">
              <a-statistic title="题目数量" :value="formFix.questions.length" :value-style="{ color: '#1890ff', fontSize: '24px', fontWeight: 'bold' }">
                <template #prefix>
                  <a-icon type="file-text" />
                </template>
              </a-statistic>
              <a-statistic title="总分值" :value="totalScore" :value-style="{ color: '#52c41a', fontSize: '24px', fontWeight: 'bold' }" suffix="分">
                <template #prefix>
                  <a-icon type="trophy" />
                </template>
              </a-statistic>
            </a-card>
          </div>
        </a-col>

        <a-col :span="18">
          <!-- 工具栏 -->
          <div class="toolbar">
            <div class="toolbar-buttons">
              <a-button type="primary" icon="plus" @click="addQuestion" size="large">
                添加题目
              </a-button>
<!--              <a-button icon="copy" @click="batchAddQuestions" size="large">-->
<!--                批量导入-->
<!--              </a-button>-->
              <a-divider type="vertical" />
              <a-button @click="expandAll" size="large">
                展开全部
              </a-button>
              <a-button @click="collapseAll" size="large">
                折叠全部
              </a-button>
              <a-divider type="vertical" />
              <a-badge :count="formFix.questions.length" :numberStyle="{ backgroundColor: '#52c41a' }">
                <span style="margin-left: 8px; font-size: 14px;">题目总数</span>
              </a-badge>
            </div>
          </div>

          <!-- 题目列表 -->
          <div class="questions-container">
            <div
              v-for="(question, qIndex) in formFix.questions"
              :key="qIndex"
              class="question-card"
              :class="{ 'active': activeKey.includes(qIndex) }">

              <div class="question-card-header" @click="togglePanel(qIndex)">
                <div class="header-left">
                  <a-icon
                    :type="activeKey.includes(qIndex) ? 'down' : 'right'"
                    class="expand-icon" />
                  <span class="panel-title">题目 {{ qIndex + 1 }}</span>
                  <a-tag :color="getQuestionTypeColor(question.questionType)">
                    {{ getQuestionTypeLabel(question.questionType) }}
                  </a-tag>
                  <a-tag v-if="isQuestionComplete(question)" color="green">
                    <a-icon type="check-circle" /> 已完成
                  </a-tag>
                  <a-tag v-else color="yellow">
                    <a-icon type="exclamation-circle" /> 待完善
                  </a-tag>
                </div>
                <div class="header-right">
                  <a-tag color="green">{{ question.score }}分</a-tag>
                  <a-tag color="purple">{{ question.tags || '未设置标签' }}</a-tag>
                </div>
              </div>

              <div v-show="activeKey.includes(qIndex)" class="question-card-content">
                <!-- 题目标签 -->
                <div class="question-tags" style="width: 100%">
                  <a-alert
                    message="请完善题目信息"
                    description="填写题目描述、选择题型、设置分值和标签，然后添加选项并标记正确答案"
                    type="info"
                    show-icon                    style="margin-bottom: 16px;" />
                </div>

                <!-- 题目描述 -->
                <a-form-model-item label="题目描述" :labelCol="{ span: 24 }" :wrapperCol="{ span: 24 }">
                  <a-textarea
                    v-model="question.desc"
                    placeholder="请输入题目描述"
                    :rows="3"
                    show-count
                    :maxlength="500" />
                </a-form-model-item>

                <!-- 分值和标签 -->
                <a-row :gutter="16">
                  <a-col :span="8">
                    <!-- 题目类型 -->
                    <a-form-model-item label="题目类型" :labelCol="{ span: 24 }" :wrapperCol="{ span: 24 }">
                      <a-radio-group v-model="question.questionType" button-style="solid">
                        <a-radio-button value="单选题">单选题</a-radio-button>
                        <a-radio-button value="多选题">多选题</a-radio-button>
<!--                        <a-radio-button value="判断题">判断题</a-radio-button>-->
                      </a-radio-group>
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-model-item label="分值" :labelCol="{ span: 24 }" :wrapperCol="{ span: 24 }">
                      <a-input-number
                        v-model="question.score"
                        :min="1"
                        :max="100"
                        style="width: 100%" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-model-item label="标签" :labelCol="{ span: 24 }" :wrapperCol="{ span: 24 }">
                      <a-input
                        v-model="question.tags" />
                    </a-form-model-item>
                  </a-col>
                </a-row>

                <!-- 选项列表 -->
                <a-divider orientation="left">
                  <span style="font-weight: 500;">选项列表</span>
                  <a-button type="dashed" size="small" icon="plus" @click.stop="addOption(qIndex)" style="margin-left: 12px;" v-if="question.questionType !== '判断题'">
                    添加选项
                  </a-button>
                </a-divider>

                <div class="options-list">
                  <div
                    v-for="(option, oIndex) in question.options"
                    :key="oIndex"
                    class="option-item"
                    :class="{ 'is-correct': option.isCorrect }">

                    <a-row :gutter="16" align="middle">
                      <a-col :span="2">
                        <div class="option-label">{{ getOptionLabel(oIndex) }}</div>
                      </a-col>
                      <a-col :span="14">
                        <a-input
                          v-model="option.content"
                          placeholder="请输入选项内容"
                          @blur="validateOptions(qIndex)" />
                      </a-col>
                      <a-col :span="6">
                        <a-checkbox
                          v-if="question.questionType === '多选题'"
                          v-model="option.isCorrect"
                          :checked="option.isCorrect">
                          <span :class="{ 'correct-text': option.isCorrect, 'error-text': !option.isCorrect }">
                            {{ option.isCorrect ? '✓ 正确答案' : '✗ 错误答案' }}
                          </span>
                        </a-checkbox>
                        <a-radio
                          v-else-if="question.questionType === '单选题'"
                          :checked="option.isCorrect"
                          @change="onRadioChange($event, qIndex, oIndex)">
                          <span :class="{ 'correct-text': option.isCorrect, 'error-text': !option.isCorrect }">
                            {{ option.isCorrect ? '✓ 正确答案' : '✗ 错误答案' }}
                          </span>
                        </a-radio>
                        <div v-else-if="question.questionType === '判断题'" class="judge-tip" :class="'judge-' + oIndex">
                          <a-icon :type="oIndex === 0 ? 'check-circle' : 'close-circle'" />
                          {{ oIndex === 0 ? '正确' : '错误' }}
                        </div>
                      </a-col>
                      <a-col :span="2" v-if="question.questionType !== '判断题'">
                        <a-popconfirm
                          title="确定删除此选项吗？"
                          @confirm="removeOption(qIndex, oIndex)"
                          okText="确定"
                          cancelText="取消">
                          <a-button
                            type="danger"
                            icon="delete"
                            shape="circle"
                            size="small"
                            :disabled="question.options.length <= 2"                            style="cursor: pointer;" />
                        </a-popconfirm>
                      </a-col>
                    </a-row>
                  </div>
                </div>

                <!-- 参考答案 -->
                <a-divider />
                <a-row>
                  <a-col :span="12">
                    <a-alert
                      message="参考答案"
                      :description="question.correctAnswer || '未生成'"
                      type="success"
                      show-icon />
                  </a-col>
                  <a-col :span="12" style="text-align: right;">
                    <a-popconfirm
                      title="确定删除这个题目吗？"
                      @confirm="removeQuestion(qIndex)"
                      okText="确定"
                      cancelText="取消"
                      v-if="formFix.questions.length > 1">
                      <a-button type="danger" icon="delete">删除题目</a-button>
                    </a-popconfirm>
                  </a-col>
                </a-row>
              </div>
            </div>
          </div>

          <!-- 空状态提示 -->
          <a-empty
            v-if="formFix.questions.length === 0"
            description="还没有添加题目，请点击上方按钮添加题目"
            image="http://127.0.0.1:9527/imagesWeb/m.svg">
            <a-button type="primary" icon="plus" @click="addQuestion" size="large">
              添加第一道题目
            </a-button>
          </a-empty>

          <!-- 提交按钮区域 -->
          <div class="submit-area" v-if="formFix.questions.length > 0">
            <a-card :bordered="false" class="submit-card">
              <a-row align="middle">
                <a-col :span="12">
                  <a-statistic title="当前题目数" :value="formFix.questions.length" :valueStyle="{ fontSize: '20px', fontWeight: 'bold' }" />
                </a-col>
                <a-col :span="12" style="text-align: right;">
                  <a-button @click="handleClose" size="large">取消</a-button>
                  <a-button type="primary" @click="handleSubmit" :loading="submitLoading" size="large" style="margin-left: 12px;">
                    <a-icon type="check" />
                    {{ submitLoading ? '提交中...' : '确认提交' }}
                  </a-button>
                </a-col>
              </a-row>
            </a-card>
          </div>
        </a-col>
      </a-row>
    </a-form>
  </a-drawer>
</template>

<script>
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'BulletinAdd',
  props: {
    bulletinAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.bulletinAddVisiable
      },
      set: function () {
      }
    },
    totalScore () {
      return this.formFix.questions.reduce((sum, q) => sum + (q.score || 0), 0)
    },
    h () {
      return this.$createElement
    }
  },
  data () {
    return {
      submitLoading: false,
      activeKey: [], // 展开的面板 key
      formFix: {
        questions: [{
          desc: '',
          questionType: '单选题',
          score: 10,
          tags: '',
          options: [
            { content: '', isCorrect: false },
            { content: '', isCorrect: false }
          ],
          correctAnswer: ''
        }]
      },
      rules: {
        desc: [
          { required: true, message: '请输入题目描述', trigger: 'blur' }
        ],
        questionType: [
          { required: true, message: '请选择题目类型', trigger: 'change' }
        ],
        score: [
          { required: true, message: '请输入分值', trigger: 'blur' }
        ],
        tags: [
          { required: true, message: '请输入标签', trigger: 'blur' }
        ]
      },
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  watch: {
    'form.questions': {
      handler: function (newQuestions) {
        newQuestions.forEach((question, index) => {
          this.generateCorrectAnswer(index)
        })
      },
      deep: true
    }
  },
  methods: {

    // 切换面板展开/折叠
    togglePanel (index) {
      const key = index.toString()
      if (this.activeKey.includes(index)) {
        this.activeKey = this.activeKey.filter(k => k !== index)
      } else {
        this.activeKey = [...this.activeKey, index]
      }
    },

    // 判断题目是否完成
    isQuestionComplete (question) {
      const hasDesc = question.desc && question.desc.trim()
      const hasOptions = question.options.every(opt => opt.content || question.questionType === '判断题')
      const hasCorrect = question.options.some(opt => opt.isCorrect)
      return hasDesc && hasOptions && hasCorrect
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

    // 获取题型标签
    getQuestionTypeLabel (type) {
      const map = {
        '单选题': '单选题',
        '多选题': '多选题',
        '判断题': '判断题'
      }
      return map[type] || type
    },

    // 展开所有
    expandAll () {
      this.activeKey = this.formFix.questions.map((_, index) => index)
    },

    // 折叠所有
    collapseAll () {
      this.activeKey = []
    },

    // 批量导入（简化版）
    batchAddQuestions () {
      this.$message.info('批量导入功能开发中...')
    },

    // 获取选项标签
    getOptionLabel (index) {
      return String.fromCharCode(65 + index)
    },

    // 单选框改变
    onRadioChange (e, questionIndex, optionIndex) {
      const question = this.formFix.questions[questionIndex]
      question.options.forEach((opt, i) => {
        opt.isCorrect = (i === optionIndex)
      })
      this.generateCorrectAnswer(questionIndex)
    },

    // 重置选项
    resetOptions (questionIndex, type) {
      const question = this.formFix.questions[questionIndex]
      if (type === '判断题') {
        question.options = [
          { content: '正确', isCorrect: true },
          { content: '错误', isCorrect: false }
        ]
      } else {
        question.options = [
          { content: '', isCorrect: false },
          { content: '', isCorrect: false }
        ]
      }
      this.generateCorrectAnswer(questionIndex)
    },

    // 添加选项
    addOption (questionIndex) {
      const question = this.formFix.questions[questionIndex]
      question.options.push({ content: '', isCorrect: false })
    },

    // 删除选项
    removeOption (questionIndex, optionIndex) {
      const question = this.formFix.questions[questionIndex]
      if (question.options.length > 2) {
        question.options.splice(optionIndex, 1)
        this.generateCorrectAnswer(questionIndex)
      } else {
        this.$message.warning('至少需要保留两个选项')
      }
    },

    // 添加题目
    addQuestion () {
      const newIndex = this.formFix.questions.length
      this.formFix.questions.push({
        desc: '',
        questionType: '单选题',
        score: 10,
        tags: '',
        options: [
          { content: '', isCorrect: false },
          { content: '', isCorrect: false }
        ],
        correctAnswer: ''
      })
      // 自动展开新题目
      this.activeKey = [newIndex.toString()]
      // 滚动到底部
      this.$nextTick(() => {
        const container = document.querySelector('.questions-container')
        if (container) {
          container.scrollTop = container.scrollHeight
        }
      })
    },

    // 删除题目
    removeQuestion (questionIndex) {
      if (this.formFix.questions.length > 1) {
        this.formFix.questions.splice(questionIndex, 1)
        // 更新展开的面板
        if (this.activeKey.includes(questionIndex.toString())) {
          this.activeKey = this.activeKey.filter(key => parseInt(key) !== questionIndex)
        }
      }
    },

    // 验证选项
    validateOptions (questionIndex) {
      const question = this.formFix.questions[questionIndex]
      const emptyOption = question.options.find(opt => !opt.content.trim() && question.questionType !== '判断题')
      if (emptyOption) {
        this.$message.warning('请填写完整的选项内容')
      }
    },

    // 生成正确答案
    generateCorrectAnswer (questionIndex) {
      const question = this.formFix.questions[questionIndex]
      const correctOptions = question.options
        .filter(opt => opt.isCorrect)
        .map((opt, index) => String.fromCharCode(65 + index))

      if (question.questionType === '单选题') {
        question.correctAnswer = correctOptions[0] || ''
      } else if (question.questionType === '多选题') {
        question.correctAnswer = correctOptions.join('') || ''
      } else if (question.questionType === '判断题') {
        const isCorrect = question.options[0].isCorrect
        question.correctAnswer = isCorrect ? 'true' : 'false'
      }
    },

    // 提交
    handleSubmit () {
      // 验证逻辑保持不变
      let hasError = false

      for (let qIndex = 0; qIndex < this.formFix.questions.length; qIndex++) {
        const question = this.formFix.questions[qIndex]

        if (!question.desc || !question.desc.trim()) {
          this.$message.error(`第 ${qIndex + 1} 题的题目描述不能为空`)
          hasError = true
          break
        }

        if (!question.tags || !question.tags.trim()) {
          this.$message.error(`第 ${qIndex + 1} 题的标签不能为空`)
          hasError = true
          break
        }

        if (!question.score || question.score <= 0) {
          this.$message.error(`第 ${qIndex + 1} 题的分值必须大于 0`)
          hasError = true
          break
        }

        if (question.questionType !== '判断题') {
          const emptyOption = question.options.find(opt => !opt.content || !opt.content.trim())
          if (emptyOption) {
            this.$message.warning(`第 ${qIndex + 1} 题的选项内容不完整`)
            hasError = true
            break
          }
        }

        const hasCorrect = question.options.some(opt => opt.isCorrect)
        if (!hasCorrect) {
          this.$message.warning(`第 ${qIndex + 1} 题请至少设置一个正确答案`)
          hasError = true
          break
        }

        if (question.questionType === '多选题') {
          const correctCount = question.options.filter(opt => opt.isCorrect).length
          if (correctCount < 2) {
            this.$message.warning(`第 ${qIndex + 1} 题是多选题，请至少设置两个正确答案`)
            hasError = true
            break
          }
        }
      }

      if (hasError) return

      const collectionItemList = this.formFix.questions.map(question => ({
        collection: JSON.stringify({
          questionType: question.questionType,
          score: question.score,
          desc: question.desc,
          tags: question.tags
        }),
        collectionOptions: JSON.stringify(question.options),
        answer: question.correctAnswer,
        correctAnswer: question.correctAnswer
      }))

      this.submitLoading = true
      console.log(JSON.stringify(collectionItemList))

      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.image = images.length > 0 ? images.join(',') : null
        values.collectionItemList = JSON.stringify(collectionItemList)
        if (!err) {
          this.submitLoading = true
          this.$post('/business/question-bank', {
            ...values
          }).then((r) => {
            this.$message.success(`成功添加 ${collectionItemList.length} 道题目`)
            this.submitLoading = false
            this.$emit('success', collectionItemList)
            this.handleClose()
          }).catch(() => {
            this.submitLoading = false
          })
        } else {
          this.submitLoading = false
        }
      })
    },

    handleClose () {
      this.resetForm()
      this.$emit('close')
    },

    resetForm () {
      this.activeKey = []
      this.formFix = {
        questions: [{
          desc: '',
          questionType: '单选题',
          score: 10,
          tags: '',
          options: [
            { content: '', isCorrect: false },
            { content: '', isCorrect: false }
          ],
          correctAnswer: ''
        }]
      }
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    // handleSubmit () {
    //   // 获取图片List
    //   let images = []
    //   this.fileList.forEach(image => {
    //     images.push(image.response)
    //   })
    //   this.form.validateFields((err, values) => {
    //     values.images = images.length > 0 ? images.join(',') : null
    //     if (!err) {
    //       this.loading = true
    //       this.$post('/business/question-bank', {
    //         ...values
    //       }).then((r) => {
    //         this.reset()
    //         this.$emit('success')
    //       }).catch(() => {
    //         this.loading = false
    //       })
    //     }
    //   })
    // }
  }
}
</script>

<style lang="less" scoped>.basic-info-card {
  position: sticky;
  top: 20px;

  .stats-card {
    margin-top: 16px;

    :deep(.ant-statistic) {
      margin-bottom: 16px;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

.toolbar {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.questions-container {
  :deep(.questions-collapse) {
    .ant-collapse-item {
      margin-bottom: 16px;
      border: 1px solid #e8e8e8;
      border-radius: 8px;
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }

      > .ant-collapse-header {
        background: #fafafa;
        border-radius: 8px;
        padding: 12px 16px !important;

        .panel-header {
          display: flex;
          align-items: center;
          gap: 12px;

          .panel-title {
            font-size: 16px;
            font-weight: 600;
            color: #262626;
          }

          .warning-tag,
          .complete-tag {
            margin-left: auto;
          }
        }
      }
    }
  }

  .question-content {
    padding: 16px;

    .question-tags {
      margin-bottom: 16px;
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }

    .options-list {
      .option-item {
        padding: 16px;
        margin-bottom: 12px;
        background: #fafafa;
        border-radius: 8px;
        border: 2px solid transparent;
        transition: all 0.3s;

        &.is-correct {
          background: #f6ffed;
          border-color: #b7eb8f;
        }

        &:hover {
          background: #fff;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        }

        .option-label {
          display: inline-block;
          width: 36px;
          height: 36px;
          line-height: 36px;
          text-align: center;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: #fff;
          border-radius: 50%;
          font-weight: bold;
          font-size: 16px;
          box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
        }

        .judge-tip {
          display: inline-flex;
          align-items: center;
          gap: 6px;
          padding: 8px 20px;
          border-radius: 6px;
          font-size: 15px;
          font-weight: 500;

          &.judge-0 {
            background: #f6ffed;
            color: #52c41a;
            border: 2px solid #b7eb8f;
          }

          &.judge-1 {
            background: #fff1f0;
            color: #f5222d;
            border: 2px solid #ffa39e;
          }
        }

        .correct-text {
          color: #52c41a;
          font-weight: 500;
        }

        .error-text {
          color: #8c8c8c;
        }
      }
    }
  }
}

.submit-area {
  margin-top: 24px;

  .submit-card {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;

    :deep(.ant-statistic) {
      .ant-statistic-title {
        color: rgba(255, 255, 255, 0.85);
      }

      .ant-statistic-content {
        color: #fff;
      }
    }
  }
}

.basic-info-card {
  position: sticky;
  top: 20px;

  .stats-card {
    margin-top: 16px;

    :deep(.ant-statistic) {
      margin-bottom: 16px;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

.toolbar {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

  .toolbar-buttons {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-wrap: wrap;
  }
}
</style>
<style lang="less" scoped>.basic-info-card {
  position: sticky;
  top: 20px;

  .stats-card {
    margin-top: 16px;

    :deep(.ant-statistic) {
      margin-bottom: 16px;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

.toolbar {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

  .toolbar-buttons {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-wrap: wrap;
  }
}

.questions-container {
  .question-card {
    background: #fff;
    border: 2px solid #e8e8e8;
    border-radius: 8px;
    margin-bottom: 16px;
    transition: all 0.3s;

    &.active {
      border-color: #1890ff;
      box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2);
    }

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    .question-card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px 20px;
      cursor: pointer;
      background: #fafafa;
      border-radius: 8px;

      &:hover {
        background: #f0f0f0;
      }

      .header-left {
        display: flex;
        align-items: center;
        gap: 12px;

        .expand-icon {
          font-size: 16px;
          color: #1890ff;
          transition: transform 0.3s;
        }

        .panel-title {
          font-size: 16px;
          font-weight: 600;
          color: #262626;
        }
      }

      .header-right {
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }

    .question-card-content {
      padding: 20px;
      border-top: 1px solid #e8e8e8;
    }
  }

  .question-tags {
    margin-bottom: 16px;
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
  }

  .options-list {
    .option-item {
      padding: 16px;
      margin-bottom: 12px;
      background: #fafafa;
      border-radius: 8px;
      border: 2px solid transparent;
      transition: all 0.3s;

      &.is-correct {
        background: #f6ffed;
        border-color: #b7eb8f;
      }

      &:hover {
        background: #fff;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
      }

      .option-label {
        display: inline-block;
        width: 36px;
        height: 36px;
        line-height: 36px;
        text-align: center;
        background: linear-gradient(135deg, #2294d7 0%, #16488a 100%);
        color: #fff;
        border-radius: 50%;
        font-weight: bold;
        font-size: 16px;
        box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
      }

      .judge-tip {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        padding: 8px 20px;
        border-radius: 6px;
        font-size: 15px;
        font-weight: 500;

        &.judge-0 {
          background: #f6ffed;
          color: #52c41a;
          border: 2px solid #b7eb8f;
        }

        &.judge-1 {
          background: #fff1f0;
          color: #f5222d;
          border: 2px solid #ffa39e;
        }
      }

      .correct-text {
        color: #52c41a;
        font-weight: 500;
      }

      .error-text {
        color: #8c8c8c;
      }
    }
  }
}

.submit-area {
  margin-top: 24px;

  .submit-card {
    background: linear-gradient(135deg, #ffffff 0%, #f6f5f5 100%);
    color: #fff;

    :deep(.ant-statistic) {
      .ant-statistic-title {
        color: rgba(255, 255, 255, 0.85);
      }

      .ant-statistic-content {
        color: #fff;
      }
    }
  }
}
</style>
