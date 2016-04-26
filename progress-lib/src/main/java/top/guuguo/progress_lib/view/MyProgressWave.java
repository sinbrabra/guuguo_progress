package top.guuguo.progress_lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import top.guuguo.progress_lib.R;
import top.guuguo.progress_lib.util.DensityUtil;

/**
 * Created by guugu on 2016/4/8.
 */
public class MyProgressWave extends View {
    private Paint textPaint;
    private RectF rectF = new RectF();

    private float textSize;
    private float circleWidth;
    private int textColor;
    private boolean enableCenterText;
    private boolean enableWave;
    private float progress = 0.0f;
    private int max;
    private int style;
    private int finishedColor;
    private int unfinishedColor;
    private int circleColor;
    private String prefixText = "";
    private String suffixText = "%";
    private Bitmap mMaskBitmap;
    private boolean changeHeight = true;
    private Bitmap mWaveBitmap;

    public String getForceTitle() {
        return forceTitle;
    }

    public void setForceTitle(String forceTitle) {
        this.forceTitle = forceTitle;
        invalidate();
    }

    private final int STYLE_CIRCLE = 0;
    private final int STYLE_SQUARE = 1;

    private String forceTitle;

    private final int default_finished_color = Color.rgb(66, 145, 241);
    private final int default_unfinished_color = Color.rgb(204, 204, 204);
    private final int default_text_color = Color.WHITE;
    private final int default_max = 100;
    private final float default_text_size;
    private final boolean default_enable_center_text = false;
    private final float default_circle_width;
    private double periodMultiple=15;

    private Paint paint = new Paint();

    public MyProgressWave(Context context) {
        this(context, null);
    }

    public MyProgressWave(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressWave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = DensityUtil.dip2px(getContext(), 18);
        default_circle_width = DensityUtil.dip2px(getContext(), 2);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyProgressWave, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
        StartWave();
    }

    protected void initByAttributes(TypedArray attributes) {
        finishedColor = attributes.getColor(R.styleable.MyProgressWave_wave_finished_color, default_finished_color);
        unfinishedColor = attributes.getColor(R.styleable.MyProgressWave_wave_unfinished_color, default_unfinished_color);
        enableWave = attributes.getBoolean(R.styleable.MyProgressWave_wave_enable, true);
        circleColor = attributes.getColor(R.styleable.MyProgressWave_wave_circle_color, finishedColor);
        textColor = attributes.getColor(R.styleable.MyProgressWave_wave_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.MyProgressWave_wave_text_size, default_text_size);
        circleWidth = attributes.getDimension(R.styleable.MyProgressWave_wave_circle_width, default_circle_width);
        enableCenterText = attributes.getBoolean(R.styleable.MyProgressWave_wave_enable_center_text, default_enable_center_text);
        style = attributes.getInt(R.styleable.MyProgressWave_wave_style, STYLE_CIRCLE);
        setMax(attributes.getInt(R.styleable.MyProgressWave_wave_max, default_max));
        setProgress(attributes.getFloat(R.styleable.MyProgressWave_wave_progress, 0.0f));

        if (attributes.getString(R.styleable.MyProgressWave_wave_prefix_text) != null) {
            setPrefixText(attributes.getString(R.styleable.MyProgressWave_wave_prefix_text));
        }
        if (attributes.getString(R.styleable.MyProgressWave_wave_suffix_text) != null) {
            setSuffixText(attributes.getString(R.styleable.MyProgressWave_wave_suffix_text));
        }
        if (attributes.getString(R.styleable.MyProgressWave_wave_force_title) != null) {
            setForceTitle(attributes.getString(R.styleable.MyProgressWave_wave_force_title));
        }
    }

    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        paint.setAntiAlias(true);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress = getMax();
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    public int getFinishedColor() {
        return finishedColor;
    }

    public void setFinishedColor(int finishedColor) {
        this.finishedColor = finishedColor;
        this.invalidate();
    }

    public int getUnfinishedColor() {
        return unfinishedColor;
    }

    public void setUnfinishedColor(int unfinishedColor) {
        this.unfinishedColor = unfinishedColor;
        this.invalidate();
    }

    public String getPrefixText() {
        return prefixText;
    }

    public void setPrefixText(String prefixText) {
        this.prefixText = prefixText;
        this.invalidate();
    }

    public String getSuffixText() {
        return suffixText;
    }

    public void setSuffixText(String suffixText) {
        this.suffixText = suffixText;
        this.invalidate();
    }

    public String getDrawText() {
        if (forceTitle != null) return getForceTitle();
        else if (!enableCenterText) return null;
        return getPrefixText() + getProgress() + getSuffixText();
    }

