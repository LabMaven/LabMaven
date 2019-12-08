<template>
    <section>
        <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="12">
		        <div>
			        <h2>普通设备饼状图(总数：<span id="total"></span>)</h2>
			        <div id="main" style="width: 100%;height: 300px;"></div>
		        </div>
            </el-col>
            <el-col :span="12">
		        <div>
			        <h2>风机盘管设备饼状图(总数：<span id="fanTotal"></span>)</h2>
			        <div id="main2" style="width: 100%;height: 300px;"></div>
		        </div>
            </el-col>
        </el-row>
    
    </section>
</template>

<script>
import { countSensor } from '../../api/device';

let echarts = require('echarts');

// 引入饼图组件
require('echarts/lib/chart/pie');
// 引入提示框和图例组件
require('echarts/lib/component/title');
require('echarts/lib/component/tooltip');
require('echarts/lib/component/legend');
require("echarts/lib/component/legendScroll");

export default {
		name: 'HelloWorld',
		data() {
			return {
			}
		},
		mounted() {
			this.initData();			
		},
		methods: {
		    initData() {
                let params = {};
                countSensor(params).then((res) => {
                    let { msg, code, total, abnormalCount, fanTotal, fanAbnormalCount } = res;
                
                    if (code !== 0) {

                    } else {
                        document.getElementById('total').innerHTML = total;
                        document.getElementById('fanTotal').innerHTML = fanTotal;

                        // 绘制图表
                        this.echartPie1(total - abnormalCount, abnormalCount);
			            this.echartPie2(fanTotal - fanAbnormalCount, fanAbnormalCount);
                    }
                });
		    },
			echartPie1(normalCount, abnormalCount) { // 饼状图的相关配置与事件 
				
				// 1. 默认选中了某一个
				// 2. 鼠标离开记录上次选中的作为当前选中的
				
				
				
				var echarts = require('echarts');
 
				// 基于准备好的dom，初始化echarts实例
				var myChart = echarts.init(document.getElementById('main'));
				// 不能在单个容器上初始化多个 ECharts 实例。
				//实例容器，一般是一个具有高宽的div元素。
 
				var option = {
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b}: {c} ({d}%)"
					},
					color: [ '#FF0000', '#298A08' ],
					legend: {
						orient: 'vertical',
						x: 'left',
						data: [ '异常个数', '正常个数' ]
					},
					series: [{
						name: '访问来源',
						type: 'pie',
						radius: ['50%', '70%'],
						avoidLabelOverlap: false,
						label: {
							normal: {
								show: false,
								position: 'center'
							},
							emphasis: {
								show: true,
								textStyle: {
									fontSize: '16',
									fontWeight: 'bold'
								}
							}
						},
						labelLine: {
							normal: {
								show: false
							}
						},
						data: [
							{
								value: abnormalCount,
								name: '异常个数'
							},
						    {
								value: normalCount,
								name: '正常个数'
							},
						],
						itemStyle: { // 饼状图之间隔开一点的配置参数
							normal: {
								borderWidth: 4,
								borderColor: '#ffffff',
							}
						}
					}]
				};
				myChart.setOption(option);
				init(0);
 
				function init(index) {
					myChart.dispatchAction({
						type: 'highlight',
						seriesIndex: 0,
						dataIndex: index
					});
				}
				//记录上次高亮的索引
				var lastMouseOverIndex = null;
				// mouseover事件，记录当前数据索引并取消其他高亮，over在out之后
				myChart.on('mouseover', function(params) {
					var dataLen = option.series[0].data.length;
					lastMouseOverIndex = params.dataIndex;
					for(var i = 0; i < dataLen; i++) {
						if(i != params.dataIndex) {
							myChart.dispatchAction({
								type: 'downplay',
								seriesIndex: 0,
								dataIndex: i
							})
						}
					}
				});
				// mouseout事件，将上次的高亮
				myChart.on('mouseout', function(params) {
					myChart.dispatchAction({
						type: 'highlight',
						seriesIndex: 0,
						dataIndex: lastMouseOverIndex
					})
				});
			},
			echartPie2(fanNormalCount, fanAbnormalCount) { // 饼状图的相关配置与事件 
				
				// 1. 默认选中了某一个
				// 2. 鼠标离开记录上次选中的作为当前选中的			
				
				var echarts = require('echarts');
 
				// 基于准备好的dom，初始化echarts实例
				var myChart = echarts.init(document.getElementById('main2'));
				// 不能在单个容器上初始化多个 ECharts 实例。
				//实例容器，一般是一个具有高宽的div元素。
 
				var option = {
					tooltip: {
						trigger: 'item',
						formatter: "{a} <br/>{b}: {c} ({d}%)"
					},
					color: [ '#FF0000', '#298A08' ],
					legend: {
						orient: 'vertical',
						x: 'left',
						data: [ '异常个数', '正常个数' ]
					},
					series: [{
						name: '访问来源',
						type: 'pie',
						radius: ['50%', '70%'],
						avoidLabelOverlap: false,
						label: {
							normal: {
								show: false,
								position: 'center'
							},
							emphasis: {
								show: true,
								textStyle: {
									fontSize: '16',
									fontWeight: 'bold'
								}
							}
						},
						labelLine: {
							normal: {
								show: false
							}
						},
						data: [{
								value: fanAbnormalCount,
								name: '异常个数',
							},
							{
								value: fanNormalCount,
								name: '正常个数'
							}
						],
						itemStyle: { // 饼状图之间隔开一点的配置参数
							normal: {
								borderWidth: 4,
								borderColor: '#ffffff',
							}
						}
					}]
				};
				myChart.setOption(option);
				init(0);
 
				function init(index) {
					myChart.dispatchAction({
						type: 'highlight',
						seriesIndex: 0,
						dataIndex: index
					});
				}
				//记录上次高亮的索引
				var lastMouseOverIndex = null;
				// mouseover事件，记录当前数据索引并取消其他高亮，over在out之后
				myChart.on('mouseover', function(params) {
					var dataLen = option.series[0].data.length;
					lastMouseOverIndex = params.dataIndex;
					for(var i = 0; i < dataLen; i++) {
						if(i != params.dataIndex) {
							myChart.dispatchAction({
								type: 'downplay',
								seriesIndex: 0,
								dataIndex: i
							})
						}
					}
				});
				// mouseout事件，将上次的高亮
				myChart.on('mouseout', function(params) {
					myChart.dispatchAction({
						type: 'highlight',
						seriesIndex: 0,
						dataIndex: lastMouseOverIndex
					})
				});
			}
		}
	}
</script>

<style scoped>

</style>