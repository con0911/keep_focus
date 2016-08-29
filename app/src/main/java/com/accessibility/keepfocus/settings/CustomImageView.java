package com.accessibility.keepfocus.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width != height) {
            width = height;
        }
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );
    }

}
