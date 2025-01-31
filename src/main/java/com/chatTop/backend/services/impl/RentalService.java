package com.chatTop.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.chatTop.backend.entities.Rental;
import com.chatTop.backend.repository.RentalRepository;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
public class RentalService {

    @Autowired
    RentalRepository rentalRepository;

    static final long MAX_FILE_SIZE = 8 * 1024 * 1024;
    static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp", ".avif", ".apng", ".gif", ".svg");

    
    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    public Optional<Rental> getRentalById(Long rental_id) {
        return rentalRepository.findById(rental_id);
    }

    public Rental createRental(Rental rental, MultipartFile picture) throws IOException {
        String pictureUrl = processRentalPicture(picture, null);
        rental.setPicture(pictureUrl);

        System.out.println("Saving rental: " + rental);
        
        Rental savedRental = rentalRepository.save(rental);

        System.out.println("Saved rental ID: " + savedRental.getId());

        return savedRental;
    }
 
    public Rental updateRental(Long rental_id, Rental rental, MultipartFile picture) throws IOException {
        // Récupérer l'entité existante
        Optional<Rental> existingRentalOpt = rentalRepository.findById(rental_id);
        if (existingRentalOpt.isEmpty()) {
            throw new IOException("The rental you are trying to modify does not exist");
        }
        
        if (rental.getSurface() == null) {
            throw new IllegalArgumentException("Surface cannot be null");
        }
 
        Rental existingRental = existingRentalOpt.get();

        // Mettre à jour les champs de l'entité avec les valeurs de l'objet Rental
        existingRental.setName(rental.getName());
        existingRental.setSurface(rental.getSurface());
        existingRental.setPrice(rental.getPrice());
        existingRental.setDescription(rental.getDescription());
        existingRental.setUpdatedAt(new Date());

        // Traitement de l'image (voir fonction dédiée)
        if (picture != null && !picture.isEmpty()) {
            String pictureUrl = processRentalPicture(picture, existingRental);
            existingRental.setPicture(pictureUrl);
        }

        // Enregistrer les modifications dans la base de données
        return rentalRepository.save(existingRental);
    }

    
    public void deleteRental(Long rental_id) throws IOException {
        // Récupérer l'entité existante
        Optional<Rental> existingRentalOpt = rentalRepository.findById(rental_id);
        if (existingRentalOpt.isEmpty()) {
            throw new IOException("The rental you are trying to delete does not exist");
        }
        Rental existingRental = existingRentalOpt.get();

        // Supprimer l'image associée si elle existe
        String currentPicturePath = existingRental.getPicture();
        if (currentPicturePath != null && !currentPicturePath.isEmpty()) {
            try {
                // Extraire le nom de fichier de l'URL
                String fileName = Paths.get(new URI(currentPicturePath).getPath()).getFileName().toString();
                // Construire le chemin complet en utilisant le répertoire d'uploads
                Path oldFilePath = Paths.get("uploads").resolve(fileName);
                Files.deleteIfExists(oldFilePath);
            } catch (Exception e) {
                throw new IOException("Failed to delete image file", e);
            }
        }

        // Supprimer l'entité du rental de la base de données
        rentalRepository.delete(existingRental);
    }

    String processRentalPicture(MultipartFile picture, Rental existingRental) throws IOException {
        if (picture != null && !picture.isEmpty()) {
            // Vérifier la taille du fichier
            if (picture.getSize() > MAX_FILE_SIZE) {
                throw new IOException("File size exceeds the maximum allowed size of "+MAX_FILE_SIZE+" MB");
            }

            // Vérifier l'extension du fichier
            String originalFileName = picture.getOriginalFilename();
            if (originalFileName != null) {
                // Remplacer les caractères gênants dans les urls
                originalFileName = originalFileName.replaceAll("[ '&]", "_");
                originalFileName = originalFileName.replaceAll("[éèêë]", "e");
                originalFileName = originalFileName.replaceAll("[àâä]", "a");
                originalFileName = originalFileName.replaceAll("[ôö]", "o");
                originalFileName = originalFileName.replace("ç", "c");
                originalFileName = originalFileName.replaceAll("[ùûü]", "u");
                originalFileName = originalFileName.replaceAll("[ïî]", "i");
                originalFileName = originalFileName.replace("ÿ", "y");

                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
                if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                    throw new IOException("File type not allowed. Allowed types are: " + ALLOWED_EXTENSIONS);
                }

                // Supprimer l'ancienne image si elle existe
                if(existingRental != null){
                    String currentPicturePath = existingRental.getPicture();
                    if (currentPicturePath != null && !currentPicturePath.isEmpty()) {
                        try {
                            // Extraire le nom de fichier de l'URL
                            String fileName = Paths.get(new URI(currentPicturePath).getPath()).getFileName().toString();
                            // Construire le chemin complet en utilisant le répertoire d'uploads
                            Path oldFilePath = Paths.get("uploads").resolve(fileName);
                            System.out.println(oldFilePath);
                            Files.deleteIfExists(oldFilePath);
                        } catch (Exception e) {
                            throw new IOException("Failed to delete old image file", e);
                        }
                    }
                }

                // Assurer que le répertoire d'uploads existe
                String uploadDir = "uploads";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Générer un nom de fichier unique en utilisant le nom du fichier original et l'horodatage actuel
                String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf("."));
                String uniqueFileName = fileNameWithoutExtension + "_" + System.currentTimeMillis() + fileExtension;

                // Enregistrer le fichier dans le répertoire d'uploads
                Path filePath = uploadPath.resolve(uniqueFileName);

                // Compresser et redimensionner l'image en utilisant la librairie Thumbnailator
                try (InputStream inputStream = picture.getInputStream()) {
                    Thumbnails.of(inputStream)
                            .size(960, 639) // taille de l'img std donnée par les développeur front
                            .outputFormat(fileExtension.substring(1)) // enlever le point
                            .outputQuality(0.8)
                            .toFile(filePath.toFile());
                }

                // Définir l'URL de téléchargement du fichier dans le RentalDTO
                return ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(uniqueFileName)
                        .toUriString();
            }
        }
        return null;
    }
}