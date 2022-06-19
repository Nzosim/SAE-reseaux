import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * classe LectureFichierXML qui permet de lire la configurations dans le fichier XML
 */
public class LectureFichierXML {

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

    /**
     * methode static de la classe LectureFichierXML qui ouvre le fichier XML et initialise les attributs
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static void LectureConfiguration() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse("src/configuration.xml");
        doc.getDocumentElement().normalize();

        String port = doc.getElementsByTagName("port").item(0).getTextContent();
        PORT = port != "" ? Integer.parseInt(port) : 80;
        String root = doc.getElementsByTagName("root").item(0).getTextContent();
        ROOT = root != "" ? root : "src";
        INDEX = Boolean.parseBoolean(doc.getElementsByTagName("index").item(0).getTextContent());
        ACCEPT = doc.getElementsByTagName("accept").item(0).getTextContent();
        REJECT = doc.getElementsByTagName("reject").item(0).getTextContent();
    }
}