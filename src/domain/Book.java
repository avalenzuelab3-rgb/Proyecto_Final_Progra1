package domain;

public class Book extends Material {

    private String autor;

    public Book(String title, String autor, int pages, int code, int year, boolean available) {
        super(title, code, year, available, pages);
        setAutor(autor);
    }

    public Book(String title, String autor, int pages, int code, int year, boolean available, int stock) {
        super(title, code, year, available, pages, stock);
        setAutor(autor);
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }

        this.autor = autor.trim();
    }

    public boolean isLarge() {
        return getPages() > 300;
    }

    @Override
    public double calculateFine() {
        return 1.00;
    }

    @Override
    public int daysMaxLoan() {
        return 15;
    }

    public String view() {
        return "Título: " + getTitle()
                + ", Autor: " + autor
                + ", Páginas: " + getPages()
                + ", Año: " + getYear()
                + ", Código de Libro: " + getCode()
                + ", Stock: " + getStock()
                + ", Prestados: " + getBorrowedCopies()
                + ", Disponibles: " + getAvailableCopies();
    }

    @Override
    public String toString() {
        return view();
    }
}