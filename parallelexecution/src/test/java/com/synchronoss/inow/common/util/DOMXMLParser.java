package com.synchronoss.inow.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xpath.internal.XPathAPI;

public class DOMXMLParser {

	private Document doc = null;

	Logger logger = Logger.getLogger(DOMXMLParser.class.getName());

	public DOMXMLParser() {
	}

	public void setNodeValue(String xPath, String value, int index) {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");
		}

		if (!xPath.endsWith("/text()")) {
			xPath = xPath + "/text()";
		}

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if ((nodelist != null) && (nodelist.getLength() > index)) {
				nodelist.item(index).setNodeValue(value);
			}
		} catch (TransformerException e) {
		}
	}

	public void setNodeValue(String xPath, String value) {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		if (!xPath.endsWith("/text()")) {
			xPath = xPath + "/text()";
		}

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if ((nodelist != null) && (nodelist.getLength() > 0)) {
				nodelist.item(0).setNodeValue(value);
			}
		} catch (TransformerException e) {
		}
	}

	public void removeNodes(String parent, Collection<Node> nodeList) {

		if (nodeList == null || parent == null) {
			System.out.println("document object or nodeList or parent is null");
		}
		if (nodeList != null) {
			for (Node n : nodeList) {
				try {
					Node a = getNode(parent);
					a.removeChild(n);

				} catch (Exception e) {
					System.out.println("Exception occurred while removing node " + e);
				}
			}
		}

	}

	public void removeNode(String xPath) {
		System.out.println("DOMXMLParser::removeNode::START");

		try {
			if ((doc == null) || (xPath == null)) {
				System.out.println("document or xPath is null");
			} else {
				Element docElement = doc.getDocumentElement();
				System.out.println("Local name:" + docElement.getLocalName());
				System.out.println("Node name:" + docElement.getNodeName());

				Node nodeTobeRemoved = XPathAPI.selectSingleNode(docElement, xPath, doc);

				if (nodeTobeRemoved != null) {
					System.out.println("Local name:" + nodeTobeRemoved.getLocalName());
					System.out.println("Node name:" + nodeTobeRemoved.getNodeName());
					docElement.removeChild(nodeTobeRemoved);
				} else {
					System.out.println("Node to be removed not found:" + xPath);
				}
			}
		} catch (TransformerException e) {
			System.out.println("TransformerException Occured");
		}

		System.out.println("DOMXMLParser::removeNode::END");
	}

	public static void main(String[] args) {
		DOMXMLParser dOMXMLParser = new DOMXMLParser();
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader("E:/CreateRequest.xml");
			BufferedReader br = new BufferedReader(fr);
			String s;

			while ((s = br.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}

			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			dOMXMLParser.parse(sb.toString());

			dOMXMLParser.removeNode("header");
			System.out.println(dOMXMLParser.getXMLString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertNode(String whereToInsert, String nodeName, String value) throws Exception {
		if ((doc == null) || (whereToInsert == null) || (nodeName == null) || (value == null)) {
			System.out.println("Atleast one of the parameter is null");
		}

		Node parent = this.getNode(whereToInsert);
		Node child = doc.createElement(nodeName);
		Text text = doc.createTextNode(value);

		if (child != null) {
			child.appendChild(text);
		}
		if (parent != null) {
			parent.appendChild(child);
		}
	}

	public void insertNodeForList(String whereToInsert, String nodeName, String value) throws Exception {
		if ((doc == null) || (whereToInsert == null) || (nodeName == null) || (value == null)) {
			System.out.println("Atleast one of the parameter is null");
		}

		Node parent = this.getNode(whereToInsert);
		Node child = doc.createElement(nodeName);
		Text text = doc.createTextNode(value);

		if (child != null) {
			child.appendChild(text);
		}
		if (parent != null) {
			parent.appendChild(child);
		}
	}

	public void insertNode(String parent, Node nodeToInsert) throws Exception {
		Node _parent = getNode(parent);
		_parent.appendChild(nodeToInsert);
	}

	public void insertNodeWithCDATASection(String whereToInsert, String nodeName, String cDatavalue) throws Exception {
		if ((doc == null) || (whereToInsert == null) || (nodeName == null) || (cDatavalue == null)) {
			System.out.println("Atleast one of the parameter is null");
		}

		Node parent = this.getNode(whereToInsert);
		Node child = doc.createElement(nodeName);
		CDATASection cData = doc.createCDATASection(cDatavalue);

		if (child != null) {
			child.appendChild(cData);
		}
		if (parent != null) {
			parent.appendChild(child);
		}
	}

	public String getXMLString() {
		String returnString = null;

		try {
			Source source = new DOMSource(doc);
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			returnString = stringWriter.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
		} catch (TransformerException e) {
		}

		return returnString;
	}

	private Node getNode(String xPath) throws Exception {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		Node retNode = null;

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if ((nodelist != null) && (nodelist.getLength() > 0)) {
				retNode = nodelist.item(0);
			}
		} catch (TransformerException e) {
		}

		return retNode;
	}

	public Collection<Node> getNodeList(String xPath) {
		Collection<Node> list = null;

		if (doc == null || xPath == null) {
			System.out.println("document object or xpath is null");
		}

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);
			if (nodelist != null && nodelist.getLength() > 0) {
				list = new ArrayList<Node>();
				for (int i = 0; i < nodelist.getLength(); i++) {
					list.add(nodelist.item(i));
				}
			}
		} catch (TransformerException e) {
			System.out.println("TransformerException occurred while getting node list " + e);
		}

		return list;

	}

	public String getAttributeValue(String xPath) throws Exception {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		String retValue = null;

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if ((nodelist != null) && (nodelist.getLength() > 0)) {
				retValue = nodelist.item(0).getNodeValue();
			}
		} catch (TransformerException e) {
		}

		return retValue;
	}

	public void setAttributeValue(String xPath, String value) throws Exception {
		if (doc == null || xPath == null) {
			System.out.println("document object or xpath is null");
		}

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);
			if (nodelist != null && nodelist.getLength() > 0) {
				nodelist.item(0).setNodeValue(value);
			}
		} catch (TransformerException e) {
			System.out.println("Error while parsing the xml");
		}
	}

	public String getNodeValue(String xPath) throws Exception {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		if (!xPath.endsWith("/text()") && !xPath.equals("/*")) {
			xPath = xPath + "/text()";
		}

		String retValue = null;

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if (xPath != null && xPath.equals("/*")) {
				retValue = nodelist.item(0).getNodeName();
			} else if ((nodelist != null) && (nodelist.getLength() > 0)) {
				retValue = nodelist.item(0).getNodeValue();
			}
		} catch (TransformerException e) {
		}

		return retValue;
	}

	public String getChildNodeName(String xPath) throws Exception {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if ((nodelist != null) && (nodelist.getLength() > 0)) {
				NodeList node;
				node = nodelist.item(0).getChildNodes();

				for (int i = 0; i < node.getLength(); i++) {
					Node _node = node.item(i);

					if (_node.getNodeType() == Node.ELEMENT_NODE) {
						return _node.getNodeName();
					}
				}

			}
		} catch (TransformerException e) {
		}

		return null;
	}

	public String ifNodeExists(String xPath) throws Exception {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		String retValue = null;

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if ((nodelist != null) && (nodelist.getLength() > 0)) {
				retValue = "Node is present";
			}
		} catch (TransformerException e) {
		}

		return retValue;
	}

	public Node getChildNode(String xPath) throws Exception {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		Node retValue = null;

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if ((nodelist != null) && (nodelist.getLength() > 0)) {
				for (int i = 0; i < nodelist.getLength(); i++) {
					Node _node = nodelist.item(i);

					if (_node.getNodeType() == Node.ELEMENT_NODE) {
						NodeList childNodeList = _node.getChildNodes();

						for (int j = 0; j < childNodeList.getLength(); j++) {
							Node _childNode = childNodeList.item(j);

							if (_childNode.getNodeType() == Node.ELEMENT_NODE) {
								return _childNode;
							}
						}
					}
				}
			}
		} catch (TransformerException e) {
		}

		return retValue;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getAllChildNodes(String xPath) throws Exception {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if ((nodelist != null) && (nodelist.getLength() > 0)) {
				for (int i = 0; i < nodelist.getLength(); i++) {
					Node _node = nodelist.item(i);

					if (_node.getNodeType() == Node.ELEMENT_NODE) {
						NodeList childNodeList = _node.getChildNodes();

						ArrayList allChildNodes = new ArrayList();

						for (int j = 0; j < childNodeList.getLength(); j++) {
							Node _childNode = childNodeList.item(j);

							if (_childNode.getNodeType() == Node.ELEMENT_NODE) {
								String data = _childNode.getFirstChild().getNodeValue();
								System.out.println("The data for each child node is " + data);
								allChildNodes.add(data);
								System.out.println("The Array List contains  " + allChildNodes);
							}
						}

						return allChildNodes;
					}
				}
			}
		} catch (TransformerException e) {
		}

		return null;
	}

	public Node getNODE(String xPath) throws Exception {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		Node retValue = null;

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if ((nodelist != null) && (nodelist.getLength() > 0)) {
				for (int i = 0; i < nodelist.getLength(); i++) {
					Node _node = nodelist.item(i);

					if (_node.getNodeType() == Node.ELEMENT_NODE) {
						return _node;
					}
				}
			}
		} catch (TransformerException e) {
		}

		return retValue;
	}

	public void parse(String xmlContent) throws Exception {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource source = new InputSource(new StringReader(xmlContent));
			doc = builder.parse(source);
		} catch (ParserConfigurationException e) {
			throw new Exception();
		} catch (SAXException e) {
			throw new Exception();
		} catch (IOException e) {
			throw new Exception();
		}
	}

	public String getRootElement(String xmlContent) throws Exception {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource source = new InputSource(new StringReader(xmlContent));
			doc = builder.parse(source);
			doc.getDocumentElement().normalize();

			Element rootElement = doc.getDocumentElement();
			String rootnodevalue = rootElement.getTagName();

			return rootnodevalue;
		} catch (ParserConfigurationException e) {
			throw new Exception();
		} catch (SAXException e) {
			throw new Exception();
		} catch (IOException e) {
			throw new Exception();
		}
	}

	public void setAllNodeValues(String xPath, String value) {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		if (!xPath.endsWith("/text()")) {
			xPath = xPath + "/text()";
		}

		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);

			if (nodelist != null) {
				for (int i = 0; i < nodelist.getLength(); i++) {
					nodelist.item(i).setNodeValue(value);
				}
			}

		} catch (TransformerException e) {
			System.out.println("Exception while setting the node values " + e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection getAllNodeValues(String xPath) throws Exception {
		if ((doc == null) || (xPath == null)) {
			System.out.println("document object or xpath is null");

		}

		if (!xPath.endsWith("/text()")) {
			xPath = xPath + "/text()";
		}

		String retValue = null;
		ArrayList nodeValueList = new ArrayList();
		try {
			NodeList nodelist = XPathAPI.selectNodeList(doc, xPath);
			if (nodelist != null) {
				for (int i = 0; i < nodelist.getLength(); i++) {
					retValue = nodelist.item(i).getNodeValue();
					nodeValueList.add(retValue);
				}
			}
		} catch (TransformerException e) {
		}

		return nodeValueList;
	}

	public void insertNodeBefore(String whereToInsert, String nodeName, String value, String refNode) {
		if (doc == null || whereToInsert == null || nodeName == null || value == null || refNode == null) {
			System.out.println("Atleast one of the parameter is null");
		}
		try {
			Node ref = this.getNode(refNode);
			Node parent = this.getNode(whereToInsert);
			Node child = doc.createElement(nodeName);
			Text text = doc.createTextNode(value);
			child.appendChild(text);
			parent.insertBefore(child, ref);
		} catch (Exception e) {
			System.out.println("Exception while inserting the node");
		}
	}

	public Element returnNewDomElement(String nodeName) {
		return doc.createElement(nodeName);
	}

	public Element getRootNodeFromXml() {
		Element result = null;
		try {
			result = doc.getDocumentElement();
		} catch (Exception e) {
			System.out.println("Exception while parsing the XML");
		}
		return result;
	}

	public String getXmlAsString(String xmlPath) throws Exception {

		logger.info("getXmlAsString :: START()");
		String strXML = new String();
		StringBuffer xmlInstance = new StringBuffer();
		FileReader fr = null;
		BufferedReader reader = null;
		try {
			fr = new FileReader(xmlPath);
			reader = new BufferedReader(fr);
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				xmlInstance.append(temp);
				xmlInstance.append(System.lineSeparator());
			}
		} catch (Exception e) {
			logger.error("exception while reading the xml", e);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (fr != null) {
				fr.close();
			}
		}
		strXML = xmlInstance.toString();
		logger.info("getXmlAsString :: END()");
		return strXML;
	}

}
