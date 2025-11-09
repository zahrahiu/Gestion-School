package org.example.gschool.Controller;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jakarta.servlet.http.HttpServletResponse;
import org.example.gschool.Entity.Filiere;
import org.example.gschool.Service.FiliereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class PDFController {

    @Autowired
    private FiliereService filiereService;

    @GetMapping("/downloadPDFM")
    public void downloadPdfUM(HttpServletResponse response,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) throws IOException {

        // Set the response headers for PDF download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Liste_Filieres.pdf");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document document = new Document(pdfDoc);

        // Add the title in bold and underline before the table
        Paragraph title = new Paragraph("Liste des Filières")
                .setBold()  // Set text to bold
                .setUnderline()  // Set text to underline
                .setFontSize(16);  // Optional: adjust the font size
        document.add(title);

        // Column widths for the table
        float[] columnWidths = {1, 2, 2, 2, 2, 2};
        Table table = new Table(columnWidths);

        // Add table headers
        addTableHeader(table);

        // Fetch filieres with pagination
        List<Filiere> filieres = filiereService.getFilieresPaginated(page, size);
        System.out.println("Nombre de filières récupérées : " + filieres.size());

        if (filieres.isEmpty()) {
            document.add(new Paragraph("⚠ Aucun module pour l'instant !"));
        } else {
            // Add rows for each filiere
            for (Filiere filiere : filieres) {
                addFiliereRow(table, filiere);
            }
            document.add(table);
        }

        // Close the document
        document.close();
    }

    private void addTableHeader(Table table) {
        table.addCell(createHeaderCell("ID"));
        table.addCell(createHeaderCell("Titre"));
        table.addCell(createHeaderCell("Code"));
        table.addCell(createHeaderCell("La durée d'études"));
        table.addCell(createHeaderCell("Le nombre de semestres"));
        table.addCell(createHeaderCell("Responsable de filière"));
    }

    private Cell createHeaderCell(String text) {
        return new Cell().add(new Paragraph(text)).setBackgroundColor(ColorConstants.LIGHT_GRAY);
    }

    private void addFiliereRow(Table table, Filiere filiere) {
        table.addCell(createCell(String.valueOf(filiere.getId())));
        table.addCell(createCell(filiere.getNom()));
        table.addCell(createCell(filiere.getCode()));

        // Add the duree d'etude and nombre de semestres
        table.addCell(createCell(String.valueOf(filiere.getDureeEtude())));  // Durée d'étude
        table.addCell(createCell(String.valueOf(filiere.getNombreSemestres()))); // Nombre de semestres

        // Add responsable
        table.addCell(createCell(filiere.getResponsable()));
    }

    private Cell createCell(String value) {
        return new Cell().add(new Paragraph(value != null ? value : "-"));
    }


}
