
package com.act.mbanking.bean;

import java.io.Serializable;

public class HelpItemListModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int helpItemId;

    private String text="";

    private String title;

    public HelpItemListModel() {
    }

    public HelpItemListModel(String title) {
        this.title = title;

    }

    public int getHelpItemId() {
        return helpItemId;
    }

    public void setHelpItemId(int helpItemId) {
        this.helpItemId = helpItemId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
