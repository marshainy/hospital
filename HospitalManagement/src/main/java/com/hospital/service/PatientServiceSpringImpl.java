package com.hospital.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.dao.PatientDAOInt;
import com.hospital.dao.RoleDAOInt;
import com.hospital.dto.PatientDTO;
import com.hospital.exception.DuplicateRecordException;

@Service
public class PatientServiceSpringImpl implements PatientServiceInt {
	
	@Autowired
	PatientDAOInt dao;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public long add(PatientDTO dto) throws DuplicateRecordException {

		PatientDTO existdto = dao.findByName(dto.getFirstName());

		if (existdto != null) {
			throw new DuplicateRecordException("Patient is already exits");
		}

		return dao.add(dto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(PatientDTO dto) {
		// TODO Auto-generated method stub
		dao.delete(dto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(PatientDTO dto) throws DuplicateRecordException {

		PatientDTO existdto = dao.findByName(dto.getFirstName());
		
		if (existdto != null && existdto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Patient is already exists");
		}

		dao.update(dto);
	}

	@Transactional(readOnly = true)
	public PatientDTO findByPK(long pk) {

		return dao.findByPk(pk);
	}

	@Transactional(readOnly = true)
	public PatientDTO findByName(String name) {
		// TODO Auto-generated method stub
		return dao.findByName(name);
	}

	@Transactional(readOnly = true)
	public List<PatientDTO> search(PatientDTO dto) {
		// TODO Auto-generated method stub
		return dao.search(dto);
	}

	@Transactional(readOnly = true)
	public List search(PatientDTO dto, int pageNo, int pageSize) {

		return dao.search(dto, pageNo, pageSize);
	}

	@Transactional(readOnly = true)
	public Map<Long, PatientDTO> getMapDTO(Set<Long> ids) {
		// TODO Auto-generated method stub
		return dao.getMapDTO(ids);
	}
}
