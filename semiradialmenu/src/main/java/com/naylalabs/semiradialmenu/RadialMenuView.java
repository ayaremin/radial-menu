package com.naylalabs.semiradialmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;

import java.util.ArrayList;

import io.codetail.widget.RevealFrameLayout;

public class RadialMenuView extends RevealFrameLayout {
    Utils utils;

    RadialMenuListener listener;
    private int maxDistance;
    public boolean isOpen = false;
    private ArrayList<MenuItemView> menuItemViews;
    //inner circle is true default
    private boolean innerCircle = true;
    private int innerCircleColor;
    private boolean allowTitle = true;
    private int minDistance;
    private SliceView v1;
    private FrameLayout rootView;
    private int offset = 0;

    public RadialMenuView(Context context) {
        super(context);
    }

    public RadialMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadialMenuView setListener (RadialMenuListener listener) {
        this.listener = listener;
        return this;
    }

    public RadialMenuView setInnerCircle (boolean innerCircle, int innerCircleColor) {
        this.innerCircle = innerCircle;
        this.innerCircleColor = innerCircleColor;
        return this;
    }

    public RadialMenuView setMenuItems (ArrayList<MenuItemView> menuItemViews) {
        this.menuItemViews = menuItemViews;
        return this;
    }

    public RadialMenuView setOffset (int offset) {
        this.offset = Utils.getInstance(getContext()).dpTopixel(offset);
        Utils.getInstance(getContext()).setOffset(this.offset);
        return this;
    }

    public RadialMenuView setCenterView (View v) {
        utils = Utils.getInstance(getContext());
        Utils.getInstance(getContext()).setCenterView(v);
        return this;
    }

    public void build() {
        utils = Utils.getInstance(getContext());
        if (menuItemViews == null ) {
            throw new IllegalArgumentException("You should have at least 2 menu item");
        }
        if (menuItemViews.isEmpty() || menuItemViews.size() < 1 ) {
            throw new IllegalArgumentException("You should have at least 2 menu item");
        }
        if (menuItemViews.size() > 5) {
            throw new IllegalArgumentException("You should have max 5 menu item");
        }
        init();
    }

