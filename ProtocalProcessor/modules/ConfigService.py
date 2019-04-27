#!/usr/bin/python
# -*- coding: UTF-8 -*-.
import pymysql
from LoggerService import LoggerService

CacheDict = {}

#localLogger = LoggerService().getlog()

def main():
    #localLogger.info("This is the main function in ConfigService!")
    #CacheDict = { "b01f001r0001c00001" : { "ip": "127.0.0.1", "port" : "10001", "address": "40000", "length" : "1" } }

    #localLogger
    conn = pymysql.connect(host="localhost", user="root",password="1qaz!QAZ", db="ailab", port=3306)
    #conn = pymysql.connect(host="192.168.2.101", user="root", password="root", db="ailab", port=3306)
    #print(conn)
    #localLogger.info(conn)
    cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)
    #localLogger.info(cursor)
    #effect_row = cursor.execute("select * from c_define")

    #effect_row = cursor.execute("select c_define.s_id, e_list.m_t_ip, e_list.m_t_port, c_define.m_t_address, c_define.m_t_lenght, c_define.code_value_min, c_define.code_value_max, c_define.project_value_min, c_define.project_value_max, c_define.offsett from  c_define , e_list  where c_define.c_id = e_list.c_id")
    effect_row = cursor.execute("select c_define.c_id, c_define.s_id, e_list.m_t_ip, e_list.m_t_port, c_define.m_t_address, " +
                                "c_define.m_t_length, c_define.code_value_min, c_define.code_value_max, " +
                                "c_define.project_value_min, c_define.project_value_max, c_define.offset, " +
                                "s_max, s_min from c_define , e_list  where c_define.c_id = e_list.c_id " +
                                "and c_define.m_t_port is not null and c_define.m_t_collect = 1")
    #localLogger.info(effect_row)
    #rows=cursor.fetchall()
    #print(rows)
    effect_row = cursor.execute("select c_define.c_id, c_define.s_id, e_list.m_t_ip, e_list.m_t_port, c_define.m_t_address, " +
                                "c_define.m_t_length, c_define.code_value_min, c_define.code_value_max, " +
                                "c_define.project_value_min, c_define.project_value_max, c_define.offset, " +
                                "s_max, s_min from c_define , e_list  where c_define.c_id = e_list.c_id " +
                                "and c_define.m_t_port is not null and c_define.m_t_collect = 1")

    hosts_mapping_dict = {}
    for row in cursor.fetchall():
        #print(row["s_id"] + "m_t_ip :" + row["m_t_ip"])
        #print(row["s_id"] + "m_t_port :" + row["m_t_port"])
        #print(row["s_id"] + "m_t_address :" + str(row["m_t_address"]))
        #print(row["s_id"] + "m_t_lenght :" + str(row["m_t_lenght"]))
        if row["c_id"] not in hosts_mapping_dict:
            hosts_mapping_dict[row["c_id"]] = {}
            hosts_mapping_dict[row["c_id"]]["common"] = {}
        if row ["s_id"] not in hosts_mapping_dict[row["c_id"]]:
            hosts_mapping_dict[row["c_id"]][row["s_id"]]={}
        hosts_mapping_dict[row["c_id"]]["common"]["m_t_ip"]=row["m_t_ip"]
        hosts_mapping_dict[row["c_id"]]["common"]["m_t_port"] = row["m_t_port"]
        hosts_mapping_dict[row["c_id"]][row["s_id"]]["m_t_address"] = row["m_t_address"]
        hosts_mapping_dict[row["c_id"]][row["s_id"]]["m_t_length"] = row["m_t_length"]
        hosts_mapping_dict[row["c_id"]][row["s_id"]]["code_value_min"] = row["code_value_min"]
        hosts_mapping_dict[row["c_id"]][row["s_id"]]["code_value_max"] = row["code_value_max"]
        hosts_mapping_dict[row["c_id"]][row["s_id"]]["project_value_min"] = row["project_value_min"]
        hosts_mapping_dict[row["c_id"]][row["s_id"]]["project_value_max"] = row["project_value_max"]
        hosts_mapping_dict[row["c_id"]][row["s_id"]]["s_min"] = row["s_min"]
        hosts_mapping_dict[row["c_id"]][row["s_id"]]["s_max"] = row["s_max"]
        hosts_mapping_dict[row["c_id"]][row["s_id"]]["offset"] = row["offset"]

    print(hosts_mapping_dict)
    CacheDict=hosts_mapping_dict

    return CacheDict

#main()