
package com.act.mbanking.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class Model implements JsonTransfor, Serializable {

    public Model() {

    }

    public Model(JSONObject jsonObject) {

        generate(jsonObject);
    }

    @Override
    public JSONObject toJsonObject() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void generate(JSONObject jsonObject) {

    }

}
