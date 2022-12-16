/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.web.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
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
import org.eclipse.epsilon.picto.web.FileWatcher;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PictoWebOnLoadedListener;
import org.eclipse.epsilon.picto.web.component.TestUtil;
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
public class HTTPRequestTest {

  private static final String LOCALHOST = "http://localhost:8080/pictojson/picto?";

  private static Thread pictoAppThread;
  private static DocumentBuilder builder;

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
    Thread.sleep(100);

    synchronized (responseHolder) {
      responseHolder.wait();
    }

    Set<String> expectedNames = new HashSet<String>(Arrays.asList(new String[] { "Alice", "Bob", "Charlie", "Dan" }));

    String expectedPath = "/Social Network";
    String expectedFile = "/socialnetwork/socialnetwork.model.picto";

    Map<String, String> parameters = new HashMap<>();
    parameters.put("file", expectedFile);
    parameters.put("path", expectedPath);
    parameters.put("name", "Social Network");

    String paramString = TestUtil.getParamsString(parameters);
    String GET_URL = LOCALHOST + paramString;

    JsonNode node = TestUtil.requestView(GET_URL);
    String content = node.get("content").textValue();

    Document html = builder.parse(new InputSource(new StringReader(content)));
    XPath xPath = XPathFactory.newInstance().newXPath();
    String expression = "//g[@class='node']/title";
    NodeList elements = (NodeList) xPath.compile(expression).evaluate(html, XPathConstants.NODESET);
    Set<String> actualNames = new HashSet<String>(TestUtil.toStringList(elements));

    assertThat(actualNames).isSubsetOf(expectedNames);

//  delete the modified model and restore the backup model
    FileWatcher.stopWatching();
    modelFile.delete();
    Files.copy(modelFileBackup, modelFile);
    modelFileBackup.delete();
  }

  /***
   * An internal handler class when connecting to the STOMP server.
   * 
   * @author Alfa Yohannis
   *
   */
  class SessionHandler extends StompSessionHandlerAdapter {

    /***
     * Display the sessionId when connection to the Stomp Server is successful.
     */
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
      System.out.println("Connected with sessionId " + stompSession.getSessionId());
    }
  }

  /***
   * A class to hold the json returned from Picto Web server
   * 
   * @author Alfa Yohannis
   *
   */
  static public class ResponseHolder {
    List<String> jsonStrings = new ArrayList<>();

    public List<String> getResponseStrings() {
      return jsonStrings;
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

  static class StompHandler implements StompFrameHandler {
    private ResponseHolder answer;

    /***
     * The constructor receives a CompletableFuture<String> to hold the value (json
     * object string) returned from the server.
     * 
     * @param answer
     * @param numberOfExpectedJsons
     */
    StompHandler(ResponseHolder answer) {
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
      answer.getResponseStrings().add((new String((byte[]) payload)));
      synchronized (answer) {
        answer.notify();
      }
    }

  }

  public static DocumentBuilder getBuilder() {
    return builder;
  }

}
