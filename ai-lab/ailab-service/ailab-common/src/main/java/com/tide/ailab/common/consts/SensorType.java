package com.tide.ailab.common.consts;

public enum SensorType {

	PRESURE_SENSOR(1, "pressure", "压力传感器"), TEMPERATURE_SENSOR(2, "temperature", "温度传感器"), HUMIDITY_SENSOR(3,
			"humidity", "湿度传感器"), VENTILATOR(4, "ventilator", "通风柜门高"), WORK_STATE(5, "workState",
					"人员工作状态"), AIR_SPEED(6, "airSpeed", "面风速"), AIR_VOLUMN(7, "airVolumn", "余风量"), FLYGATE(8, "flygate",
							"变风量送风蝶阀风量"), sensor_9(9, "sensor9", "排风管道静压"), sensor_10(10, "sensor10",
									"风机-运行/停止"), sensor_11(11, "sensor11", "风机-手动/自动"), sensor_12(12, "sensor12",
											"风机状态-正常/报警"), sensor_13(13, "sensor13", "防火阀连锁");

	private int type;

	private String sensorClass;

	private String message;

	private SensorType(int type, String sensorClass, String message) {
		this.type = type;
		this.sensorClass = sensorClass;
		this.message = message;
	}

	public static String getSensorClass(int type) {
		switch (type) {
		case 1:
			return "pressure";
		case 2:
			return "temperature";
		case 3:
			return "humidity";
		case 4:
			return "ventilator";
		case 5:
			return "workState";
		case 6:
			return "airSpeed";
		case 7:
			return "airVolumn";
		case 8:
			return "flygate";
		default:
			return "unknown";
		}
	}

	public static String getMessage(int type) {
		switch (type) {
		case 1:
			return "压力传感器";
		case 2:
			return "温度传感器";
		case 3:
			return "湿度传感器";
		case 4:
			return "通风柜门高";
		case 5:
			return "人员工作状态";
		case 6:
			return "面风速";
		case 7:
			return "余风量";
		case 8:
			return "变风量送风蝶阀风量";
		default:
			return "未知";
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSensorClass() {
		return sensorClass;
	}

	public void setSensorClass(String sensorClass) {
		this.sensorClass = sensorClass;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
