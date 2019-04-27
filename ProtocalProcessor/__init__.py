#!/usr/bin/python
# -*- coding: UTF-8 -*-
import sys
import os
import json
import time
from threading import Thread
import modbus_tk
import modbus_tk.defines as cst
import modbus_tk.modbus_tcp as modbus_tcp
import socket

LOGGER = modbus_tk.utils.create_logger("console")

workpath = os.path.dirname(os.path.abspath(sys.argv[0]))
sys.path.insert(0, os.path.join(workpath, 'modules'))

# import multiprocessing
# import subprocess
# import threading
# from Processor.AlarmProcessor import funcA
# from multiprocessing.sharedctypes import
import CacheService
from CacheService import CacheService
import ConfigService
from LoggerService import LoggerService
import pymysql
import requests

##############################################Variable########################################
CacheDict = {}
ConfigDict = {}
localLogger = LoggerService().getlog()

PYNAME = os.path.basename(__file__)
WORKPATH = os.path.dirname(os.path.realpath(__file__))

stop_flag_file = os.path.join(WORKPATH, "run", "stop_flag_file")
ps_conf_fn = os.path.join(WORKPATH, "conf", "PotocalProcessor.json")

with open(ps_conf_fn, 'r') as cfp:
    ps_conf_dict = (json.load(cfp))
# print(json.dump(ps_conf_fn))
'''
The configpare don't have the capacity to load the whole configuration file by one op, so i chnange 
the configuration file to json format. 

print("[%s][%d][ps_conf_fn]" % (PYNAME, sys._getframe().f_lineno), ps_conf_fn)
'''

# stop_flag_file="C:\\Users\\sammyjeep\\PycharmProjects\\LabMaven\\ProtocalProcessor\\run\\stop_flag_file"
count = 0

