package ru.itis.services;

import ru.itis.models.UserData;

import java.io.FileNotFoundException;

public interface LetterGenerator {
    void generateLetter(UserData userData, String filename) throws FileNotFoundException;
}
