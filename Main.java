import org.w3c.dom.*;
import org.xml.sax.*;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;

public class Main {
    public static void main(String[] args){

        try {
            String filePath = "sample-xml.template.xml";
            FileWriter writer = new FileWriter("output.csv");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(filePath);

            document.getDocumentElement().normalize();

            NodeList nodeList = document.getDocumentElement().getChildNodes();
            List<String> headers = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList fields = node.getChildNodes();
                    for (int j = 0; j < fields.getLength(); j++) {
                        if (fields.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            String fieldName = fields.item(j).getNodeName();
                            if (!headers.contains(fieldName)) {
                                headers.add(fieldName);
                            }
                        }
                    }
                }
            }
            writer.write(String.join(",", headers) + "\n");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList fields = node.getChildNodes();
                    List<String> row = new ArrayList<>();
                    for (String header : headers) {
                        boolean valueFound = false;
                        for (int j = 0; j < fields.getLength(); j++) {
                            if (fields.item(j).getNodeType() == Node.ELEMENT_NODE &&
                                    fields.item(j).getNodeName().equals(header)) {
                                String string = fields.item(j).getTextContent().trim();
                                row.add(string);
                                valueFound = true;
                                break;
                            }
                        }
                        if (!valueFound) {
                            row.add("");
                        }
                    }
                    writer.write(String.join(",", row) + "\n");
                }
            }

            writer.close();

        } catch (SAXException e) {
            // Handle parsing errors (e.g., mismatched or invalid XML structure)
            System.err.println("Error: The XML file is not well-formed.");
            System.err.println("Details: " + e.getMessage());
        } catch (IOException e) {
            // Handle file-related errors (e.g., file not found)
            System.err.println("Error: Unable to read the XML file.");
            System.err.println("Details: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other general exceptions
            System.err.println("An unexpected error occurred.");
            System.err.println("Details: " + e.getMessage());
        }
    }
}
