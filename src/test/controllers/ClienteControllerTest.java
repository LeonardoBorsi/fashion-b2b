package test.controllers;

import org.junit.BeforeClass;
import org.junit.Test;

import main.domain.Cliente;
import main.domain.articolo.Articolo;
import main.domain.articolo.ArticoloOrdineCliente;
import main.domain.ordine.OrdineCliente;
import main.domain.ordine.ResoOrdine;
import main.logic.ClienteController;
import main.logic.MagazzinoController;
import main.logic.RepartoCommercialeController;
import main.orm.dao.ordine.OrdineClienteDAO;
import main.orm.dao.ordine.ResoOrdineDAO;
import test.TestUtils;

import static org.junit.Assert.*;


public class ClienteControllerTest {
	
	@BeforeClass
    public static void setup() {
		try {
			TestUtils.setupDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Test
    public void effettuaOrdine() {
        System.out.println("Cliente Controller Test > Effettua Ordine");
        
        try {
        	Cliente cliente = TestUtils.getRandomCliente();
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrello(5, 10));
        	OrdineCliente ordineDB = OrdineClienteDAO.getOrdineCliente(ordine.getId());

        	assertEquals(ordine.getId(), ordineDB.getId());
        	assertEquals(ordine.getStato().getId(), 1);
        	assertEquals(ordine.getStato().getId(), ordineDB.getStato().getId());
        	assertEquals(ordine.getCliente().getId(), cliente.getId());
        	assertEquals(ordine.getCliente().getId(), ordineDB.getCliente().getId());
        	assertEquals(ordine.getArticoli().size(), ordineDB.getArticoli().size());
        	
        	for(ArticoloOrdineCliente articolo : ordine.getArticoli()) {
        		ArticoloOrdineCliente articoloDB = ordineDB.getArticoloOrdineClienteByArticoloId(articolo.getId());
            	assertEquals(articolo.getQntOrdinataDalCliente(), articoloDB.getQntOrdinataDalCliente());
        	}        	
         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@Test
    public void richiediAnnullamentoOrdine() {
        System.out.println("Cliente Controller Test > Richiedi Annullamento Ordine");
        
        try {
        	Cliente cliente = TestUtils.getRandomCliente();
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrello(5, 10));
        	
        	ClienteController.richiediAnnullamentoOrdine(ordine);
        	
        	assertEquals(ordine.getStato().getId(), 7);

        	OrdineCliente ordineDB = OrdineClienteDAO.getOrdineCliente(ordine.getId());

        	assertEquals(ordineDB.getStato().getId(), 7);
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@Test
    public void richiediResoOrdine() {
        System.out.println("Cliente Controller Test > Richiedi Reso Ordine");
        
        try {
        	Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	
        	MagazzinoController.aggiungiArticoloMagazzino(articolo, 1);
        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 1));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine);
        	MagazzinoController.preparaOrdineCliente(ordine);
        	MagazzinoController.spedisciOrdineCliente(ordine);
        	
        	String motivazioneReso = "Difettoso";
        	
        	ResoOrdine reso = ClienteController.richiediResoOrdine(ordine, motivazioneReso);
        	
        	assertEquals(reso.getMotivazione(), motivazioneReso);
        	assertEquals(reso.getOrdine().getId(), ordine.getId());
        	assertEquals(reso.getStato().getId(), 1);
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@Test
    public void effettuaResoOrdine() {
        System.out.println("Cliente Controller Test > Effettua Reso Ordine");
        
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
        	ClienteController.effettuaResoOrdine(ordine);

        	ResoOrdine reso = ResoOrdineDAO.getResoOrdineByOrdineClienteId(ordine.getId());
        	
        	assertEquals(reso.getStato().getId(), 3);
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
