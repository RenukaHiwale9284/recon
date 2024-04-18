package com.anemoi.itr;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.anemoi.itr.ref.RefDataUtiity;
import com.anemoi.itr.xml.SimpleNamespaceContext;

public class XMLITR6File extends ITR6File
	{
	String command ;
	Document doc ;
	XPath xpath ;
		
	
	public XMLITR6File()
		{
		super.type = "XML" ;
		}
	public static void main(String[] argv) throws Exception
		{
		File xmlFile = new File("ITR6_DATA.xml") ;
		
		XMLITR6File docproc = new XMLITR6File();
		
		docproc.init(xmlFile, "read");
		String tag = "//ITRForm:PartA_GEN2For6//ITRForm:FrnCompUltimatePrntCompDtls[1]//ITRForm:CountryOfResidence" ;
		
//		String str = docproc.readSingleValue("//ITRForm:PAN/text()");
//		String str = docproc.readTableValue("//ITRForm:PartA_GEN2For6//ITRForm:FrnCompUltimatePrntCompDtls", 0, "//ITRForm:CountryOfResidence") ;
		
//		String str = docproc.readSingleValue(tag);
		
		System.out.print("\nValue=" + docproc.getLastTag(tag) + ":::");
		
//		List<String> str = docproc.readMultipleValue("//ITRForm:PartA_GEN1//ITRForm:PAN/text()");
		}

	
	public void init( File file , String operation) throws Exception
		{
				
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // never forget this!

        // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all
        // XML entity attacks are prevented
        // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
        String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
        factory.setFeature(FEATURE, true);

        // If you can't completely disable DTDs, then at least do the following:
        // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
        // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
        // JDK7+ - http://xml.org/sax/features/external-general-entities
        //This feature has to be used together with the following one, otherwise it will not protect you from XXE for sure
        FEATURE = "http://xml.org/sax/features/external-general-entities";
        factory.setFeature(FEATURE, false);

        // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
        // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
        // JDK7+ - http://xml.org/sax/features/external-parameter-entities
        //This feature has to be used together with the previous one, otherwise it will not protect you from XXE for sure
        FEATURE = "http://xml.org/sax/features/external-parameter-entities";
        factory.setFeature(FEATURE, false);

        // Disable external DTDs as well
        FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
        factory.setFeature(FEATURE, false);

        // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

                
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        
        doc = builder.parse(file);	
        
               
        XPathFactory xpathfactory = XPathFactory.newInstance();
        xpath = xpathfactory.newXPath();
       
        HashMap<String, String> prefMap = new HashMap<String, String>() {{

        	put("ITRETURN", "http://incometaxindiaefiling.gov.in/main" );
            put("ITR6FORM", "http://incometaxindiaefiling.gov.in/ITR6");
            put("ITRForm", "http://incometaxindiaefiling.gov.in/Corpmaster");
            
            
            
        }};
        
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext(prefMap);
        xpath.setNamespaceContext(namespaces);
        
        
        this.command = operation ;        
		}
		

	public List<String> readMultipleValue(String xpression) throws Exception
		{
		ArrayList<String> values = new ArrayList<String>();
		XPathExpression expr = xpath.compile(xpression);
	    Object result = expr.evaluate(doc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) result;
	    System.out.print("\n Nodes ::: " + nodes.getLength());
	    
	    for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
            values.add(nodes.item(i).getNodeValue());
        	}
	    return values ;
		}
	
	
	
	public String readTableValue(String tblxpression, int rowId, String xpression) throws Exception
		{
	    System.out.print("\nReading XML file : Xpression --> " + xpression + ")" );
		
		if(xpression == null || xpression.trim().length() ==0)
			return "NA" ;
		

		XPathExpression expr = xpath.compile(tblxpression);
		XPath xnew = XPathFactory.newInstance().newXPath();
		
	    NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		System.out.print("\n Table Length : " + nodes.getLength());
		showNodeList(nodes, "row");

	    Element rowelement = (Element)nodes.item(rowId);
	   
		XPathExpression subexpr = xpath.compile("." + xpression + "/text()");

		NodeList subnodes = (NodeList) subexpr.evaluate(rowelement, XPathConstants.NODESET);
				
		showNodeList(subnodes, "Column");
		
		System.out.print("\n Value Length : " + subnodes.getLength());

        if( subnodes.getLength() > 1 )
        	throw new Exception("ERR42031: Multiple values exit for the path" + xpression) ;
        
        if( subnodes.getLength() == 0 )
        	return "NA" ;
        
        if( subnodes.item(0).getNodeValue() == null || subnodes.item(0).getNodeValue().equalsIgnoreCase("null") )
        	return "NA" ;

		
		System.out.print("\n Table Length : " + nodes.getLength());
				
        return subnodes.item(0).getNodeValue() ;
		}
	
	
	
	public String readSingleValue(String xpression) throws Exception
		{
		String nullValue = "" ;
		
		String lastTag = getLastTag(xpression);
        System.out.print("\nReading XML file : Xpression --> " + xpression + ")" );
		
		if(xpression == null || xpression.trim().length() ==0)
			return nullValue ;
		
		xpression = xpression + "/text()" ;
		
		XPathExpression expr = xpath.compile(xpression);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        
        NodeList nodes = (NodeList) result;
        System.out.print("\n Nodes ::: " + nodes.getLength());
        
        if( nodes.getLength() > 1 )
        	throw new Exception("ERR42031: Multiple values exit for the path" + xpression) ;
        
        if( nodes.getLength() == 0 )
        	return nullValue ;
        
        if( nodes.item(0).getNodeValue() == null || nodes.item(0).getNodeValue().equalsIgnoreCase("null") )
        	return nullValue ;
        
        
        System.out.print("\nReading XML file : " + nodes.item(0).getNodeValue() + " --> Expr(" + xpression + ")" );
        
        String value = nodes.item(0).getNodeValue() ;
        
        value = RefDataUtiity.one().keyMapping( lastTag, value);
        
        return  value;
		}
	
	
	public String getLastTag(String xpression)
		{
		int index1 = xpression.lastIndexOf("/");
		String lastTag = xpression.substring(index1+1);
		
		int index2 = lastTag.lastIndexOf(":");
		
		System.out.print("\nChar pos at :" + lastTag + " positioned at" + index2);
		
		if(index2 != -1) lastTag = lastTag.substring(index2+1);
		
		return lastTag ;
		}
	
	
	
	
	
	
	
    public static void test() throws Exception 
    	{
        //Build DOM    	
    	
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse("ITR6_DATA.xml");	
        
        
        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
       
       
 
        //Create XPath
         System.out.println("\n ITR-6 XML file parsing ::::::::::::::::::::::::::::: ");
 
         // 1) Get book titles written after 2001
        XPathExpression expr = xpath.compile("//ITRForm:PAN/text()");
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        System.out.print("\n Nodes ::: " + nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }
 
    }

	
    public void showNodeList(NodeList nodes , String tag)
    	{
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.print("\n" + tag + "-->" + nodes.item(i).getTextContent() + " -->" + nodes.item(i).getNamespaceURI());
        	}
    	}

    
    public int countRows(String xpression) throws Exception {
    	
    	System.out.print("\nReading Table Lenght : Xpression --> " + xpression + ")" );
  		
  		if(xpression == null || xpression.trim().length() ==0)
  			{
  			return 0 ;
  			}
    	
		XPathExpression expr = xpath.compile(xpression);
	    Object result = expr.evaluate(doc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) result;
	    System.out.print("\n Nodes ::: " + nodes.getLength());
	    return nodes.getLength() ;
	    }
    
	}
