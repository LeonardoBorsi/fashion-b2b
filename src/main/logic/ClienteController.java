package main.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.Cliente;
import main.domain.articolo.Articolo;
import main.domain.articolo.ArticoloOrdineCliente;
import main.domain.ordine.OrdineCliente;
import main.domain.ordine.ResoOrdine;
import main.domain.ordine.StatoOrdineCliente;
import main.domain.ordine.StatoResoOrdine;
import main.orm.dao.ordine.OrdineClienteDAO;
import main.orm.dao.ordine.ResoOrdineDAO;
import main.orm.dao.ordine.StatoOrdineClienteDAO;
import main.orm.dao.ordine.StatoResoOrdineDAO;

public class ClienteController {
	
	public static OrdineCliente effettuaOrdine(Cliente cliente, Map<Articolo, Integer> articoliCarrello) throws ClassNotFoundException, SQLException {
		List<ArticoloOrdineCliente> articoliOrdine = new ArrayList<>();
		
		for(Map.Entry<Articolo, Integer> articoloCarrello : articoliCarrello.entrySet()) {
			Articolo articolo = articoloCarrello.getKey();
			int qnt = articoloCarrello.getValue();
			articoliOrdine.add(new ArticoloOrdineCliente(articolo, qnt));
		}
		
		StatoOrdineCliente statoIniziale = StatoOrdineClienteDAO.getStatoOrdineCliente(1);
		return OrdineClienteDAO.insertOrdineCliente(statoIniziale, cliente, articoliOrdine);
	}
	
	public static void richiediAnnullamentoOrdine(OrdineCliente ordine) throws ClassNotFoundException, SQLException {
		StatoOrdineCliente statoRichiestaAnnullamento = StatoOrdineClienteDAO.getStatoOrdineCliente(7);
		ordine.setStato(statoRichiestaAnnullamento);
		OrdineClienteDAO.updateOrdineCliente(ordine);
	}
	
	public static ResoOrdine richiediResoOrdine(OrdineCliente ordine, String motivazione) throws Exception {
		if(ordine.getStato().getId() == 6) {
			StatoResoOrdine statoInApprovazione = StatoResoOrdineDAO.getStatoResoOrdine(1);
			return ResoOrdineDAO.insertResoOrdine(statoInApprovazione, ordine, motivazione);
		} else {
			throw new Exception("Ordine non ancora spedito o annullato");
		}
	}
	
	public static void effettuaResoOrdine(OrdineCliente ordine) throws Exception {
		ResoOrdine reso = ResoOrdineDAO.getResoOrdineByOrdineClienteId(ordine.getId());
		if(reso.getStato().getId() == 2) {
			StatoResoOrdine statoResoEffettuato = StatoResoOrdineDAO.getStatoResoOrdine(3);
			reso.setStato(statoResoEffettuato);
			ResoOrdineDAO.updateResoOrdine(reso);
		} else {
			throw new Exception("Reso non ancora approvato");
		}
	}

}
