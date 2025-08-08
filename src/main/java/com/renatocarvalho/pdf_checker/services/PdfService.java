package com.renatocarvalho.pdf_checker.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import de.redsix.pdfcompare.PdfComparator;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;


@Service
public class PdfService {
    public byte[] getTextHash(byte[] pdfBytes) throws Exception {
        PdfReader reader = new PdfReader(pdfBytes);
        StringBuilder fullText = new StringBuilder();

        try {
            TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                String text = PdfTextExtractor.getTextFromPage(reader, i, strategy);
                fullText.append(text);
            }
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(fullText.toString().getBytes(StandardCharsets.UTF_8));
            return md.digest();

        } catch (Exception e) {
            throw new Exception("Falha ao processar o PDF: ", e);
        } finally {
            reader.close();
        }
    }

    public byte[] getFullCotentHash(byte[] pdfBytes) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pdfBytes);
            return md.digest();
        } catch (Exception e) {
            throw new Exception("Falha ao processar o PDF: ", e);
        }
    }

    public byte[] generateVisualDiffPdf(byte[] originalBytes, byte[] modifiedBytes) throws IOException {
        File originalPdf = File.createTempFile("original-", ".pdf");
        File modifiedPdf = File.createTempFile("modified-", ".pdf");
        File diffPdf = new File("difOutput.pdf");

        try (FileOutputStream fosOriginal = new FileOutputStream(originalPdf);
            FileOutputStream fosModified = new FileOutputStream(modifiedPdf)) {
            fosOriginal.write(originalBytes);
            fosModified.write(modifiedBytes);
        }

        new PdfComparator(originalPdf, modifiedPdf).compare().writeTo("difOutput");

        byte[] diffPdfBytes;
        try (FileInputStream fis = new FileInputStream(diffPdf)) {
            diffPdfBytes = fis.readAllBytes();
        }

        System.out.println("diffPdfBytes.length: " + diffPdfBytes.length);


        originalPdf.delete();
        modifiedPdf.delete();
        diffPdf.delete();

        return diffPdfBytes;
    }

    public boolean isContentEqual(byte[] originalBytes, byte[] modifiedBytes) throws Exception {
        byte[] fullHashOriginal = getFullCotentHash(originalBytes);
        byte[] fullHashSigned = getFullCotentHash(modifiedBytes);

        if (!Arrays.equals(fullHashOriginal, fullHashSigned)) {
            return false;
        }

        byte[] textHashOriginal = getTextHash(originalBytes);
        byte[] textHashSigned = getTextHash(modifiedBytes);

        return Arrays.equals(textHashOriginal, textHashSigned);
    }
}