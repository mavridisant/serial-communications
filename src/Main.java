package com.company;

public class Main {

    public static void main(String[] args) {
        EchoPackage echo = new EchoPackage();
        echo.echoPackage("E4084\r", "echoS2.txt");

        Image img = new Image();
        img.image("M3991\r", "imageS2.jpg");

        Image imgWithError = new Image();
        imgWithError.image("G2997\r", "imageErrorS2.jpg");

        Gps gps = new Gps();
        gps.gps("P5938", "0030", "99");

        Arq arq = new Arq();
        arq.arq("Q9566\r", "R3546\r", "timeS2.txt", "nacksS2.txt");

    }

}
