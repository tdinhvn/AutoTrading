package dataAccess.databaseManagement.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataAccess.databaseManagement.ConnectionFactory;
import dataAccess.databaseManagement.entity.AssetEntity;

public class AssetManager {

	private Connection connection = null;
	private PreparedStatement ptmt = null;
	private ResultSet resultSet = null;

	public AssetManager() {
	}

	private Connection getConnection() throws SQLException {
		Connection conn;
		conn = ConnectionFactory.getInstance().getConnection();
		return conn;
	}

	public void add(AssetEntity assetEntity) {
		String queryString = "INSERT INTO asset(asset_id, name, symbol, exchange_id, asset_info, fluctuation_range) VALUES(?,?,?,?,?,?)";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString,
					Statement.RETURN_GENERATED_KEYS);
			ptmt.setNull(1, java.sql.Types.INTEGER);
			ptmt.setString(2, assetEntity.getName());
			ptmt.setString(3, assetEntity.getSymbol());
			ptmt.setLong(4, assetEntity.getExchangeID());
			ptmt.setString(5, assetEntity.getAssetInfo());
			ptmt.setDouble(6, assetEntity.getFluctuationRange());
			ptmt.executeUpdate();

			ResultSet rs = ptmt.getGeneratedKeys();
			long autoIncValue = -1;

			if (rs.next()) {
				autoIncValue = rs.getLong(1);
			}

			assetEntity.setAssetID(autoIncValue);

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

	public void update(AssetEntity assetEntity) {
		try {
			String queryString = "UPDATE asset SET name=?, symbol=?, exchange_id=?, asset_info=?, fluctuation_range=? WHERE asset_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, assetEntity.getName());
			ptmt.setString(2, assetEntity.getSymbol());
			ptmt.setLong(3, assetEntity.getExchangeID());
			ptmt.setString(4, assetEntity.getAssetInfo());
			ptmt.setDouble(5, assetEntity.getFluctuationRange());
			ptmt.setLong(6, assetEntity.getAssetID());
			ptmt.executeUpdate();
			System.out.println("Table Updated Successfully");
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
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	public void delete(long assetID) {
		try {
			String queryString = "DELETE FROM asset WHERE asset_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, assetID);
			ptmt.executeUpdate();
			System.out.println("Data deleted Successfully");
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
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	public AssetEntity getAssetByID(long assetID) {
		try {
			AssetEntity assetEntity = null;

			String queryString = "SELECT * FROM asset WHERE asset_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, assetID);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				assetEntity = new AssetEntity();
				assetEntity.setAssetID(assetID);
				assetEntity.setName(resultSet.getString("name"));
				assetEntity.setSymbol(resultSet.getString("symbol"));
				assetEntity.setExchangeID(resultSet.getLong("exchange_id"));
				assetEntity.setAssetInfo(resultSet.getString("asset_info"));
				assetEntity.setFluctuationRange(resultSet
						.getDouble("fluctuation_range"));
			}

			return assetEntity;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public AssetEntity getAssetBySymbolAndExchange(String symbol, String exchangeName) {
		try {
			AssetEntity assetEntity = null;

			String queryString = "SELECT t1.* FROM asset as t1, exchange as t2 WHERE t1.symbol=? AND t2.name=? AND t1.exchange_id=t2.exchange_id";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, symbol);
			ptmt.setString(2, exchangeName);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				assetEntity = new AssetEntity();
				assetEntity.setAssetID(resultSet.getLong("asset_id"));
				assetEntity.setName(resultSet.getString("name"));
				assetEntity.setSymbol(resultSet.getString("symbol"));
				assetEntity.setExchangeID(resultSet.getLong("exchange_id"));
				assetEntity.setAssetInfo(resultSet.getString("asset_info"));
				assetEntity.setFluctuationRange(resultSet
						.getDouble("fluctuation_range"));
			}

			return assetEntity;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public AssetEntity getAssetBySymbolAndExchangeID(String symbol,
			long exchangeID) {
		try {
			AssetEntity assetEntity = null;

			String queryString = "SELECT t1.* FROM asset as t1, exchange as t2 WHERE t1.symbol=? AND t2.exchange_id=? AND t1.exchange_id=t2.exchange_id";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, symbol);
			ptmt.setLong(2, exchangeID);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				assetEntity = new AssetEntity();
				assetEntity.setAssetID(resultSet.getLong("asset_id"));
				assetEntity.setName(resultSet.getString("name"));
				assetEntity.setSymbol(resultSet.getString("symbol"));
				assetEntity.setExchangeID(resultSet.getLong("exchange_id"));
				assetEntity.setAssetInfo(resultSet.getString("asset_info"));
				assetEntity.setFluctuationRange(resultSet
						.getDouble("fluctuation_range"));
			}

			return assetEntity;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}
	
	
	public ArrayList<AssetEntity> getAllAssets() {
		try {
			ArrayList<AssetEntity> listAllAssets = new ArrayList<AssetEntity>();

			String queryString = "SELECT * FROM asset";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				AssetEntity assetEntity = new AssetEntity();

				assetEntity.setAssetID(resultSet.getLong("asset_id"));
				assetEntity.setName(resultSet.getString("name"));
				assetEntity.setSymbol(resultSet.getString("symbol"));
				assetEntity.setExchangeID(resultSet.getLong("exchange_id"));
				assetEntity.setAssetInfo(resultSet.getString("asset_info"));
				assetEntity.setFluctuationRange(resultSet
						.getDouble("fluctuation_range"));

				listAllAssets.add(assetEntity);
			}

			return listAllAssets;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	public ArrayList<AssetEntity> getAssetsBySymbol(String symbol) {
		try {

			String queryString = "SELECT * FROM asset WHERE symbol=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setString(1, symbol);
			resultSet = ptmt.executeQuery();

			ArrayList<AssetEntity> listAssets = new ArrayList<AssetEntity>();

			while (resultSet.next()) {
				AssetEntity assetEntity = new AssetEntity();

				assetEntity.setAssetID(resultSet.getLong("asset_id"));
				assetEntity.setName(resultSet.getString("name"));
				assetEntity.setSymbol(symbol);
				assetEntity.setExchangeID(resultSet.getLong("exchange_id"));
				assetEntity.setAssetInfo(resultSet.getString("asset_info"));
				assetEntity.setFluctuationRange(resultSet
						.getDouble("fluctuation_range"));

				listAssets.add(assetEntity);
			}

			return listAssets;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	public ArrayList<AssetEntity> getAssetsByExchange(long exchangeID) {
		try {

			String queryString = "SELECT * FROM asset WHERE exchange_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, exchangeID);
			resultSet = ptmt.executeQuery();

			ArrayList<AssetEntity> listAssets = new ArrayList<AssetEntity>();

			while (resultSet.next()) {
				AssetEntity assetEntity = new AssetEntity();

				assetEntity.setAssetID(resultSet.getLong("asset_id"));
				assetEntity.setName(resultSet.getString("name"));
				assetEntity.setSymbol(resultSet.getString("symbol"));
				assetEntity.setExchangeID(resultSet.getLong("exchange_id"));
				assetEntity.setAssetInfo(resultSet.getString("asset_info"));
				assetEntity.setFluctuationRange(resultSet
						.getDouble("fluctuation_range"));

				listAssets.add(assetEntity);
			}

			return listAssets;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return null;
	}
}
