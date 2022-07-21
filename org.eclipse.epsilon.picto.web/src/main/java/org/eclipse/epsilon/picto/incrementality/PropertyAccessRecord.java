package org.eclipse.epsilon.picto.incrementality;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;

public class PropertyAccessRecord {

	public enum AccessRecordStatus {
		NEW,
		PROCESSED
	}
	
	public static final String INITIAL_VALUE = "INITIAL_VALUE ";
	protected String modulePath;
	protected String generationRuleName;
	protected String contextResourceUri;
	protected String elementResourceUri;
	protected String contextObjectId;
	protected String elementObjectId;
	protected String propertyName;
	protected String oldValue = INITIAL_VALUE;
	protected String value = INITIAL_VALUE;
	protected String path;
	protected AccessRecordStatus status = AccessRecordStatus.NEW;

	public PropertyAccessRecord(String modulePath, String generationRuleName, String contextResourceUri,
			String contextObjectId, String elementResourceUri, String elementObjectId, String propertyName,
			Object value, String path) {
		super();
		this.modulePath = modulePath;
		this.generationRuleName = generationRuleName;
		this.contextResourceUri = contextResourceUri;
		this.contextObjectId = contextObjectId;
		this.elementResourceUri = elementResourceUri;
		this.elementObjectId = elementObjectId;
		this.propertyName = propertyName;
		setValue(value);
		this.path = path;
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

	public String getOldValue() {
		return oldValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(Object value) {
		String val = null;
		if (value instanceof EObject) {
			EObject eObject = (EObject) value;
			Resource eResource = eObject.eResource();
			val = ((XMIResource) eResource).getID(eObject);
			if (val == null)
				val = eResource.getURIFragment(eObject);
			val = val + ":" + eObject.eClass().getName();

		} else if (value instanceof EList<?>) {
			EList<?> eList = (EList<?>) value;
			if (eList.size() > 0) {
				Object o = eList.get(0);
				if (o instanceof EObject) {
					EObject eObject = (EObject) o;
					EStructuralFeature eFeature = eObject.eContainingFeature();
					String typeName = eObject.eClass().getName();
					List<String> vals = eList.stream().map(e -> {
						String id = ((XMIResource) ((EObject) e).eResource()).getID((EObject) e);
						if (id == null)
							if (id == null)
								id = ((EObject) e).eResource().getURIFragment((EObject) e);
						return id;
					}).collect(Collectors.toList());
					if (!eFeature.isOrdered()) {
						vals = vals.stream().sorted().collect(Collectors.toList());
					}
					val = "[" + String.join(",", vals) + "]:" + typeName;
				} else {
					val = o.toString() + ":" + o.getClass().getName();
				}
			} else {
				val = eList.toString() + ":";
			}
		} else if (value != null) {
			val = value.toString() + ":" + value.getClass().getName();
		}

		this.setValue(val);
	}

	public void setValue(String value) {
		String[] temp = new String[1];
		temp[0] = this.value;
		this.oldValue = temp[0];
		this.value = value;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public AccessRecordStatus getStatus() {
		return status;
	}

	public void setStatus(AccessRecordStatus status) {
		this.status = status;
	}

	public static String getInitialValue() {
		return INITIAL_VALUE;
	}

	@Override
	public String toString() {
		return status.name().toString() + ", " + modulePath + ", " + generationRuleName + ", " + contextResourceUri + ", " + contextObjectId + ", "
				+ elementResourceUri + ", " + elementObjectId + ", " + propertyName + ", " + oldValue + ", " + value
				+ ", " + path;
	}

}
