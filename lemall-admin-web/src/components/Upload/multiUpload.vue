<template>
  <div>
    <el-upload
      :action="''"
      list-type="picture-card"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-preview="handlePreview"
      :multiple="true"
      :limit="maxCount"
      :on-exceed="handleExceed"
    >
      <i class="el-icon-plus"></i>
    </el-upload>

    <el-dialog :visible.sync="dialogVisible" width="50%">
      <img width="100%" :src="dialogImageUrl" alt="图片预览" />
    </el-dialog>
  </div>
</template>

<script>
import { uploadFile } from '@/utils/upload'

export default {
  name: 'multiUpload',
  props: {
    value: {
      type: Array,
      default: () => []
    },
    maxCount: {
      type: Number,
      default: 5
    }
  },
  data() {
    return {
      dialogVisible: false,
      dialogImageUrl: '',
      fileList: []
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        this.fileList = val.map(url => ({ name: url.split('/').pop(), url }))
      }
    }
  },
  methods: {
    emitInput(val) {
      this.$emit('input', val)
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
      this.emitInput(fileList.map(f => f.url))
    },
    handlePreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    beforeUpload(file) {
      uploadFile(file)
        .then(res => {
          const newFile = { name: file.name, url: res.data.url }
          this.fileList.push(newFile)
          this.emitInput(this.fileList.map(f => f.url))
        })
        .catch(err => {
          console.error('上传失败', err)
          this.$message.error('上传失败')
        })
      return false
    },
    handleExceed(files, fileList) {
      this.$message.warning(`最多只能上传${this.maxCount}张图片`)
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
