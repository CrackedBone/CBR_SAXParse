import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class MySaxApp extends DefaultHandler {
    final private static String URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    String thisElement = "";

    public static void main(String args[])
            throws Exception {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        MySaxApp handler = new MySaxApp();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        URLConnection con = new URL(URL).openConnection();
        if (((HttpURLConnection) con).getResponseCode() == 200)
            try
                    (
                            InputStream is = con.getInputStream();
                    ) {
                xr.parse(new InputSource(is));
            }
        else
            throw new IOException("Connection failed");
    }


    public MySaxApp() {
        super();
    }

    public void startDocument() {
        System.out.println("Start document");
    }


    public void endDocument() {
        System.out.println("End document");
    }

    public void startElement(String uri, String name,
                             String qName, Attributes atts) throws SAXException {
        thisElement = qName;
    }

    public void endElement(String uri, String name, String qName, Attributes atts) throws SAXException {
        thisElement = "";
    }

    public void characters(char ch[], int start, int length) {

        String numCode = "";

        if (thisElement.equals("NumCode")) {
            for (int i = start; i < start + length; i++) {
                if (ch[i] != 0) {
                    numCode += ch[i];
                }
            }

        }
    }

}
