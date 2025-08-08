package com.renatocarvalho.pdf_checker.dtos;

public record ResponsePdfDTO(boolean isContentValid, String message, String base64DiffPdf) {

    public ResponsePdfDTO(boolean isContentValid, String message) {
        this(isContentValid, message, null);
    }
}
