package domain;

public class Magazine extends Material {
    
    // Atributo propio de Magazine
    private int editionNumber;

    // Constructor
    public Magazine(String title, int editionNumber, int code, int year, boolean available, int pages) {
        super(title, code, year, available, pages);
        setEditionNumber(editionNumber);
    }

    // Getter
    public int getEditionNumber() {
        return editionNumber;
    }

    // Setter con validación
    public void setEditionNumber(int editionNumber) {
        if (editionNumber <= 0) {
            throw new IllegalArgumentException("El número de edición debe ser mayor a 0.");
        }
        this.editionNumber = editionNumber;
    }

    // Las revistas tendrán una multa diferente a los libros
    @Override
    public double calculateFine() {
        return 1.00;
    }

    // Las revistas se prestan por menos días que un libro
    @Override
    public int daysMaxLoan() {
        return 7;
    }

    // Método para mostrar la información de la revista
    public String view() {
        return "Título: " + getTitle()
                + ", Edición: " + editionNumber
                + ", Páginas: " + getPages()
                + ", Año: " + getYear()
                + ", Código de Revista: " + getCode()
                + ", Disponibilidad: " + isAvailable();
    }
}