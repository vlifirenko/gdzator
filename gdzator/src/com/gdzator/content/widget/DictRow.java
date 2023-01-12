package com.gdzator.content.widget;

public class DictRow {
    public Long id;
    public String data;

    public DictRow(String entry) {
        this.id = Long.valueOf(entry.substring(0, entry.indexOf(":")));
        this.data = entry.substring(entry.indexOf(":") + 1);
    }

    @Override
    public String toString() {
        return this.data;
    }
}
