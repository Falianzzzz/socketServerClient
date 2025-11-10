package it.proiettoclient;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        Socket clientSocket = new Socket("localhost", 3000);
        BufferedReader in = new BufferedReader(new java.io.InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        if (in.readLine().equals("WELCOME")) {
            System.out.println("\nConnessione effettuata.");
        }

        do {
            System.out.println("Inserire nome utente: ");
            String user = scanner.nextLine();
            out.println("LOGIN " + user);
            String answer = in.readLine();
            System.out.println(answer);

            if (answer.equals("OK")) {
                System.out.println("\nLogin effettuato con successo.");
                break;
            } else {
                System.out.println("\nErrore reinserire credenziali.");
            }
        } while (true);

        String command;
        do {
            System.out.println("\nInserisci l'operazione da fare sulla lavagna\n1: add\n2: delete\n3: view\n4: quit");
            command = scanner.nextLine();
            switch (command) {
                case "add":
                    System.out.println("Inserisci l'argomento da aggiungere: ");
                    String arg = scanner.nextLine();
                    out.println("ADD " + arg);
                    break;

                case "delete":
                    System.out.println("Inserisci l'id dell'elemento da eliminare: ");
                    String id = scanner.nextLine();
                    out.println("DEL " + id);
                    break;

                case "view":
                    out.println("LIST");
                    //visualizzazione della lista

                    while (true) {
                        String line = in.readLine();
                        if (line.equals("END")) {
                            System.out.println(line);
                            break;
                        }
                        System.out.println(line);
                    }

                    break;

                case "quit":
                    out.println("QUIT");
                    //chiusura del socket e quindi del client
                    System.out.println("Connessione con il server interrotta");
                    return;

                default:
                    System.out.println("comando inesistente reinserire.\n");
                    break;
            }
        } while (!command.equals("quit"));

    }
}
