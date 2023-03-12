package org.eclipse.epsilon.picto.web.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/***
 * A utility class used in Picto Web tests.
 * 
 * @author Alfa Yohannis
 *
 */

public class TestUtil {

  public static ObjectMapper MAPPER = new ObjectMapper();
  
  
  /***
   * This method constructs the parameters string for HTTP URL.
   * 
   * @param params A String to String Map that maps parameters and their values.
   * @return The parameters string.
   * @throws UnsupportedEncodingException
   */
  public static String getParamsString(Map<String, String> params)
      throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();

    for (Map.Entry<String, String> entry : params.entrySet()) {
      result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
      result.append("=");
      result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
      result.append("&");
    }

    return result.toString();
  }

  /***
   * Construct a list of the values of interest contained in the returned view
   * (HTML document). The list contains the actual values and should compared
   * against the expected values defined in each test.
   * 
   * @param <T>
   * @param nodeList The nodeList retrieved using XPath.
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> toStringList(NodeList nodeList) {
    List<T> result = new ArrayList<>();
    for (int i = 0; i < nodeList.getLength(); i++) {
      T name = (T) nodeList.item(i).getTextContent();
      result.add(name);
    }
    return result;
  }

  /***
   * Get the view, a HTML document, retrieved from the Picto Web server.
   * 
   * @param GET_URL
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   * @throws MalformedURLException
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException 
   */
  public static Document getHtml(String GET_URL)
      throws JsonMappingException, JsonProcessingException, MalformedURLException, IOException, SAXException, ParserConfigurationException {
    
    JsonNode node = TestUtil.requestView(GET_URL);
    String htmlString = node.get("content").textValue();
    DocumentBuilder builder = (DocumentBuilderFactory.newInstance()).newDocumentBuilder();
    Document html = builder.parse(new InputSource(new StringReader(htmlString)));
    builder.reset();
    return html;
  }

  /***
   * Get the JsonNode retrieved from Picto Web server.
   * 
   * @param GET_URL
   * @return
   * @throws MalformedURLException
   * @throws IOException
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  public static JsonNode requestView(String GET_URL)
      throws MalformedURLException, IOException, JsonProcessingException, JsonMappingException {
    URL obj = new URL(GET_URL);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestProperty("accept", "application/json");
    InputStream inputStream = con.getInputStream();
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    BufferedReader in = new BufferedReader(inputStreamReader);
    String inputLine;
    StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    con.disconnect();
    String json = response.toString();
    JsonNode node = TestUtil.MAPPER.readTree(json);
    return node;
  }

  

}