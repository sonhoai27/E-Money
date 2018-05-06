//package com.it.sonh.affiliate.Template;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Movie;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.SystemClock;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//
//import com.it.sonh.affiliate.R;
//
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.util.jar.Attributes;
//
///**
// * Created by sonho on 2/25/2018.
// */
//
//public class GIFView extends View {
//    public Movie mMovie;
//    public long movieStart;
//
//    public GIFView(Context context) {
//        super(context);
//        init();
//    }
//
//    public GIFView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//
//    private void init(){
//        @SuppressLint("ResourceType") InputStream stream = getContext().getResources().openRawResource(R.drawable.animat_checkmark);
//        mMovie = Movie.decodeStream(stream);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.TRANSPARENT);
//        super.onDraw(canvas);
//        long now = android.os.SystemClock.uptimeMillis();
//        if(movieStart == 0){
//            movieStart = now;
//        }
//        if(mMovie != null){
//            int relTime =(int)((now - movieStart) % mMovie.duration());
//            mMovie.setTime(relTime);
//            mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
//            this.invalidate();
//        }
//    }
//    private int gifId;
//    public void setGIFResource(int resId){
//        this.gifId = resId;
//        init();
//    }
//    public int getGIFResource(){
//        return this.gi
//    }
//}
package com.it.sonh.affiliate.Template;

        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Movie;
        import android.net.Uri;
        import android.os.SystemClock;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.View;

        import java.io.FileNotFoundException;
        import java.io.InputStream;

public class GIFView extends View {

    private InputStream mInputStream;
    private Movie mMovie;
    private int mWidth, mHeight;
    private long mStart;
    private Context mContext;

    public GIFView(Context context) {
        super(context);
        this.mContext = context;
    }

    public GIFView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GIFView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        if (attrs.getAttributeName(1).equals("background")) {
            int id = Integer.parseInt(attrs.getAttributeValue(1).substring(1));
            setGifImageResource(id);
        }
    }


    private void init() {
        setFocusable(true);
        mMovie = Movie.decodeStream(mInputStream);
        mWidth = mMovie.width();
        mHeight = mMovie.height();

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        long now = SystemClock.uptimeMillis();
        canvas.drawColor(Color.TRANSPARENT);
        if (mStart == 0) {
            mStart = now;
        }

        if (mMovie != null) {

            int duration = mMovie.duration();
            if (duration == 0) {
                duration = 2000;
            }

            int relTime = (int) ((now - mStart) % duration);

            mMovie.setTime(relTime);

            mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
            invalidate();
        }
    }

    public void setGifImageResource(int id) {
        mInputStream = mContext.getResources().openRawResource(id);
        init();
    }

    public void setGifImageUri(Uri uri) {
        try {
            mInputStream = mContext.getContentResolver().openInputStream(uri);
            init();
        } catch (FileNotFoundException e) {
            Log.e("GIfImageView", "File not found");
        }
    }
}