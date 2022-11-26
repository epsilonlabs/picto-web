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
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfUtil;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.dom.AssignmentStatement;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.IEolVisitor;
import org.eclipse.epsilon.eol.dom.IntegerLiteral;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PictoWebOnLoadedListener;
import org.eclipse.jetty.client.HttpChannel;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.JettyXhrTransport;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

  private static ObjectMapper mapper;
  private static DocumentBuilder builder;

  private static final int numberOfNodes = 20;
  private static final int numberOfClients = 4;
  private static final int numberOfEdges = 3;
  private static final int duration = 5 * 60 * 1000; // miliseconds
  private static final int modifyEvery = 3 * 1000; // miliseconds
  private static final double addProbability = 0.8; // 0.0 to 1.0

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

    // create a model
    emfModel.setResource(resource);
    emfModel.setReadOnLoad(true);
    emfModel.setExpand(true);
    emfModel.setName("M");
    emfModel.setMetamodelUri("graph");
    emfModel.setModelFile(modelFile.getAbsolutePath());
    eolModule.getContext().getModelRepository().addModel(emfModel);

    eolModule.parse(opInitFile);

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
  }

  /***
   * Shutdown Picto Web server.
   * 
   * @throws Exception
   */
  @AfterAll
  static void tearDownAfterClass() throws Exception {
    server.stop();
  }

  @Test
  public void testPerformance() throws Exception {

    // create clients
    for (int i = 0; i < numberOfClients; i++) {
      Client client = new Client();
      client.setName("Client-" + i);
      client.start();
    }

    ModelModifier modelModifier = new ModelModifier();
    modelModifier.start();

    Thread.sleep(duration);
    modelModifier.shutdown();
  }

  private class ModelModifier extends Thread {
    boolean isRunning = false;

    public void shutdown() {
      isRunning = false;
      synchronized (this) {
        this.notify();
      }
    }

    public void run() {
      try {
        isRunning = true;
        while (isRunning) {
          eolModule.getChildren().clear();
          eolModule.clearCache();

          // if true then add edges (60% probability), else delete edges (40% probability)
          boolean isAdd = (random.nextDouble() <= addProbability) ? true : false;
          // number of edges to be added or deleted
          int n = random.nextInt(numberOfEdges);
//          int n = 1;

          // if true then add edges, else delete edges
          if (isAdd) {
            eolModule.parse(opAddEdgeFile);
          } else {
            eolModule.parse(opDelEdgeFile);
          }

          AssignmentStatement as = (AssignmentStatement) eolModule.getChildren().get(eolModule.getChildren().size() - 1)
              .getChildren().get(0);
          IntegerLiteral il = (IntegerLiteral) as.getValueExpression();
          il.setText(String.valueOf(n));

          eolModule.execute();

          FileOutputStream fos = new FileOutputStream(modelFile);
          emfModel.store(fos);
          fos.flush();
          fos.close();

          synchronized (this) {
            this.wait(modifyEvery);
          }
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

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
   * This class mocks Picto Web client.
   * 
   * @author Alfa Yohannis
   *
   */
  static class Client extends Thread {

    private WebSocketStompClient stompClient;
    private StompSession session;
    private String requestedResponse;

    public void run() {
      // initialise connection to the stomp server
      HttpClient jettyHttpClient = new HttpClient();
      jettyHttpClient.setMaxConnectionsPerDestination(Integer.MAX_VALUE);
      jettyHttpClient.setExecutor(new QueuedThreadPool(Integer.MAX_VALUE));
      try {
        jettyHttpClient.start();
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      
      StandardWebSocketClient client = new StandardWebSocketClient();
      List<Transport> transports = new ArrayList<>(2);
//      WebSocketTransport wst = new WebSocketTransport(client);
//      RestTemplateXhrTransport rtxt = new RestTemplateXhrTransport();
      JettyXhrTransport jxt = new JettyXhrTransport(jettyHttpClient);
//      transports.add(wst);
//      transports.add(rtxt);
      transports.add(jxt);
      SockJsClient sockJsClient = new SockJsClient(transports);
      
      
      
      stompClient = new WebSocketStompClient(sockJsClient);
      try {
//        StompHeaders sh = new StompHeaders();
//        sh.setHeartbeat(new long[] {2000, 2000});
        DefaultManagedTaskScheduler ts = new DefaultManagedTaskScheduler();
        stompClient.setTaskScheduler(ts);
        stompClient.setDefaultHeartbeat(new long[] {Long.MAX_VALUE, Long.MAX_VALUE});
        stompClient.setReceiptTimeLimit(Long.MAX_VALUE);
        stompClient.setInboundMessageSizeLimit(Integer.MAX_VALUE);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        session = stompClient.connect(WEB_SOCKET_ADDRESS, new SessionHandler()).get();

        session.subscribe(PICTO_TOPIC + PICTO_FILE, new StompFrameHandler() {
          public void handleFrame(StompHeaders headers, Object payload) {
            System.out.println("PICTO: " + Client.this.getName() + " received");
//            String message = new String((byte[]) payload);
//            System.out.println(message);
//            session.subscribe(PICTO_TOPIC + PICTO_FILE, this);
//            session.acknowledge(headers, true);
//            session.disconnect();
//            System.console();
          }

          public Type getPayloadType(StompHeaders headers) {
            return byte[].class;
//            return null;

          }
        });
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

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
}
