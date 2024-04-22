package main.domain.articolo;

public class ArticoloOrdineFornitore extends Articolo {
	
	private int qntOrdinata;
	private int qntProdotta;
	private int qntSpedita;
	
	public ArticoloOrdineFornitore(int id, Capo capo, Variante variante, Taglia taglia, int qntOrdinata, int qntProdotta, int qntSpedita) {
		super(id, capo, variante, taglia);
		this.qntOrdinata = qntOrdinata;
		this.qntProdotta = qntProdotta;
		this.qntSpedita = qntSpedita;
	}
	
	public ArticoloOrdineFornitore(int id, Capo capo, Variante variante, Taglia taglia, int qntOrdinata) {
		super(id, capo, variante, taglia);
		this.qntOrdinata = qntOrdinata;
		this.qntProdotta = 0;
		this.qntSpedita = 0;
	}
	
	public ArticoloOrdineFornitore(Articolo articolo, int qntOrdinata, int qntProdotta, int qntSpedita) {
		super(articolo.getId(), articolo.getCapo(), articolo.getVariante(), articolo.getTaglia());
		this.qntOrdinata = qntOrdinata;
		this.qntProdotta = qntProdotta;
		this.qntSpedita = qntSpedita;
	}
	
	public ArticoloOrdineFornitore(Articolo articolo, int qntOrdinata) {
		super(articolo.getId(), articolo.getCapo(), articolo.getVariante(), articolo.getTaglia());
		this.qntOrdinata = qntOrdinata;
		this.qntProdotta = 0;
		this.qntSpedita = 0;
	}

	public int getQntOrdinata() {
		return qntOrdinata;
	}

	public void setQntOrdinata(int qntOrdinata) {
		this.qntOrdinata = qntOrdinata;
	}

	public int getQntProdotta() {
		return qntProdotta;
	}

	public void setQntProdotta(int qntProdotta) {
		this.qntProdotta = qntProdotta;
	}

	public void addQntProdotta(int qnt) {
		this.qntProdotta += qnt;
	}
	
	public int getQntSpedita() {
		return qntSpedita;
	}

	public void setQntSpedita(int qntSpedita) {
		this.qntSpedita = qntSpedita;
	}
	
	public void addQntSpedita(int qnt) {
		this.qntSpedita += qnt;
	}

}
