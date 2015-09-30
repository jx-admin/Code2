
package com.act.mbanking.bean;

import java.util.List;

public class AdvNewsResponseModel {
    private List<ListAdvNewsModel> listAdvNews;

    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public List<ListAdvNewsModel> getListAdvNews() {
        return listAdvNews;
    }

    public void setListAdvNews(List<ListAdvNewsModel> listAdvNews) {
        this.listAdvNews = listAdvNews;
    }
}
