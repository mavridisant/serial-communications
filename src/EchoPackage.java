package com.company;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import ithakimodem.Modem;

public class EchoPackage {

    public void echoPackage(String request_echo, String filename){

        Modem modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(6000);
        int k;
        int packNum = 0;
        long startLoop;
        long packTime = 0;
        long loopTime = (4 * 60000);
        long endTime = System.currentTimeMillis();

        modem.open("ithaki");

        Writer writer = null;

        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
            writer.write("Echo data with request code: " + request_echo);
            ((BufferedWriter) writer).newLine();
            ((BufferedWriter) writer).newLine();
        }catch (Exception x) {
            System.out.println(x);
        }

        startLoop = System.currentTimeMillis();
        do {
            modem.write(request_echo.getBytes());
            String response = "";
            for (; ; ) {
                try {
                    k = modem.read();
                    if (k == -1) {
                        System.out.println("System fail try again later! ");
                        break;
                    }
                    response += (char)k;

                    if (response.contains("PSTOP")) {
                        packTime = (System.currentTimeMillis() - endTime);
                        packNum++;

                        writer.write(packTime + "");
                        ((BufferedWriter) writer).newLine();

                        break;
                    }
                } catch (Exception x) {
                    System.out.println(x);
                    break;
                }

            }
            System.out.println(response);
            endTime = System.currentTimeMillis();


        }while (System.currentTimeMillis() < (loopTime + startLoop));

        try{
            ((BufferedWriter) writer).newLine();
            writer.write("Total number of packages: " + packNum);

            writer.close();
        }catch (Exception x) {
            System.out.println(x);
        }

        System.out.println(packNum);

        modem.close();

        System.out.println(filename + " file created!");

    }
}
