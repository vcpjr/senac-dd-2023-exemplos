package model.dao.telefonia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dao.Banco;
import model.vo.telefonia.Telefone;

public class TelefoneDAO {

	/**
	 * Insere um novo telefone no banco
	 * @param novoTelefone o telefone a ser persistido
	 * @return o telefone inserido com a chave primária gerada
	 */
	public Telefone inserir(Telefone novoTelefone) {
		//Conectar ao banco
		Connection conexao = Banco.getConnection();
		String sql =  " INSERT INTO TELEFONE (ID_CLIENTE, DDD, NUMERO, ATIVO, MOVEL) "
				    + " VALUES (?,?,?,?,?) ";

		PreparedStatement query = Banco.getPreparedStatementWithPk(conexao, sql);
			
		//executar o INSERT
		try {
			query.setInt(1, novoTelefone.getIdCliente());
			query.setString(2, novoTelefone.getDdd());
			query.setString(3, novoTelefone.getNumero());
			query.setBoolean(4, novoTelefone.isAtivo());
			query.setBoolean(5, novoTelefone.isMovel());
			query.execute();
			
			//Preencher o id gerado no banco no objeto
			ResultSet resultado = query.getGeneratedKeys();
			if(resultado.next()) {
				novoTelefone.setId(resultado.getInt(1));
			}
			
		} catch (SQLException e) {
			System.out.println("Erro ao inserir telefone. "
					+ "\nCausa: " + e.getMessage());
		}finally {
			//Fechar a conexão
			Banco.closePreparedStatement(query);
			Banco.closeConnection(conexao);
		}
		
		return novoTelefone;
	}
	
	public boolean atualizar(Telefone telefoneEditado) {
		boolean atualizou = false;
		Connection conexao = Banco.getConnection();
		String sql = " UPDATE TELEFONE "
				   + " SET ID_CLIENTE = ?, DDD = ?, NUMERO  = ?, "
				   + "     ATIVO = ?, MOVEL = ?"
				   + " WHERE ID = ? ";
		PreparedStatement query = Banco.getPreparedStatement(conexao, sql);
		try {
			query.setInt(1, telefoneEditado.getIdCliente());
			query.setString(2, telefoneEditado.getDdd());
			query.setString(3, telefoneEditado.getNumero());
			query.setBoolean(4, telefoneEditado.isAtivo());
			query.setBoolean(5, telefoneEditado.isMovel());
			query.setInt(6, telefoneEditado.getId());
			
			int quantidadeLinhasAtualizadas = query.executeUpdate();
			atualizou = quantidadeLinhasAtualizadas > 0;
		} catch (SQLException excecao) {
			System.out.println("Erro ao atualizar telefone. "
					+ "\n Causa: " + excecao.getMessage());
		}finally {
			Banco.closePreparedStatement(query);
			Banco.closeConnection(conexao);
		}
		
		return atualizou;
	}
	
	public Telefone consultarPorId(int id) {
		Telefone telefoneConsultado = null;
		Connection conexao = Banco.getConnection();
		String sql =  " SELECT * FROM TELEFONE "
				    + " WHERE ID = ?";
		PreparedStatement query = Banco.getPreparedStatement(conexao, sql);
		
		try {
			query.setInt(1, id);
			ResultSet resultado = query.executeQuery();
			
			if(resultado.next()) {
				telefoneConsultado = converterDeResultSetParaEntidade(resultado);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar telefone com id: + " + id 
								+ "\n Causa: " + e.getMessage());	
		}finally {
			Banco.closePreparedStatement(query);
			Banco.closeConnection(conexao);
		}
		
		return telefoneConsultado;
	}
	
	public boolean excluir(int id) {
		boolean excluiu = false;
		
		Connection conexao = Banco.getConnection();
		String sql = " DELETE FROM TELEFONE "
				   + " WHERE ID = ? ";
		PreparedStatement query = Banco.getPreparedStatement(conexao, sql);
		try {
			query.setInt(1, id);
			
			int quantidadeLinhasAtualizadas = query.executeUpdate();
			excluiu = quantidadeLinhasAtualizadas > 0;
		} catch (SQLException excecao) {
			System.out.println("Erro ao excluir telefone. "
					+ "\n Causa: " + excecao.getMessage());
		}finally {
			Banco.closePreparedStatement(query);
			Banco.closeConnection(conexao);
		}
		return excluiu;
	}
	
	public List<Telefone> consultarTodos() {
		List<Telefone> telefones = new ArrayList<Telefone>();
		Connection conexao = Banco.getConnection();
		String sql =  " SELECT * FROM TELEFONE ";
		PreparedStatement query = Banco.getPreparedStatement(conexao, sql);
		
		try {
			ResultSet resultado = query.executeQuery();
			while(resultado.next()) {
				Telefone telefoneConsultado = converterDeResultSetParaEntidade(resultado);
				telefones.add(telefoneConsultado);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar todos os telefones" 
								+ "\n Causa: " + e.getMessage());	
		} finally {
			Banco.closePreparedStatement(query);
			Banco.closeConnection(conexao);
		}
		
		return telefones;
	}
	
	private Telefone converterDeResultSetParaEntidade(ResultSet resultado) throws SQLException {
		Telefone telefoneConsultado = new Telefone(); 
		telefoneConsultado.setId(resultado.getInt("id"));
		telefoneConsultado.setIdCliente(resultado.getInt("id_cliente"));
		telefoneConsultado.setDdd(resultado.getString("ddd"));
		telefoneConsultado.setNumero(resultado.getString("numero"));
		telefoneConsultado.setAtivo(resultado.getBoolean("ativo"));
		telefoneConsultado.setMovel(resultado.getBoolean("movel"));
		return telefoneConsultado;
	}

	public List<Telefone> consultarPorIdCliente(Integer id) {
		List<Telefone> telefones = new ArrayList<Telefone>();
		Connection conexao = Banco.getConnection();
		String sql =  " SELECT * FROM TELEFONE "
				+ " WHERE ID_CLIENTE = ? ";
		PreparedStatement query = Banco.getPreparedStatement(conexao, sql);
		
		try {
			query.setInt(1, id);
			ResultSet resultado = query.executeQuery();
			while(resultado.next()) {
				Telefone telefoneConsultado = converterDeResultSetParaEntidade(resultado);
				telefones.add(telefoneConsultado);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar todos os telefones do cliente informado" 
								+ "\n Causa: " + e.getMessage());	
		} finally {
			Banco.closePreparedStatement(query);
			Banco.closeConnection(conexao);
		}
		
		return telefones;
	}
			
}

