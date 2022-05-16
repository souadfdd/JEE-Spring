package com.ensah.springsecurityperson.core.web;

import com.ensah.springsecurityperson.core.services.IPersonService;
import com.ensah.springsecurityperson.core.services.IUserService;
import com.ensah.springsecurityperson.core.web.models.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping("/admin") // Très important car, dans Spring security les URL qui commencent par ADMIN
							// sont dédiées
							// à l'admin. Toutes les URL dédinies dans ce controleur définissent des
							// fonctionnalités
							// accessibles uniquement à l'administrateur
public class AccountController {

	@Autowired
	private IUserService userService; // On obtient par injection automatique le service

	@Autowired
	private IPersonService personService; // On obtient par injection automatique le service
	
	//Cette méthode initialise le formulaire de création de compte
	@RequestMapping(value = "createAccountForm/{idPerson}", method = RequestMethod.GET)
	public String createAccountForm(@PathVariable int idPerson, Model model) {

		
		//On crée le model 
		AccountModel accountModel = new AccountModel(Long.valueOf(idPerson));

		//On enregistre le modèle pour le passer à la vue
		model.addAttribute("accountModel", accountModel);

		//On ajoute la liste des roles dans le modele 
		model.addAttribute("roleList", userService.getAllRoles());
		
		//On ajoute également la liste des comptes dans le modèle
		model.addAttribute("accountList", userService.getAllAccounts());

		
		//On affiche la vue
		return "admin/formAccount";
	}

	@GetMapping("manageAccounts")
	public String manageAccounts(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {

		model.addAttribute("accountList", userService.getAllAccounts());

		return "admin/accountList";
	}
	@GetMapping("createAccounts")
	public String createAccounts(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {


		model.addAttribute("personList", personService.getAllPersons());

		return "admin/accountCreation";
	}
	
	
	//Cette méthode permet de créer un comote
	@PostMapping("addAccount")
	public String addAccount(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {

		
		//La création du compte est implémenter au niveau service
		//Il suffit de passer l'id du role et l'id de personne
		//à la couche service
		String password = userService.createUser(accountModel.getRoleId(), accountModel.getPersonId());

		//On affiche le mot de passe dans la vue 
		accountModel.setPassword(password);
		
		//On affiche également la liste des comptes dans la vue
		model.addAttribute("accountList", userService.getAllAccounts());

		
		//On affiche la vue 
		return "/admin/accountList";

	}

}
