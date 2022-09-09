package com.sa.product.easyExcel.test.observer;

public class HexaObserver extends  Observer {
    int index = 0;
    public HexaObserver(Subject subject, int index) {
        this.subject = subject;
        this.index = index;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println("-------"+Integer.toHexString(subject.getState()).toUpperCase());
    }
}
