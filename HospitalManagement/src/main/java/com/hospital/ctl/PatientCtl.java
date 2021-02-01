package com.hospital.ctl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.dto.PatientDTO;
import com.hospital.dto.UserDTO;
import com.hospital.exception.DuplicateRecordException;
import com.hospital.form.DoctorForm;
import com.hospital.form.PatientForm;
import com.hospital.service.PatientServiceInt;

@Controller
@RequestMapping(value = "/ctl/Patient")
public class PatientCtl extends BaseCtl {
	
	@Autowired
	PatientServiceInt patientservice;

	@Autowired
	MessageSource messageSource;
	
	
	@ModelAttribute
	public void preload(Model model) {
		Map<String, String> genderMap = new LinkedHashMap<String, String>();
		genderMap.put("Female", "Female");
		genderMap.put("Male", "Male");
		
		
		
		model.addAttribute("genderMap", genderMap);
		
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String display(@RequestParam(required = false) Long id, @ModelAttribute("form") PatientForm form, Model model) {

		if (form.getId() > 0) {
			form.populate(patientservice.findByPK(id));
		}

		return "Patient";

	}
	
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String submit(Locale locale, @RequestParam String operation, @ModelAttribute("form") @Valid PatientForm form,
			BindingResult bindingResult, Model model) {

	
		if (OP_RESET.equalsIgnoreCase(operation)) {
			return "redirect:User";
		}
		
		if (OP_CANCEL.equalsIgnoreCase(operation)) {
			return "redirect:User/Search";
		}

		if (OP_SAVE.equalsIgnoreCase(operation)) {

			

			if (bindingResult.hasErrors()) {
				System.err.println("input error");
				return "Patient";
			}

			PatientDTO dto = (PatientDTO) form.getDto();
	
			try {
				String msg = null;

				if (form.getId() > 0) {
					patientservice.update(dto);
					msg = messageSource.getMessage("message.success.update", null, locale);
				}
				if (form.getId() == 0) {
					patientservice.add(dto);
					msg = messageSource.getMessage("message.success.add", null, locale);
				}
				model.addAttribute("success", msg);
			} catch (DuplicateRecordException e) {
				String msg = messageSource.getMessage("message.userIsAlreadyExist", null, locale);
				model.addAttribute("error", msg);
			}
			return "Patient";
		}
		return null;

	}
	
	@RequestMapping(value = "/Search", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchList(Locale locale, @ModelAttribute("form") PatientForm form,
			@RequestParam(required = false) String operation,HttpSession session, Model model) {

	
		
		
		if (OP_RESET.equalsIgnoreCase(operation)) {
			return "redirect:/ctl/Patient/Search";
		}
		
		
		int pageNo = form.getPageNo();
		int pageSize = form.getPageSize();

		if (OP_NEXT.equals(operation)) {
			pageNo++;
		} else if (OP_PREVIOUS.equals(operation)) {
			pageNo--;
		}else if(OP_NEW.equals(operation)) {
			return "redirect:/ctl/Patient";
		}

		pageNo = (pageNo < 1) ? 1 : pageNo;
		pageSize = (pageSize < 1) ? 10 : pageSize;

		if (OP_DELETE.equals(operation)) {
			pageNo = 1;
			if (form.getIds() != null) {

				for (long id : form.getIds()) {
					PatientDTO dto = new PatientDTO();
					dto.setId(id);
					patientservice.delete(dto);
				}
				String msg = messageSource.getMessage("message.success.delete", null, locale);
				model.addAttribute("success", msg);

				// redirectAttributes.addFlashAttribute("success", msg);
				// return "UserList";

			} else {

				System.out.println("select at least one records");
				String msg = messageSource.getMessage("message.unsuccess.selectatleastonerecords", null, locale);
				model.addAttribute("error", msg);
			}

		}
		
		PatientDTO dto=(PatientDTO)form.getDto();
		List<PatientDTO> list = patientservice.search(dto, pageNo, pageSize);

		List<PatientDTO> totallist = patientservice.search(null);

		model.addAttribute("list", list);

		
	

		if (list.size() == 0 && !OP_DELETE.equalsIgnoreCase(operation)) {
			String msg = messageSource.getMessage("message.unsuccess.recoudNotFound", null, locale);
			model.addAttribute("error", msg);
		}

		int listsize = list.size();
		int total = totallist.size();
		int pageNoPageSize = pageNo * pageSize;

		form.setPageNo(pageNo);
		form.setPageSize(pageSize);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("pageSize", pageSize);
		
		
		model.addAttribute("listsize", listsize);
		model.addAttribute("total", total);
		model.addAttribute("pagenosize", pageNoPageSize);

		return "PatientList";
	}

}
