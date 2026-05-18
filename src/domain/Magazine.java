package domain;

public class Magazine extends Material{
	//Representa una revista o segundo tipo de material. Sirve para que la herencia tenga sentido y no sea decorativa.
	private int editionNumber;

	public Magazine(String title, int editionNumber, int code, int year, boolean available, int pages) {
		super(title, code, year, available, pages);
        this.editionNumber = editionNumber;
		
	}
	
	@Override
	public double calculateFine() {
		return 0;
	}

}
