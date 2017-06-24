package com.lga.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

/**
 * 调色板
 * @author xiaojie
 */
public class ImagePalette extends View {

	private static final int PICKER_WIDTH = 6;
	private static final int PICKER_HEIGHT = 24;
	private static final int PALETTE_WIDTH = 200;
	private static final int PALETTE_HEIGHT = PICKER_HEIGHT / 2;

	/**x滑动精度*/
	private int mPrecisionX;
	/**y滑动精度*/
	private int mPrecisionY;
	/**全局缺省宽*/
	private int mWidth;
	/**全局缺省高*/
	private int mHeight;
	/**拾色器缺省宽*/
	private int mPickerWidth;
	/**画板缺省高*/
	private int mPaletteHeight;

	/**调色板资源*/
	private Bitmap mBitmapPaletteSrc;
	/**拉伸后的调色板资源*/
	private Bitmap mBitmapPalette;

	/**背景框*/
	private Rect mRectBack;
	/**圆角拾色器框*/
	private RectF mRectFPicker;
	/**画板框*/
	private Rect mRectPalette;

	/**背景画笔*/
	private Paint mPaintBack;
	/**拾色器画笔*/
	private Paint mPaintPicker;

	/**拾色器进度（0~100），需要配合mColor预设一个值*/
	private int mProgress = 50;
	/**当前颜色，需要配合mProgress预设一个颜色*/
	private int mColor = Color.WHITE;

	private boolean isDragging;

	private OnColorChangedListener mListener;

	public ImagePalette(Context context) {
		this(context, null);
	}

	public ImagePalette(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ImagePalette(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.palette);
		Drawable drawablePalette = typedArray.getDrawable(R.styleable.palette_src);
		typedArray.recycle();

		if(drawablePalette == null) {
			drawablePalette = context.getResources().getDrawable(R.drawable.ic_palette);
		}
		mBitmapPaletteSrc = ((BitmapDrawable) drawablePalette).getBitmap();

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

		mPaintPicker = new Paint();
		mPaintPicker.setColor(Color.WHITE);
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

			mBitmapPalette = Bitmap.createScaledBitmap(mBitmapPaletteSrc, mRectPalette.width(), mRectPalette.height(), true);
		}

		canvas.drawRect(mRectBack, mPaintBack);
		canvas.drawBitmap(mBitmapPalette, null, mRectPalette, null);

		refreshPickerLocation(mProgress);
		canvas.drawRoundRect(mRectFPicker, 8, 8, mPaintPicker);
	}

	private void refreshPickerLocation(int progress) {
		float x = progress / 100f * mRectPalette.width() + mRectPalette.left;

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
					} else if(x >= mRectPalette.right) {
						x = mRectPalette.right - 1;
						mProgress = 100;
					} else {
						mProgress = (int) ((x - mRectPalette.left) / mRectPalette.width() * 100);
					}
					mColor = mBitmapPalette.getPixel((int) x - mRectPalette.left, mBitmapPalette.getHeight() / 2);
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

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int progress) {
		mProgress = progress;
	}

	public int getColor() {
		if(mRectPalette != null) {
			float x = mProgress / 100f * mRectPalette.width() + mRectPalette.left;
			mColor = mBitmapPalette.getPixel((int) x, mBitmapPalette.getHeight() / 2);
		}
		return mColor;
	}

	public void setColor(int color) {
		mColor = color;

		for(int i = 0; i < mRectPalette.width(); i++) {
			int tmpColor = mBitmapPalette.getPixel(i, mBitmapPalette.getHeight() / 2);
			if(tmpColor == color) {
				mProgress = (int) (i * 1.0f / mRectPalette.width() * 100);
				break;
			}
		}
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
