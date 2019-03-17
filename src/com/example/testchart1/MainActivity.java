package com.example.testchart1;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	MYChart chart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		chart = (MYChart) findViewById(R.id.chart);

		String[] xvalues = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
		int[] yvalues = new int[] { 1, 2, 1, 9, 6, 3, 4 };
		chart.setValues(xvalues, yvalues);

	}
}