##############################################Func########################################
def checkSingleDevice(deviceid, deviceinfo):
    # global count
    global ps_conf_dict
    count = 0
    threadId = deviceid
    global stop_flag_file
    localLogger.info("current device is [" + deviceid + ']:' + str(deviceinfo))

    #MASTER = modbus_tcp.TcpMaster(host="127.0.0.1", port=12600)
    # localLogger.info("current device is [" + deviceId + '][m_t_ip]:' + str(type(deviceInfo["m_t_ip"])) )
    # localLogger.info("current device is [" + deviceId + '][m_t_port]:' + str(type(deviceInfo["m_t_port"])))
    MASTER = modbus_tcp.TcpMaster(host=deviceinfo["m_t_ip"], port=deviceinfo["m_t_port"])
    MASTER.set_timeout(5.0)

    # while not os.path.exists(stop_flag_file):
    # cursor = pymysql.connect(host=ps_conf_dict["mysql"]["host"], user=ps_conf_dict["mysql"]["user"],
    #                        password=ps_conf_dict["mysql"]["password"], db=ps_conf_dict["mysql"]["db"],
    #                        port=ps_conf_dict["mysql"]["port"]).cursor(cursor=pymysql.cursors.DictCursor)
    conn = pymysql.connect(host=ps_conf_dict["mysql"]["host"], user=ps_conf_dict["mysql"]["user"],
                           password=ps_conf_dict["mysql"]["password"], db=ps_conf_dict["mysql"]["db"],
                           port=ps_conf_dict["mysql"]["port"])


    cursor = conn.cursor()
    while True:
        if os.path.exists(stop_flag_file):
            break

        localLogger.info(count)
        # localLogger.info()
        localLogger.info("current device is [" + deviceid + ']:' + str(count))
        localLogger.info("current device is [" + deviceid + '][m_t_address]:' + str(deviceinfo["m_t_address"]))
        localLogger.info("current device is [" + deviceid + '][m_t_length]:' + str(deviceinfo["m_t_length"]))
        count = count + 1
        # value=MASTER.execute(1, cst.READ_HOLDING_REGISTERS, 0, 1)
        LOGGER.info(MASTER.execute(1, cst.READ_HOLDING_REGISTERS, deviceinfo["m_t_address"], deviceinfo["m_t_length"]))
        current_value=MASTER.execute(1, cst.READ_HOLDING_REGISTERS, deviceinfo["m_t_address"], deviceinfo["m_t_length"])[0]

        #print(deviceinfo)
        code_value=deviceinfo["project_value_min"] + ((deviceinfo["project_value_max"] -  deviceinfo["project_value_min"]) / (deviceinfo["code_value_max"] - deviceinfo["code_value_min"]) ) * ( current_value + deviceinfo["offset"] - deviceinfo["code_value_min"])
        #print(code_value)

        #rtn=cursor.execute("insert into e_data(s_id, input_time, value) VALUES(%s, %s, %s)", (deviceid, time.gmtime(time.time()), int(current_value)))
        rtn = cursor.execute("insert into e_data(s_id, input_time, value) VALUES(%s, %s, %s)",
                             (deviceid, time.gmtime(time.time()), float(code_value)))
        conn.commit()

        if code_value > deviceinfo["s_max"] or code_value < deviceinfo["s_min"]:
            url = 'http://192.168.2.169:8088/ailab/alarm/collectAlarmInfo?alarmLevel=1&alarmStatus=1'
            body = {"type": "text", "content": "Ailib", "tag_id": "20181204"}
            headers = {'content-type': "application/json"}

            print(CacheDict[deviceid])
            if CacheDict[deviceid]["alarm_status"] == 0:
                # rtn = cursor.execute("insert into system_alarm_info( c_id, s_id, enter_time, " +
                #                     "update_time, alarm_info) VALUES(%s, %s, %s, %s, %s)",
                #                     (deviceid[0:14], deviceid, time.gmtime(time.time()), time.gmtime(time.time()),
                #                      "current value is", 0,))
                rtn = cursor.execute("insert into system_alarm_info( c_id, s_id, enter_time, " +
                                     "update_time, alarm_info) VALUES(%s, %s, %s, %s, %s)",
                                     (deviceid[0:14], deviceid, time.gmtime(time.time()), time.gmtime(time.time()),
                                      "current value is" + str(code_value)))
                conn.commit()
                CacheDict[deviceid]["alarm_status"] = 1
            else:
                cursor.execute("update system_alarm_info set update_time=%s where s_id=%s",
                                     (time.gmtime(time.time()), deviceid))
                conn.commit()
        else:
            url = 'http://192.168.2.169:8088/ailab/alarm/collectAlarmInfo?alarmLevel=1&alarmStatus=1'
            body = {"type": "text", "content": "Ailib", "tag_id": "20181204"}
            headers = {'content-type': "application/json"}
            if CacheDict[deviceid]["alarm_status"] == 1:
                rtn = cursor.execute("update system_alarm_info set rollbackTime=%s, rollbackflag=0, " +
                                     "confirmflag=0, confirmdate=%s where s_id=%s",
                                     (time.gmtime(time.time()), time.gmtime(time.time()), deviceid ))
                conn.commit()
                CacheDict[deviceid]["alarm_status"] = 0
                # print type(body)
                # print type(json.dumps(body))
                # 这里有个细节，如果body需要json形式的话，需要做处理
                # 可以是data = json.dumps(body)
                # response = requests.post(url, data=json.dumps(body), headers=headers)
                response = requests.post(url, data=json.dumps(body), headers=headers)
                # 也可以直接将data字段换成json字段，2.4.3版本之后支持
                # response  = requests.post(url, json = body, headers = headers)

                # 返回信息
                print(response.text)
                # 返回响应头
                print(response.status_code)

        #print(rtn)
        # LOGGER.info(MASTER.execute(1, cst.READ_HOLDING_REGISTERS, 4, 14))
        time.sleep(ps_conf_dict["common"]["checkInterval"])


