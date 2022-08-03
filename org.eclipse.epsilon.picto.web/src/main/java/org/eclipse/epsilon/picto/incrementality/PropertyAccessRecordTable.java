package org.eclipse.epsilon.picto.incrementality;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;
import org.eclipse.epsilon.picto.incrementality.PropertyAccessRecord.AccessRecordState;

public class PropertyAccessRecordTable implements IncrementalResource {

	List<PropertyAccessRecord> propertyAccessRecords = new LinkedList<>();

	@Override
	public void clear() {
		propertyAccessRecords.clear();
	}
	
	@Override
	public void add(PropertyAccessRecord propertyAccessRecord) {
		List<PropertyAccessRecord> records = propertyAccessRecords.stream()
				.filter(r -> Util.equals(r.getModulePath(), propertyAccessRecord.getModulePath())
						&& Util.equals(r.getGenerationRuleName(), propertyAccessRecord.getGenerationRuleName())
						&& Util.equals(r.getContextResourceUri(), propertyAccessRecord.getContextResourceUri())
						&& Util.equals(r.getContextObjectId(), propertyAccessRecord.getContextObjectId())
						&& Util.equals(r.getElementResourceUri(), propertyAccessRecord.getElementResourceUri())
						&& Util.equals(r.getElementObjectId(), propertyAccessRecord.getElementObjectId())
						&& Util.equals(r.getPropertyName(), propertyAccessRecord.getPropertyName())
						&& Util.equals(r.getPath(), propertyAccessRecord.getPath()))
				.collect(Collectors.toList());
		if (records.size() > 0) {
			for (PropertyAccessRecord record : records) {
				record.setValue(propertyAccessRecord.getValue());
				record.setState(AccessRecordState.MODIFIED);
			}
		} else {
			propertyAccessRecords.add(propertyAccessRecord);
		}
	}

	@Override
	public List<PropertyAccessRecord> getIncrementalRecords() {
		return propertyAccessRecords;
	}

	@Override
	public void printIncrementalRecords() {
		System.out.println();
		System.out.println("Trace at " + Timestamp.from(Instant.now()).toString() + ":");
		System.out.println();
		int lineNum = 1;
		for (PropertyAccessRecord record : propertyAccessRecords) {
			System.out.println(lineNum++ + ". " + record.toString());
		}
		System.out.println();
	}

