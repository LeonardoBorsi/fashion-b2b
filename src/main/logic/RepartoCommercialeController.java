package main.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.domain.Fornitore;
import main.domain.articolo.Articolo;
import main.domain.articolo.ArticoloOrdineCliente;
import main.domain.articolo.ArticoloOrdineFornitore;
import main.domain.ordine.OrdineCliente;
import main.domain.ordine.OrdineFornitore;
import main.domain.ordine.ResoOrdine;
import main.domain.ordine.StatoOrdineCliente;
import main.domain.ordine.StatoOrdineFornitore;
import main.domain.ordine.StatoResoOrdine;
import main.orm.dao.articolo.ArticoloOrdineClienteDAO;
import main.orm.dao.ordine.OrdineClienteDAO;
import main.orm.dao.ordine.OrdineFornitoreDAO;
import main.orm.dao.ordine.ResoOrdineDAO;
import main.orm.dao.ordine.StatoOrdineClienteDAO;
import main.orm.dao.ordine.StatoOrdineFornitoreDAO;
import main.orm.dao.ordine.StatoResoOrdineDAO;

public class RepartoCommercialeController {
	
	public static void ordinaArticoliMancantiOrdineCliente(OrdineCliente ordine) throws Exception {
		if(ordine.getStato().getId() == 2) {
			Map<Fornitore, List<ArticoloOrdineFornitore>> articoliFornitoriMap = new HashMap<>();
			
			for(ArticoloOrdineCliente articoloOrdineCliente : ordine.getArticoli()) {
				if (articoloOrdineCliente.getQntMagazzino() < articoloOrdineCliente.getQntOrdinataDalCliente()) {
					Fornitore fornitore = articoloOrdineCliente.getCapo().getFornitore();
					
					int qntDaOrdinareAlFornitore = articoloOrdineCliente.getQntOrdinataDalCliente() - articoloOrdineCliente.getQntMagazzino();
					articoloOrdineCliente.setQntOrdinataAlFornitore(qntDaOrdinareAlFornitore);
					ArticoloOrdineClienteDAO.updateArticoloOrdineCliente(ordine.getId(), articoloOrdineCliente);
					ArticoloOrdineFornitore articoloOrdineFornitore = new ArticoloOrdineFornitore(
						(Articolo) articoloOrdineCliente,
						qntDaOrdinareAlFornitore
					);
				
					if(articoliFornitoriMap.containsKey(fornitore)) {
						articoliFornitoriMap.get(fornitore).add(articoloOrdineFornitore);
					} else {
						List<ArticoloOrdineFornitore> articoliFornitore = new ArrayList<>();
						articoliFornitore.add(articoloOrdineFornitore);
						articoliFornitoriMap.put(fornitore, articoliFornitore);
					}	
				}
			}
			
			if(articoliFornitoriMap.isEmpty()) {
				StatoOrdineCliente statoDaPreparare = StatoOrdineClienteDAO.getStatoOrdineCliente(4);
				ordine.setStato(statoDaPreparare);
				OrdineClienteDAO.updateOrdineCliente(ordine);
			} else {
				List<OrdineFornitore> ordiniFornitoriEffettuati = new ArrayList<>();
				
				for(Map.Entry<Fornitore, List<ArticoloOrdineFornitore>> articoliFornitoriMapEntry : articoliFornitoriMap.entrySet()) {
					Fornitore fornitore = articoliFornitoriMapEntry.getKey();
					List<ArticoloOrdineFornitore> articoliDaOrdinare = articoliFornitoriMapEntry.getValue();
					StatoOrdineFornitore statoDaProdurre = StatoOrdineFornitoreDAO.getStatoOrdineFornitore(1);
					ordiniFornitoriEffettuati.add(OrdineFornitoreDAO.insertOrdineFornitore(statoDaProdurre, fornitore, ordine, articoliDaOrdinare));
				}
				
				ordine.setOrdiniFornitori(ordiniFornitoriEffettuati);
				
				StatoOrdineCliente statoOrdinatoAiFornitori = StatoOrdineClienteDAO.getStatoOrdineCliente(3);
				ordine.setStato(statoOrdinatoAiFornitori);
				OrdineClienteDAO.updateOrdineCliente(ordine);	
			}
		} else {
			throw new Exception("Ordine non controllato o già ordinato"); 
		}
	}
	
	public static boolean verificaSpedizioneDaFornitori(OrdineCliente ordine) throws Exception {
		if(ordine.getOrdiniFornitori().isEmpty())
			return false;	
		for(OrdineFornitore ordineFornitore : ordine.getOrdiniFornitori()) {
			for(ArticoloOrdineFornitore articolo : ordineFornitore.getArticoli()) {
				if(articolo.getQntSpedita() > 0) {
					return true;
				}
			}
		}
		return false;
	}
	

	public static void annullaOrdine(OrdineCliente ordine) throws Exception {
		if(verificaSpedizioneDaFornitori(ordine)) {
			throw new Exception("Impossibile annullare l'ordine, i fornitori hanno già spedito degli articoli"); 
		} else if (ordine.getStato().getId() != 7) {
			throw new Exception("Richiesta annullamento ordine non effettuata"); 
		} else {
			StatoOrdineCliente statoAnnullato = StatoOrdineClienteDAO.getStatoOrdineCliente(8);
			ordine.setStato(statoAnnullato);
			OrdineClienteDAO.updateOrdineCliente(ordine);
		}
	}
	
	public static void approvaResoOrdine(OrdineCliente ordine) throws Exception {
		ResoOrdine reso = ResoOrdineDAO.getResoOrdineByOrdineClienteId(ordine.getId());
		if(reso != null) {
			StatoResoOrdine statoResoApprovato = StatoResoOrdineDAO.getStatoResoOrdine(2);
			reso.setStato(statoResoApprovato);
			ResoOrdineDAO.updateResoOrdine(reso);
		} else {
			throw new Exception("Non esiste alcun reso per l'ordine");
		}
	}
	
}
