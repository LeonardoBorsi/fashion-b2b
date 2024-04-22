package main.domain.articolo;

public class ArticoloOrdineCliente extends Articolo {
	private int qntOrdinataDalCliente;
	private int qntMagazzino;
	private int qntOrdinataAlFornitore;
	private int qntArrivataDalFornitore;
	
	public ArticoloOrdineCliente(int id, Capo capo, Variante variante, Taglia taglia, int qntOrdinataDalCliente, int qntMagazzino, int qntOrdinataAlFornitore, int qntArrivataDalFornitore) {
		super(id, capo, variante, taglia);
		this.qntOrdinataDalCliente = qntOrdinataDalCliente;
		this.qntMagazzino = qntMagazzino;
		this.qntOrdinataAlFornitore = qntOrdinataAlFornitore;
		this.qntArrivataDalFornitore = qntArrivataDalFornitore;
	}
	
	public ArticoloOrdineCliente(int id, Capo capo, Variante variante, Taglia taglia, int qntOrdinataDalCliente) {
		super(id, capo, variante, taglia);
		this.qntOrdinataDalCliente = qntOrdinataDalCliente;
		this.qntMagazzino = 0;
		this.qntOrdinataAlFornitore = 0;
		this.qntArrivataDalFornitore = 0;
	}
	
	public ArticoloOrdineCliente(Articolo articolo, int qntOrdinataDalCliente, int qntMagazzino, int qntOrdinataAlFornitore, int qntArrivataDalFornitore) {
		super(articolo.getId(), articolo.getCapo(), articolo.getVariante(), articolo.getTaglia());
		this.qntOrdinataDalCliente = qntOrdinataDalCliente;
		this.qntMagazzino = qntMagazzino;
		this.qntOrdinataAlFornitore = qntOrdinataAlFornitore;
		this.qntArrivataDalFornitore = qntArrivataDalFornitore;
	}
	
	public ArticoloOrdineCliente(Articolo articolo, int qntOrdinataDalCliente) {
		super(articolo.getId(), articolo.getCapo(), articolo.getVariante(), articolo.getTaglia());
		this.qntOrdinataDalCliente = qntOrdinataDalCliente;
		this.qntMagazzino = 0;
		this.qntOrdinataAlFornitore = 0;
		this.qntArrivataDalFornitore = 0;
	}

	public int getQntOrdinataDalCliente() {
		return qntOrdinataDalCliente;
	}

	public void setQntOrdinataDalCliente(int qntOrdinataDalCliente) {
		this.qntOrdinataDalCliente = qntOrdinataDalCliente;
	}

	public int getQntMagazzino() {
		return qntMagazzino;
	}

	public void setQntMagazzino(int qntMagazzino) {
		this.qntMagazzino = qntMagazzino;
	}

	public int getQntOrdinataAlFornitore() {
		return qntOrdinataAlFornitore;
	}

	public void setQntOrdinataAlFornitore(int qntOrdinataAlFornitore) {
		this.qntOrdinataAlFornitore = qntOrdinataAlFornitore;
	}

	public int getQntArrivataDalFornitore() {
		return qntArrivataDalFornitore;
	}

	public void setQntArrivataDalFornitore(int qntArrivataDalFornitore) {
		this.qntArrivataDalFornitore = qntArrivataDalFornitore;
	}
	
	public void addQntArrivataDalFornitore(int qnt) {
		this.qntArrivataDalFornitore += qnt;
	}
}
