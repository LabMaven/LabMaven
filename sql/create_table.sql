CREATE TABLE `t_cfg_dict` (
  `dict_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '主键标识',
  `dict_code` int(5) NOT NULL COMMENT '枚举值id',
  `col_name` varchar(20) NOT NULL COMMENT '枚举值类型',
  `dict_note` varchar(20) NOT NULL COMMENT '对应的枚举值',
  `remark` varchar(100) NOT NULL COMMENT '其他说明信息',
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置表';

CREATE TABLE `t_cfg_menu` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` int(5) NOT NULL COMMENT '上级菜单id',
  `url` varchar(255) DEFAULT NULL COMMENT 'url链接',
  `style` varchar(50) DEFAULT NULL COMMENT '样式',
  `sort` int(5) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

CREATE TABLE `t_cfg_role` (
  `id` varchar(40) NOT NULL COMMENT '角色id',
  `name` varchar(40) NOT NULL COMMENT '角色名称',
  `description` varchar(50) DEFAULT NULL COMMENT '角色描述',
  `editable` tinyint(2) NOT NULL DEFAULT 1 COMMENT '是否可编辑:1编辑，0不可编辑',
  `deletable` tinyint(2) NOT NULL DEFAULT 1 COMMENT '是否可删除:1可删除，0不可删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE `t_cfg_rolemenu` (
  `role_id` varchar(40) NOT NULL DEFAULT '' COMMENT '角色id',
  `menu_id` int(5) NOT NULL DEFAULT '0' COMMENT '菜单id',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表';

CREATE TABLE `t_cfg_user` (
  `id` varchar(40) NOT NULL COMMENT '主键,用户id',
  `customer_id` varchar(40) NOT NULL COMMENT '所属客户id',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `true_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `user_type` int(5) NOT NULL COMMENT '用户类型 1:系统管理员;2:客户管理员;3:普通客户用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE `t_cfg_userrole` (
  `user_id` varchar(40) NOT NULL DEFAULT '' COMMENT '用户id',
  `role_id` varchar(40) NOT NULL DEFAULT '' COMMENT '角色id',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

CREATE TABLE `b_define` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `b_code` varchar(20) NOT NULL COMMENT '编码',
  `b_id` varchar(5) NOT NULL COMMENT '楼宇ID',
  `o_id` varchar(100) DEFAULT NULL COMMENT '管理员ID',
  `des` varchar(100) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_b_define_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='楼宇表';

CREATE TABLE `f_define` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `b_id` varchar(5) NOT NULL COMMENT '楼宇ID',
  `f_code` varchar(100) DEFAULT NULL,
  `f_id` varchar(10) NOT NULL COMMENT '楼层ID',
  `o_id` varchar(100) DEFAULT NULL COMMENT '管理员ID',
  `des` varchar(100) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  KEY `idx_f_define_id` (`b_id`,`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='楼层定义表';

CREATE TABLE `r_define` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `b_id` varchar(5) NOT NULL COMMENT '楼宇ID',
  `f_id` varchar(10) NOT NULL COMMENT '楼层ID',
  `r_id` varchar(15) NOT NULL COMMENT '房间ID',
  `o_id` varchar(100) DEFAULT NULL COMMENT '管理员ID',
  `des` varchar(100) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  KEY `idx_r_define_id` (`b_id`,`f_id`,`r_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房间定义表';

CREATE TABLE `c_define` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `c_code` varchar(20) DEFAULT NULL COMMENT '控制器code',
  `c_id` varchar(20) NOT NULL COMMENT '控制器Id',
  `s_code` varchar(20) DEFAULT NULL COMMENT '传感器code',
  `s_id` varchar(20) NOT NULL COMMENT '传感器Id',
  `c_type` tinyint(1) NOT NULL COMMENT '控制器类型:1.新风机,2.VAV,3.通风柜,4.H2探头',
  `s_type` tinyint(1) NOT NULL COMMENT '传感器类型:1 压差传感器,2 温湿,3 湿度,4 通风柜门高,5 人员工作状态,6 面风速,7 余风量,8 变风量送风蝶阀风量',
  `s_max` float DEFAULT NULL COMMENT '上门限值',
  `s_min` float DEFAULT NULL COMMENT '下门限值',
  `c_out` JSON DEFAULT NULL COMMENT '下发指令集',
  `alarm_type` int(1) NOT NULL COMMENT '是否告警：0不告警，1告警',
  `code_value_min` int(20) NOT NULL COMMENT '码值最小',
  `code_value_max` int(20) NOT NULL COMMENT '码值最大',
  `project_value_min` int(2) NOT NULL COMMENT '工程值最小',
  `project_value_max` int(2) NOT NULL COMMENT '工程值最大',
  `offset` int(20) NOT NULL COMMENT '偏移量',
  `unit_item` varchar(2) NOT NULL COMMENT '界面单位',
  `des` varchar(200) DEFAULT NULL COMMENT '描述',
  `m_t_address` smallint(5) unsigned NOT NULL COMMENT 'modbus tcp寄存器地址',
  `m_t_length` tinyint(4) NOT NULL COMMENT 'modbus tcp长度',
  `m_t_collect` tinyint(1) DEFAULT NULL COMMENT 'modbus tcp是否采集该设备',
  `s_pid` varchar(20) NOT NULL COMMENT '传感器集合Id',
  `readorwrite` int(2) NOT NULL DEFAULT 0 COMMENT '寄存器地址为只读或写入：0只读;1写入',
  `configurable` int(2) NOT NULL DEFAULT 1 COMMENT '该寄存器是否可以从界面进行控制:0可以控制;1不可以控制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='控制器定义表';

CREATE TABLE `e_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `c_id` varchar(20) NOT NULL COMMENT '控制器Id',
  `p_type` tinyint(1) NOT NULL COMMENT '协议类型：1 Modbus TCP,2 TCP,3 UDP',
  `m_t_port` smallint(6) NOT NULL COMMENT 'modbus tcp使用的端口',
  `m_t_ip` varchar(15) NOT NULL COMMENT 'modbus tcp使用的ip',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '设备添加时间',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0 正常,1 异常,2 手工离线',
  `o_id` varchar(100) DEFAULT NULL COMMENT '管理员ID',
  `des` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_e_list_c_id` (`c_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='系统信息表';

CREATE TABLE `s_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `c_id` varchar(20) NOT NULL COMMENT '控制器Id',
  `s_id` varchar(20) NOT NULL COMMENT '传感器Id',
  `lastupdatetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后状态更新时间',
  `s_flag` varchar(2) DEFAULT '-1' COMMENT '开关类传感器状态：-1 未使用,0 打开,1 关闭',
  `m_t_address` smallint(5) unsigned NOT NULL COMMENT 'modbus tcp寄存器地址',
  `m_t_length` tinyint(4) NOT NULL COMMENT 'modbus tcp长度',
  `alarm_type` int(1) NOT NULL COMMENT '是否告警：0不告警，1告警',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开关量状态数据表';

CREATE TABLE `e_data` (
  `s_id` varchar(20) NOT NULL COMMENT '传感器Id',
  `input_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上报时间',
  `value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='控制器数据表';


drop table if exists `c_ctl_info`;
CREATE TABLE `c_ctl_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `c_code` varchar(20) DEFAULT NULL COMMENT '控制器code',
  `c_id` varchar(20) NOT NULL COMMENT '控制器Id',
  `s_code` varchar(20) DEFAULT NULL COMMENT '传感器code',
  `s_id` varchar(20) NOT NULL COMMENT '传感器Id',
  `v_type` int(2) NOT NULL COMMENT '下发值类型：0开关量；1模拟量；2一键强排',
  `switch_flag` int(2) COMMENT '开关量：0打开，1关闭',
  `s_value` int(2) COMMENT '工程值',
  `s_status` int(2) COMMENT '一键强排：0打开，1关闭',
  `rec_status` int(2) NOT NULl DEFAULT 0 COMMENT '记录下发状态：0未下发，1已经下发',
  `re_crea_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `rec_upda_time` datetime DEFAULT NULL COMMENT '记录更新时间',
  `o_id` varchar(100) NOT NULL COMMENT '管理员ID',  
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cid_sid` (`c_id`, `s_id`),
  KEY `idx_ctl_info_crea_time` (`re_crea_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='控制器控制信息表';

drop table if exists `c_ctl_info_his`;
CREATE TABLE `c_ctl_info_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `c_code` varchar(20) DEFAULT NULL COMMENT '控制器code',
  `c_id` varchar(20) NOT NULL COMMENT '控制器Id',
  `s_code` varchar(20) DEFAULT NULL COMMENT '传感器code',
  `s_id` varchar(20) NOT NULL COMMENT '传感器Id',
  `v_type` int(2) NOT NULL COMMENT '下发值类型：0开关量；1模拟量；2一键强排',
  `switch_flag` int(2) COMMENT '开关量：0打开，1关闭',
  `s_value` int(2) COMMENT '工程值',
  `s_status` int(2) COMMENT '一键强排：0打开，1关闭',
  `rec_status` int(2) NOT NULl DEFAULT 0 COMMENT '记录下发状态：0未下发，1已经下发',
  `re_crea_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `rec_upda_time` datetime DEFAULT NULL COMMENT '记录更新时间',
  `o_id` varchar(100) NOT NULL COMMENT '管理员ID',  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='控制器控制信息历史表';


alter table c_define add coords VARCHAR(20) comment '传感器热区坐标值,格式:x1,y1,x2,y2';

drop table if exists `s_system_map`;
CREATE TABLE `s_system_map` (
  `s_map_id` int(11) NOT NULL DEFAULT '1' COMMENT '系统图键值',
  `s_point_id` int(11) DEFAULT NULL COMMENT '系统图测点序列号',
  `coordinate` varchar(20) NOT NULL DEFAULT '' COMMENT '热区坐标',
  `s_id` varchar(20) NOT NULL COMMENT '传感器Id',
  `des` varchar(100) DEFAULT NULL COMMENT '协议端口号',
  PRIMARY KEY (`s_map_id`,`s_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='传感器热区坐标数据表';

