package top.guuguo.progress_lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import top.guuguo.progress_lib.R;
import top.guuguo.progress_lib.util.DensityUtil;

/**
 * Created by guodeqing on 16/4/5.
 */
public class MyProgressRoundBarView extends View {
    private Drawable icon;
    /**
     * id:&:R.color.
     */
    private Paint mPaint;
    private float mIntervalNone = 1;
    private float mInterval = 1;

    private int mProgress;
    private int mStartAngle;
    private int mEndAngle;
    private float mCenterTextSize;
    private int mProgressMax;
    private int mCenterTextColor;
    private int mFinishedColor;
    private float mProgressWidth;
    private int mUnfinishedColor;
    private String mCenterText;

//    private boolean isCamera;
    private Camera camera;
    private float mPadding = 30;

    private int mRotateProgress = 0;
    private Bitmap mWaveBitmap;
    private Bitmap mProgressBitmap;

    public float getmRotateProgress() {
        return mRotateProgress;
    }

    public void setmRotateProgress(int mRotateProgress) {
        if (100 - mRotateProgress >= 2)
            if (Math.abs(this.mRotateProgress - mRotateProgress) <= 2) return;
        this.mRotateProgress = mRotateProgress;
        refreshImage();
    }

    public int getmProgressMax() {
        return mProgressMax;
    }

    public void setmProgressMax(int mProgressMax) {
        this.mProgressMax = mProgressMax;
    }

    public int getmProgress() {
        return mProgress;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
    }

    public int getmCenterTextColor() {
        return mCenterTextColor;
    }

    public void setmCenterTextColor(int mCenterTextColor) {
        this.mCenterTextColor = mCenterTextColor;
    }

    public int getmFinishedColor() {
        return mFinishedColor;
    }

    public void setmFinishedColor(int mFinishedColor) {
        this.mFinishedColor = mFinishedColor;
    }

    public int getmUnfinishedColor() {
        return mUnfinishedColor;
    }

    public void setmUnfinishedColor(int mUnfinishedColor) {
        this.mUnfinishedColor = mUnfinishedColor;
    }

    public String getmCenterText() {
        return mCenterText;
    }

    public void setmCenterText(String mCenterText) {
        this.mCenterText = mCenterText;
    }

    public MyProgressRoundBarView(Context context) {
        this(context, null);

    }

