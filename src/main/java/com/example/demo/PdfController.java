package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;

@Controller
@AllArgsConstructor
@RequestMapping("/pdf")
public class PdfController {

    private final TemplateEngine templateEngine;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generatePdf() throws Exception {
        // Create a Thymeleaf context with the template data
        Context context = new Context();
        // Add the data to the context
        context.setVariable("myData", new MyData("John Doe", 30)); // Replace with your data

        // Render HTML using Thymeleaf
        String html = templateEngine.process("index", context);

        // Generate PDF from HTML using Flying Saucer
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(pdfOutputStream);

        // Convert the PDF content to a byte array
        byte[] pdfBytes = pdfOutputStream.toByteArray();

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "generated.pdf"); // Change the file name if needed

        // Return the PDF as a ResponseEntity with appropriate headers
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }
}

@Data
@AllArgsConstructor
class MyData{
    private String name;
    private int age;
}