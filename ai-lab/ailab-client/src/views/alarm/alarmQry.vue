<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input  v-model="filters.cId" placeholder="设备号" style="width:160px;"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="filters.sId" placeholder="传感器号" style="width:160px;"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="filters.alarmType" placeholder="告警类型" style="width:160px;"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="getAlarms">查询</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <template>
            <el-table :data="alarms" highlight-current-row v-loading="listLoading" style="width: 100%;">
                <el-table-column prop="cId" label="设备号" min-width="140">
                </el-table-column>
                <el-table-column prop="sId" label="传感器号" width="140">
                </el-table-column>
                <el-table-column prop="alarmType" label="告警类型" width="160" sortable>
                </el-table-column>
                <el-table-column prop="alarmCode" label="告警ID" width="160" sortable>
                </el-table-column>
                <el-table-column prop="alarmInfo" label="告警信息" width="140">
                </el-table-column>
                <el-table-column prop="aFlag" label="告警状态" width="140">
                </el-table-column>
                <el-table-column prop="entryTime" label="告警上报时间" width="140">
                </el-table-column>
                <el-table-column prop="rollbackAlarmId" label="告警恢复id" width="140">
                </el-table-column>
                <el-table-column prop="rollbackTime" label="告警恢复时间" width="140">
                </el-table-column>
                <el-table-column prop="rollbackFlag" label="告警恢复方式" width="140">
                </el-table-column>
                <el-table-column prop="rollbackDes" label="告警恢复原因" width="140">
                </el-table-column>
                <el-table-column label="操作" width="150">
                    <template scope="scope">
                        <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-col :span="24" class="toolbar">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                               :current-page="pageObj.curPage" :page-sizes="[10, 20, 50, 100]"
                               :page-size="pageObj.pageSize" :total="pageObj.total"
                               layout="total, sizes, prev, pager, next, jumper" style="float:right;">
                </el-pagination>
            </el-col>
        </template>
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="140px" :rules="editFormRules" ref="editForm">
                <el-form-item label="告警恢复原因" prop="rollbackDes">
                    <el-input v-model="editForm.rollbackDes" auto-complete="off" :maxlength="maxlength"
                              style="width:80%;"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
                <el-button @click.native="editFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

    </section>
</template>
<style>
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
</style>

<script>
import {qryAlarmList} from '../../api/alarm'
import {updateAlarm} from '../../api/alarm'

export default {
  data () {
    return {
      filters: {
        cId: '',
        sId: '',
        alarmType: ''
      },
      // 分页对象
      pageObj: {
        pageNum: 1, // 页码
        pageSize: 10, // 每页数量
        total: 0, // 总数
        curPage: 1, // 当前页
        orderBy: ''
      },
      listLoading: false,
      alarms: [],
      editFormVisible: false, // 编辑界面是否显示
      editLoading: false,
      maxlength: 200,
      editFormRules: {
        name: [
          { required: true, message: '请输入恢复原因', trigger: 'blur' },
          { min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur' }
        ]
      }

    }
  },
  methods: {

    // 获取用户列表
    getAlarms: function () {
      this.listLoading = false

      // 默认以创建时间倒序排列
      if (this.pageObj.orderBy == '') {
        this.pageObj.orderBy = `entry_time desc`
      }

      // 对象合并:查询对象+分页对象
      let params = Object.assign({}, this.filters, this.pageObj)

      qryAlarmList(params).then((res) => {
        this.listLoading = false

        let {msg, code, data} = res
        this.alarms = res.data.list

        this.pageObj.pageNum = data.pageNum
        this.pageObj.pageSize = data.pageSize
        this.pageObj.total = data.total
      })
    },

    // 显示编辑界面
    handleEdit: function (index, row) {
      this.editFormVisible = true
      this.editForm = Object.assign({}, row)
    },

    // 编辑界面数据
    editForm: {
      rollbackDes: ''
    },
    // 编辑
    editSubmit: function () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
          this.editLoading = true
          // NProgress.start();
          let para = Object.assign({}, this.editForm)
          // delete para.rollbackDes
              console.log(para);
              updateAlarm(para).then((res) => {
            this.editLoading = false
            let { code, msg } = res
            // NProgress.done();
            if (code !== 0) {
              this.$message({
                message: msg,
                type: 'error'
              })
            } else {
              this.$message({
                message: '编辑成功',
                type: 'success'
              })
              this.$refs['editForm'].resetFields()
              this.editFormVisible = false
              // this.getFaultType();
            }
          })
          });
        }
      })
    },

    // 每页数量改变
    handleSizeChange (val) {
      this.pageObj.pageSize = val
      this.getAlarms()
    },
    // 页码改变
    handleCurrentChange (val) {
      this.pageObj.pageNum = val
      this.getAlarms()
    },
    // 排序
    handleSortChange (val) {
      let order = val.order == 'descending' ? 'desc' : 'asc'
      this.pageObj.orderBy = `${val.prop} ${order}`
      this.getAlarms()
    }
  },
  mounted () {
    this.getAlarms()
  }
}

</script>

<style lang="scss" scoped>

</style>