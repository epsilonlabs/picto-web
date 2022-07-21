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

import org.eclipse.epsilon.picto.incrementality.PropertyAccessRecord.AccessRecordStatus;

public class PropertyAccessRecordTable implements IncrementalResource {

	List<PropertyAccessRecord> propertyAccessRecords = new LinkedList<>();

	@Override
	public void add(PropertyAccessRecord propertyAccessRecord) {
		List<PropertyAccessRecord> records = propertyAccessRecords.stream()
				.filter(r -> Util.equals(r.getModulePath(), propertyAccessRecord.getModulePath())
						&& Util.equals(r.getGenerationRuleName(), propertyAccessRecord.getGenerationRuleName())
						&& Util.equals(r.getContextResourceUri(), propertyAccessRecord.getContextResourceUri())
						&& Util.equals(r.getContextObjectId(), propertyAccessRecord.getContextObjectId())
						&& Util.equals(r.getContextResourceUri(), propertyAccessRecord.getContextResourceUri())
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
	public Set<String> getToBeProcessedPaths() {
		Set<String> accessRecords = new HashSet<>();
		for (PropertyAccessRecord r : propertyAccessRecords) {
			if (((!PropertyAccessRecord.INITIAL_VALUE.equals(r.getOldValue())
					&& !Util.equals(r.getOldValue(), r.getValue())) //
//					|| r.getStatus().equals(AccessRecordStatus.NEW)
					)) {
				accessRecords.add(r.getPath());
			}

		}
		return accessRecords;
	}

	@Override
	public List<PropertyAccessRecord> getModifiedPropertiesTargets() {
		List<PropertyAccessRecord> records = propertyAccessRecords.stream().filter(
				r -> !Util.equals(r.oldValue, PropertyAccessRecord.INITIAL_VALUE) && !Util.equals(r.oldValue, r.value))
				.collect(Collectors.toCollection(ArrayList::new));
		return records;
	}

	@Override
	public List<String> getNewPaths() {
		Set<String> records = propertyAccessRecords.stream()
				.filter(r -> r.getStatus().equals(AccessRecordStatus.NEW) && r.getPath() != null)
				.map(r -> r.getPath().toString()).collect(Collectors.toCollection(HashSet::new));
		return new ArrayList<>(records);
	}

	@Override
	public List<PropertyAccessRecord> getNewAccessRecords() {
		List<PropertyAccessRecord> records = propertyAccessRecords.stream()
				.filter(r -> r.getStatus().equals(AccessRecordStatus.NEW) && r.getPath() != null)
				.collect(Collectors.toCollection(ArrayList::new));
		return records;
	}

	@Override
	public void updateStatusToProcessed(Collection<String> paths) {
		propertyAccessRecords.stream().filter(r -> paths.contains(r.getPath())).forEach(r -> {
			r.setStatus(AccessRecordStatus.PROCESSED);
		});
	}

	@Override
	public void updateStatusToProcessed(String path) {
		for (PropertyAccessRecord r : propertyAccessRecords.stream().filter(r -> path.equals(r.getPath().toString()))
				.collect(Collectors.toList())) {
			r.setStatus(AccessRecordStatus.PROCESSED);
		}
		System.console();
	}

}
