package com.example.jpushdemo;

/*
	create table if not exists device(device_id varchar(32) NOT NULL unique,temperature int,pwr_led int,heat_led int,detecter1_temp int,detecter2_temp int,err_code int)CHARACTER SET utf8 COLLATE utf8_general_ci;
*/
public class Device {
	public String device_id;
	public int temperature;
	// 电源灯 value:0/1
	public int pwr_led;
	// 加热指示灯 value:0/1
	public int heat_led;
	// 探头1温度 
	public int detecter1_temp;
	// 探头2温度
	public int detecter2_temp;
	/*
		0 : 没有异常;
		1 : 高温异常;
		2 : 低温异常;
		3 : 无水;
		4 : 温度失控;
	*/
	public int err_code;
	public Device(String device_id, int temperature, int pwr_led, int heat_led, int detecter1_temp, int detecter2_temp, int err_code) {
		super();
		this.device_id = device_id;
		this.temperature = temperature;
		this.pwr_led = pwr_led;
		this.heat_led = heat_led;
		this.detecter1_temp = detecter1_temp;
		this.detecter2_temp = detecter2_temp;
		this.err_code = err_code;
	}
	
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getPwr_led() {
		return pwr_led;
	}
	public void setPwr_led(int pwr_led) {
		this.pwr_led = pwr_led;
	}
	public int getHeat_led() {
		return heat_led;
	}
	public void setHeat_led(int heat_led) {
		this.heat_led = heat_led;
	}
	public int getDetecter1_temp() {
		return detecter1_temp;
	}
	public void setDetecter1_temp(int detecter1_temp) {
		this.detecter1_temp = detecter1_temp;
	}
	public int getDetecter2_temp() {
		return detecter2_temp;
	}
	public void setDetecter2_temp(int detecter2_temp) {
		this.detecter2_temp = detecter2_temp;
	}
	public int getErr_code() {
		return err_code;
	}
	public void setErr_code(int err_code) {
		this.err_code = err_code;
	}

	@Override
	public String toString() {
		return "Device{" +
				"device_id='" + device_id + '\'' +
				", temperature=" + temperature +
				", pwr_led=" + pwr_led +
				", heat_led=" + heat_led +
				", detecter1_temp=" + detecter1_temp +
				", detecter2_temp=" + detecter2_temp +
				", err_code=" + err_code +
				'}';
	}
}
