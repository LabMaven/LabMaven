package com.tide.ailab.model;

/**
 * 数据字典
 * 
 * @author User
 */
public class DictInfo {

	private Integer dictId;

	private Integer dictCode;

	private String colName;

	private String dictNote;

	private String remark;

	public Integer getDictId() {
		return dictId;
	}

	public void setDictId(Integer dictId) {
		this.dictId = dictId;
	}

	public Integer getDictCode() {
		return dictCode;
	}

	public void setDictCode(Integer dictCode) {
		this.dictCode = dictCode;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getDictNote() {
		return dictNote;
	}

	public void setDictNote(String dictNote) {
		this.dictNote = dictNote;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
