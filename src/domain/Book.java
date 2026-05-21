package domain;

public class Book extends Material{
	//Representa un libro normal de la biblioteca: código, título, autor, año y disponibilidad.	
    private String autor;

    public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		if (autor == null || autor.trim().isEmpty()) {
	           throw new IllegalArgumentException("El autor no puede estar vacío.");
	       }
		this.autor = autor.trim();
	}

	public Book(String title, String autor, int pages, int code, int year, boolean available) {
    	super(title, code, year, available, pages);
        this.autor = autor;
    }

    public boolean isLarge() { //esLargo?
        return super.getPages() > 300;
    }
    
    @Override
    public double calculateFine() {
		return 1.00; 
    	
    }
    
    public int daysMaxLoan() {
    	return 15; 
    }

    
    public String view() { //mostrar
		return "Título: " + getTitle() + ", Autor: " + autor + ", Páginas: " + getPages()
        		+", Año: " + getYear() + ", Codigo de Libro: " + getCode() + ", Disponibilidad " + isAvailable();
    }
    
    @Override
    public String toString() {
        return view();
    }

}

