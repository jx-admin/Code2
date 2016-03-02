package wu.a.lib.view;

import java.io.Serializable;

public  final class ItemButton implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ItemButton(){}
	public float value;
	public String text;
	public int weight;
	public int backgroundColor;
	public int textColor=0xffffffff;
	public int shaderColor=0xff000000;
	public boolean clickAble=true;
	public boolean visibility=true;
}
