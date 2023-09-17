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
        Context context = new Context();
        context.setVariable("myData", new MyData("John Doe", 30));

        // Render HTML using Thymeleaf
        String html = templateEngine.process("cv-template", context);

        // Generate PDF from HTML using Flying Saucer
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(pdfOutputStream);
        byte[] pdfBytes = pdfOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "generated.pdf");

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