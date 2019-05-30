package com.wildcodeschool.wizardsSchool.controllers;

import com.wildcodeschool.wizardsSchool.entities.School;
import com.wildcodeschool.wizardsSchool.repositories.SchoolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Controller
@ResponseBody

public class SchoolController {

    private final static String DB_URL = "jdbc:mysql://localhost:3306/wild_db_quest?serverTimezone=GMT";
    private final static String DB_USER = "___"; // put your mysql user
    private final static String DB_PASSWORD = "___"; // put your database password

    @GetMapping("/api/schools")
    public List<School> getSchools(@RequestParam(defaultValue = "%") String chosenCountry) {
        return SchoolRepository.selectByCountry(chosenCountry);

    }

    @PostMapping("api/schools")
    @ResponseStatus(HttpStatus.CREATED)
    public School store(
            @RequestParam String name,
            @RequestParam int capacity,
            @RequestParam String country
    ) {
        int idGeneratedByInsertion = SchoolRepository.insert(
                name, capacity, country
        );
        return SchoolRepository.selectById(
                idGeneratedByInsertion
        );
    }

}
