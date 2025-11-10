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

                if (login.length == 2 && login[0].equals("LOGIN") && !login[1].isBlank()) {
                    utente = login[1];
                    success = true;
                    out.println("LOGIN OK " + utente);
                } else {
                    out.println("ERR LOGINREQUIRED");
                }
            }

            // --- LOOP PRINCIPALE ---
            while (true) {
                String cmd = in.readLine();
                if (cmd == null) {
                    break;
                }

                switch (cmd) {
                    case "ADD":
                        String testo = in.readLine();
                        if (testo != null) {
                            synchronized (lavagna) {
                                lavagna.add(new Messaggio(testo, utente));
                            }
                            out.println("MSG ADDED");
                        }
                        break;

                    case "DEL":
                        int index = Integer.parseInt(in.readLine());
                        synchronized (lavagna) {
                            if (index >= 0 && index < lavagna.size()) {
                                lavagna.remove(index);
                                out.println("MSG " + index + " DELETED BY " + utente);
                            } else {
                                out.println("ERR INVALIDINDEX");
                            }
                        }
                        break;

                    case "VIEW":
                        synchronized (lavagna) {
                            if (lavagna.isEmpty()) {
                                out.println("BOARD EMPTY");
                            } else {
                                out.println("BOARD:");
                                for (int i = 0; i < lavagna.size(); i++) {
                                    Messaggio m = lavagna.get(i);
                                    out.println(i + ") " + m.getAutore() + ": " + m.getTesto());
                                }
                            }
                        }
                        break;

                    case "QUIT":
                        out.println("BYE");
                        socket.close();
                        return;

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
