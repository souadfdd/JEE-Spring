package com.ensah.springsecurityperson.core.services;

import com.ensah.springsecurityperson.core.bo.Person;
import com.ensah.springsecurityperson.core.bo.Role;
import com.ensah.springsecurityperson.core.bo.UserAccount;
import com.ensah.springsecurityperson.core.dao.IPersonDao;
import com.ensah.springsecurityperson.core.dao.IRoleDao;
import com.ensah.springsecurityperson.core.dao.IUserAccountDao;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional

public class UserServiceImpl implements IUserService{
    @Autowired
    private IUserAccountDao userDao;
    @Autowired
    private IRoleDao roleDao;
    @Autowired
    private IPersonDao personDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<Role> getAllRoles() {
        return roleDao.findAll();
    }

    @Override
    public List<UserAccount> getAllAccounts() {
        return userDao.findAll();
    }

    @Override
    public String createUser(Long idRole, Long idPerson) {
        Person user= personDao.getById(idPerson);
        Role role=roleDao.getById(idRole);
        //create userAccount
        UserAccount userAccount = new UserAccount();
        userAccount.setPerson(user);
        userAccount.setRole(role);
        // generate a random password
        String generatedPass = generatePassayPassword();
        // hachage du mot de passe + gain de sel
        String encodedPass = passwordEncoder.encode(generatedPass);
        // affecter ce mot de passe
        userAccount.setPassword(encodedPass);
        // On construit un login de type "nom+prenom " s'il est dispo
        String login = user.getFirstName() + user.getLastName();

        UserAccount account = userDao.getUserByUsername(login);

        if (account == null) {

            userAccount.setUsername(login);

            // Créer le compte
            userDao.save(userAccount);
            return generatedPass;
        }
        int i = 0;

        // sinon, on cherche un login de type nom+prenom+"_"+ entier
        while (true) {

            login = user.getFirstName() + user.getLastName() + "_" + i;
            account = userDao.getUserByUsername(login);
            if (account == null) {
                userAccount.setUsername(login);
                // Créer le compte
                userDao.save(userAccount);
                return generatedPass;
            }

            i++;
        }
    }



    // génère le mot de passe. Il se base sur Passay
    private String generatePassayPassword() {
        CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);

        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String password = passwordGenerator.generatePassword(10, digits);

        return password;
    }
}
