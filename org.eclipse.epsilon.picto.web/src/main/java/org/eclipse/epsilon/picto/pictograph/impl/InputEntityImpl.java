/**
 */
package org.eclipse.epsilon.picto.pictograph.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.epsilon.picto.pictograph.InputEntity;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.PictographPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input Entity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.picto.pictograph.impl.InputEntityImpl#getAffects <em>Affects</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class InputEntityImpl extends EntityImpl implements InputEntity {
	/**
	 * The cached value of the '{@link #getAffects() <em>Affects</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAffects()
	 * @generated
	 * @ordered
	 */
	protected EList<Path> affects;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputEntityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PictographPackage.Literals.INPUT_ENTITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Path> getAffects() {
		if (affects == null) {
			affects = new EObjectWithInverseResolvingEList.ManyInverse<Path>(Path.class, this, PictographPackage.INPUT_ENTITY__AFFECTS, PictographPackage.PATH__AFFECTED_BY);
		}
		return affects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PictographPackage.INPUT_ENTITY__AFFECTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getAffects()).basicAdd(otherEnd, msgs);
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
			case PictographPackage.INPUT_ENTITY__AFFECTS:
				return ((InternalEList<?>)getAffects()).basicRemove(otherEnd, msgs);
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
			case PictographPackage.INPUT_ENTITY__AFFECTS:
				return getAffects();
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
			case PictographPackage.INPUT_ENTITY__AFFECTS:
				getAffects().clear();
				getAffects().addAll((Collection<? extends Path>)newValue);
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
			case PictographPackage.INPUT_ENTITY__AFFECTS:
				getAffects().clear();
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
			case PictographPackage.INPUT_ENTITY__AFFECTS:
				return affects != null && !affects.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //InputEntityImpl
