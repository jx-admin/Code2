
package com.act.mbanking.bean;

import org.json.JSONObject;

public interface JsonTransfor {

    /**
     * 本方法可以将自己的属性生成jsonObject
     * 
     * @return
     */
    JSONObject toJsonObject();

    /**
     * 本方法可以通过jsonObject设置自己的属性
     * 
     * @param jsonObject
     */
    void generate(JSONObject jsonObject);

}
