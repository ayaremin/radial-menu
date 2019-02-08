package circualreveal.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.util.Property;
import android.view.View;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class ViewRevealManager {
  public static final ClipRadiusProperty REVEAL = new ClipRadiusProperty();

  private final ViewTransformation viewTransformation;
  private final Map<View, RevealValues> targets = new HashMap<>();
  private final Map<Animator, RevealValues> animators = new HashMap<>();

  private final AnimatorListenerAdapter animatorCallback = new AnimatorListenerAdapter() {
    @Override public void onAnimationStart(Animator animation) {
      final RevealValues values = getValues(animation);
      values.clip(true);
    }

    @Override public void onAnimationCancel(Animator animation) {
      endAnimation(animation);
    }

    @Override public void onAnimationEnd(Animator animation) {
      endAnimation(animation);
    }

    private void endAnimation(Animator animation) {
      final RevealValues values = getValues(animation);
      values.clip(false);

      targets.remove(values.target);
      animators.remove(animation);
    }
  };

  public ViewRevealManager() {
    this(new PathTransformation());
  }

  public ViewRevealManager(ViewTransformation transformation) {
    this.viewTransformation = transformation;
  }

  Animator dispatchCreateAnimator(RevealValues data) {
    final Animator animator = createAnimator(data);

    // Before animation is started keep them
    targets.put(data.target(), data);
    animators.put(animator, data);
    return animator;
  }

  protected Animator createAnimator(RevealValues data) {
    final ObjectAnimator animator =
        ObjectAnimator.ofFloat(data, REVEAL, data.startRadius, data.endRadius);

    animator.addListener(getAnimatorCallback());
    return animator;
  }

  protected final AnimatorListenerAdapter getAnimatorCallback() {
    return animatorCallback;
  }

  protected final RevealValues getValues(Animator animator) {
    return animators.get(animator);
  }

  protected final RevealValues getValues(View view) {
    return targets.get(view);
  }

  protected boolean overrideNativeAnimator() {
    return false;
  }

  public boolean isClipped(View child) {
    final RevealValues data = getValues(child);
    return data != null && data.isClipping();
  }

  public final boolean transform(Canvas canvas, View child) {
    final RevealValues revealData = targets.get(child);

    if (revealData == null) {
      return false;
    }
    else if (revealData.target != child) {
      throw new IllegalStateException("Inconsistency detected, contains incorrect target view");
    }
    else if (!revealData.clipping) {
      return false;
    }

    return viewTransformation.transform(canvas, child, revealData);
  }

  public static final class RevealValues {
    private static final Paint debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    static {
      debugPaint.setColor(Color.GREEN);
      debugPaint.setStyle(Paint.Style.FILL);
      debugPaint.setStrokeWidth(2);
    }

    final int centerX;
    final int centerY;

    final float startRadius;
    final float endRadius;

    boolean clipping;

    float radius;

    View target;

    public RevealValues(View target, int centerX, int centerY, float startRadius, float endRadius) {
      this.target = target;
      this.centerX = centerX;
      this.centerY = centerY;
      this.startRadius = startRadius;
      this.endRadius = endRadius;
    }

    public void radius(float radius) {
      this.radius = radius;
    }

    public float radius() {
      return radius;
    }

    public View target() {
      return target;
    }

    public void clip(boolean clipping) {
      this.clipping = clipping;
    }

    public boolean isClipping() {
      return clipping;
    }
  }

  interface ViewTransformation {

    boolean transform(Canvas canvas, View child, RevealValues values);
  }

  public static class PathTransformation implements ViewTransformation {

    private final Path path = new Path();

    private Region.Op op = Region.Op.REPLACE;

    public Region.Op op() {
      return op;
    }

    public void op(Region.Op op) {
      this.op = op;
    }

    @Override public boolean transform(Canvas canvas, View child, RevealValues values) {
      path.reset();
      path.addCircle(child.getX() + values.centerX, child.getY() + values.centerY, values.radius,
          Path.Direction.CW);

      canvas.clipPath(path, op);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        child.invalidateOutline();
      }
      return false;
    }
  }

  private static final class ClipRadiusProperty extends Property<RevealValues, Float> {

    ClipRadiusProperty() {
      super(Float.class, "supportCircularReveal");
    }

    @Override public void set(RevealValues data, Float value) {
      data.radius = value;
      data.target.invalidate();
    }

    @Override public Float get(RevealValues v) {
      return v.radius();
    }
  }

  static class ChangeViewLayerTypeAdapter extends AnimatorListenerAdapter {
    private RevealValues viewData;
    private int featuredLayerType;
    private int originalLayerType;

    ChangeViewLayerTypeAdapter(RevealValues viewData, int layerType) {
      this.viewData = viewData;
      this.featuredLayerType = layerType;
      this.originalLayerType = viewData.target.getLayerType();
    }

    @Override public void onAnimationStart(Animator animation) {
      viewData.target().setLayerType(featuredLayerType, null);
    }

    @Override public void onAnimationCancel(Animator animation) {
      viewData.target().setLayerType(originalLayerType, null);
    }

    @Override public void onAnimationEnd(Animator animation) {
      viewData.target().setLayerType(originalLayerType, null);
    }
  }
}
