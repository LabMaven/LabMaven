#!/usr/bin/python
# -*- coding: UTF-8 -*-.
__author__ = 'sammyjeep'

import os
import json
import time
from threading import Thread

WORK_PATH = os.path.dirname(os.path.realpath(__file__))
CONF_PATH = os.path.join(WORK_PATH, "conf")

config_dict = {}
last_loaded = 0

def load_conf_files():
    global config_dict
    #global last_loaded
    while True:
        print("load_conf_files while true")
        for file_name in os.listdir(CONF_PATH):
            file_path = os.path.join(CONF_PATH, file_name)
            with  open(file_path, 'r') as file_handler:
                config_dict.update(json.load(file_handler))
        print(config_dict)
        time.sleep(5)
        #last_loaded = time.time()
#
# def get_config():
#     global config_dict
#     cur_loaded = time.time()
#     # print(config_dict)
#     # print(cur_loaded)
#     # print(last_loaded)
#     if len(config_dict.keys()) == 0 or cur_loaded - last_loaded > 5:
#         load_conf_files()
#         # print(json.dumps(config_dict, sort_keys=True, indent=4, separators=(',', ':')))

confThread = Thread(target=load_conf_files, args=())
confThread.setDaemon(True)
confThread.start()
print("")