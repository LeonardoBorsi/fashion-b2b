package main.domain.ordine;

public class ResoOrdine {
	
	private int id;
	private StatoResoOrdine stato;
	private OrdineCliente ordine;
	private String motivazione;
	
	public ResoOrdine(int id, StatoResoOrdine stato, OrdineCliente ordine, String motivazione) {
		super();
		this.id = id;
		this.stato = stato;
		this.ordine = ordine;
		this.motivazione = motivazione;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public StatoResoOrdine getStato() {
		return stato;
	}
	
	public void setStato(StatoResoOrdine stato) {
		this.stato = stato;
	}
	
	public OrdineCliente getOrdine() {
		return ordine;
	}
	
	public void setOrdine(OrdineCliente ordine) {
		this.ordine = ordine;
	}
	
	public String getMotivazione() {
		return motivazione;
	}
	
	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}
	
}
