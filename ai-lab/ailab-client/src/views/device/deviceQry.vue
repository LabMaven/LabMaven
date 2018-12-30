<template>
<div style="hight: 100%;">
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
                <el-form-item style="display:none">
                    <el-input v-model="filters.sType" placeholder="传感器类型"></el-input>
                </el-form-item>
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
            </el-form>

            <el-tag type="warning" style="float:right;margin:3px;">离线:2</el-tag>
            <el-tag type="danger" style="float:right;margin:3px;">异常:3</el-tag>            
            <el-tag style="float:right;margin:3px;">正常:5</el-tag>
            <el-tag style="float:right;margin:3px;">总数:10</el-tag>
        </el-col>
        
        <el-col id="room2">
            <div v-for="(item, index) in deviceList" :class="item.classType"  @click="addTab(item)">
                <p style="padding-left:100px;" :id="item.sId">
                   {{item.fDes}}<br/>
                   {{item.rDes}}<br/>
                   当前值：{{item.value}}<br/>
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
        -->
        <el-tab-pane v-for="(item, index) in chartTabs" :key="item.name" :name="item.name" closable
            :label="item.label">
                <div :id="item.sIdDiv" :style="{width:'700px',height:'280px'}"></div>            
        </el-tab-pane>
        </el-tabs>
    </div>
</div>
</template>

<script>
import { getDeviceTree, getSensorChartList, getChartData} from '../../api/device';
import room1 from '@/components/room1.vue';

let echarts = require('echarts');

require('echarts/lib/chart/line');
require('echarts/lib/chart/lines');
require('echarts/lib/component/tooltip');
require('echarts/lib/component/toolbox');
require('echarts/lib/component/legend');
require('echarts/lib/component/markLine');

export default {
    components:{room1},
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
               this.getSensorChartList();
            }            
        },
        showMessage(data) {
            document.getElementById(data.sId).style.display='block';
        },
        hideMessage(data) {
            document.getElementById(data.sId).style.display='none';
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
        
        getSensorChartList() {
            // 对象合并:查询对象+分页对象
            let param = Object.assign({}, this.filters, this.pageObj);
            
            //this.firstTimer = setInterval(() => {
                getSensorChartList(param).then(res => {
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
            this.getSensorChartList();
        }, 
        getDeviceTree(){
            getDeviceTree({"bId" : "b0001"}).then((res) => {
                this.models = res.data;
            });
        },
        // 每页数量改变
        handleSizeChange(val) {
            this.pageObj.pageSize = val;
            this.getSensorChartList();
        },
        // 页码改变
        handleCurrentChange(val) {
            this.pageObj.pageNum = val;
            this.getSensorChartList();
        },
        // 排序
        handleSortChange(val) {
            let order = val.order == "descending" ? "desc" : "asc"
            this.pageObj.orderBy = `${val.prop} ${order}`
            this.getSensorChartList();
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
.pressure_Normal {
    background: url('~@/assets/sensor/pressure_sensor_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.temperature_Normal {
    background: url('~@/assets/sensor/temperature_sensor_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.tempAndHum_Normal {
    background: url('~@/assets/sensor/temperature_humidity_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.ventilator_Normal {
    background: url('~@/assets/sensor/ventilator_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.workState_Normal {
    background: url('~@/assets/sensor/work_state_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.airSpeed_Normal {
    background: url('~@/assets/sensor/air_speed_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.airVolumn_Normal {
    background: url('~@/assets/sensor/air_volumn_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.flygate_Normal {
    background: url('~@/assets/sensor/flygate_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.unknown_Normal {
    background: url('~@/assets/sensor/default_sensor_normal.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.pressure_Alarm {
    background: url('~@/assets/sensor/pressure_sensor_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.temperature_Alarm {
    background: url('~@/assets/sensor/temperature_sensor_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.tempAndHum_Alarm {
    background: url('~@/assets/sensor/temperature_humidity_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.ventilator_Alarm {
    background: url('~@/assets/sensor/ventilator_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.workState_Alarm {
    background: url('~@/assets/sensor/work_state_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.airSpeed_Alarm {
    background: url('~@/assets/sensor/air_speed_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}


.airVolumn_Alarm {
    background: url('~@/assets/sensor/air_volumn_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.flygate_Alarm {
    background: url('~@/assets/sensor/flygate_alarm.png') no-repeat;
    width:33%;
    height: 100px;
    float:left;
}

.unknown_Alarm {
    background: url('~@/assets/sensor/default_sensor_normal.png') no-repeat;
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
    width:20%;
    hight:100%;
    float:left;
    border:1 solid;
}

.right {
    width:78%;
    hight:100%;
    margin-left:5px;
    float:right
}

</style>