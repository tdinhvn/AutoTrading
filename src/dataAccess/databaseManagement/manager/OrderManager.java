package dataAccess.databaseManagement.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataAccess.databaseManagement.ConnectionFactory;
import dataAccess.databaseManagement.entity.OrderEntity;

public class OrderManager {

	private Connection connection = null;
	private PreparedStatement ptmt = null;
	private ResultSet resultSet = null;

	public OrderManager() {
	}

	private Connection getConnection() throws SQLException {
		Connection conn;
		conn = ConnectionFactory.getInstance().getConnection();
		return conn;
	}

	public void add(OrderEntity orderEntity) {
		String queryString = "INSERT INTO `order`(`order_id`, `order_type`, `user_id`, `date`, `asset_id`, `price`, `volume`, `matched`) VALUES(?,?,?,?,?,?,?,?)";
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString,
					Statement.RETURN_GENERATED_KEYS);
			ptmt.setNull(1, java.sql.Types.INTEGER);
			ptmt.setBoolean(2, orderEntity.getOrderType());
			ptmt.setLong(3, orderEntity.getUserID());
			ptmt.setDate(4, orderEntity.getDate());
			ptmt.setLong(5, orderEntity.getAssetID());
			ptmt.setDouble(6, orderEntity.getPrice());
			ptmt.setDouble(7, orderEntity.getVolume());
			ptmt.setBoolean(8, orderEntity.isMatched());
			ptmt.executeUpdate();

			ResultSet rs = ptmt.getGeneratedKeys();
			long autoIncValue = -1;

			if (rs.next()) {
				autoIncValue = rs.getLong(1);
			}

			orderEntity.setOrderID(autoIncValue);

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

