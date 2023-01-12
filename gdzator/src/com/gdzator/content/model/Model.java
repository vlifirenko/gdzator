package com.gdzator.content.model;

import android.content.ContentValues;

public abstract class Model {
    protected Long id;

    public Long getId() {
        return id;
    }

    public abstract ContentValues toCV();

    protected void setId(long id) {
        if (id == -1) {
            this.id = null;
        } else {
            this.id = id;
        }
    }
}
