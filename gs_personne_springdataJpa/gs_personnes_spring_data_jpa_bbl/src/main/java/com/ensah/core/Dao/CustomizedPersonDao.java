package com.ensah.core.Dao;
import java.util.List;
import com.ensah.core.bo.Person;
public interface CustomizedPersonDao   {	
	List<Person> findPersonsStateOrderdAge(String pState);

    //d'autres méthodes 
}

