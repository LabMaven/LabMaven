package com.tide.ailab.common.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.google.common.base.CaseFormat;
import com.tide.ailab.common.log.Logger;

/**
 * 阿里云OSS存储服务，用于存取数据
 */
@Service
@PropertySource(value = "classpath:config.properties") //配置文件路径
public class OssStorageService {

    @Value("${accessKeyId}")
    private String accessKeyId;

    @Value("${accessKeySecret}")
    private String accessKeySecret;

    @Value("${regionId}")
    private String regionId;

    @Value("${endpoint}")
    private String endpoint;

    @Value("${bucketName}")
    private String bucketName;

    /**
     * 调用阿里云API向OSS存储数据
     * @param files 文件列表
     * @param destDir 存在阿里云的路径
     * @return
     */
    public void putFiles(List<File> files, String destDir) {
        // 初始化oss客户端
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                return;
            }

            for (File file : files) {
                String key = destDir + File.separator + file.getName();
                key = key.replaceAll("\\\\", "/");
                ossClient.putObject(bucketName, key, file);
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 调用阿里云API向OSS存储数据
     * @param files 文件map
     * @param destDir 存在阿里云的路径
     * @return
     */
    public Map<String, String> putFiles(Map<String, MultipartFile> files, String destDir) {

        // 初始化oss客户端
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        Map<String, String> urlMap = new HashMap<String, String>();
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                return urlMap;
            }

            for (Entry<String, MultipartFile> m : files.entrySet()) {
                //文件名处理
                String itemName = m.getKey();
                MultipartFile file = m.getValue();
                String originalFilename = file.getOriginalFilename();
                String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String fileName = itemName + "." + suffix;
                String key = destDir + File.separator + fileName;
                key = key.replaceAll("\\\\", "/");
                //上传oss
                ossClient.putObject(bucketName, key, file.getInputStream());
                urlMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, itemName),
                        String.format("http://%s.%s/%s", bucketName, endpoint, key));
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        } finally {
            ossClient.shutdown();
        }

        return urlMap;
    }
}
