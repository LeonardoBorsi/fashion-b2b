package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import main.domain.Cliente;
import main.domain.Fornitore;
import main.domain.articolo.Articolo;
import main.domain.articolo.Capo;
import main.domain.articolo.Taglia;
import main.domain.articolo.Variante;
import main.orm.Database;
import main.orm.dao.ClienteDAO;
import main.orm.dao.FornitoreDAO;
import main.orm.dao.articolo.ArticoloDAO;
import main.orm.dao.articolo.CapoDAO;
import main.orm.dao.articolo.TagliaDAO;
import main.orm.dao.articolo.VarianteDAO;
import main.orm.dao.ordine.StatoOrdineClienteDAO;
import main.orm.dao.ordine.StatoOrdineFornitoreDAO;
import main.orm.dao.ordine.StatoResoOrdineDAO;

public class TestUtils {
	
	public static final Random random = new Random();
	public static final int POPULATED_ARTICOLI_CONT = 35;
	public static final int POPULATED_CLIENTI_CONT = 3;

	
	public static Map<Articolo, Integer> generateArticoliCarrello(int numArticoli, int maxArticoloQnt) throws Exception {		
		Map<Articolo, Integer> articoliCarrello = new HashMap<>();
		int[] articoliIds = getMultipleDifferentRandomInt(numArticoli, 1, POPULATED_ARTICOLI_CONT);
		for(int idArticolo : articoliIds) {
			Articolo articolo = ArticoloDAO.getArticolo(idArticolo);
			int qnt = getRandomInt(1, maxArticoloQnt);
			articoliCarrello.put(articolo, qnt);
		}
		return articoliCarrello;
	}
	
	public static Map<Articolo, Integer> generateArticoliCarrelloByArticolo(Articolo articolo, int qnt) {
		Map<Articolo, Integer> articoliCarrello = new HashMap<>();
		articoliCarrello.put(articolo, qnt);
    	return articoliCarrello;
	}
	
	public static Articolo getRandomArticolo() throws Exception {		
		return ArticoloDAO.getArticolo(getRandomInt(1, POPULATED_ARTICOLI_CONT));
	}
	
	public static Cliente getRandomCliente() throws Exception {
		return ClienteDAO.getCliente(getRandomInt(1, POPULATED_CLIENTI_CONT));
	}
	
	public static void populateDatabase() throws Exception {
		System.out.println("Creating database...");
    	Database.createSchema();
		System.out.println("Populating database...");
		populateStatiOrdineCliente();
		populateStatiOrdineFornitore();
		populateStatiResoOrdine();
		populateClienti();
		List<Fornitore> fornitori = populateFornitori();
		List<Taglia> taglie = populateTaglie();
		List<Capo> capi = populateCapi(fornitori);
		List<Variante> varianti = populateVarianti(capi);
		populateArticoli(capi, varianti, taglie);
		System.out.println("Database populated");
	}
	
	public static void setupDatabase() throws Exception {
		Database.rawUpdate("DELETE FROM resi_ordini;");		
        Database.rawUpdate("DELETE FROM articoli_ordine_fornitore;");
        Database.rawUpdate("DELETE FROM ordini_fornitori;");
        Database.rawUpdate("DELETE FROM articoli_ordine_cliente;");
        Database.rawUpdate("DELETE FROM ordini_clienti;");
        Database.rawUpdate("DELETE FROM articoli_magazzino;");
	}
	
	public static void populateStatiOrdineCliente() throws ClassNotFoundException, SQLException {
		StatoOrdineClienteDAO.insertStatoOrdineCliente("Da controllare");
		StatoOrdineClienteDAO.insertStatoOrdineCliente("Controllato da magazzino");
		StatoOrdineClienteDAO.insertStatoOrdineCliente("Ordinato ai fornitori");
		StatoOrdineClienteDAO.insertStatoOrdineCliente("Da preparare");
		StatoOrdineClienteDAO.insertStatoOrdineCliente("Preparato");
		StatoOrdineClienteDAO.insertStatoOrdineCliente("Spedito");
		StatoOrdineClienteDAO.insertStatoOrdineCliente("Richiesto annullamento");
		StatoOrdineClienteDAO.insertStatoOrdineCliente("Annullato");
		//System.out.println("'stati_ordine_cliente' table populated");
	}
	
