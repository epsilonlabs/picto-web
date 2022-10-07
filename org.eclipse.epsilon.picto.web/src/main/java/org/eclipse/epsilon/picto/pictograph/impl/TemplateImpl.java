/**
 */
package org.eclipse.epsilon.picto.pictograph.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.epsilon.picto.pictograph.Element;
import org.eclipse.epsilon.picto.pictograph.PictographPackage;
import org.eclipse.epsilon.picto.pictograph.Rule;
import org.eclipse.epsilon.picto.pictograph.Template;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Template</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.TemplateImpl#getModules <em>Modules</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.TemplateImpl#getRules <em>Rules</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.TemplateImpl#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TemplateImpl extends InputEntityImpl implements Template {
	/**
	 * The cached value of the '{@link #getModules() <em>Modules</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModules()
	 * @generated
	 * @ordered
	 */
	protected EList<org.eclipse.epsilon.picto.pictograph.Module> modules;

	/**
	 * The cached value of the '{@link #getRules() <em>Rules</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRules()
	 * @generated
	 * @ordered
	 */
	protected EList<Rule> rules;

	/**
	 * The cached value of the '{@link #getElements() <em>Elements</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElements()
	 * @generated
	 * @ordered
	 */
	protected EList<Element> elements;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TemplateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PictographPackage.Literals.TEMPLATE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<org.eclipse.epsilon.picto.pictograph.Module> getModules() {
		if (modules == null) {
			modules = new EObjectResolvingEList<org.eclipse.epsilon.picto.pictograph.Module>(org.eclipse.epsilon.picto.pictograph.Module.class, this, PictographPackage.TEMPLATE__MODULES);
		}
		return modules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Rule> getRules() {
		if (rules == null) {
			rules = new EObjectResolvingEList<Rule>(Rule.class, this, PictographPackage.TEMPLATE__RULES);
		}
		return rules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Element> getElements() {
		if (elements == null) {
			elements = new EObjectResolvingEList<Element>(Element.class, this, PictographPackage.TEMPLATE__ELEMENTS);
		}
		return elements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PictographPackage.TEMPLATE__MODULES:
				return getModules();
			case PictographPackage.TEMPLATE__RULES:
				return getRules();
			case PictographPackage.TEMPLATE__ELEMENTS:
				return getElements();
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
			case PictographPackage.TEMPLATE__MODULES:
				getModules().clear();
				getModules().addAll((Collection<? extends org.eclipse.epsilon.picto.pictograph.Module>)newValue);
				return;
			case PictographPackage.TEMPLATE__RULES:
				getRules().clear();
				getRules().addAll((Collection<? extends Rule>)newValue);
				return;
			case PictographPackage.TEMPLATE__ELEMENTS:
				getElements().clear();
				getElements().addAll((Collection<? extends Element>)newValue);
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
			case PictographPackage.TEMPLATE__MODULES:
				getModules().clear();
				return;
			case PictographPackage.TEMPLATE__RULES:
				getRules().clear();
				return;
			case PictographPackage.TEMPLATE__ELEMENTS:
				getElements().clear();
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
			case PictographPackage.TEMPLATE__MODULES:
				return modules != null && !modules.isEmpty();
			case PictographPackage.TEMPLATE__RULES:
				return rules != null && !rules.isEmpty();
			case PictographPackage.TEMPLATE__ELEMENTS:
				return elements != null && !elements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TemplateImpl
