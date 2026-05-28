package domain;

public class Magazine extends Material {

    private int editionNumber;

    public Magazine(String title, int editionNumber, int code, int year, boolean available, int pages) {
        super(title, code, year, available, pages);
        setEditionNumber(editionNumber);
    }

    public Magazine(String title, int editionNumber, int code, int year, boolean available, int pages, int stock) {
        super(title, code, year, available, pages, stock);
        setEditionNumber(editionNumber);
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public void setEditionNumber(int editionNumber) {
        if (editionNumber <= 0) {
            throw new IllegalArgumentException("El número de edición debe ser mayor a 0.");
        }

        this.editionNumber = editionNumber;
    }

    @Override
    public double calculateFine() {
        return 1.00;
    }

    @Override
    public int daysMaxLoan() {
        return 7;
    }

    public String view() {
        return "Título: " + getTitle()
                + ", Edición: " + editionNumber
                + ", Páginas: " + getPages()
                + ", Año: " + getYear()
                + ", Código de Revista: " + getCode()
                + ", Stock: " + getStock()
                + ", Prestados: " + getBorrowedCopies()
                + ", Disponibles: " + getAvailableCopies();
    }

    @Override
    public String toString() {
        return view();
    }
}