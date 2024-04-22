package main.domain.articolo;

public class ArticoloMagazzino extends Articolo {

	private int qntDisponibile;
	
	public ArticoloMagazzino(int id, Capo capo, Variante variante, Taglia taglia, int qntDisponibile) {
		super(id, capo, variante, taglia);
		this.qntDisponibile = qntDisponibile;
	}
	
	public ArticoloMagazzino(Articolo articolo, int qntDisponibile) {
		super(articolo.getId(), articolo.getCapo(), articolo.getVariante(), articolo.getTaglia());
		this.qntDisponibile = qntDisponibile;
	}
	
	public int getQntDisponibile() {
		return qntDisponibile;
	}

	public void setQntDisponibile(int qntDisponibile) {
		this.qntDisponibile = qntDisponibile;
	}
	
	public void addQntDisponibile(int qnt) {
		this.qntDisponibile += qnt;
	}
	
}
