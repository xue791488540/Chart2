package com.example.testchart1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * ��״ͼ
 */
public class MYChart extends View implements OnTouchListener {
	public static String Tag = "MYHistogram";

	private int width;// �߶�
	private int height;// ���
	private Paint PBar;// ��״����
	private Paint pGoal;// Ŀ����
	private Paint pCircle;// СȦ
	private Paint pInaxle;// ������
	private Paint pLable;// ����

	private int[] color_bar;// ��״����ɫ
	private int color_goal;// Ŀ������ɫ
	private int color_circle;// СȦ��ɫ
	private int color_inaxle;// ��������ɫ
	private int color_lable;// ������ɫ

	private int[] Yvalues;// Y��
	private PointF[] YPoints;// Y���Ӧ����
	private float max;// Y�����ֵ
	private float min;// Y����Сֵ
	private String[] Xvalues;// X��
	private float bar_width;// ����

	private int showIndicateIndex = -1;

	private int padLeft, padRight, padTop, padBot;
	private int lable_txt_size;
	/**
	 * Ŀ��
	 */
	private float goal;
	private float Dgoal;// Ŀ���Ӧ����

	public MYChart(Context context) {
		super(context);
		init();
	}

	public MYChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MYChart(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(0xff27a6e9);
		if (YPoints == null) {
			return;
		}

		int d = 5;// Y��̶���
		int iy = (height - padTop - padBot) / d;// Y��̶ȼ��
		// ��X����
		canvas.drawLine(padLeft, height - padBot, width - padRight, height - padBot, pInaxle);
		// ��������
		for (int i = 0; i < d; i++) {
			canvas.drawLine(padLeft, i * iy + padTop, width - padRight, i * iy + padTop, pInaxle);
		}

		for (int i = 0; i < YPoints.length; i++) {
			/* ������ */
			PBar.setShader(new LinearGradient(YPoints[i].x, YPoints[i].y, YPoints[i].x, height - padBot, color_bar, null, TileMode.MIRROR));
			canvas.drawRect(YPoints[i].x - bar_width / 2, YPoints[i].y, YPoints[i].x + bar_width / 2, height - padBot, PBar);

			/* ������ֵ */
			float textWidth = pLable.measureText(Yvalues[i] + "") / 2;
			canvas.drawText(Yvalues[i] + "", YPoints[i].x - textWidth, YPoints[i].y - lable_txt_size / 3, pLable);

			/* ��X���� */
			if (Xvalues.length == 24) {
				if (i % 6 != 0) {
					continue;
				}
			}
			if (Xvalues.length >= 28) {
				if ((i + 1) % 5 != 0) {
					continue;
				}
			}
			textWidth = pLable.measureText(Xvalues[i]) / 2;
			canvas.drawText(Xvalues[i], YPoints[i].x - textWidth, height - padBot + lable_txt_size * 4 / 3, pLable);
		}

		// ��Ŀ����
		if (goal != 0) {
			canvas.drawLine(padLeft, Dgoal, width - padRight, Dgoal, pGoal);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = measureWidth(widthMeasureSpec);
		height = measureHeight(heightMeasureSpec);

		initData();
	}

	private void init() {
		this.setOnTouchListener(this);

		lable_txt_size = dip2px(getContext(), 10);

		color_bar = new int[] { Color.parseColor("#55d3c4"), Color.parseColor("#cbeee9") };
		PBar = new Paint();
		PBar.setAntiAlias(true);

		color_inaxle = Color.GRAY;
		pInaxle = new Paint();
		pInaxle.setColor(color_inaxle);

		color_lable = Color.GRAY;
		pLable = new Paint();
		pLable.setColor(color_lable);
		pLable.setTextSize(lable_txt_size);
		pLable.setAntiAlias(true);

		color_circle = Color.parseColor("#3ca73b");
		pCircle = new Paint();
		pCircle.setColor(color_circle);
		pCircle.setStyle(Paint.Style.FILL);
		pCircle.setStrokeWidth(2);
		pCircle.setAntiAlias(true);

		color_goal = Color.parseColor("#3ca73b");
		pGoal = new Paint();
		pGoal.setColor(color_goal);
		pGoal.setStyle(Paint.Style.STROKE);
		pGoal.setStrokeWidth(6);

		padTop = lable_txt_size * 3 / 2;
		padBot = padTop;
		padRight = lable_txt_size / 2;
		padLeft = padRight;
	}

	private void initData() {
		if (Yvalues == null) {
			return;
		}

		/* ����Y�������Сֵ */
		max = Yvalues[0];
		min = Yvalues[0];
		for (float i : Yvalues) {
			if (max < i) {
				max = i;
			}
			if (min > i) {
				min = i;
			}
		}
		if (goal != 0 && max < goal) {
			max = (float) (goal * 1.1);
		}
		if (max == 0) {
			max = 1000;
		}
		max = formatInt(max);
		/* ����Y�� */
		YPoints = new PointF[Yvalues.length];
		float scal = (height - padTop - padBot) / (float) max;
		float ix = (float) (width - padLeft - padRight) / YPoints.length;
		bar_width = ix * 2 / 5;
		for (int i = 0; i < Yvalues.length; i++) {
			float x = i * ix + padLeft + ix / 2;
			YPoints[i] = new PointF(x, (int) (height - Yvalues[i] * scal - padBot));
		}
		/* ����Ŀ��Y�� */
		Dgoal = (int) (height - goal * scal - padBot);
	}

	private int measureHeight(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	private int measureWidth(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				float xTuch = event.getX();
				for (int i = 0; i < YPoints.length; i++) {
					if (xTuch < (YPoints[i].x + bar_width / 2) && xTuch > (YPoints[i].x - bar_width / 2)) {
						showIndicateIndex = i;
						break;
					} else {
						showIndicateIndex = -1;
					}
				}
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				xTuch = event.getX();
				for (int i = 0; i < YPoints.length; i++) {
					if (xTuch < (YPoints[i].x + bar_width / 2) && xTuch > (YPoints[i].x - bar_width / 2)) {
						showIndicateIndex = i;
						break;
					} else {
						showIndicateIndex = -1;
					}
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				showIndicateIndex = -1;
				invalidate();
				break;
			}

		}
		return true;
	}

	public void setValues(String[] xvalues, int[] yvalues) {
		Xvalues = xvalues;
		Yvalues = yvalues;
	}

	public void setGoal(float goal) {
		this.goal = goal;
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * ȡ�� ������λ
	 * 
	 * @param num
	 * @return
	 */
	public static int formatInt(float num) {
		int b = (int) Math.ceil(num);
		int l = (b + "").length();
		if (l >= 2) {
			l = l - 1;
		}
		int d = (int) Math.pow(10, (l - 1));
		num = num / d;
		b = (int) Math.ceil(num);
		b = b * d;
		return b;
	}

}
