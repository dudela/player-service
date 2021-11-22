package com.intuit.app.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileUpload<T> {
    List<T> parseFile(final MultipartFile file) throws Exception;
}
