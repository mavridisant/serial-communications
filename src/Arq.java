package com.company;

import ithakimodem.Modem;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class Arq {

    public void arq(String ack_code, String nack_code, String filename1, String filename2){

        Modem modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(6000);
        modem.open("ithaki");

        int k;
        String response = "";
        int xOr = 0;
        String fcs = "";

        long loopTime = (4 * 60000);
        int packNum = 0;
        long packTime;
        long startLoop;
        long endTime = System.currentTimeMillis();
        int nacksSend = 0;
        int nacksPerPack = 0;

        Writer writer1 = null;
        Writer writer2 = null;

        try{
            writer1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename1), "utf-8"));
            writer1.write("Arq packages with ack_code: " + ack_code + "and nack_code: " + nack_code);
            ((BufferedWriter) writer1).newLine();
            ((BufferedWriter) writer1).newLine();
        }catch (Exception x) {
            System.out.println(x);
        }

        try{
            writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename2), "utf-8"));
            writer2.write("Arq Nacks per Package with ack_code: " + ack_code + "and nack_code: " + nack_code);
            ((BufferedWriter) writer2).newLine();
            ((BufferedWriter) writer2).newLine();
        }catch (Exception x) {
            System.out.println(x);
        }

        startLoop = System.currentTimeMillis();
        do{
            modem.write(ack_code.getBytes());
            response = "";

            do {
                k = modem.read();
                if (k == -1) {
                    System.out.println("System fail try again later! ");
                    break;
                }
                response += (char) k;

                if (response.contains("<")) {
                    k = modem.read();
                    response += (char) k;
                    xOr = k;
                    for (int i = 0; i < 15; i++) {
                        k = modem.read();
                        response += (char) k;
                        xOr ^= k;
                    }
                    k = modem.read();
                    response += (char) k;
                    k = modem.read();
                    response += (char) k;

                    for (int i = 0; i < 3; i++) {
                        k = modem.read();
                        response += (char) k;
                        fcs += (char) k;
                    }
                    for (int i = 0; i < 6; i++) {
                        k = modem.read();
                        response += (char) k;
                    }
                    System.out.println(response);
                    System.out.println(xOr);

                }
            } while (!response.contains("PSTOP"));

            if (xOr == Integer.valueOf(fcs)) {
                try {
                    packTime = (System.currentTimeMillis() - endTime);
                    packNum++;
                    modem.write(ack_code.getBytes());

                    writer1.write(packTime + "");
                    ((BufferedWriter) writer1).newLine();

                    writer2.write(nacksPerPack + "");
                    ((BufferedWriter) writer2).newLine();

                    nacksPerPack = 0;

                } catch (Exception x) {
                    System.out.println(x);
                    break;
                }
            } else {
                modem.write(nack_code.getBytes());
                nacksSend++;
                nacksPerPack++;
            }

            fcs = "";
            endTime = System.currentTimeMillis();

        }while(System.currentTimeMillis() < (loopTime + startLoop));

        try{
            ((BufferedWriter) writer1).newLine();
            writer1.write("Total number of packages: " + packNum);
            ((BufferedWriter) writer1).newLine();
            writer1.write("Total number of wrong pachages and nacks send: " + nacksSend);

            writer1.close();
            writer2.close();
        }catch (Exception x) {
            System.out.println(x);
        }

        System.out.println(packNum);

        modem.close();

        System.out.println(filename1 + " file created!");
        System.out.println(filename2 + " file created!");

    }
}
