<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.sId" placeholder="传感器ID" :maxlength="20"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="getDevices">查询</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="sensorList" highlight-current-row v-loading="listLoading" style="width: 100%;">
            <el-table-column prop="cId" label="控制器ID" min-width="80" sortable>
            </el-table-column>
            
            <el-table-column prop="sId" label="传感器ID" min-width="80" sortable>
            </el-table-column>

            <el-table-column prop="des" label="传感器类型" min-width="60" sortable>
            </el-table-column>

            <el-table-column prop="alarmTypeDes" label="是否告警" min-width="60" sortable>
            </el-table-column>

            <el-table-column label="操作" width="200">
                <template scope="scope">       
                    <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                </template>
            </el-table-column>

            
        </el-table>
        <el-col :span="24" class="toolbar">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageObj.curPage" :page-sizes="[10, 20, 50, 100]" :page-size="pageObj.pageSize" :total="pageObj.total" layout="total, sizes, prev, pager, next, jumper" style="float:right;">
            </el-pagination>
        </el-col>

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="120px" :rules="editFormRules" ref="editForm">
                <el-form-item label="控制器ID" prop="cId">
                    <el-input v-model="editForm.cId" auto-complete="off" readonly
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="传感器ID" prop="sId">
                    <el-input v-model="editForm.sId" auto-complete="off" readonly
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="传感器描述" prop="des">
                    <el-input v-model="editForm.des" auto-complete="off" readonly
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="是否需要告警" prop="alarmType">                    
                    <el-select v-model="editForm.alarmType" placeholder="请选择是否需要告警" style="width:80%;">
                        <el-option label="是" value="1">是</el-option>
                        <el-option label="否" value="0">否</el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
                <el-button @click.native="editFormVisible = false">取消</el-button>                
            </div>
        </el-dialog>
        
    </section>
</template>

<script>
    import { getSensorDevList, updateSensor } from '../../api/device';

    export default {
        data() {
            return {
                filters: {
                    orderNo: ''
                },
                // 分页对象
                pageObj: {
                    pageNum: 1, // 页码
                    pageSize: 10, // 每页数量
                    total: 0, // 总数
                    curPage: 1, // 当前页
                    orderBy: ""
                },
                listLoading: false,
                sensorList: [],
                statusList: [],
                timer: null,

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    alarmType: [
                        { required: true, message: '请选择是否需要告警', trigger: 'blur' }
                    ],
                },
                editForm: {
                    cId: '',
                    sId: '',
                    des: '',
                    alarmType: ''
                }        
            }
        },
        methods: {
            onSubmit() {
                console.log('submit!');
            },
            getDevices() {
                this.listLoading = true;
                // 对象合并:查询对象+分页对象
                let params = Object.assign({}, this.filters, this.pageObj);

                console.log("查询设备列表");
                                
                getSensorDevList(params).then((res) => {
                    this.listLoading = false;

                    let { msg, code, data } = res;
                    this.sensorList = res.data.list;
                                        
                    this.pageObj.pageNum = data.pageNum;
                    this.pageObj.pageSize = data.pageSize;
                    this.pageObj.total = data.total;         
                });
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);              
            },            
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {cancelButtonClass:'znv-flow-right'}).then(() => {
                            this.editLoading = true;
                            let params = Object.assign({}, this.editForm);                          
                            updateSensor(params).then((res) => {
                                this.editLoading = false;
                                if (res.code !== 0) {
                                    this.$message({
                                        message: res.msg,
                                        type: 'error'
                                    });
                                } else {
                                    this.$message({
                                        message: '提交成功',
                                        type: 'success'
                                    });
                                }
                                this.$refs['editForm'].resetFields();
                                this.editFormVisible = false;
                                this.getDevices();
                            });
                        });
                    }
                });
            },            
            // 每页数量改变
            handleSizeChange(val) {
                this.pageObj.pageSize = val;
                this.getDevices();
            },
            // 页码改变
            handleCurrentChange(val) {
                this.pageObj.pageNum = val;
                this.getDevices();
            },
            // 排序
            handleSortChange(val) {
                let order = val.order == "descending" ? "desc" : "asc"
                this.pageObj.orderBy = `${val.prop} ${order}`
                this.getDevices();
            }            
        },
        mounted() {
            this.getDevices();
        }
    }

</script>