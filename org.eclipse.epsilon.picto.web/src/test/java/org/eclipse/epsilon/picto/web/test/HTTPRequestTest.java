package org.eclipse.epsilon.picto.web.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PictoWebOnLoadedListener;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class HTTPRequestTest {

  private static final String LOCALHOST = "http://localhost:8080/pictojson/picto?";

  private static Thread pictoAppThread;
  private static ObjectMapper mapper;
  private static DocumentBuilder builder;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    String[] args = new String[] {};
    pictoAppThread = new Thread() {
      @Override
      public void run() {
        try {
          PictoApplication.main(args);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    PictoApplication.setPictoWebOnLoadedListener(new PictoWebOnLoadedListener() {
      @Override
      public void onLoaded() {
        if (args != null) {
          synchronized (args) {
            args.notify();
          }
        }
      }
      // ---
    });
    synchronized (args) {
      pictoAppThread.start();
      args.wait();
    }
    mapper = new ObjectMapper();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    builder = factory.newDocumentBuilder();
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
    PictoApplication.exit();
  }

  @BeforeEach
  void setUp() throws Exception {
  }

  @AfterEach
  void tearDown() throws Exception {
  }

  @Test
  void testGetSocialNetwork() throws IOException, SAXException, XPathExpressionException {
    String expectedPath = "/Social Network";
    String expectedFile = "/socialnetwork/socialnetwork.model.picto";
    Map<String, String> parameters = new HashMap<>();
    parameters.put("file", expectedFile);
    parameters.put("path", expectedPath);
    parameters.put("name", "Social Network");

    String paramString = TestUtil.getParamsString(parameters);

    String GET_URL = LOCALHOST + paramString;

    JsonNode node = getJsonNode(GET_URL);
    String actualPath = node.get("path").textValue();
//    String actualFile = node.get("filename").textValue();
    assertThat(expectedPath.equals(actualPath));

    List<String> names = Arrays.asList(new String[] { "Alice", "Bob", "Charlie" });

    String htmlString = node.get("content").textValue();
    Document html = builder.parse(new InputSource(new StringReader(htmlString)));

    XPath xPath = XPathFactory.newInstance().newXPath();
    String expression = "//title";
    NodeList elements = (NodeList) xPath.compile(expression).evaluate(html, XPathConstants.NODESET);

//    NodeList elements = html.getElementsByTagName("title");
    for (int i = 0; i < elements.getLength(); i++) {
      String name = elements.item(i).getTextContent();
      assertThat(names.contains(name));
    }

  }

  public static Document getHtml(String GET_URL)
      throws JsonMappingException, JsonProcessingException, MalformedURLException, IOException, SAXException {
    JsonNode node = getJsonNode(GET_URL);
    String htmlString = node.get("content").textValue();
    Document html = builder.parse(new InputSource(new StringReader(htmlString)));
    return html;
  }

  public static JsonNode getJsonNode(String GET_URL)
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
    String json = response.toString();
    JsonNode node = mapper.readTree(json);
    return node;
  }

}
