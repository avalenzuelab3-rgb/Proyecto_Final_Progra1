package domain;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Material> materials;
    private List<User> users;
    private List<Loan> loans;

    public Library() {
        materials = new ArrayList<Material>();
        users = new ArrayList<User>();
        loans = new ArrayList<Loan>();
    }

    public void registerMaterial(Material m) {
        if (m != null && findMaterialByCode(m.getCode()) == null) {
            materials.add(m);
        }
    }

    public void registerUser(User u) {
        if (u != null && findUserById(u.getId()) == null) {
            users.add(u);
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

        material.setAvailable(false);

        return true;
    }

    public boolean returnMaterial(int materialCode) {
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);

            if (loan.getMaterial().getCode() == materialCode) {
                loan.getMaterial().setAvailable(true);
                loans.remove(i);
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
            if (id != null && id.equals(user.getId())) {
                return user;
            }
        }

        return null;
    }

    public int countUserLoans(User user) {
        int counter = 0;

        for (Loan loan : loans) {
            if (loan.getUser().getId().equals(user.getId())) {
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