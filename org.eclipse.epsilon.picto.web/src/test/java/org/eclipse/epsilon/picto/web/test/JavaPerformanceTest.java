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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.epsilon.picto.web.PictoApplication;
import org.eclipse.epsilon.picto.web.PromisesGenerationListener;
import org.eclipse.epsilon.picto.web.component.Server;
import org.eclipse.epsilon.picto.web.component.TestUtil;
import org.eclipse.gmt.modisco.java.BodyDeclaration;
import org.eclipse.gmt.modisco.java.ClassDeclaration;
import org.eclipse.gmt.modisco.java.FieldDeclaration;
import org.eclipse.gmt.modisco.java.Modifier;
import org.eclipse.gmt.modisco.java.TypeAccess;
import org.eclipse.gmt.modisco.java.VariableDeclarationFragment;
import org.eclipse.gmt.modisco.java.VisibilityKind;
import org.eclipse.gmt.modisco.java.emf.meta.JavaFactory;
import org.eclipse.gmt.modisco.java.emf.meta.JavaPackage;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
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
public class JavaPerformanceTest {

  static final String PICTO_WEB_ADDRESS = "http://localhost:8081/pictojson/picto?";
  public static final String WEB_SOCKET_ADDRESS = "ws://localhost:8081/picto-web";
  public static final String PICTO_FILE = "/java/java.picto";
  private static final String MODEL_ORIGINAL = "/java/java.big.xmi";
  private static final String MODEL = "/java/java.xmi";
  public static final String PICTO_TOPIC = "/topic/picto";
  private static final File modelFileOriginal = new File(PictoApplication.WORKSPACE + File.separator + MODEL_ORIGINAL);
  private static final File modelFile = new File(PictoApplication.WORKSPACE + File.separator + MODEL);
  private static final XMIResource resourceOriginal = new XMIResourceImpl(
      URI.createFileURI(modelFileOriginal.getAbsolutePath()));
  private static final XMIResource resource = new XMIResourceImpl(URI.createFileURI(modelFile.getAbsolutePath()));

  static final Random random = new Random();

  private static Server server;
  private static final List<Client> clients = new ArrayList<>();
  static Set<String> clientWaitingList = Collections.synchronizedSet(new HashSet<>());

  static ObjectMapper mapper = new ObjectMapper();
  static Set<String> expectedViews;
  private static List<String> classNames = new ArrayList<>();

