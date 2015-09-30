
package com.act.mbanking.bean;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {
    private String productCode;

    private String productDesc;

    private List<ServicesModel> services;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public List<ServicesModel> getServices() {
        return services;
    }

    public void setServices(List<ServicesModel> services) {
        this.services = services;
    }

}
