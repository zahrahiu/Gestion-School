package org.example.gschool.Controller;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import com.itextpdf.layout.properties.TextAlignment;
import jakarta.servlet.http.HttpServletResponse;
import org.example.gschool.Entity.ImageEntity;
import org.example.gschool.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
public class StudentPDFController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/download")
    public void downloadPdfUM(HttpServletResponse response,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) throws IOException {

        // Set the response headers for PDF download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Liste_etudiants.pdf");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document document = new Document(pdfDoc);

        // Add the title in bold and underline before the table
        Paragraph title = new Paragraph("Liste des Etudiants")
                .setBold()  // Set text to bold
                .setUnderline()  // Set text to underline
                .setFontSize(16);  // Optional: adjust the font size
        document.add(title);

        // Column widths for the table
        float[] columnWidths = {1, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        Table table = new Table(columnWidths);

        // Add table headers
        addStudentTableHeader(table);

        // Fetch students with pagination
        List<ImageEntity> students = imageService.getEtudiantsPaginated(page, size);
        System.out.println("Nombre d'étudiants récupérés : " + students.size());

        if (students.isEmpty()) {
            document.add(new Paragraph("⚠ Aucun étudiant pour l'instant !"));
        } else {
            // Add rows for each student
            for (ImageEntity student : students) {
                try {
                    addStudentRow(table, student);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            document.add(table);
        }

        // Close the document
        document.close();
    }

    private void addStudentTableHeader(Table table) {
        table.addCell(createHeaderCell("Image"));
        table.addCell(createHeaderCell("CNE"));
        table.addCell(createHeaderCell("Nom"));
        table.addCell(createHeaderCell("Prénom"));
        table.addCell(createHeaderCell("Email"));
        table.addCell(createHeaderCell("Téléphone"));
        table.addCell(createHeaderCell("Adresse"));
        table.addCell(createHeaderCell("Filière"));
        table.addCell(createHeaderCell("Sexe"));
        table.addCell(createHeaderCell("Ville de Naissance"));
    }

    private void addStudentRow(Table table, ImageEntity student) throws MalformedURLException {
        // Ajouter l'image
        if (student.getData() != null) {
            ImageData imageData = ImageDataFactory.create(student.getData());
            Image pdfImage = new Image(imageData).setWidth(50).setHeight(50); // Ajustez la taille si nécessaire
            table.addCell(new Cell().add(pdfImage));
        } else {
            table.addCell(createCell("Pas d'image"));
        }

        // Ajouter les autres cellules
        table.addCell(createCell(student.getCne()));
        table.addCell(createCell(student.getNom()));
        table.addCell(createCell(student.getPrenom()));
        table.addCell(createCell(student.getEmail()));
        table.addCell(createCell(student.getTelephone()));
        table.addCell(createCell(student.getAdresse()));
        table.addCell(createCell(student.getFiliere()));
        table.addCell(createCell(student.getSexe()));
        table.addCell(createCell(student.getVilleDenaissance()));
    }

    private Cell createHeaderCell(String text) {
        return new Cell().add(new Paragraph(text))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell createCell(String value) {
        return new Cell().add(new Paragraph(value != null ? value : "-"))
                .setTextAlignment(TextAlignment.CENTER);
    }
}