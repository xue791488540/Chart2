package com.example.testchart1;

import java.io.Serializable;

public class TimeData implements Serializable {
	private static final long serialVersionUID = -977324640952747570L;

	private long time;// ∫¡√Î
	private float value;// ÷µ

	public TimeData(long time, float value) {
		super();
		this.time = time;
		this.value = value;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}



}
