package com.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class MioThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static ArrayList<Messaggio> lavagna = new ArrayList<>();

    public MioThread(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        String utente = "";

        try {
            out.println("WELCOME");

            // --- LOGIN ---
            boolean success = false;
            while (!success) {
                String messaggio = in.readLine();
                if (messaggio == null) {
                    break;
                }
                String[] login = messaggio.split(" ", 2);

                if (login.length == 2 && login[0].equals("LOGIN") || !(login[1] == "")) {
                    utente = login[1];
                    success = true;
                    out.println("OK");
                } else {
                    out.println("ERR LOGINREQUIRED");
                }
            }

            // --- LOOP PRINCIPALE ---
            String[] cmd = {"", ""};
            while (true) {
                // String cmd = in.readLine().substring(0, 3);
                String linea = in.readLine();
                cmd = linea.split(" ", 2);

                if (cmd[0].equals("QUIT")) {
                    out.println("BYE");
                    socket.close();
                    break;
                }

                switch (cmd[0]) {
                    case "ADD":
                        // String testo = in.readLine();
                        if (cmd[1] != null) {
                            synchronized (lavagna) {
                                lavagna.add(new Messaggio(cmd[1], utente));
                                out.println("OK");
                            }
                        }
                        break;

                    case "DEL":
                        int index = Integer.parseInt(cmd[1]);
                        synchronized (lavagna) {
                            if (index >= 0 && index <= lavagna.size()) {
                                lavagna.remove(index);
                            } else {
                                out.println("ERR INVALIDINDEX");
                            }

                        }
                        break;

                    case "LIST":
                        synchronized (lavagna) {
                            if (lavagna.isEmpty()) {
                                out.println("BOARD:\nEND");
                            } else {
                                out.println("BOARD:");
                                for (int i = 0; i < lavagna.size(); i++) {
                                    Messaggio m = lavagna.get(i);
                                    out.println(i + ") " + m.getAutore() + ": " + m.getTesto());
                                }
                                out.println("END");
                            }
                        }
                        break;

                    default:
                        out.println("ERR UNKNOWNCMD");
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nel thread: " + e.getMessage());
        }
    }
}
