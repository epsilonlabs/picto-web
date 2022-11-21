package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.emfatic.core.EmfaticResourceFactory;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.CachedResourceSet;
import org.eclipse.epsilon.emc.emf.CachedResourceSet.Cache;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfUtil;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.FrameStack;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.EolAnyType;
import org.eclipse.epsilon.flexmi.FlexmiResourceFactory;
import org.eclipse.epsilon.picto.Layer;
import org.eclipse.epsilon.picto.LazyEgxModule;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRule;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.PictoView;
import org.eclipse.epsilon.picto.StaticContentPromise;
import org.eclipse.epsilon.picto.ViewTree;
import org.eclipse.epsilon.picto.dom.CustomView;
import org.eclipse.epsilon.picto.dom.Model;
import org.eclipse.epsilon.picto.dom.Parameter;
import org.eclipse.epsilon.picto.dom.Patch;
import org.eclipse.epsilon.picto.dom.Picto;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.dummy.IEditorPart;
import org.eclipse.epsilon.picto.incrementality.AccessRecordResource;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule.IncrementalLazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.incrementality.Util;
import org.eclipse.epsilon.picto.source.EglPictoSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebEglPictoSource extends EglPictoSource {

  public static boolean generateAll = true; // true for first time running

  public Map<String, String> generatePromises(String modifiedFilePath, PictoProject pictoProject) throws Exception {
    return this.generatePromises(modifiedFilePath, pictoProject, false);
  }

  /***
   * This method is executed when a file associated with a Picto file is modified,
   * Picto Web is accessed for the first time. The method generate Promises, which
   * contain references to EGX rules, EGL templates, and models. The final views
   * haven't been generated yet. When the promises are executed, they generate the
   * final views displayed to users.
   * 
   * @param modifiedFilePath The path to the modified file.
   * @param pictoProject     The PictoProject, a concept that .
   * @param fromFileWatcher  Indicating if the the method is executed by the
   *                         FileWatcher. False means the method is triggered
   *                         by a request from a client (e.g., when Picto Web is
   *                         accessed for the first time), not from file
   *                         modification.
   * @return The paths affected by the modification.
   * @throws Exception
   */
  @SuppressWarnings({ "unchecked", "deprecation", "null" })
  public Map<String, String> generatePromises(String modifiedFilePath, PictoProject pictoProject,
      boolean fromFileWatcher) throws Exception {
    Map<String, String> modifiedViewContents = new HashMap<>();
    try {
      
      
      PictoView pictoView = new PictoView();
      ViewTree rootViewTree = pictoView.getViewTree();

      File pictoFile = pictoProject.getPictoFile();
      String pictoFilePath = pictoFile.getAbsolutePath()
          .replace(new File(PictoApplication.WORKSPACE).getAbsolutePath(), "").replace("\\", "/");

      PromiseViewCache viewContentCache = FileViewContentCache.addPictoFile(pictoFilePath);
      AccessRecordResource accessRecordResource = FileViewContentCache.createAccessRecordResource(pictoFilePath);

      Picto picto = this.getRenderingMetadata(pictoFile);

      if (picto != null) {

        // Define the EgxModule.
        IEolModule module;
        if (picto.getFormat() == null)
          picto.setFormat("egx");
        if ("egx".equals(picto.getFormat())) {
          module = new IncrementalLazyEgxModule(accessRecordResource);
        } else {
          module = new EglTemplateFactoryModuleAdapter(new EglFileGeneratingTemplateFactory());
        }

        // Get the transformation context.
        IEolContext context = module.getContext();

        // Get all parameters from the Picto file.
        FrameStack fs = context.getFrameStack();
        for (Parameter customParameter : picto.getParameters()) {
          fs.put(new Variable(customParameter.getName(), getValue(customParameter), EolAnyType.Instance));
        }

        // Parse the EgxModule. 
        URI transformationUri = null;
        if (picto.getTransformation() != null) {
          transformationUri = new File(
              pictoFile.getParentFile().getAbsolutePath() + File.separator + picto.getTransformation())
              .toURI();
          module.parse(transformationUri);
        } else {
          module.parse("");
        }

        // load all models defined in the Picto file
        File mainModelFile = null;
        for (Model toBeLoadedModel : picto.getModels()) {
          File modelFile = null;
          File metamodelFile = null;
          String modelName = null;
          String metamodelUri = null;
          for (Parameter param : toBeLoadedModel.getParameters()) {
            if (param.getName().equals(EmfModel.PROPERTY_NAME)) {
              modelName = (String) param.getValue();
            } else if (param.getName().equals(EmfModel.PROPERTY_MODEL_FILE)) {
              modelFile = new File(pictoFile.getParent() + File.separator + param.getFile());
              if (mainModelFile == null)
                mainModelFile = modelFile;
            } else if (param.getName().equals(EmfModel.PROPERTY_METAMODEL_FILE)) {
              metamodelFile = new File(pictoFile.getParent() + File.separator + param.getFile());
            } else if (param.getName().equals(EmfModel.PROPERTY_METAMODEL_URI)) {
              metamodelUri = (String) param.getValue();
            }
          }
          IModel loadedModel = loadModel(modelName, metamodelUri, modelFile, metamodelFile);
          models.add(loadedModel);
        }

        context.getModelRepository().addModels(models);

        if ("egx".equals(picto.getFormat())) {

          Set<String> toBeProcessedPaths = new HashSet<>();
//
          /** PROPERTY ACCESS RECORDS **/
          ((IncrementalLazyEgxModule) module).startRecording();
          List<IncrementalLazyGenerationRuleContentPromise> promises = (List<IncrementalLazyGenerationRuleContentPromise>) module
              .execute();
          ((IncrementalLazyEgxModule) module).stopRecording();
//          accessRecordResource.printIncrementalRecords();
//
          /**
           * the handleDynamicViews will add the generated lazy contents (tagged
           * with @lazy in the EGX) to instances to
           * handled later in the next loop
           **/
          promises.addAll(handleCustomViews(picto, module, context, fs));

          /** loop through the content promises of rules **/
          System.out.println("\nGENERATING VIEWS: ");
          toBeProcessedPaths.addAll(accessRecordResource.getToBeProcessedPaths(promises, (EgxModule) module));
//
          for (IncrementalLazyGenerationRuleContentPromise promise : promises) {

            String pathString = Util.getPath(promise);

            System.out.print("Processing " + pathString + " ... ");

            ViewTree viewTree = generateViewTree(rootViewTree, promise);

            // Replace the old promises with the new ones.
            PromiseView promiseView = new PromiseView(pictoView, viewTree);
            viewContentCache.putPromiseView(promiseView);

            // Check if the path should be processed to generated new view.
            // Skip to next promise if path is not in the the toBeProcessedPaths.
            if (!toBeProcessedPaths.contains(pathString)) {
              System.out.println("SKIP");
              continue;
            }

            // Immediately generates the final view of the UN-skipped path and put the view
            // into the modified contents so that it can be immediately sent to the Broker
            // server and then retrieved by clients.
            String content = "";
            if (fromFileWatcher) {
              content = promiseView.getViewContent(null);
            }
            modifiedViewContents.put(pathString, content);

            System.out.println("PROCESSED");

          }

          accessRecordResource.printIncrementalRecords();
          accessRecordResource.updateStatusToProcessed(toBeProcessedPaths);
          generateAll = false;

          System.out.println();
        }
        // if it's other than EGX transformation, use static content promise.
        else {
          String content = module.execute() + "";
          rootViewTree = new ViewTree();
          rootViewTree.setPromise(new StaticContentPromise(content));
          rootViewTree.setFormat(picto.getFormat());
        }

        // Handle static views (i.e. where source != null), add the custom view loaded
        // from a file
        // defined in the picto file
        handleStaticViews(modifiedViewContents, rootViewTree, pictoFilePath, viewContentCache,
            picto, module, pictoView);

        // Handle patches for existing views (i.e. where source == null and type/rule ==
        // null)
        handlePatchesForExistingViews(rootViewTree, picto);

        // set URIs of rootViewTree
        if (transformationUri != null) {
          rootViewTree.getBaseUris().add(transformationUri);
          rootViewTree.getBaseUris().add(transformationUri.resolve("./icons/"));
        }
        rootViewTree.getBaseUris().add(new URI(mainModelFile.toURI().toString()));

        // generate JSON of the JsTree library (the tree panel on the client-side web
        // browser)
        PictoResponse pictoResponse = generateJsTreeData(pictoFilePath, rootViewTree);
        PromiseView promiseView = new PromiseView(pictoResponse);
        viewContentCache.putPromiseView(promiseView);
        modifiedViewContents.put(promiseView.getPath(), promiseView.getViewContent());

      } else {
        rootViewTree = createEmptyViewTree();
        PictoResponse pictoResponse = generateJsTreeData(pictoFilePath, rootViewTree);
        PromiseView promiseView = new PromiseView(pictoResponse);
        modifiedViewContents.put(promiseView.getPath(), promiseView.getViewContent());
      }

//      if (fromFileWatcher) {
//        for (IModel model : models) {
//          model.dispose();
//        }
//      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return modifiedViewContents;

  }

  /***
   * The method to handle patches for existing views.
   * 
   * @param rootViewTree
   * @param renderingMetadata
   */
  private void handlePatchesForExistingViews(ViewTree rootViewTree, Picto renderingMetadata) {
    for (CustomView customView : renderingMetadata.getCustomViews().stream()
        .filter(cv -> cv.getSource() == null && cv.getType() == null).collect(Collectors.toList())) {
      ArrayList<String> path = new ArrayList<>();
      path.add(rootViewTree.getName());
      path.addAll(customView.getPath());

      ViewTree existingView = rootViewTree.forPath(path);

      if (existingView != null) {
        if (customView.getIcon() != null)
          existingView.setIcon(customView.getIcon());
        if (customView.getFormat() != null)
          existingView.setFormat(customView.getFormat());
        if (customView.getPosition() != null)
          existingView.setPosition(customView.getPosition());

        existingView.getPatches().addAll(customView.getPatches());
        if (customView.eIsSet(PictoPackage.eINSTANCE.getCustomView_Layers())) {
          Collection<?> layers = customView.getLayers();
          for (Layer layer : existingView.getLayers()) {
            layer.setActive(layers.contains(layer.getId()));
          }
        }
      }
    }
  }

  /***
   * The method to handle static view, the view loaded from a HTML file.
   * 
   * @param modifiedViewContents
   * @param rootViewTree
   * @param filename
   * @param viewContentCache
   * @param picto
   * @param module
   * @param pictoView
   * @throws Exception
   */
  private void handleStaticViews(Map<String, String> modifiedViewContents, ViewTree rootViewTree, String filename,
      PromiseViewCache viewContentCache, Picto picto, IEolModule module, PictoView pictoView)
      throws Exception {
    for (CustomView customView : picto.getCustomViews().stream().filter(cv -> cv.getSource() != null)
        .collect(Collectors.toList())) {
      String format = customView.getFormat() != null ? customView.getFormat() : getDefaultFormat();
      String icon = customView.getIcon() != null ? customView.getIcon() : getDefaultIcon();

      rootViewTree.add(customView.getPath(),
          new ViewTree(
              new StaticContentPromise(
                  new File(new File(customView.eResource().getURI().toFileString()).getParentFile(),
                      customView.getSource())),
              format, icon, customView.getPosition(), customView.getPatches(), Collections.emptyList()));

      ViewTree vt = rootViewTree.getChildren().get(rootViewTree.getChildren().size() - 1);

      PromiseView promiseView = new PromiseView(pictoView, vt);
      viewContentCache.putPromiseView(promiseView.getPath(), promiseView);
      modifiedViewContents.put(promiseView.getPath(), promiseView.getViewContent());
    }
  }

  /***
   * The method to handle custom views defined in the Picto file.
   * 
   * @param picto
   * @param module
   * @param context
   * @param fs
   * @return
   * @throws EolRuntimeException
   */
  private List<IncrementalLazyGenerationRuleContentPromise> handleCustomViews(Picto picto,
      IEolModule module, IEolContext context, FrameStack fs) throws EolRuntimeException {
    List<IncrementalLazyGenerationRuleContentPromise> customPromises = new ArrayList<>();
    List<CustomView> customViews = picto.getCustomViews().stream().filter(cv -> cv.getType() != null)
        .collect(Collectors.toList());
    for (CustomView customView : customViews) {

      LazyGenerationRule generationRule = ((LazyEgxModule) module).getGenerationRules().stream()
          .filter(r -> r.getName().equals(customView.getType()) && r instanceof LazyGenerationRule)
          .map(LazyGenerationRule.class::cast).findFirst().orElse(null);

      if (generationRule != null) {
        Object source = null;
        if (generationRule.getSourceParameter() != null) {
          String sourceParameterName = generationRule.getSourceParameter().getName();
          Parameter sourceParameter = customView.getParameters().stream()
              .filter(sp -> sp.getName().equals(sourceParameterName)).findFirst().orElse(null);
          if (sourceParameter != null) {
            customView.getParameters().remove(sourceParameter);
            source = sourceParameter.getValue();
          }
        }

        if (customView.getPath() != null)
          customView.getParameters().add(createParameter("path", customView.getPath()));
        if (customView.getIcon() != null)
          customView.getParameters().add(createParameter("icon", customView.getIcon()));
        if (customView.getFormat() != null)
          customView.getParameters().add(createParameter("format", customView.getFormat()));
        customView.getParameters().add(createParameter("patches", customView.getPatches()));
        if (customView.getPosition() != null)
          customView.getParameters().add(createParameter("position", customView.getPosition()));
        if (customView.eIsSet(PictoPackage.eINSTANCE.getCustomView_Layers())) {
          customView.getParameters().add(createParameter("activeLayers", customView.getLayers()));
        }

        for (Parameter customViewParameter : customView.getParameters()) {
          fs.put(new Variable(customViewParameter.getName(), getValue(customViewParameter),
              EolAnyType.Instance));
        }

        ((IncrementalLazyEgxModule) module).stopRecording();
        IncrementalLazyGenerationRuleContentPromise contentPromise = (IncrementalLazyGenerationRuleContentPromise) generationRule
            .execute(context, source);
        ((IncrementalLazyEgxModule) module).stopRecording();
//				WebEglPictoSource.updateIncrementalResource(module, "/" + String.join("/", customView.getPath()));

        Collection<Variable> variables = contentPromise.getVariables();

        for (Parameter parameter : customView.getParameters()) {
          Object value = getValue(parameter);
          String paramName = parameter.getName();

          Variable variable = variables.stream().filter(v -> v.getName().equals(paramName)).findAny()
              .orElse(null);

          if (variable != null) {
            variable.setValue(value, context);
          } else {
            variables.add(new Variable(paramName, value, EolAnyType.Instance, false));
          }
        }
        customPromises.add(contentPromise);
      }
    }
    return customPromises;
  }

  /***
   * Generate the response containing the data for the JsTree panel on the client
   * side.
   * 
   * @param filename
   * @param viewTree
   * @return
   * @throws JsonProcessingException
   */
  private PictoResponse generateJsTreeData(String filename, ViewTree viewTree) throws JsonProcessingException {
    JsTreeNode root = new JsTreeNode();
    copyViewTreeToJsTreeData(filename, viewTree, root);
    List<JsTreeNode> jsTreeNodes = new ArrayList<>();
    jsTreeNodes.addAll(root.getChildren());

    String response = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsTreeNodes);

    PictoResponse pictoResponse = new PictoResponse();
    pictoResponse.setFilename(filename);
    pictoResponse.setPath(FileViewContentCache.PICTO_TREE);
    pictoResponse.setType("treeview");
    pictoResponse.setContent(response);

    return pictoResponse;

  }

  /***
   * Copy some information from the Picto ViewTree nodes to JsTree nodes to
   * represent
   * the ViewTree as JsTree on the client side.
   * 
   * @param filename
   * @param viewTree
   * @param jsTreeNode
   */
  private void copyViewTreeToJsTreeData(String filename, ViewTree viewTree, JsTreeNode jsTreeNode) {
    for (ViewTree viewTreeChild : viewTree.getChildren()) {
      JsTreeNode jsonChild = new JsTreeNode();
      jsTreeNode.getChildren().add(jsonChild);
      copyViewTreeToJsTreeData(filename, viewTreeChild, jsonChild);
    }
    String text = viewTree.getName();
    jsTreeNode.setText(text);
    jsTreeNode.setPath(viewTree.getPathString());
    if (viewTree.getIcon() != null) {
      jsTreeNode.setIcon(viewTree.getIcon());
    }
  }

  /***
   * Load model file as an EmfModel.
   * 
   * @param modelName
   * @param metamodelUri
   * @param modelFile
   * @param metamodelFile
   * @return
   * @throws Exception
   */
  private IModel loadModel(String modelName, String metamodelUri, File modelFile, File metamodelFile)
      throws Exception {
    org.eclipse.emf.common.util.URI uri = (metamodelFile != null) ? loadMetamodel(metamodelFile) : null;

    EmfModel newModel = null;
    Cache cache = CachedResourceSet.getCache();
    cache.clear();
    if (modelFile.getName().endsWith(".model") || modelFile.getName().endsWith(".xmi")) {
      newModel = new EmfModel();
    } else {
      newModel = new InMemoryEmfModel(modelName, getResource(modelFile));
    }
    newModel.setReadOnLoad(true);
    newModel.setExpand(true);
    newModel.setName(modelName);

    if (metamodelUri != null)
      newModel.setMetamodelUri(metamodelUri);
    if (metamodelFile != null)
      newModel.setModelFile(modelFile.getAbsolutePath());
    if (uri != null)
      newModel.setMetamodelFileUri(uri);
    newModel.load();
    return newModel;

  }

  /***
   * Load a metamodel from a file.
   * 
   * @param metamodelFile The File object that represents the file.
   * @return
   * @throws Exception
   */
  private org.eclipse.emf.common.util.URI loadMetamodel(File metamodelFile) throws Exception {
    org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI
        .createFileURI(metamodelFile.getAbsolutePath());
    EmfUtil.register(uri, EPackage.Registry.INSTANCE);
    return uri;
  }

  /***
   * Load a resource from a file.
   * 
   * @param modelFile The File object that represents the file.
   * @return The Resource.
   */
  private Resource getResource(File modelFile) {
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("emf", new EmfaticResourceFactory());
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("model", new XMIResourceFactoryImpl());
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new FlexmiResourceFactory());
    Resource resource = resourceSet
        .getResource(org.eclipse.emf.common.util.URI.createFileURI(modelFile.getAbsolutePath()), true);
    try {
      resource.load(null);
      return resource;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /***
   * Load the Picto file to get the rendering metadata.
   * 
   * @param pictoFile The File object that represents the Picto file.
   * @return
   */
  private Picto getRenderingMetadata(File pictoFile) {
    try {
      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new FlexmiResourceFactory());
      Resource resource = resourceSet
          .getResource(org.eclipse.emf.common.util.URI.createFileURI(pictoFile.getAbsolutePath()), true);
      resource.load(null);
      return (Picto) resource.getContents().iterator().next();
    } catch (Exception ex) {
      return null;
    }
  }

  /***
   * A method to generate ViewTree data structure, the tree displayed on the left
   * panel.
   * 
   * @param rootViewTree
   * @param instance
   * @return
   */
  @SuppressWarnings("unchecked")
  public static ViewTree generateViewTree(ViewTree rootViewTree, LazyGenerationRuleContentPromise instance) {
    String format = getDefaultFormat();
    String icon = getDefaultIcon();
    List<Patch> patches = new ArrayList<>(1);
    Collection<String> path = Arrays.asList("");
    List<Layer> layers = new ArrayList<>();
    Variable layersVariable = null;
    Integer position = null;

    Collection<Variable> instanceVariables = instance.getVariables();

    for (Variable variable : instanceVariables) {
      Object varValue = variable.getValue();
      switch (variable.getName()) {
        case "format": {
          format = varValue + "";
          break;
        }
        case "path": {
          if (!(varValue instanceof Collection)) {
            (path = (Collection<String>) (varValue = new ArrayList<>(1))).add(Objects.toString(varValue));
          } else if (!((Collection<?>) varValue).isEmpty()) {
            path = ((Collection<?>) varValue).stream().map(Objects::toString).collect(Collectors.toList());
          }
          break;
        }
        case "icon": {
          icon = varValue + "";
          break;
        }
        case "position": {
          if (varValue instanceof Integer) {
            position = (Integer) varValue;
          } else if (varValue != null) {
            position = Integer.parseInt(varValue.toString());
          }
          break;
        }
        case "layers": {
          layersVariable = variable;
          for (Object layerMapObject : (Iterable<?>) varValue) {
            Map<Object, Object> layerMap = (Map<Object, Object>) layerMapObject;
            Layer layer = new Layer();
            layer.setId(layerMap.get("id") + "");
            layer.setTitle(layerMap.get("title") + "");
            if (layerMap.containsKey("active")) {
              layer.setActive((boolean) layerMap.get("active"));
            }
            layers.add(layer);
          }
          break;
        }
        case "patches": {
          if (varValue instanceof List) {
            patches = (List<Patch>) varValue;
          } else if (varValue instanceof Patch) {
            patches.add((Patch) varValue);
          } else if (varValue instanceof Collection) {
            patches.addAll((Collection<? extends Patch>) varValue);
          }
          break;
        }
      }

    }

    // If this is a custom view there may be an activeLayers variable in the
    // variables list
    Variable activeLayersVariable = instanceVariables.stream().filter(v -> v.getName().equals("activeLayers"))
        .findAny().orElse(null);
    if (activeLayersVariable != null) {
      Collection<?> activeLayers = (Collection<?>) activeLayersVariable.getValue();
      for (Layer layer : layers) {
        layer.setActive(activeLayers.contains(layer.getId()));
      }
    }

    // Replace layers variable from list of maps to list of Layer objects
    if (layersVariable != null) {
      instanceVariables.remove(layersVariable);
    }
    instanceVariables.add(Variable.createReadOnlyVariable("layers", layers));

    ViewTree vt = new ViewTree(instance, format, icon, position, patches, layers);
    rootViewTree.add(new ArrayList<>(path), vt);

    return vt;
  }

  @Override
  public void showElement(String id, String uri, IEditorPart editor) {
    // TODO Auto-generated method stub

  }

  @Override
  protected Picto getRenderingMetadata(IEditorPart editorPart) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Resource getResource(IEditorPart editorPart) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected IFile getFile(IEditorPart editorPart) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected boolean supportsEditorType(IEditorPart editorPart) {
    // TODO Auto-generated method stub
    return false;
  }

}
