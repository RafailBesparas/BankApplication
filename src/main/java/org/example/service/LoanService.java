package org.example.service;

import org.example.model.AccountModel;
import org.example.model.LoanApplication;
import org.example.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service // Registers this class as a Spring-managed service
public class LoanService {

    // Inject the LoanApplicationRepository in order to perform crud operations for loans
    @Autowired
    private LoanApplicationRepository loanRepository;

    // Folder where uploaded loan documents will be saved
    private static final String DOCUMENT_UPLOAD_DIR = "uploads/documents/";

    // Save a loan application for a user, process document upload and assign a default status as Pending
    public void applyForLoan(AccountModel user, LoanApplication loan, MultipartFile document) {
        if (document != null && !document.isEmpty()) { // Checks if the user uploaded a document
            try {
                Path uploadPath = Paths.get(DOCUMENT_UPLOAD_DIR);  // Creates a path to the upload directory
                if (!Files.exists(uploadPath)) { // If the directory does not exist, create it
                    Files.createDirectories(uploadPath); // Create the directory
                }
                String fileName = System.currentTimeMillis() + "_" + document.getOriginalFilename(); // Creates a unique filename
                Path filePath = uploadPath.resolve(fileName); // Resolves full file path
                document.transferTo(filePath.toFile()); // Saves the file to disk
                loan.setDocumentPath(filePath.toString()); // Stores the path in the loan object
            } catch (IOException e) {
                throw new RuntimeException("Failed to store document", e);// Fails if file saving fails
            }
        }

        loan.setAccount(user); // link the loan to the person who submits it
        loan.setStatus("PENDING"); // Default loan state must be pending
        loanRepository.save(loan); // Save the loan to the database
    }

    // Returns all loans submitted by a specific user
    public List<LoanApplication> getLoansByUser(AccountModel user) {
        return loanRepository.findByAccount(user);
    }
}
