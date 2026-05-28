package domain;

public abstract class Material {

    private String title;
    private int code;
    private int year;
    private int pages;

    private int stock;
    private int borrowedCopies;

    public Material(String title, int code, int year, boolean available, int pages) {
        this(title, code, year, available, pages, available ? 1 : 0);
    }

    public Material(String title, int code, int year, boolean available, int pages, int stock) {
        setTitle(title);
        setCode(code);
        setYear(year);
        setPages(pages);
        setStock(stock);
        this.borrowedCopies = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.title = title.trim();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        if (code <= 0) {
            throw new IllegalArgumentException("El código debe ser mayor que 0.");
        }
        this.code = code;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year < 1500 || year > 2100) {
            throw new IllegalArgumentException("El año debe estar entre 1500 y 2100.");
        }
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        if (pages <= 0) {
            throw new IllegalArgumentException("Las páginas deben ser mayores que 0.");
        }
        this.pages = pages;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        if (stock < borrowedCopies) {
            throw new IllegalArgumentException("El stock no puede ser menor que las copias prestadas.");
        }

        this.stock = stock;
    }

    public int getBorrowedCopies() {
        return borrowedCopies;
    }

    public int getAvailableCopies() {
        return stock - borrowedCopies;
    }

    public boolean hasAvailableCopies() {
        return getAvailableCopies() > 0;
    }

    public boolean isAvailable() {
        return hasAvailableCopies();
    }

    public void setAvailable(boolean available) {
        // Se deja solo para compatibilidad con código viejo.
        // Con stock, la disponibilidad real depende de getAvailableCopies().
    }

    public void borrowCopy() {
        if (!hasAvailableCopies()) {
            throw new IllegalStateException("No hay copias disponibles de este material.");
        }

        borrowedCopies++;
    }

    public void returnCopy() {
        if (borrowedCopies > 0) {
            borrowedCopies--;
        }
    }

    public abstract double calculateFine();

    public abstract int daysMaxLoan();
}