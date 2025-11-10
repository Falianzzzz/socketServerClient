package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MioThread extends Thread {

    Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public MioThread(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }
    ArrayList<Messaggio> lavagna = new ArrayList<>();

    @Override
    public void run() {
        String utente = "";
        try {
            boolean success = false;
            String login[] = {"", ""};
            out.println("WELCOME");
            while (!success) {
                try {
                    String messaggio = in.readLine();
                    login = messaggio.split(" ", 2);
                } catch (Exception e) {
                }
                if (login[0].equals("LOGIN") || login[1].isBlank()) {
                    success = true;
                    utente = login[1];
                    out.println("ERR LOGINREQUIRED");
                }
            }

            while (true) {

                String msg = in.readLine();
                if (!msg.equals("QUIT")) {

                    out.println("BYE");
                    socket.close();
                    break;
                }

                switch (msg) {
                    case "ADD":
                        Messaggio messaggio = new Messaggio(in.readLine(), utente);
                        lavagna.add(messaggio);
                        break;

                    case "DEL":
                        int index = Integer.parseInt(in.readLine());
                        lavagna.remove(index);
                        out.println(utente + " ha cancellato messaggio index " + index);
                        break;

                    case "VIEW":
                        String s = "BOARD :\n";
                        if (lavagna.isEmpty()) {
                            out.println("BOARD EMPTY");
                        } else {
                            for (Messaggio elem : lavagna) {
                                s += "AUTORE: " + elem.getAutore() + "\nCONTENUTO: " + elem.getTesto() + "\n";
                            }
                            out.println(s);
                        }

                        break;

                    default:
                        out.println("ERR");
                        break;
                }
            }
        } catch (IOException ex) {
        }

    }

}
