package com.renatocarvalho.pdf_checker.controllers;

import com.renatocarvalho.pdf_checker.dtos.RequestPdfDTO;
import com.renatocarvalho.pdf_checker.dtos.ResponsePdfDTO;
import com.renatocarvalho.pdf_checker.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/compare")
    public ResponseEntity<ResponsePdfDTO> compare(@RequestBody RequestPdfDTO data) throws Exception {
        byte[] originalBytes = Base64.getDecoder().decode(data.originalBase64());
        byte[] modifiedBytes = Base64.getDecoder().decode(data.modifiedBase64());

        if (!isPdf(originalBytes) || !isPdf(modifiedBytes)) {
            return ResponseEntity.badRequest().body(new ResponsePdfDTO(false, "Arquivo não é um PDF válido."));
        }

        boolean isContentValid = pdfService.isContentEqual(originalBytes, modifiedBytes);
        if (isContentValid) {
            return ResponseEntity.ok(new ResponsePdfDTO(true, "O conteúdo dos PDFs são iguais."));
        } else {
            byte[] diffPdfBytes = pdfService.generateVisualDiffPdf(originalBytes, modifiedBytes);
            String base64DiffPdf = Base64.getEncoder().encodeToString(diffPdfBytes);
            return ResponseEntity.ok(new ResponsePdfDTO(false, "O conteúdo dos PDFs são diferentes.", base64DiffPdf));
        }
    }

    private static boolean isPdf(byte[] fileBytes) {
        if (fileBytes == null || fileBytes.length < 5) {
            return false;
        }
        return fileBytes[0] == 0x25 && // %
                fileBytes[1] == 0x50 && // P
                fileBytes[2] == 0x44 && // D
                fileBytes[3] == 0x46 && // F
                fileBytes[4] == 0x2D;
    }
}