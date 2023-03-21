package com.leonardo.springwebservice.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.leonardo.springwebservice.services.exceptions.FileException;

@Service
public class ImageService {
    
    public BufferedImage getJpgImageFromFile(MultipartFile multipartFile) {
        String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!"png".equals(ext) && !"jpg".equals(ext)) {
            throw new FileException("Somente imagens PNG e JPG são permitidas");
        }
        
        try {
            BufferedImage img = ImageIO.read(multipartFile.getInputStream());
            if ("png".equals(ext)) {
                img = pngToJpg(img);
            }
            return img;
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo");
        }
    }

    public BufferedImage pngToJpg(BufferedImage img) {
        BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
        return jpgImage;
    }

    public InputStream getInputStream(BufferedImage bufferedImage, String extension) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, extension, byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo");
        }
    }

    public BufferedImage cropSquare(BufferedImage bufferedImage) {
        int min = (bufferedImage.getHeight() <= bufferedImage.getWidth()) ? bufferedImage.getHeight() : bufferedImage.getWidth();
        return Scalr.crop(bufferedImage, (bufferedImage.getWidth() / 2) - (min / 2), (bufferedImage.getHeight() / 2) - (min / 2), min, min);
    }

    public BufferedImage resize(BufferedImage bufferedImage, int size) {
        return Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, size);
    }
}
