<template>
  <a-modal v-model="show" title="新增房间" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='房间名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'roomName',
            { rules: [{ required: true, message: '请输入房间名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='最大人数' v-bind="formItemLayout">
            <a-input-number
              v-decorator="[
                'maxMemberCount',
                { rules: [{ required: true, message: '请输入最大人数!' }] }
              ]"
              :min="1"              style="width: 100%"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='讨论时长(分钟)' v-bind="formItemLayout">
            <a-input-number
              v-decorator="[
                'durationMinutes',
                { rules: [{ required: true, message: '请输入讨论时长!' }] }
              ]"
              :min="5"
              :max="60"              style="width: 100%"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='是否自动开始' v-bind="formItemLayout">
            <a-select v-decorator="[
              'autoStart',
              { rules: [{ required: true, message: '请选择是否自动开始!' }] }
              ]">
              <a-select-option :value="0">否</a-select-option>
              <a-select-option :value="1">是</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='讨论主题' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'topic',
             { rules: [{ required: true, message: '请输入讨论主题!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
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
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
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
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$post('/business/discussion-room', {
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
