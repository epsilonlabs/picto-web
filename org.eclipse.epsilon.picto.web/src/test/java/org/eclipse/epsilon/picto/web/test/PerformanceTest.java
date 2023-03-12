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
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfUtil;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.dom.AssignmentStatement;
import org.eclipse.epsilon.eol.dom.IntegerLiteral;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PromisesGenerationListener;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.JettyXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
public class PerformanceTest {

  static final String PICTO_WEB_ADDRESS = "http://localhost:8081/pictojson/picto?";
  public static final String WEB_SOCKET_ADDRESS = "ws://localhost:8081/picto-web";
  public static final String PICTO_FILE = "/performance/graph.picto";
  private static final String GRAPH_METAMODEL = "/performance/graph.ecore";
  private static final String GRAPH_MODEL = "/performance/graph.model";
  private static final String OP_INIT = "/performance/opinit.eol";
  private static final String OP_ADD_EDGE = "/performance/opaddedge.eol";
  private static final String OP_DEL_EDGE = "/performance/opdeledge.eol";
  public static final String PICTO_TOPIC = "/topic/picto";
  private static final File metamodelFile = new File(PictoApplication.WORKSPACE + File.separator + GRAPH_METAMODEL);
  private static final File modelFile = new File(PictoApplication.WORKSPACE + File.separator + GRAPH_MODEL);
  private static final File opInitFile = new File(PictoApplication.WORKSPACE + File.separator + OP_INIT);
  private static final File opAddEdgeFile = new File(PictoApplication.WORKSPACE + File.separator + OP_ADD_EDGE);
  private static final File opDelEdgeFile = new File(PictoApplication.WORKSPACE + File.separator + OP_DEL_EDGE);
  private static final XMIResource resource = new XMIResourceImpl(URI.createFileURI(modelFile.getAbsolutePath()));
  private static final EolModule eolModule = new EolModule();
  private static final EmfModel emfModel = new EmfModel();

  static final Random random = new Random();

  private static Server server;
  private static final List<Client> clients = new ArrayList<>();
  static Set<String> clientWaitingList = Collections.synchronizedSet(new HashSet<>());

  static ObjectMapper mapper = new ObjectMapper();
  static Set<String> expectedViews;

  /***
   * Initialise and run Picto Web server.
   * 
   * @throws Exception
   */
  @BeforeAll
  public static void setUpBeforeClass() throws Exception {
    // load picto metamodel
    PictoPackage.eINSTANCE.eClass();

    // load graph metamodel
    org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI
        .createFileURI(metamodelFile.getAbsolutePath());
    EmfUtil.register(uri, EPackage.Registry.INSTANCE);
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
  }

  @BeforeEach
  void setUp() throws Exception {
    // create a model
    emfModel.setResource(resource);
    emfModel.setReadOnLoad(true);
    emfModel.setExpand(true);
    emfModel.setName("M");
    emfModel.setMetamodelUri("graph");
    emfModel.setModelFile(modelFile.getAbsolutePath());
    eolModule.getContext().getModelRepository().addModel(emfModel);

    eolModule.parse(opInitFile);

    PerformanceRecorder.startRecording();
  }

  @AfterEach
  void tearDown() throws Exception {
    PerformanceRecorder.stopRecording();

    server.stop();
    emfModel.dispose();
    emfModel.close();
  }

