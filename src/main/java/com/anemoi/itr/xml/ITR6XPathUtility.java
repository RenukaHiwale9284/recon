package com.anemoi.itr.xml;


import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
 
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class ITR6XPathUtility 
{
    public static void main(String[] args) throws Exception 
    {
        //Build DOM    	
    	
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse("ITR6_DATA.xml");	
        
        
        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
       
        HashMap<String, String> prefMap = new HashMap<String, String>() {{
            put("ITRETURN", "http://incometaxindiaefiling.gov.in/main" );
            put("ITR6FORM", "http://incometaxindiaefiling.gov.in/ITR6");
            put("ITRForm", "http://incometaxindiaefiling.gov.in/Corpmaster");
            
        }};
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext(prefMap);
        xpath.setNamespaceContext(namespaces);
 
        //Create XPath
         System.out.println("\n ITR-6 XML file parsing ::::::::::::::::::::::::::::: ");
 
         // 1) Get book titles written after 2001
         XPathExpression expr = xpath.compile("//ITRForm:PartA_GEN1//ITRForm:PAN/text()");
         //XPathExpression expr = xpath.compile("//ITRForm:PartA_GEN1//ITRForm:PAN");
      
        
        
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        System.out.print("\n Nodes ::: " + nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }
 
   }
}