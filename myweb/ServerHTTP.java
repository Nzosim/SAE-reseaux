import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ServerHTTP {
    private static Socket socket;
    /**
     * attribut PORT qui est le port utilise pour la connection
     */
    public static int PORT;
    /**
     * attribut ROOT qui est le dossier racine du serveur
     */
    public static String ROOT;
    /**
     * attribut INDEX qui est un boolean qui permet de savoir si l'index est utilise
     */
    public static boolean INDEX;
    /**
     * attribut ACCEPT qui contient l adresse IP autorise
     */
    public static String ACCEPT;
    /**
     * attribut REJECT qui contient l adresse IP refuse
     */
    public static String REJECT;

    public static void main(String[] args) {
        try {
            ServerSocket serveur = new ServerSocket(PORT);
            while (true) {
                LectureConfiguration();
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
            LectureConfiguration();

            OutputStream os = socket.getOutputStream();
            BufferedInputStream is = new BufferedInputStream(socket.getInputStream());

            byte[] buffer = new byte[1024];
            int nbLecture = is.read(buffer);
            String requete = new String(buffer, 0, nbLecture, StandardCharsets.UTF_8);
            String[] requeteTab = requete.split(" ");
            File file = new File(ROOT + requeteTab[1]);
            FileInputStream fl = new FileInputStream(file);
            byte[] arr = new byte[(int) file.length()];

            fl.read(arr);
            os.write(arr);
            fl.close();
            os.close();
        } catch (Exception e) {}
    }

    /**
     * methode qui ouvre le fichier XML et assigne les attributs au valeurs présentent dans le fichier de configuration
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static void LectureConfiguration() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        // récupération du fichier de configuration
        Document doc = db.parse("/etc/myweb/myweb.conf");
        doc.getDocumentElement().normalize();

        // récupération du port, si le port n'est pas renseigné on prend le port 80
        String port = doc.getElementsByTagName("port").item(0).getTextContent();
        PORT = port != "" ? Integer.parseInt(port) : 80;
        // récupération de la racine, si la racine n'est pas renseigné on prend src comme racine
        String root = doc.getElementsByTagName("root").item(0).getTextContent();
        ROOT = root != "" ? root : "src";
        // récupération de la condition de l'index
        INDEX = Boolean.parseBoolean(doc.getElementsByTagName("index").item(0).getTextContent());
        // récupération des adresses IP acceptées
        ACCEPT = doc.getElementsByTagName("accept").item(0).getTextContent();
        // récupération des adresses IP rejetées
        REJECT = doc.getElementsByTagName("reject").item(0).getTextContent();
    }
    
    /**
     * Methode qui permet de verifier si l'adresse IP est valide
     * @param ip l'adresse IP du client
     * @return true si l'adresse IP est valide, false sinon
     */
    public static boolean ipValide(String ip) {

        boolean valide = false;
        String ip2 = "192:168:0:0";
        //Transforme l ip du client en un tableau de string
        String[] s = ip.split(":");


        //String qui recuperer le nombre d'occurence
        int fin = Integer.parseInt(REJECT.split("/")[1]);
        //transforme l ip du client en un tableau de string
        String[] rejet = REJECT.split("\\.");
        String[] accept = ACCEPT.split("\\.");
        //Parcours des tableaux
        for (int i = 0; i < fin / 8; i++) {
            //Si l ip du client est egale a l ip autorise
            if (s[i].equals(rejet)) {
                valide = false;
                break;
            }
            if (!s[i].equals(accept)) {
                valide = false;
                break;
            }
        }
        return valide;
    }
}
