package dataAccess.databaseManagement.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataAccess.databaseManagement.ConnectionFactory;
import dataAccess.databaseManagement.entity.PortfolioEntity;

public class PortfolioManager {

	private Connection connection = null;
	private PreparedStatement ptmt = null;
	private ResultSet resultSet = null;

	public PortfolioManager() {
	}

	private Connection getConnection() throws SQLException {
		Connection conn;
		conn = ConnectionFactory.getInstance().getConnection();
		return conn;
	}

	public void add(PortfolioEntity portfolioEntity) {
		String queryString = "INSERT INTO portfolio(portfolio_id, user_id, asset_id, price, volume, date) VALUES(?,?,?,?,?,?)";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString,
					Statement.RETURN_GENERATED_KEYS);
			ptmt.setNull(1, java.sql.Types.INTEGER);
			ptmt.setLong(2, portfolioEntity.getUserID());
			ptmt.setLong(3, portfolioEntity.getAssetID());
			ptmt.setDouble(4, portfolioEntity.getPrice());
			ptmt.setDouble(5, portfolioEntity.getVolume());
			ptmt.setDate(6, portfolioEntity.getDate());
			ptmt.executeUpdate();

			ResultSet rs = ptmt.getGeneratedKeys();
			long autoIncValue = -1;

			if (rs.next()) {
				autoIncValue = rs.getLong(1);
			}

			portfolioEntity.setPortfolioID(autoIncValue);

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

