package com.toocms.dink5.mylibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */

public class LinerView extends View {
    public LinerView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public LinerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public LinerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        init();

    }

    public LinerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
    }

    //-------------View相关-------------
//View自身的宽和高
    private int mHeight;
    private int mWidth;

    //-------------统计图相关-------------
//x轴的条目
    private int xNum = 6;
    //y轴的条目
    private int yNum = 5;
    //y轴条目之间的距离
    private int ySize = AutoUtils.getPercentHeightSize(100);
    //x轴条目之间的距离
    private int xSize = AutoUtils.getPercentHeightSize(100);
    //y轴的长度,11个条目只有10段距离
    private int yLastSize = (yNum) * ySize;

//    //-------------必须给的资源相关-------------
//    private String[] xData = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
//    private String[] yData = new String[]{"0", "10", "20", "30", "40", "50"};
//    private String str = "项目完成的进度（单位%）";
//    //折线表示的最大值,取yData的最大值
//    private int maxData = Integer.parseInt(yData[yData.length - 1]);
//    //折线真实值
//    private int[] yValue = new int[]{8, 20, 42, 12, 30, 2, 50};


    private float maxData = 0;
    private ArrayList<Float> dataList = new ArrayList<>();
    private ArrayList<String> yData = new ArrayList<>();
    private ArrayList<String> xData = new ArrayList<>();
    //-------------画笔相关-------------
//边框的画笔
    private Paint borderPaint;
    private Paint borderPaint2;
    //文字的画笔
    private Paint textPaint;
    //折线的画笔
    private Paint linePaint;
    //黑点的画笔
    private Paint pointPaint;

    //-------------颜色相关-------------
