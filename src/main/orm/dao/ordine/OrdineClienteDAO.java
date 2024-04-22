package main.orm.dao.ordine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.Cliente;
import main.domain.articolo.ArticoloOrdineCliente;
import main.domain.ordine.OrdineCliente;
import main.domain.ordine.OrdineFornitore;
import main.domain.ordine.StatoOrdineCliente;
import main.orm.Database;
import main.orm.dao.ClienteDAO;
import main.orm.dao.articolo.ArticoloOrdineClienteDAO;


public class OrdineClienteDAO {
	
	public static OrdineCliente insertOrdineCliente(StatoOrdineCliente stato, Cliente cliente, List<ArticoloOrdineCliente> articoli) throws ClassNotFoundException, SQLException {
		int idOrdineCliente = Database.insert(Database.Table.ordini_clienti, Map.of(
			"id_stato_ordine_cliente", stato.getId(),
			"id_cliente", cliente.getId()
		));
		ArticoloOrdineClienteDAO.insertOrdineClienteArticoli(idOrdineCliente, articoli);
		return getOrdineCliente(idOrdineCliente);
	}
	
	public static OrdineCliente getOrdineCliente(int idOrdineCliente) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.ordini_clienti, idOrdineCliente);	
		StatoOrdineCliente stato = StatoOrdineClienteDAO.getStatoOrdineCliente((int) res.get("id_stato_ordine_cliente"));
		Cliente cliente = ClienteDAO.getCliente((int) res.get("id_cliente"));
		List<ArticoloOrdineCliente> articoli = ArticoloOrdineClienteDAO.getArticoliOrdineClinteByOrdineClienteId((int) res.get("id"));
		List<OrdineFornitore>  ordiniFornitori = OrdineFornitoreDAO.getOrdiniFornitoriByOrdineClienteId(idOrdineCliente);		
		return new OrdineCliente((int) res.get("id"), stato, cliente, articoli, ordiniFornitori);
	}
	
	public static List<OrdineCliente> getAllOrdiniClienti() throws ClassNotFoundException, SQLException {
		List<OrdineCliente> ordiniClienti = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.ordini_clienti);	
        for (Map<String, Object> row : res) {
        	ordiniClienti.add(getOrdineCliente((int) row.get("id")));
        }
		return ordiniClienti;
	}
	
	public static void updateOrdineCliente(OrdineCliente ordine) throws ClassNotFoundException, SQLException {
		StringBuilder sqlBuilder = new StringBuilder("UPDATE ")
			.append(Database.Table.ordini_clienti)
			.append(" SET id_stato_ordine_cliente = ")
			.append(ordine.getStato().getId())
			.append(", id_cliente = ")
			.append(ordine.getCliente().getId())
			.append(" WHERE id = ")
			.append(ordine.getId());
		Database.rawUpdate(sqlBuilder.toString());
	}
	
}
