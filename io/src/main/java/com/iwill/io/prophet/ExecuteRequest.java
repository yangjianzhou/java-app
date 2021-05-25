package com.iwill.io.prophet;

import java.io.Serializable;

public class ExecuteRequest implements Serializable {

    private static final long serialVersionUID = -4565560110672780832L;

    private Integer index;

    private Integer size;

    public ExecuteRequest(Integer index, Integer size) {
        this.index = index;
        this.size = size;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
