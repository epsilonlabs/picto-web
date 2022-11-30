package org.eclipse.epsilon.picto.web.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
import org.eclipse.epsilon.eol.types.EolSet;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PictoWebOnLoadedListener;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
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
class PerformanceTest {

  private static final String PICTO_WEB_ADDRESS = "http://localhost:8080/pictojson/picto?";
  private static final String WEB_SOCKET_ADDRESS = "ws://localhost:8080/picto-web";
  private static final String PICTO_FILE = "/performance/graph.picto";
  private static final String GRAPH_METAMODEL = "/performance/graph.ecore";
  private static final String GRAPH_MODEL = "/performance/graph.model";
  private static final String OP_INIT = "/performance/opinit.eol";
  private static final String OP_ADD_EDGE = "/performance/opaddedge.eol";
  private static final String OP_DEL_EDGE = "/performance/opdeledge.eol";
  private static final String PICTO_TOPIC = "/topic/picto";
  private static final File metamodelFile = new File(PictoApplication.WORKSPACE + File.separator + GRAPH_METAMODEL);
  private static final File modelFile = new File(PictoApplication.WORKSPACE + File.separator + GRAPH_MODEL);
  private static final File opInitFile = new File(PictoApplication.WORKSPACE + File.separator + OP_INIT);
  private static final File opAddEdgeFile = new File(PictoApplication.WORKSPACE + File.separator + OP_ADD_EDGE);
  private static final File opDelEdgeFile = new File(PictoApplication.WORKSPACE + File.separator + OP_DEL_EDGE);
  private static final XMIResource resource = new XMIResourceImpl(URI.createFileURI(modelFile.getAbsolutePath()));
  private static final EolModule eolModule = new EolModule();
  private static final EmfModel emfModel = new EmfModel();

  private static final Random random = new Random();

  private static Server server;
  private static final List<Client> clients = new ArrayList<>();
  private static final Set<String> waitList = new HashSet<>();

  private static ObjectMapper mapper = new ObjectMapper();
  private static long startTime;
  private static Set<String> expectedViews;

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
  }

  @AfterEach
  void tearDown() throws Exception {
    server.stop();
    emfModel.dispose();
    emfModel.close();
  }

  /***
   * 
   * @throws Exception
   */
  @Test
  public void testAlwaysGeneratedVsCachedViewRequests() throws Exception {

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

  }

  /***
   * This test performs simulation that modifies a model and send the updates to
   * clients.
   * 
   * @throws Exception
   */
  @Test
  public void testSimulation() throws Exception {

    int numberOfNodes = 1000; // Number of nodes the graph model.
    int numberOfClients = 100; // number of clients subscribed to Picto Web's STOMP server.
    int numberOfEdges = 1; // number of edges added for each modification
    int modifyEvery = 10 * 1000; // miliseconds
    int numberOfModification = 10;

    // /The probability of add operation. The rest is delete operation.
    double addProbability = 1; // 0.0 to 1.0

    // create the initial model
    AssignmentStatement as = (AssignmentStatement) eolModule.getChildren().get(0).getChildren().get(0);
    IntegerLiteral il = (IntegerLiteral) as.getValueExpression();
    il.setText(String.valueOf(numberOfNodes));
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
      client.start();
      client.join(); // wait until each process finishes one by one
    }

