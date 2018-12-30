<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.userName" placeholder="用户名" style="width:160px;"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="filters.trueName" placeholder="姓名" style="width:160px;"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="filters.phone" placeholder="手机号码" style="width:160px;"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="getUsers">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleAdd">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <template>
            <el-table :data="users" highlight-current-row v-loading="listLoading" style="width: 100%;">
                <el-table-column prop="userName" label="用户名" width="160" sortable>
                </el-table-column>
                <el-table-column prop="trueName" label="姓名" width="160" sortable>
                </el-table-column>
                <el-table-column prop="customerName" label="所属客户" min-width="140">
                </el-table-column>
                <el-table-column prop="phone" label="手机号码" width="140">
                </el-table-column>
                <el-table-column label="操作" width="150">            
                    <template scope="scope">
                        <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-col :span="24" class="toolbar">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageObj.curPage" :page-sizes="[10, 20, 50, 100]" :page-size="pageObj.pageSize" :total="pageObj.total" layout="total, sizes, prev, pager, next, jumper" style="float:right;">
                </el-pagination>
            </el-col>
        </template>

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="90px" :rules="editFormRules" ref="editForm" :inline="true">
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="editForm.userName" auto-complete="off" :maxlength="20" 
                    style="width:190px;" disabled>
                    </el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="trueName">
                    <el-input v-model="editForm.trueName" auto-complete="off" :maxlength="50" style="width:190px;">
                    </el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="editForm.password" type="password" auto-complete="off" style="width:190px;">
                    </el-input>
                </el-form-item>
                <el-form-item label="手机号码" prop="phone">
                    <el-input v-model="editForm.phone" auto-complete="off" :maxlength="20" style="width:190px;">
                    </el-input>
                </el-form-item>
                <el-form-item label="所属客户" prop="customerId">
                    <el-select v-model="editForm.customerId" placeholder="请选择" style="width:190px;">
                        <el-option v-for="item in customers" :key="item.value" :label="item.label"
                           :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="用户类型" prop="userType">
                    <el-select v-model="editForm.userType" placeholder="请选择" style="width:190px;">
                        <el-option v-for="item in userTypes" :key="item.value" :label="item.label"
                           :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="角色" prop="roleIds">
                    <div style="height:120px;width:190px;overflow:auto">
                    <el-tree :data="roleIds" show-checkbox node-key="id" height="150px" width="170px"
                        ref="roleTree" highlight-current :props="defaultProps">
                    </el-tree>
                    </div>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
                <el-button @click.native="editFormVisible = false">取消</el-button>
            </div>
        </el-dialog>        

        <!--新增界面-->
        <el-dialog title="新增" v-model="addFormVisible" :close-on-click-modal="false">   
            <el-form :model="addForm" label-width="90px" :rules="addFormRules" ref="addForm" :inline="true">
                <el-form-item label="用户名" prop="userName">
                    <el-input v-model="addForm.userName" auto-complete="off" :maxlength="20" style="width:190px;">
                    </el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="trueName">
                    <el-input v-model="addForm.trueName" auto-complete="off" :maxlength="50" style="width:190px;">
                    </el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="addForm.password" type="password" auto-complete="off" style="width:190px;">
                    </el-input>
                </el-form-item>
                <el-form-item label="手机号码" prop="phone">
                    <el-input v-model="addForm.phone" auto-complete="off" :maxlength="20" style="width:190px;">
                    </el-input>
                </el-form-item>
                <el-form-item label="所属客户" prop="customerId">
                    <el-select v-model="addForm.customerId" placeholder="请选择" style="width:190px;">
                        <el-option v-for="item in customers" :key="item.value" :label="item.label"
                           :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="用户类型" prop="userType">
                    <el-select v-model="addForm.userType" placeholder="请选择" style="width:190px;">
                        <el-option v-for="item in userTypes" :key="item.value" :label="item.label"
                           :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="角色" prop="roleIds">
                    <div style="height:120px;width:190px;overflow:auto">
                    <el-tree :data="roleIds" show-checkbox node-key="id" height="150px" width="170px"
                        ref="roleTree" highlight-current :props="defaultProps">
                    </el-tree>
                    </div>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
                <el-button @click.native="addFormVisible = false">取消</el-button>
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
    import { getUserList, getUserType, getRoleTree, getCustomers, addUser, editUser, removeUser } from '../../api/user';
    import photo from '@/assets/avatar.png';

    export default {
        data() {
            return {
                filters: {
                    userName: '',
                    trueName: '',
                    phone: '',
                    customerName: ''
                },
                activeName: 'first',           
                // 分页对象
                pageObj: {
                    pageNum: 1, // 页码
                    pageSize: 10, // 每页数量
                    total: 0, // 总数
                    curPage: 1, // 当前页
                    orderBy: ""
                },
                listLoading: false,
                users: [],
                roleIds: [],
                customers: [],
                userTypes: [],
                imageSrc:'',
                imageUrl:'',
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    trueName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
                    phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
                    customerId: [{ required: true, message: '请选择所有客户', trigger: 'blur' }],
                    userType: [{ required: true, message: '请选择用户类型', trigger: 'blur' }],
                    roleIds: [{ required: true, message: '请分配角色权限', trigger: 'blur' }]
                },
                //编辑界面数据
                editForm: {
                    userName: '',
                    trueName: '',
                    password: '',
                    phone: '',
                    email: '',
                    customerId: '',
                    roleIds: ''
                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    userName: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        {
                            validator: function(rule, value, callback) {
                                // 特殊字符
                                var regEn = /[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im,
                                regCn = /[·！#￥（——）：；“”‘、，|《。》？、【】[\]]/im;
                                if (regEn.test(value) || regCn.test(value)) {
                                    callback(new Error('名称不能包含特殊字符！'))
                                } else {
                                    callback();
                                }
                            },
                            trigger: 'blur'
                        }
                    ],
                    trueName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
                    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
                    phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
                    customerId: [{ required: true, message: '请选择所有客户', trigger: 'blur' }],
                    userType: [{ required: true, message: '请选择用户类型', trigger: 'blur' }],
                    roleIds: [{ required: true, message: '请分配角色权限', trigger: 'blur' }]
                },
                //新增界面数据
                addForm: {
                    userName: '',
                    trueName: '',
                    password: '',
                    phone: '',
                    email: '',
                    customerId: '',
                    roleIds: ''
                }
            }
        },
        methods: {

            //获取用户列表
            getUsers: function () {    
                this.listLoading = true;

                // 默认以创建时间倒序排列
                if(this.pageObj.orderBy == '') {
                    this.pageObj.orderBy = `create_time desc`
                }

                // 对象合并:查询对象+分页对象
                let params = Object.assign({}, this.filters, this.pageObj);
                
                getUserList(params).then((res) => {
                    this.listLoading = false;

                    let { msg, code, data } = res;
                    this.users = res.data.list;

                    this.pageObj.pageNum = data.pageNum;
                    this.pageObj.pageSize = data.pageSize;
                    this.pageObj.total = data.total;

                });
            },
                    
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                if(this.$refs['addForm']) {
                    this.$refs['addForm'].resetFields();
                }                
                this.addForm = {};
                this.imageSrc = '';      

                getRoleTree({}).then((res) => {
                    this.roleIds = res.data;
                });

                getUserType({}).then((res) => {
                    this.userTypes = res.data;
                });

                getCustomers({}).then((res) => {
                    this.customers = res.data;
                });                  
            },
            //新增
            addSubmit: function () {
                this.addForm.roleIds = this.$refs.roleTree.getCheckedKeys().join(",");
            
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.addLoading = true;
                        let params = Object.assign({}, this.addForm);

                        addUser(params).then((res) => {
                            this.addLoading = false;                                
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
                                
                            this.$refs['addForm'].resetFields();
                            this.addFormVisible = false;
                            this.getUsers();
                        });
                    }
                });
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                if(this.$refs['editForm']) {
                    this.$refs['editForm'].resetFields();
                }                
                this.editForm = Object.assign({}, row);
                this.editForm.password = '';

                getRoleTree({}).then((res) => {
                    this.roleIds = res.data;
                    if (row.roleIds) {
                        this.$refs.roleTree.setCheckedKeys(row.roleIds.split(","));
                    } else {
                        this.$refs.roleTree.setCheckedKeys([]);
                    }
                });

                getUserType({}).then((res) => {
                    this.userTypes = res.data;
                });

                this.editForm.userType = row.userType + '';

                getCustomers({}).then((res) => {
                    this.customers = res.data;
                });
            }, 
            //编辑
            editSubmit: function () {
                this.editForm.roleIds = this.$refs.roleTree.getCheckedKeys().join(",");                 

                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.editLoading = true;
                        let params = Object.assign({}, this.editForm);
                        params.imageUrl = this.imageUrl;

                        editUser(params).then((res) => {
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
                            this.getUsers();
                        });
                    }
                });
            },
            //删除
            handleDel: function (index, row) {
                this.$confirm('确认删除该记录吗?', '提示', {
                    type: 'warning',
                    cancelButtonClass:'znv-flow-right'
                }).then(() => {
                    this.listLoading = true;
                    let params = { id: row.id };
                    
                    removeUser(params).then((res) => {
                        this.listLoading = false;
                        if (res.code !== 0) {
                            this.$message({
                                message: res.msg,
                                type: 'error'
                            });
                        } else {
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                        }
                        this.getUsers();
                    });
                }).catch(() => {

                });
            },
            // 每页数量改变
            handleSizeChange(val) {
                this.pageObj.pageSize = val;
                this.getUsers();
            },
            // 页码改变
            handleCurrentChange(val) {
                this.pageObj.pageNum = val;
                this.getUsers();
            },
            // 排序
            handleSortChange(val) {
                let order = val.order == "descending" ? "desc" : "asc"
                this.pageObj.orderBy = `${val.prop} ${order}`
                this.getUsers();
            }
        },
        mounted() {
            this.getUsers();
        }
    };

</script>

<style lang="scss" scoped>

</style>