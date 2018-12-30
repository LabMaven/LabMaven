package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.DictInfo;

/**
 * 字典数据原子操作
 * @author User
 */
@Repository
public interface DictDao {

    /**
     * 查询所有的字典数据
     * @param dictId
     * @return
     */
    List<DictInfo> findAll();

    /**
     * 根据code和colname查询字典表
     * @param dictCode 编码
     * @param colName 枚举值类型
     * @return 字典对象
     */
    public DictInfo findByCodeAndCName(String dictCode, String colName);

    /**
     * 获取所有枚举值
     */
    public List<DictInfo> getAllDict();

    /**
     * 获取所有枚举值类型
     */
    public List<String> getColName();

    /**
     * 根据ColName查询
     * @param colName
     * @return
     */
    public List<DictInfo> getByColName(String colName);

}
