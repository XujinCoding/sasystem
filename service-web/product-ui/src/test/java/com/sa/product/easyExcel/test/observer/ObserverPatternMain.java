package com.sa.product.easyExcel.test.observer;

public class ObserverPatternMain {
    public static void main(String[] args) {
        //被观察的对象
        Subject subject = new Subject();
        //观察者们
        new HexaObserver(subject,1);
        new OtcalObserver(subject,1);
        new BinaryObserver(subject,0);
        System.out.println("First state change: 15");
        subject.setState(15);
        System.out.println("Second state change: 10");
        subject.setState(10);
    }
}
