package com.example.exemplocrud.persistence;

import java.sql.SQLException;

public interface IProfessor {
    public ProfessorDao open() throws SQLException;
    public void close();
}