  /***
   * Evaluate the performance of cached views vs non-cached views.
   * 
   * @throws Exception
   */
  @Test
  public void testGeneratedVsCachedViewRequests() throws Exception {

    PerformanceRecorder.setOutputFile(new File("data/cached.csv"));
    PerformanceRecorder.startRecording();

    try {

      Object invalidatedViewsWaiter = new Object();

//      // a configuration for a larger scale test
//      int numberOfNodes = 10000; // Number of nodes the graph model.
//      int numberOfClients = 100; // number of clients subscribed to Picto Web's STOMP server.
//      int numberOfIteration = 200; // Number of requests per client

      // a configuration for a smaller scale test
      int numberOfNodes = 12; // Number of nodes the graph model.
      int numberOfClients = 3; // number of clients subscribed to Picto Web's STOMP server.
      int numberOfIteration = 12; // Number of requests per client

      boolean[] isAlwaysGenerated = { true, false };

      // create the initial model
      AssignmentStatement as = (AssignmentStatement) eolModule.getChildren().get(0).getChildren().get(0);
      IntegerLiteral il = (IntegerLiteral) as.getValueExpression();
      il.setText(String.valueOf(numberOfNodes));

      System.out.println("Creating the initial model ...");
      eolModule.execute();
      FileOutputStream fos = new FileOutputStream(modelFile);
      emfModel.store(fos);
      fos.flush();
      fos.close();

      // start Picto Web server
      server = new Server();
      server.start();
      Thread.sleep(3000);

      // create clients
      clients.clear();
      for (int i = 0; i < numberOfClients; i++) {
        Client client = new Client();
        clients.add(client);
        client.setName("Client-" + i);
      }

      for (boolean temp : isAlwaysGenerated) {

        PictoApplication.setPromisesGenerationListener(new PromisesGenerationListener() {
          @Override
          public void onGenerated(Set<String> invalidatedViews) {
            synchronized (invalidatedViewsWaiter) {
              // notify the main thread to continue iteration
              invalidatedViewsWaiter.notify();
            }
          }
        });

        // just save again to trigger 1st-time generation of promises
        fos = new FileOutputStream(modelFile);
        emfModel.store(fos);
        fos.close();

        // wait until the 1st-time generation of promises finished
        synchronized (invalidatedViewsWaiter) {
          invalidatedViewsWaiter.wait();
        }

        // reload the model
        System.out.print("Reload the model ... ");
        emfModel.dispose();
        emfModel.load();
        System.out.println("Done");

        // modify again to generate invalidated views
        eolModule.getChildren().clear();
        eolModule.clearCache();
        eolModule.parse(opAddEdgeFile);

        // update the numOfAffectedViews in the oppaddedge.eol
        as = (AssignmentStatement) eolModule.getChildren().get(eolModule.getChildren().size() - 1).getChildren().get(0);
        il = (IntegerLiteral) as.getValueExpression();
        il.setText(String.valueOf(numberOfNodes));

        eolModule.execute();

        fos = new FileOutputStream(modelFile);
        emfModel.store(fos);
        fos.close();

        // wait until all promises are processed
        synchronized (invalidatedViewsWaiter) {
          invalidatedViewsWaiter.wait();
        }

        PerformanceRecorder.generateAlways = temp;
        PictoApplication.setNoCache(PerformanceRecorder.generateAlways);

        // get the repetitive tasks from all clients
        List<Thread> tasks = clients.stream().map(c -> {
          try {
            return c.sendMultipleRequests(numberOfIteration, numberOfNodes);
          } catch (Exception e) {
          }
          return null;
        }).collect(Collectors.toList());

        for (Thread task : tasks) {
          if (!task.isAlive())
            task.start();
          Thread.sleep(100);
        }
        for (Thread task : tasks) {
          task.join();
        }
      }

      // saving data to a file
      File outputFile = new File("data/cached.csv");
      if (!outputFile.getParentFile().exists())
        outputFile.getParentFile().mkdir();
      if (outputFile.exists())
        outputFile.delete();
      outputFile.createNewFile();

      System.out.println("FINISHED!");
    } catch (Exception e) {
      e.printStackTrace();
    }

    PerformanceRecorder.stopRecording();
  }

