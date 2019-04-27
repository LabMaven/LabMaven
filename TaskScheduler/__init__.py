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

LOGGER = modbus_tk.utils.create_logger("console")

workpath = os.path.dirname(os.path.abspath(sys.argv[0]))
sys.path.insert(0, os.path.join(workpath, 'modules'))


checkThread = {}
for DeviceId in ConfigDict.keys():
    # checkSingleDevice(DeviceInfo)
    checkThread[DeviceId] = Thread(target=checkSingleDevice, args=(DeviceId, ConfigDict[DeviceId],))
    checkThread[DeviceId].start()
