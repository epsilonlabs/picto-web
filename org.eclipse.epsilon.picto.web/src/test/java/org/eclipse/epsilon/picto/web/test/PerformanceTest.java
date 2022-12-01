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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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

  private static List<GreedyVsSelectiveGenerationRecord> greedyVsSelectiveGenerationRecords = new ArrayList<>();
  private static List<GeneratedVsCachedViewRecord> generatedVsCachedViewRecords = new ArrayList<>();
  private static int gloNumViews;
  private static int gloNumIter;
  private static boolean genAll;
  private static boolean genAlways;

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
  public void testGeneratedVsCachedViewRequests() throws Exception {

    int numberOfNodes = 100; // Number of nodes the graph model.
    int numberOfClients = 100; // number of clients subscribed to Picto Web's STOMP server.
    int numberOfIteration = 10; // Number of requests per client

    boolean[] isAlwaysGenerated = { false, true };

    // create the initial model
    AssignmentStatement as = (AssignmentStatement) eolModule.getChildren().get(0).getChildren().get(0);
    IntegerLiteral il = (IntegerLiteral) as.getValueExpression();
    il.setText(String.valueOf(numberOfNodes));

    as = (AssignmentStatement) eolModule.getChildren().get(0).getChildren().get(1);
    il = (IntegerLiteral) as.getValueExpression();
    il.setText(String.valueOf(numberOfIteration));

    eolModule.execute();
    FileOutputStream fos = new FileOutputStream(modelFile);
    emfModel.store(fos);
    fos.flush();
    fos.close();

    // start Picto Web server
    server = new Server();
    server.start();
    Thread.sleep(5000);

    // create clients
    clients.clear();
    for (int i = 0; i < numberOfClients; i++) {
      Client client = new Client();
      clients.add(client);
      client.setName("Client-" + i);
//      client.start();
//      client.join(); // wait until each process finishes one by one
    }

    for (boolean temp : isAlwaysGenerated) {
      genAlways = temp;
      PictoApplication.setEachRequestAlwaysRegeneratesView(genAlways);

      for (Client client : clients) {
        client.sendMultipleRequests(numberOfIteration, numberOfNodes);
        Thread.sleep(1000);
      }
    }

    for (Client client : clients) {
      client.join();
    }

    Thread.sleep(2 * 60 * 1000);
    // saving data to a file
    File outputFile = new File("data/cached.csv");
    if (!outputFile.getParentFile().exists())
      outputFile.getParentFile().delete();
    if (outputFile.exists())
      outputFile.delete();
    outputFile.createNewFile();

    Files.write(Path.of(outputFile.toURI()),
        ("always,iteration,client,path,waittime,bytes" + System.lineSeparator()).getBytes(),
        StandardOpenOption.APPEND);

    for (GeneratedVsCachedViewRecord record : generatedVsCachedViewRecords) {
      String line = record.isAlwaysGenerated() + "," + record.getIteration()
          + "," + record.getClient().getName() + "," + record.getPath() + "," + record.getDuration()
          + "," + record.getPayloadSize() + System.lineSeparator();
      Files.write(Path.of(outputFile.toURI()), line.getBytes(), StandardOpenOption.APPEND);
    }
    System.out.println("FINISHED!");
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
    // clear all previous records
    greedyVsSelectiveGenerationRecords.clear();

    int numberOfNodes = 100; // Number of nodes the graph model.
    int numberOfClients = 1; // number of clients subscribed to Picto Web's STOMP server.
    int numberOfIteration = 10; // Number of iteration measuring for each number of affected views

    // a.k.a number of edges added for each modification + 3 views (origin node,
    // viewtree, overall graph)
//    int[] numbersOfAffectedViews = { 1, 6, 10/* , 60, 100, 600, 1000 */ };
    int[] numbersOfAffectedViews = {50};

    boolean[] genAllViews = {false};

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

    // modify
    try {

      for (boolean temp : genAllViews) {
        genAll = temp;
        if (temp) {
          PictoApplication.setModelModificationRegeneratesAllViews(true);
        } else {
          PictoApplication.setModelModificationRegeneratesAllViews(false);
        }

        // iterate for each number of affected views
        for (int numViews : numbersOfAffectedViews) {
          gloNumViews = numViews;
          for (int iterationIndex = 1; iterationIndex <= numberOfIteration; iterationIndex++) {
            gloNumIter = iterationIndex;

            System.out
                .println("\n## GEN ALL: " + genAll + " AFFECTED VIEWS: " + numViews + ", ITERATION: " + iterationIndex
                    + " ##");

            waitList.clear();
            Set<String> set = clients.stream().map(c -> c.getName()).collect(Collectors.toSet());
            waitList.addAll(set);
//        // System.out.println("Initial waitlist size = " + waitList.size());
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
            as = (AssignmentStatement) eolModule.getChildren().get(eolModule.getChildren().size() - 1)
                .getChildren().get(0);
            il = (IntegerLiteral) as.getValueExpression();
            il.setText(String.valueOf(numViews));

//        eolModule.execute();
            @SuppressWarnings("unchecked")
            EolSet<String> affectedNodes = (EolSet<String>) eolModule.execute();

            expectedViews = new HashSet<>();
            expectedViews.add("/");
            expectedViews.add("/Graph");
            expectedViews.add("/Graph/I0");

            // initially, all views should be generated
            if (!genAll) {
              if (iterationIndex == 1 && numViews == numbersOfAffectedViews[0]) {
                for (int a = 0; a < numberOfNodes; a++) {
                  expectedViews.add("/Graph/N" + a);
                }
              } else {
                for (String item : affectedNodes) {
                  expectedViews.add("/Graph/" + item);
                }
              }
            } else {
              for (int a = 0; a < numberOfNodes; a++) {
                expectedViews.add("/Graph/N" + a);
              }
            }

            System.out.println("Expected Views : " + expectedViews);

            fos = new FileOutputStream(modelFile);
            emfModel.store(fos);
            fos.close();
            Thread.sleep(100);
            startTime = System.currentTimeMillis();

//         wait until all clients are already subscribed to the broker
            synchronized (waitList) {
              waitList.wait();
            }
            // ----
          } // for (int i = 1; i <= numberOfIteration; i++) {
        } // for (int numViews : numbersOfAffectedViews) {
      }

      System.out.println("FINISHED!");

      // saving data to a file
      File outputFile = new File("data/selective.csv");
      if (!outputFile.getParentFile().exists())
        outputFile.getParentFile().delete();
      if (outputFile.exists())
        outputFile.delete();
      outputFile.createNewFile();

      Files.write(Path.of(outputFile.toURI()),
          ("genall,views,iteration,client,path,waittime,bytes" + System.lineSeparator()).getBytes(),
          StandardOpenOption.APPEND);

      for (GreedyVsSelectiveGenerationRecord record : greedyVsSelectiveGenerationRecords) {
        String line = record.isGenAll() + "," + record.getNumOfAffectedView() + "," + record.getIteration()
            + "," + record.getClient().getName() + "," + record.getPath() + "," + record.getDuration()
            + "," + record.getPayloadSize() + System.lineSeparator();
        Files.write(Path.of(outputFile.toURI()), line.getBytes(), StandardOpenOption.APPEND);
      }

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
            // System.out.println( "PICTO: " + Client.this.getName() + " connected with
            // sessionId " + stompSession.getSessionId());
          }
        }).get();

        // subscribe
        session.subscribe(PICTO_TOPIC + PICTO_FILE, new StompFrameHandler() {

          /**
           * Handle the payload from the Picto Web/broker
           * 
           * @param headers
           * @param payload
           */
          public void handleFrame(StompHeaders headers, Object payload) {
            long waitTime = (System.currentTimeMillis() - startTime);
            byte[] payloadByte = (byte[]) payload;
            String message = new String(payloadByte);
            try {
              JsonNode node = mapper.readTree(message);
              String path = node.get("path").textValue();
              GreedyVsSelectiveGenerationRecord record = new GreedyVsSelectiveGenerationRecord(genAll, gloNumViews,
                  gloNumIter, Client.this, path,
                  waitTime,
                  payloadByte.length);
              greedyVsSelectiveGenerationRecords.add(record);
           
              System.out
                  .println("PICTO: " + Client.this.getName() + ",  " + path + " " + " received, " + waitTime + " ms");
              
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
      // System.out.println("End " + getName());
    }

    /***
     * Randomly request different views from Picto Web
     * 
     * @param iteration
     * @param numberOfNodes
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws MalformedURLException
     * @throws IOException
     */
    public void sendMultipleRequests(int iteration, int numberOfNodes)
        throws JsonMappingException, JsonProcessingException, MalformedURLException, IOException {

      Thread t = new Thread() {
        public void run() {
          try {
            for (int i = 0; i < iteration; i++) {

              int num = random.nextInt(numberOfNodes);
              String expectedPath = "/Graph/N" + num;
              String expectedFile = "/performance/graph.picto";
              Map<String, String> parameters = new HashMap<>();
              parameters.put("file", expectedFile);
              parameters.put("path", expectedPath);
              parameters.put("name", "N" + num);

              String pageAddress = PICTO_WEB_ADDRESS + TestUtil.getParamsString(parameters);

//              URL url = new URL(pageAddress);
//              
//              long startTime = System.currentTimeMillis();
//              
//              HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
//              httpConnection.setRequestProperty("accept", "application/json");
//              
//              InputStream inputStream = httpConnection.getInputStream();
//              InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//              BufferedReader in = new BufferedReader(inputStreamReader);
//              String inputLine;
//              StringBuffer response = new StringBuffer();
//              while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//              }
//              
//              long duration = System.currentTimeMillis() - startTime;
//              
//              in.close();
//              httpConnection.disconnect();
//              
//              String message = response.toString();
//              int size = message.getBytes().length;
//
//              JsonNode node = mapper.readTree(message);

              long startTime = System.currentTimeMillis();
              JsonNode node = TestUtil.requestView(pageAddress);
              long waitTime = System.currentTimeMillis() - startTime;

              int size = node.toString().getBytes().length;
//              System.out
//                  .println("PICTO: " + Client.this.getName() + " received, " + size + " byte(s)");
              if (size > 0) {
                String path = node.get("path").textValue();
                System.out
                    .println("PICTO: " + Client.this.getName() + ",  " + path + " " + " received, " + waitTime + " ms");

                GeneratedVsCachedViewRecord record = new GeneratedVsCachedViewRecord(genAlways, i + 1, Client.this,
                    path,
                    waitTime, size);
                generatedVsCachedViewRecords.add(record);
              }

              Thread.sleep(1000);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      t.start();
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

  /***
   * Record for (always) generated vs cached view test
   * 
   * @author Alfa Yohannis
   *
   */
  static class GeneratedVsCachedViewRecord {
    boolean alwaysGenerate;
    int iteration;
    Client client;
    String path;
    long duration;
    int payloadSize;

    public GeneratedVsCachedViewRecord(boolean alwaysGenerate, int iteration, Client client, String path, long duration,
        int payloadSize) {
      this.alwaysGenerate = alwaysGenerate;
      this.iteration = iteration;
      this.client = client;
      this.path = path;
      this.duration = duration;
      this.payloadSize = payloadSize;
    }

    public int getIteration() {
      return iteration;
    }

    public boolean isAlwaysGenerated() {
      return alwaysGenerate;
    }

    public Client getClient() {
      return client;
    }

    public String getPath() {
      return path;
    }

    public long getDuration() {
      return duration;
    }

    public int getPayloadSize() {
      return payloadSize;
    }
  }

  /***
   * Record for All vs Selective Regeneration Test
   * 
   * @author Alfa Yohannis
   *
   */
  static class GreedyVsSelectiveGenerationRecord {

    boolean genAll;
    int numOfAffectedView;
    int iteration;
    Client client;
    String path;
    long duration;
    int payloadSize;

    public GreedyVsSelectiveGenerationRecord(boolean genAll, int numOfAffectedView, int iteration, Client client,
        String path,
        long duration,
        int payloadSize) {
      this.genAll = genAll;
      this.numOfAffectedView = numOfAffectedView;
      this.iteration = iteration;
      this.client = client;
      this.path = path;
      this.duration = duration;
      this.payloadSize = payloadSize;
    }

    public boolean isGenAll() {
      return genAll;
    }

    public int getNumOfAffectedView() {
      return numOfAffectedView;
    }

    public int getIteration() {
      return iteration;
    }

    public Client getClient() {
      return client;
    }

    public String getPath() {
      return path;
    }

    public long getDuration() {
      return duration;
    }

    public int getPayloadSize() {
      return payloadSize;
    }

  }
}