	public void update(PortfolioEntity portfolioEntity) {
		try {
			String queryString = "UPDATE portfolio SET user_id=?, asset_id=?, price=?, volume=?, date=? WHERE portfolio_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, portfolioEntity.getUserID());
			ptmt.setLong(2, portfolioEntity.getAssetID());
			ptmt.setDouble(3, portfolioEntity.getPrice());
			ptmt.setDouble(4, portfolioEntity.getVolume());
			ptmt.setDate(5, portfolioEntity.getDate());
			ptmt.setLong(6, portfolioEntity.getPortfolioID());
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

	public void delete(long portfolioID) {
		try {
			String queryString = "DELETE FROM portfolio WHERE portfolio_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, portfolioID);
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

	public PortfolioEntity getPortfolio(long portfolioID) {
		try {
			PortfolioEntity portfolioEntity = null;

			String queryString = "SELECT * FROM portfolio WHERE portfolio_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, portfolioID);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				portfolioEntity = new PortfolioEntity();
				portfolioEntity.setPortfolioID(portfolioID);
				portfolioEntity.setUserID(resultSet.getLong("user_id"));
				portfolioEntity.setAssetID(resultSet.getLong("asset_id"));
				portfolioEntity.setPrice(resultSet.getDouble("price"));
				portfolioEntity.setVolume(resultSet.getDouble("volume"));
				portfolioEntity.setDate(resultSet.getDate("date"));
			}

			return portfolioEntity;
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

	public ArrayList<PortfolioEntity> getPortfolio(long userID, Date date) {
		try {
			ArrayList<PortfolioEntity> listPortfolios = new ArrayList<PortfolioEntity>();

			if (date == null) {
				return listPortfolios;
			}
			
			String queryString = "SELECT * FROM portfolio WHERE user_id=? AND date=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, userID);
			ptmt.setDate(2, date);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				PortfolioEntity portfolioEntity = new PortfolioEntity();

				portfolioEntity.setPortfolioID(resultSet
						.getLong("portfolio_id"));
				portfolioEntity.setUserID(resultSet.getLong("user_id"));
				portfolioEntity.setAssetID(resultSet.getLong("asset_id"));
				portfolioEntity.setPrice(resultSet.getDouble("price"));
				portfolioEntity.setVolume(resultSet.getDouble("volume"));
				portfolioEntity.setDate(resultSet.getDate("date"));

				listPortfolios.add(portfolioEntity);
			}

			return listPortfolios;
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

	public PortfolioEntity getPortfolio(long userID, long assetID, Date date) {
		try {			
			String queryString = "SELECT * FROM portfolio WHERE user_id=? AND asset_id=? AND date=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, userID);
                        ptmt.setLong(2, assetID);
			ptmt.setDate(3, date);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				PortfolioEntity portfolioEntity = new PortfolioEntity();

				portfolioEntity.setPortfolioID(resultSet
						.getLong("portfolio_id"));
				portfolioEntity.setUserID(resultSet.getLong("user_id"));
				portfolioEntity.setAssetID(resultSet.getLong("asset_id"));
				portfolioEntity.setPrice(resultSet.getDouble("price"));
				portfolioEntity.setVolume(resultSet.getDouble("volume"));
				portfolioEntity.setDate(resultSet.getDate("date"));

				return portfolioEntity;
			}

			return null;
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
        
	public ArrayList<PortfolioEntity> getPortfolio(Date date) {
		try {
			ArrayList<PortfolioEntity> listPortfolios = new ArrayList<PortfolioEntity>();

			String queryString = "SELECT * FROM portfolio WHERE date=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setDate(1, date);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				PortfolioEntity portfolioEntity = new PortfolioEntity();

				portfolioEntity.setPortfolioID(resultSet
						.getLong("portfolio_id"));
				portfolioEntity.setUserID(resultSet.getLong("user_id"));
				portfolioEntity.setAssetID(resultSet.getLong("asset_id"));
				portfolioEntity.setPrice(resultSet.getDouble("price"));
				portfolioEntity.setVolume(resultSet.getDouble("volume"));
				portfolioEntity.setDate(resultSet.getDate("date"));

				listPortfolios.add(portfolioEntity);
			}

			return listPortfolios;
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

	public Date getPortfolioStartDateOfUserID(long userID) {
		Date startDate = null;

		try {
			connection = getConnection();
			Statement statement = connection.createStatement();
			resultSet = statement
					.executeQuery("SELECT min(date) FROM portfolio WHERE user_id="
							+ userID);
			resultSet.next();
			startDate = resultSet.getDate(1);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return startDate;
	}

	public Date getPortfolioLatestDateOfUserID(long userID) {
		Date latestDate = null;

		try {
			connection = getConnection();
			Statement statement = connection.createStatement();
			resultSet = statement
					.executeQuery("SELECT max(date) FROM portfolio WHERE user_id="
							+ userID);
			resultSet.next();
			latestDate = resultSet.getDate(1);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return latestDate;
	}

	public ArrayList<PortfolioEntity> getPortfoliosOfUserIDUntilDate(long userID, Date date) {
		try {
			Date latestDate = null;
			
			connection = getConnection();
			Statement statement = connection.createStatement();
			String statementStr = "SELECT MAX(`date`) FROM `portfolio` WHERE `user_id`="
					+ userID + " AND `date` <= '" + date.toString() + "'";
			resultSet = statement.executeQuery(statementStr);
			resultSet.next();
			latestDate = resultSet.getDate(1);

			ArrayList<PortfolioEntity> liPortfolioEntities = getPortfolio(userID, latestDate);
			
			return liPortfolioEntities;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return null;
	}
	
	public ArrayList<PortfolioEntity> getAllPortfolios() {
		try {
			ArrayList<PortfolioEntity> listAllPortfolios = new ArrayList<PortfolioEntity>();

			String queryString = "SELECT * FROM portfolio";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				PortfolioEntity portfolioEntity = new PortfolioEntity();

				portfolioEntity.setPortfolioID(resultSet
						.getLong("portfolio_id"));
				portfolioEntity.setUserID(resultSet.getLong("user_id"));
				portfolioEntity.setAssetID(resultSet.getLong("asset_id"));
				portfolioEntity.setPrice(resultSet.getDouble("price"));
				portfolioEntity.setVolume(resultSet.getDouble("volume"));
				portfolioEntity.setDate(resultSet.getDate("date"));

				listAllPortfolios.add(portfolioEntity);
			}

			return listAllPortfolios;
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
	
	public Date getLatestDate() {
		Date latestDate = null;

		try {
			connection = getConnection();
			Statement statement = connection.createStatement();
			resultSet = statement
					.executeQuery("SELECT max(date) FROM portfolio");
			resultSet.next();
			latestDate = resultSet.getDate(1);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return latestDate;
	}
}
