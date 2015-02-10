package dataAccess.databaseManagement.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import dataAccess.databaseManagement.ConnectionFactory;
import dataAccess.databaseManagement.entity.TotalAssetEntity;

public class TotalAssetManager {

	private Connection connection = null;
	private PreparedStatement ptmt = null;
	private ResultSet resultSet = null;

	public TotalAssetManager() {
	}

	private Connection getConnection() throws SQLException {
		Connection conn;
		conn = ConnectionFactory.getInstance().getConnection();
		return conn;
	}

	public void add(TotalAssetEntity totalAssetEntity) {
		String queryString = "INSERT INTO total_asset(total_asset_id, date, user_id, total_asset) VALUES(?,?,?,?)";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString,
					Statement.RETURN_GENERATED_KEYS);
			ptmt.setNull(1, java.sql.Types.INTEGER);
			ptmt.setDate(2, totalAssetEntity.getDate());
			ptmt.setLong(3, totalAssetEntity.getUserID());
			ptmt.setDouble(4, totalAssetEntity.getTotalAsset());

			ptmt.executeUpdate();

			ResultSet rs = ptmt.getGeneratedKeys();
			long autoIncValue = -1;

			if (rs.next()) {
				autoIncValue = rs.getLong(1);
			}

			totalAssetEntity.setTotalAssetID(autoIncValue);

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

	public void update(TotalAssetEntity totalAssetEntity) {
		try {
			String queryString = "UPDATE total_asset SET date=?, user_id=?, total_asset=? WHERE total_asset_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setDate(1, totalAssetEntity.getDate());
			ptmt.setLong(2, totalAssetEntity.getUserID());
			ptmt.setDouble(3, totalAssetEntity.getTotalAsset());
			ptmt.setLong(4, totalAssetEntity.getTotalAssetID());
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

	public void delete(long totalAssetID) {
		try {
			String queryString = "DELETE FROM total_asset WHERE total_asset_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, totalAssetID);
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

	public TotalAssetEntity getTotalAssetByID(long totalAssetID) {
		try {
			TotalAssetEntity totalAssetEntity = null;

			String queryString = "SELECT * FROM total_asset WHERE total_asset_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, totalAssetID);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				totalAssetEntity = new TotalAssetEntity();
				totalAssetEntity.setTotalAssetID(totalAssetID);
				totalAssetEntity.setUserID(resultSet.getLong("user_id"));
				totalAssetEntity.setDate(resultSet.getDate("date"));
				totalAssetEntity.setTotalAsset(resultSet.getDouble("total_asset"));
			}

			return totalAssetEntity;
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

	public TotalAssetEntity getTotalAssetByUserIDAndDate(long userID, Date date) {
		try {
			TotalAssetEntity totalAssetEntity = null;

			String queryString = "SELECT * FROM total_asset WHERE user_id=? AND date=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, userID);
			ptmt.setDate(2, date);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				totalAssetEntity = new TotalAssetEntity();
				totalAssetEntity.setTotalAssetID(resultSet.getLong("total_asset_id"));
				totalAssetEntity.setUserID(resultSet.getLong("user_id"));
				totalAssetEntity.setDate(resultSet.getDate("date"));
				totalAssetEntity.setTotalAsset(resultSet.getDouble("total_asset"));
			}

			return totalAssetEntity;
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

	public ArrayList<TotalAssetEntity> getTotalAssetByUserID(long user_id) {
		try {
			ArrayList<TotalAssetEntity> listTotalAssets = new ArrayList<TotalAssetEntity>();

			String queryString = "SELECT * FROM total_asset WHERE user_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, user_id);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				TotalAssetEntity totalAssetEntity = new TotalAssetEntity();

				totalAssetEntity.setTotalAssetID(resultSet.getLong("total_asset_id"));
				totalAssetEntity.setUserID(resultSet.getLong("user_id"));
				totalAssetEntity.setDate(resultSet.getDate("date"));
				totalAssetEntity.setTotalAsset(resultSet.getDouble("total_asset"));

				listTotalAssets.add(totalAssetEntity);
			}

			Collections.sort(listTotalAssets);

			return listTotalAssets;
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
