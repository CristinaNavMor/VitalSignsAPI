package com.vitalNow.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vitalNow.model.Record;
import com.vitalNow.model.RecordId;



public interface RecordRepository extends JpaRepository<Record, RecordId> {
	
	@Query(value = "SELECT * FROM Record "
			+ "WHERE patient_recordNumber = :medicalRecordNumber "
			+ "AND vitalSign = :vitalSignName "
			+ "AND (dateTime BETWEEN DATE_SUB(NOW(), INTERVAL 24 HOUR) AND NOW()) "
			+ "ORDER BY dateTime DESC;", nativeQuery= true)
	List<Record> getLast24hRecords(@Param("medicalRecordNumber") String medicalRecordNumber, @Param("vitalSignName") String vitalSignName);
	
	@Query(value = "SELECT * FROM Record "
			+ "WHERE patient_recordNumber = :medicalRecordNumber "
			+ "AND vitalSign = :vitalSignName "
			+ "AND (dateTime BETWEEN DATE_SUB(NOW(), INTERVAL :day DAY) AND NOW()) "
			+ "ORDER BY dateTime DESC;", nativeQuery= true)
	List<Record> getLastRecordsInDays(@Param("medicalRecordNumber") String medicalRecordNumber, @Param("vitalSignName") String vitalSignName, @Param("day") Integer day);
	
	@Query(value = "SELECT * FROM Record "
			+ "WHERE patient_recordNumber = :medicalRecordNumber "
			+ "AND vitalSign = :vitalSignName "
			+ "ORDER BY dateTime DESC "
			+ "LIMIT 1;", nativeQuery= true)
	Optional<Record> getLastRecord(@Param(value = "medicalRecordNumber") String medicalRecordNumber,@Param(value = "vitalSignName") String vitalSignName);
	
	
	@Query(value = "SELECT r FROM Record r "
			+ "WHERE r.patient.medicalRecordNumber = :medicalRecordNumber "
			+ "AND r.vitalSign.name = :vitalSignName "
			+ "AND r.user.username = :username "
			+ "AND r.dateTime = :dateTime ")
	Optional<Record> getRecord(@Param(value = "medicalRecordNumber") String medicalRecordNumber,
									@Param(value = "vitalSignName") String vitalSignName,
									@Param(value = "username") String username,
									@Param(value = "dateTime") LocalDateTime dateTime);
}
