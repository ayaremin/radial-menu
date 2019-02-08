package circualreveal.animation;

import android.view.ViewGroup;

public interface RevealViewGroup {

  ViewRevealManager getViewRevealManager();

  void setViewRevealManager(ViewRevealManager manager);
}