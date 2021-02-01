package com.hospital.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.dao.UserDAOInt;
import com.hospital.dto.UserDTO;
import com.hospital.exception.DuplicateRecordException;
import com.hospital.util.EmailBuilder;


/**
 * @author Navigable Set
 *
 *         User Service Implementation
 *
 */

@Service
public class UserServiceSpringImpl implements UserServiceInt {

	@Autowired
	private UserDAOInt dao;

	@Autowired
	private JavaMailSenderImpl mailSender;
 
	
	

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public long add(UserDTO dto) throws DuplicateRecordException {

		

		UserDTO Exitsdto = dao.findByLogin(dto.getUserName());

		if (Exitsdto != null) {
			throw new DuplicateRecordException("User Name is already Exits");
		}

		
		return dao.add(dto);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(UserDTO dto) {
		
		
		dao.delete(dto);

		

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(UserDTO dto) throws DuplicateRecordException {

		
		UserDTO Exitsdto = dao.findByLogin(dto.getUserName());

		if (Exitsdto != null && dto.getId() != Exitsdto.getId()) {
			throw new DuplicateRecordException("User is already Exits");
		}

		dao.update(dto);
		

	}

	@Transactional(readOnly = true)

	public UserDTO findByPK(long pk) {
		
		return dao.findByPk(pk);
	}

	@Transactional(readOnly = true)
	public UserDTO findByLogin(String login) {

		return dao.findByLogin(login);
	}

	@Transactional(readOnly = true)
	public List<UserDTO> search(UserDTO dto) {
		// TODO Auto-generated method stub
		return dao.search(dto);
	}

	@Transactional(readOnly = true)
	public List search(UserDTO dto, int pageNo, int pageSize) {

		return dao.search(dto, pageNo, pageSize);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean changePassword(Long id, String oldPassword, String newPassword) {
		
		
		UserDTO dtoExist = findByPK(id);

		if (dtoExist != null && dtoExist.getPassword().equals(oldPassword)) {

			dtoExist.setPassword(newPassword);

			dao.update(dtoExist);

			
			return true;
		} else {
			return false;
		}

	}

	@Transactional(readOnly = true)
	public UserDTO authenticate(UserDTO dto) {
		
		return dao.authenticate(dto);
		
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public long registerUser(UserDTO dto) {
		long id = dao.add(dto);

		

		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean forgetPassword(String login) {

		UserDTO dtoExist = dao.findByLogin(login);

		return true;
	}

}
