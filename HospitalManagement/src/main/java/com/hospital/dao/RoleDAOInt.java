package com.hospital.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hospital.dto.RoleDTO;




public interface RoleDAOInt {

	
	
	public long add(RoleDTO dto);

	
	public void update(RoleDTO dto);

	
	public void delete(RoleDTO dto);
	

  
	public RoleDTO findByName(String name);
	

	
	public RoleDTO findByPk(long id);

	
	public List<RoleDTO> search(RoleDTO dto, long pageNo, int pageSize);

	
	public List<RoleDTO> search(RoleDTO dto);
	
	
	
	public Map<Long, RoleDTO> getMapDTO(Set<Long> ids);
	
}
