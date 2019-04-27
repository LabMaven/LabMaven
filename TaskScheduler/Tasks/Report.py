#!/usr/bin/python

import os
import sys
import time
import pymysql
import json
import datetime

WORK_PATH = os.path.dirname(os.path.realpath(__file__))
BASE_PATH = os.path.dirname(WORK_PATH)
CONF_CENT_PATH = os.path.join(os.path.dirname(BASE_PATH), "ConfigurationCenter")
if CONF_CENT_PATH not in sys.path:
    sys.path.append(CONF_CENT_PATH)

from ConfigurationCenter.ConfService import config_dict

running_flag = 0

def gen_report():
    print("Running gen_report")
    global running_flag
    if running_flag == 1:
        return False
    running_flag = 1

    retry_times = 1
    while retry_times < 10:
        retry_times += 1
        if retry_times >= 10:
            sys.exit()
        elif len(config_dict) > 0:
            break

        #print(retry_times)
        time.sleep(0.1)

    timestamp_str = time.strftime("%Y-%m-%d %H:%M:%S", time.gmtime(time.time() - 60))
    #print(timestamp_str)
    #sys.exit()
    get_data_sql = "select * from e_data where input_time < '%s' order by input_time desc;" % (timestamp_str)
    print(get_data_sql)

    try:
        conn = pymysql.connect(host=config_dict["mysql"]["host"], user=config_dict["mysql"]["user"],
                               password=config_dict["mysql"]["password"], db=config_dict["mysql"]["db"],
                               port=config_dict["mysql"]["port"], cursorclass = pymysql.cursors.DictCursor)
        cursor = conn.cursor()

        cursor.execute(get_data_sql)
        data_dict = cursor.fetchall()
        #print(json.dumps(data_dict, indent=4, sort_keys=True, separators=(',', ':')))
        #print(data_dict)
        data_sid_set={}
        for row in data_dict:
            value = (row["input_time"], row["value"])
            if row["s_id"] not in data_sid_set:
                data_sid_set[row["s_id"]]=[]
            if row["input_time"].timestamp() % 300 != 0:
                continue
            data_sid_set[row["s_id"]].append(value)

        for sid in sorted(data_sid_set.keys()):
            #table_name_sql = 'select TABLE_NAME, table_type from information_schema.tables where table_schema="ailab"'
            table_name = "e_data_%s_300" % (sid)
            table_name_sql = 'select TABLE_NAME from information_schema.tables where TABLE_NAME="%s"' % (table_name)
            cursor.execute(table_name_sql)
            talbe_names = cursor.fetchall()
            if len(talbe_names) == 0:
                create_table_sql = "CREATE TABLE %s ( input_time DATETIME, value FLOAT(10 , 4 )) " % (table_name) + \
                                "PARTITION BY RANGE (YEAR(input_time)) " + \
                                "(PARTITION p2019 VALUES LESS THAN (2020) , " + \
                                "PARTITION p2020 VALUES LESS THAN (2021) , " +  \
                                "PARTITION p2021 VALUES LESS THAN (2022) , " + \
                                "PARTITION p2022 VALUES LESS THAN (2023) , " + \
                                "PARTITION p2023 VALUES LESS THAN (2024)); "
                #print(create_table_sql)
                cursor.execute(create_table_sql)

            inserts_sql = "INSERT INTO %s VALUES(%%s,%%s)" % (table_name)
            cursor.executemany(inserts_sql, data_sid_set[sid])
            conn.commit()
    except():
        pass
    finally:
        running_flag = 0

    print("gen_report in Report")

if __name__ == "__main__":
    gen_report()