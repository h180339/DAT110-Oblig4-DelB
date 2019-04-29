package no.hvl.dat110.aciotdevice.client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RestClient {

    public RestClient() {
        // TODO Auto-generated constructor stub
    }

    private static String logpath = "/accessdevice/log";
    private static int port = 8080;
    private static String host = "localhost";
    private String getURI = "/accessdevice/code";
    private String postUri = "/accessdevice/log/";

    public void doPostAccessEntry(String message) {

        // TODO: implement a HTTP POST on the service to post the message
        try (Socket s = new Socket(host, port)) {

            // construct the HTTP request
            Gson gson = new Gson();
            AccessMessage accessMessage = new AccessMessage(message);
            String jsonbody = gson.toJson(accessMessage);
            //System.out.println("JSON BODY::" + jsonbody);

            String httpputrequest =
                    "POST " + postUri + " HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "Content-type: application/json\r\n" +
                            "Content-length: " + jsonbody.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n" +
                            jsonbody +
                            "\r\n";

            // send the response over the TCP connection
            OutputStream output = s.getOutputStream();

            PrintWriter pw = new PrintWriter(output, false);
            pw.print(httpputrequest);
            pw.flush();

            // read the HTTP response
            InputStream in = s.getInputStream();

            Scanner scan = new Scanner(in);
            StringBuilder jsonresponse = new StringBuilder();
            boolean header = true;

            while (scan.hasNext()) {

                String nextline = scan.nextLine();

                if (header) {
                    //System.out.println(nextline);
                } else {
                    jsonresponse.append(nextline);
                }

                if (nextline.isEmpty()) {
                    header = false;
                }

            }

            System.out.println("BODY:");
            System.out.println(jsonresponse.toString());

            scan.close();

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private static String codepath = "/accessdevice/code";

    public AccessCode doGetAccessCode() {

        AccessCode code = null;

        try (Socket s = new Socket(host, port)) {

            // construct the GET request
            String httpgetrequest = "GET " + getURI + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
                    + "Host: localhost\r\n" + "Connection: close\r\n" + "\r\n";

            // sent the HTTP request
            OutputStream output = s.getOutputStream();

            PrintWriter pw = new PrintWriter(output, false);

            pw.print(httpgetrequest);
            pw.flush();

            // read the HTTP response
            InputStream in = s.getInputStream();

            Scanner scan = new Scanner(in);
            StringBuilder jsonresponse = new StringBuilder();
            boolean header = true;

            while (scan.hasNext()) {

                String nextline = scan.nextLine();

                if (header) {
                    System.out.println(nextline);
                } else {
                    jsonresponse.append(nextline);
                }

                // simplified approach to identifying start of body: the empty line
                if (nextline.isEmpty()) {
                    header = false;
                }

            }

            // TODO: implement a HTTP GET on the service to get current access code
            Gson gson = new Gson();
            code = gson.fromJson(jsonresponse.toString(), AccessCode.class);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }
}
