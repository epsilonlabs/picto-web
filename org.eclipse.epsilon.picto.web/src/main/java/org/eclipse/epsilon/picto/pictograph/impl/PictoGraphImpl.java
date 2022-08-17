/**
 */
package org.eclipse.epsilon.picto.pictograph.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.epsilon.picto.pictograph.Entity;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.PictoGraph;
import org.eclipse.epsilon.picto.pictograph.PictographPackage;
import org.eclipse.epsilon.picto.pictograph.Resource;
import org.eclipse.epsilon.picto.pictograph.Template;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Picto Graph</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl#getPaths <em>Paths</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl#getModules <em>Modules</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl#getRules <em>Rules</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl#getTemplates <em>Templates</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl#getResources <em>Resources</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl#getElements <em>Elements</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.PictoGraphImpl#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PictoGraphImpl extends EObjectImpl implements PictoGraph {
	/**
	 * The cached value of the '{@link #getPaths() <em>Paths</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaths()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Entity> paths;
	/**
	 * The cached value of the '{@link #getModules() <em>Modules</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModules()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Entity> modules;
	/**
	 * The cached value of the '{@link #getRules() <em>Rules</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRules()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Entity> rules;
	/**
	 * The cached value of the '{@link #getTemplates() <em>Templates</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemplates()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Entity> templates;
	/**
	 * The cached value of the '{@link #getResources() <em>Resources</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResources()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Entity> resources;
	/**
	 * The cached value of the '{@link #getElements() <em>Elements</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElements()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Entity> elements;
	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Entity> properties;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PictoGraphImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PictographPackage.Literals.PICTO_GRAPH;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, Entity> getPaths() {
		if (paths == null) {
			paths = new EcoreEMap<String,Entity>(PictographPackage.Literals.ENTITY_MAP, EntityMapImpl.class, this, PictographPackage.PICTO_GRAPH__PATHS);
		}
		return paths;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, Entity> getModules() {
		if (modules == null) {
			modules = new EcoreEMap<String,Entity>(PictographPackage.Literals.ENTITY_MAP, EntityMapImpl.class, this, PictographPackage.PICTO_GRAPH__MODULES);
		}
		return modules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, Entity> getRules() {
		if (rules == null) {
			rules = new EcoreEMap<String,Entity>(PictographPackage.Literals.ENTITY_MAP, EntityMapImpl.class, this, PictographPackage.PICTO_GRAPH__RULES);
		}
		return rules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, Entity> getTemplates() {
		if (templates == null) {
			templates = new EcoreEMap<String,Entity>(PictographPackage.Literals.ENTITY_MAP, EntityMapImpl.class, this, PictographPackage.PICTO_GRAPH__TEMPLATES);
		}
		return templates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, Entity> getResources() {
		if (resources == null) {
			resources = new EcoreEMap<String,Entity>(PictographPackage.Literals.ENTITY_MAP, EntityMapImpl.class, this, PictographPackage.PICTO_GRAPH__RESOURCES);
		}
		return resources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, Entity> getElements() {
		if (elements == null) {
			elements = new EcoreEMap<String,Entity>(PictographPackage.Literals.ENTITY_MAP, EntityMapImpl.class, this, PictographPackage.PICTO_GRAPH__ELEMENTS);
		}
		return elements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, Entity> getProperties() {
		if (properties == null) {
			properties = new EcoreEMap<String,Entity>(PictographPackage.Literals.ENTITY_MAP, EntityMapImpl.class, this, PictographPackage.PICTO_GRAPH__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PictographPackage.PICTO_GRAPH__PATHS:
				return ((InternalEList<?>)getPaths()).basicRemove(otherEnd, msgs);
			case PictographPackage.PICTO_GRAPH__MODULES:
				return ((InternalEList<?>)getModules()).basicRemove(otherEnd, msgs);
			case PictographPackage.PICTO_GRAPH__RULES:
				return ((InternalEList<?>)getRules()).basicRemove(otherEnd, msgs);
			case PictographPackage.PICTO_GRAPH__TEMPLATES:
				return ((InternalEList<?>)getTemplates()).basicRemove(otherEnd, msgs);
			case PictographPackage.PICTO_GRAPH__RESOURCES:
				return ((InternalEList<?>)getResources()).basicRemove(otherEnd, msgs);
			case PictographPackage.PICTO_GRAPH__ELEMENTS:
				return ((InternalEList<?>)getElements()).basicRemove(otherEnd, msgs);
			case PictographPackage.PICTO_GRAPH__PROPERTIES:
				return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PictographPackage.PICTO_GRAPH__PATHS:
				if (coreType) return getPaths();
				else return getPaths().map();
			case PictographPackage.PICTO_GRAPH__MODULES:
				if (coreType) return getModules();
				else return getModules().map();
			case PictographPackage.PICTO_GRAPH__RULES:
				if (coreType) return getRules();
				else return getRules().map();
			case PictographPackage.PICTO_GRAPH__TEMPLATES:
				if (coreType) return getTemplates();
				else return getTemplates().map();
			case PictographPackage.PICTO_GRAPH__RESOURCES:
				if (coreType) return getResources();
				else return getResources().map();
			case PictographPackage.PICTO_GRAPH__ELEMENTS:
				if (coreType) return getElements();
				else return getElements().map();
			case PictographPackage.PICTO_GRAPH__PROPERTIES:
				if (coreType) return getProperties();
				else return getProperties().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PictographPackage.PICTO_GRAPH__PATHS:
				((EStructuralFeature.Setting)getPaths()).set(newValue);
				return;
			case PictographPackage.PICTO_GRAPH__MODULES:
				((EStructuralFeature.Setting)getModules()).set(newValue);
				return;
			case PictographPackage.PICTO_GRAPH__RULES:
				((EStructuralFeature.Setting)getRules()).set(newValue);
				return;
			case PictographPackage.PICTO_GRAPH__TEMPLATES:
				((EStructuralFeature.Setting)getTemplates()).set(newValue);
				return;
			case PictographPackage.PICTO_GRAPH__RESOURCES:
				((EStructuralFeature.Setting)getResources()).set(newValue);
				return;
			case PictographPackage.PICTO_GRAPH__ELEMENTS:
				((EStructuralFeature.Setting)getElements()).set(newValue);
				return;
			case PictographPackage.PICTO_GRAPH__PROPERTIES:
				((EStructuralFeature.Setting)getProperties()).set(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PictographPackage.PICTO_GRAPH__PATHS:
				getPaths().clear();
				return;
			case PictographPackage.PICTO_GRAPH__MODULES:
				getModules().clear();
				return;
			case PictographPackage.PICTO_GRAPH__RULES:
				getRules().clear();
				return;
			case PictographPackage.PICTO_GRAPH__TEMPLATES:
				getTemplates().clear();
				return;
			case PictographPackage.PICTO_GRAPH__RESOURCES:
				getResources().clear();
				return;
			case PictographPackage.PICTO_GRAPH__ELEMENTS:
				getElements().clear();
				return;
			case PictographPackage.PICTO_GRAPH__PROPERTIES:
				getProperties().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PictographPackage.PICTO_GRAPH__PATHS:
				return paths != null && !paths.isEmpty();
			case PictographPackage.PICTO_GRAPH__MODULES:
				return modules != null && !modules.isEmpty();
			case PictographPackage.PICTO_GRAPH__RULES:
				return rules != null && !rules.isEmpty();
			case PictographPackage.PICTO_GRAPH__TEMPLATES:
				return templates != null && !templates.isEmpty();
			case PictographPackage.PICTO_GRAPH__RESOURCES:
				return resources != null && !resources.isEmpty();
			case PictographPackage.PICTO_GRAPH__ELEMENTS:
				return elements != null && !elements.isEmpty();
			case PictographPackage.PICTO_GRAPH__PROPERTIES:
				return properties != null && !properties.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //PictoGraphImpl
