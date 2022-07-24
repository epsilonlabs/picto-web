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
import org.eclipse.epsilon.egl.EgxModule;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.picto.incrementality.PropertyAccessRecord.AccessRecordState;

public class PropertyAccessRecordTable implements IncrementalResource {

	List<PropertyAccessRecord> propertyAccessRecords = new LinkedList<>();

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
	public boolean isViewNewOrUpdated(String checkedPath, EgxModule currentModule) {
		boolean result = false;
		Set<String> paths = propertyAccessRecords.stream().map(r -> r.getPath())
				.collect(Collectors.toCollection(HashSet::new));
		if (!paths.contains(checkedPath)) {
			return true;
		}

		Collection<PropertyAccessRecord> checkedPathRecords = propertyAccessRecords.stream()
				.filter(r -> checkedPath.equals(r.getPath())).collect(Collectors.toCollection(HashSet::new));

		for (PropertyAccessRecord record : checkedPathRecords) {
			
//			System.out.println(record.toString());
			
			String propertyName = record.getPropertyName();
			String previousValue = record.getValue();
			String uriFragment = record.getElementObjectId();
			String elementResourceUri = record.getElementResourceUri();

			EmfModel model = (EmfModel) currentModule.getContext().getModelRepository().getModels().stream().filter(
					m -> ((AbstractEmfModel) m).getResource().getURI().toFileString().equals(elementResourceUri))
					.findFirst().orElse(null);

			Resource currentResource = model.getResource();
			EObject currentEObject = currentResource.getEObject(uriFragment);
			EStructuralFeature currentProperty = currentEObject.eClass().getEStructuralFeature(propertyName);
			Object currentValueObject = currentEObject.eGet(currentProperty);

			String currentValue = PropertyAccessRecord.convertValueToString(currentValueObject);
			if (!Util.equals(previousValue, currentValue)) {
				return true;
			}
		}
		return result;
	}

	@Override
	public Set<String> getToBeProcessedPaths() {
		Set<String> paths = propertyAccessRecords.stream().filter(
//				r -> !Util.equals(r.oldValue, r.value)
				r -> r.getState().equals(AccessRecordState.NEW)).map(r -> r.getPath())
				.collect(Collectors.toCollection(HashSet::new));
		return paths;
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
