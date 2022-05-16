package com.ensah.springsecurityperson.core.web;

import java.util.HashMap;
import java.util.Map;

import com.ensah.springsecurityperson.core.bo.Person;
import com.ensah.springsecurityperson.core.services.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class PersonAdminController {

	@Autowired
	private IPersonService personService;

	private Map<String, String> countryList = new HashMap<String, String>();

	public PersonAdminController() {
		countryList.put("Morocco", "Morocco");
		countryList.put("France", "France");
		countryList.put("Spain", "Spain");

	}



	
	@RequestMapping("/showForm")
	public String showForm(Model model) {

		model.addAttribute("personModel", new Person());
		model.addAttribute("countryList", countryList);
		model.addAttribute("personList", personService.getAllPersons());
		return "admin/form";
	}

	@RequestMapping(value = "updatePersonForm/{idPerson}", method = RequestMethod.GET)
	public String updatePersonForm(@PathVariable int idPerson, Model model) {

		model.addAttribute("personModel", personService.getPersonById(Long.valueOf(idPerson)));
		model.addAttribute("countryList", countryList);

		return "admin/updateForm";
	}

	@RequestMapping("updatePerson")
	public String updatePerson(@Valid @ModelAttribute("personModel") Person person, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {

			model.addAttribute("countryList", countryList);
			return "admin/updateForm";
		}

		personService.updatePerson(person);
		model.addAttribute("personList", personService.getAllPersons());

		return "redirect:/admin/managePersons";
	}

	@RequestMapping("addPerson")
	public String process(@Valid @ModelAttribute("personModel") Person person, BindingResult bindingResult,
						  Model model) {

		if (bindingResult.hasErrors()) {

			model.addAttribute("countryList", countryList);
			return "admin/form";
		}

		personService.addPerson(person);
		model.addAttribute("personList", personService.getAllPersons());

		return "redirect:/admin/showForm";
	}



	@RequestMapping("managePersons")
	public String managePersons(Model model) {

		model.addAttribute("personList", personService.getAllPersons());

		return "admin/listPersons";
	}

	@RequestMapping(value = "deletePerson/{idPerson}", method = RequestMethod.GET)
	public String delete(@PathVariable int idPerson) {

		personService.deletePerson(Long.valueOf(idPerson));

		return "redirect:/admin/managePersons";
	}

	public Map<String, String> getCountryList() {
		return countryList;
	}

	public void setCountryList(Map<String, String> countryList) {
		this.countryList = countryList;
	}

}
