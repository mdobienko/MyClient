package com.md;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSource {

    //wzór na poprawy 4-cyfrowy zakres
    private static String newPattern = "^[0-9]{4}$";

    //funkcja korygujaca wprowadzanie danych do zakresy liczb tylko - niezależnie jaki dług ciąg liczbowy by był
    public static int getInt() {

        Scanner s = new Scanner(System.in);

        System.out.println("Enter socket, preferred from 5000 to 5555, current range 1-9999");

        while (true) {
            try {
                return s.nextInt();

            } catch (InputMismatchException e) {
                s.nextLine();
                System.out.println("Incorrect, please try again");

            }
        }

    }
//wprowadzanie danych
    public void EnterData() {

        int mySocketInt = getInt();
        String mySocketString = Integer.toString(mySocketInt);

        Pattern pattern = Pattern.compile(newPattern);
        Matcher matcher = pattern.matcher(mySocketString);
        matcher.reset();

        if (matcher.find()) {
            System.out.println("Correct current socket is: " + mySocketInt);

            try (Socket socket = new Socket("localhost", mySocketInt)) {
                BufferedReader echoes = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter stringToEcho = new PrintWriter(socket.getOutputStream(), true);

                Scanner scanner = new Scanner(System.in);
                String echoString;
                String response;

                do {
                    System.out.println("Enter position: ");
                    echoString = scanner.nextLine();
                    stringToEcho.println(echoString);

                    if (!echoString.equals("exit")) {
                        response = echoes.readLine();
                        System.out.println(response);
                    }
                } while (!echoString.equals("exit"));

            } catch (IOException e) {
                System.out.println("Client Error: " + e.getMessage());

            }
        } else {
            System.out.println("Not available range, please try next time");
        }
    }
}
