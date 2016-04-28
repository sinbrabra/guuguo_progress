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
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import top.guuguo.progress_lib.R;
import top.guuguo.progress_lib.util.DensityUtil;

/**
 * Created by guodeqing on 16/4/5.
 */
public class MyProgressInstrument extends View {
    private Drawable icon;
    /**
     * id:&:R.color.
     */
    private Paint mPaint;

    private float mProgress = 0;
    private float mCenterTextSize;
    private int mProgressMax;
    private int mCenterTextColor;
    private int mFinishedColor;
    private int mBackgroundColor;

    private String mCenterText = "";

    private Bitmap mBgBitmap;

    public int getmProgressMax() {
        return mProgressMax;
    }

    public void setmProgressMax(int mProgressMax) {
        this.mProgressMax = mProgressMax;
    }

    public float getmProgress() {
        return mProgress;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        progressAnimation();
    }

    private void progressAnimation() {
        invalidate();
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
        invalidate();
    }

    public String getmCenterText() {
        return mCenterText;
    }

    public void setmCenterText(String mCenterText) {
        this.mCenterText = mCenterText;
    }

    public MyProgressInstrument(Context context) {
        this(context, null);

    }

    public MyProgressInstrument(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressInstrument(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyProgressInstrument, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPaint();

    }

    protected void initByAttributes(TypedArray attributes) {
        mProgress = attributes.getInteger(R.styleable.MyProgressInstrument_Instrument_progress, 0);
        mProgressMax = attributes.getInteger(R.styleable.MyProgressInstrument_Instrument_progress_max, 100);
        mCenterTextColor = attributes.getColor(R.styleable.MyProgressInstrument_Instrument_center_text_color, Color.BLACK);
        if (attributes.getString(R.styleable.MyProgressInstrument_Instrument_center_text) != null)
            mCenterText = attributes.getString(R.styleable.MyProgressInstrument_Instrument_center_text);
        mFinishedColor = attributes.getColor(R.styleable.MyProgressInstrument_Instrument_finished_color, Color.BLUE);
        mBackgroundColor = attributes.getColor(R.styleable.MyProgressInstrument_Instrument_background_color, Color.BLACK);
        mCenterTextSize = attributes.getDimension(R.styleable.MyProgressInstrument_Instrument_center_text_size, DensityUtil.dip2px(getContext(), 25));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initPaint() {
        // 初始化paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.progress_instrument_bg);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);

        int saveLayerCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawBitmap(mBgBitmap, new Rect(0, 0, mBgBitmap.getWidth(), mBgBitmap.getHeight()), new Rect(0, 0, getWidth(), getHeight()), mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(saveLayerCount);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(false);
        mPaint.setColor(mFinishedColor);
        canvas.drawArc(new RectF(0, 0, getHeight(), getHeight()), 0, 200 * mProgress / mProgressMax, true, mPaint);
        canvas.save();
        canvas.rotate(200 * mProgress / mProgressMax, getWidth() / 2, getHeight() / 2);
        mPaint.setColor(mBackgroundColor);
        mPaint.setStrokeWidth(5);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
        canvas.restore();


        //        Rect textBounds = new Rect();
        //        mPaint.reset();
        //        mPaint.setColor(mFinishedColor);
        //        mPaint.setTextSize(50);
        //        mPaint.getTextBounds("你好吗", 0, "你好吗".length(), textBounds);
        //        mPaint.setTextAlign(Paint.Align.CENTER);
        //        int textHeight = textBounds.bottom - textBounds.top;
        //        canvas.drawText("你好吗", getWidth() / 2, getHeight() - textHeight / 2, mPaint);
    }

    private static final String INSTANCE_STATE = "saved_instance";
    //        private static final String INSTANCE_TEXT_COLOR = "text_color";
    //        private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    //        private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    //        private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";

    //        private static final String INSTANCE_SUFFIX = "suffix";
    //        private static final String INSTANCE_PREFIX = "prefix";
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, mFinishedColor);
        bundle.putFloat(INSTANCE_PROGRESS, mProgress);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mFinishedColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            mProgress = bundle.getFloat(INSTANCE_PROGRESS);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
