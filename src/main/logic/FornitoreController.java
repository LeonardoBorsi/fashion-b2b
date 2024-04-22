package main.logic;

import java.sql.SQLException;

import main.domain.articolo.Articolo;
import main.domain.articolo.ArticoloOrdineFornitore;
import main.domain.ordine.OrdineCliente;
import main.domain.ordine.OrdineFornitore;
import main.domain.ordine.StatoOrdineFornitore;
import main.orm.dao.articolo.ArticoloOrdineFornitoreDAO;
import main.orm.dao.ordine.OrdineFornitoreDAO;
import main.orm.dao.ordine.StatoOrdineFornitoreDAO;

public class FornitoreController {

	public static void produciArticolo(OrdineCliente ordineCliente, OrdineFornitore ordineFornitore, Articolo articolo, int qntProdotta) throws ClassNotFoundException, SQLException {
		if(ordineFornitore.getStato().getId() != 2) {
			StatoOrdineFornitore statoProduzioneAvviata = StatoOrdineFornitoreDAO.getStatoOrdineFornitore(2);
			ordineFornitore.setStato(statoProduzioneAvviata);
			OrdineFornitoreDAO.updateOrdineFornitore(ordineFornitore);
		}

		ArticoloOrdineFornitore articoloOrdineFornitore = ordineCliente.getOrdineFornitoreById(ordineFornitore.getId()).getArticoloOrdineFornitoreByArticoloId(articolo.getId());
		articoloOrdineFornitore.addQntProdotta(qntProdotta);
		ArticoloOrdineFornitoreDAO.updateArticoloOrdineFornitore(ordineFornitore.getId(), articoloOrdineFornitore);
	}
	
	
	public static void spedisciArticolo(OrdineCliente ordineCliente, OrdineFornitore ordineFornitore, Articolo articolo, int qntDaSpedire) throws Exception {
		ArticoloOrdineFornitore articoloOrdineFornitore = ordineCliente.getOrdineFornitoreById(ordineFornitore.getId()).getArticoloOrdineFornitoreByArticoloId(articolo.getId());
		if(articoloOrdineFornitore.getQntProdotta() < qntDaSpedire) {
			throw new Exception("Impossibile spedire articoli non ancora prodotti");
		} else {
			articoloOrdineFornitore.addQntSpedita(qntDaSpedire);
			ArticoloOrdineFornitoreDAO.updateArticoloOrdineFornitore(ordineFornitore.getId(), articoloOrdineFornitore);
			
			boolean tuttiArticoliSpediti = true;
			for(ArticoloOrdineFornitore a: ordineFornitore.getArticoli()) {
				if(a.getQntProdotta() != a.getQntSpedita()) {
					tuttiArticoliSpediti = false;
				}
			}
			
			if(tuttiArticoliSpediti) {
				StatoOrdineFornitore statoCompletamenteSpedito = StatoOrdineFornitoreDAO.getStatoOrdineFornitore(3);
				ordineFornitore.setStato(statoCompletamenteSpedito);
				OrdineFornitoreDAO.updateOrdineFornitore(ordineFornitore);
			}
		}
	}
}
