
package com.act.mbanking.secure;

import java.io.Serializable;

public interface ObjectEncryptAble {

    /**
     * 加密对象
     * 
     * @param o
     * @return
     */
    public Serializable encrypt(Serializable o);

    /**
     * 解密对象
     * 
     * @param o
     * @return
     */
    public Serializable decrypt(Serializable o);

}
