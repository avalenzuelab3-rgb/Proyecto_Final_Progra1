package domain;

import java.util.ArrayList;
import java.util.List;

public class User {
	//Guarda la información de cada usuario: carnet/ID, nombre, límite de préstamos y préstamos activos.
	private String carnet;
	private String name;
	private int loanLimit;
    private List<Loan> activeLoans;

    // Constructor con límite por defecto
    public User(String carnet, String name) {
        this(carnet, name, 3);
    }

    // Constructor completo
    public User(String carnet, String name, int loanLimit) {
        setCarnet(carnet);
        setName(name);
        setLoanLimit(loanLimit);
        this.activeLoans = new ArrayList<Loan>();
    }
    
    public boolean canBorrow() {
        return activeLoans.size() < loanLimit;
    }

    // Agrega un préstamo activo al usuario
    public void addLoan(Loan loan) {
        if (loan != null) {
            activeLoans.add(loan);
        }
    }

    // Elimina un préstamo activo del usuario
    public void removeLoan(Loan loan) {
        activeLoans.remove(loan);
    }

	
	public String getcarnet() {
		return carnet;
	}
	public void setCarnet(String carnet) {
		if (carnet == null || carnet.trim().isEmpty()) {
            throw new IllegalArgumentException("El carnet o carnet no puede estar vacío.");
        }
		this.carnet = carnet;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
		this.name = name;
	}
	public String getCarnet() {
		return carnet;
	}

	public int getLoanLimit() {
		return loanLimit;
	}
	public void setLoanLimit(int loanLimit) {
        if (loanLimit <= 0) {
            throw new IllegalArgumentException("El límite de préstamos debe ser mayor que 0.");
        }
		this.loanLimit = loanLimit;
	}
	
    @Override
    public String toString() {
        return "Carnet: " + carnet +
               ", Nombre: " + name +
               ", Préstamos activos: " + activeLoans.size() +
               "/" + loanLimit;
    }
	

}
