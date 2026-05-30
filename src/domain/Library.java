package domain;

import java.util.ArrayList;
import java.util.List;

import persistence.LoanRepository;
import persistence.MaterialRepository;
import persistence.UserRepository;

public class Library {

    private List<Material> materials;
    private List<User> users;
    private List<Loan> loans;

    private MaterialRepository materialRepository;
    private UserRepository userRepository;
    private LoanRepository loanRepository;
//constructor de library
    public Library() {
        materialRepository = new MaterialRepository();
        userRepository = new UserRepository();
        loanRepository = new LoanRepository();

        materials = new ArrayList<Material>();
        users = new ArrayList<User>();
        loans = new ArrayList<Loan>();

        loadData();
    }
//este metodo carga los datos desde los archivos CSV
    private void loadData() {
        materials = materialRepository.load();
        users = userRepository.load();
        loans = loanRepository.load(materials, users);
    }
//guarda todo en un CSV
    private void saveData() {
        materialRepository.save(materials);
        userRepository.save(users);
        loanRepository.save(loans);
    }

    public void registerMaterial(Material material) {
        if (material == null) {
            return;
        }

        if (findMaterialByCode(material.getCode()) != null) {
            return;
        }

        materials.add(material);
        saveData();
    }

    public void registerUser(User user) {
        if (user == null) {
            return;
        }

        if (findUserById(user.getCarnet()) != null) {
            return;
        }

        users.add(user);
        saveData();
    }

    public boolean loanMaterial(int materialCode, String userId) {
        Material material = findMaterialByCode(materialCode);
        User user = findUserById(userId);

        if (material == null) {
            return false;
        }

        if (user == null) {
            return false;
        }

        if (!material.hasAvailableCopies()) {
            return false;
        }

        if (countUserLoans(user) >= user.getLoanLimit()) {
            return false;
        }
//en el try cach ocurre el prestamos real
        try {
            material.borrowCopy();

            Loan loan = new Loan(material, user);
            loans.add(loan);
            user.addLoan(loan);

            saveData();
            return true;

        } catch (IllegalStateException ex) {
            System.err.println("No se pudo prestar el material: " + ex.getMessage());
            return false;
        }
    }

    public boolean returnMaterial(int materialCode) {
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);

            if (loan != null
                    && loan.isActive()
                    && loan.getMaterial().getCode() == materialCode) {

                loan.markAsReturned();
                loan.getMaterial().returnCopy();
                loan.getUser().removeLoan(loan);
                loans.remove(i);

                saveData();
                return true;
            }
        }

        return false;
    }

    public boolean returnMaterial(int materialCode, String userCarnet) {
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);

            if (loan != null
                    && loan.isActive()
                    && loan.getMaterial().getCode() == materialCode
                    && loan.getUser().getCarnet().equals(userCarnet)) {

                loan.markAsReturned();
                loan.getMaterial().returnCopy();
                loan.getUser().removeLoan(loan);
                loans.remove(i);

                saveData();
                return true;
            }
        }

        return false;
    }

    public Material findMaterialByCode(int code) {
        for (Material material : materials) {
            if (material != null && material.getCode() == code) {
                return material;
            }
        }

        return null;
    }

    public User findUserById(String id) {
        if (id == null) {
            return null;
        }

        for (User user : users) {
            if (user != null && id.equals(user.getCarnet())) {
                return user;
            }
        }

        return null;
    }

    public int countUserLoans(User user) {
        int counter = 0;

        if (user == null) {
            return counter;
        }

        for (Loan loan : loans) {
            if (loan != null
                    && loan.isActive()
                    && loan.getUser() != null
                    && loan.getUser().getCarnet().equals(user.getCarnet())) {
                counter++;
            }
        }

        return counter;
    }

    public List<Material> getAvailableMaterials() {
        List<Material> availableMaterials = new ArrayList<Material>();

        for (Material material : materials) {
            if (material != null && material.hasAvailableCopies()) {
                availableMaterials.add(material);
            }
        }

        return availableMaterials;
    }

    public List<Loan> getActiveLoans() {
        List<Loan> activeLoans = new ArrayList<Loan>();

        for (Loan loan : loans) {
            if (loan != null && loan.isActive()) {
                activeLoans.add(loan);
            }
        }

        return activeLoans;
    }

    public boolean canDeleteMaterial(int materialCode) {
        Material material = findMaterialByCode(materialCode);

        if (material == null) {
            return false;
        }

        return material.getBorrowedCopies() == 0;
    }

    public boolean deleteMaterial(int materialCode) {
        Material material = findMaterialByCode(materialCode);

        if (material == null) {
            return false;
        }

        if (material.getBorrowedCopies() > 0) {
            return false;
        }

        materials.remove(material);
        saveData();
        return true;
    }
    
    public boolean addStockToMaterial(int materialCode, int quantity) {
        Material material = findMaterialByCode(materialCode);

        if (material == null) {
            return false;
        }

        if (quantity <= 0) {
            return false;
        }

        material.addStock(quantity);
        saveData();

        return true;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Loan> getLoans() {
        return loans;
    }
}