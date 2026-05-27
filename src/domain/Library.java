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

    public Library() {
        materialRepository = new MaterialRepository();
        userRepository = new UserRepository();
        loanRepository = new LoanRepository();

        materials = new ArrayList<Material>();
        users = new ArrayList<User>();
        loans = new ArrayList<Loan>();

        loadData();
    }

    private void loadData() {
        materials = materialRepository.load();
        users = userRepository.load();
        loans = loanRepository.load(materials, users);
    }

    private void saveData() {
        materialRepository.save(materials);
        userRepository.save(users);
        loanRepository.save(loans);
    }

    public void registerMaterial(Material material) {
        if (material != null && findMaterialByCode(material.getCode()) == null) {
            materials.add(material);
            saveData();
        }
    }

    public void registerUser(User user) {
        if (user != null && findUserById(user.getCarnet()) == null) {
            users.add(user);
            saveData();
        }
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

        if (!material.isAvailable()) {
            return false;
        }

        if (countUserLoans(user) >= user.getLoanLimit()) {
            return false;
        }

        Loan loan = new Loan(material, user);

        loans.add(loan);
        user.addLoan(loan);
        material.setAvailable(false);

        saveData();
        return true;
    }

    public boolean returnMaterial(int materialCode) {
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);

            if (loan.getMaterial().getCode() == materialCode && loan.isActive()) {
                loan.markAsReturned();
                loan.getMaterial().setAvailable(true);
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
            if (material.getCode() == code) {
                return material;
            }
        }

        return null;
    }

    public User findUserById(String id) {
        for (User user : users) {
            if (id != null && id.equals(user.getCarnet())) {
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
                    && loan.getUser().getCarnet().equals(user.getCarnet())) {
                counter++;
            }
        }

        return counter;
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