  /**
   * @param args
   * @throws Exception 
   */
  @Test
  public void testInvalidatedViewsPerformance() throws Exception {

    PerformanceRecorder.startRecording();

    Object invalidatedViewsWaiter = new Object();
    final int numberOfMeasurementPoints = 5;

//    // configuration for larger experiment
//    int numberOfViews = 10000; // Number of nodes the graph model.
//    int numberOfClients = 100; // number of clients subscribed to Picto Web's STOMP server.
//    int numberOfIteration = 13; // Number of iteration measuring for each number of affected views
//    // a.k.a number of edges added for each modification + 3 views (origin node,
//    // viewtree, overall graph)
//    int[] numbersOfAffectedViews = { 10000, 5000, 1000, 500, 100, 50, 10, 5, 1 };
//    boolean[] genAllViews = { true, false };

//     configuration for smaller experiment
    int numberOfViews = 12; // Number of nodes the graph model
    int numberOfClients = 100; // number of clients subscribed to Picto Web's STOMP server.
    int numberOfIteration = 8; // Number of iteration measuring for each number of affected views
    int[] numbersOfAffectedViews = { 12, 8, 4 };
//    int[] numbersOfAffectedViews = { 100000 };
//    int[] numbersOfAffectedViews = { 10, 9, 8, 7, 5, 4, 3, 2, 1 };
    boolean[] genAllViews = { true, false };
//    boolean[] genAllViews = { true };

    Map<Object, Object> loadOptions = ((XMIResourceImpl)resource).getDefaultLoadOptions();
    loadOptions.put(XMIResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);

    Map<Object, Object> saveOptions = ((XMIResourceImpl)resource).getDefaultSaveOptions();
    saveOptions.put(XMIResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
    saveOptions.put(XMIResource.OPTION_PROCESS_DANGLING_HREF, XMIResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);

    JavaPackage.eINSTANCE.eClass();
    System.out.print("Loading the original model ... ");
    resourceOriginal.load(loadOptions);
    System.out.println("Done");
    System.out.print("Copy the original model ... ");
    resource.getContents().addAll(EcoreUtil.copyAll(resourceOriginal.getContents()));
    System.out.println("Done");
    System.out.print("Unload the original model ... ");
    resourceOriginal.unload();
    System.out.println("Done");

    List<String> classList = new ArrayList<>();
    classNames.clear();
    int counter = 0;
    TreeIterator<EObject> treeIterator = resource.getAllContents();
    while (treeIterator.hasNext()) {
      EObject eObject = treeIterator.next();
      resource.setID(eObject, "e" + counter);

      if (eObject instanceof ClassDeclaration) {
        ClassDeclaration cd = (ClassDeclaration) eObject;
        classNames.add(cd.getName());

        FieldDeclaration fd = JavaFactory.eINSTANCE.createFieldDeclaration();
        Modifier modifier = JavaFactory.eINSTANCE.createModifier();
        modifier.setVisibility(VisibilityKind.PRIVATE);
        fd.setModifier(modifier);
        TypeAccess typeAccess = JavaFactory.eINSTANCE.createTypeAccess();
        typeAccess.setType(cd);
        fd.setType(typeAccess);
        VariableDeclarationFragment vd = JavaFactory.eINSTANCE.createVariableDeclarationFragment();
        vd.setName("dummy");
        fd.getFragments().add(0, (VariableDeclarationFragment) vd);
        cd.getBodyDeclarations().add(0, fd);

        counter++;
        resource.setID(modifier, "e" + counter);
        counter++;
        resource.setID(typeAccess, "e" + counter);
        counter++;
        resource.setID(vd, "e" + counter);
        counter++;
        resource.setID(fd, "e" + counter);

        classList.add(resource.getID(eObject));
      }

      counter += 1;
    }
    resource.save(saveOptions);
//    resource.unload();

    numberOfViews = classList.size();
    PerformanceRecorder.globalNumberOfViews = numberOfViews;

    numbersOfAffectedViews = new int[numberOfMeasurementPoints];
    for (

        int i = 0; i < numberOfMeasurementPoints; i++) {
      numbersOfAffectedViews[i] = numberOfViews - (numberOfViews / numberOfMeasurementPoints) * i;
    }
    System.out.println("Iteration = " + Arrays.toString(numbersOfAffectedViews));

    // start Picto Web server
    System.out.println("Starting server ...");
    server = new Server();
    server.start();
    Thread.sleep(1000);

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
        PictoApplication.setGreedyGeneration(PerformanceRecorder.genenerateAll);

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

            expectedViews = new HashSet<>();

//            resource.load(options);

            Object previousMovedElement = new Object();
            Set<String> temp2 = new HashSet<>();
            for (int i = 0; i < numViews; i++) {

              // update the names of variables
              String id1 = classList.get(i);
              ClassDeclaration class1 = (ClassDeclaration) resource.getEObject(id1);
              List<BodyDeclaration> fields = class1.getBodyDeclarations().stream()
                  .filter(b -> b instanceof FieldDeclaration).toList();
              if (fields.size() > 0) {
                BodyDeclaration element = null;
//              FieldDeclaration element = (FieldDeclaration) class1.getBodyDeclarations().get(0);
//                while (!(/* element instanceof MethodDeclaration || */ element instanceof FieldDeclaration)
//                    || previousMovedElement.equals(element)) {
//                  element = fields.get(random.nextInt(fields.size()));
                element = fields.get(0);
//                }
                VariableDeclarationFragment variable = ((FieldDeclaration) element).getFragments().get(0);
                variable.setName(variable.getName() + "_" + numViews);
              }

              // move methods
              id1 = classList.get(i);
              class1 = (ClassDeclaration) resource.getEObject(id1);
              List<BodyDeclaration> fieldsAndmethods = class1.getBodyDeclarations().stream()
                  .filter(b -> b instanceof FieldDeclaration).toList();

              if (fieldsAndmethods.size() > 0) {
                String id2 = (i == numViews - 1) ? classList.get(0) : classList.get(i + 1);
                ClassDeclaration class2 = (ClassDeclaration) resource.getEObject(id2);
                temp2.add(class1.getName());
                temp2.add(class2.getName());
                BodyDeclaration movedElement = null;
//                while (/*
//                        * !(movedElement instanceof MethodDeclaration || movedElement instanceof
//                        * FieldDeclaration) ||
//                        */previousMovedElement.equals(movedElement)) {
                movedElement = fieldsAndmethods.get(0);
//                }
                class2.getBodyDeclarations().add(movedElement);
                previousMovedElement = movedElement;
              }

            }

            resource.save(saveOptions);

//          Thread.sleep(100);
            PerformanceRecorder.startTime = System.currentTimeMillis();

            // wait until we get the paths of invalidated views
            synchronized (invalidatedViewsWaiter) {
              invalidatedViewsWaiter.wait();
              System.out.println("Expected Views : " + expectedViews);
            }

            synchronized (clientWaitingList) {
              clientWaitingList.wait(5 * 60 * 1000);
            }
            // ----
          } // for (int i = 1; i <= numberOfIteration; i++) {
        } // for (int numViews : numbersOfAffectedViews) {
      }

      Thread.sleep(1000);
      System.out.println("FINISHED!");

      // saving data to a file
      File outputFile = new File("data/selective.csv");
      if (!outputFile.getParentFile().exists())
        outputFile.getParentFile().mkdir();
      if (outputFile.exists())
        outputFile.delete();
      outputFile.createNewFile();

      Thread.sleep(1000);

      Files.write(Path.of(outputFile.toURI()),
          ("genall,views,iteration,client,path,waittime,bytes,type" + System.lineSeparator()).getBytes(),
          StandardOpenOption.APPEND);

      for (PerformanceRecord record : PerformanceRecorder.getPerformanceRecords()) {
        String line = ((record.isGenAll()) ? "all" : "N") + "," + record.getNumOfInvalidatedViews() + ","
            + record.getIteration() + "," + record.getClient() + "," + record.getPath() + "," + record.getDuration()
            + "," + record.getPayloadSize() + "," + record.getType() + System.lineSeparator();
        Files.write(Path.of(outputFile.toURI()), line.getBytes(), StandardOpenOption.APPEND);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    for (Client client : clients) {
      client.shutdown();
    }
    
    PerformanceRecorder.stopRecording();
    server.stop();
//    System.exit(0);
  }

  /***
   * This class mocks Picto Web client.
   * 
   * @author Alfa Yohannis
   *
   */
  public static class Client extends Thread {

    private WebSocketStompClient stompClient;
    private StompSession session;
    private HttpClient jettyHttpClient;

    
    public void shutdown() throws Exception {
      session.disconnect();
      stompClient.stop();
      jettyHttpClient.stop();
      jettyHttpClient.destroy();
    }
    
    /***
     * The methods connects the client to the STOMP server provided by Picto Web and
     * starts retrieving messages.
     */
    public void run() {

      jettyHttpClient = new HttpClient();
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
            System.out.println(
                "PICTO: " + Client.this.getName() + " connected with sessionId " + stompSession.getSessionId());
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

            try {
              // get and parse the payload into json
              JsonNode node = mapper.readTree(new String((byte[]) payload));
              String type = node.get("type").textValue();
              if (!"newviews".equals(type)) {
                return;
              }

              String content = node.get("content").textValue();
              List<String> invalidatedViews = mapper.readValue(content, new TypeReference<List<String>>() {
              });

              // randomly select the view to be requested
//              String invalidatedView = "/Graph/N" + random.nextInt(PerformanceRecorder.globalNumberOfViews);
              String invalidatedView = "/" + classNames.get(random.nextInt(classNames.size()));

              // request the content
              String expectedPath = invalidatedView;
              String expectedFile = "/java/java.picto";
              Map<String, String> parameters = new HashMap<>();
              parameters.put("file", expectedFile);
              parameters.put("path", expectedPath);

              String pageAddress = PICTO_WEB_ADDRESS + TestUtil.getParamsString(parameters);
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
              JsonNode contentNode = mapper.readTree(new String((byte[]) viewBytes));
              String path = contentNode.get("path").textValue();

              // record the response time, from requesting a view to receiving the view
              PerformanceRecord record = new PerformanceRecord(PerformanceRecorder.genenerateAll,
                  PerformanceRecorder.generateAlways, PerformanceRecorder.globalNumberOfAffectedViews,
                  PerformanceRecorder.globalNumberIteration, Client.this.getName(), path, responseTime,
                  viewBytes.length, PerformanceTestType.RESPONSE_TIME);
              PerformanceRecorder.getPerformanceRecords().add(record);

              // record the overall time from changing a model file to receiving the view
              record = new PerformanceRecord(PerformanceRecorder.genenerateAll, PerformanceRecorder.generateAlways,
                  PerformanceRecorder.globalNumberOfAffectedViews, PerformanceRecorder.globalNumberIteration,
                  Client.this.getName(), path, overallTime, viewBytes.length, PerformanceTestType.OVERALL_TIME);
              PerformanceRecorder.getPerformanceRecords().add(record);

              System.out.println("PICTO: Type " + PerformanceTestType.OVERALL_TIME + ", GenAll "
                  + PerformanceRecorder.genenerateAll + ", N-views " + PerformanceRecorder.globalNumberOfAffectedViews
                  + ", Iter " + PerformanceRecorder.globalNumberIteration + ", " + Client.this.getName()
                  + " received, path " + "No Path" + ", time " + overallTime + " ms");

            } catch (IOException e) {
              e.printStackTrace();
            }

            synchronized (clientWaitingList) {
              clientWaitingList.remove(Client.this.getName());
//              System.out.println("Waiting list = " + clientWaitingList);
              if (clientWaitingList.isEmpty()) {
                clientWaitingList.notify();
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

              int num = random.nextInt(numberOfNodes);
              String expectedPath = "/Graph/N" + num;
              String expectedFile = "/performance/graph.picto";
              Map<String, String> parameters = new HashMap<>();
              parameters.put("file", expectedFile);
              parameters.put("path", expectedPath);
              parameters.put("name", "N" + num);

              String pageAddress = PICTO_WEB_ADDRESS + TestUtil.getParamsString(parameters);

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

              JsonNode node = mapper.readTree(message);

              if (size > 0) {
                String receivedPath = node.get("path").textValue();

                System.out.println("PICTO: Type " + PerformanceTestType.RESPONSE_TIME + ", GenAlways "
                    + PerformanceRecorder.generateAlways + ", N-views " + numberOfNodes + ", Iter " + (i + 1) + ", "
                    + Client.this.getName() + " received, path " + receivedPath + ", time " + waitTime + " ms");

//                System.out
//                    .println("PICTO: always generate " + genAlways + ", " + Client.this.getName()
//                        + ",  request "
//                        + (i + 1) + ", " + receivedPath + " "
//                        + " received, " + waitTime
//                        + " ms");

                PerformanceRecord record = new PerformanceRecord(PerformanceRecorder.genenerateAll,
                    PerformanceRecorder.generateAlways, numberOfNodes, i + 1, Client.this.getName(), receivedPath,
                    waitTime, size, PerformanceTestType.RESPONSE_TIME);
                PerformanceRecorder.getPerformanceRecords().add(record);
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