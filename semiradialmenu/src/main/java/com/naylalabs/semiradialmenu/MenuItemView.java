package com.naylalabs.semiradialmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuItemView extends LinearLayout {
    Utils utils;
    private String title;
    private int image;
    @ColorRes
    private int color;
    private float angleRotation;
    private boolean allowTitle = true;
    private View v;

    public MenuItemView(Context context, String title, int image, int color) {
        super(context);
        this.title = title;
        this.image = image;
        this.color = color;
        init();
    }

    public MenuItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private void init() {
        if (v == null) {
            v = inflate(getContext(), R.layout.radial_menu_item_layout, this);
        }

        TextView tv = v.findViewById(R.id.text);
        ImageView iv = v.findViewById(R.id.image);

        if (TextUtils.isEmpty(title) || !allowTitle) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(title);
        }
        iv.setImageResource(image);
    }

    public void setAllowTitle(boolean allowTitle) {
        this.allowTitle = allowTitle;
        init();
    }

    public void setAngleRotation(float angleRotation) {
        this.angleRotation = angleRotation;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect clipBounds = canvas.getClipBounds();
        canvas.save();
        canvas.rotate(angleRotation, clipBounds.exactCenterX(), clipBounds.exactCenterY());
        super.onDraw(canvas);
        canvas.restore();
    }
}
