package com.example.spring_rest_app.core.web;

import com.example.spring_rest_app.core.bo.Person;
import com.example.spring_rest_app.core.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonRestController {
    // On y injecte le service métier
    // On y injecte le service métier
    @Autowired
    private IPersonService personService;
    //permet de chercher une personne par son ID
    @GetMapping("/persons/{idPerson}")
    public Person getPersonById(@PathVariable int idPerson){
        Person p=personService.getPersonById(Long.valueOf(idPerson));
        return p;
    }
    //cas le contolleur recieve donnée
    // Ajoute une personne dans la base de données
    @PostMapping("/persons")
    public Person addPerson(@RequestBody Person person) {
        // En cas ou un id a été envoyé on le rend égale à null
        // pour éviter une mise à jour par erreur
        person.setIdPersonne(null);
        personService.addPerson(person);
        return person;
    }
    // Permer de récupérer toutes les personnes
    @GetMapping("/persons")
    public List<Person> getAllPersons(){
        return personService.getAllPersons();
    }
    // Permet de modifier une personne
    // La personne sera reçu dans le corps de la requete (Request Body)
    @PutMapping("/persons")
    public Person updatePerson(@RequestBody Person person) {

        personService.updatePerson(person);

        return person;
    }
    // Permet de supprimer une personne
    // La personne sera reçu dans le corps de la requete (Request Body)
    @DeleteMapping("/persons/{idPerson}")
    public String deletePersonById(@PathVariable int idPerson) {

        personService.deletePerson(Long.valueOf(idPerson));

        return "deleted person id = " + idPerson;
    }




}
