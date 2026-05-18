package domain;

public class Book extends Material{
	//Representa un libro normal de la biblioteca: código, título, autor, año y disponibilidad.	
    private String autor;

    public Book(String title, String autor, int pages, int code, int year, boolean available) {
    	super(title, code, year, available, pages);
        this.autor = autor;
    }

    public boolean isLarge() { //esLargo?
        return super.getPages() > 300;
    }
    
    @Override
    public double calculateFine() {
		return 0; //TODO Por ahora lo dejo asi mientras se implementa clase User
    	
    }

    
    public String view() { //mostrar
		return "Título: " + getTitle() + ", Autor: " + autor + ", Páginas: " + getPages()
        		+", Año: " + getYear() + ", Codigo de Libro: " + getCode() + ", Disponibilidad " + isAvailable();
    }

}

