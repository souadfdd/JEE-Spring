package com.ensah.web;

import com.ensah.core.bo.Person;
import com.ensah.core.services.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class PersonController {
    @Autowired
    private IPersonService PersonDao;
    private HashMap<String,String> countries=new HashMap();

    public PersonController() {
        countries.put("ma","Maroc");
        countries.put("fr","France");
    }

    @RequestMapping("/showForm")
    public String ShowForm(Model model){
        model.addAttribute("personModel",new Person());
        model.addAttribute("countries",countries);
        model.addAttribute("personList",PersonDao.getAllPersons());
        return "form";
    }
    @PostMapping("/addPerson")
    public String addPerson(@Valid @ModelAttribute("personModel") Person person, BindingResult bindingResult,Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("countries",countries);
            System.out.println("error");
            return "form";
        }
        PersonDao.addPerson(person);
        System.out.println("save");
        model.addAttribute("countries",countries);
        model.addAttribute("personList",PersonDao.getAllPersons());
        return "form";

    }
    @RequestMapping("/managePersons")
    public String ShowList(Model model){
        model.addAttribute("personList",PersonDao.getAllPersons());

        return "listPersons";
    }

    // on récupère la valeur du path variable idPerson dans le paramètre annotée
    // @PathVariable
    // Ici puisque nous avons utilisé le même nom pour le pathVariable et le
    // paramètre de la méthode, nous pouvons
    // remplacer @PathVariable(name = "idPerson") par @PathVariable
    @RequestMapping(value="/updatePersonForm/{id}",method= RequestMethod.GET)
    public String updatePersonForm(@PathVariable(name = "id") int id, Model model) {
        model.addAttribute("personeModel",PersonDao.getPersonById((long) id));
        model.addAttribute("countryList",countries );

        return "updateForm";
    }
    // l'annotation @Valid est nécessaire pour faire la validation avec Hibernate
    // Validator
    // On obtient les données envoyées de la vue dans l'attribut du modèle
    // personModel
    // ces données sont copiées vers l'argument person
    // l'argument bindingResult permet de savoir si il y a des erreurs de validation
    @RequestMapping("/updatePerson")
    public String updatePerson(@Valid @ModelAttribute("personeModel") Person person, BindingResult bindingResult,
                               Model model) {

        // Si il y a des erreurs de validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("countryList", countries);
            return "updateForm";
        }

        // Si il y a pas des erreurs
        PersonDao.updatePerson(person);
        model.addAttribute("personList", PersonDao.getAllPersons());

        // rediriger vers un autre handler
        return "redirect:/managePersons";
    }
 @RequestMapping(value="/deletePerson/{id}",method=RequestMethod.GET)
    public String delete(@PathVariable int id){
        PersonDao.deletePerson((long) id);
     // Behind the scenes, RedirectView will trigger a
     // HttpServletResponse.sendRedirect() – which will perform the actual redirect.
     return  "redirect:/managePersons";


 }

    @GetMapping(value = "/serachPerson")
    public String serachPerson(@RequestParam String nid, Model model) {

        Person per = PersonDao.getPersonByNationalIdNumber(nid);
        List<Person> list = new ArrayList<>();
        if (per != null) {
            list.add(per);

        }
        // Initialiser le modele
        model.addAttribute("personList", list);

        return "listPersons";
    }


}