//    // wait until all clients are already subscribed to the broker
//    for (Client client : clients) {
//      client.join();
//    }

    // modify
    try {
      for (int i = 1; i <= numberOfModification; i++) {

         System.out.println(" ## ITERATION " + i + " ##");

        waitList.clear();
        Set<String> set = clients.stream().map(c -> c.getName()).collect(Collectors.toSet());
        waitList.addAll(set);
//        // System.out.println("Initial waitlist size = " + waitList.size());

        eolModule.getChildren().clear();
        eolModule.clearCache();

        // if true then add edges (60% probability), else delete edges (40% probability)
        boolean isAdd = (random.nextDouble() <= addProbability) ? true : false;
        // number of edges to be added or deleted
        int n = numberOfEdges;

        // if true then add edges, else delete edges
        if (i == 1)
          isAdd = true;
        if (isAdd) {
          // System.out.println("Operation ADD");
          eolModule.parse(opAddEdgeFile);
        } else {
          // System.out.println("Operation DELETE");
          eolModule.parse(opDelEdgeFile);
        }

        as = (AssignmentStatement) eolModule.getChildren().get(eolModule.getChildren().size() - 1)
            .getChildren().get(0);
        il = (IntegerLiteral) as.getValueExpression();
        il.setText(String.valueOf(n));

//        eolModule.execute();
        EolSet<String> affectedNodes = (EolSet<String>) eolModule.execute();
        expectedViews = new HashSet<>();
        expectedViews.add("/");
        expectedViews.add("/Graph");
        if (i == 1) {
          for (int a = 0; a < numberOfNodes; a++  ) {
            expectedViews.add("/Graph/N" + a);
          }
        } else {
          for (String item : affectedNodes) {
            expectedViews.add("/Graph/" + item);
          }
        }
        // System.out.println("Expected Views : " + expectedViews);

        fos = new FileOutputStream(modelFile);
        emfModel.store(fos);
        fos.close();
        Thread.sleep(100);
        startTime = System.currentTimeMillis();

//         wait until all clients are already subscribed to the broker
        synchronized (waitList) {
          waitList.wait();
        }

//        if (i == 1) {
//          Thread.sleep(4 * 1000);
//        } else {
//          Thread.sleep(3 * 1000);
//        }
      }

       System.out.println("FINISHED!");
      Thread.sleep(60 * 1000);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /***
   * This class mocks Picto Web client.
   * 
   * @author Alfa Yohannis
   *
   */
  static class Client extends Thread {

    private WebSocketStompClient stompClient;
    private StompSession session;
    private String requestedResponse;
    private Set<String> receivedViews = new HashSet<>();

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
        session = stompClient.connect(WEB_SOCKET_ADDRESS, new StompSessionHandlerAdapter() {
          public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            // System.out.println( "PICTO: " + Client.this.getName() + " connected with sessionId " + stompSession.getSessionId());
          }
        }).get();

        // subscribe
        session.subscribe(PICTO_TOPIC + PICTO_FILE, new StompFrameHandler() {
          public void handleFrame(StompHeaders headers, Object payload) {
            long waitTime = (System.currentTimeMillis() - startTime);
            String message = new String((byte[]) payload);
            try {
              JsonNode node = mapper.readTree(message);
              String path = node.get("path").textValue();
//              System.out.println("PICTO: " + Client.this.getName() + ",  " + path + " " + " received, " + waitTime + " ms");
              receivedViews.add(path);
            } catch (JsonProcessingException e) {
              e.printStackTrace();
            }

            // System.out.println("Received views: " + receivedViews);
            if (receivedViews.equals(expectedViews)) {
//              // System.out.println("Received views: " + receivedViews);
              waitList.remove(Client.this.getName());
              // System.out.println("Waitlist size = " + waitList.size());
              if (waitList.size() == 0) {
                synchronized (waitList) {
                  waitList.notify();
                }
              }
              receivedViews.clear();
            }
            // ---
          }

          public Type getPayloadType(StompHeaders headers) {
            return byte[].class;
          }
        });
        // ---
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
      // System.out.println("End " + getName());
    }

    /***
     * The method sends a request to Picto Web server
     * 
     * @param pageAddress
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws MalformedURLException
     * @throws IOException
     */
    public void request(String pageAddress)
        throws JsonMappingException, JsonProcessingException, MalformedURLException, IOException {
      URL url = new URL(pageAddress);
      HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
      httpConnection.setRequestProperty("accept", "application/json");
      InputStream inputStream = httpConnection.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader in = new BufferedReader(inputStreamReader);
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      httpConnection.disconnect();
      requestedResponse = response.toString();
    }

    public String getRequestedResponse() {
      return requestedResponse;
    }

    public void dispose() {
      session.disconnect();
      stompClient.stop();
    }
  }

  /***
   * This class mocks Picto Web server.
   * 
   * @author Alfa Yohannis
   *
   */
  private static class Server {

    private Thread pictoAppThread;

    public void start() throws InterruptedException {
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
    }

    public void stop() throws IOException, InterruptedException {
      PictoApplication.exit();
    }
  }
}
