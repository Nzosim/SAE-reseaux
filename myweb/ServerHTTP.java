import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ServerHTTP {
    private static Socket socket;

    public static void main(String[] args) {
        try {
            ServerSocket serveur = new ServerSocket(80);
            while (true) {
                socket = serveur.accept();
                String ip = String.valueOf(socket.getLocalAddress());
                System.out.println(ip);
                Chargement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Chargement() {
        try {

            OutputStream os = socket.getOutputStream();
            BufferedInputStream is = new BufferedInputStream(socket.getInputStream());

            byte[] buffer = new byte[1024];
            int nbLecture = is.read(buffer);
            String requete = new String(buffer, 0, nbLecture, StandardCharsets.UTF_8);
            String[] requeteTab = requete.split(" ");
            File file = new File("/bin/myweb"+requeteTab[1]);
            FileInputStream fl = new FileInputStream(file);
            byte[] arr = new byte[(int) file.length()];

            fl.read(arr);
            os.write(arr);
            fl.close();
            os.close();
        } catch (Exception e) {}
    }
}
