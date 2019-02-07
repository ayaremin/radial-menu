package com.naylalabs.semiradialmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class SliceView extends View {

    Utils utils;

    private int color;
    private int start;
    private int sweep;
    private boolean inner;
    private RectF oval;
    public float center_x, center_y;

    public SliceView(Context context, int color, int start, int sweep) {
        super(context);
        this.color = color;
        this.start = start;
        this.sweep = sweep;
        utils = Utils.getInstance(getContext());
    }

    public SliceView(Context context, int color, int start, int sweep, boolean inner) {
        super(context);
        this.color = color;
        this.start = start;
        this.sweep = sweep;
        this.inner = inner;
        utils = Utils.getInstance(getContext());
    }

    public RectF getOval() {
        return oval;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //There is another flag for inner white circle
        if (inner) {
            float width = (float) getWidth();
            float height = utils.getScreenHeight() - utils.getActionBarSize();
            float radius;
            radius = (width - utils.dpTopixel(50)) / 4;

            Path path = new Path();
            path.addCircle((width - utils.dpTopixel(50)) / 4,
                    (height - utils.dpTopixel(50)) / 4, radius,
                    Path.Direction.CW);

            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(getContext(), color));
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);

            float center_x, center_y;
            final RectF oval = new RectF();
            paint.setStyle(Paint.Style.FILL);

            center_x = width / 2;
            if (utils.getCenterView() == null) {
                center_y = utils.getScreenHeight() - utils.getActionBarSize();
            } else{
                center_y = utils.getCenterView().getY();
            }

            oval.set(center_x - radius,
                    center_y - radius,
                    center_x + radius,
                    center_y + radius);
            canvas.drawArc(oval, start, sweep, true, paint);


        } else {
            float width = (float) getWidth();
            float radius;
            radius = (width - utils.dpTopixel(50)) / 2;

            Path path = new Path();
            path.addCircle((width - utils.dpTopixel(50)) / 2,
                    (width - utils.dpTopixel(50)) / 2, radius,
                    Path.Direction.CW);

            Paint paint = new Paint();
            paint.setColor(ContextCompat.getColor(getContext(), color));
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            oval = new RectF();
            paint.setStyle(Paint.Style.FILL);

            center_x = width / 2;
            if (utils.getCenterView() == null) {
                center_y = utils.getScreenHeight() - utils.getActionBarSize();
            } else{
                center_y = utils.getCenterView().getY();
            }

            oval.set(center_x - radius,
                    center_y - radius,
                    center_x + radius,
                    center_y + radius);
            canvas.drawArc(oval, start, sweep, true, paint);
        }
    }
}