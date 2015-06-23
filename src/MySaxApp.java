import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

class CurrencyRate {
    String numCode;
    String charCode;
    int nominal;
    String name;
    BigDecimal value;

}

public class MySaxApp extends DefaultHandler {
    final private static String URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    String curElement = "";
    static CurrencyRate current;
    static String code;

    public static void main(String args[])
            throws Exception {
        if (args.length > 0){
            code = args[0];
            //TODO: check the right currency numCode
        }
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
        //System.out.println("Start document");
    }


    public void endDocument() {
        //System.out.println("End document");
    }

    public void startElement(String uri, String name,
                             String qName, Attributes atts) throws SAXException {
        curElement = qName;
        if (qName.equals("Valute")) {
            current = new CurrencyRate();
        }

    }

    public void endElement(String uri, String name, String qName) throws SAXException {
        if (qName.equals("Valute")) {
            if (current.numCode.equals(code)){
                System.out.println(current.nominal + " " + current.name + " " + current.value);
                //TODO: throw exception to stop parsing if needed currency found
            }
        }
        curElement = "";
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (curElement.equals("NumCode")) {
            current.numCode = new String(ch, start, length);
        }
        if (curElement.equals("CharCode")) {
            current.charCode = new String(ch, start, length);
        }
        if (curElement.equals("Nominal")) {
            current.nominal = new Integer(new String(ch, start, length));
        }
        if (curElement.equals("Name")) {
            current.name = new String(ch, start, length);
        }
        if (curElement.equals("Value")) {
            String value = new String(ch, start, length);
            current.value = new BigDecimal(value.replace(",", "."));
        }
    }

}
