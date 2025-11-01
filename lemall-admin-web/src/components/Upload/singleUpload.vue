<template>
  <div>
    <el-upload
      :action="''"
      :file-list="fileList"
      :show-file-list="showFileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-preview="handlePreview"
      list-type="picture"
      :multiple="false"
    >
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传图片文件，且不超过10MB</div>
    </el-upload>

    <el-dialog :visible.sync="dialogVisible" width="50%">
      <img width="100%" :src="fileList.length ? fileList[0].url : ''" alt="图片预览">
    </el-dialog>
  </div>
</template>

<script>
import { uploadFile } from '@/utils/upload'

export default {
  name: 'singleUpload',
  props: {
    value: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false,
      fileList: []
    }
  },
  computed: {
    showFileList() {
      return this.fileList.length > 0
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        this.fileList = val && val.url ? [{name: val.url.split('/').pop(), url: val.url}] : []
      }
    }
  },
  methods: {
    emitInput(val) {
      this.$emit('input', val)
    },
    handleRemove() {
      this.fileList = []
      this.emitInput({}) // 删除后发送空对象
    },
    handlePreview() {
      this.dialogVisible = true
    },
    beforeUpload(file) {
      // 阻止 el-upload 默认上传
      // 调用统一上传方法
      uploadFile(file)
        .then(res => {
          // 更新 fileList 以保持 el-upload 样式
          this.fileList = [{name: file.name, url: res.data.url}]
          // 发对象给父组件
          this.emitInput({
            id: res.data.id || '',
            url: res.data.url,
            name: file.name,
            size: file.size
          })
        })
        .catch(err => {
          console.error('上传失败', err)
          this.$message.error('上传失败')
        })

      return false
    }
  }
}
</script>

<style scoped>
.el-upload__tip {
  font-size: 12px;
  color: #999;
}
</style>
