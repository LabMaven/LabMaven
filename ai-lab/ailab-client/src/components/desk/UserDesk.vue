<template>
    <section>
        <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="12">
                <div class="grid-content">
                    <ul>
                        <li>
                            <p class="stats-content">{{userTotal}}</p>
                            <p class="stats-title">派单人员总数</p>                            
                        </li>
                        <li>
                            <p class="stats-content">{{userOnline}}</p>
                            <p class="stats-title">在线人员数</p>
                        </li>
                        <li>
                            <p class="stats-content">{{taskTotal}}</p>
                            <p class="stats-title">任务总数</p>
                        </li>
                    </ul>
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24">
                <div id="map" class="stats-map"></div>
            </el-col>
        </el-row>
    </section>
</template>
<script>
import { lazyAMapApiLoaderInstance } from 'vue-amap';
import usericon from '@/assets/images/user.gif';
import weihuuser from '@/assets/images/weihuuser.png'
import userface from '@/assets/images/userface.png'
import { getUserTotalCount, getUserOnlineCount, getUserTaskStats } from '../../api/user';
import { getTaskCount } from '../../api/task';

export default {
    data() {
        return {
            userTotal: 0,
            userOnline: 0,
            taskTotal: 0,
            map: {},
            markers: []
        }
    },
    methods: {
        initUserTotalCount() {
            let params = {};
            getUserTotalCount(params).then((res) => {
                let { msg, code, totalCount } = res;
                
                if (code !== 0) {

                } else {
                    this.userTotal = totalCount;
                }
            });
        },
        initUserOnlineCount() {
            let params = {};
            getUserOnlineCount(params).then((res) => {
                let { msg, code, onlineCount } = res;

                if (code !== 0) {

                } else {
                    this.userOnline = onlineCount;
                }
            });
        },
        initTaskCount() {
            let params = {};
            getTaskCount(params).then((res) => {
                let { msg, code, taskCount } = res;
                
                if (code !== 0) {

                } else {
                    this.taskTotal = taskCount;
                }
            });
        },
        initMap() {
            lazyAMapApiLoaderInstance.load().then(() => {
                this.map = new AMap.Map('map', {
                    center: [103.596667, 39.264949],
                    zoom: 4
                });
                
                //初始化人员标记
                this.initUserMarkers();
            });
        },
        initUserMarkers() {
            let _this = this;
            let params = {};
            let users = [];
            let markers = [];
            //这里暂时未调用接口，返回固定值
            getUserTaskStats(params).then((res) => {
                let { msg, code, data } = res;
                users = data;
                for (let i = 0; i < users.length; i++) {
                    let marker = new AMap.Marker({
                        icon: usericon,
                        position: [users[i].longitude, users[i].latitude],
                        extData: users[i],
                        draggable: false,
                        map: _this.map
                    });

                    //注册标记点鼠标浮动事件
                    AMap.event.addListener(marker, 'click', _this.onClick);

                    markers.push(marker);
                }
                _this.markers = markers;
            });
        },
        onClick(e) {
            let _this = this;
            let user = e.target.getExtData();
            let status = user.online==1?'在线':'离线';
            let content='<div class="info-title">调度人员信息</div><div class="info-content"><span>姓名：'+user.trueName+'</span><br/><span>工号：'+user.employeeNo+'</span><br/><span>状态：'+status+'</span><br/><span>任务数：'+user.taskCnt+'</span></div>';
            AMap.service('AMap.AdvancedInfoWindow', function() {
                let infowindow = new AMap.AdvancedInfoWindow({
                    position: e.target.getPosition(),
                    content: content,
                    asOrigin: false,
                    asDestination: false,
                    placeSearch: false,
                    offset: new AMap.Pixel(0, -30),
                    map: _this.map
                });
            });
        }
    },
    mounted() {
        this.initUserTotalCount();
        this.initUserOnlineCount();
        this.initTaskCount();
        this.initMap();
    }
}
</script>

<style scoped>
p{
    margin: 5px;
    text-align: center;
}
ul {
    width: 100%;
    list-style:none;
    height: 60px;
    margin: 0px;
    padding: 0px 20px;
}
ul li {
    padding: 5px 10px;
    float: left;
    width: 25%;
    display: inline-block;
}

.el-row {
margin-bottom: 0px;
&:last-child {
        margin-bottom: 0;
    }
}
.el-col {
    border-radius: 4px;
}
.bg-purple-dark {
    background: #99a9bf;
}
.bg-purple {
    background: #d3dce6;
}
.bg-purple-light {
    background: #e5e9f2;
}
.grid-content {
    border-radius: 4px;
    min-height: 60px;
}
.row-bg {
    background-color: #f2f2f2;
}
.stats-title {
    font-size: 16px;
    text-align: center;
    color: #999999;
}
.stats-content {
    font-size: 26px;
    color: #333333;
}
.stats-map {
    width: 100%;
    height: 600px;
}

</style>