package test.controllers;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import main.domain.Cliente;
import main.domain.articolo.Articolo;
import main.domain.ordine.OrdineCliente;
import main.domain.ordine.OrdineFornitore;
import main.logic.ClienteController;
import main.logic.FornitoreController;
import main.logic.MagazzinoController;
import main.logic.RepartoCommercialeController;
import main.orm.dao.ordine.OrdineFornitoreDAO;
import test.TestUtils;

public class FornitoreControllerTest {
	
	@BeforeClass
    public static void populate() {
		try {
			TestUtils.setupDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Test
    public void produciArticolo() {
        System.out.println("Fornitore Contoller Test > Produci Articolo");
		try {
			Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 5));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine);
        	RepartoCommercialeController.ordinaArticoliMancantiOrdineCliente(ordine);
        	
        	OrdineFornitore ordineFornitore = ordine.getOrdiniFornitori().getFirst();
        	
        	FornitoreController.produciArticolo(ordine, ordineFornitore, articolo, 2);
        	
        	assertEquals(ordineFornitore.getStato().getId(), 2);
        	assertEquals(ordineFornitore.getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntProdotta(), 2);
        	
        	FornitoreController.produciArticolo(ordine, ordineFornitore, articolo, 3);
        	
        	assertEquals(ordineFornitore.getStato().getId(), 2);
        	assertEquals(ordineFornitore.getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntProdotta(), 5);
        	
        	OrdineFornitore ordineFornitoreDB = OrdineFornitoreDAO.getOrdineFornitore(ordineFornitore.getId());
        	
        	assertEquals(ordineFornitoreDB.getStato().getId(), 2);
        	assertEquals(ordineFornitoreDB.getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntProdotta(), 5);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    public void spedisciArticolo() {
        System.out.println("Fornitore Contoller Test > Spedisci Articolo");
		try {
			Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 5));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine);
        	RepartoCommercialeController.ordinaArticoliMancantiOrdineCliente(ordine);
        	
        	OrdineFornitore ordineFornitore = ordine.getOrdiniFornitori().getFirst();
        	
        	FornitoreController.produciArticolo(ordine, ordineFornitore, articolo, 5);
        	
        	FornitoreController.spedisciArticolo(ordine, ordineFornitore, articolo, 3);
        	
        	assertEquals(ordineFornitore.getStato().getId(), 2);
        	assertEquals(ordineFornitore.getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntSpedita(), 3);
        	
        	FornitoreController.spedisciArticolo(ordine, ordineFornitore, articolo, 2);

        	assertEquals(ordineFornitore.getStato().getId(), 3);
        	assertEquals(ordineFornitore.getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntSpedita(), 5);
        	
        	OrdineFornitore ordineFornitoreDB = OrdineFornitoreDAO.getOrdineFornitore(ordineFornitore.getId());
        	
        	assertEquals(ordineFornitoreDB.getStato().getId(), 3);
        	assertEquals(ordineFornitoreDB.getArticoloOrdineFornitoreByArticoloId(articolo.getId()).getQntSpedita(), 5);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