    private void init() {
        View v =  inflate(getContext(), R.layout.radial_menu_layout, this);
        rootView = v.findViewById(R.id.root_view);
        final int sweepAngle = 180 / menuItemViews.size();
        int index = 1;
        for (MenuItemView view : menuItemViews) {
            if (index == 1) {
                v1 = new SliceView(getContext(), view.getColor(), (-180 + (index * sweepAngle)) , -1 * sweepAngle);
                rootView.addView(v1);
            } else {
                rootView.addView(new SliceView(getContext(), view.getColor(), (-180 + (index * sweepAngle)) , -1 * sweepAngle));
            }
            index++;
        }

        if (innerCircle) {
            rootView.addView(new SliceView(getContext(), innerCircleColor, 0, -180, true));
        }

        if (menuItemViews.size() > 3) {
            allowTitle = false;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                configureItemViews();
            }
        },300);

        minDistance = (utils.getScreenWidth() - utils.dpTopixel(50)) / 4;
        maxDistance = (utils.getScreenWidth() - utils.dpTopixel(50)) / 2;

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    float angle = getAngle(motionEvent.getX(), motionEvent.getY(), v1);
                    int oX = (int) motionEvent.getX();
                    int oY = (int) motionEvent.getY();

                    float distance = (float) calculateDistanceBetweenCoordinates(motionEvent.getX(), motionEvent.getY(), v1.center_x, v1.center_y);
                    if (distance < minDistance || distance > maxDistance) {
                        isOpen = false;
                        exitReveal();
                        return false;
                    }

                    loop:for (int i = 0; i< menuItemViews.size(); i++) {
                        if (angle <= (sweepAngle * (i+1))) {
                            if (listener != null) {
                                listener.onItemClicked(i);
                            }
                            break loop;
                        }
                    }
                }
                return true;
            }
        });
    }

    public void show() {
        if (isOpen) {
            isOpen = false;
            post(new Runnable() {
                @Override
                public void run() {
                    exitReveal();
                }
            });
        } else {
            isOpen = true;
            post(new Runnable() {
                @Override
                public void run() {
                    enterReveal();
                }
            });
        }
    }

    protected void enterReveal() {
        final View myView = this;

        int cWidth = utils.getScreenWidth() - utils.dpTopixel(50);
        int cHeight = cWidth / 2;
        int radius = cHeight;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim;
            if (utils.getCenterView() == null) {
                anim = ViewAnimationUtils.createCircularReveal(myView, utils.getScreenWidth() / 2, utils.getScreenHeight() - utils.getActionBarSize(), 0, radius);
            } else{
                anim = ViewAnimationUtils.createCircularReveal(myView, utils.getScreenWidth() / 2, (int)utils.getCenterView().getY() + offset , 0, radius);
            }
            anim.setDuration(300);
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            myView.setVisibility(View.VISIBLE);
        }
    }

    protected void exitReveal() {
        try {
            isOpen = false;
            final View myView = this;
            int cWidth = utils.getScreenWidth() - utils.dpTopixel(50);
            int cHeight = cWidth / 2;
            int radius = cHeight;

            Animator anim =
                    null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (utils.getCenterView() == null) {
                    anim = ViewAnimationUtils.createCircularReveal(myView, utils.getScreenWidth() / 2, utils.getScreenHeight() - utils.getActionBarSize(), radius, 0);
                } else{
                    anim = ViewAnimationUtils.createCircularReveal(myView, utils.getScreenWidth() / 2, (int)utils.getCenterView().getY() + offset, radius, 0);
                }
                anim.setDuration(200);

                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        myView.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
            } else {
                myView.setVisibility(INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void configureItemViews() {
        int diameter = utils.getScreenWidth() - utils.dpTopixel(50);

        int i = 0;
        for (MenuItemView menuItemView : menuItemViews) {
            menuItemView.setAllowTitle(allowTitle);
            final int sweepAngle = 180 / menuItemViews.size();
            float radius = diameter / 2;
            float angle = ((sweepAngle/2)  + (sweepAngle * i));
            if (angle > 90)
                angle = angle * -1;

            float oX, oY;
            double cos = Math.cos(Math.toRadians( angle ));
            double sin =  Math.sin(Math.toRadians( angle ));
            double y = sin  * 3 * radius / 4;
            double x = cos * 3 * radius / 4;

            if (x<0)
                x = x * -1;
            if (y < 0)
                y = y * -1;

            int center = utils.getScreenWidth() / 2;
            int centery = (int) utils.getCenterView().getY();
            if (utils.getCenterView() == null) {
                if ( ((sweepAngle/2)  + (sweepAngle * i)) > 90) {
                    oX = (center + (float) x - utils.dpTopixel(40));
                    oY = (utils.getScreenHeight()) - (float) y - utils.dpTopixel(40) - utils.getActionBarSize() ;
                } else {
                    oX = (center - (float) x - utils.dpTopixel(40));
                    oY = (utils.getScreenHeight()) - (float) y - utils.dpTopixel(40) - utils.getActionBarSize() ;
                }
            } else {
                if ( ((sweepAngle/2)  + (sweepAngle * i)) > 90) {
                    oX = (center + (float) x - utils.dpTopixel(40));
                    oY = (utils.getCenterView().getY() + offset - (float) y - utils.dpTopixel(30)) ;
                } else {
                    oX = (center - (float) x - utils.dpTopixel(40));
                    oY = (utils.getCenterView().getY() + offset - (float) y - utils.dpTopixel(30)) ;
                }
            }

            menuItemView.setX(oX);
            menuItemView.setY(oY);

            menuItemView.setClickable(false);
            rootView.addView(menuItemView,0);
            menuItemView.bringToFront();
            i++;
        }
    }

    private float getAngle(float x, float y, SliceView sliceView) {
        float angle = (float) Math.toDegrees(Math.atan2(sliceView.center_y - y, sliceView.center_x - x));
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    private double calculateDistanceBetweenCoordinates(
            double x1,
            double y1,
            double x2,
            double y2) {

        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);

        return Math.hypot(ac, cb);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface RadialMenuListener {
        void onItemClicked(int i);
    }
}
