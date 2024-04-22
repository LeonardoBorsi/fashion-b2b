package main.logic;

import java.sql.SQLException;

import main.domain.articolo.Articolo;
import main.domain.articolo.ArticoloMagazzino;
import main.domain.articolo.ArticoloOrdineCliente;
import main.domain.ordine.OrdineCliente;
import main.domain.ordine.StatoOrdineCliente;
import main.orm.dao.articolo.ArticoloMagazzinoDAO;
import main.orm.dao.articolo.ArticoloOrdineClienteDAO;
import main.orm.dao.ordine.OrdineClienteDAO;
import main.orm.dao.ordine.StatoOrdineClienteDAO;

public class MagazzinoController {

	public static void aggiungiArticoloMagazzino(Articolo articolo, int qnt) throws ClassNotFoundException, SQLException {
		ArticoloMagazzino articoloMagazzino = ArticoloMagazzinoDAO.getArticoloMagazzinoByArticoloID(articolo.getId());
		if(articoloMagazzino != null) {
			articoloMagazzino.addQntDisponibile(qnt);
			ArticoloMagazzinoDAO.updateArticoloMagazzino(articoloMagazzino);
		} else {
			ArticoloMagazzinoDAO.insertArticoloMagazzino(articolo, qnt);
		}
	}

	public static void controllaArticoliDisponibiliOrdineCliente(OrdineCliente ordine) throws ClassNotFoundException, SQLException {
		for(ArticoloOrdineCliente articoloOrdineCliente : ordine.getArticoli()) {
			ArticoloMagazzino articoloMagazzino = ArticoloMagazzinoDAO.getArticoloMagazzinoByArticoloID(articoloOrdineCliente.getId());
			if(articoloMagazzino != null && articoloMagazzino.getQntDisponibile() > 0) {
				int qntOrdinata = articoloOrdineCliente.getQntOrdinataDalCliente();
				int qntDisponibile = articoloMagazzino.getQntDisponibile();
				
				if(qntDisponibile >= qntOrdinata) {
					int qntDisponibileRimanente = qntDisponibile - qntOrdinata;
					articoloOrdineCliente.setQntMagazzino(qntOrdinata);
					ArticoloOrdineClienteDAO.updateArticoloOrdineCliente(ordine.getId(), articoloOrdineCliente);
					articoloMagazzino.setQntDisponibile(qntDisponibileRimanente);
					ArticoloMagazzinoDAO.updateArticoloMagazzino(articoloMagazzino);
				} else {
					articoloOrdineCliente.setQntMagazzino(qntDisponibile);
					ArticoloOrdineClienteDAO.updateArticoloOrdineCliente(ordine.getId(), articoloOrdineCliente);
					articoloMagazzino.setQntDisponibile(0);
					ArticoloMagazzinoDAO.updateArticoloMagazzino(articoloMagazzino);
				}
			}
		}
		
		StatoOrdineCliente statoControllato = StatoOrdineClienteDAO.getStatoOrdineCliente(2);
		ordine.setStato(statoControllato);
		OrdineClienteDAO.updateOrdineCliente(ordine);
		
	}
	
	public static void registraArticoloOrdineClienteArrivato(OrdineCliente ordine, Articolo articolo, int qntArrivata) throws ClassNotFoundException, SQLException {
		ArticoloOrdineCliente articoloOrdineCliente = ordine.getArticoloOrdineClienteByArticoloId(articolo.getId());
		articoloOrdineCliente.addQntArrivataDalFornitore(qntArrivata);
		ArticoloOrdineClienteDAO.updateArticoloOrdineCliente(ordine.getId(), articoloOrdineCliente);
	}
	
	public static boolean verificaPreparazioneOrdineCliente(OrdineCliente ordine) throws Exception {
		boolean tuttiArticoli = true;
		for(ArticoloOrdineCliente articolo : ordine.getArticoli()) {
			if(articolo.getQntOrdinataDalCliente() != articolo.getQntMagazzino() + articolo.getQntArrivataDalFornitore()) {
				tuttiArticoli = false;
			}
		}
		
		if(tuttiArticoli) {
			StatoOrdineCliente statoDaPreparare = StatoOrdineClienteDAO.getStatoOrdineCliente(4);
			ordine.setStato(statoDaPreparare);
			OrdineClienteDAO.updateOrdineCliente(ordine);
		}
		
		return tuttiArticoli;
	}
	
	public static void preparaOrdineCliente(OrdineCliente ordine) throws Exception {
		if(verificaPreparazioneOrdineCliente(ordine)) {
			StatoOrdineCliente statoPreparato = StatoOrdineClienteDAO.getStatoOrdineCliente(5);
			ordine.setStato(statoPreparato);
			OrdineClienteDAO.updateOrdineCliente(ordine);
		} else {
            throw new Exception("Ordine non dispone di tutti gli articoli per essere preparato"); 
		}
	}
	
	public static void spedisciOrdineCliente(OrdineCliente ordine) throws Exception {
		if(ordine.getStato().getId() == 5) {
			StatoOrdineCliente statoSpedito = StatoOrdineClienteDAO.getStatoOrdineCliente(6);
			ordine.setStato(statoSpedito);
			OrdineClienteDAO.updateOrdineCliente(ordine);
		} else {
            throw new Exception("Ordine non ancora preparato"); 
		}
	}
	
	
}