	@Override
	public Set<String> getToBeProcessedPaths(List<LazyGenerationRuleContentPromise> inProcessingPromises,
			EgxModule module) {
		Set<String> toBeProcessedPaths = new HashSet<String>();

		for (LazyGenerationRuleContentPromise promise : inProcessingPromises) {
			String checkedPath = Util.getPath(promise);

			if (checkedPath.equals("/Stats") || checkedPath.equals("/Custom/Alice and Bob")) {
				System.console();
			}

//			// check if the path is a new view
			PropertyAccessRecord pas = propertyAccessRecords.stream().filter(r -> r.getPath().equals(checkedPath))
					.findFirst().orElse(null);
			if (pas == null) {
				toBeProcessedPaths.add(checkedPath);
			}

			// check if the property access record is new
			PropertyAccessRecord accessRecord = propertyAccessRecords.stream()
					.filter(r -> r.getState().equals(AccessRecordState.NEW) && r.getPath().equals(checkedPath))
					.findFirst().orElse(null);
			if (accessRecord != null) {
				toBeProcessedPaths.add(accessRecord.getPath());

//				Set<String> affectedPaths = propertyAccessRecords.stream()
//				.filter(r -> r.getValue().contains(accessRecord.getElementObjectId())).map(r -> r.getPath())
//				.collect(Collectors.toCollection(HashSet::new));
//				
//				toBeProcessedPaths.addAll(affectedPaths);

				// get also the container of the object
				String elementResourceUri = accessRecord.getElementResourceUri();
				EmfModel model = (EmfModel) module.getContext().getModelRepository().getModels().stream().filter(
						m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(elementResourceUri))
						.findFirst().orElse(null);

				Resource resource = model.getResource();
				EObject eObject = resource.getEObject(accessRecord.getElementObjectId());
				EObject eContainer = (eObject != null) ? eObject.eContainer() : null;
				while (eContainer != null) {
					String fragment = resource.getURIFragment(eContainer);
					Set<String> temp = propertyAccessRecords.stream()
							.filter(r -> r.getElementObjectId().equals(fragment)).map(r -> r.getPath())
							.collect(Collectors.toCollection(HashSet::new));
					if (temp.size() > 0) {
						toBeProcessedPaths.addAll(temp);
					}
					eContainer = eContainer.eContainer();
				}
			}

			// check objects and properties that view of the path has
			Collection<PropertyAccessRecord> checkedPathRecords = propertyAccessRecords.stream()
					.filter(r -> checkedPath.equals(r.getPath())).collect(Collectors.toCollection(HashSet::new));

			for (PropertyAccessRecord record : checkedPathRecords) {

				String propertyName = record.getPropertyName();
				String previousValue = record.getValue();
				String uriFragment = record.getElementObjectId();
				String elementResourceUri = record.getElementResourceUri();

				EmfModel model = (EmfModel) module.getContext().getModelRepository().getModels().stream().filter(
						m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(elementResourceUri))
						.findFirst().orElse(null);

				Resource currentResource = model.getResource();
				EObject currentEObject = currentResource.getEObject(uriFragment);

				// check if the object has been deleted, does not exists in the resource
				// (deleted, updated)
				if (currentEObject == null) {
					toBeProcessedPaths.add(checkedPath);
				}
				if (currentEObject != null) {
					EStructuralFeature currentProperty = currentEObject.eClass().getEStructuralFeature(propertyName);
					Object currentValueObject = (currentProperty != null) ? currentEObject.eGet(currentProperty) : null;

					// check if the view of the path contains object with a changed property
					String currentValue = PropertyAccessRecord.convertValueToString(currentValueObject);
					if (!Util.equals(previousValue, currentValue)) {
						toBeProcessedPaths.add(checkedPath);
					}
				}

			}
		}
		return toBeProcessedPaths;
	}

	@Override
	public List<PropertyAccessRecord> getGeneratedPropertyAccessRecords() {
		List<PropertyAccessRecord> records = propertyAccessRecords.stream().filter(
				r -> !Util.equals(r.oldValue, PropertyAccessRecord.INITIAL_VALUE) && !Util.equals(r.oldValue, r.value))
				.collect(Collectors.toCollection(ArrayList::new));
		return records;
	}

//	@Override
//	public List<String> getNewPaths() {
//		Set<String> records = propertyAccessRecords.stream()
//				.filter(r -> r.getStatus().equals(AccessRecordStatus.NEW) && r.getPath() != null)
//				.map(r -> r.getPath().toString()).collect(Collectors.toCollection(HashSet::new));
//		return new ArrayList<>(records);
//	}

//	@Override
//	public List<PropertyAccessRecord> getNewAccessRecords() {
//		List<PropertyAccessRecord> records = propertyAccessRecords.stream()
//				.filter(r -> r.getStatus().equals(AccessRecordStatus.NEW) && r.getPath() != null)
//				.collect(Collectors.toCollection(ArrayList::new));
//		return records;
//	}

	@Override
	public void updateStatusToProcessed(Collection<String> paths) {
		propertyAccessRecords.stream().filter(r -> paths.contains(r.getPath())).forEach(r -> {
			r.setOldValue(r.getValue());
			r.setState(AccessRecordState.PROCESSED);
		});
	}

	@Override
	public void updateStatusToProcessed(String path) {
		for (PropertyAccessRecord r : propertyAccessRecords.stream().filter(r -> path.equals(r.getPath()))
				.collect(Collectors.toList())) {
			r.setOldValue(r.getValue());
			r.setState(AccessRecordState.PROCESSED);
		}
		System.console();
	}

}
