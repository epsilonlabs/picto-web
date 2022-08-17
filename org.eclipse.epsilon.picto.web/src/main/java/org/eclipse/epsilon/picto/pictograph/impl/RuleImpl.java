/**
 */
package org.eclipse.epsilon.picto.pictograph.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.picto.pictograph.Element;
import org.eclipse.epsilon.picto.pictograph.PictographPackage;
import org.eclipse.epsilon.picto.pictograph.Rule;
import org.eclipse.epsilon.picto.pictograph.Template;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.RuleImpl#getModule <em>Module</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.RuleImpl#getTemplate <em>Template</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.RuleImpl#getContextElements <em>Context Elements</em>}</li>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.RuleImpl#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RuleImpl extends InputEntityImpl implements Rule {
	/**
	 * The cached value of the '{@link #getTemplate() <em>Template</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemplate()
	 * @generated
	 * @ordered
	 */
	protected Template template;

	/**
	 * The cached value of the '{@link #getContextElements() <em>Context Elements</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextElements()
	 * @generated
	 * @ordered
	 */
	protected EList<Element> contextElements;

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
	protected RuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PictographPackage.Literals.RULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public org.eclipse.epsilon.picto.pictograph.Module getModule() {
		if (eContainerFeatureID() != PictographPackage.RULE__MODULE) return null;
		return (org.eclipse.epsilon.picto.pictograph.Module)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModule(org.eclipse.epsilon.picto.pictograph.Module newModule, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newModule, PictographPackage.RULE__MODULE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setModule(org.eclipse.epsilon.picto.pictograph.Module newModule) {
		if (newModule != eInternalContainer() || (eContainerFeatureID() != PictographPackage.RULE__MODULE && newModule != null)) {
			if (EcoreUtil.isAncestor(this, newModule))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newModule != null)
				msgs = ((InternalEObject)newModule).eInverseAdd(this, PictographPackage.MODULE__RULES, org.eclipse.epsilon.picto.pictograph.Module.class, msgs);
			msgs = basicSetModule(newModule, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.RULE__MODULE, newModule, newModule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Template getTemplate() {
		if (template != null && template.eIsProxy()) {
			InternalEObject oldTemplate = (InternalEObject)template;
			template = (Template)eResolveProxy(oldTemplate);
			if (template != oldTemplate) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PictographPackage.RULE__TEMPLATE, oldTemplate, template));
			}
		}
		return template;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Template basicGetTemplate() {
		return template;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTemplate(Template newTemplate) {
		Template oldTemplate = template;
		template = newTemplate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PictographPackage.RULE__TEMPLATE, oldTemplate, template));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Element> getContextElements() {
		if (contextElements == null) {
			contextElements = new EObjectResolvingEList<Element>(Element.class, this, PictographPackage.RULE__CONTEXT_ELEMENTS);
		}
		return contextElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Element> getElements() {
		if (elements == null) {
			elements = new EObjectResolvingEList<Element>(Element.class, this, PictographPackage.RULE__ELEMENTS);
		}
		return elements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PictographPackage.RULE__MODULE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetModule((org.eclipse.epsilon.picto.pictograph.Module)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PictographPackage.RULE__MODULE:
				return basicSetModule(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case PictographPackage.RULE__MODULE:
				return eInternalContainer().eInverseRemove(this, PictographPackage.MODULE__RULES, org.eclipse.epsilon.picto.pictograph.Module.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PictographPackage.RULE__MODULE:
				return getModule();
			case PictographPackage.RULE__TEMPLATE:
				if (resolve) return getTemplate();
				return basicGetTemplate();
			case PictographPackage.RULE__CONTEXT_ELEMENTS:
				return getContextElements();
			case PictographPackage.RULE__ELEMENTS:
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
			case PictographPackage.RULE__MODULE:
				setModule((org.eclipse.epsilon.picto.pictograph.Module)newValue);
				return;
			case PictographPackage.RULE__TEMPLATE:
				setTemplate((Template)newValue);
				return;
			case PictographPackage.RULE__CONTEXT_ELEMENTS:
				getContextElements().clear();
				getContextElements().addAll((Collection<? extends Element>)newValue);
				return;
			case PictographPackage.RULE__ELEMENTS:
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
			case PictographPackage.RULE__MODULE:
				setModule((org.eclipse.epsilon.picto.pictograph.Module)null);
				return;
			case PictographPackage.RULE__TEMPLATE:
				setTemplate((Template)null);
				return;
			case PictographPackage.RULE__CONTEXT_ELEMENTS:
				getContextElements().clear();
				return;
			case PictographPackage.RULE__ELEMENTS:
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
			case PictographPackage.RULE__MODULE:
				return getModule() != null;
			case PictographPackage.RULE__TEMPLATE:
				return template != null;
			case PictographPackage.RULE__CONTEXT_ELEMENTS:
				return contextElements != null && !contextElements.isEmpty();
			case PictographPackage.RULE__ELEMENTS:
				return elements != null && !elements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //RuleImpl
