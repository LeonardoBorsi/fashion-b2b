package test.controllers;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import main.domain.Cliente;
import main.domain.articolo.Articolo;
import main.domain.ordine.OrdineCliente;
import main.domain.ordine.OrdineFornitore;
import main.domain.ordine.ResoOrdine;
import main.logic.ClienteController;
import main.logic.FornitoreController;
import main.logic.MagazzinoController;
import main.logic.RepartoCommercialeController;
import main.orm.dao.ordine.OrdineClienteDAO;
import main.orm.dao.ordine.ResoOrdineDAO;
import test.TestUtils;

public class RepartoCommercialeControllerTest {

	@BeforeClass
    public static void populate() {
		try {
			TestUtils.setupDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Test
    public void ordinaArticoliMancanti() {
        System.out.println("Reparto Commerciale Test > Ordina Capi Mancanti");
		try {
			Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	
        	MagazzinoController.aggiungiArticoloMagazzino(articolo, 5);
        	
        	OrdineCliente ordine1 = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 3));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine1);
        	RepartoCommercialeController.ordinaArticoliMancantiOrdineCliente(ordine1);
        	
        	assertEquals(ordine1.getStato().getId(), 4);
        	assertEquals(ordine1.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntMagazzino(), 3);
        	assertEquals(ordine1.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntOrdinataAlFornitore(), 0);
        	assertEquals(ordine1.getOrdiniFornitori().isEmpty(), true);
        
        	OrdineCliente ordine1DB = OrdineClienteDAO.getOrdineCliente(ordine1.getId());
        	
        	assertEquals(ordine1DB.getStato().getId(), 4);
        	assertEquals(ordine1DB.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntMagazzino(), 3);
        	assertEquals(ordine1DB.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntOrdinataAlFornitore(), 0);
        	assertEquals(ordine1DB.getOrdiniFornitori().isEmpty(), true);
   
        	OrdineCliente ordine2 = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 5));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine2);
        	RepartoCommercialeController.ordinaArticoliMancantiOrdineCliente(ordine2);
        	
        	assertEquals(ordine2.getStato().getId(), 3);
        	assertEquals(ordine2.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntMagazzino(), 2);
        	assertEquals(ordine2.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntOrdinataAlFornitore(), 3);
        	assertEquals(ordine2.getOrdiniFornitori().getFirst().getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntOrdinata(), 3);
        	
        	OrdineCliente ordine2DB = OrdineClienteDAO.getOrdineCliente(ordine2.getId());
        	
        	assertEquals(ordine2DB.getStato().getId(), 3);
        	assertEquals(ordine2DB.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntMagazzino(), 2);
        	assertEquals(ordine2DB.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntOrdinataAlFornitore(), 3);
        	assertEquals(ordine2DB.getOrdiniFornitori().getFirst().getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntOrdinata(), 3);
        	
        	OrdineCliente ordine3 = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 5));

        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine3);
        	RepartoCommercialeController.ordinaArticoliMancantiOrdineCliente(ordine3);
        	
        	assertEquals(ordine3.getStato().getId(), 3);
        	assertEquals(ordine3.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntMagazzino(), 0);
        	assertEquals(ordine3.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntOrdinataAlFornitore(), 5);
        	assertEquals(ordine3.getOrdiniFornitori().getFirst().getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntOrdinata(), 5);
        	
        	OrdineCliente ordine3DB = OrdineClienteDAO.getOrdineCliente(ordine3.getId());
        	
        	assertEquals(ordine3DB.getStato().getId(), 3);
        	assertEquals(ordine3DB.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntMagazzino(), 0);
        	assertEquals(ordine3DB.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntOrdinataAlFornitore(), 5);
        	assertEquals(ordine3DB.getOrdiniFornitori().getFirst().getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntOrdinata(), 5);
        	
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    public void verificaSpedizioneArticoliOrdineClienteDaFornitori() {
        System.out.println("Reparto Commerciale Test > Verifica Spedizione Articoli Ordine Cliente da Fornitori");
		try {
			Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 3));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine);
        	RepartoCommercialeController.ordinaArticoliMancantiOrdineCliente(ordine);
        	
        	OrdineFornitore ordineFornitore = ordine.getOrdiniFornitori().getFirst();
        	
        	assertEquals(RepartoCommercialeController.verificaSpedizioneDaFornitori(ordine), false);
        	
        	FornitoreController.produciArticolo(ordine, ordineFornitore, articolo, 1);
        	FornitoreController.spedisciArticolo(ordine, ordineFornitore, articolo, 1);

        	assertEquals(RepartoCommercialeController.verificaSpedizioneDaFornitori(ordine), true);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    public void annullaOrdine() {
        System.out.println("Reparto Commerciale Test > Annulla Ordine");
		try {
			Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 3));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine);
        	RepartoCommercialeController.ordinaArticoliMancantiOrdineCliente(ordine);
        	ClienteController.richiediAnnullamentoOrdine(ordine);
        	
        	RepartoCommercialeController.annullaOrdine(ordine);

        	assertEquals(ordine.getStato().getId(), 8);
        	
        	OrdineCliente ordineDB = OrdineClienteDAO.getOrdineCliente(ordine.getId());

        	assertEquals(ordineDB.getStato().getId(), 8);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    public void approvaResoOrdine() {
        System.out.println("Reparto Commerciale Test > Approva Reso Ordine");
		try {
			Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	
        	MagazzinoController.aggiungiArticoloMagazzino(articolo, 1);
        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 1));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine);
        	MagazzinoController.preparaOrdineCliente(ordine);
        	MagazzinoController.spedisciOrdineCliente(ordine);
        	
        	String motivazioneReso = "Difettoso";
        	
        	ClienteController.richiediResoOrdine(ordine, motivazioneReso);
        	RepartoCommercialeController.approvaResoOrdine(ordine);
        	
        	ResoOrdine reso = ResoOrdineDAO.getResoOrdineByOrdineClienteId(ordine.getId());
        	
        	assertEquals(reso.getStato().getId(), 2);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
