
package com.accenture.mbank.model;

import android.graphics.Bitmap;

public class ListAdvNewsModel {
    private String date;

    private String imageRef;

    private String imageRefThumb;

    private String text;

    private String title;
    
    private Bitmap _imageRef;
    
    private Bitmap _imageRefThumb;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    public String getImageRefThumb() {
        return imageRefThumb;
    }

    public void setImageRefThumb(String imageRefThumb) {
        this.imageRefThumb = imageRefThumb;
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

	public Bitmap get_imageRef() {
		return _imageRef;
	}

	public void set_imageRef(Bitmap _imageRef) {
		this._imageRef = _imageRef;
	}

	public Bitmap get_imageRefThumb() {
		return _imageRefThumb;
	}

	public void set_imageRefThumb(Bitmap _imageRefThumb) {
		this._imageRefThumb = _imageRefThumb;
	}
}