    public float getProgressPercentage() {
        return getProgress() / (float) getMax();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        rectF.set(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private float getLineHeight(int x, float h) {
        if (enableWave) return (float) (6 * Math.sin(x * (1/periodMultiple)) + getHeight() - h);
        else {
            return getHeight() - h;
        }
    }

    float offset = 0;
    private static final String TAG = "MyProgressWave";

    @Override
    protected void onDraw(Canvas canvas) {

        float yHeight = getProgress() / (float) getMax() * getHeight();
        float radius = getWidth() / 2f;
        //保存图层
        int saveLayerCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        if (mMaskBitmap == null) mMaskBitmap = getMaskBitmap(radius);

        paint.setFilterBitmap(true);
        canvas.drawBitmap(mMaskBitmap, 0, 0, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        paint.setAntiAlias(true);

        if (changeHeight) {
//            Log.d(TAG, "onDraw: new");
            mWaveBitmap = getWaveBitmap(yHeight);
            canvas.drawBitmap(mWaveBitmap, (float) (offset/(1/periodMultiple)), 0, paint);
            changeHeight = false;
        } else {
//            Log.d(TAG, "onDraw: 2");
            if (mWaveBitmap == null) {
//                Log.d(TAG, "onDraw: new");

                mWaveBitmap = getWaveBitmap(yHeight);
            }
            canvas.drawBitmap(mWaveBitmap, (float) (offset/(1/periodMultiple)), 0, paint);
        }

        paint.setXfermode(null);
        //恢复图层
        canvas.restoreToCount(saveLayerCount);
        //画圈
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleWidth);
        paint.setColor(circleColor);
        switch (style) {
            case STYLE_CIRCLE:
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius - (circleWidth / 2), paint);
                break;
            case STYLE_SQUARE:
                canvas.drawRect(circleWidth / 2, circleWidth / 2, getWidth() - circleWidth / 2, getHeight() - circleWidth / 2, paint);
                break;
        }
        String text = getDrawText();
        if (!TextUtils.isEmpty(text)) {
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
        }
    }

    /**
     * 绘制不同的图形Bitmap
     */
    private Bitmap getMaskBitmap(float radius) {

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint lpaint = new Paint();
        lpaint.setAntiAlias(true);
        lpaint.setStyle(Paint.Style.FILL);
        lpaint.setColor(getUnfinishedColor());
        switch (style) {
            case STYLE_CIRCLE:
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, lpaint);
                break;
            case STYLE_SQUARE:
                canvas.drawRect(0, 0, getWidth(), getHeight(), lpaint);
                break;
        }
        return bitmap;
    }

    private Bitmap getWaveBitmap(float yHeight) {
        int width = (int) (((int) (getWidth() / ((2 * Math.PI)/(1/periodMultiple))) + 2) * (2 * Math.PI/(1/periodMultiple)));
        //宽度设置为
        Log.d(TAG, "getWaveBitmap: w" + width);
        Bitmap bitmap = Bitmap.createBitmap(width, getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint lpaint = new Paint();
        lpaint.setColor(getFinishedColor());
        for (int i = 0; i <= width; i++) {
            canvas.drawLine(i, getLineHeight(i, yHeight), i, getHeight(), lpaint);
        }
        return bitmap;
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    invalidate();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void StartWave() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (enableWave) {
                    try {
                        Thread.sleep(20);
                        offset -= 0.13;
                        if (offset <= -2 * Math.PI) offset += 2 * Math.PI;
                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //    @Override
    //    protected Parcelable onSaveInstanceState() {
    //        final Bundle bundle = new Bundle();
    //        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
    //        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
    //        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
    //        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedColor());
    //        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedColor());
    //        bundle.putInt(INSTANCE_MAX, getMax());
    //        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
    //        bundle.putString(INSTANCE_SUFFIX, getSuffixText());
    //        bundle.putString(INSTANCE_PREFIX, getPrefixText());
    //        return bundle;
    //    }
    //
    //    @Override
    //    protected void onRestoreInstanceState(Parcelable state) {
    //        if (state instanceof Bundle) {
    //            final Bundle bundle = (Bundle) state;
    //            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
    //            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
    //            finishedColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
    //            unfinishedColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
    //            initPainters();
    //            setMax(bundle.getInt(INSTANCE_MAX));
    //            setProgress(bundle.getFloat(INSTANCE_PROGRESS));
    //            prefixText = bundle.getString(INSTANCE_PREFIX);
    //            suffixText = bundle.getString(INSTANCE_SUFFIX);
    //            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
    //            return;
    //        }
    //        super.onRestoreInstanceState(state);
    //    }
}
