package com.sa.product.easyExcel.test.observer;

public class BinaryObserver extends  Observer {
    int index = 0;

    public BinaryObserver(Subject subject ,int index) {
        this.index = index;
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        if (index!=0)
            System.out.println("-------"+Integer.toBinaryString(subject.getState()));
    }
}