  /***
   * Compare the performance between Greedy vs Selective Generations.'Greedy'
   * means all views are generated when a model is changed.'Selective' means Picto
   * Web determines which views that needs regeneration due to the recent model
   * change.
   * 
   * @throws Exception
   */
  @Test
  public void testGreedyVsSelectiveGenerations() throws Exception {

    PerformanceRecorder.setOutputFile(new File("data/selective.csv"));
    PerformanceRecorder.startRecording();

    Object invalidatedViewsWaiter = new Object();

//    // configuration for larger experiment
//    int numberOfNodes = 10000; // Number of nodes the graph model.
//    int numberOfClients = 100; // number of clients subscribed to Picto Web's STOMP server.
//    int numberOfIteration = 13; // Number of iteration measuring for each number of affected views
//    // a.k.a number of edges added for each modification + 3 views (origin node,
//    // viewtree, overall graph)
//    int[] numbersOfAffectedViews = { 10000, 5000, 1000, 500, 100, 50, 10, 5, 1 };
//    boolean[] genAllViews = { true, false };

//     configuration for smaller experiment
    int numberOfNodes = 12; // Number of nodes the graph model
    int numberOfClients = 3; // number of clients subscribed to Picto Web's STOMP server.
    int numberOfIteration = 8; // Number of iteration measuring for each number of affected views
    int[] numbersOfAffectedViews = { 12, 8, 4 };
//    int[] numbersOfAffectedViews = { 100000 };
//    int[] numbersOfAffectedViews = { 10, 9, 8, 7, 5, 4, 3, 2, 1 };
    boolean[] genAllViews = { true, false };
//    boolean[] genAllViews = { true };

    PerformanceRecorder.globalNumberOfViews = numberOfNodes;

    // create the initial model
    AssignmentStatement as = (AssignmentStatement) eolModule.getChildren().get(0).getChildren().get(0);
    IntegerLiteral il = (IntegerLiteral) as.getValueExpression();
    il.setText(String.valueOf(numberOfNodes));

    System.out.println("Creating the initial model ...");
    eolModule.execute();
    FileOutputStream fos = new FileOutputStream(modelFile);
    emfModel.store(fos);
    fos.flush();
    fos.close();

    // start Picto Web server
    System.out.println("Starting server ...");
    server = new Server();
    server.start();
    Thread.sleep(3000);

    // create clients
    clients.clear();
    for (int i = 0; i < numberOfClients; i++) {
      Client client = new Client();
      clients.add(client);
      client.setName("Client-" + i);
      client.start();
      client.join(); // wait until each process finishes one by one
    }

    // modify
    try {

      for (boolean temp : genAllViews) {
        PerformanceRecorder.genenerateAll = temp;
        PictoApplication.setNonIncremental(PerformanceRecorder.genenerateAll);

        // iterate for each number of affected views
        for (int numViews : numbersOfAffectedViews) {
          PerformanceRecorder.globalNumberOfAffectedViews = numViews;
          for (int iterationIndex = 1; iterationIndex <= numberOfIteration; iterationIndex++) {

            PictoApplication.setPromisesGenerationListener(new PromisesGenerationListener() {
              @Override
              public void onGenerated(Set<String> invalidatedViews) {
                synchronized (invalidatedViewsWaiter) {
                  expectedViews.addAll(invalidatedViews);
                  // notify the main thread to continue iteration
                  invalidatedViewsWaiter.notify();
                }
              }
            });

            PerformanceRecorder.globalNumberIteration = iterationIndex;

            System.out.println("\n## GEN ALL: " + PerformanceRecorder.genenerateAll + " AFFECTED VIEWS: " + numViews
                + ", ITERATION: " + iterationIndex + " ##");

            clientWaitingList.clear();
            Set<String> set = clients.stream().map(c -> c.getName()).collect(Collectors.toSet());
            clientWaitingList.addAll(set);
//        // System.out.println("Initial waitlist size = " + waitList.size());

//            // reload model
//            System.out.print("Reload the model ... ");
//            emfModel.dispose();
//            emfModel.load();
//            System.out.println("Done");

            eolModule.getChildren().clear();
            eolModule.clearCache();

            if (iterationIndex % 2 == 1) {
              System.out.println("Operation ADD");
              eolModule.parse(opAddEdgeFile);
            } else {
              System.out.println("Operation DELETE");
              eolModule.parse(opDelEdgeFile);
            }

            // update the numOfAffectedViews in the oppaddedge.eol
            as = (AssignmentStatement) eolModule.getChildren().get(eolModule.getChildren().size() - 1).getChildren()
                .get(0);
            il = (IntegerLiteral) as.getValueExpression();
            il.setText(String.valueOf(numViews));

            eolModule.execute();

            expectedViews = new HashSet<>();
            expectedViews.add("/");
            expectedViews.add("/Graph");
            expectedViews.add("/Graph/I0");

            // if it's selective view generation
            if (!PerformanceRecorder.genenerateAll) {
              // when the test is run for the first time, expect to receive all views
              if (iterationIndex == 1 && numViews == numbersOfAffectedViews[0]) {
                for (int a = 0; a < numberOfNodes; a++) {
                  expectedViews.add("/Graph/N" + a);
                }
              }
            } else { // if generate all views
              for (int a = 0; a < numberOfNodes; a++) {
                expectedViews.add("/Graph/N" + a);
              }
            }

            fos = new FileOutputStream(modelFile);
            emfModel.store(fos);
            fos.close();
//            Thread.sleep(1000);
            PerformanceRecorder.startTime = System.currentTimeMillis();

            // wait until we get the paths of invalidated views
            synchronized (invalidatedViewsWaiter) {
              invalidatedViewsWaiter.wait();
              System.out.println("Expected Views : " + expectedViews);
            }

            synchronized (clientWaitingList) {
//              clientWaitingList.wait(5 * 60 * 1000);
              clientWaitingList.wait(3 * 1000);
            }
            Thread.sleep(1000);
            // ----
          } // for (int i = 1; i <= numberOfIteration; i++) {
        } // for (int numViews : numbersOfAffectedViews) {
      }

      System.out.println("FINISHED!");
      Thread.sleep(1000);

      // saving data to a file
      File outputFile = new File("data/selective.csv");
      if (!outputFile.getParentFile().exists())
        outputFile.getParentFile().mkdir();
      if (outputFile.exists())
        outputFile.delete();
      outputFile.createNewFile();

      Thread.sleep(1000);

    } catch (Exception e) {
      e.printStackTrace();
    }

    PerformanceRecorder.stopRecording();
  }

