<template>
    <el-row class="container">
        <el-col :span="24" class="header">
            <el-col :span="6" class="logo" :class="collapsed?'logo-collapse-width':'logo-width'">
                {{collapsed?'':sysName}}
            </el-col>
            <el-col :span="12">
                <aside :class="collapsed?'menu-collapsed':'menu-expanded'">
                    <el-menu :default-active="$route.path" class="el-menu-demo" mode="horizontal" id="menuA"
                             @open="handleopen" @close="handleclose" @select="handleselect" unique-opened router
                             v-show="!collapsed">
                        <template v-for="(item,index) in getMenu" v-if="!item.hidden">
                            <el-submenu :index="index+''" v-if="!item.leaf">
                                <template slot="title">
                                    <i :class="item.iconCls"></i>{{item.name}}
                                </template>
                                <el-menu-item v-for="child in item.children" :index="child.path" :key="child.path"
                                              v-if="!child.hidden">
                                    {{child.name}}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-if="item.leaf&&item.children.length>0" :index="item.children[0].path">
                                <i :class="item.iconCls"></i> {{item.children[0].name}}
                            </el-menu-item>
                        </template>
                    </el-menu>
                    <!--导航菜单-折叠后-->
                    <ul class="el-menu el-menu-vertical-demo collapsed" v-show="collapsed" ref="menuCollapsed">
                        <li v-for="(item,index) in getMenu" v-if="!item.hidden" class="el-submenu item">
                            <template v-if="!item.leaf">
                                <div class="el-submenu__title" style="padding-left: 20px;"
                                     @mouseover="showMenu(index,true)" @mouseout="showMenu(index,false)"><i
                                        :class="item.iconCls"></i></div>
                                <ul class="el-menu submenu" :class="'submenu-hook-'+index"
                                    @mouseover="showMenu(index,true)" @mouseout="showMenu(index,false)">
                                    <li v-for="child in item.children" v-if="!child.hidden" :key="child.path"
                                        class="el-menu-item" style="padding-left: 40px;"
                                        :class="$route.path==child.path?'is-active':''"
                                        @click="$router.push(child.path)">{{child.name}}
                                    </li>
                                </ul>
                            </template>
                            <template v-else>
                                <ul>
                                    <li class="el-submenu">
                                        <div class="el-submenu__title el-menu-item"
                                             style="padding-left: 20px;height: 56px;line-height: 56px;padding: 0 20px;"
                                             :class="$route.path==item.children[0].path?'is-active':''"
                                             @click="$router.push(item.children[0].path)"><i :class="item.iconCls"></i>
                                        </div>
                                    </li>
                                </ul>
                            </template>
                        </li>
                    </ul>
                </aside>
            </el-col>
            <el-col :span="3">
                <ul>
                    <el-tag type="danger" style="float:right;margin:4px;">告警:{{alarmNum}}</el-tag>
                    <!--<el-tag style="float:right;margin:3px;">正常:5</el-tag>-->
                    <!--<el-tag style="float:right;margin:3px;">总数:10</el-tag>-->
                </ul>
            </el-col>
            <el-col :span="3" class="userinfo">
                <el-dropdown trigger="hover">
                    <span class="el-dropdown-link userinfo-inner"><img :src="sysUserAvatar"/> {{sysUserName}}</span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item>我的消息</el-dropdown-item>
                        <el-dropdown-item @click.native="handleModifyPwd">修改密码</el-dropdown-item>
                        <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-col>
        </el-col>
        <el-col :span="24" class="main">

            <section class="content-container">
                <div class="grid-content bg-purple-light">
                    <el-col :span="24" class="breadcrumb-container">
                        <!--<strong class="title">{{$route.name}}</strong>-->
                        <el-breadcrumb separator="/" class="breadcrumb">
                            <el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
                                {{ item.name }}
                            </el-breadcrumb-item>
                        </el-breadcrumb>
                    </el-col>
                    <el-col :span="24" class="content-wrapper">
                        <transition name="fade" mode="out-in">
                            <router-view></router-view>
                        </transition>
                    </el-col>
                </div>
            </section>
        </el-col>
        <!--修改密码界面-->
        <el-dialog title="修改密码" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="100px" :rules="editFormRules" ref="editForm">
                <el-form-item label="原密码" prop="password">
                    <el-input v-model="editForm.password" auto-complete="off" type="password"
                              :maxlength="maxlength"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                    <el-input v-model="editForm.newPassword" auto-complete="off" type="password"
                              :maxlength="maxlength" @focus="clearTip"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="newPwdConfirm">
                    <el-input v-model="editForm.newPwdConfirm" auto-complete="off" type="password"
                              :maxlength="maxlength" @focus="clearTip"></el-input>
                    <label v-model="editForm.tip" style="color:red">{{tips}}</label>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false">取消</el-button>
                <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
            </div>
        </el-dialog>
    </el-row>
