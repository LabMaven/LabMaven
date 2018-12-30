<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true" :model="filters">
                <el-form-item>
                    <el-select v-model="filters.rId" placeholder="请选择房间" style="width:220px;"
                        @change="getSensorList">
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
                    <el-button type="primary" v-on:click="showChart">查询</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <el-col>
            <div id="echartDiv" :style="{width:'700px',height:'280px'}"></div>
        </el-col>
    </section>
</template>

<script>
    import { getRooms, getSensors, getChartData } from '../../api/device';

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
                filters: {
                    rId: '',
                    sId: ''
                },
                rooms: [],
                sensors: [],

                listLoading: false
            }
        },
        methods: {
            //显示图表界面
            showChart: function () {
                var sId = this.filters.sId;
                if(sId == null || sId == '') {
                    alert("请选择传感器");
                    return;
                }
                /*ECharts图表*/
                var timer = setInterval(() => {
                    var mainDiv = document.getElementById("echartDiv");
                    if (mainDiv) {            
                        clearInterval(timer);
                        var myChart = echarts.init(mainDiv);
                        var option = {
                            tooltip : {
                                trigger : 'axis'
                            },
                            xAxis: {
                                type: 'category',
                                data: '',
                                axisLabel: {
                                    interval: 0,
                                    rotate: 60
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
                    }
                }, 1000);
            },
            getRoomList() {
                getRooms().then(res => {
                    this.rooms = res.data;
                });
            },
            getSensorList() {
                this.sensors = [];
                this.filters.sId = '';
                let param = Object.assign({}, this.filters);
                getSensors(param).then(res => {
                    this.sensors = res.data;
                });
            }
        },
        mounted() {
            this.getRoomList();
        }
    }

</script>