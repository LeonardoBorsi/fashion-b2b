package main.domain;

public class Fornitore {
	
	private int id;
	private String ragioneSociale;
	private String indirizzo;
	
	public Fornitore(int id, String ragioneSociale, String indirizzo) {
		this.id = id;
		this.ragioneSociale = ragioneSociale;
		this.indirizzo = indirizzo;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	@Override
	public String toString() {
		return "Fornitore [id=" + id + ", ragioneSociale=" + ragioneSociale + ", indirizzo=" + indirizzo + "]";
	}
	
}