</template>
<script>
    import routes from '../routes';
    import {getMenuTree, modifyPassword} from '../api/user';
    import {mapGetters, mapActions} from 'vuex';
    import photo from '@/assets/photo.png'
    import {qryAlarmList} from '../api/alarm'

    import SockJS from 'sockjs-client';
    import Stomp from "stompjs";

    export default {
        data() {
            return {
                sysName: '晨阳水漆中央监控系统',
                collapsed: false,
                sysUserName: '',
                sysUserAvatar: "",
                form: {},
                name: '',
                region: '',
                date1: '',
                date2: '',
                delivery: false,
                type: [],
                resource: '',
                desc: '',
                tips: '',
                alarmNum: '',
                maxlength: 32,

                editFormVisible: false,//修改密码界面是否显示
                editLoading: false,
                editFormRules: {
                    password: [
                        {required: true, message: '请输入原密码', trigger: 'blur'},
                        {
                            validator: function (rule, value, callback) {
                                if (value.length < 6) {
                                    callback(new Error('密码长度6-32位！'))
                                } else {
                                    callback();
                                }
                            },
                            trigger: 'blur'
                        }
                    ],
                    newPassword: [
                        {required: true, message: '请输入新密码', trigger: 'blur'},
                        {
                            validator: function (rule, value, callback) {
                                var reg1 = /[a-zA-Z]+/im,
                                    reg2 = /[0-9]+/im, reg3 = /[!,.?:;'"]+/im;
                                if (value.length < 6) {
                                    callback(new Error('密码长度6-32位！'));
                                } else if (!(reg1.test(value) && reg2.test(value) && reg3.test(value))) {
                                    callback(new Error('密码必须是字母、数字和英文标点的组合！'));
                                } else {
                                    callback();
                                }
                            },
                            trigger: 'blur'
                        }
                    ],
                    newPwdConfirm: [
                        {required: true, message: '请再次输入新密码', trigger: 'blur'},
                        {
                            validator: function (rule, value, callback) {
                                var reg1 = /[a-zA-Z]+/im,
                                    reg2 = /[0-9]+/im, reg3 = /[!,.?:;'"]+/im;

                                if (value.length < 6) {
                                    callback(new Error('密码长度6-32位！'))
                                } else if (!(reg1.test(value) && reg2.test(value) && reg3.test(value))) {
                                    callback(new Error('密码必须是字母、数字和英文标点的组合！'));
                                } else {
                                    callback();
                                }
                            },
                            trigger: 'blur'
                        }
                    ]
                },
                //修改密码界面数据
                editForm: {
                    password: '',
                    newPassword: '',
                    newPwdConfirm: ''
                },
            }
        },
        mounted: function () {
            // 调取state中登录用户菜单查询方法
            this.$store.dispatch('initLoginUserMenu');
            var user = sessionStorage.getItem('user');
            if (user) {
                user = JSON.parse(user);
                this.sysUserName = user.trueName || '';
                this.sysUserAvatar = user.avatar || photo;
            }
            this.initWebSocket();
            this.qryAlarmNums();
        },
        computed: {
            // 使用对象展开运算符将 getters 混入 computed 对象中
            ...mapGetters([
                'getMenu'
            ]),
        },
        beforeRouteEnter(to, from, next) {
            next();
        },
        methods: {
            onSubmit() {
                console.log('submit!');
            },
            handleopen() {
                //console.log('handleopen');
            },
            handleclose() {
                //console.log('handleclose');
            },
            handleselect: function (a, b) {
            },
            handleModifyPwd: function () {
                this.editFormVisible = true;
                this.editForm = {
                    password: '',
                    newPassword: '',
                    newPwdConfirm: ''
                };
            },
            clearTip: function () {
                this.tips = '';
            },
            //编辑
            editSubmit: function () {
                this.$refs.editForm.validate((valid) => {
                    if (valid) {
                        if (this.editForm.newPassword != this.editForm.newPwdConfirm) {
                            this.tips = '两次密码不一致！';
                            return;
                        }
                        this.editLoading = true;
                        let params = Object.assign({}, this.editForm);

                        modifyPassword(params).then((res) => {
                            this.editLoading = false;
                            if (res.code !== 0) {
                                this.$message({
                                    message: res.msg,
                                    type: 'error'
                                });
                            } else {
                                this.$message({
                                    message: '修改成功',
                                    type: 'success'
                                });
                            }
                            this.$refs['editForm'].resetFields();
                            this.editFormVisible = false;
                        });
                    }
                });
            },
            //退出登录
            logout: function () {
                var _this = this;
                this.$confirm('确认退出吗?', '提示', {
                    //type: 'warning'
                    cancelButtonClass: 'znv-flow-right'
                }).then(() => {
                    sessionStorage.removeItem('user');
                    _this.$router.push('/login');
                }).catch(() => {

                });
            },
            //折叠导航栏
            collapse: function () {
                this.collapsed = !this.collapsed;
                var uiwidth = document.getElementById('menuA');
                if (uiwidth.offsetWidth === 0) {
                    uiwidth.style.width = "120px"
                }
            },
            showMenu(i, status) {
                this.$refs.menuCollapsed.getElementsByClassName('submenu-hook-' + i)[0].style.display = status ? 'block' : 'none';
            },
            //初始化
            initWebSocket() {
                this.connection();
                //模拟客户端向后台推送消息
                let self = this;
                // 断开重连机制,尝试发送消息,捕获异常发生时重连
                this.timer = setInterval(() => {
                    try {
                        self.stompClient.send("/app/receive", {}, "test");
                    } catch (err) {
                        console.log("断线了: " + err);
                        self.connection();
                    }
                }, 5000);
            },
            qryAlarmNums: function () {
                qryAlarmList().then((res) => {
                    this.listLoading = false;

                    let {msg, code, data} = res;
                    console.log(data)
                    this.alarmNum = data.total;
                });
            },
            //建立连接
            connection() {
                // 建立连接对象
                //连接服务端提供的通信接口，连接以后才可以订阅广播消息和个人消息
                let _that = this;
                //后台服务ip和port
                _that.socket = new SockJS('http://localhost:8088/ailab/websocket');
                // 获取STOMP子协议的客户端对象
                _that.stompClient = Stomp.over(_that.socket);
                // 向服务器发起websocket连接
                _that.stompClient.connect('guest', 'guest', (frame) => {
                    // 订阅服务端提供的某个topic
                    _that.stompClient.subscribe('/topic/notify', (msg) => {
                        //msg.body存放的是服务端发送给我们的信息
                        let resData = JSON.parse(msg.body);
                        console.log(resData)
                        this.alarmNum = this.alarmNum +1;
                    });
                }, (err) => {
                    console.log("后台消息推送测试")
                });

            },
            disconnect() {
                if (this.stompClient != null) {
                    this.stompClient.disconnect();
                    console.log("Disconnected");
                }
            }
        },
        //销毁页面之前，断开连接
        beforeDestroy: function () {
            //页面离开时断开连接,清除定时器
            this.disconnect();
            clearInterval(this.timer);
        }
    }
</script>
<style scoped lang="scss">
    @import '~scss_vars';

    .container {
        position: absolute;
        top: 0px;
        bottom: 0px;
        width: 100%;
        .header {
            height: 60px;
            line-height: 60px;
            background: $color-primary;
            color: #fff;
            .userinfo {
                text-align: right;
                padding-right: 35px;
                float: right;
                .userinfo-inner {
                    cursor: pointer;
                    color: #fff;
                    img {
                        width: 40px;
                        height: 40px;
                        border-radius: 20px;
                        margin: 10px 0px 10px 10px;
                        float: right;
                    }
                }
            }
            .logo {
                //width:230px;
                height: 60px;
                font-size: 22px;
                padding-left: 20px;
                padding-right: 20px;
                border-color: rgba(238, 241, 146, 0.3);
                border-right-width: 1px;
                border-right-style: solid;
                img {
                    width: 40px;
                    float: left;
                    margin: 10px 10px 10px 18px;
                }
                .txt {
                    color: #fff;
                }
            }
            .logo-width {
                width: 280px;
            }
            .logo-collapse-width {
                width: 60px
            }
            .tools {
                padding: 0px 23px;
                width: 14px;
                height: 60px;
                line-height: 60px;
                cursor: pointer;
            }
        }
        .el-menu {
            background-color: #20a0ff;
        }
        .main {
            display: flex; // background: #324057;
            position: absolute;
            top: 60px;
            bottom: 0px;
            overflow: hidden;
            aside {
                flex: 0 0 230px;
                width: 230px; // position: absolute;
                // top: 0px;
                // bottom: 0px;
                .el-menu {
                    height: 100%;
                }
                .collapsed {
                    width: 60px;
                    .item {
                        position: relative;
                    }
                    .submenu {
                        position: absolute;
                        top: 0px;
                        left: 60px;
                        z-index: 99999;
                        height: auto;
                        display: none;
                    }
                }
            }
            .menu-collapsed {
                flex: 0 0 60px;
                width: 60px;
            }
            .menu-expanded {
                flex: 0 0 230px;
                width: 230px;
            }
            .content-container {
                // background: #f1f2f7;
                flex: 1; // position: absolute;
                // right: 0px;
                // top: 0px;
                // bottom: 0px;
                // left: 230px;
                overflow-y: scroll;
                padding: 20px;
                .breadcrumb-container {
                    //margin-bottom: 15px;
                    .title {
                        width: 200px;
                        float: left;
                        color: #475669;
                    }
                    .breadcrumb-inner {
                        float: right;
                    }
                }
                .content-wrapper {
                    background-color: #fff;
                    box-sizing: border-box;
                }
            }
        }
    }
</style>