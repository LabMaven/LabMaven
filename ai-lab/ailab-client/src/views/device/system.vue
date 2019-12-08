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
        <el-tabs v-model="activeName">
            <el-tab-pane label="系统详情" name="first">
                <el-col id="room2">
                    <iframe id="my_iframe" :src="iframe_src"/>         
                </el-col>
            </el-tab-pane>
        </el-tabs>
    </div>
</div>
</template>

<script>
import { getRooms, getSensors, getDeviceTree, getSensorDevList, getChartData, addCtlInfo, batchAddCtlInfo} from '../../api/device';
import room1_img from '../../assets/system/r000000101.jpg';
import iframe_src from '../../api/myThing.js';


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
            models: [
                {label: '系统图1', id: 1 }
            ],
            models_bak: [
                {label: '系统图1', id: 1 },
                {label: '系统图2', id: 2 },
                {label: '系统图3', id: 3 },
                {label: '系统图4', id: 4 },
                {label: '系统图5', id: 5 },
                {label: '系统图6', id: 6 },
                {label: '系统图7', id: 7 },
                {label: '系统图8', id: 8 },
                {label: '系统图9', id: 9 },
                {label: '系统图10', id: 10 },
                {label: '系统图11', id: 11 },
                {label: '系统图12', id: 12 }
            ],
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
                pageSize: 200, // 每页数量
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
            imgSrc: '',
            valText: '',
            iframe_src: iframe_src     
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
            //this.filters.rId = "r000000101";
            this.getSensorDevList();      
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
        
        getSensorDevList() {
            // 对象合并:查询对象+分页对象
            let param = Object.assign({}, this.filters, this.pageObj);       

            getSensorDevList(param).then(res => {
                let { msg, code, data } = res;
                this.deviceList = data.list;

                this.imgSrc = '';
                //if (this.deviceList[0].sMapId == 1) {
                    this.imgSrc = room1_img;
                //}

                let app = this;
                let text = '';
                this.deviceList.forEach(function(item) {
                    var myarea= document.createElement("area");
                    myarea.shape = 'rect';
                    myarea.coords = item.coordinate;
                    myarea.href = '#/deviceQry/'+item.rId;
                    myarea.alt = 'this is hot spot';
                    myarea.title = item.des + '<br>当前值:' + item.value;
                        
                    myarea.onclick=function() {
                        let targetName = item.fDes + '_' + item.rDes + '_' + item.des;
                        let newTabName = ++app.tabIndex + targetName;
                        app.chartTabs.push({
                            sIdDiv : 'main_' + item.sId,
                            sId : item.sId,
                            label: targetName,
                            name: newTabName
                        });
                        app.chartTabsValue = newTabName;
                        app.$nextTick(() => {
                        app.showChart(targetName, item.sId);
                        });
                    };
                    $('#imgMap').append(myarea);
                        
                    var classType = "span_normal";
                    if (item.classType.indexOf('_Alarm') != -1) {
                        classType = "span_alarm";
                    }                  
                    text +=  '<span class="' + classType + '">' + item.des + '当前值：' + item.value + '</span><br>';
                });

                this.pageObj.pageNum = data.pageNum;
                this.pageObj.total = data.total;
                this.pageObj.orderBy = data.orderBy;

                $("#data_div").html(text);
            });            

            this.firstTimer = setInterval(() => {
                this.getSensorDevList2();
            }, 2000);
        },

        getSensorDevList2(){
            let param = Object.assign({}, this.filters, this.pageObj);
            
            getSensorDevList(param).then(res => {
                let { msg, code, data } = res;
                this.deviceList = data.list;                   

                let text = '';
                this.deviceList.forEach(function(item) {
                    var classType = "span_normal";
                    if (item.classType.indexOf('_Alarm') != -1) {
                        classType = "span_alarm";
                    }                  
                    text +=  '<span class="' + classType + '">' + item.des + '当前值：' + item.value + '</span><br>';
                });

                this.pageObj.pageNum = data.pageNum;
                this.pageObj.total = data.total;
                this.pageObj.orderBy = data.orderBy;

                $("#data_div").html(text);
            });                
        },

        getSensorList(sType) {
            this.filters.sType = sType;
            this.filters.rId = "";
            this.filters.cId = "";
            this.getSensorDevList();
        },
        init() {
            $("#my_iframe").src = iframe_src;
        }
    },
    mounted() {
        this.init();
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

.data_div {
    width:20%;
    float:left;
    height:100%;
    display: inline-block;
    padding: 0px;
    position: relative;
    border:1 solid;
    backgroud: gray;
}
.img_div {
    float:right;
    width:80%;
    height:550px;
    display: inline-block;
    padding: 0px;
    position: relative;
}

.canvas_cls {
    width:100%;
    height:550px;
    position: relative;
    border:0px none;
    top:0px;
    left:0;
    position:absolute;
    padding: 0px;
    opacity: 1;
}

.span_normal {
    color:black;
}

.span_alarm {
    color:red;
}
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
    width:10%;
    hight:100%;
    float:left;
    border:1 solid;
}

.right {
    width:88%;
    hight:100px;
    margin-left:5px;
    float:right
}

</style>