package com.wildcodeschool.wizardsSchool.repositories;

import com.wildcodeschool.wizardsSchool.entities.School;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SchoolRepository {

    private final static String DB_URL = "jdbc:mysql://localhost:3306/wild_db_quest?serverTimezone=GMT";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "___"; //put your mysql password

    public static School selectById(int id) {
        try(
                Connection connection = DriverManager.getConnection(
                        DB_URL, DB_USER, DB_PASSWORD
                );
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM school WHERE id=?"
                );
        ) {
            statement.setInt(1, id);

            try(
                    ResultSet resultSet = statement.executeQuery();
            ) {
                if(resultSet.next()){
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
                    String country = resultSet.getString("country");

                    return new School(id, name, capacity, country);
                }
                else {
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, "unknown id in table school"
                    );
                }
            }
        }
        catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "", e
            );
        }
    }

    public static List<School> selectByCountry(String chosenCountry) {
        try(
                Connection connection = DriverManager.getConnection(
                        DB_URL, DB_USER, DB_PASSWORD
                );
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM school WHERE country LIKE ?"
                );
        ) {
            statement.setString(1, chosenCountry);

            try(
                    ResultSet resultSet = statement.executeQuery();
            ) {
                List<School> schools = new ArrayList<>();

                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
                    String country = resultSet.getString("country");

                    schools.add(new School(id, name, capacity, country));
                }
                return schools;
            }
        }
        catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "", e
            );
        }
    }

    public static int insert(
            String name,
            int capacity,
            String country
    ) {
        try(
                Connection connection = DriverManager.getConnection(
                        DB_URL, DB_USER, DB_PASSWORD
                );
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO school (name, capacity, country) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
        ) {
            statement.setString(1, name);
            statement.setInt(2, capacity);
            statement.setString(3, country);

            if (statement.executeUpdate() != 1) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "failed to insert data"
                );
            }

            try(
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    ) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR, "failed to get inserted id"
                    );
                }
            }
        }
        catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, " ", e
            );
        }
    }

    public static int update(
            int id,
            String name,
            int capacity,
            String country
    ) {
        try(
                Connection connection = DriverManager.getConnection(
                        DB_URL, DB_USER, DB_PASSWORD
                );
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE school SET name=?, capacity=?, country=? WHERE id=?"
                );
                ) {
            statement.setString(1, name);
            statement.setInt(2, capacity);
            statement.setString(3, country);
            statement.setInt(4, id);

            return statement.executeUpdate();
        }
        catch(SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "failed to update data", e
            );
        }
    }

}
