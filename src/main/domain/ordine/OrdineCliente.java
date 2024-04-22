package main.domain.ordine;

import java.util.List;

import main.domain.Cliente;
import main.domain.articolo.ArticoloOrdineCliente;

public class OrdineCliente {
	
	private int id;
	private StatoOrdineCliente stato;
	private Cliente cliente;
	private List<ArticoloOrdineCliente> articoli;
	private List<OrdineFornitore> ordiniFornitori;
	
	public OrdineCliente(int id, StatoOrdineCliente stato, Cliente cliente, List<ArticoloOrdineCliente> articoli ) {
		this.id = id;
		this.stato = stato;
		this.cliente = cliente;
		this.articoli = articoli;
	}
	
	public OrdineCliente(int id, StatoOrdineCliente stato, Cliente cliente, List<ArticoloOrdineCliente> articoli, List<OrdineFornitore> ordiniFornitori) {
		this.id = id;
		this.stato = stato;
		this.cliente = cliente;
		this.articoli = articoli;
		this.ordiniFornitori = ordiniFornitori;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public StatoOrdineCliente getStato() {
		return stato;
	}

	public void setStato(StatoOrdineCliente stato) {
		this.stato = stato;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ArticoloOrdineCliente> getArticoli() {
		return articoli;
	}

	public void setArticoli(List<ArticoloOrdineCliente> articoli) {
		this.articoli = articoli;
	}
	
	public List<OrdineFornitore> getOrdiniFornitori() {
		return ordiniFornitori;
	}

	public void setOrdiniFornitori(List<OrdineFornitore> ordiniFornitori) {
		this.ordiniFornitori = ordiniFornitori;
	}
	
	public OrdineFornitore getOrdineFornitoreById(int idOrdineFornitore) {
		for (int i = 0; i < this.ordiniFornitori.size(); i++) {
			OrdineFornitore ordineFornitore = this.ordiniFornitori.get(i);
	        if (ordineFornitore.getId() == idOrdineFornitore) {
	            return ordineFornitore;
	        }
	    }
	    return null; 
	}

	public ArticoloOrdineCliente getArticoloOrdineClienteByArticoloId(int idArticolo) {
		for (int i = 0; i < this.articoli.size(); i++) {
			ArticoloOrdineCliente articolo = this.articoli.get(i);
	        if (articolo.getId() == idArticolo) {
	            return articolo;
	        }
	    }
	    return null; 
	}
	
}
