package org.eclipse.epsilon.picto.incrementality;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccessRecorder;
import org.eclipse.epsilon.picto.web.PictoWeb;

public class GenerationRulePropertyAccessRecorder extends PropertyAccessRecorder {

	private boolean recording = false;

	private final List<GenerationRulePropertyAccess> currentPropertyAccesses = new ArrayList<>();
	private AccessRecordResource accessRecordResource = PictoWeb.ACCESS_RECORD_RESOURCE;

	protected URI templateUri = null;
	protected GenerationRule rule = null;
	protected Object contextElement = null;
	protected String path = null;

	public GenerationRulePropertyAccessRecorder() {
		super();
	}

	public List<GenerationRulePropertyAccess> getCurrentPropertyAccesses() {
		return currentPropertyAccesses;
	}

	public void updateCurrentPropertyAccessesPath(String path) {
		currentPropertyAccesses.stream().forEach(r -> r.setPath(path));
	}
	
	public void saveToAccessRecordResource() {
		for (GenerationRulePropertyAccess r : currentPropertyAccesses) {
//			if (r.getPath() == null) continue;
			accessRecordResource.add(r);
		}
		currentPropertyAccesses.clear();
	}

	public Object getContextElement() {
		return contextElement;
	}

	public void setContextElement(Object contextElement) {
		this.contextElement = contextElement;
	}

	public void setRule(GenerationRule rule) {
		this.rule = rule;
	}

	public GenerationRule getRule() {
		return rule;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public URI getTemplateUri() {
		return templateUri;
	}

	public void setTemplateUri(URI templateUri) {
		this.templateUri = templateUri;
	}

	@Override
	protected GenerationRulePropertyAccess createPropertyAccess(Object modelElement, String propertyName) {
		return this.createPropertyAccess(modelElement, propertyName, null);
	}

	private GenerationRulePropertyAccess createPropertyAccess(Object modelElement, String propertyName, Object result) {

		String ruleName = (rule == null) ? null : rule.getName();
		EgxModule module = (rule == null) ? null : (EgxModule) rule.getModule();
		String modulePath = (module == null) ? null : module.getFile().getAbsolutePath();
		String templatePath = (templateUri == null) ? null : (new File(templateUri)).getAbsolutePath();

		EObject contextElement = (EObject) this.contextElement;
		Resource contextResource = null;
		String contextResourceUri = null;
		String contextElementId = null;
		if (contextElement != null) {
			contextResource = contextElement.eResource();
			contextResourceUri = contextResource.getURI().toFileString();
			contextElementId = contextResource.getURIFragment(contextElement);
		}

		Object element = null;
		Resource elementResource = null;
		String elementResourceUri = null;
		String modelElementId = null;
		if (modelElement instanceof EObject) {
			element = (EObject) modelElement;
			elementResource = ((EObject) element).eResource();
			elementResourceUri = ((EObject) element).eResource().getURI().toFileString();
			modelElementId = elementResource.getURIFragment((EObject) element);

		} else if (modelElement instanceof NameExpression) {
			modelElementId = ((NameExpression) modelElement).getName();
		}

		GenerationRulePropertyAccess access = null;
		try {
			access = new GenerationRulePropertyAccess(modulePath, templatePath, ruleName, contextResourceUri,
					contextElementId, elementResourceUri, modelElementId, propertyName, (Object) result, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return access;
	}

	@Override
	public void startRecording() {
		recording = true;
	}

	@Override
	public void stopRecording() {
		recording = false;
	}

	@Override
	public void record(Object modelElement, String propertyName) {
		if (recording) {
			currentPropertyAccesses.add(createPropertyAccess(modelElement, propertyName));
		}
	}

	public void record(Object modelElement, String propertyName, Object result) {
		if (recording) {
			currentPropertyAccesses.add(createPropertyAccess(modelElement, propertyName, result));
		}
	}

}