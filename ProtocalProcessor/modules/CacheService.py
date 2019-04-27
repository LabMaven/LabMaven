#!/usr/bin/python
# -*- coding: UTF-8 -*-.
import pymysql
from LoggerService import LoggerService

#localLogger = LoggerService().getlog()

class CacheService:

    def __init__(cls):
        if not hasattr(cls, "CacheDict"):
            CacheService.CacheDict={}

    def instance(cls,*args, **kwargs):
        if not hasattr(cls, "_instance"):
            CacheService._instance = CacheService(*args, **kwargs)
        return CacheService._instance

    def loadcache(self):
        conn = pymysql.connect(host="localhost", user="root",password="1qaz!QAZ", db="ailab", port=3306)
        cursor = conn.cursor(cursor=pymysql.cursors.DictCursor)
        #localLogger.info(cursor)
        effect_row = cursor.execute("select * from c_define_dyn")

        host_cache_dict={}

        for row in cursor.fetchall():
            if row["s_id"] not in host_cache_dict:
                host_cache_dict[row["s_id"]]={}
            host_cache_dict[row["s_id"]]["alarm_status"]=row["alarm_status"]

        print("host_cache_dict=%s" % (host_cache_dict))
        CacheService.CacheDict=host_cache_dict

        return CacheService.CacheDict

    #main()
#cache1 = CacheService()
#cache1.loadcache()
#print(CacheService.CacheDict)

#cache2 = CacheService()
#print(CacheService.CacheDict)

