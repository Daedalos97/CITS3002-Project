import java.io.*;
import java.net.InetAddress;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import net.Message;

//"-Djavax.net.ssl.trustStore=bin/Analysts/publicKey.jks"

public class main {
    
    public static void main(String[] args) {
        if (args.length == 2) {
            try {
                String current_dir = System.getProperty("user.dir") + "\\cacerts.jks";
                System.out.println(current_dir);
                System.setProperty("javax.net.ssl.trustStore", current_dir);
                System.setProperty("javax.net.ssl.trustStorePassword", "123456");
                
                SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
                sslsocket.startHandshake();
                System.out.println("Connected to Network");
                
                InputStream serverstream = sslsocket.getInputStream();
                InputStreamReader serverstreamreader = new InputStreamReader(serverstream);
                BufferedReader bufferedserverreader = new BufferedReader(serverstreamreader);
                
                OutputStream outputstream = sslsocket.getOutputStream();
                PrintWriter pwrite = new PrintWriter(outputstream, true);
                
                pwrite.println("REQBC:Need it fam");
                pwrite.flush();
                
                String string = null;
                while ((string = bufferedserverreader.readLine()) != null) {
                    Message m = new Message(string);
                    switch (m.getType()) {
                        case "REQRS":
                            System.out.println("Block-Chain Recieved");
                            break;
                        default:
                            System.out.println(m.getRawData());
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            System.out.println("You must enter atleast 2 variables");
            System.exit(-1);
        }
    }
}