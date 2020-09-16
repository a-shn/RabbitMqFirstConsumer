package ru.itis.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import ru.itis.models.UserData;

import java.io.File;
import java.io.FileNotFoundException;

public class DischargeLetterGenerator implements LetterGenerator {
    private static final String dest = "/home/xiu-xiu/Desktop/letters/";

    public void generateLetter(UserData userData, String filename) throws FileNotFoundException {
        String filepath = dest + filename;
        File file = new File(filepath);
        file.getParentFile().mkdirs();
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(filepath);
        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);
        // Initialize document
        Document document = new Document(pdf);
        Paragraph p1 = new Paragraph("Discharge");
        Paragraph p2 = new Paragraph("name: " + userData.getName());
        Paragraph p3 = new Paragraph("passport: " + userData.getPassport());
        Paragraph p4 = new Paragraph("age: " + userData.getAge());
        Paragraph p5 = new Paragraph("passport given date: " + userData.getPassportDate());

        // Add Paragraph to document
        document.add(p1);
        document.add(p2);
        document.add(p3);
        document.add(p4);
        document.add(p5);

        //Close document
        document.close();
    }
}
