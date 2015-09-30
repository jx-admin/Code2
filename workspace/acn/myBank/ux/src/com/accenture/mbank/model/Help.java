
package com.accenture.mbank.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Help extends Model {

    public Help() {

    }

    public Help(JSONObject jsonObject) {
        super(jsonObject);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String title;

    String content;

    List<Help> subHelpList;

    @Override
    public JSONObject toJsonObject() {
        return null;
    }

    @Override
    public void generate(JSONObject jsonObject) {
        super.generate(jsonObject);
    }

}
