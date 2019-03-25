<template>
<div style="hight: 100%;">
    <div class="left">
        <el-tree class="filter-tree tree" :data="models" :props="defaultProps" highlight-current  accordion
        :filter-node-method="filterNode" ref="tree" @node-click="handleNodeClick" :expand-on-click-node="false">
        </el-tree>
    </div>

    <div class="right">
      <el-tabs v-model="chartTabsValue" @tab-remove="removeTab">
        <el-tab-pane label="设备列表" name="first">
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item style="display:none">
                    <el-input v-model="filters.sType" placeholder="传感器类型"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(39)">排风变频器</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(40)">新风变频器</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(11)">可编程控制器</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(12)">管道静压</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(13)">流量开关</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(14)">管道温湿度</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(15)">气流开关</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(16)">防冻开关</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(17)">冷热水调节阀</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-on:click="getSensorList(18)">电动风阀执行器</el-button>
                </el-form-item>
            </el-form>
        </el-col>
        
        <el-col id="room2">
            <div v-for="(item, index) in deviceList" :class="item.classType"  @click="addTab(item)">
                <p style="padding-left:100px;">
                   {{item.fDes}}<br/>
                   {{item.rDes}}<br/>
                   {{item.des}}
                </p>               
            </div>
        </el-col>

        <el-col :span="24" class="toolbar">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageObj.curPage" :page-sizes="[10, 20, 50, 100]" :page-size="pageObj.pageSize" :total="pageObj.total" layout="total, sizes, prev, pager, next, jumper" style="float:right;">
            </el-pagination>
        </el-col>
        </el-tab-pane>
        <!--
        <el-tab-pane label="监控图表" v-model="secondTabVisible" name="second" >
            <div style="width:'420px',height:'280px'">
                <div id="main" ref="mainBox" :style="{width:'420px',height:'280px'}"></div>
            </div>
        </el-tab-pane>
        <el-tab-pane v-for="(item, index) in chartTabs" :key="item.name" :name="item.name" closable
            :label="item.label">
                <div :id="item.sIdDiv" :style="{width:'700px',height:'280px'}"></div>            
        </el-tab-pane>
        -->
        <el-tab-pane v-for="(item, index) in chartTabs" :key="item.name" :name="item.name" closable
            :label="item.label">
            <p v-if="item.sType==39" style="padding-left:10px" :id="item.sId">
                   {{item.des}}<br/>
                   排风管道静压：{{item.value}}<br/>
                   风机-运行/停止：运行<br/>
                   风机-手动/自动：自动<br/>
                   风机状态-正常/报警：正常<br/>
                   防火阀连锁：<br/>
                   <!--
                   <el-button  size="small" @click="handleExhaust(item)">一键强排</el-button>
                   -->
                </p>
                <p v-else-if="item.sType==40" style="padding-left:10px;" :id="item.sId">
                   {{item.des}}<br/>
                   新风管道静压：{{item.value}}<br/>
                   风机-运行/停止：运行<br/>
                   风机-手动/自动：自动<br/>
                   风机状态-正常/报警：正常<br/>
                   防火阀连锁：<br/>
                   新风管道温度：{{item.value}}<br/>
                   新风管道湿度：{{item.value}}<br/>
                   防冻开关状态：开<br/>
                   气流开关状态：开<br/>
                   <!--
                   <el-button  size="small" @click="handleExhaust(item)">一键强排</el-button>
                   -->
                </p>
                <p v-else="item.sType!=39 && item.sType!=40" style="padding-left:10px;" :id="item.sId">
                   {{item.des}}<br/>
                   当前值：{{item.value}}<br/>
                   <!--
                   <el-button  size="small" @click="handleExhaust(item)">一键强排</el-button>
                   -->
                </p>
            <div :id="item.sIdDiv" :style="{width:'700px',height:'280px'}"></div>           
        </el-tab-pane>
        </el-tabs>
    </div>
</div>
</template>

