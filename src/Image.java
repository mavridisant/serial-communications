package com.company;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import ithakimodem.Modem;

public class Image {

    public void image(String request_image, String filename) {

        Modem modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(6000);
        int k;


        ArrayList<Byte> image = new ArrayList<Byte>();

        modem.open("ithaki");

        modem.write(request_image.getBytes());

        for (;;){
            k = modem.read();

            if ((byte) k == -1) {
                image.add((byte)k);
                System.out.println((byte)k);
                k = modem.read();

                if ((byte) k == -40) {
                    image.add((byte)k);
                    System.out.println((byte)k);
                    break;
                }
                else
                    image.clear();
            }
        }


        for (;;) {
            k = modem.read();

            if (k == -1) {
                System.out.println("System fail try again later! ");
                break;
            }
            image.add((byte)k);
            System.out.println((byte)k);

            if (image.get(image.size()-1) == -39) {
                if (image.get(image.size()-2) == -1) {
                    System.out.println(image.size());
                    break;
                }
            }

        }



        byte[] imageArray = new byte[image.size()];
        for (int i = 0; i < image.size(); i++) {
            imageArray[i] = image.get(i);
        }


        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(imageArray);
            BufferedImage bImage = ImageIO.read(bis);
            ImageIO.write(bImage, "jpg", new File(filename));
            System.out.println("image created");
        } catch (Exception x) {
            System.out.println(x);
        }


        modem.close();

    }
}