package com.example.dimanxe.practica3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by dimanxe on 29/12/2016.
 */

public class Conect implements Runnable {
    private Autentication aut = new Autentication();
    private Billete bil = new Billete();

    public Conect(Autentication mAut, Billete mBil) {
        this.aut = mAut;
        this.bil = mBil;
    }

    @Override
    public void run() {
        Logeo log = new Logeo();
        log.setUser(aut.getmUser());
        log.setPass(aut.getmPass());
        Socket sClient = null;
        DataOutputStream output = null;

        BufferedReader is = null;
        try {
            SocketAddress sockaddr = new InetSocketAddress(aut.getmIP(), aut.mPort);
            sClient = new Socket();
            sClient.connect(sockaddr, 5000);
            is = new BufferedReader(new InputStreamReader(sClient.getInputStream()));
            output = new DataOutputStream(sClient.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        String resp = "";
        int count = 0;
        if (sClient != null && is != null && output != null) {
            try {
                output.writeBytes("USER "+aut.getmUser()+"\r\n");//TODO añadir peticion del servicio y enviar quit y enviar respuesta
                output.writeBytes("PASS "+aut.getmPass()+"\r\n");
                output.writeBytes("1 ORG "+bil.getOrigen()+"\r\n");
                output.writeBytes("1 DST "+bil.getDestino()+"\r\n");
                output.writeBytes("1 FCH "+bil.getFecha()+"\r\n");
                output.writeBytes("QUIT\r\n");
                String responseLine;
                while ((responseLine = is.readLine()) != null) {
                    if (count == 2) {
                        resp = responseLine;
                        break;
                    }
                    if (responseLine.indexOf("OK") != -1) {
                        count += 1;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}