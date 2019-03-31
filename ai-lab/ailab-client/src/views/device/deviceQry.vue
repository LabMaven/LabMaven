<template>
<div style="hight: 50%;">
    <div class="left">
        <!--el-input placeholder="输入关键字进行过滤" v-model="filterText" style="padding-top: 10px;">
        </el-input-->
        <el-tree class="filter-tree tree" :data="models" :props="defaultProps" highlight-current  accordion
        :filter-node-method="filterNode" ref="tree" @node-click="handleNodeClick" :expand-on-click-node="false">
        </el-tree>
    </div>

    <div class="right">
      <el-tabs v-model="chartTabsValue" @tab-remove="removeTab">
        <el-tab-pane label="设备列表" name="first">
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-select v-model="filters.rId" placeholder="请选择房间" style="width:220px;"
                        @change="getSensors">
                        <el-option v-for="item in rooms" :key="item.rId" :label="item.des"
                           :value="item.rId">
                        </el-option>
                    </el-select>
                    <el-select v-model="filters.sId" placeholder="请选择传感器" style="width:220px;">
                        <el-option v-for="item in sensors" :key="item.sId" :label="item.des"
                           :value="item.sId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" v-on:click="getSensorCollect">查询</el-button>
                </el-form-item>
                <!--
                <el-form-item>
                    <el-button v-on:click="getSensorList(1)">压差</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(2)">温湿度</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(4)">通风柜门高</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(5)">工作状态</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(6)">面风速</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(7)">余风量</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(8)">蝶阀风量</el-button>
                </el-form-item>
                -->
            </el-form>

            <!--
            <el-tag type="warning" style="float:right;margin:3px;">离线:2</el-tag>
            <el-tag type="danger" style="float:right;margin:3px;">异常:3</el-tag>            
            <el-tag style="float:right;margin:3px;">正常:5</el-tag>
            <el-tag style="float:right;margin:3px;">总数:10</el-tag>
            -->
        </el-col>

        <el-col>
        <div id="allExhaust" style="display:none">
            一键强排:
            <el-button type="primary" size="small" v-on:click="allExhaust(0)">全部打开</el-button>
            <el-button type="primary" size="small" v-on:click="allExhaust(1)">全部关闭</el-button>
            <br/>
        </div>
        </el-col>
        
        <el-col id="room2">
            <div v-for="(item, index) in deviceList" class="sensor_div">
                <div :class="item.classType" @click="addTab(item)"></div>
                <div class="sensor_info">
                    <span :id="'p_' + item.sId">
                        {{item.fDes}}<br/>
                        {{item.rDes}}<br/>
                        {{item.des}}<br/>
                        <!--压差和余风量为模拟量-->
                        <el-button size="small" v-if="(item.sType == 1 || item.sType == 10) 
                            && item.configurable == 0"
                            v-on:click="openEditForm(item)">修改</el-button>

                        <!--人员工作状态 为开关量 -->
                        <el-button type="small" v-if="item.sType == 7 && item.configurable == 0"
                            v-on:click="chgSwitchInfo(item, 0)">打开</el-button>
                        <el-button type="small" v-if="item.sType == 7 && item.configurable == 0"
                            v-on:click="chgSwitchInfo(item, 1)">关闭</el-button>

                        <!-- 通风柜 -->
                        <span v-if="item.sType == 5 && item.configurable == 0">一键强排:</span>
                        <el-button type="mini" v-if="item.sType == 5 && item.configurable == 0"
                            v-on:click="exhaust(item, 0)">打开</el-button>
                        <el-button style="margin-left:0px;" type="mini" 
                            v-if="item.sType == 5 && item.configurable == 0"
                            v-on:click="exhaust(item, 1)">关闭</el-button>
                    </span>
                </div>          
            </div>
        </el-col>

        <!--编辑界面-->
        <el-dialog title="编辑" v-model="editFormVisible" :close-on-click-modal="false">
            <el-form :model="editForm" label-width="120px" :rules="editFormRules" ref="editForm">
                <el-form-item label="下发值类型" prop="vType" v-show="false">
                    <el-input v-model="editForm.vType" auto-complete="off" value="1"
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="控制器ID" prop="cId">
                    <el-input v-model="editForm.cId" auto-complete="off" readonly
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="传感器ID" prop="sId">
                    <el-input v-model="editForm.sId" auto-complete="off" readonly
                        style="width:80%;"></el-input>
                </el-form-item>
                <el-form-item label="工程值" prop="sValue">                    
                    <el-input v-model="editForm.sValue" auto-complete="off"
                        style="width:80%;"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click.native="editFormSubmit" :loading="editLoading">提交</el-button>
                <el-button @click.native="editFormVisible = false">取消</el-button>                
            </div>
        </el-dialog>

        <el-col :span="24" class="toolbar">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageObj.curPage" :page-sizes="[15, 30, 60]" :page-size="pageObj.pageSize" :total="pageObj.total" layout="total, sizes, prev, pager, next, jumper" style="float:right;">
            </el-pagination>
        </el-col>
        </el-tab-pane>
        <el-tab-pane v-for="(item, index) in chartTabs" :key="item.name" :name="item.name" closable
            :label="item.label">
            <!--列表-->
            <el-table :data="subSensorList" highlight-current-row v-loading="listLoading" style="width: 70%;">
                <el-table-column prop="cId" label="控制器ID" min-width="80">
                </el-table-column>
            
                <el-table-column prop="sId" label="传感器ID" min-width="80">
                </el-table-column>

                <el-table-column prop="des" label="传感器描述" min-width="60">
                </el-table-column>

                <el-table-column prop="value" label="当前值" min-width="60">
                </el-table-column>
            </el-table>
            <div v-for="(item, index) in subSensorList" :id="'main_'+item.sId"
                :style="{width:'700px',height:'280px'}"></div>            
        </el-tab-pane>
        </el-tabs>
    </div>