//边框颜色
    private int mColor = 0xFF888888;
    //文字颜色
    private int textColor = 0xFF888888;
    //折线颜色
    private int lineColor = 0xFF000000;
    //黑点颜色
    private int pointColor = 0xFF000000;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void init() {
//        xData.clear();
        dataList.clear();
        yData.clear();
        maxData = 0;

        dataList.add(0.0f);
        dataList.add(0.0f);
        dataList.add(0.0f);
        dataList.add(0.0f);
        dataList.add(0.0f);
        dataList.add(0.0f);
        dataList.add(0.0f);

        yData.add("0");
        yData.add("1");
        yData.add("2");
        yData.add("3");
        yData.add("4");
        yData.add("5");
//        xData.add("01日");
//        xData.add("02日");
//        xData.add("03日");
//        xData.add("04日");
//        xData.add("05日");
//        xData.add("06日");
//        xData.add("07日");

        maxData = 1.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //初始化画笔
        initPaint();
        //画布移到左下角，留出100的空间给予文字填充
        canvas.translate(AutoUtils.getPercentHeightSize(100), mHeight - AutoUtils.getPercentHeightSize(100));
        //画边框
        drawBorder(canvas);
        //画黑点
        drawPoint(canvas);
        //画文字
        drawText(canvas);
        //画折线
        drawLine(canvas);
        drawPointText(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置宽高
        setMeasuredDimension(widthMeasureSpec, AutoUtils.getPercentHeightSize(650));
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //边框画笔
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(0xffD4D4D5);
        borderPaint.setStrokeWidth((float) AutoUtils.getPercentWidthSize(2));
        //边框画笔
        borderPaint2 = new Paint();
        borderPaint2.setAntiAlias(true);
        borderPaint2.setStyle(Paint.Style.STROKE);
        borderPaint2.setColor(0xff08B0FD);
        borderPaint2.setStrokeWidth((float) AutoUtils.getPercentWidthSize(5));
        //文字画笔
        textPaint = new Paint();
        textPaint.setTextSize(AutoUtils.getPercentWidthSizeBigger(25));
        textPaint.setColor(0xff000000);
        textPaint.setAntiAlias(true);
        //区域画笔
        linePaint = new Paint();
        linePaint.setColor(0xffFA7C20);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth((float) AutoUtils.getPercentWidthSize(3));
        //黑点画笔
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(0xffff0000);
    }

    /**
     * 画边框
     *
     * @param canvas
     */
    private void drawBorder(Canvas canvas) {
        Path path = new Path();

        path.moveTo(0, 0);
        path.lineTo(xNum * xSize, 0);
        canvas.drawPath(path, borderPaint2);

        for (int i = 1; i <= yNum; i++) {
            path.reset();
            path.moveTo(0, -i * ySize);
            path.lineTo(xNum * xSize, -i * ySize);
            canvas.drawPath(path, borderPaint);
        }
        for (int i = 1; i <= xNum; i++) {
            path.reset();
            path.moveTo(i * xSize, 0);
            path.lineTo(i * xSize, -yNum * ySize);
            canvas.drawPath(path, borderPaint);
        }
        path.reset();
        path.moveTo(0, 0);
        path.lineTo(0, -yNum * ySize);
        canvas.drawPath(path, borderPaint2);
    }

    /**
     * 画黑点
     *
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
        for (int i = 0; i <= xNum; i++) {
            canvas.drawCircle(i * xSize, 0, 5, pointPaint);
        }
    }

    /**
     * 画文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        //事先说明：文字排版为了好看，这里的20，都为20px的边距
        //x轴的文字
        for (int i = 0; i < xData.size(); i++) {
            //测量文字的宽高
            Rect rect = new Rect();
            textPaint.getTextBounds(xData.get(i), 0, xData.get(i).length(), rect);
            float textWidth = rect.width();
            float textHeight = rect.height();
            canvas.drawText(xData.get(i), i * xSize - textWidth / 2, textHeight + 20, textPaint);
        }
        //y轴的文字
        for (int i = 0; i < yData.size(); i++) {
            //测量文字的宽高
            Rect rect = new Rect();
            textPaint.getTextBounds(yData.get(i), 0, yData.get(i).length(), rect);
            float textWidth = rect.width();
            float textHeight = rect.height();
            canvas.drawText(yData.get(i), -textWidth - 20, i * (-ySize) + (textHeight / 2), textPaint);
        }
        //顶部文字
//        canvas.drawText(str, 0, (-ySize) * (yData.length - 1) - 20, textPaint);
    }

    /**
     * 画折线
     *
     * @param canvas
     */
    private List<Float> point = new ArrayList<>();

    private void drawLine(Canvas canvas) {
        Path path = new Path();
        point.clear();
        for (int i = 0; i < dataList.size(); i++) {
            //计算折线的位置：（当前点的值/最大值）拿到百分比percent
            //用百分比percent乘与y轴总长，就获得了折线的位置
            //这里拿到的百分比一直为0，所以换一种方法，先乘与总长再除与最大值，而且记得加上负号
            float position = -(dataList.get(i) * yLastSize / maxData);
            point.add(position);
            if (i == 0) {
                //第一个点需要移动
                path.moveTo(i * xSize, position);
            } else {
                //其余的点直接画线
                path.lineTo(i * xSize, position);
            }
            canvas.drawPath(path, linePaint);
            //画黑点
            canvas.drawCircle(i * xSize, position, 5, pointPaint);
        }
    }

    private void drawPointText(Canvas canvas) {
        for (int i = 0; i < dataList.size(); i++) {
            //测量文字的宽高
            String i1 = dataList.get(i) + "";
            Rect rect = new Rect();
            textPaint.getTextBounds(i1, 0, i1.length(), rect);
            float textWidth = rect.width();
            canvas.drawText(dataList.get(i) + "", i * xSize - textWidth / 2, point.get(i) - 10, textPaint);
        }
    }

    private int ttt;

    public void setData(ArrayList<Float> list, ArrayList<String> list2) {
        xData.clear();
        dataList.clear();
        yData.clear();
        maxData = 0;
        xData.addAll(list2);
        dataList.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > maxData) {
                maxData = list.get(i);
            }
        }
        if (maxData > 0) {
            ttt = (int) (maxData * 1000);
            int v1 = ttt % 5;
            int v2 = ttt / 5;
            if (v1 != 0) {
                if (v2 * 5 < ttt) {
                    ttt = v2 * 6;
                    maxData = ttt / 1000;
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            float i1 = (float) i * (ttt / 5) / 1000f;
            yData.add(i1 + "");
        }
        if (maxData == 0) {
            init();
        }
        invalidate();
    }
}
