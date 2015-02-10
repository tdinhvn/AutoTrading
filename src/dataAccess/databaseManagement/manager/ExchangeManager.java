package dataAccess.databaseManagement.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataAccess.databaseManagement.ConnectionFactory;
import dataAccess.databaseManagement.entity.ExchangeEntity;

public class ExchangeManager {
	private Connection connection = null;
	private PreparedStatement ptmt = null;
	private ResultSet resultSet = null;

	public ExchangeManager() {
	}

	private Connection getConnection() throws SQLException {
		Connection conn;
		conn = ConnectionFactory.getInstance().getConnection();
		return conn;
	}

	public void add(ExchangeEntity exchangeEntity) {
		String queryString = "INSERT INTO exchange(exchange_id, name, fluctuation_range) VALUES(?,?,?)";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString,
					Statement.RETURN_GENERATED_KEYS);
			ptmt.setNull(1, java.sql.Types.INTEGER);
			ptmt.setString(2, exchangeEntity.getName());
			ptmt.setDouble(3, exchangeEntity.getFluctuationRange());
			ptmt.executeUpdate();

			ResultSet rs = ptmt.getGeneratedKeys();
			long autoIncValue = -1;

			if (rs.next()) {
				autoIncValue = rs.getLong(1);
			}

			exchangeEntity.setExchangeID(autoIncValue);

			System.out.println("Data Added Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ptmt != null) {
					ptmt.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void update(ExchangeEntity exchangeEntity) {
		try {
			String queryString = "UPDATE exchange SET name=?, fluctuation_range=? WHERE exchange_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, exchangeEntity.getName());
			ptmt.setDouble(2, exchangeEntity.getFluctuationRange());
			ptmt.setLong(3, exchangeEntity.getExchangeID());
			ptmt.executeUpdate();
			System.out.println("Table Updated Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ptmt != null)
					ptmt.close();
				if (connection != null)
					connection.close();
			}

			catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	public void delete(long exchangeID) {
		try {
			String queryString = "DELETE FROM exchange WHERE exchange_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, exchangeID);
			ptmt.executeUpdate();
			System.out.println("Data deleted Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ptmt != null)
					ptmt.close();
				if (connection != null)
					connection.close();
			}

			catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	public ExchangeEntity getExchangeByID(long exchangeID) {
		try {
			ExchangeEntity exchangeEntity = null;

			String queryString = "SELECT * FROM exchange WHERE exchange_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, exchangeID);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				exchangeEntity = new ExchangeEntity();
				exchangeEntity.setExchangeID(exchangeID);
				exchangeEntity.setName(resultSet.getString("name"));
				exchangeEntity.setFluctuationRange(resultSet
						.getDouble("fluctuation_range"));
			}

			return exchangeEntity;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (ptmt != null)
					ptmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public ExchangeEntity getExchangeByName(String exchangeName) {
		try {
			ExchangeEntity exchangeEntity = null;

			String queryString = "SELECT * FROM exchange WHERE name=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, exchangeName);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				exchangeEntity = new ExchangeEntity();
				exchangeEntity.setExchangeID(resultSet.getLong("exchange_id"));
				exchangeEntity.setName(exchangeName);
				exchangeEntity.setFluctuationRange(resultSet
						.getDouble("fluctuation_range"));
			}

			return exchangeEntity;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (ptmt != null)
					ptmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public ArrayList<ExchangeEntity> getAllExchanges() {
		try {
			ArrayList<ExchangeEntity> listAllExchanges = new ArrayList<ExchangeEntity>();

			String queryString = "SELECT * FROM exchange";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				ExchangeEntity exchangeEntity = new ExchangeEntity();

				exchangeEntity.setExchangeID(resultSet.getLong("exchange_id"));
				exchangeEntity.setName(resultSet.getString("name"));
				exchangeEntity.setFluctuationRange(resultSet
						.getDouble("fluctuation_range"));

				listAllExchanges.add(exchangeEntity);
			}

			return listAllExchanges;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (ptmt != null)
					ptmt.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}

}
