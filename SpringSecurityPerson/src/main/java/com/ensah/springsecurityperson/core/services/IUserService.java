package com.ensah.springsecurityperson.core.services;

import com.ensah.springsecurityperson.core.bo.Role;
import com.ensah.springsecurityperson.core.bo.UserAccount;

import java.util.List;

public interface IUserService {
    public List<Role> getAllRoles();

    public List<UserAccount> getAllAccounts();

    public String createUser(Long idRole, Long idPerson);
}
