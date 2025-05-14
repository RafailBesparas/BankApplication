netstat -ano | findstr :8080
taskkill /PID 12345 /F



Add to the project
- Arc42 template for the backend documentation that captures these controllers and their roles in the system architecture
- ClientProfileController governs the client-facing profile module. This is a compliance-critical class in banking applications since it deals with Personally Identifiable Information (PII).
- Must document this controller like a  software engineer working under GDPR, KYC, and auditing requirements.
- How to add KYC Controlls
- How to implement documentation based on standards like GDPR, PCI DSS, and ISO/IEC 27001

### What is BCrypt?
- BCrypt is a cryptographic hashing function specifically designed for securely hashing passwords. It’s widely used in web and enterprise applications to store passwords in a way that resists brute-force and rainbow table attacks 

### Features of the BCrypt
- One-Way Function: Once a password is hashed with BCrypt, it cannot be reversed back into the original password.
- Salting: BCrypt automatically generates a random salt for each password, which is combined with the password before hashing. This ensures that even identical passwords produce different hashes.
- Work Factor (Cost Parameter): BCrypt allows you to configure how computationally expensive the hashing process should be. This is known as the "cost factor", and it determines how many rounds of hashing are performed. The higher the cost, the slower the hash – which helps slow down brute-force attacks.
- Adaptive: As computers become faster, you can increase the cost factor to make it harder for attackers to crack passwords.
- Cross-Platform: Available in many languages like Java (Spring Security), Python (bcrypt library), and others.

# Why use BCrypt
- It protects against dictionary attacks, brute-force attacks, and precomputed rainbow tables.
- It is more secure than older hashing algorithms like MD5 or SHA-1, which are fast and therefore easier for attackers to exploit.

###  2. Relevant Standards for Banking and Financial Applications
- Standard/Guideline	Description	Applicability
- PCI DSS	Payment Card Industry Data Security Standard	If you're handling credit/debit card data
- ISO/IEC 27001	Information Security Management System (ISMS)	General info security management
- ISO/IEC 20022	Messaging standard for financial services	For payment and transaction messaging systems
- PSD2 / Open Banking API Security	EU directive on secure payments & access to bank accounts	If you're building APIs for third-party access
- SOC 2	Controls for security, availability, and confidentiality	For SaaS or cloud-based systems
- OWASP ASVS	Application Security Verification Standard	Technical checklist for app security (ideal for Spring Security apps)
- BAFIN (Germany-specific)	Bundesanstalt für Finanzdienstleistungsaufsicht regulatory requirements	If your system operates in Germany under banking laws