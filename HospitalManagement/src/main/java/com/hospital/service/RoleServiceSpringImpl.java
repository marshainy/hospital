package com.hospital.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.dao.RoleDAOInt;
import com.hospital.dto.RoleDTO;
import com.hospital.exception.DuplicateRecordException;



@Service
public class RoleServiceSpringImpl implements RoleServiceInt {

	@Autowired
	RoleDAOInt dao;

	

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public long add(RoleDTO dto) throws DuplicateRecordException {

		

		RoleDTO existdto = dao.findByName(dto.getRoleName());

		if (existdto != null) {
			throw new DuplicateRecordException("role is already exits");
		}

		
		return dao.add(dto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(RoleDTO dto) {
		// TODO Auto-generated method stub
		dao.delete(dto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(RoleDTO dto) throws DuplicateRecordException {
		
		RoleDTO existdto = dao.findByName(dto.getRoleName());
		
		if (existdto != null && existdto.getId() != dto.getId()) {
			throw new DuplicateRecordException("role is already exits");
		}

		

		dao.update(dto);
	}

	@Transactional(readOnly = true)
	public RoleDTO findByPK(long pk) {

		return dao.findByPk(pk);
	}

	@Transactional(readOnly = true)
	public RoleDTO findByName(String name) {
		// TODO Auto-generated method stub
		return dao.findByName(name);
	}

	@Transactional(readOnly = true)
	public List<RoleDTO> search(RoleDTO dto) {
		// TODO Auto-generated method stub
		return dao.search(dto);
	}

	@Transactional(readOnly = true)
	public List search(RoleDTO dto, int pageNo, int pageSize) {

		return dao.search(dto, pageNo, pageSize);
	}

	@Transactional(readOnly = true)
	public Map<Long, RoleDTO> getMapDTO(Set<Long> ids) {
		// TODO Auto-generated method stub
		return dao.getMapDTO(ids);
	}

}
