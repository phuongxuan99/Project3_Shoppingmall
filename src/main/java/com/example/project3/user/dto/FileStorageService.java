package com.example.project3.user.dto;

import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads"; // 디렉토리 경로를 설정합니다.

    public String storeFile(MultipartFile file) throws IOException {
        // 업로드 디렉토리가 존재하지 않는 경우 생성합니다.
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 파일 이름을 UUID와 원래 파일 이름을 결합하여 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath);
        } catch (IOException e) {
            throw new IOException("Failed to store file: " + fileName, e);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

        // 저장된 파일의 URL 반환 (디렉토리 경로와 파일 이름 결합)
        return filePath.toString();
    }

}
