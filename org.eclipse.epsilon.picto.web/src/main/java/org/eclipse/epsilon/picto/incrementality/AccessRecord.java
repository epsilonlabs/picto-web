package org.eclipse.epsilon.picto.incrementality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.eol.execute.introspection.recording.PropertyAccess;

public class AccessRecord extends PropertyAccess {

	public static int counter = 0;
	public static final String INITIAL_VALUE = "INITIAL_VALUE ";
	protected int id;
	protected String modulePath;
	protected String templatePath;
	protected String generationRuleName;
	protected String contextResourceUri;
	protected String elementResourceUri;
	protected String contextObjectId;
	protected String elementObjectId;
	protected String propertyName;
	protected String value = INITIAL_VALUE;
	protected String path;
	protected AccessRecordState state = AccessRecordState.NEW;

	public AccessRecord(String modulePath, String templatePath, String generationRuleName,
			String contextResourceUri, String contextObjectId, String elementResourceUri, String elementObjectId,
			String propertyName, Object value, String path) {
		super(null, null);
		this.id = ++counter;
		if (counter == Integer.MAX_VALUE)
			counter = 0;
		this.modulePath = modulePath;
		this.templatePath = templatePath;
		this.generationRuleName = generationRuleName;
		this.contextResourceUri = contextResourceUri;
		this.contextObjectId = contextObjectId;
		this.elementResourceUri = elementResourceUri;
		this.elementObjectId = elementObjectId;
		this.propertyName = propertyName;
		this.path = path;
		setValue(value);
	}

	public AccessRecord(Object modelElement, String propertyName2, GenerationRule rule,
			Object contextElement) {
		super(null, null);
	}

	public int getId() {
		return id;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getContextResourceUri() {
		return contextResourceUri;
	}

	public void setContextResourceUri(String contextResourceUri) {
		this.contextResourceUri = contextResourceUri;
	}

	public String getElementResourceUri() {
		return elementResourceUri;
	}

	public void setElementResourceUri(String elementResourceUri) {
		this.elementResourceUri = elementResourceUri;
	}

	public String getModulePath() {
		return modulePath;
	}

	public void setModulePath(String modulePath) {
		this.modulePath = modulePath;
	}

	public String getGenerationRuleName() {
		return generationRuleName;
	}

	public void setGenerationRuleName(String generationRuleName) {
		this.generationRuleName = generationRuleName;
	}

	public String getContextObjectId() {
		return contextObjectId;
	}

	public void setContextObjectId(String contextObjectId) {
		this.contextObjectId = contextObjectId;
	}

	public String getElementObjectId() {
		return elementObjectId;
	}

	public void setElementObjectId(String elementObjectId) {
		this.elementObjectId = elementObjectId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getValue() {
		return value;
	}

	/***
	 * The value object will be converted to string in the internal implementation.
	 * So every value is stored as string.
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
		String val = convertValueToString(value);
		this.setValue(val);
	}

	public static String convertValueToString(Object value) {
		String val = null;
		if (value instanceof EObject) {
			EObject eObject = (EObject) value;
			Resource eResource = eObject.eResource();
			val = eResource.getURIFragment(eObject);
			val = val + "#" + eObject.eClass().getEPackage().getName() + "." + eObject.eClass().getName();

		} else if (value instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) value;
			if (collection.size() > 0) {
				Object o = collection.iterator().next();
				if (o instanceof EObject) {
					EObject eObject = (EObject) o;
					EStructuralFeature eFeature = eObject.eContainingFeature();
					String typeName = eFeature.getEType().getEPackage().getName() + "." + eFeature.getEType().getName();
					List<String> vals = collection.stream().map(e -> {
						String id = ((EObject) e).eResource().getURIFragment((EObject) e);
						String type = ((EObject) e).eClass().getName();
						return id + ":" + type;
					}).collect(Collectors.toList());
					if (!eFeature.isOrdered()) {
						vals = vals.stream().sorted().collect(Collectors.toList());
					}
					val = "[" + String.join(",", vals) + "]#" + typeName;
				} else {
					List<String> vals = collection.stream().map(e -> {
						String id = e.toString();
						String type = e.getClass().getName();
						return id + ":" + type;
					}).collect(Collectors.toList());
					val = "[" + String.join(",", vals) + "]#" + o.getClass().getName();
				}
			} else {
				val = collection.toString() + "#";
			}
		} else if (value != null) {
			val = value.toString() + "#" + value.getClass().getName();
		}

		return val;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public AccessRecordState getState() {
		return state;
	}

	public void setState(AccessRecordState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return id + ", " + state.name().toString() + ", " + modulePath + ", " + templatePath + ", " + generationRuleName
				+ ", " + contextResourceUri + ", " + contextObjectId + ", " + elementResourceUri + ", "
				+ elementObjectId + ", " + propertyName + ", " + value + ", " + path;
	}

}
