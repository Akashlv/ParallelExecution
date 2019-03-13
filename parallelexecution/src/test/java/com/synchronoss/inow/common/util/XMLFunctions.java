package com.synchronoss.inow.common.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.testng.Reporter;
import org.w3c.dom.Document;

public class XMLFunctions {
	static Document doc = null;
	static int responseCode;
	static String responseBody;
	

	// Update the XML and and Post
	public HashMap<Integer, String> updatePonFromXmlNPost(String xmlPath, String url, String ponNumber) throws Exception {

		DOMXMLParser reqXmlParser = new DOMXMLParser();
		reqXmlParser.parse(reqXmlParser.getXmlAsString(xmlPath));
		if (reqXmlParser.ifNodeExists("//TRANSPORT_SVC/ASR/ADMIN") != null) {

			if (reqXmlParser.ifNodeExists("//TRANSPORT_SVC/ASR/ADMIN/PON") != null) {

				reqXmlParser.setNodeValue("//TRANSPORT_SVC/ASR/ADMIN/PON", ponNumber);
			}
		}

		// ASR Confirm Response
		if (reqXmlParser.ifNodeExists("//CONFIRMATION/header") != null) {

			if (reqXmlParser.ifNodeExists("//HDR/PON") != null) {

				reqXmlParser.setNodeValue("//HDR/PON", ponNumber);
			}

			if (reqXmlParser.ifNodeExists("//CN/DD") != null) {

				Date date = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				String DDD = dateFormat.format(date);

				reqXmlParser.setNodeValue("//CN/DD", DDD);
			}
		}

		// ASR Error Response
		if (reqXmlParser.ifNodeExists("//ORDER_INFO_NOTIFY/header") != null) {

			if (reqXmlParser.ifNodeExists("//HDR/PON") != null) {

				reqXmlParser.setNodeValue("//HDR/PON", ponNumber);
			}
		}

		// LSR Response
		if (reqXmlParser.ifNodeExists("//LSR_RESP/header") != null) {

			if (reqXmlParser.ifNodeExists("//HDR/PON") != null) {

				reqXmlParser.setNodeValue("//HDR/PON", ponNumber);
			}
		}

		// ASR Service Response
		if (reqXmlParser.ifNodeExists("//ASR_SERVICE_RESPONSE/header") != null) {

			if (reqXmlParser.ifNodeExists("//ASR_SERVICE_RESPONSE/PON") != null) {

				reqXmlParser.setNodeValue("//ASR_SERVICE_RESPONSE/PON", ponNumber);
			}
		}
		
		// LSR Response
		if (reqXmlParser.ifNodeExists("//localresponse/pon") != null) {

			if (reqXmlParser.ifNodeExists("//localresponse/pon") != null) {

				reqXmlParser.setNodeValue("//localresponse/pon", ponNumber);
				Reporter.log("Updated PON in Reponse XML : " + ponNumber);
			}
		}

		String xmlString = reqXmlParser.getXMLString();

		HashMap<Integer, String> respCode_body = postXmlToUrlInputIsString(url, xmlString);

		return respCode_body;
	}

	// To Post the XML
	@SuppressWarnings("deprecation")
	public static HashMap<Integer, String> postXmlToUrlInputIsString(String url, String xmlString) {

		HashMap<Integer, String> respCd_body = new HashMap<Integer, String>();

		String strSoapAction = null;
		PostMethod post = new PostMethod(url);
		post.setRequestBody(xmlString);
		post.setRequestHeader("SOAPAction", strSoapAction);
		HttpClient httpclient = new HttpClient();

		try {

			responseCode = httpclient.executeMethod(post);
			Reporter.log("Response status code: " + responseCode, true);
			String responseBody = post.getResponseBodyAsString();
			Reporter.log(responseBody, true);

			respCd_body.put(responseCode, responseBody);

		} catch (HTTPException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			post.releaseConnection();
		}

		return respCd_body;
	}
}