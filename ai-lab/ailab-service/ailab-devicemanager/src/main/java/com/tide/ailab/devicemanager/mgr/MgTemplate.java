package com.tide.ailab.devicemanager.mgr;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tide.ailab.common.util.DateTimeUtil;

@Component
public class MgTemplate {
	
	 @Autowired
	    private MongoClient mongoClient;

	    public MongoCollection<Document> getCurMonthDeviceTable() {
	        String month = DateTimeUtil.getMonthDateString();
	        return getLogDatabase().getCollection("device" + month);
	    }

	    public MongoDatabase getLogDatabase() {
	        return mongoClient.getDatabase("devinfo");
	    }

}
