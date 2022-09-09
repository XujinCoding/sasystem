package com.sa.product.easyExcel.test.observer;

public class OtcalObserver extends  Observer {
    int  index = 0;
    public OtcalObserver(Subject subject,int index) {
        this.subject = subject;
        this.index = index;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println("-------"+Integer.toOctalString(subject.getState()));
    }
}
