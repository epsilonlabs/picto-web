package org.eclipse.epsilon.picto.web.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/***
 * A test class that mocks http requests from web clients. For assertions, The
 * tests check the values and elements inside the returned view to examine their
 * correctness.
 * 
 * @author Alfa Yohannis
 *
 */
class HTTPRequestTest {

  private static final String LOCALHOST = "http://localhost:8080/pictojson/picto?";

  private static Thread pictoAppThread;
  private static ObjectMapper mapper;
  private static DocumentBuilder builder;

  /***
   * Initialise and run Picto Web server.
   * 
   * @throws Exception
   */
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

  /***
   * Shutdown Picto Web server.
   * 
   * @throws Exception
   */
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

  /***
   * Using the '/socialnetwork/socialnetwork.model.picto' file under the
   * Workspace, this method tests whether '/Social Network' path returns a view
   * that contains Alice, Bob, and Charlie.
   * 
   * @throws IOException
   * @throws SAXException
   * @throws XPathExpressionException
   */
  @Test
  public void testGetSocialNetwork() throws IOException, SAXException, XPathExpressionException {
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

    Set<String> expectedNames = new HashSet<String>(Arrays.asList(new String[] { "Alice", "Bob", "Charlie" }));

    String htmlString = node.get("content").textValue();
    Document html = builder.parse(new InputSource(new StringReader(htmlString)));

    XPath xPath = XPathFactory.newInstance().newXPath();
    String expression = "//g[@class='node']/title";
    NodeList elements = (NodeList) xPath.compile(expression).evaluate(html, XPathConstants.NODESET);

    Set<String> actualNames = new HashSet<String>(TestUtil.toStringList(elements));

    assertThat(actualNames.equals(expectedNames));
  }

  /***
   * This test modifies the '/socialnetwork/socialnetwork.model.picto' file under the
   * Workspace by adding Daniel and the check whether '/Social Network' path returns a view
   * that contains Alice, Bob, Charlie, and the new node, Daniel.
   * 
   * 
   * @throws InterruptedException
   * @throws ExecutionException
   * @throws TimeoutException
   * @throws SAXException
   * @throws IOException
   * @throws XPathExpressionException
   */
  @Test
  public void testGeneration() throws InterruptedException, ExecutionException, TimeoutException, SAXException,
      IOException, XPathExpressionException {

    StandardWebSocketClient client = new StandardWebSocketClient();
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(client));

    SockJsClient sockJsClient = new SockJsClient(transports);
    WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
    StompSession session = stompClient.connect("ws://localhost:8080/picto-web", new SessionHandler()).get();

    CompletableFuture<String> answer = new CompletableFuture<String>();
    session.subscribe("/topic/picto/socialnetwork/socialnetwork.model.picto", new StompHandler(answer));

    // get the json
    String json = answer.get(5, TimeUnit.SECONDS);
    JsonNode node = mapper.readTree(json);
    Set<String> expectedNames = new HashSet<String>(Arrays.asList(new String[] { "Alice", "Bob", "Charlie" }));

    String htmlString = node.get("content").textValue();
    Document html = builder.parse(new InputSource(new StringReader(htmlString)));

    XPath xPath = XPathFactory.newInstance().newXPath();
    String expression = "//g[@class='node']/title";
    NodeList elements = (NodeList) xPath.compile(expression).evaluate(html, XPathConstants.NODESET);
    Set<String> actualNames = new HashSet<String>(TestUtil.toStringList(elements));
    assertThat(actualNames.equals(expectedNames));
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
   */
  public static Document getHtml(String GET_URL)
      throws JsonMappingException, JsonProcessingException, MalformedURLException, IOException, SAXException {
    JsonNode node = getJsonNode(GET_URL);
    String htmlString = node.get("content").textValue();
    Document html = builder.parse(new InputSource(new StringReader(htmlString)));
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

  /***
   * An internal handler class when connecting to the STOMP server.
   * 
   * @author Alfa Yohannis
   *
   */
  private class SessionHandler extends StompSessionHandlerAdapter {

    /***
     * Display the sessionId when connection to the Stomp Server is successful.
     */
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
      System.out.println("Connected with sessionId " + stompSession.getSessionId());
    }
  }

  /***
   * An internal handler class that handles the STOMP response from the server.
   * Using this class, we can get the values, the returned pages, returned from
   * Picto Web server.
   * 
   * @author Alfa Yohannis
   *
   */
  private class StompHandler implements StompFrameHandler {
    private final CompletableFuture<String> answer;

    /***
     * The constructor receives a CompletableFuture<String> to hold the value (json
     * object string) returned from the server.
     * 
     * @param answer A sync CompletableFuture<String> to get the value returned from
     *               the server.
     */
    private StompHandler(CompletableFuture<String> answer) {
      this.answer = answer;
    }

    public Type getPayloadType(StompHeaders headers) {
      return byte[].class;
    }

    /***
     * This method receives the page returned by Picto Web server.
     * 
     * @param payload The payload (.e.g., json, html) returned by the server.
     * @param headers The Stomp Headers.
     */
    public void handleFrame(StompHeaders headers, Object payload) {
      answer.complete(new String((byte[]) payload));

    }

  }
}
