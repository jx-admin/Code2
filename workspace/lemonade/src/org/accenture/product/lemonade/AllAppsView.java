


package org.accenture.product.lemonade;

import java.util.ArrayList;

public interface AllAppsView {
    public interface Watcher {
        public void zoomed(float zoom);
    }

    public void setLauncher(Launcher launcher);

    public void setDragController(DragController dragger);

    public void zoom(float zoom, boolean animate);

    public boolean isVisible();

    public boolean isOpaque();

    public void setApps(ArrayList<? extends IconItemInfo> list);

    public void addApps(ArrayList<? extends IconItemInfo> list);

    public void removeApps(ArrayList<? extends IconItemInfo> list);

    public void updateApps(ArrayList<? extends IconItemInfo> list);

    public void dumpState();

    public void surrender();

    public void sort();
}
