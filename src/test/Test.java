package test;

import joymufeng.CNIPRecognizer;

/**
 * Created by joymufeng on 2016/11/19.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("8.8.8.8: " + CNIPRecognizer.isCNIP("8.8.8.8"));
        System.out.println("114.114.114.114: " + CNIPRecognizer.isCNIP("114.114.114.114"));
    }
}