def check_single_controller(controller_id, controller_info):
    # global count
    global ps_conf_dict
    count = 0
    thread_id = controller_id
    global stop_flag_file

    addr_list = {}
    action_list = []

    localLogger.info("current controller is [" + controller_id + ']:' + str(controller_info))

    localLogger.info("[Thread %15s]Begin to connect to controller %s:%s" %
                     (thread_id, controller_info["common"]["m_t_ip"], controller_info["common"]["m_t_port"]))
    MASTER = modbus_tcp.TcpMaster(host=controller_info["common"]["m_t_ip"], port=controller_info["common"]["m_t_port"])
    MASTER.set_timeout(5.0)

    conn = pymysql.connect(host=ps_conf_dict["mysql"]["host"], user=ps_conf_dict["mysql"]["user"],
                           password=ps_conf_dict["mysql"]["password"], db=ps_conf_dict["mysql"]["db"],
                           port=ps_conf_dict["mysql"]["port"])
    cursor = conn.cursor()

    for device_id in controller_info:
        if device_id == "common":
            continue
        localLogger.info(device_id)
        addr_list[controller_info[device_id]["m_t_address"]]=device_id

        localLogger.info("current device is [%s]" % (device_id))
        localLogger.info("current device address is [%s]" % (controller_info[device_id]["m_t_address"]))

    #addr_list.sort()

    output_addr_offset={}
    opt_addr_list = sorted(addr_list.keys())
    init_addr = min(opt_addr_list)
    stop_addr = max(opt_addr_list)
    #action_list=[]
    while True:
        org_addr_list = []
        cur_len = stop_addr - init_addr + 1
        if cur_len <=100:
            action_list.append([init_addr, cur_len])
            org_addr_list=opt_addr_list[:]
            for each_org_addr in org_addr_list:
                output_addr_offset[each_org_addr] = (init_addr, each_org_addr - init_addr, addr_list[each_org_addr])
            break
        else:
            cur_len = 100
            print(min(opt_addr_list))

            for i in range(100):
                try:
                     opt_addr_list.remove(init_addr + i)
                     if init_addr + i in addr_list:
                        org_addr_list.append(init_addr + i)
                except:
                     pass

            print(org_addr_list)
            cur_len = max(org_addr_list) - min(org_addr_list) + 1
            action_list.append([init_addr, cur_len])
            for each_org_addr in org_addr_list:
                output_addr_offset[each_org_addr]=(init_addr, each_org_addr - init_addr, addr_list[each_org_addr])

            next_min_addr = min(opt_addr_list)
            init_addr = init_addr + 100
            if next_min_addr > init_addr:
                init_addr = next_min_addr

    print(action_list)
    print(output_addr_offset)

    while True:
        if os.path.exists(stop_flag_file):
            break

        PLC_values={}

        for one_action in action_list:
            print("one_action: %s" % one_action)

            try:
                #LOGGER.info(MASTER.execute(1, cst.READ_HOLDING_REGISTERS, one_action[0], one_action[1]))
                PLC_values[one_action[0]]=MASTER.execute(1, cst.READ_HOLDING_REGISTERS, one_action[0], one_action[1])
                #PLC_values[one_action[0]]=MASTER.execute(1, cst.READ_HOLDING_REGISTERS, one_action[0], 1)
                print("PLC_values is %s" % PLC_values)
            except socket.timeout:
                print("Failed to connect to the PLC due to the connection time out, wait 10 seconds to retry.")
                time.sleep(10)
            except ConnectionRefusedError:
                print("Failed to connect to the PLC due to the service isn't ready, wait 10 seconds to retry.")
                time.sleep(10)
            # except Exception as exp:
            #    print("Failed to get value from PLC due to Exception %s" % type(exp))
            #    print("Failed to get value from PLC due to Exception %s" % exp)
            #    time.sleep(10)

        if len(PLC_values) == 0:
            continue

        print(output_addr_offset)
        for one_position in output_addr_offset:
            print("one_positon is %s" % one_position)
            current_value = PLC_values[output_addr_offset[one_position][0]][output_addr_offset[one_position][1]]
            print(current_value)

            #print(deviceinfo)
            code_value=controller_info[device_id]["project_value_min"] + ((controller_info[device_id]["project_value_max"] -
                                                                           controller_info[device_id]["project_value_min"]) /
                                                                          (controller_info[device_id]["code_value_max"] -
                                                                           controller_info[device_id]["code_value_min"]) ) * \
                       ( current_value + controller_info[device_id]["offset"] - controller_info[device_id]["code_value_min"])
            #print(code_value)

            rtn = cursor.execute("insert into e_data(s_id, input_time, value) VALUES(%s, %s, %s)",
                                 (output_addr_offset[one_position][2], time.gmtime(time.time()), float(code_value)))
            conn.commit()

            if code_value > controller_info[device_id]["s_max"] or code_value < controller_info[device_id]["s_min"]:
                url = 'http://192.168.2.169:8088/ailab/alarm/collectAlarmInfo?alarmLevel=1&alarmStatus=1'
                body = {"type": "text", "content": "Ailib", "tag_id": "20181204"}
                headers = {'content-type': "application/json"}

                #print(CacheDict[device_id])
                print(CacheDict)
                if CacheDict[device_id]["alarm_status"] == 0:
                    #rtn = cursor.execute("insert into system_alarm_info( c_id, s_id, enter_time, " +
                    #                     "update_time, alarm_info) VALUES(%s, %s, %s, %s, %s)",
                    #                     (deviceid[0:14], deviceid, time.gmtime(time.time()), time.gmtime(time.time()),
                    #                      "current value is", 0,))
                    rtn = cursor.execute("insert into system_alarm_info( c_id, s_id, enter_time, " +
                                         "update_time, alarm_info) VALUES(%s, %s, %s, %s, %s)",
                                         (device_id[0:14], device_id, time.gmtime(time.time()), time.gmtime(time.time()),
                                          "current value is" + str(code_value)))
                    conn.commit()
                    CacheDict[device_id]["alarm_status"] = 1
                else:
                    rtn = cursor.execute("update system_alarm_info set update_time=%s where s_id=%s",
                                         (time.gmtime(time.time()), device_id))
                    conn.commit()
            else:
                url = 'http://192.168.2.169:8088/ailab/alarm/collectAlarmInfo?alarmLevel=1&alarmStatus=1'
                body = {"type": "text", "content": "Ailib", "tag_id": "20181204"}
                headers = {'content-type': "application/json"}
                if CacheDict[device_id]["alarm_status"] == 1:
                    rtn = cursor.execute("update system_alarm_info set rollbackTime=%s, rollbackflag=0, " +
                                         "confirmflag=0, confirmdate=%s where s_id=%s",
                                         (time.gmtime(time.time()), time.gmtime(time.time()), device_id ))
                    conn.commit()
                    CacheDict[device_id]["alarm_status"] = 0
                    # print type(body)
                    # print type(json.dumps(body))
                    # 这里有个细节，如果body需要json形式的话，需要做处理
                    # 可以是data = json.dumps(body)
                    # response = requests.post(url, data=json.dumps(body), headers=headers)
                    response = requests.post(url, data=json.dumps(body), headers=headers)
                    # 也可以直接将data字段换成json字段，2.4.3版本之后支持
                    # response  = requests.post(url, json = body, headers = headers)

                    # 返回信息
                    print(response.text)
                    # 返回响应头
                    print(response.status_code)

        #print(rtn)
        # LOGGER.info(MASTER.execute(1, cst.READ_HOLDING_REGISTERS, 4, 14))
        time.sleep(ps_conf_dict["common"]["checkInterval"])


##############################################Main########################################
localLogger.info('Potocal Stack main process start!')

localLogger.info('Fetching configuration start')
ConfigDict = ConfigService.main()
localLogger.info('Fetching configuration stoped')

localLogger.info('Fetching device start')
cacheService = CacheService()
cacheService.loadcache()
CacheDict=CacheService.CacheDict
localLogger.info('Fetching device stoped')

localLogger.info(ConfigDict)
localLogger.info(CacheDict)

checkThread = {}
for each_controller_id in ConfigDict.keys():
    checkThread[each_controller_id] = \
        Thread(target=check_single_controller, args=(each_controller_id, ConfigDict[each_controller_id],))
    checkThread[each_controller_id].start()

localLogger.info("Main process after check_single_controller")
# for DeviceId in ConfigDict.keys():
#     # checkSingleDevice(DeviceInfo)
#     #checkThread[DeviceId] = Thread(target=checkSingleDevice, args=(DeviceId, ConfigDict[DeviceId],))
#     checkThread[DeviceId] = Thread(target=checkSingleDevice, args=(DeviceId, ConfigDict[DeviceId],))
#     checkThread[DeviceId].start()
