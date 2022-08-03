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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.common.util.UriUtil;
import org.eclipse.epsilon.egl.EglFileGeneratingTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfUtil;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.FrameStack;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.introspection.recording.IPropertyAccess;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.eol.types.EolAnyType;
import org.eclipse.epsilon.flexmi.FlexmiResourceFactory;
import org.eclipse.epsilon.picto.Layer;
import org.eclipse.epsilon.picto.LazyEgxModule;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRule;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.PictoView;
import org.eclipse.epsilon.picto.ResourceLoadingException;
import org.eclipse.epsilon.picto.StaticContentPromise;
import org.eclipse.epsilon.picto.ViewContent;
import org.eclipse.epsilon.picto.ViewTree;
import org.eclipse.epsilon.picto.dom.CustomView;
import org.eclipse.epsilon.picto.dom.Model;
import org.eclipse.epsilon.picto.dom.Parameter;
import org.eclipse.epsilon.picto.dom.Patch;
import org.eclipse.epsilon.picto.dom.Picto;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.eclipse.epsilon.picto.dummy.IEditorPart;
import org.eclipse.epsilon.picto.incrementality.GenerationRulePropertyAccess;
import org.eclipse.epsilon.picto.incrementality.IncrementalLazyEgxModule;
import org.eclipse.epsilon.picto.incrementality.IncrementalResource;
import org.eclipse.epsilon.picto.incrementality.PropertyAccessRecord;
import org.eclipse.epsilon.picto.incrementality.PropertyAccessRecord.AccessRecordState;
import org.eclipse.epsilon.picto.incrementality.PropertyAccessRecordTable;
import org.eclipse.epsilon.picto.incrementality.Util;
import org.eclipse.epsilon.picto.source.EglPictoSource;
import org.eclipse.epsilon.picto.transformers.CsvContentTransformer;
import org.eclipse.epsilon.picto.transformers.GraphvizContentTransformer;
import org.eclipse.epsilon.picto.transformers.HtmlContentTransformer;
import org.eclipse.epsilon.picto.transformers.MarkdownContentTransformer;
import org.eclipse.epsilon.picto.transformers.SvgContentTransformer;
import org.eclipse.epsilon.picto.transformers.TextContentTransformer;
import org.eclipse.epsilon.picto.transformers.ViewContentTransformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebEglPictoSource extends EglPictoSource {

	protected static final List<ViewContentTransformer> TRANSFORMERS = new ArrayList<>();
	public static final IncrementalResource INCREMENTAL_RESOURCE = new PropertyAccessRecordTable();
	public static boolean generateAll = true; // true for first running

	protected File modelFile;
	protected File pictoFile;
	protected List<EPackage> ePackages = new ArrayList<>();

	public WebEglPictoSource() throws Exception {
		TRANSFORMERS.add(new GraphvizContentTransformer());
		TRANSFORMERS.add(new SvgContentTransformer());
		TRANSFORMERS.add(new TextContentTransformer());
		TRANSFORMERS.add(new CsvContentTransformer());
		TRANSFORMERS.add(new HtmlContentTransformer());
		TRANSFORMERS.add(new MarkdownContentTransformer());
	}

	public File getModelFile() {
		return modelFile;
	}

	public void setModelFile(File modelFile) {
		this.modelFile = modelFile;
	}

	public File getPictoFile() {
		return pictoFile;
	}

	public void setPictoFile(File pictoFile) {
		this.pictoFile = pictoFile;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> transform(File modifiedFile) throws Exception {
		Map<String, String> modifiedViewContents = new HashMap<>();
		Resource resource = null;
		PictoView pictoView = new PictoView();
		ViewTree rootViewTree = new ViewTree();

		String filename = modifiedFile.getAbsolutePath()
				.replace(new File(PictoApplication.WORKSPACE).getAbsolutePath() + File.separator, "")
				.replace("\\", "/");

		try {

			if (modifiedFile.getAbsolutePath().endsWith(".model") || modifiedFile.getAbsolutePath().endsWith(".flexmi")
					|| modifiedFile.getAbsolutePath().endsWith(".xmi")) {
				this.modelFile = new File(modifiedFile.getAbsolutePath());
				this.pictoFile = new File(modifiedFile.getAbsolutePath() + ".picto");
				loadMetamodel(this.pictoFile.getName().replace(".model.picto", ""));
				resource = getResource(this.modelFile);
			} else if (modifiedFile.getAbsolutePath().endsWith(".egx")) {
				String modelFileName = modifiedFile.getName().replace(".egx", ".model");
				this.modelFile = new File(
						modifiedFile.getParentFile().getAbsolutePath() + File.separator + modelFileName);
				this.pictoFile = new File(
						modifiedFile.getParentFile().getAbsolutePath() + File.separator + modelFileName + ".picto");
				loadMetamodel(this.pictoFile.getName().replace(".model.picto", ""));
				resource = getResource(this.modelFile);
			} else if (modifiedFile.getAbsolutePath().endsWith(".model.picto")) {
				this.pictoFile = modifiedFile;
				this.modelFile = new File(modifiedFile.getAbsolutePath().replace(".model.picto", ".model"));
				loadMetamodel(this.pictoFile.getName().replace(".model.picto", ""));
				resource = getResource(this.modelFile);
			} else if (modifiedFile.getAbsolutePath().endsWith("-standalone.picto")) {
				this.pictoFile = modifiedFile;
				this.modelFile = modifiedFile;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ViewContentCache viewContentCache = FileViewContentCache.addPictoFile(this.pictoFile.getName());

		Picto renderingMetadata = this.getRenderingMetadata(this.pictoFile);

		if (renderingMetadata != null) {
			IEolModule module;
			IModel model = null;

			if (resource != null) {
				// synchronization prevents races when using multiple Picto views
				synchronized (resource) {
					model = new InMemoryEmfModel("M", resource);
					((InMemoryEmfModel) model).setExpand(true);
				}
			}
			// }

			if (renderingMetadata.getFormat() == null)
				renderingMetadata.setFormat("egx");

			if ("egx".equals(renderingMetadata.getFormat())) {
				module = new IncrementalLazyEgxModule(INCREMENTAL_RESOURCE);
			} else {
				module = new EglTemplateFactoryModuleAdapter(new EglFileGeneratingTemplateFactory());
			}

			IEolContext context = module.getContext();

			FrameStack fs = context.getFrameStack();
			for (Parameter customParameter : renderingMetadata.getParameters()) {
				fs.put(new Variable(customParameter.getName(), getValue(customParameter), EolAnyType.Instance));
			}

			URI transformationUri = null;

			if (renderingMetadata.getTransformation() != null) {
				transformationUri = UriUtil.resolve(renderingMetadata.getTransformation(), modelFile.toURI());
				module.parse(transformationUri);
			} else {
				module.parse("");
			}

			if (model != null)
				context.getModelRepository().addModel(model);

			for (Model pictoModel : renderingMetadata.getModels()) {
				model = loadModel(pictoModel, modelFile);
				if (model != null)
					models.add(model);
			}

			context.getModelRepository().addModels(models);

			if ("egx".equals(renderingMetadata.getFormat())) {

				Set<String> toBeProcessedPaths = new HashSet<>();

				/** PROPERTY ACCESS RECORDS **/
				((IncrementalLazyEgxModule) module).startRecording();
				List<LazyGenerationRuleContentPromise> promises = (List<LazyGenerationRuleContentPromise>) module
						.execute();
				((IncrementalLazyEgxModule) module).stopRecording();
				WebEglPictoSource.updateIncrementalResource(module, null);
				INCREMENTAL_RESOURCE.printIncrementalRecords();

				/**
				 * the handleDynamicViews will add the generated lazy contents to instances to
				 * handled later in the next loop
				 **/
				handleDynamicViews(renderingMetadata, module, context, fs, promises);

//				INCREMENTAL_RESOURCE.printIncrementalRecords();

				/** Filter promises that only need regeneration **/
				List<LazyGenerationRuleContentPromise> inProcessingPromises = promises;
//				List<LazyGenerationRuleContentPromise> inProcessingPromises = getToBeProcPromises(promises,
//						generateAll);

				/** loop through the content promises of rules **/
				System.out.println("\nGENERATING VIEWS: ");
				toBeProcessedPaths
						.addAll(INCREMENTAL_RESOURCE.getToBeProcessedPaths(inProcessingPromises, (EgxModule) module));

				for (LazyGenerationRuleContentPromise inProcessingPromise : inProcessingPromises) {

					String pathString = Util.getPath(inProcessingPromise);
					System.out.print("Processing " + pathString + " ... ");
					if (pathString.equals("/Stats") || pathString.equals("/Custom/Alice and Bob")) {
//						INCREMENTAL_RESOURCE.getIncrementalRecords().clear();
						System.console();
					}

					ViewTree vt = this.generateViewTree(rootViewTree, inProcessingPromise);

					// Check if the path should be processed to generated new view
					if (!toBeProcessedPaths.contains(pathString)) {
						System.out.println("SKIP");
						continue;
					}

					((IncrementalLazyEgxModule) module).startRecording();
					ViewContent vc = vt.getContent();
					((IncrementalLazyEgxModule) module).stopRecording();
					WebEglPictoSource.updateIncrementalResource(module, pathString);
					System.console();
					if (pathString.equals("/Stats") || pathString.equals("/Custom/Alice and Bob")) {
//						INCREMENTAL_RESOURCE.getIncrementalRecords().clear();
						System.console();
					}

					/** transform to target format (e.g., svg, html) **/
					for (ViewContentTransformer transformer : TRANSFORMERS) {
						if (transformer.canTransform(vc)) {
							vc = transformer.transform(vc, pictoView);
							vt.setContent(vc);
							break;
						}
					}

					PictoResponse pictoResponse = new PictoResponse();
					pictoResponse.setFilename(filename);
					pictoResponse.setPath(pathString);
					pictoResponse.setType(vc.getFormat());
					pictoResponse.setContent(vc.getText());

					String jsonString = new ObjectMapper().writerWithDefaultPrettyPrinter()
							.writeValueAsString(pictoResponse);
					/** put the generated Picto response's json string to cache **/
//					if (!jsonString.equals(viewContentCache.getViewContentCache(pathString))) {
					viewContentCache.putViewContentCache(pathString, jsonString);
					modifiedViewContents.put(pathString, jsonString);
//					}
					System.out.println("PROCESSED");

				}
				INCREMENTAL_RESOURCE.printIncrementalRecords();
				INCREMENTAL_RESOURCE.updateStatusToProcessed(toBeProcessedPaths);
				generateAll = false;
				System.out.println();
				System.console();
			} else {
				String content = module.execute() + "";
				rootViewTree = new ViewTree();
				rootViewTree.setPromise(new StaticContentPromise(content));
				rootViewTree.setFormat(renderingMetadata.getFormat());
			}

			// Handle static views (i.e. where source != null), add the custom view loaded from a file
			// defined in the picto file
			handleStaticViews(modifiedViewContents, rootViewTree, filename, viewContentCache, renderingMetadata,
					module);

			// Handle patches for existing views (i.e. where source == null and type/rule ==
			// null)
			handlePatchesForExistingViews(rootViewTree, renderingMetadata);

			// set URIs of rootViewTree
			if (transformationUri != null) {
				rootViewTree.getBaseUris().add(transformationUri);
				rootViewTree.getBaseUris().add(transformationUri.resolve("./icons/"));
			}
			rootViewTree.getBaseUris().add(new URI(modelFile.toURI().toString()));

			// generate JSON of the JsTree library (the tree panel on the client-side web
			// browser)
			String jsTreeJson = generateJsTreeData(filename, rootViewTree);
			if (!jsTreeJson.equals(viewContentCache.getViewContentCache(FileViewContentCache.PICTO_TREE))) {
				viewContentCache.putViewContentCache(FileViewContentCache.PICTO_TREE, jsTreeJson);
				modifiedViewContents.put(FileViewContentCache.PICTO_TREE, jsTreeJson);
			}

		} else {
			rootViewTree = createEmptyViewTree();
			String jsTreeJson = generateJsTreeData(filename, rootViewTree);
			if (!jsTreeJson.equals(viewContentCache.getViewContentCache(FileViewContentCache.PICTO_TREE))) {
				viewContentCache.putViewContentCache(FileViewContentCache.PICTO_TREE, jsTreeJson);
				modifiedViewContents.put(FileViewContentCache.PICTO_TREE, jsTreeJson);
			}
		}

		return modifiedViewContents;

	}

	/***
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

	private void handleStaticViews(Map<String, String> modifiedViewContents, ViewTree rootViewTree, String filename,
			ViewContentCache viewContentCache, Picto renderingMetadata, IEolModule module)
			throws EolRuntimeException, JsonProcessingException {
		for (CustomView customView : renderingMetadata.getCustomViews().stream().filter(cv -> cv.getSource() != null)
				.collect(Collectors.toList())) {
			String format = customView.getFormat() != null ? customView.getFormat() : getDefaultFormat();
			String icon = customView.getIcon() != null ? customView.getIcon() : getDefaultIcon();

			rootViewTree.add(customView.getPath(),
					new ViewTree(
							new StaticContentPromise(
									new File(new File(customView.eResource().getURI().toFileString()).getParentFile(),
											customView.getSource())),
							format, icon, customView.getPosition(), customView.getPatches(), Collections.emptyList()));

			// put the content into the elementViewContentMap
			String pathString = "/" + String.join("/", customView.getPath());
			ViewTree vt = rootViewTree.getChildren().get(rootViewTree.getChildren().size() - 1);

//			// record property accesses
//			((IncrementalLazyEgxModule) module).startRecording();
			ViewContent vc = vt.getContent();
//			((IncrementalLazyEgxModule) module).stopRecording();
//			WebEglPictoSource.updateIncrementalResource(module, pathString);

			// set picto response
			PictoResponse pictoResponse = new PictoResponse();
			pictoResponse.setFilename(filename);
			pictoResponse.setType(vc.getFormat());
			pictoResponse.setContent(vc.getText());

			pictoResponse.setPath(pathString);
			String jsonString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(pictoResponse);

			if (!jsonString.equals(viewContentCache.getViewContentCache(pathString))) {
				viewContentCache.putViewContentCache(pathString, jsonString);
				modifiedViewContents.put(pathString, jsonString);
			}
		}
	}

	private void handleDynamicViews(Picto renderingMetadata, IEolModule module, IEolContext context, FrameStack fs,
			List<LazyGenerationRuleContentPromise> instances) throws EolRuntimeException {
		List<CustomView> customViews = renderingMetadata.getCustomViews().stream().filter(cv -> cv.getType() != null)
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
				LazyGenerationRuleContentPromise contentPromise = (LazyGenerationRuleContentPromise) generationRule
						.execute(context, source);
				((IncrementalLazyEgxModule) module).stopRecording();
				WebEglPictoSource.updateIncrementalResource(module, "/" + String.join("/", customView.getPath()));

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
				instances.add(contentPromise);
			}
		}
	}

	protected String generateJsTreeData(String filename, ViewTree viewTree) throws JsonProcessingException {
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

		return (new ObjectMapper()).writerWithDefaultPrettyPrinter().writeValueAsString(pictoResponse);

	}

	protected void copyViewTreeToJsTreeData(String filename, ViewTree viewTree, JsTreeNode jsTreeNode) {
		for (ViewTree child : viewTree.getChildren()) {
			JsTreeNode jsonChild = new JsTreeNode();
			jsTreeNode.getChildren().add(jsonChild);
			copyViewTreeToJsTreeData(filename, child, jsonChild);
		}
		String text = viewTree.getName();
		jsTreeNode.setText(text);
		jsTreeNode.setPath(viewTree.getPathString());
	}

	protected JsonViewTree generateJsonViewTree(ViewTree viewTree) {
		JsonViewTree root = new JsonViewTree();
		copyViewTreeToJsonViewTree(viewTree, root);
		return root;
	}

	protected void copyViewTreeToJsonViewTree(ViewTree viewTree, JsonViewTree jsonViewTree) {
		for (ViewTree child : viewTree.getChildren()) {
			JsonViewTree jsonChild = new JsonViewTree();
			jsonViewTree.getChildren().add(jsonChild);
			copyViewTreeToJsonViewTree(child, jsonChild);
		}
		JsonViewContent jvc = new JsonViewContent();
		jvc.setFormat(viewTree.getContent().getFormat());
		jvc.setText(viewTree.getContent().getText());
		jvc.setLabel(viewTree.getContent().getLabel());
		jsonViewTree.setContent(jvc);
		jsonViewTree.setName(viewTree.getName());
		jsonViewTree.setIcon(viewTree.getIcon());
		jsonViewTree.setPosition(viewTree.getPosition());
		jsonViewTree.setUri(viewTree.getPathString());
	}

	protected IModel loadModel(Model model, File baseFile) throws Exception {
		if ("EMF".equals(model.getType())) {
			String metamodelName = (String) model.getParameters().stream()
					.filter(p -> EmfModel.PROPERTY_METAMODEL_URI.equals(p.getName())).findFirst().orElse(null)
					.getValue();
			loadMetamodel(metamodelName);
		}
		IRelativePathResolver relativePathResolver = relativePath -> new File(baseFile.getParentFile(), relativePath)
				.getAbsolutePath();
		String filePath = null;
		for (Parameter parameter : model.getParameters()) {
			if (parameter.getFile() != null) {
				filePath = relativePathResolver.resolve(parameter.getFile());
			}
		}
		IModel model2 = new InMemoryEmfModel("M", getResource(new File(filePath)));
		((InMemoryEmfModel) model2).setExpand(true);
		return model2;

	}

	protected IModel loadModel(Model model, String code) throws Exception {
		IModel m = new EmfModel();
		m.setName(model.getName());
		m.setReadOnLoad(true);
		m.setStoredOnDisposal(false);
		StringProperties properties = new StringProperties();
		m.load(properties, code);
		return m;
	}

	public List<EPackage> loadMetamodel(String metamodelName) throws Exception {
		if (!ePackages.stream().anyMatch(p -> p.getName().equals(metamodelName))) {
			File metamodelFile = new File(
					this.pictoFile.getParentFile().getAbsolutePath() + File.separator + metamodelName + ".ecore");
			if (metamodelFile.exists()) {
				org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI
						.createFileURI(metamodelFile.getAbsolutePath());
				List<EPackage> ePackage = EmfUtil.register(uri, EPackage.Registry.INSTANCE);
				ePackages.addAll(ePackage);
			}
		}
		return ePackages;
	}

	public Resource getResource(File modelFile) {
		ResourceSet resourceSet = new ResourceSetImpl();

		if (modelFile.getName().endsWith(".ecore")) {
			resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		}

		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
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

	public Picto getRenderingMetadata(File file) {
		try {
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new FlexmiResourceFactory());
			Resource resource = resourceSet
					.getResource(org.eclipse.emf.common.util.URI.createFileURI(file.getAbsolutePath()), true);
			resource.load(null);
			return (Picto) resource.getContents().iterator().next();
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public Picto getRenderingMetadata(IEditorPart editorPart) {
		return this.getRenderingMetadata(this.pictoFile);
	}

	@Override
	public void showElement(String id, String uri, IEditorPart editor) {
		// TODO Auto-generated method stub

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

	/***
	 * Update the state incremental resource with the recorded protected accesses.
	 * 
	 * @param module
	 * @param pathString
	 * @throws EolRuntimeException
	 */
	@SuppressWarnings("unchecked")
	public static void updateIncrementalResource(IEolModule module, String pathString) throws EolRuntimeException {
		for (IPropertyAccess propertyAccess : ((IncrementalLazyEgxModule) module).getPropertyAccessRecorder()
				.getPropertyAccesses().all()) {
			GenerationRulePropertyAccess generationRulePropertyAccess = (GenerationRulePropertyAccess) propertyAccess;

			String ruleName = (generationRulePropertyAccess.getRule() == null) ? null
					: generationRulePropertyAccess.getRule().getName();

			EObject contextElement = (EObject) generationRulePropertyAccess.getContextElement();
			Resource contextResource = null;
			String contextResourceUri = null;
			String contextElementId = null;
			if (contextElement != null) {
				contextResource = contextElement.eResource();
				contextResourceUri = contextResource.getURI().toFileString();
				contextElementId = contextResource.getURIFragment(contextElement);
			}

			EObject modelElement = (EObject) generationRulePropertyAccess.getModelElement();
			Resource elementResource = modelElement.eResource();
			String elementResourceUri = modelElement.eResource().getURI().toFileString();
			String modelElementId = elementResource.getURIFragment(modelElement);

			EStructuralFeature property = modelElement.eClass()
					.getEStructuralFeature(generationRulePropertyAccess.getPropertyName());
			String propertyName = (property != null) ? property.getName()
					: generationRulePropertyAccess.getPropertyName();
			Object value = (property != null) ? modelElement.eGet(property) : null;

			String path = null;
			if (pathString == null) {
				GenerationRule rule = generationRulePropertyAccess.getRule();
				if (rule.getName().equals("Persons2Table")) {
					System.console();
				}

				Object result = rule.execute(module.getContext());
				Collection<LazyGenerationRuleContentPromise> list = (result instanceof Collection<?>)
						? (Collection<LazyGenerationRuleContentPromise>) result
						: Arrays.asList(
								new LazyGenerationRuleContentPromise[] { (LazyGenerationRuleContentPromise) result });
//				if (list.size() == 1) {
				for (LazyGenerationRuleContentPromise e : list) {
					path = Util.getPath((LazyGenerationRuleContentPromise) e);

					Variable var = e.getVariables().iterator().next();
					Object obj = var.getValue();
					if (obj instanceof EObject) {
						if (modelElement.equals(obj)) {

							PropertyAccessRecord record = new PropertyAccessRecord(module.getFile().getAbsolutePath(),
									ruleName, contextResourceUri, contextElementId, elementResourceUri, modelElementId,
									propertyName, value, path);

//						System.out.println("added: " + record);
							INCREMENTAL_RESOURCE.add(record);
						}
					} else {
						PropertyAccessRecord record = new PropertyAccessRecord(module.getFile().getAbsolutePath(),
								ruleName, contextResourceUri, contextElementId, elementResourceUri, modelElementId,
								propertyName, value, path);

//					System.out.println("added: " + record);
						INCREMENTAL_RESOURCE.add(record);
					}
				}
//				}
			} else {
				path = pathString;
				PropertyAccessRecord record = new PropertyAccessRecord(module.getFile().getAbsolutePath(), ruleName,
						contextResourceUri, contextElementId, elementResourceUri, modelElementId, propertyName, value,
						pathString);
//				System.out.println("added: " + record);
				INCREMENTAL_RESOURCE.add(record);
			}
		}
//		((IncrementalLazyEgxModule) module).getPropertyAccessRecorder().getPropertyAccesses().clear();
		System.console();
	}

	@SuppressWarnings("unchecked")
	public ViewTree generateViewTree(ViewTree rootViewTree, LazyGenerationRuleContentPromise instance) {
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

}
