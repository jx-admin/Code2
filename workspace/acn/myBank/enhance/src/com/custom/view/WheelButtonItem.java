
package com.custom.view;

import java.io.Serializable;

public final class WheelButtonItem implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public WheelButtonItem() {
    }

    protected float value;

    public String text;

    public int id;

    public int weight;

    public int backgroundColor;

    public int textColor = 0xffffffff;

    public int shaderColor = 0xff000000;

    public boolean clickAble = true;

    public boolean visibility = true;
}
