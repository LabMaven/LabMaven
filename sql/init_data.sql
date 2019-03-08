INSERT INTO t_cfg_user(id, customer_id, user_name, password, true_name, phone, user_type)
values('b7d0a1cff9e511e7bf401866daf21a53','0000', 'admin', '21218cca77804d2ba1922c33e0151105', '超级管理员', '0000', 1);


INSERT INTO `t_cfg_menu` (`id`, `name`, `parent_id`, `url`, `style`, `sort`) VALUES (10000, '设备', 0, '/deviceQry', '', 1);
INSERT INTO `t_cfg_menu` (`id`, `name`, `parent_id`, `url`, `style`, `sort`) VALUES (20000, '风机盘管', 0, '/fanCoiler', '', 1);
INSERT INTO `t_cfg_menu` (`id`, `name`, `parent_id`, `url`, `style`, `sort`) VALUES (30000, '报表', 0, '/report', '', 1);
INSERT INTO `t_cfg_menu` (`id`, `name`, `parent_id`, `url`, `style`, `sort`) VALUES (40000, '历史曲线', 0, '/hisChart', '', 1);
INSERT INTO `t_cfg_menu` (`id`, `name`, `parent_id`, `url`, `style`, `sort`) VALUES (50000, '告警', 0, '/alarm', '', 1);
INSERT INTO `t_cfg_menu` (`id`, `name`, `parent_id`, `url`, `style`, `sort`) VALUES (60000, '用户', 0, '/', '', 1);
INSERT INTO `t_cfg_menu` (`id`, `name`, `parent_id`, `url`, `style`, `sort`) VALUES (60001, '用户管理', 60000, '/user', '', 1);
INSERT INTO `t_cfg_menu` (`id`, `name`, `parent_id`, `url`, `style`, `sort`) VALUES (60002, '角色管理', 60000, '/role', '', 1);

truncate table t_cfg_rolemenu;
INSERT INTO `t_cfg_rolemenu` VALUES ('0093f579f9e711e7bf401866daf21a53', 10000);
INSERT INTO `t_cfg_rolemenu` VALUES ('0093f579f9e711e7bf401866daf21a53', 20000);
INSERT INTO `t_cfg_rolemenu` VALUES ('0093f579f9e711e7bf401866daf21a53', 30000);
INSERT INTO `t_cfg_rolemenu` VALUES ('0093f579f9e711e7bf401866daf21a53', 40000);
INSERT INTO `t_cfg_rolemenu` VALUES ('0093f579f9e711e7bf401866daf21a53', 50000);
INSERT INTO `t_cfg_rolemenu` VALUES ('0093f579f9e711e7bf401866daf21a53', 60000);


truncate table t_cfg_dict;
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (11, 1, 'sensor_type', '压差', 'pressure');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (12, 2, 'sensor_type', '温湿度', 'tempAndHum');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (13, 3, 'sensor_type', '温度', 'temperature');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (14, 4, 'sensor_type', '湿度', 'humidity');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (15, 5, 'sensor_type', '通风柜', 'ventilator');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (16, 6, 'sensor_type', '通风柜门高', 'ventilator');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (17, 7, 'sensor_type', '人员工作状态', 'workState');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (18, 8, 'sensor_type', '面风速', 'airSpeed');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (19, 9, 'sensor_type', '通风柜变风量排风阀风量', 'ventilator_airVolumn');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (20, 10, 'sensor_type', '余风量', 'airVolumn');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (21, 11, 'sensor_type', '房间排风量', 'room_out_airVolumn');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (22, 12, 'sensor_type', '房间送风量', 'room_in_airVolumn');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (23, 13, 'sensor_type', '变风量送风蝶阀风量', 'flygate');

INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (31, 31, 'sensor_type', '可编程控制器', 'programmable_controller');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (32, 32, 'sensor_type', '管道静压传感器', 'pipe_pressure');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (33, 33, 'sensor_type', '流量开关', 'flow_switch');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (34, 34, 'sensor_type', '管道温湿度传感器', 'pipe_tempAndHum');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (35, 35, 'sensor_type', '气流开关', 'airflow_switch');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (36, 36, 'sensor_type', '防冻开关', 'freezeprotect_switch');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (37, 37, 'sensor_type', '冷热水调节阀', 'regulating_valve');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (38, 38, 'sensor_type', '电动风阀执行器', 'airvalve_actuator');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (39, 39, 'sensor_type', '排风变频器', 'frequency_converter');
INSERT INTO `t_cfg_dict` (`dict_id`, `dict_code`, `col_name`, `dict_note`, `remark`) VALUES (40, 40, 'sensor_type', '新风变频器', 'frequency_converter');



