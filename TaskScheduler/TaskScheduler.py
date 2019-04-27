#!/usr/bin/python

import os, sys
import time

WORK_PATH = os.path.dirname(os.path.realpath(__file__))
TASK_PATH = os.path.join(WORK_PATH, "Tasks")
if TASK_PATH not in sys.path:
    sys.path.append(TASK_PATH)

from threading import Thread
from Tasks.Report import *

TASK_INTERVAL = 60

taskThread = {}

Task_Dict = {
    "gen_report" : {
        "function" : "gen_report",
    }
}

#def gen_report():
#    print("gen_report in main")

#tmp_dict = copy.deepcopy(globals())
#for i in globals():
#    print(type(i))
while True:
    for task_id in Task_Dict.keys():
        func = globals()[Task_Dict[task_id]["function"]]
        taskThread[task_id] = \
            Thread(target=func, args=())
        taskThread[task_id].setDaemon(True)
        taskThread[task_id].start()
    time.sleep(TASK_INTERVAL)