	public static void populateStatiOrdineFornitore() throws ClassNotFoundException, SQLException {
		StatoOrdineFornitoreDAO.insertStatoOrdineFornitore("Da produrre");
		StatoOrdineFornitoreDAO.insertStatoOrdineFornitore("Produzione avviata");
		StatoOrdineFornitoreDAO.insertStatoOrdineFornitore("Spedito interamente");
		//System.out.println("'stati_ordine_fornitore' table populated");
	}
	
	public static void populateStatiResoOrdine() throws ClassNotFoundException, SQLException {
		StatoResoOrdineDAO.insertStatoResoOrdine("In approvazione");
		StatoResoOrdineDAO.insertStatoResoOrdine("Approvato");
		StatoResoOrdineDAO.insertStatoResoOrdine("Effettuato");
		//System.out.println("'stati_reso_ordine' table populated");
	}
	
	public static List<Cliente> populateClienti() throws ClassNotFoundException, SQLException {
		List<Cliente> clienti = new ArrayList<>();
		clienti.add(ClienteDAO.insertCliente("Leonardo", "Borsi"));
		clienti.add(ClienteDAO.insertCliente("Mario", "Rossi"));
		clienti.add(ClienteDAO.insertCliente("Paolo", "Bianchi"));
		//System.out.println("'clienti' table populated");
		return clienti;
	}
	
	public static List<Fornitore> populateFornitori() throws ClassNotFoundException, SQLException {
		List<Fornitore> fornitori = new ArrayList<>();
		fornitori.add(FornitoreDAO.insertFornitore("Fornitore Prato", "Viale della Repubblica 100, Prato"));
		fornitori.add(FornitoreDAO.insertFornitore("Fornitore Firenze", "Via Roma 34, Firenze"));
		fornitori.add(FornitoreDAO.insertFornitore("Fornitore Sesto Fiorentino", "Via Milano 76, Sesto Fiorentino"));
		//System.out.println("'fornitori' table populated");
		return fornitori;
	}

	public static List<Taglia> populateTaglie() throws ClassNotFoundException, SQLException {
		List<Taglia> taglie = new ArrayList<>();
		taglie.add(TagliaDAO.insertTaglia("XS"));
		taglie.add(TagliaDAO.insertTaglia("S"));
		taglie.add(TagliaDAO.insertTaglia("M"));
		taglie.add(TagliaDAO.insertTaglia("L"));
		taglie.add(TagliaDAO.insertTaglia("XL"));
		//System.out.println("'taglie' table populated");
		return taglie;
	}
	
	
	public static List<Capo> populateCapi(List<Fornitore> fornitori) throws ClassNotFoundException, SQLException {
		List<Capo> capi = new ArrayList<>();
		capi.add(CapoDAO.insertCapo("T-Shirt basic", fornitori.get(0)));
		capi.add(CapoDAO.insertCapo("T-Shirt con scollo a V", fornitori.get(0)));
		capi.add(CapoDAO.insertCapo("Felpa con cappuccio", fornitori.get(1)));
		capi.add(CapoDAO.insertCapo("Felpa paricollo", fornitori.get(1)));
		capi.add(CapoDAO.insertCapo("Pantalone lungo", fornitori.get(2)));
		capi.add(CapoDAO.insertCapo("Pantaloncini corti", fornitori.get(2)));
		//System.out.println("'capi' table populated");
		return capi;	
	}
	
