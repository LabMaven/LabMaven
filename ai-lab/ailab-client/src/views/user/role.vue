<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-input v-model="filters.name" placeholder="角色名称" :maxlength="nameMaxLength"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="getRoles">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleAdd">新增</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <!--列表-->
        <el-table :data="roles" highlight-current-row v-loading="listLoading" style="width: 100%;">
            <el-table-column prop="name" label="角色名称" width="200">
            </el-table-column>
            
            <el-table-column prop="description" label="角色描述" min-width="240">
            </el-table-column>

            <el-table-column prop="createTime" label="创建时间" min-width="240" sortable>
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

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="120px" :rules="editFormRules" ref="editForm">
                <el-form-item label="角色名称" prop="name">
                    <el-input v-model="editForm.name" auto-complete="off" :maxlength="nameMaxLength"
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="角色描述">
                    <el-input v-model="editForm.description" auto-complete="off" :maxlength="descMaxLength"
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="菜单权限" prop="menuIds">
                    <div style="height:150px;width:80%;overflow:auto">
                    <el-tree :data="menus" show-checkbox node-key="id"
                        ref="tree" highlight-current :props="defaultProps">
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
            <el-form :model="addForm" label-width="120px" :rules="addFormRules" ref="addForm">
                <el-form-item label="角色名称" prop="name" >
                    <el-input v-model="addForm.name" auto-complete="off" :maxlength="nameMaxLength"
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="角色描述" prop="description">
                    <el-input v-model="addForm.description" auto-complete="off" :maxlength="descMaxLength"
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="菜单权限" prop="menuIds">
                    <div style="height:150px;width:80%;overflow:auto">
                    <el-tree :data="menus" show-checkbox node-key="id"
                        ref="tree" highlight-current :props="defaultProps">
                    </el-tree>
                    </div>
                </el-form-item>                
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>
    </section>
</template>

<script>
    import { getRoleList, addRole, editRole, removeRole, getLoginUserMenu } from '../../api/user';
    export default {
        data() {
            return {
                nameMaxLength: 20,
                descMaxLength: 50,
                filters: {
                    name: ''
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
                roles: [],

                editFormVisible: false,//编辑界面是否显示
                editLoading: false,
                editFormRules: {
                    name: [
                        { required: true, message: '请输入角色名称', trigger: 'blur' },
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
                    ]
                },
                //编辑界面数据
                editForm: {
                    name: '',
                    description: ''
                },

                addFormVisible: false,//新增界面是否显示
                addLoading: false,
                addFormRules: {
                    name: [
                        { required: true, message: '请输入角色名称', trigger: 'blur' },
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
                    ]
                },
                //新增界面数据
                addForm: {
                    name: '',
                    description: ''
                },
                // 菜单树
                menus: [{
                    id:'',
                    label: '',
                    children: []
                }],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                }
            }
        },
        methods: {
            onSubmit() {
                console.log('submit!');
            },
            getRoles() {
                this.listLoading = true;
                // 对象合并:查询对象+分页对象
                let params = Object.assign({}, this.filters, this.pageObj);
                                
                getRoleList(params).then((res) => {
                    this.listLoading = false;

                    let { msg, code, data } = res;
                    this.roles = res.data.list;
                                        
                    this.pageObj.pageNum = data.pageNum;
                    this.pageObj.pageSize = data.pageSize;
                    this.pageObj.total = data.total;            
                });
            },            
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.addForm = {
                    name: '',
                    description: ''
                };
                getLoginUserMenu({}).then((res) => {
                    this.menus = res.data;
                });
            },
            //新增
            addSubmit: function () {
                this.$refs.addForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {cancelButtonClass:'znv-flow-right'}).then(() => {
                            this.addLoading = true;                            
                            let params = Object.assign({}, this.addForm);
                            let { accountId } = JSON.parse(sessionStorage.getItem("user"));

                            params.menuIds = this.$refs.tree.getCheckedKeys().join(",");
                            
                            addRole(params).then((res) => {
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
                                this.getRoles();
                            });
                        });
                    }
                });
            },
            //显示编辑界面
            handleEdit: function (index, row) {
                this.editFormVisible = true;
                this.editForm = Object.assign({}, row);

                getLoginUserMenu({}).then((res) => {
                    this.menus = res.data;
                    if (row.menuIds) {
                        this.$refs.tree.setCheckedKeys(row.menuIds.split(","));
                    } else {
                        this.$refs.tree.setCheckedKeys([]);
                    }                
                });
            },            
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {cancelButtonClass:'znv-flow-right'}).then(() => {
                            this.editLoading = true;
                            let params = Object.assign({}, this.editForm);
                            params.menuIds = this.$refs.tree.getCheckedKeys().join(",");                            
                            editRole(params).then((res) => {
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
                                this.getRoles();
                            });
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
                    
                    removeRole(params).then((res) => {
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
                        this.getRoles();
                    });
                }).catch(() => {

                });
            },
            // 每页数量改变
            handleSizeChange(val) {
                this.pageObj.pageSize = val;
                this.getRoles();
            },
            // 页码改变
            handleCurrentChange(val) {
                this.pageObj.pageNum = val;
                this.getRoles();
            },
            // 排序
            handleSortChange(val) {
                let order = val.order == "descending" ? "desc" : "asc"
                this.pageObj.orderBy = `${val.prop} ${order}`
                this.getRoles();
            }            
        },
        mounted() {
            this.getRoles();
        }
    }

</script>