	public void update(OrderEntity orderEntity) {
		try {
			String queryString = "UPDATE `order` SET `order_type`=?, `user_id`=?, `date`=?, `asset_id`=?, `price`=?, `volume`=?, `matched`=? WHERE `order_id`=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setBoolean(1, orderEntity.getOrderType());
			ptmt.setLong(2, orderEntity.getUserID());
			ptmt.setDate(3, orderEntity.getDate());
			ptmt.setLong(4, orderEntity.getAssetID());
			ptmt.setDouble(5, orderEntity.getPrice());
			ptmt.setDouble(6, orderEntity.getVolume());
			ptmt.setBoolean(7, orderEntity.isMatched());
			ptmt.setLong(8, orderEntity.getOrderID());
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

	public void delete(long orderID) {
		try {
			String queryString = "DELETE FROM `order` WHERE `order_id`=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, orderID);
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

	public OrderEntity getOrderByID(long orderID) {
		try {
			OrderEntity orderEntity = null;

			String queryString = "SELECT * FROM `order` WHERE `order_id`=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, orderID);
			resultSet = ptmt.executeQuery();

			if (resultSet.next()) {
				orderEntity = new OrderEntity();
				orderEntity.setOrderID(orderID);
				orderEntity.setOrderType(resultSet.getBoolean("order_type"));
				orderEntity.setUserID(resultSet.getLong("user_id"));
				orderEntity.setDate(resultSet.getDate("date"));
				orderEntity.setAssetID(resultSet.getLong("asset_id"));
				orderEntity.setPrice(resultSet.getDouble("price"));
				orderEntity.setVolume(resultSet.getDouble("volume"));
				orderEntity.setMatched(resultSet.getBoolean("matched"));
			}

			return orderEntity;
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

	public ArrayList<OrderEntity> getOrderByDate(Date date) {
		try {
			ArrayList<OrderEntity> listOrders = new ArrayList<OrderEntity>();

			String queryString = "SELECT * FROM `order` WHERE `date`=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setDate(1, date);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				OrderEntity orderEntity = new OrderEntity();

				orderEntity.setOrderID(resultSet.getLong("order_id"));
				orderEntity.setOrderType(resultSet.getBoolean("order_type"));
				orderEntity.setUserID(resultSet.getLong("user_id"));
				orderEntity.setDate(resultSet.getDate("date"));
				orderEntity.setAssetID(resultSet.getLong("asset_id"));
				orderEntity.setPrice(resultSet.getDouble("price"));
				orderEntity.setVolume(resultSet.getDouble("volume"));
				orderEntity.setMatched(resultSet.getBoolean("matched"));

				listOrders.add(orderEntity);
			}

			return listOrders;
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

	public ArrayList<OrderEntity> getOrderUntilDateOfUserID(long userID,
			Date date) {
		try {
			ArrayList<OrderEntity> listOrders = new ArrayList<OrderEntity>();

			String queryString = "SELECT * FROM `order` WHERE `user_id` = ? AND `date` < ?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, userID);
			ptmt.setDate(2, date);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				OrderEntity orderEntity = new OrderEntity();

				orderEntity.setOrderID(resultSet.getLong("order_id"));
				orderEntity.setOrderType(resultSet.getBoolean("order_type"));
				orderEntity.setUserID(resultSet.getLong("user_id"));
				orderEntity.setDate(resultSet.getDate("date"));
				orderEntity.setAssetID(resultSet.getLong("asset_id"));
				orderEntity.setPrice(resultSet.getDouble("price"));
				orderEntity.setVolume(resultSet.getDouble("volume"));
				orderEntity.setMatched(resultSet.getBoolean("matched"));

				listOrders.add(orderEntity);
			}

			return listOrders;
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

	public ArrayList<OrderEntity> getOrderByDateAndUserID(long userID, Date date) {
		try {
			ArrayList<OrderEntity> listOrders = new ArrayList<OrderEntity>();

			String queryString = "SELECT * FROM `order` WHERE `user_id` = ? AND `date` = ?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, userID);
			ptmt.setDate(2, date);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				OrderEntity orderEntity = new OrderEntity();

				orderEntity.setOrderID(resultSet.getLong("order_id"));
				orderEntity.setOrderType(resultSet.getBoolean("order_type"));
				orderEntity.setUserID(resultSet.getLong("user_id"));
				orderEntity.setDate(resultSet.getDate("date"));
				orderEntity.setAssetID(resultSet.getLong("asset_id"));
				orderEntity.setPrice(resultSet.getDouble("price"));
				orderEntity.setVolume(resultSet.getDouble("volume"));
				orderEntity.setMatched(resultSet.getBoolean("matched"));

				listOrders.add(orderEntity);
			}

			return listOrders;
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

	public ArrayList<OrderEntity> getOrderByUserID(long userID) {
		try {
			ArrayList<OrderEntity> listOrders = new ArrayList<OrderEntity>();

			String queryString = "SELECT * FROM `order` WHERE `user_id`=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, userID);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				OrderEntity orderEntity = new OrderEntity();

				orderEntity.setOrderID(resultSet.getLong("order_id"));
				orderEntity.setOrderType(resultSet.getBoolean("order_type"));
				orderEntity.setUserID(resultSet.getLong("user_id"));
				orderEntity.setDate(resultSet.getDate("date"));
				orderEntity.setAssetID(resultSet.getLong("asset_id"));
				orderEntity.setPrice(resultSet.getDouble("price"));
				orderEntity.setVolume(resultSet.getDouble("volume"));
				orderEntity.setMatched(resultSet.getBoolean("matched"));

				listOrders.add(orderEntity);
			}

			return listOrders;
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

	public ArrayList<OrderEntity> getAllOrders() {
		try {
			ArrayList<OrderEntity> listAllOrders = new ArrayList<OrderEntity>();

			String queryString = "SELECT * FROM `order`";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			resultSet = ptmt.executeQuery();

			while (resultSet.next()) {
				OrderEntity orderEntity = new OrderEntity();

				orderEntity.setOrderID(resultSet.getLong("order_id"));
				orderEntity.setOrderType(resultSet.getBoolean("order_type"));
				orderEntity.setUserID(resultSet.getLong("user_id"));
				orderEntity.setDate(resultSet.getDate("date"));
				orderEntity.setAssetID(resultSet.getLong("asset_id"));
				orderEntity.setPrice(resultSet.getDouble("price"));
				orderEntity.setVolume(resultSet.getDouble("volume"));
				orderEntity.setMatched(resultSet.getBoolean("matched"));

				listAllOrders.add(orderEntity);
			}

			return listAllOrders;
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

        public Date getLatestDateUntilDateOfOrderOfUserID (long userID, Date date) {
		try {
			Date latestDate = null;

			connection = getConnection();
			Statement statement = connection.createStatement();
			String statementStr = "SELECT MAX(`date`) FROM `order` WHERE `user_id`="
					+ userID + " AND `date` <= '" + date.toString() + "'";
			resultSet = statement.executeQuery(statementStr);
			resultSet.next();
			latestDate = resultSet.getDate(1);

			return latestDate;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
