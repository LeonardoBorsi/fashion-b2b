package main.domain.ordine;

import java.util.List;

import main.domain.Fornitore;
import main.domain.articolo.ArticoloOrdineFornitore;

public class OrdineFornitore {

	private int id;
	private StatoOrdineFornitore stato;
	private Fornitore fornitore;
	private List<ArticoloOrdineFornitore> articoli;
	
	public OrdineFornitore(int id, StatoOrdineFornitore stato, Fornitore fornitore, List<ArticoloOrdineFornitore> articoli) {
		this.id = id;
		this.stato = stato;
		this.fornitore = fornitore;
		this.articoli = articoli;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StatoOrdineFornitore getStato() {
		return stato;
	}

	public void setStato(StatoOrdineFornitore stato) {
		this.stato = stato;
	}

	public Fornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}

	public List<ArticoloOrdineFornitore> getArticoli() {
		return articoli;
	}

	public void setArticoli(List<ArticoloOrdineFornitore> articoli) {
		this.articoli = articoli;
	}
	
	public ArticoloOrdineFornitore getArticoloOrdineFornitoreByArticoloId(int idArticolo) {
		for (int i = 0; i < this.articoli.size(); i++) {
			ArticoloOrdineFornitore articolo = this.articoli.get(i);
	        if (articolo.getId() == idArticolo) {
	            return articolo;
	        }
	    }
	    return null; 
	}
	
}