	public static List<Variante> populateVarianti(List<Capo> capi) throws ClassNotFoundException, SQLException {
		List<Variante> varianti = new ArrayList<>();
		varianti.add(VarianteDAO.insertVariante("T-Shirt basic rossa", "Rosso", capi.get(0)));
		varianti.add(VarianteDAO.insertVariante("T-Shirt basic blu", "Blu", capi.get(0)));
		varianti.add(VarianteDAO.insertVariante("T-Shirt basic nera", "Nero", capi.get(0)));
		varianti.add(VarianteDAO.insertVariante("T-Shirt con scollo a V bianca", "Bianco", capi.get(1)));
		varianti.add(VarianteDAO.insertVariante("T-Shirt con scollo a V nera", "Nero", capi.get(1)));
		varianti.add(VarianteDAO.insertVariante("Felpa con cappuccio gialla", "Giallo", capi.get(2)));
		varianti.add(VarianteDAO.insertVariante("Felpa con cappuccio verde", "Verde", capi.get(2)));
		varianti.add(VarianteDAO.insertVariante("Felpa con cappuccio nera", "Nero", capi.get(2)));
		varianti.add(VarianteDAO.insertVariante("Felpa paricollo grigia", "Grigia", capi.get(3)));
		varianti.add(VarianteDAO.insertVariante("Pantalone lungo nero", "Nero", capi.get(4)));
		varianti.add(VarianteDAO.insertVariante("Pantalone lungo blu", "Blu", capi.get(4)));
		varianti.add(VarianteDAO.insertVariante("Pantaloncini", "Beige", capi.get(5)));
		//System.out.println("'varianti' table populated");
		return varianti;	
	}
	
	public static List<Articolo> populateArticoli(List<Capo> capi, List<Variante> varianti, List<Taglia> taglie) throws ClassNotFoundException, SQLException {
		List<Articolo> articoli = new ArrayList<>();
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(0), taglie.get(0)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(0), taglie.get(1)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(0), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(0), taglie.get(3)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(0), taglie.get(4)));
		
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(1), taglie.get(0)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(1), taglie.get(1)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(1), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(1), taglie.get(3)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(1), taglie.get(4)));
		
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(2), taglie.get(0)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(2), taglie.get(1)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(2), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(2), taglie.get(3)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(0), varianti.get(2), taglie.get(4)));
		
		articoli.add(ArticoloDAO.insertArticolo(capi.get(1), varianti.get(3), taglie.get(1)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(1), varianti.get(3), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(1), varianti.get(3), taglie.get(3)));
		
		articoli.add(ArticoloDAO.insertArticolo(capi.get(1), varianti.get(4), taglie.get(1)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(1), varianti.get(4), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(1), varianti.get(4), taglie.get(3)));
		
		articoli.add(ArticoloDAO.insertArticolo(capi.get(2), varianti.get(5), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(2), varianti.get(6), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(2), varianti.get(7), taglie.get(2)));
		
		articoli.add(ArticoloDAO.insertArticolo(capi.get(3), varianti.get(8), taglie.get(0)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(3), varianti.get(8), taglie.get(1)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(3), varianti.get(8), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(3), varianti.get(8), taglie.get(3)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(3), varianti.get(8), taglie.get(4)));
		
		articoli.add(ArticoloDAO.insertArticolo(capi.get(4), varianti.get(9), taglie.get(1)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(4), varianti.get(9), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(4), varianti.get(9), taglie.get(3)));
		
		articoli.add(ArticoloDAO.insertArticolo(capi.get(4), varianti.get(10), taglie.get(1)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(4), varianti.get(10), taglie.get(2)));
		articoli.add(ArticoloDAO.insertArticolo(capi.get(4), varianti.get(10), taglie.get(3)));

		articoli.add(ArticoloDAO.insertArticolo(capi.get(5), varianti.get(11), taglie.get(2)));

		//System.out.println("'articoli' table populated");
		return articoli;
	}
	
	public static int getRandomInt(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}
	
	public static int[] getMultipleDifferentRandomInt(int N, int min, int max) {
        Set<Integer> generated = new HashSet<>();
        int[] result = new int[N];
        int count = 0;

        while (count < N) {
            int randomNumber = getRandomInt(min, max);
            if (!generated.contains(randomNumber)) {
                generated.add(randomNumber);
                result[count++] = randomNumber;
            }
        }

        return result;
    }
	
	
}
