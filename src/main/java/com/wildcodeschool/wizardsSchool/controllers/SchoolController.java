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

    @PutMapping("api/schools/{id}")
    public School update(
            @PathVariable int id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String country
    ) {
        School school = SchoolRepository.selectById(id);
        SchoolRepository.update(
                id,
                name != null ? name : school.getName(),
                capacity != null ? capacity : school.getCapacity(),
                country != null ? country : school.getCountry()
        );
        return SchoolRepository.selectById(id);
    }

}
