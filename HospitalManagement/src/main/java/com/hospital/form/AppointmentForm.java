package com.hospital.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.hospital.dto.AppointmentDTO;
import com.hospital.dto.BaseDTO;
import com.hospital.dto.PatientDTO;
import com.hospital.dto.UserDTO;
import com.hospital.util.DataUtility;

public class AppointmentForm extends BaseForm {

	
	
	@NotEmpty
	@Pattern(regexp = "(^[A-Za-z ]*)*$")
	private String firstName;
	
	@NotEmpty
	@Pattern(regexp = "(^[A-Za-z ]*)*$")
	private String lastName;
	
	
	@NotEmpty
	private String phoneNo;
	
	
	private String docName;
	@NotEmpty
	private String appDate;
	@NotEmpty
	private String time;
	
	@Min(value = 1,message = "Doctor Name is required")
	private long docId;
	
	
	
	
	public long getDocId() {
		return docId;
	}

	public void setDocId(long docId) {
		this.docId = docId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getAppDate() {
		return appDate;
	}

	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public BaseDTO getDto() {
		AppointmentDTO dto=new AppointmentDTO();
		dto.setId(id);
		dto.setCreatedBy(createdBy);
		dto.setModifiedBy(modifiedBy);
		dto.setCreatedDatetime(createdDateTime);
		dto.setModifiedDatetime(modifiedDateTime);
		
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		dto.setPhoneNo(phoneNo);
		dto.setAppDate(DataUtility.getDate(appDate));
		dto.setTime(time);
		dto.setDoctorName(docName);
		dto.setDocId(docId);
		return dto;
		
	}

	@Override
	public void populate(BaseDTO bDto) {
		AppointmentDTO dto = (AppointmentDTO) bDto;
		
		id = dto.getId();
		createdBy = dto.getCreatedBy();
		modifiedBy = dto.getModifiedBy();
		createdDateTime = dto.getCreatedDatetime();
		modifiedDateTime = dto.getModifiedDatetime();
		firstName = dto.getFirstName();
		lastName =dto.getLastName();
		
		docId=dto.getDocId();
		phoneNo =dto.getPhoneNo();
		appDate=DataUtility.getStringDate(dto.getAppDate());
		docName =dto.getDoctorName();
		time=dto.getTime();
		
		
	}

}
