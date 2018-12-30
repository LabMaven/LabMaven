<template>
    <div class="areaTree">
        <div class="tree-box">
            <div class="box-title">
                <a href="#"><i class="fa fa-refresh" @click="freshArea"></i></a>
            </div>
            <div class="zTreeDemoBackground left">                 
                <ul :id="skillTreeId" class="ztree"></ul>
            </div>
        </div>
        <el-input type="hidden" v-model="sids"></el-input>
    </div>
</template>

<script>
import { getSkillTree } from '../api/user';
    export default {
      props: {
        skillIds: {
            type: String,
            default: ''
        },
        skillTreeId: {
            type: String,
            default: 'treeDemo'
        }
      },
      watch: {
        skillIds: function() {
            var zTreeObj = $.fn.zTree.init($("#"+this.skillTreeId), this.setting, this.zNodes);
            var childNodes = zTreeObj.transformToArray(zTreeObj.getNodes());
            this.checkNode(childNodes);
            
            if(zTreeObj && this.skillIds != '') {
                var skillIdArr = this.skillIds.split(',');
                
                for (var i = 0; i < skillIdArr.length; i++) {
                    var node = zTreeObj.getNodeByParam('id', skillIdArr[i]);
                    zTreeObj.checkNode(node, true, true, true);                  
                }
            }
        }
      },
      data: function() {
        return {
            setting:{
                check: { 
                    enable: true,
                    chkStyle: "radio",
                    radioType: "level",
                    nocheckInherit: true,  
                    chkboxType: { "Y": "p", "N": "s" }   
                },
                data: { 
                    simpleData: { enable: true } 
                },
                callback: {
                    beforeClick: this.beforeClick,
                    onCheck: this.zTreeOnCheck,
                    onClick: this.zTreeOnCheck
                }
            },
            zNodes:[],
            sids: ''
        }
      },
      methods:{
        beforeClick :function(treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj(this.skillTreeId);         
            if (treeNode.isParent) {
                zTree.expandNode(treeNode);
            } else {
                zTree.checkNode(treeNode, !treeNode.checked, true);
            }
        
            return !treeNode.isParent;
        },
        zTreeOnCheck: function(e, treeId, treeNode) {
            this.sids = '';
            var treeObj = $.fn.zTree.getZTreeObj(this.skillTreeId);
            var nodes = treeObj.getNodesByFilter(function (node) { return node.level == 0; });
            for (var i = 0; i < nodes.length; i ++) {
               this.getSelected(nodes[i]); 
            }            
        },
        getSelected: function(node) {
            if (node.isParent) {
                var children = node.children;
                var len = children.length;
                for(var i = 0; i < len; i ++) {
                    this.getSelected(children[i]);
                }
            } else if (node.checked) {
                if (this.sids.length > 0) {
                    this.sids += ',';
                }
                this.sids += node.id;
            }
        },

        freshArea:function() {
            getSkillTree({}).then((res) => {
                this.zNodes = res.data;                
                var zTreeObj = $.fn.zTree.init($("#"+this.skillTreeId), this.setting, this.zNodes);                    
                var childNodes = zTreeObj.transformToArray(zTreeObj.getNodes());
                this.checkNode(childNodes);                

                if (zTreeObj != null && this.skillIds != '') {
                    var skillIdArr = this.skillIds.split(',');
                
                    for (var i = 0; i < skillIdArr.length; i++) {
                        var node = zTreeObj.getNodeByParam('id', skillIdArr[i]);
                        zTreeObj.checkNode(node, true, true, true);
                    }
                }
            });
        },
        checkNode: function(nodes) {
            for (var j = 0; j < nodes.length; j++) {
                if (nodes[j].isParent) {
                    nodes[j].nocheck = true;              
                    var childNodes = nodes[j].children;
                    this.checkNode(childNodes);
                } else {
                    nodes[j].nocheck = false;
                }
            }
        }
      },
      mounted() {
        this.freshArea();
      }
    }
</script>
<style>
    @import '../../plugins/ztree/css/zTreeStyle/zTreeStyle.css';
    .areaTree {
        border:1px solid #e5e5e5;
        margin-bottom: 2px;
        border-radius: 4px;
        overflow: hidden;
    }
    .box-title {
        border-radius: 3px 3px 0 0;
        background-color: #f5f5f5;
    }
    .box-title a {
        color: #2fa4e7;
        text-decoration: none;
        font-size:14px;    
        display: block;
        padding: 8px 15px;
        cursor: pointer;
    }
    .box-title .fa {
        float:right;line-height: 20px;
    }
</style>