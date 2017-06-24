package com.lga.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.lga.util.SizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay.X
 *
 * 波纹效果的view
 */
public class RippleView extends View {

    private int inside_radius = 12;
    private Paint paint;
    private int maxRadius = 255;
    // 是否运行
    private boolean isStarting = false;
    private List<Integer> alphaList = new ArrayList<>();
    private List<Integer> startRadiusList = new ArrayList<>();

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inside_radius = SizeUtil.dp2px(context, inside_radius);
        paint = new Paint();
        // 设置波纹的颜色
        paint.setColor(0x0059ccf5);
        alphaList.add(255);// 圆心的不透明度
        startRadiusList.add(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredSize(widthMeasureSpec), getMeasuredSize(heightMeasureSpec));
    }

    private int getMeasuredSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            int width = (maxRadius + inside_radius) * 2;
            result = width + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明
        // 依次绘制 同心圆
        for (int i = 0; i < alphaList.size(); i++) {
            int alpha = alphaList.get(i);
            // 圆半径
            int startWidth = startRadiusList.get(i);
            paint.setAlpha(alpha);
            // 这个半径决定你想要多大的扩散面积
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, startWidth + inside_radius, paint);
            // 同心圆扩散
            if (isStarting && alpha > 0 && startWidth < maxRadius) {
                alphaList.set(i, alpha - 1);
                startRadiusList.set(i, startWidth + 1);
            }
        }

        // 控制扩散环的半径
        if (isStarting && startRadiusList.get(startRadiusList.size() - 1) == maxRadius / 5) {
            alphaList.add(255);
            startRadiusList.add(0);
        }
        // 同心圆数量达到10个，删除最外层圆
        if (isStarting && startRadiusList.size() == 6) {
            startRadiusList.remove(0);
            alphaList.remove(0);
        }
        // 刷新界面
        invalidate();
    }

    // 执行动画
    public void start() {
        isStarting = true;
    }

    // 停止动画
    public void stop() {
        isStarting = false;
    }

    // 判断是都在不在执行
    public boolean isStarting() {
        return isStarting;
    }
}
