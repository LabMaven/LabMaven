/*参考Element官网 */
<template>
    <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2" label-position="left" label-width="0px" class="demo-ruleForm login-container">
        <h3 class="title">系统登录</h3>
        <el-form-item prop="account">
            <el-input type="text" v-model="ruleForm2.account" auto-complete="off" placeholder="账号"></el-input>
        </el-form-item>
        <el-form-item prop="checkPass">
            <el-input type="password" v-model="ruleForm2.checkPass" auto-complete="off" placeholder="密码"></el-input>
        </el-form-item>
        <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
        <el-form-item style="width:100%;">
            <el-button type="primary" style="width:100%;" @click.native.prevent="handleSubmit2" :loading="logining">登录</el-button>
            <!--<el-button @click.native.prevent="handleReset2">重置</el-button>-->
        </el-form-item>
    </el-form>
</template>
<script>
import { requestLogin } from '../api/user';
import md5 from 'js-md5';
//import NProgress from 'nprogress'
export default {
    data() {
        return {
            logining: false,
            ruleForm2: {
                account: '',
                checkPass: ''
            },
            rules2: {
                account: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                    //{ validator: validaePass }
                ],
                checkPass: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    //{ validator: validaePass2 }
                ]
            },
            checked: true
        };
    },
    mounted: function() {
        this.$nextTick(function() {
            if (localStorage.getItem('user')) {
                let { userName = "", password = "", remember = false } = JSON.parse(localStorage.getItem('user'));
                this.ruleForm2.account = userName;
                this.ruleForm2.checkPass = password;
                this.checked = remember;
            }
        })
    },
    methods: {
        handleReset2() {
            this.$refs.ruleForm2.resetFields();
        },
        handleSubmit2(ev) {
            var _this = this;
            this.$refs.ruleForm2.validate((valid) => {
                if (valid) {
                    this.logining = true;
                    //NProgress.start();
                    // var loginParams = { username: this.ruleForm2.account, password: this.ruleForm2.checkPass };
                    var loginParams = { username: this.ruleForm2.account, password: md5(this.ruleForm2.checkPass) };

                    requestLogin(loginParams).then(data => {

                        this.logining = false;
                        //NProgress.done();
                        let { msg, code, user, token } = data;
                        if (code !== 0) {
                            this.$message({
                                message: msg,
                                type: 'error'
                            });
                        } else {
                            sessionStorage.setItem('user', JSON.stringify(user));
                            localStorage.setItem('token', token);
                            // 是否记住密码
                            let localUserInfo = {
                                userName: this.checked ? this.ruleForm2.account : "",
                                password: this.checked ? this.ruleForm2.checkPass : "",
                                remember: this.checked ? true : false
                            }
                            localStorage.setItem('user', JSON.stringify(localUserInfo));
                            
                            
                            this.$router.push({ path: '/desk' });
                        }
                    });
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        }
    }
}
</script>
<style lang="scss" scoped>
.login-container {
    /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
        margin: 0px auto 40px auto;
        text-align: center;
        color: #505458;
    }
    .remember {
        margin: 0px 0px 35px 0px;
    }
}
</style>