package com.company;

import java.lang.Math;
import ithakimodem.Modem;

public class Gps {

    public void gps(String code, String  m, String n){
        Modem modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(6000);
        int k;

        String response = "";

        String first_code = code + "R=1" + m + n + "\r";
        String final_code = code;


        modem.open("ithaki");
        modem.write(first_code.getBytes());

        for(;;)
        {
            k = modem.read();
            response += (char)k;

            if(response.contains("START ITHAKI GPS TRACKING\r\n"))
                break;
        }



        for(int i = 0; i < Integer.parseInt(n); i++)
        {
            String response_row = "";
            String latitude = "";
            String longitude = "";
            String decN = "";
            String decE = "";

            for(;;)
            {
                k = modem.read();
                response_row += (char) k;
                System.out.print((char)k);

                if (response_row.contains("\r\n"))
                {
                    for (int j = 18; j < 22; j++) {
                        latitude += response_row.charAt(j);
                    }

                    for (int j = 23; j < 27; j++) {
                        decN += response_row.charAt(j);
                    }
                    int intdecN = Integer.parseInt(decN);
                    long longdecN = (intdecN * 6) / 1000;
                    intdecN = Math.round(longdecN);

                    latitude += String.valueOf(intdecN);
                    System.out.println(latitude);

                    for (int j = 31; j < 35; j++) {
                        longitude += response_row.charAt(j);
                    }

                    for (int j = 36; j < 40; j++) {
                        decE += response_row.charAt(j);
                    }
                    int intdecE = Integer.parseInt(decE);
                    long longdecE = (intdecE * 6) / 1000;
                    intdecE = Math.round(longdecE);

                    longitude += String.valueOf(intdecE);
                    System.out.println(longitude);

                    if(i==0 || i % 20 == 0 )
                        final_code += "T=" + longitude + latitude;

                    break;
                }
            }
        }
        final_code += "\r";
        System.out.println(final_code);

        modem.close();

        Image img = new Image();
        img.image(final_code, "gpsS2.jpg");



    }
}
