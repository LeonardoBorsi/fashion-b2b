package main.orm.dao.ordine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.ordine.StatoOrdineCliente;
import main.orm.Database;

public class StatoOrdineClienteDAO {
		
	public static StatoOrdineCliente insertStatoOrdineCliente(String nome) throws ClassNotFoundException, SQLException {
		int id = Database.insert(Database.Table.stati_ordine_cliente, Map.of(
			"nome", nome
		));
		return getStatoOrdineCliente(id);
	}
	
	public static StatoOrdineCliente getStatoOrdineCliente(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.stati_ordine_cliente, id);				
		return new StatoOrdineCliente((int) res.get("id"), (String) res.get("nome"));
	}
	
	public static List<StatoOrdineCliente> getAllStatiOrdineCliente() throws ClassNotFoundException, SQLException {
        List<StatoOrdineCliente> stati = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.stati_ordine_cliente);	
        for (Map<String, Object> row : res) {
        	stati.add(new StatoOrdineCliente((int) row.get("id"), (String) row.get("nome")));
        }
		return stati;
	}
}
