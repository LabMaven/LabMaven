<template>
    <section>
    <el-select v-model="province" clearable placeholder="请选择省" :style="{width:width+'px'}" 
    @change="getCityList">
        <el-option v-for="item in provinces" :key="item.code" :label="item.name"
           :value="item.code">
        </el-option>
    </el-select>
    <el-select v-model="city" clearable v-if="level != '1'" placeholder="请选择市" :style="{'margin-left':margin+'px',width:width+'px'}"
    @change="getDistrictList">
        <el-option v-for="item in citys" :key="item.code" :label="item.name"
           :value="item.code">
        </el-option>
    </el-select>
    <el-select v-model="district" clearable v-if="level === '3'" placeholder="请选择区" :style="{'margin-left':margin+'px',width:width+'px'}">
        <el-option v-for="item in districts" :key="item.code" :label="item.name"
           :value="item.code">
        </el-option>
    </el-select>
</section>
</template>

<script>
import { getProvinceList, getCityList, getDistrictList } from '../api/user';

export default {
    props: {
        level: {
            type: String,
            default: '3'
        },
        width: {
            type: String,
            default: '140'
        },
        defaultProvince: {
            type: String,
            default: ''
        },
        defaultCity: {
            type: String,
            default: ''
        },
        defaultDistrict: {
            type: String,
            default: ''
        }
    },
    watch: {
        defaultProvince: function() {
            this.province = this.defaultProvince;
        },
        defaultCity: function() {
            this.city = this.defaultCity;
        },
        defaultDistrict: function() {
            this.district = this.defaultDistrict;
        }
    },
    data() {
        return {
            provinces: [],
            citys: [],
            districts: [],
            province: '',
            city: '',
            district: '',
            margin: 8
        }
    },
    methods: {
        getCityList: function () {            
            if (this.province == '' || this.province == this.defaultProvince) {
                return;
            }
            this.city = '';
            this.citys = [];
            this.district = '';
            this.districts = [];
            
            let params = {parentCode: this.province};
            getCityList(params).then((res) => {
                this.citys = res.data;
            });
        },
        getDistrictList: function () {            
            if (this.city == '' || this.city == this.defaultCity) {
                return;
            }
            this.district = '';
            this.districts = [];

            let params = {parentCode : this.city};
            getDistrictList(params).then((res) => {
                this.districts = res.data;
            });
        },
        initProvinces: function () {
            getProvinceList({areaLevel : 1}).then((res) => {                
                this.provinces = res.data;
            });
        }
    },
    mounted() {
        this.initProvinces();
    }
}
</script>

<style scoped>

</style>