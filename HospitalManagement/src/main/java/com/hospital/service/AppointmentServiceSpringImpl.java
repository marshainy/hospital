package com.hospital.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.dao.AppointmentDAOInt;
import com.hospital.dto.AppointmentDTO;
import com.hospital.exception.DuplicateRecordException;

@Service
public class AppointmentServiceSpringImpl  implements AppointmentServiceInt{

	
	@Autowired
	AppointmentDAOInt dao;

	

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public long add(AppointmentDTO dto) throws DuplicateRecordException {

		

		AppointmentDTO existdto = dao.findByName(dto.getFirstName());

		if (existdto != null) {
			throw new DuplicateRecordException("Appointment is already exits");
		}

		
		return dao.add(dto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(AppointmentDTO dto) {
		// TODO Auto-generated method stub
		dao.delete(dto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(AppointmentDTO dto) throws DuplicateRecordException {
		

		AppointmentDTO existdto = dao.findByName(dto.getFirstName());
		
		if (existdto != null && existdto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Appointment is already exits");
		}


		dao.update(dto);
	}

	@Transactional(readOnly = true)
	public AppointmentDTO findByPK(long pk) {

		return dao.findByPk(pk);
	}

	@Transactional(readOnly = true)
	public AppointmentDTO findByName(String name) {
		// TODO Auto-generated method stub
		return dao.findByName(name);
	}

	@Transactional(readOnly = true)
	public List<AppointmentDTO> search(AppointmentDTO dto) {
		// TODO Auto-generated method stub
		return dao.search(dto);
	}

	@Transactional(readOnly = true)
	public List search(AppointmentDTO dto, int pageNo, int pageSize) {

		return dao.search(dto, pageNo, pageSize);
	}

	@Transactional(readOnly = true)
	public Map<Long, AppointmentDTO> getMapDTO(Set<Long> ids) {
		// TODO Auto-generated method stub
		return dao.getMapDTO(ids);
	}
	
}