  /***
   * This class mocks Picto Web client.
   * 
   * @author Alfa Yohannis
   *
   */
  public class Client extends Thread {

    private WebSocketStompClient stompClient;
    private StompSession session;

    /***
     * The methods connects the client to the STOMP server provided by Picto Web and
     * starts retrieving messages.
     */
    public void run() {

      // System.out.println("Start " + getName());
      // initialise connection to the stomp server
      HttpClient jettyHttpClient = new HttpClient();
      jettyHttpClient.setMaxConnectionsPerDestination(Integer.MAX_VALUE);
      jettyHttpClient.setExecutor(new QueuedThreadPool(Integer.MAX_VALUE));
      jettyHttpClient.setRequestBufferSize(10 * 1024 * 1024);
      jettyHttpClient.setResponseBufferSize(10 * 1024 * 1024);

      try {
        jettyHttpClient.start();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      List<Transport> transports = new ArrayList<>(1);
      JettyXhrTransport jxt = new JettyXhrTransport(jettyHttpClient);
      transports.add(jxt);
      SockJsClient sockJsClient = new SockJsClient(transports);

      stompClient = new WebSocketStompClient(sockJsClient);
      try {
        DefaultManagedTaskScheduler ts = new DefaultManagedTaskScheduler();
        stompClient.setTaskScheduler(ts);
        stompClient.setDefaultHeartbeat(new long[] { Long.MAX_VALUE, Long.MAX_VALUE });
        stompClient.setReceiptTimeLimit(Long.MAX_VALUE);
        stompClient.setInboundMessageSizeLimit(Integer.MAX_VALUE);

        // connect
        session = stompClient.connect(PerformanceTest.WEB_SOCKET_ADDRESS, new StompSessionHandlerAdapter() {
          public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            System.out.println(
                "PICTO: " + Client.this.getName() + " connected with sessionId " + stompSession.getSessionId());
          }
        }).get();

        // subscribe
        session.subscribe(PerformanceTest.PICTO_TOPIC + PerformanceTest.PICTO_FILE, new StompFrameHandler() {

          /**
           * Handle the payload from the Picto Web/broker
           * 
           * @param headers
           * @param payload
           */
          public void handleFrame(StompHeaders headers, Object payload) {

            try {
              // get and parse the payload into json
              JsonNode node = PerformanceTest.mapper.readTree(new String((byte[]) payload));
              String type = node.get("type").textValue();
              if (!"newviews".equals(type)) {
                return;
              }

              String content = node.get("content").textValue();
              List<String> invalidatedViews = PerformanceTest.mapper.readValue(content,
                  new TypeReference<List<String>>() {
                  });

              // randomly select the view to be requested
              String invalidatedView = "/Graph/N" + random.nextInt(PerformanceRecorder.globalNumberOfViews);

              // request the content
              String expectedPath = invalidatedView;
              String expectedFile = "/performance/graph.picto";
              Map<String, String> parameters = new HashMap<>();
              parameters.put("file", expectedFile);
              parameters.put("path", expectedPath);

              String pageAddress = PerformanceTest.PICTO_WEB_ADDRESS + TestUtil.getParamsString(parameters);
              URL url = new URL(pageAddress);

              HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
              httpConnection.setRequestProperty("accept", "application/json");
              long start = System.currentTimeMillis();
              InputStream inputStream = httpConnection.getInputStream();
              byte[] viewBytes = inputStream.readAllBytes();
              long end = System.currentTimeMillis();
              long responseTime = end - start;
              long overallTime = end - PerformanceRecorder.startTime;
              httpConnection.disconnect();

              // parse the content
              JsonNode contentNode = PerformanceTest.mapper.readTree(new String((byte[]) viewBytes));
              String path = contentNode.get("path").textValue();

              // record the response time, from requesting a view to receiving the view
              PerformanceRecord record = new PerformanceRecord(PerformanceRecorder.genenerateAll,
                  PerformanceRecorder.generateAlways, PerformanceRecorder.globalNumberOfAffectedViews,
                  PerformanceRecorder.globalNumberIteration, Client.this.getName(), path, responseTime,
                  viewBytes.length, PerformanceTestType.RESPONSE_TIME, PerformanceRecorder.accessRecordResourceSize());
              PerformanceRecorder.record(record);

              // record the overall time from changing a model file to receiving the view
              record = new PerformanceRecord(PerformanceRecorder.genenerateAll, PerformanceRecorder.generateAlways,
                  PerformanceRecorder.globalNumberOfAffectedViews, PerformanceRecorder.globalNumberIteration,
                  Client.this.getName(), path, overallTime, viewBytes.length, PerformanceTestType.OVERALL_TIME,
                  PerformanceRecorder.accessRecordResourceSize());
              PerformanceRecorder.record(record);

              assertThat(path).isEqualTo(expectedPath);
              assertThat(invalidatedViews).containsAll(expectedViews);

              System.out.println("PICTO: Type " + PerformanceTestType.OVERALL_TIME + ", GenAll "
                  + PerformanceRecorder.genenerateAll + ", N-views " + PerformanceRecorder.globalNumberOfAffectedViews
                  + ", Iter " + PerformanceRecorder.globalNumberIteration + ", " + Client.this.getName()
                  + " received, path " + "No Path" + ", time " + overallTime + " ms");

            } catch (IOException e) {
              e.printStackTrace();
            }

            synchronized (PerformanceTest.clientWaitingList) {
              PerformanceTest.clientWaitingList.remove(Client.this.getName());
//              System.out.println("Waiting list = " + PerformanceTest.clientWaitingList);
              if (PerformanceTest.clientWaitingList.isEmpty()) {
                PerformanceTest.clientWaitingList.notify();
              }
            }
//            receivedViews.clear();

            // ---
          }

          /***
           * Get the payload type
           * 
           * @param headers
           * @return
           */
          public Type getPayloadType(StompHeaders headers) {
            return byte[].class;
          }
        });
        // ---
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

    /***
     * Randomly request different views from Picto Web
     * 
     * @param iteration
     * @param numberOfNodes
     * @return
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws MalformedURLException
     * @throws IOException
     */
    public Thread sendMultipleRequests(int iteration, int numberOfNodes)
        throws JsonMappingException, JsonProcessingException, MalformedURLException, IOException {

      Thread t = new Thread() {
        public void run() {
          try {
            for (int i = 0; i < iteration; i++) {

              this.setName(Client.this.getName());

              int num = PerformanceTest.random.nextInt(numberOfNodes);
              String expectedPath = "/Graph/N" + num;
              String expectedFile = "/performance/graph.picto";
              Map<String, String> parameters = new HashMap<>();
              parameters.put("file", expectedFile);
              parameters.put("path", expectedPath);
              parameters.put("name", "N" + num);

              String pageAddress = PerformanceTest.PICTO_WEB_ADDRESS + TestUtil.getParamsString(parameters);

              URL url = new URL(pageAddress);

              long startTime = System.currentTimeMillis();

              HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
              httpConnection.setRequestProperty("accept", "application/json");

              InputStream inputStream = httpConnection.getInputStream();
              byte[] bytes = inputStream.readAllBytes();
              long waitTime = System.currentTimeMillis() - startTime;

              httpConnection.disconnect();

              String message = new String(bytes);
              int size = message.getBytes().length;

              JsonNode node = PerformanceTest.mapper.readTree(message);

              if (size > 0) {
                String receivedPath = node.get("path").textValue();
                assertThat(receivedPath).isEqualTo(receivedPath);

                System.out.println("PICTO: Type " + PerformanceTestType.RESPONSE_TIME + ", GenAlways "
                    + PerformanceRecorder.generateAlways + ", N-views " + numberOfNodes + ", Iter " + (i + 1) + ", "
                    + Client.this.getName() + " received, path " + receivedPath + ", time " + waitTime + " ms");

//                System.out
//                    .println("PICTO: always generate " + PerformanceTest.genAlways + ", " + Client.this.getName()
//                        + ",  request "
//                        + (i + 1) + ", " + receivedPath + " "
//                        + " received, " + waitTime
//                        + " ms");

                PerformanceRecord record = new PerformanceRecord(PerformanceRecorder.genenerateAll,
                    PerformanceRecorder.generateAlways, numberOfNodes, i + 1, Client.this.getName(), receivedPath,
                    waitTime, size, PerformanceTestType.RESPONSE_TIME, PerformanceRecorder.accessRecordResourceSize());
                PerformanceRecorder.record(record);
              }

            }
          } catch (Exception e) {
            e.printStackTrace();
          }

        }

      };
      return t;
    }

    public void dispose() {
      session.disconnect();
      stompClient.stop();
    }
  }
}
