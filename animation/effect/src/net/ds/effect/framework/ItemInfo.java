package net.ds.effect.framework;


/**
 * 界面排版项（桌面项、停靠条项、文件夹、抽屉项、图片项、视频项）
 * @author songzhaochun
 *
 */
public class ItemInfo {

    /**
     * Iindicates the screen in which the shortcut appears.
     */
    public int screen = -1;

    /**
     * Indicates the X position of the associated cell.
     */
    public int cellX = -1;

    /**
     * Indicates the Y position of the associated cell.
     */
    public int cellY = -1;

    /**
     * Indicates the X cell span.
     */
    public int spanX = 1;

    /**
     * Indicates the Y cell span.
     */
    public int spanY = 1;

    public int getScreen() {
        return screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }

    public int getCellX() {
        return cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    public boolean isIcon() {
        return spanX == 1 && spanY == 1;
    }
}