</div>
</template>

<script>
import { getRooms, getSensors, getDeviceTree, getSensorCollect, getChartData, addCtlInfo, batchAddCtlInfo} from '../../api/device';

let echarts = require('echarts');

require('echarts/lib/chart/line');
require('echarts/lib/chart/lines');
require('echarts/lib/component/tooltip');
require('echarts/lib/component/toolbox');
require('echarts/lib/component/legend');
require('echarts/lib/component/markLine');

export default {
    data() {
        return {
            disabled: true,
            filterText: '',
            defaultProps: {
              children: 'children',
              label: 'label'
            },
            activeName: 'first',
            items:[
                {text:"第一组"},
                {text:"第二组"},
                {text:"第三组"},
                {text:"第四组"},
                {text:"第五组"}
            ],
            models: '',
            message: '',

            maxlength: 20,
            filters: {
                rId: '',
                sId: ''
            },
            rooms: [],
            sensors: [],
            subSensorList: [],
            subChartList: [],
            // 分页对象
            pageObj: {
                pageNum: 1, // 页码
                pageSize: 15, // 每页数量
                total: 0, // 总数
                curPage: 1, // 当前页
                orderBy: ""
            },
            
            deviceList: [],
            listLoading: false,
            chartVisible : false,
            chartTabsValue: 'first',
            chartTabs: [],
            tabIndex: 0,
            firstTimer: null,
            echartsTimer: null,

            editFormVisible: false,//编辑界面是否显示
            editLoading: false,
            editFormRules: {
                sValue: [
                    { required: true, message: '请输入工程值', trigger: 'blur' }
                ],
            },
            editForm: {
                vType: 1,
                cId: '',
                sId: '',
                sValue: ''
            },            
        }
    },
    watch: {
        filterText(val) {
            this.$refs.tree.filter(val);
        },
        chartTabsValue(val) {
            if (val == 'first') {
                clearInterval(this.echartsTimer);
            }
        }
    },
    methods: {
        enter: function(){
            this.message = '当前温度：100';
        },
        leave: function(){
            this.message = '';
        },
        filterNode(value, data) {
            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        },
        handleNodeClick(data) {
            this.chartVisible = false;
            if (data.level == 3) {
                this.filters.rId = data.id;           
            } else if(data.level == 4) {
                this.filters.cId = data.id;
            }

            if(data.level == 3 || data.level == 4) {
               this.filters.sType = "";
               this.getSensorCollect();
            }            
        },
        getRooms() {
            getRooms().then(res => {
                this.rooms = res.data;
            });
        },
        getSensors() {
            this.sensors = [];
            this.filters.sId = '';
            let param = Object.assign({}, this.filters);
            getSensors(param).then(res => {
                this.sensors = res.data;
            });
        },
        showMessage(data) {
            document.getElementById('p_'+data.sId).style.display='block';
        },
        hideMessage(data) {
            document.getElementById('p_'+data.sId).style.display='none';
        },

        //显示图表界面
        showChart: function (targetName, sId) {
            this.chartVisible = true;
            this.activeName = targetName;

            let param = {"sPid" : sId};
            getSensors(param).then(res => {
                let { msg, code, data } = res;
                this.subSensorList = data;
            });

            let params = Object.assign({"sPid" : sId}, {"pageSize": 30});
            getChartData(params).then(res => {
                this.subChartList = res.data;
                this.subChartList.forEach(function(item){
                console.log('sid'+item.sid);
                    /*ECharts图表*/
                    var timer = setInterval(() => {
                        var sIdDiv = 'main_' + item.sId;
                        var mainDiv = document.getElementById(sIdDiv);
                        if (mainDiv) {
                            var option = {
                                title : {
                                    text : item.des + '采集数据趋势图'
                                },
                                tooltip : {
                                    trigger : 'axis'
                                },
                                xAxis: {
                                    type: 'category',
                                    data: '',
                                    axisLabel: {
                                        interval:0,
                                        rotate:60
                                    }
                                },
                                grid: {
                                    left: '10%',
                                    bottom:'35%'
                                },
                                yAxis: {
                                    type: 'value'
                                },
                                series: [{           
                                    type: 'line',
                                    data: ''
                                }]
                            };
                                                 
                            var myChart = echarts.init(mainDiv);
                            option.xAxis.data = item.xData;
                            option.series[0].data = item.yData;

                            myChart.setOption(option); 
                            clearInterval(timer);
                        }

                    }, 1000);
                });

            });
            
        },

        addTab(target) {
            let targetName = target.fDes + '_' + target.rDes + '_' + target.des;
            let newTabName = ++this.tabIndex + targetName;
            this.chartTabs.push({
                sIdDiv : 'main_' + target.sId,
                sId : target.sId,
                label: targetName,
                name: newTabName
            });
            this.chartTabsValue = newTabName;
            this.$nextTick(() => {
               this.showChart(targetName, target.sId);
            });          
        },
        removeTab(targetName) {
            this.chartVisible = false;
            let tabs = this.chartTabs;
            let activeName = this.chartTabsValue;
            if (activeName === targetName) {
                tabs.forEach((tab, index) => {
                    if (tab.name === targetName) {
                        let nextTab = tabs[index + 1] || tabs[index - 1];
                        if (nextTab) {
                            activeName = nextTab.name;
                        }
                    }
                });
            }
        
            if (tabs.length == 1) {
                activeName = 'first';
            }
            this.chartTabsValue = activeName;
            this.chartTabs = tabs.filter(tab => tab.name !== targetName);
        },
        
        getSensorCollect() {
            // 对象合并:查询对象+分页对象
            let param = Object.assign({}, this.filters, this.pageObj);
            
            //this.firstTimer = setInterval(() => {

            document.getElementById("allExhaust").style.display='none';

                getSensorCollect(param).then(res => {
                    let { msg, code, data } = res;
                    this.deviceList = data.list;

                    this.deviceList.forEach(function(item) {
                        if (item.sType == 5 && item.configurable == 0) {
                            document.getElementById('allExhaust').style.display='block';
                        }
                    });

                    this.pageObj.pageNum = data.pageNum;
                    this.pageObj.total = data.total;
                    this.pageObj.orderBy = data.orderBy;
                });
            //} , 30000);
        },
        getSensorList(sType) {
            this.filters.sType = sType;
            this.filters.rId = "";
            this.filters.cId = "";
            this.getSensorCollect();
        }, 
        getDeviceTree(){
            getDeviceTree({"bId" : "b0001"}).then((res) => {
                this.models = res.data;
            });
        },

        exhaust (data, sStatus) {
            let params = {'cId':data.cId, 'sId':data.sId, vType: 2, 'sStatus':sStatus };
            let msg = sStatus == 0 ? '打开' : '关闭';
            this.$confirm('确认'+msg+'一键强排吗？', '提示',
              {cancelButtonClass:'znv-flow-right'}).then(() => {        
                addCtlInfo(params).then((res) => {
                    if (res.code !== 0) {
                        this.$message({
                            message: res.msg == undefined || res.msg== null || res.msg == '' ?
                                msg + '一键强排失败' : res.msg,
                            type: 'error'
                        });
                    } else {
                        this.$message({
                            message: msg + '一键强排成功',
                            type: 'success'
                        });
                    }
                });
            });
        },

        allExhaust (sStatus) {
            if (this.filters.rId == undefined || this.filters.rId == '') {
               this.$message({message: '请选择房间', type : 'error'});
               return;
            }
            let params = {'rId':this.filters.rId, vType: 2, 'sStatus':sStatus };
            let msg = sStatus == 0 ? '全部打开' : '全部关闭';
            this.$confirm('确认'+msg+'一键强排吗？', '提示',
              {cancelButtonClass:'znv-flow-right'}).then(() => {        
                batchAddCtlInfo(params).then((res) => {
                    if (res.code !== 0) {
                        this.$message({
                            message: res.msg == undefined || res.msg== null || res.msg == '' ?
                                msg + '一键强排失败' : res.msg,
                            type: 'error'
                        });
                    } else {
                        this.$message({
                            message: msg + '一键强排成功，总共' + res.totalCount 
                              + '个，成功'+ res.addCount + '个。',
                            type: 'success'
                        });
                    }
                });
            });
        },

        chgSwitchInfo (data, switchFlag) {
            let params = {'cId':data.cId, 'sId':data.sId, vType: 0, 'switchFlag':switchFlag };
            let msg = switchFlag == 0 ? '打开' : '关闭';
            this.$confirm('确认'+msg+'开关吗？', '提示', {cancelButtonClass:'znv-flow-right'}).then(() => {        
                addCtlInfo(params).then((res) => {
                    if (res.code !== 0) {
                        this.$message({
                            message: res.msg == undefined || res.msg== null || res.msg == '' ?
                                msg + '开关失败' : res.msg,
                            type: 'error'
                        });
                    } else {
                        this.$message({
                            message: msg + '开关成功',
                            type: 'success'
                        });
                    }
                });
            });
        },
        openEditForm: function (data) {            
            this.editFormVisible = true;
            this.editForm = {'cId':data.cId, 'sId':data.sId, vType: 1 }; 
        },
        editFormSubmit: function () {
            this.$refs.editForm.validate((valid) => {
                if (valid) {
                    this.editLoading = true;
                    let params = Object.assign({}, this.editForm); 
                    console.log(params);                         
                    addCtlInfo(params).then((res) => {
                        this.editLoading = false;
                        if (res.code !== 0) {
                            this.$message({
                                message: res.msg == undefined || res.msg== null || res.msg == '' ?
                                    '修改工程值失败' : res.msg,
                                type: 'error'
                            });
                        } else {
                            this.$message({
                                message: '修改工程值成功',
                                type: 'success'
                            });
                        }
                        this.$refs['editForm'].resetFields();
                        this.editFormVisible = false;
                        this.getDevices();
                    });
                }
            });
        },

        // 每页数量改变
        handleSizeChange(val) {
            this.pageObj.pageSize = val;
            this.getSensorCollect();
        },
        // 页码改变
        handleCurrentChange(val) {
            this.pageObj.pageNum = val;
            this.getSensorCollect();
        },
        // 排序
        handleSortChange(val) {
            let order = val.order == "descending" ? "desc" : "asc"
            this.pageObj.orderBy = `${val.prop} ${order}`
            this.getSensorCollect();
        },
        showDetail() {
            
        }  
    },
    mounted() {
        this.getDeviceTree();
        this.getRooms();
    },
    beforeDestroy() {
        if(this.firstTimer) { //如果定时器还在运行 或者直接关闭，不用判断
            clearInterval(this.firstTimer); //关闭
        }

        if(this.chartTabsValue) {
            clearInterval(this.echartsTimer);
        }
    }
}  
</script>


