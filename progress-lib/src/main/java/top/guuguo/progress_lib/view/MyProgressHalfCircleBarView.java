package top.guuguo.progress_lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import top.guuguo.progress_lib.R;
import top.guuguo.progress_lib.util.DensityUtil;

/**
 * Created by guodeqing on 16/4/5.
 */
public class MyProgressHalfCircleBarView extends View {
    private Drawable icon;
    /**
     * id:&:R.color.
     */
    private Paint mPaint;

    private int mProgress = 0;
    private float mCenterTextSize;
    private int mProgressMax;
    private int mCenterTextColor;
    private int mFinishedColor;
    private float mProgressWidth;
    private int mUnfinishedColor;
    private int mBackgroundColor;

    private String mCenterText = "";

    private Bitmap mBgBitmap;

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
        progressAnimation();
    }

    private void progressAnimation() {

    }

    int currentProgress;

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

    public MyProgressHalfCircleBarView(Context context) {
        this(context, null);

    }

    public MyProgressHalfCircleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressHalfCircleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyProgressHalfCircleBarView, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPaint();

    }

    protected void initByAttributes(TypedArray attributes) {
        mProgress = attributes.getInteger(R.styleable.MyProgressHalfCircleBarView_half_progress, 0);
        mProgressMax = attributes.getInteger(R.styleable.MyProgressHalfCircleBarView_half_progress_max, 100);
        mCenterTextColor = attributes.getColor(R.styleable.MyProgressHalfCircleBarView_half_center_text_color, Color.BLACK);
        if (attributes.getString(R.styleable.MyProgressHalfCircleBarView_half_center_text) != null)
            mCenterText = attributes.getString(R.styleable.MyProgressHalfCircleBarView_half_center_text);
        mFinishedColor = attributes.getColor(R.styleable.MyProgressHalfCircleBarView_half_finished_color, Color.BLUE);
        mUnfinishedColor = attributes.getColor(R.styleable.MyProgressHalfCircleBarView_half_unfinished_color, Color.GRAY);
        mBackgroundColor = attributes.getColor(R.styleable.MyProgressHalfCircleBarView_half_background_color, mUnfinishedColor);
        mCenterTextSize = attributes.getDimension(R.styleable.MyProgressHalfCircleBarView_half_center_text_size, DensityUtil.dip2px(getContext(), 25));
        mProgressWidth = attributes.getDimension(R.styleable.MyProgressHalfCircleBarView_half_width, DensityUtil.dip2px(getContext(), 15));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initPaint() {
        // 初始化paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.heart_rat_progress_bg);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        mPaint.setStrokeWidth(mProgressWidth);

        int saveLayerCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        canvas.drawBitmap(mBgBitmap, new Rect(0, 0, mBgBitmap.getWidth(), mBgBitmap.getHeight()), new Rect(0, 0, getWidth(), getHeight()), mPaint);
        mPaint.setXfermode(null);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.restoreToCount(saveLayerCount);
        //        mPaint.d
        float margin = 2 / 14f;
        float y = getHeight() * margin;
        float x = getWidth() / 2 - getHeight() + y;
        mPaint.setColor(mUnfinishedColor);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(false);

        canvas.drawArc(new RectF(x, y, x + 2 * getHeight() * (1 - margin), y + 2 * getHeight() * (1 - margin)), 180, 180, false, mPaint);
        mPaint.setColor(mFinishedColor);
        mPaint.setStrokeWidth(mProgressWidth);
        canvas.drawArc(new RectF(x, y, x + 2 * getHeight() * (1 - margin), y + 2 * getHeight() * (1 - margin)), 180, 180 * mProgress / mProgressMax, false, mPaint);

        Rect textBounds = new Rect();
        mPaint.reset();
        mPaint.setColor(mCenterTextColor);
        mPaint.setTextSize(mCenterTextSize);
        mPaint.getTextBounds(mCenterText, 0, mCenterText.length(), textBounds);
        mPaint.setTextAlign(Paint.Align.CENTER);
        int textHeight = textBounds.bottom - textBounds.top;
        canvas.drawText(mCenterText, getWidth() / 2, getHeight() - textHeight / 2, mPaint);
    }


}
