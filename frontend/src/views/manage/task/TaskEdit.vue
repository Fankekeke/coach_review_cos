<template>
  <a-modal v-model="show" title="修改评估任务" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='任务名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'taskName',
            { rules: [{ required: true, message: '请输入任务名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-form-item label='所属方向' v-bind="formItemLayout">
          <a-select
            v-decorator="['tagId', { rules: [{ required: true, message: '请选择所属方向!' }] }]"
            placeholder="请选择所属方向"
            size="large">
            <a-select-option v-for="tag in tagList" :key="tag.id" :value="tag.id">
              {{ tag.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-col :span="12">
          <a-form-item label='任务类型' v-bind="formItemLayout">
            <a-select v-decorator="[
              'taskType',
              { rules: [{ required: true, message: '请选择任务类型!' }] }
              ]">
              <a-select-option :value="1">视频</a-select-option>
              <a-select-option :value="2">文档</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='任务维度' v-bind="formItemLayout">
            <a-select v-decorator="[
              'taskDimension',
              { rules: [{ required: true, message: '请选择任务维度!' }] }
              ]">
              <a-select-option value="专业技能">专业技能</a-select-option>
              <a-select-option value="沟通能力">沟通能力</a-select-option>
              <a-select-option value="团队协作">团队协作</a-select-option>
              <a-select-option value="应急处理">应急处理</a-select-option>
              <a-select-option value="学习能力">学习能力</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='方向ID' v-bind="formItemLayout">
            <a-input-number v-decorator="[
            'tagId',
            { rules: [{ required: true, message: '请输入方向ID!' }] }
            ]" :min="1" style="width: 100%"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='任务描述' v-bind="formItemLayout">
            <a-textarea :rows="4" v-decorator="[
            'taskDescription',
             { rules: [{ required: true, message: '请输入任务描述!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='评分标准' v-bind="formItemLayout">
            <a-textarea :rows="4" v-decorator="[
            'scoringStandard',
             { rules: [{ required: true, message: '请输入评分标准!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='参考材料' v-bind="formItemLayout">
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
                <div class="ant-upload-text">
                  Upload
                </div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='状态' v-bind="formItemLayout">
            <a-select v-decorator="[
              'status',
              { rules: [{ required: true, message: '请选择状态!' }] }
              ]">
              <a-select-option :value="0">关闭</a-select-option>
              <a-select-option :value="1">进行中</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
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
  name: 'BulletinEdit',
  props: {
    bulletinEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.bulletinEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      tagList: [],
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  mounted () {
    this.queryTagList()
  },
  methods: {
    queryTagList () {
      this.$get('/business/tag-info/list').then((r) => {
        this.tagList = r.data.data
      })
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
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    setFormValues ({...bulletin}) {
      this.rowId = bulletin.id
      let fields = ['taskName', 'taskType', 'taskDimension', 'taskDescription', 'scoringStandard', 'referenceMaterial', 'deadline', 'status', 'tagId']
      let obj = {}
      Object.keys(bulletin).forEach((key) => {
        if (key === 'images' || key === 'referenceMaterial') {
          this.fileList = []
          this.imagesInit(bulletin['images'] || bulletin['referenceMaterial'])
        }
        if (key === 'status' || key === 'taskType' || key === 'tagId') {
          bulletin[key] = bulletin[key] !== null && bulletin[key] !== undefined ? Number(bulletin[key]) : undefined
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = bulletin[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$put('/business/assessment-task', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