<style>
.sensor_div {
    width:33%;
    height:100px;
    float:left;
}
.sensor_info {
    width:50%;
    height:100px;
    float:right;
}
.pressure_Normal {
    background: url('~@/assets/sensor/pressure_sensor_normal.png') no-repeat;
    width:50%;
    height:100px;
    float:left;
}

.temperature_Normal {
    background: url('~@/assets/sensor/temperature_sensor_normal.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.tempAndHum_Normal {
    background: url('~@/assets/sensor/temperature_humidity_normal.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.ventilator_Normal {
    background: url('~@/assets/sensor/ventilator_normal.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.workState_Normal {
    background: url('~@/assets/sensor/work_state_normal.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.airSpeed_Normal {
    background: url('~@/assets/sensor/air_speed_normal.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.airVolumn_Normal {
    background: url('~@/assets/sensor/air_volumn_normal.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.flygate_Normal {
    background: url('~@/assets/sensor/flygate_normal.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.unknown_Normal {
    background: url('~@/assets/sensor/default_sensor_normal.png') no-repeat;
    width:16%;
    height: 100px;
    float:left;
}

.pressure_Alarm {
    background: url('~@/assets/sensor/pressure_sensor_alarm.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.temperature_Alarm {
    background: url('~@/assets/sensor/temperature_sensor_alarm.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.tempAndHum_Alarm {
    background: url('~@/assets/sensor/temperature_humidity_alarm.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.ventilator_Alarm {
    background: url('~@/assets/sensor/ventilator_alarm.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.workState_Alarm {
    background: url('~@/assets/sensor/work_state_alarm.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.airSpeed_Alarm {
    background: url('~@/assets/sensor/air_speed_alarm.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}


.airVolumn_Alarm {
    background: url('~@/assets/sensor/air_volumn_alarm.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.flygate_Alarm {
    background: url('~@/assets/sensor/flygate_alarm.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.unknown_Alarm {
    background: url('~@/assets/sensor/default_sensor_normal.png') no-repeat;
    width:50%;
    height: 100px;
    float:left;
}

.subTable {
border:0;
margin:10px 0 10px 0;
cursor:pointer
}

.subTable td, td:hover{
opacity: 1;
}

.subTable td{
height:10px;
background-color: red;
width:25px;
}

.block {
    text-align: right;
    padding: 20px 0;
}

.tree {
    height:625px;
    overflow: auto;
}
.left {
    width:20%;
    hight:100%;
    float:left;
    border:1 solid;
}

.right {
    width:78%;
    hight:100px;
    margin-left:5px;
    float:right
}

</style>