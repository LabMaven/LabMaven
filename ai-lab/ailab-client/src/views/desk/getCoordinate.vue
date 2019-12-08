<template>	
	<div>
		<el-form :model="addForm" style="height: 80px;" :rules="addFormRules" ref="addForm" :inline="true">
            <el-form-item label="系统标识" prop="sMapId">
            	<el-select v-model="addForm.sMapId" placeholder="请选择">
                    <el-option v-for="item in systemPics" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <!--
            <el-form-item label="坐标类型" prop="coopType">
            	<el-select v-model="addForm.coopType" placeholder="请选择">
                    <el-option v-for="item in coopTypes" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
        	-->
            <el-form-item label="坐标值" prop="coordinate">
                <el-input v-model="addForm.coordinate" auto-complete="off" readonly="readonly"></el-input>
            </el-form-item>
            <el-form-item label="传感器" prop="sId">
            	<el-select v-model="addForm.sId" placeholder="请选择">
                    <el-option v-for="item in sensors" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addSubmit">提交</el-button>
            </el-form-item>              
        </el-form>

        <input type="file" class="input-file" id="uploadImg" accept="image/*" @change="uploadImg($event)">		
		<div id="myCanvas" @click="cnvs_getCoordinates($event)"></div>
	</div> 
</template>

<script>
import { getSensorCollect, addCoordinate } from '../../api/device';
export default {
	data() {
        return {
        	x: '',
        	y: '',
        	addForm: {
        		sMapId: '',
        		coopType: '',
        		coordinate: '',
        		sId: ''
        	},
        	coopTypes: [
        	    {label:'左上', value: '1', key: '1'},
        	    {label:'右下', value: '2', key: '2'}
        	],
        	systemPics: [
        	    {label: '系统图1', value: '1', key: '1'},
        	    {label: '系统图2', value: '2', key: '2'},
        	    {label: '系统图3', value: '3', key: '3'},
        	    {label: '系统图4', value: '4', key: '4'},
        	    {label: '系统图5', value: '5', key: '5'},
        	    {label: '系统图6', value: '6', key: '6'},
        	    {label: '系统图7', value: '7', key: '7'},
        	    {label: '系统图8', value: '8', key: '8'},
        	    {label: '系统图9', value: '9', key: '9'},
        	    {label: '系统图10', value: '10', key: '10'},
        	    {label: '系统图11', value: '11', key: '11'},
        	    {label: '系统图12', value: '12', key: '12'}
        	],
            addFormRules: {
                sMapId: [ {required: true, message: '请选择系统标识', trigger: 'blur' }],
                coordinate: [ {required: true, message: '请在系统图上选取坐标', trigger: 'blur' }],
                sId: [ {required: true, message: '请选择传感器', trigger: 'blur' }]
            },
            sensors: []
        }
    },
	methods: {
		cnvs_getCoordinates: function(e) {	
			this.x=e.clientX - $("#myCanvas").offset().left;
			this.y=e.clientY - $("#myCanvas").offset().top;
			this.addForm.coordinate = parseInt(this.x) + "," + parseInt(this.y);
		},
		uploadImg: function(e) {
			var prevDiv = document.getElementById('myCanvas'); 
            if (e.path && e.path[0].files[0]) {
                var reader = new FileReader();  
                reader.onload = function(evt){  
                    prevDiv.innerHTML = '<img src="' + evt.target.result + '" />';  
                }    
                reader.readAsDataURL(e.path[0].files[0]);  
            }
		},
		addSubmit: function() {
			this.$refs.addForm.validate((valid) => {
                if (valid) {
					let params = Object.assign({}, this.addForm);

                    addCoordinate(params).then((res) => {                               
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
                    });
                }
            });
		},
		init: function() {
			let param = Object.assign({pageNum: 1, pageSize: 200 });
            getSensorCollect(param).then(res => {
            	res.data.list.forEach(item => {
            		this.sensors.push({label: item.rDes+"_"+item.des, value: item.sId ,key: item.sId });
            	});
            });
		}
	},
	mounted() {
		this.init();
	}
}
</script>
