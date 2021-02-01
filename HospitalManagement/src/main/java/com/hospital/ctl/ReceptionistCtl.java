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

import com.hospital.dto.UserDTO;
import com.hospital.exception.DuplicateRecordException;
import com.hospital.form.DoctorForm;
import com.hospital.form.ReceptionistForm;
import com.hospital.service.RoleServiceInt;
import com.hospital.service.UserServiceInt;

@Controller
@RequestMapping(value = "/ctl/Receptionist")
public class ReceptionistCtl extends BaseCtl{

	
	@Autowired
	UserServiceInt userservice;

	@Autowired
	RoleServiceInt roleservice;

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
	public String display(@RequestParam(required = false) Long id, @ModelAttribute("form") ReceptionistForm form, Model model) {

		if (form.getId() > 0) {
			form.populate(userservice.findByPK(id));
		}

		return "Receptionist";

	}
	
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String submit(Locale locale, @RequestParam String operation, @ModelAttribute("form") @Valid ReceptionistForm form,
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
				return "Receptionist";
			}

			UserDTO dto = (UserDTO) form.getDto();
			dto.setRoleId(3L);
			try {
				String msg = null;

				if (form.getId() > 0) {
					userservice.update(dto);
					msg = messageSource.getMessage("message.success.update", null, locale);
				}
				if (form.getId() == 0) {
					userservice.add(dto);
					msg = messageSource.getMessage("message.success.add", null, locale);
				}
				model.addAttribute("success", msg);
			} catch (DuplicateRecordException e) {
				String msg = messageSource.getMessage("message.userIsAlreadyExist", null, locale);
				model.addAttribute("error", msg);
			}
			return "Receptionist";
		}
		return null;

	}
	
	
	@RequestMapping(value = "/Search", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchList(Locale locale, @ModelAttribute("form") ReceptionistForm form,
			@RequestParam(required = false) String operation,HttpSession session, Model model) {

	
		
		
		if (OP_RESET.equalsIgnoreCase(operation)) {
			return "redirect:/ctl/Receptionist/Search";
		}
		
		
		int pageNo = form.getPageNo();
		int pageSize = form.getPageSize();

		if (OP_NEXT.equals(operation)) {
			pageNo++;
		} else if (OP_PREVIOUS.equals(operation)) {
			pageNo--;
		}else if(OP_NEW.equals(operation)) {
			return "redirect:/ctl/Receptionist";
		}

		pageNo = (pageNo < 1) ? 1 : pageNo;
		pageSize = (pageSize < 1) ? 10 : pageSize;

		if (OP_DELETE.equals(operation)) {
			pageNo = 1;
			if (form.getIds() != null) {

				for (long id : form.getIds()) {
					UserDTO dto = new UserDTO();
					dto.setId(id);
					userservice.delete(dto);
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
		
		UserDTO dto=(UserDTO)form.getDto();
		dto.setRoleId(3L);
		List<UserDTO> list = userservice.search(dto, pageNo, pageSize);

		List<UserDTO> totallist = userservice.search(null);

		model.addAttribute("list", list);

		Set<Long> roleIds = new HashSet<Long>();
		for (UserDTO userDTO : list) {
			roleIds.add(userDTO.getRoleId());
		}

		model.addAttribute("roleMap", roleservice.getMapDTO(roleIds));

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

		return "ReceptionistList";
	}
	
	
}
