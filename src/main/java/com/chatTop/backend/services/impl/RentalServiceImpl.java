package com.chatTop.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.chatTop.backend.entities.Rental;
import com.chatTop.backend.repository.RentalRepository;

import jakarta.transaction.Transactional;
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
@Validated
public class RentalServiceImpl {

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
    @Transactional
    public Rental createRental(Rental rental, MultipartFile picture) throws IOException {
        try {
            System.out.println("Saving rental: " + rental);

            // Traiter l'image
            String pictureUrl = processRentalPicture(picture, null);
            rental.setPicture(pictureUrl);
            rental.setCreated_at(new Date());
            System.out.println("Saving rental with picture URL: " + pictureUrl);

            // Vérifier si les données de base du rental sont valides
            if (rental.getName() == null || rental.getPrice() == null) {
                throw new IllegalArgumentException("Rental name and price cannot be null");
            }
            if (rental.getSurface() == null) { // Retirer la vérification sur description
                throw new IllegalArgumentException("Rental surface cannot be null");
            }
            
            // Sauvegarder le rental
            Rental savedRental = rentalRepository.save(rental);

            System.out.println("Saved rental ID: " + savedRental.getId());

            return savedRental;
        } catch (IOException e) {
            System.err.println("Error processing image: " + e.getMessage());
            throw new IOException("Failed to process image or save rental", e);
        } catch (Exception e) {
            System.err.println("Error saving rental: " + e.getMessage());
            throw new RuntimeException("Failed to save rental: " + e.getMessage(), e);
        }
    }
    @Transactional
 
    public Rental updateRental(Long rental_id, Rental rental, MultipartFile picture) throws IOException {
        // Récupérer l'entité existante
        Optional<Rental> existingRentalOpt = rentalRepository.findById(rental_id);
        if (existingRentalOpt.isEmpty()) {
            throw new IOException("The rental you are trying to modify does not exist");
        }
        Rental existingRental = existingRentalOpt.get();

        // Mettre à jour les champs de l'entité avec les valeurs de l'objet Rental
        existingRental.setName(rental.getName());
        existingRental.setSurface(rental.getSurface());
        existingRental.setPrice(rental.getPrice());
        existingRental.setDescription(rental.getDescription());
        existingRental.setUpdated_at(new Date());

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
            // Vérification de la taille du fichier
            if (picture.getSize() > MAX_FILE_SIZE) {
                throw new IOException("File size exceeds the maximum allowed size of " + MAX_FILE_SIZE + " bytes");
            }

            // Vérifier l'extension du fichier
            String originalFileName = picture.getOriginalFilename();
            if (originalFileName != null) {
                // Nettoyer le nom de fichier
                originalFileName = cleanFileName(originalFileName);
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();

                if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
                    throw new IOException("File type not allowed. Allowed types are: " + ALLOWED_EXTENSIONS);
                }

                // Supprimer l'ancienne image si nécessaire
                if (existingRental != null && existingRental.getPicture() != null) {
                    deleteOldPicture(existingRental.getPicture());
                }

                // Sauvegarder l'image
                return savePicture(picture, originalFileName, fileExtension);
            }
        }
        return null; // Si aucune image, retour à null
    }

    // Nettoyer le nom de fichier (remplacer caractères spéciaux)
    private String cleanFileName(String originalFileName) {
        return originalFileName.replaceAll("[ '&]", "_")
                                .replaceAll("[éèêë]", "e")
                                .replaceAll("[àâä]", "a")
                                .replaceAll("[ôö]", "o")
                                .replace("ç", "c")
                                .replaceAll("[ùûü]", "u")
                                .replaceAll("[ïî]", "i")
                                .replace("ÿ", "y");
    }

    // Sauvegarder l'image dans le dossier uploads
    private String savePicture(MultipartFile picture, String originalFileName, String fileExtension) throws IOException {
        // Créer le répertoire uploads s'il n'existe pas
        Path uploadPath = Paths.get("uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Créer un nom de fichier unique pour l'image
        String uniqueFileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFileName);

        // Compresser et enregistrer l'image
        try (InputStream inputStream = picture.getInputStream()) {
            Thumbnails.of(inputStream)
                    .size(960, 639) // Taille de l'image
                    .outputFormat(fileExtension.substring(1))
                    .outputQuality(0.8)
                    .toFile(filePath.toFile());
        }

        // Retourner l'URL de l'image
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(uniqueFileName)
                .toUriString();
    }
    
    

    // Supprimer l'ancienne image
    private void deleteOldPicture(String currentPicturePath) throws IOException {
        try {
            String fileName = Paths.get(new URI(currentPicturePath).getPath()).getFileName().toString();
            Path oldFilePath = Paths.get("uploads").resolve(fileName);
            Files.deleteIfExists(oldFilePath);
        } catch (Exception e) {
            throw new IOException("Failed to delete old image file", e);
        }
    }
}