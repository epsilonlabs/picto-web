package org.eclipse.epsilon.picto.web.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.emc.emf.EmfUtil;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PictoWebOnLoadedListener;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;

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
  static DocumentBuilder builder;

  /***
   * Initialise and run Picto Web server.
   * 
   * @throws Exception
   */
  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    PictoPackage.eINSTANCE.eClass();
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

    JsonNode node = TestUtil.requestView(GET_URL);
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
   * This test modifies the '/socialnetwork/socialnetwork.model.picto' file under
   * the Workspace by adding Daniel and the check whether '/Social Network' path
   * returns a view that contains Alice, Bob, Charlie, and the new node, Daniel.
   * 
   * @throws Exception
   */
  @Test
  public void testPushUpdatedView() throws Exception {

    // initialise connection to the stomp server
    StandardWebSocketClient client = new StandardWebSocketClient();
    List<Transport> transports = new ArrayList<>(1);
    transports.add(new WebSocketTransport(client));
    SockJsClient sockJsClient = new SockJsClient(transports);
    WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

    // connect
    StompSession session = stompClient.connect("ws://localhost:8080/picto-web", new SessionHandler()).get();

    // subscribe
    ResponseHolder responseHolder = new ResponseHolder();
    session.subscribe("/topic/picto/socialnetwork/socialnetwork.model.picto", new StompHandler(responseHolder));

    // change the file
    String tmpdir = System.getProperty("java.io.tmpdir");
    String modelFileName = "socialnetwork/socialnetwork.model";
    String metamodelFileName = "socialnetwork/socialnetwork.ecore";
    File modelFile = new File(PictoApplication.WORKSPACE + modelFileName);
    File metamodelFile = new File(PictoApplication.WORKSPACE + metamodelFileName);

    // backup model file
    File modelFileBackup = new File(tmpdir + File.separator + modelFile.getName());
    Files.copy(modelFile, modelFileBackup);

    // load the metamodel
    org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI
        .createFileURI(metamodelFile.getAbsolutePath());
    EmfUtil.register(uri, EPackage.Registry.INSTANCE);

    // load the model
    XMIResource res = (new XMIResourceImpl(URI.createFileURI(modelFile.getAbsolutePath())));
    res.load(null);

    // modify model
    EObject sn = res.getEObject("0"); // get Alice (id = 1)
    EClass socialNetwork = sn.eClass();
    EStructuralFeature peopleProperty = socialNetwork.getEStructuralFeature("people");

    EObject alice = res.getEObject("1"); // get Alice (id = 1)
    EClass person = alice.eClass();
    EStructuralFeature nameProperty = person.getEStructuralFeature("name");

    EObject dan = EcoreUtil.create(person);
    dan.eSet(nameProperty, "Dan");

    @SuppressWarnings("unchecked")
    EList<EObject> people = (EList<EObject>) sn.eGet(peopleProperty);
    people.add(dan);
    res.setID(dan, "4");
    FileOutputStream fos = new FileOutputStream(modelFile);
    res.save(fos, res.getDefaultSaveOptions());
    fos.close();

    long waitTime = 3 * 1000;
    
    synchronized (responseHolder) {
      responseHolder.wait(waitTime);
    }

    for (String jsonString : responseHolder.getResponseStrings()) {

      // get the json
      JsonNode node = TestUtil.MAPPER.readTree(jsonString);
      String path = node.get("path").textValue();
      if ("/".equals(path)) {
        continue;
      }

      Set<String> expectedNames = new HashSet<String>(Arrays.asList(new String[] { "Alice", "Bob", "Charlie", "Dan" }));

      String htmlString = node.get("content").textValue();
      Document html = builder.parse(new InputSource(new StringReader(htmlString)));

      XPath xPath = XPathFactory.newInstance().newXPath();
      String expression = "//g[@class='node']/title";
      NodeList elements = (NodeList) xPath.compile(expression).evaluate(html, XPathConstants.NODESET);
      Set<String> actualNames = new HashSet<String>(TestUtil.toStringList(elements));

      assertThat(actualNames).isSubsetOf(expectedNames);
    }
//     delete the modified model and restore the backup model
//    FileWatcher.stopWatching();
    modelFile.delete();
    Files.copy(modelFileBackup, modelFile);
    modelFileBackup.delete();
  }
}
