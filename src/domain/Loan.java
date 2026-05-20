package domain;

public class Loan {
	
    private Material material;
    private User user;

    public Loan(Material material, User user) {
        this.material = material;
        this.user = user;
    }
    
    public Material getMaterial() {
        return material;
    }

    public User getUser() {
        return user;
    }
    
    public void loanStatus() {
    	//se queda asi para mientras
    }
    
    
    //Esta clase que nos dejara ver que usuario pidio y que material fue prestado 
}
