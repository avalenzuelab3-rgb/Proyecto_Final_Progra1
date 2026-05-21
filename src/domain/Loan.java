package domain;

import java.time.LocalDate;

public class Loan {

    private Material material;
    private User user;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean active;

    public Loan(Material material, User user) {
        if (material == null) {
            throw new IllegalArgumentException("El material no puede ser nulo.");
        }

        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }

        this.material = material;
        this.user = user;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(material.daysMaxLoan());
        this.returnDate = null;
        this.active = true;
    }

    public Loan(Material material, User user, LocalDate loanDate) {
        if (material == null) {
            throw new IllegalArgumentException("El material no puede ser nulo.");
        }

        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }

        if (loanDate == null) {
            throw new IllegalArgumentException("La fecha de préstamo no puede ser nula.");
        }

        this.material = material;
        this.user = user;
        this.loanDate = loanDate;
        this.dueDate = loanDate.plusDays(material.daysMaxLoan());
        this.returnDate = null;
        this.active = true;
    }

    public Material getMaterial() {
        return material;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isActive() {
        return active;
    }
    
    public int getMaterialCode() {
        return material.getCode();
    }

    public String getUserCarnet() {
        return user.getCarnet();
    }

    public void markAsReturned() {
        this.returnDate = LocalDate.now();
        this.active = false;
    }

    public boolean isOverdue() {
        return active && LocalDate.now().isAfter(dueDate);
    }

    public long getDaysLate() {
        if (!isOverdue()) {
            return 0;
        }

        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    public String getStatus() {
        if (active) {
            if (isOverdue()) {
                return "Vencido";
            }
            return "Activo";
        }

        return "Devuelto";
    }

    @Override
    public String toString() {
        return "Loan{" +
                "material=" + material.getTitle() +
                ", user=" + user.getName() +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", status=" + getStatus() +
                '}';
    }
}