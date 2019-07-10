package com.example.marcos.last;

/**
 * Created by windows7 on 7/3/2019.
 */
import android.os.Environment;
import android.util.Xml;

import com.example.marcos.last.ListDatos;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by marcos on 1/28/2016.
 */
public class BaseDatos {

    public void CrearBaseDatosXML(ListDatos l) throws IOException
    {
        XmlSerializer ser = Xml.newSerializer();

        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()/*.concat("/dbanatomia/")*/, "/DataGPS/data.xml");

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));

        ser.setOutput(osw);
        if (!l.getEstado()) {
            ser.startTag("", "data_gps");

                ser.startTag("", "identificador");
                ser.text(l.getNombre());
                ser.endTag("", "identificador");

                ser.startTag("", "numero");
                ser.text(l.getNumero());
                ser.endTag("", "numero");

                ser.startTag("", "estado");
                ser.text(String.valueOf(l.getEstado()));
                ser.endTag("", "estado");

            ser.endTag("", "data_gps");
        }else {
            ser.startTag("", "data_gps");

            ser.startTag("", "identificador");
            ser.text(l.getNombre());
            ser.endTag("", "identificador");

            ser.startTag("", "numero");
            ser.text(l.getNumero());
            ser.endTag("", "numero");

            ser.startTag("", "estado");
            ser.text(String.valueOf(l.getEstado()));
            ser.endTag("", "estado");

            ser.endTag("", "data_gps");
        }
        ser.endDocument();
        osw.flush();
        osw.close();
    }
    public ListDatos LeerBaseDatosXML() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ListDatos terms = new ListDatos();

        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()/*.concat("/dbanatomia/")*/, "/DataGPS/data.xml");

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document dom = builder.parse(file);



            Element root = dom.getDocumentElement();


            NodeList items = root.getChildNodes();// getElementsByTagName("terminos");

            //  Node item2 = items.item(0);
            //  NodeList datosNoticia = item2.getChildNodes();




                for (int j = 0; j < items.getLength(); j++) {
                    Node dato = items.item(j);
                    String etiqueta = dato.getNodeName();

                    if (etiqueta.equals("identificador")) {
                        String texto = obtenerTexto(dato);
                        terms.setNombre(texto);
                    } else if (etiqueta.equals("numero")) {
                        String texto = obtenerTexto(dato);
                        terms.setNumero(texto);
                    } else if (etiqueta.equals("image")) {
                        String texto = obtenerTexto(dato);
                        terms.setEstado(Boolean.parseBoolean(texto));
                    }
                }



        } catch(Exception ex) {
            //throw new RuntimeException(ex);

            return terms;
        }
        return terms;
    }
    private String obtenerTexto(Node dato)
    {
        StringBuilder texto = new StringBuilder();
        NodeList fragmentos = dato.getChildNodes();

        for (int k=0;k<fragmentos.getLength();k++)
        {
            texto.append(fragmentos.item(k).getNodeValue());
        }
        return texto.toString();
    }
}