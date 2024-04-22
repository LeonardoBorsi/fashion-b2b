package test.controllers;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import main.domain.Cliente;
import main.domain.articolo.Articolo;
import main.domain.articolo.ArticoloMagazzino;
import main.domain.ordine.OrdineCliente;
import main.logic.ClienteController;
import main.logic.MagazzinoController;
import main.orm.Database;
import main.orm.dao.articolo.ArticoloMagazzinoDAO;
import main.orm.dao.ordine.OrdineClienteDAO;
import test.TestUtils;

public class MagazzinoTest {
		
	@BeforeClass
    public static void populate() {
		try {
			TestUtils.setupDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@BeforeEach
    public static void svuotaMagazzino() {
		try {
			Database.rawUpdate("DELETE FROM " + Database.Table.articoli_magazzino);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	@Test
    public void aggiungiArticoloMagazzino() {
        System.out.println("Magazzino Test > Aggiungi Articolo al Magazzino");
        try {
        	Articolo articolo = TestUtils.getRandomArticolo();
        	
        	MagazzinoController.aggiungiArticoloMagazzino(articolo, 5);
        	
        	ArticoloMagazzino articoloMagazzino1DB = ArticoloMagazzinoDAO.getArticoloMagazzinoByArticoloID(articolo.getId());
        	assertEquals(articoloMagazzino1DB.getQntDisponibile(), 5);
        	
        	MagazzinoController.aggiungiArticoloMagazzino(articolo, 5);
        	
        	ArticoloMagazzino articoloMagazzino2DB = ArticoloMagazzinoDAO.getArticoloMagazzinoByArticoloID(articolo.getId());
        	assertEquals(articoloMagazzino2DB.getQntDisponibile(), 10);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    public void controllaArticoliDisponibiliOrdineCliente() {
        System.out.println("Magazzino Test > Controlla Articoli Disponibili di Ordine Cliente");
        try {
			Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	
        	MagazzinoController.aggiungiArticoloMagazzino(articolo, 7);
        	
        	OrdineCliente ordine1 = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 5));

        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine1);
        	
        	assertEquals(ordine1.getArticoli().getFirst().getQntMagazzino(), 5);
        	assertEquals(ordine1.getStato().getId(), 2);
        	
        	OrdineCliente ordine1DB = OrdineClienteDAO.getOrdineCliente(ordine1.getId());
        	
        	assertEquals(ordine1DB.getArticoli().getFirst().getQntMagazzino(), 5);
        	assertEquals(ordine1DB.getStato().getId(), 2);
        	
        	ArticoloMagazzino articoloMagazzino1DB = ArticoloMagazzinoDAO.getArticoloMagazzinoByArticoloID(articolo.getId());
        	assertEquals(articoloMagazzino1DB.getQntDisponibile(), 2);
        	
        	OrdineCliente ordine2 = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 3));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine2);
        	
        	assertEquals(ordine2.getArticoli().getFirst().getQntMagazzino(), 2);
        	assertEquals(ordine2.getStato().getId(), 2);
        	
        	OrdineCliente ordine2DB = OrdineClienteDAO.getOrdineCliente(ordine2.getId());
        	
        	assertEquals(ordine2DB.getArticoli().getFirst().getQntMagazzino(), 2);
        	assertEquals(ordine2DB.getStato().getId(), 2);
        	
        	ArticoloMagazzino articoloMagazzino2DB = ArticoloMagazzinoDAO.getArticoloMagazzinoByArticoloID(articolo.getId());
        	assertEquals(articoloMagazzino2DB.getQntDisponibile(), 0);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    public void registraArticoloOrdineClienteArrivato() {
		System.out.println("Magazzino Test > Registra Articolo Ordine Cliente arrivato dal Fornitore");
        try {
        	Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 5));
        	
        	MagazzinoController.registraArticoloOrdineClienteArrivato(ordine, articolo, 3);
        	
        	assertEquals(ordine.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntArrivataDalFornitore(),3);
        	
        	OrdineCliente ordineDB = OrdineClienteDAO.getOrdineCliente(ordine.getId());
        	
        	assertEquals(ordineDB.getArticoloOrdineClienteByArticoloId(articolo.getId()).getQntArrivataDalFornitore(),3);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    public void verificaPreparazioneOrdineCliente() {
		System.out.println("Magazzino Test > Verifica Preparazione Ordine Cliente");
        try {
        	Cliente cliente = TestUtils.getRandomCliente();
        	Articolo articolo = TestUtils.getRandomArticolo();
        	
        	MagazzinoController.aggiungiArticoloMagazzino(articolo, 2);
        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 5));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine);
        	
        	MagazzinoController.registraArticoloOrdineClienteArrivato(ordine, articolo, 2);
        
        	assertEquals(MagazzinoController.verificaPreparazioneOrdineCliente(ordine), false);
        	
        	MagazzinoController.registraArticoloOrdineClienteArrivato(ordine, articolo, 1);
            
        	assertEquals(MagazzinoController.verificaPreparazioneOrdineCliente(ordine), true);
        	
        	assertEquals(ordine.getStato().getId(), 4);
        	
        	OrdineCliente ordineDB = OrdineClienteDAO.getOrdineCliente(ordine.getId());
        	
        	assertEquals(ordineDB.getStato().getId(), 4);
  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    public void preparaOrdineCliente() {
		System.out.println("Magazzino Test > Prepara Ordine Cliente");
        try {
        	Cliente cliente = TestUtils.getRandomCliente();
			Articolo articolo = TestUtils.getRandomArticolo();
			
        	MagazzinoController.aggiungiArticoloMagazzino(articolo, 5);
        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 5));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine);
        	MagazzinoController.verificaPreparazioneOrdineCliente(ordine);
        	MagazzinoController.preparaOrdineCliente(ordine);
        	
        	assertEquals(ordine.getStato().getId(), 5);
        	
        	OrdineCliente ordineDB = OrdineClienteDAO.getOrdineCliente(ordine.getId());
        	
        	assertEquals(ordineDB.getStato().getId(), 5);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
    public void spedisciOrdineCliente() {
		System.out.println("Magazzino Test > Spedisci Ordine Cliente");
        try {
        	Cliente cliente = TestUtils.getRandomCliente();
			Articolo articolo = TestUtils.getRandomArticolo();
			
        	MagazzinoController.aggiungiArticoloMagazzino(articolo, 5);
        	
        	OrdineCliente ordine = ClienteController.effettuaOrdine(cliente, TestUtils.generateArticoliCarrelloByArticolo(articolo, 5));
        	
        	MagazzinoController.controllaArticoliDisponibiliOrdineCliente(ordine);
        	MagazzinoController.verificaPreparazioneOrdineCliente(ordine);
        	MagazzinoController.preparaOrdineCliente(ordine);
        	MagazzinoController.spedisciOrdineCliente(ordine);
        	
        	assertEquals(ordine.getStato().getId(), 6);

        	OrdineCliente ordineDB = OrdineClienteDAO.getOrdineCliente(ordine.getId());
        	
        	assertEquals(ordineDB.getStato().getId(), 6);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
