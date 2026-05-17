<template>
  <a-modal v-model="show" title="教练详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="primary">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="moduleData !== null">
      <a-descriptions bordered :column="2">
        <a-descriptions-item label="教练编号">
          {{ moduleData.code }}
        </a-descriptions-item>
        <a-descriptions-item label="教练名称">
          {{ moduleData.name }}
        </a-descriptions-item>
        <a-descriptions-item label="性别">
          {{ moduleData.staffSex === 1 ? '男' : moduleData.staffSex === 2 ? '女' : '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="出生日期">
          {{ moduleData.birthDate || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="邮箱地址">
          {{ moduleData.email || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="联系方式">
          {{ moduleData.phone || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="创建日期">
          {{ moduleData.createDate || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="方向">
          {{ moduleData.tagName || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="备注" :span="2">
          {{ moduleData.content || '-' }}
        </a-descriptions-item>
      </a-descriptions>

      <a-divider>教练评分</a-divider>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-card size="small" title="专业素养" style="margin-bottom: 16px">
            <div style="text-align: center; font-size: 24px; font-weight: bold; color: #1890ff">
              {{ moduleData.professionalScore || '-' }}
            </div>
          </a-card>
        </a-col>
        <a-col :span="12">
          <a-card size="small" title="沟通能力" style="margin-bottom: 16px">
            <div style="text-align: center; font-size: 24px; font-weight: bold; color: #52c41a">
              {{ moduleData.communicationScore || '-' }}
            </div>
          </a-card>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-card size="small" title="团队协作" style="margin-bottom: 16px">
            <div style="text-align: center; font-size: 24px; font-weight: bold; color: #faad14">
              {{ moduleData.teamworkScore || '-' }}
            </div>
          </a-card>
        </a-col>
        <a-col :span="12">
          <a-card size="small" title="应急处理" style="margin-bottom: 16px">
            <div style="text-align: center; font-size: 24px; font-weight: bold; color: #f5222d">
              {{ moduleData.emergencyScore || '-' }}
            </div>
          </a-card>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-card size="small" title="学习能力">
            <div style="text-align: center; font-size: 24px; font-weight: bold; color: #722ed1">
              {{ moduleData.learningScore || '-' }}
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'

moment.locale('zh-cn')

function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

export default {
  name: 'moduleView',
  props: {
    moduleShow: {
      type: Boolean,
      default: false
    },
    moduleData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.moduleShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  watch: {
    moduleShow: function (value) {
      if (value) {
        this.fileList = []
        if (this.moduleData.images) {
          this.imagesInit(this.moduleData.images)
        }
      } else {
        this.fileList = []
        this.previewVisible = false
      }
    }
  },
  methods: {
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
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
    picHandleChange ({fileList}) {
      this.fileList = fileList
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
