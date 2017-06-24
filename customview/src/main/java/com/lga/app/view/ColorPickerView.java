package com.lga.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

public class ColorPickerView extends View {

	private static final int PICKER_WIDTH = 6;
	private static final int PICKER_HEIGHT = 24;
	private static final int PALETTE_WIDTH = 200;
	private static final int PALETTE_HEIGHT = PICKER_HEIGHT  / 2;

	/**x滑动精度*/
	private int mPrecisionX;
	/**y滑动精度*/
	private int mPrecisionY;
	/**全局缺省宽*/
	private int mWidth;
	/**全局缺省高*/
	private int mHeight;
	/**画笔缺省宽*/
	private int mPickerWidth;
	/**画板缺省高*/
	private int mPaletteHeight;

	/**背景框*/
	private Rect mRectBack;
	/**圆角拾色器框*/
	private RectF mRectFPicker;
	/**画板框*/
	private Rect mRectPalette;

	/**背景画笔*/
	private Paint mPaintBack;
	/**画板画笔*/
	private Paint mPaintPalette;
	/**拾色器画笔*/
	private Paint mPaintPicker;

	private int[] mHue = new int[361];

	/**拾色器进度（0~100）*/
	private int mProgress = 50;
	/**当前颜色*/
	private int mColor = Color.rgb(0, 255, 255);

	private boolean isDragging;

	private OnColorChangedListener mListener;

	public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		init(context);
	}

	private void init(Context context) {
		mPrecisionX = ViewConfiguration.get(context).getScaledTouchSlop();
		mPrecisionX = dp2Px(mPrecisionX);

		mPrecisionY = mPrecisionX / 2;

		mWidth = dp2Px(PALETTE_WIDTH) + mPrecisionX * 2;
		mHeight = dp2Px(PICKER_HEIGHT) + mPrecisionY * 2;

		mPickerWidth = dp2Px(PICKER_WIDTH);
		mPaletteHeight = dp2Px(PALETTE_HEIGHT);

		mPaintBack = new Paint();
		mPaintBack.setStyle(Paint.Style.STROKE);
		mPaintBack.setStrokeWidth(dp2Px(1));
		mPaintBack.setColor(Color.TRANSPARENT);

		mPaintPalette = new Paint();

		mPaintPicker = new Paint();
		mPaintPicker.setColor(Color.WHITE);
	}

	public ColorPickerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColorPickerView(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getMeasuredWidth(widthMeasureSpec), getMeasuredHeight(heightMeasureSpec));
	}

	private int getMeasuredWidth(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if(specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			int width = mWidth;
			result = width + getPaddingLeft() + getPaddingRight();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int getMeasuredHeight(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			int height = mHeight;
			result = height + getPaddingTop() + getPaddingBottom();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mRectBack == null) {
			mWidth = getWidth();
			mHeight = getHeight();
			mRectBack = new Rect(0, 0, mWidth, mHeight);
			mRectFPicker = new RectF(new Rect(mPrecisionX - mPickerWidth / 2, mPrecisionY, mPrecisionX + mPickerWidth / 2, mHeight - mPrecisionY));
			mRectPalette = new Rect(mPrecisionX, (mHeight - mPaletteHeight) / 2, mWidth - mPrecisionX, (mHeight + mPaletteHeight) / 2);

			/**颜料*/
			LinearGradient shaderPalette = new LinearGradient(
					mRectPalette.left,
					mRectPalette.top,
					mRectPalette.right,
					mRectPalette.bottom,
					buildHueColorArray(),
					null,
					TileMode.CLAMP
			);

			mPaintPalette.setShader(shaderPalette);
		}

		canvas.drawRect(mRectBack, mPaintBack);
		canvas.drawRect(mRectPalette, mPaintPalette);

		refreshPickerLocation(mProgress);
		canvas.drawRoundRect(mRectFPicker, 8, 8, mPaintPicker);
	}

	private int[] buildHueColorArray() {
		for (int i = 0; i < mHue.length; i++) {
			mHue[i] = Color.HSVToColor(new float[]{i, 1f, 1f});
		}
		return mHue;
	}

	private void refreshPickerLocation(int progress) {
		float x;
		x = progress / 100f * mRectPalette.width() + mRectPalette.left;

		mRectFPicker.left = (int) (x - mRectFPicker.width() / 2);
		mRectFPicker.right = mRectFPicker.left + mPickerWidth;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isDragging = isInPicker(x, y);
				if(isDragging) {
					setOnStartTrackingTouch();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(isDragging && isInPallete(x)) {
					attemptClaimDrag();
					if(x < mRectPalette.left) {
						x = mRectPalette.left;
					} else if(x > mRectPalette.right) {
						x = mRectPalette.right;
					}

					mProgress = (int) ((x - mRectPalette.left) / mRectPalette.width() * 100);

					int h = Math.round(mProgress / 100f * mHue.length);
					mColor = Color.HSVToColor(new float[] {h, 1f, 1f});
					setColorChangedListener();

					invalidate();
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				setOnStopTrackingTouch();
				break;
		}
		return true;
	}

	private boolean isInPicker(float x, float y) {
		return (mRectFPicker.left - mPrecisionX <= x && x <= mRectFPicker.right + mPrecisionX
				&& mRectFPicker.top - mPrecisionY <= y && y <= mRectFPicker.bottom + mPrecisionY);
	}

	private boolean isInPallete(float x) {
		return (mRectFPicker.left - mPrecisionX <= x && x <= mRectFPicker.right + mPrecisionX);
	}

	private void attemptClaimDrag() {
		ViewParent parent = getParent();
		if(parent != null) {
			parent.requestDisallowInterceptTouchEvent(true);
		}
	}

	public void setProgress(int progress) {
		mProgress = progress;
	}

	public int getProgress() {
		return mProgress;
	}

	public int getColor() {
		int h = Math.round(mProgress / 100f * mHue.length);
		mColor = Color.HSVToColor(new float[] {h, 1f, 1f});
		return mColor;
	}

	public void setColor(int color) {
		mColor = color;

		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		mProgress = (int) (hsv[0] / mHue.length * 100);
	}

	public void setOnColorChangedListener(OnColorChangedListener listener) {
		mListener = listener;
	}

	private void setOnStartTrackingTouch() {
		if(mListener != null) {
			mListener.onStartTrackingTouch(this);
		}
	}

	private void setColorChangedListener() {
		if(mListener != null) {
			mListener.onColorChanged(this, mColor, mProgress, true);
		}
	}

	private void setOnStopTrackingTouch() {
		if(mListener != null) {
			mListener.onStopTrackingTouch(this, mColor, mProgress);
		}
	}

	private int dp2Px(int value) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
	}
}