<script>
import { getDeviceTree, getSensorCollect, getChartData} from '../../api/device';

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
                parentId: '',
                parentName: '',
                des: '',
                bId : '',
                fId : '',
                rId : '',
                cId : '',
                sType: ''
            },
            // 分页对象
            pageObj: {
                pageNum: 1, // 页码
                pageSize: 10, // 每页数量
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
            echartsTimer: null
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
        showMessage(data) {
                document.getElementById(data.sId).style.display='block';
             
        },
        hideMessage(data) {
                document.getElementById(data.sId).style.display='none';
               
        },
        handleExhaust(data) {
            alert("一键强排:" + data.des);
        },

        //显示图表界面
        showChart: function (targetName, sId) {
            this.chartVisible = true;
            this.activeName = targetName;
            /*ECharts图表*/
            var timer = setInterval(() => {
                    var sIdDiv = 'main_' + sId;
                    var mainDiv = document.getElementById(sIdDiv);
                    if (mainDiv) {            
                        clearInterval(timer);
                        var myChart = echarts.init(mainDiv);
                        var option = {
                                title : {
                                    text : '传感器采集数据趋势图'
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
                        
                        var xData = new Array();
                        var yData = new Array();

                        let param = Object.assign({"sId" : sId}, {"pageSize": 30});
                        getChartData(param).then(res => {

                            option.xAxis.data = res.xData;
                            option.series[0].data = res.yData;

                            myChart.setOption(option); 
                        });
                       
                        this.echartsTimer = setInterval(() => {
                            let tabs = this.chartTabs;
                            let activeName = this.chartTabsValue;
                            //console.log("activeName:" + activeName);
                            tabs.forEach((tab, index) => {
                                if (tab.name == activeName) {
                                    let param = Object.assign({"sId" : tab.sId}, {"pageSize": 30});
                                    getChartData(param).then(res => {
                                        option.xAxis.data = res.xData;
                                        option.series[0].data = res.yData;

                                        myChart.setOption(option);                       
                                    });
                                }
                            });    
                        }, 30000);
                    }
            } , 1000);
        },

        addTab(target) {
            let targetName = target.fDes + '_' + target.rDes + '_' + target.des;
            let newTabName = ++this.tabIndex + targetName;
            this.chartTabs.push({
                sIdDiv : 'main_' + target.sId,
                sId : target.sId,
                sType: target.sType,
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
                getSensorCollect(param).then(res => {
                    let { msg, code, data } = res;
                    this.deviceList = data.list;

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
            getDeviceTree({"bId" : "b0002"}).then((res) => {
                this.models = res.data;
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
.programmable_controller_Normal {
    background: url('~@/assets/fanCoiler/programmable_controller_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.pipe_pressure_Normal {
    background: url('~@/assets/fanCoiler/pipe_pressure_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.flow_switch_Normal {
    background: url('~@/assets/fanCoiler/flow_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.pipe_tempAndHum_Normal {
    background: url('~@/assets/fanCoiler/pipe_temp_and_hum_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.airflow_switch_Normal {
    background: url('~@/assets/fanCoiler/airflow_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.freezeprotect_switch_Normal {
    background: url('~@/assets/fanCoiler/freezeprotect_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.regulating_valve_Normal {
    background: url('~@/assets/fanCoiler/regulating_valve_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.airvalve_actuator_Normal {
    background: url('~@/assets/fanCoiler/airvalve_actuator_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.frequency_converter_Normal {
    background: url('~@/assets/fanCoiler/frequency_converter_normal.png') left center no-repeat;
    width:33%;
    height: 100px;
    float:left;
    padding: 20px;
}

.programmable_controller_Alarm {
    background: url('~@/assets/fanCoiler/programmable_controller_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.pipe_pressure_Alarm {
    background: url('~@/assets/fanCoiler/pipe_pressure_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.flow_switch_Alarm {
    background: url('~@/assets/fanCoiler/flow_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.pipe_tempAndHum_Alarm {
    background: url('~@/assets/fanCoiler/pipe_temp_and_hum_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.airflow_switch_Alarm {
    background: url('~@/assets/fanCoiler/airflow_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}


.freezeprotect_switch_Alarm {
    background: url('~@/assets/fanCoiler/freezeprotect_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.regulating_valve_Alarm {
    background: url('~@/assets/fanCoiler/regulating_valve_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.airvalve_actuator_Alarm {
    background: url('~@/assets/fanCoiler/airvalve_actuator_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.frequency_converter_Alarm {
    background: url('~@/assets/fanCoiler/frequency_converter_alarm.png') left center no-repeat;
    width:33%;
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
    width:25%;
    hight:100%;
    float:left;
    border:1 solid;
}

.right {
    width:74%;
    hight:100%;
    margin-left:5px;
    float:right
}

</style>