    public MyProgressRoundBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressRoundBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        camera = new Camera();
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyProgressRoundBarView, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPaint();

    }

    protected void initByAttributes(TypedArray attributes) {
        mProgress = attributes.getInteger(R.styleable.MyProgressRoundBarView_round_bar_progress, 0);
        mProgressMax = attributes.getInteger(R.styleable.MyProgressRoundBarView_round_bar_progress_max, 100);
        mStartAngle = attributes.getInteger(R.styleable.MyProgressRoundBarView_round_bar_progress_startangle, 135);
        mEndAngle = attributes.getInteger(R.styleable.MyProgressRoundBarView_round_bar_progress_endangle, 405);
        mCenterTextColor = attributes.getColor(R.styleable.MyProgressRoundBarView_round_bar_center_text_color, Color.BLACK);
        mCenterText = attributes.getString(R.styleable.MyProgressRoundBarView_round_bar_center_text);
        mFinishedColor = attributes.getColor(R.styleable.MyProgressRoundBarView_round_bar_finished_color, Color.BLUE);
        mUnfinishedColor = attributes.getColor(R.styleable.MyProgressRoundBarView_round_bar_unfinished_color, Color.GRAY);
        mCenterTextSize = attributes.getDimension(R.styleable.MyProgressRoundBarView_round_bar_center_text_size, DensityUtil.dip2px(getContext(),25));
        mProgressWidth = attributes.getDimension(R.styleable.MyProgressRoundBarView_round_bar_width, DensityUtil.dip2px(getContext(),2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initPaint() {
        // 初始化paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        //        Bitmap mProgressBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        //        Canvas lBitmapCanvas = new Canvas(mProgressBitmap);


        if (mRotateProgress!=0) {
            CameraCanvas(canvas);
        } else {
            mProgressBitmap = getProgressBitmapComplet();
        }
        canvas.drawBitmap(mProgressBitmap, 0, 0, mPaint);
        //恢复图层
        canvas.restore();
        if (mCenterText != null && mCenterText.equals("")) return;
        mPaint.setColor(mCenterTextColor);
        Rect textBounds = new Rect();
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mCenterTextSize);
        if (mCenterText != null) {
            mPaint.getTextBounds(mCenterText, 0, mCenterText.length(), textBounds);
            int textHeight = textBounds.bottom - textBounds.top;
            canvas.drawText(mCenterText, getWidth() / 2, getHeight() / 2 + textHeight / 2, mPaint);
        } else {
            String text = mProgress * 100 / mProgressMax + "%";
            mPaint.getTextBounds(text, 0, text.length(), textBounds);
            int textHeight = textBounds.bottom - textBounds.top;
            canvas.drawText(text, getWidth() / 2, getHeight() / 2 + textHeight / 2, mPaint);
        }

    }

    int changeInt = 5;

    public void refreshImage() {

        invalidate();
    }

    private void CameraCanvas(Canvas canvas) {
        // 获取待处理的图像

        // 开始处理图像
        // 1.获取处理矩阵
        // 记录一下初始状态。save()和restore()可以将图像过渡得柔和一些。
        // Each save should be balanced with a call to restore().
        camera.save();
        Matrix matrix = new Matrix();

        // 恢复到之前的初始状态。
        camera.rotateX(90 * mRotateProgress / 100);
        //        camera.translate(0,-mRotateProgress*2,0);
        camera.getMatrix(matrix);
        // 设置图像处理的中心点

        matrix.preTranslate(-getWidth() >> 1, -getHeight() >> 1);
        matrix.postTranslate(getWidth() >> 1, getHeight() >> 1);
        camera.restore();
        canvas.concat(matrix);
    }

    private Bitmap getMaskBitmap(float radius) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint lpaint = new Paint();
        lpaint.setAntiAlias(true);
        lpaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, lpaint);

        return bitmap;
    }

    private Bitmap getProgressBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        int allSectorNum = (int) getAllSectorNum();
        Paint lpaint = new Paint();
        lpaint.setAntiAlias(true);
        lpaint.setColor(mFinishedColor);
        int finishNum = allSectorNum * mProgress / mProgressMax;
        for (int i = 0; i < finishNum; i++) {
            float lAngle = mStartAngle + i * (mIntervalNone + mInterval);
            canvas.drawArc(new RectF(mPadding, mPadding, getWidth() - mPadding, getHeight() - mPadding), lAngle, mInterval, true, lpaint);
        }
        lpaint.setColor(mUnfinishedColor);
        for (int i = finishNum; i < allSectorNum; i++) {
            float lAngle = mStartAngle + i * (mIntervalNone + mInterval);
            canvas.drawArc(new RectF(mPadding, mPadding, getWidth() - mPadding, getHeight() - mPadding), lAngle, mInterval, true, lpaint);
        }
        return bitmap;
    }

    private Bitmap getProgressBitmapComplet() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        int saveLayerCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        //画洞洞.不绘制的区域
        Paint lPaint = new Paint();
        lPaint.setAntiAlias(true);
        lPaint.setStyle(Paint.Style.FILL);
        lPaint.setColor(Color.WHITE);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2 - mProgressWidth - mPadding, lPaint);

        //画进度条
        lPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        mWaveBitmap = getProgressBitmap();
        canvas.drawBitmap(mWaveBitmap, 0, 0, lPaint);


        canvas.restoreToCount(saveLayerCount);
        lPaint.setXfermode(null);
        return bitmap;
    }

    private float getAllSectorNum() {
        return (Math.abs(mStartAngle - mEndAngle) + mIntervalNone) / (mInterval + mIntervalNone);
    }
}
