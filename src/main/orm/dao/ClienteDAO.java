package main.orm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.Cliente;
import main.orm.Database;

public class ClienteDAO {
	public static Cliente insertCliente(String nome, String cognome) throws ClassNotFoundException, SQLException {
		int id = Database.insert(Database.Table.clienti, Map.of(
			"nome", nome,
			"cognome", cognome
		));
		return getCliente(id);
	}
	
	public static void deleteCliente(int id) throws ClassNotFoundException, SQLException {
		Database.deleteById(Database.Table.clienti, id);
	}
	
	public static Cliente getCliente(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.clienti, id);				
		return new Cliente((int) res.get("id"), (String) res.get("nome"), (String) res.get("cognome"));
	}
	
	public static List<Cliente> getAllClienti() throws ClassNotFoundException, SQLException {
        List<Cliente> clienti = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.clienti);	
        for (Map<String, Object> row : res) {
        	clienti.add(new Cliente((int) row.get("id"), (String) row.get("nome"), (String) row.get("cognome")));
        }
		return clienti;
	}
}
