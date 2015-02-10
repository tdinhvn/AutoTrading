package business.virtualTrading;

import dataAccess.databaseManagement.entity.*;
import dataAccess.databaseManagement.manager.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class Name: User
 * 
 * @version 1.5
 * @date June 1, 2011
 * @author Xuan Ngoc
 */

public class User {
	private UserEntity user;
	private double initialCapital;
	private ArrayList<TotalAssetEntity> totalAssetList;
	private ArrayList<Order> curOrderList;
	private ArrayList<PortfolioEntry> curPortfolioList;
	private Date latestTradingDate = null;

	/**
	 * Constructor <li>userID is automatically created
	 */
	public User(String name, double cash) {
		user = new UserEntity(name, cash, 0, 0, 0);
		curOrderList = new ArrayList<Order>();
		curPortfolioList = new ArrayList<PortfolioEntry>();
		totalAssetList = new ArrayList<TotalAssetEntity>();
		initialCapital = cash;
	}
	
	/**
	 * Constructor <li>userID is automatically created
	 */
	public User(UserEntity userEntity) {
		user = userEntity;
		curOrderList = new ArrayList<Order>();
		curPortfolioList = new ArrayList<PortfolioEntry>();
		totalAssetList = new TotalAssetManager().getTotalAssetByUserID(userEntity.getUserID());
		if (totalAssetList == null) {
			totalAssetList = new ArrayList<TotalAssetEntity>();
			initialCapital = this.getTotalCash();
		} else {
			initialCapital = totalAssetList.get(0).getTotalAsset();
			latestTradingDate = totalAssetList.get(totalAssetList.size()-1).getDate();
		}
	}

	/**
	 * Constructor for user get from database <li>userID is automatically
	 * created
	 */
	public User(UserEntity userEntity,
			ArrayList<PortfolioEntry> portfolioEntryList) {
		user = userEntity;
		curOrderList = new ArrayList<Order>();
		curPortfolioList = new ArrayList<PortfolioEntry>();
				
		for (PortfolioEntry curPortfolioEntry : portfolioEntryList) {
			curPortfolioList.add(curPortfolioEntry);
		}
		
		totalAssetList = new TotalAssetManager().getTotalAssetByUserID(userEntity.getUserID());
		if (totalAssetList.size() == 0) {
			totalAssetList = new ArrayList<TotalAssetEntity>();
			initialCapital = this.getTotalCash() + this.portfolioCurrentValue();
		} else {
			initialCapital = totalAssetList.get(0).getTotalAsset();
			latestTradingDate = totalAssetList.get(totalAssetList.size()-1).getDate();
		}
	}
	
	/**
	 * Constructor <li>userID is automatically created
	 */
	public User(String name, double cash, ArrayList<PortfolioEntry> portfolioEntryList) {
		user = new UserEntity(name, cash, 0, 0, 0);
		curOrderList = new ArrayList<Order>();
		curPortfolioList = new ArrayList<PortfolioEntry>();
		totalAssetList = new ArrayList<TotalAssetEntity>();
		for (PortfolioEntry curPortfolioEntry : portfolioEntryList) {
			curPortfolioList.add(curPortfolioEntry);
		}
		initialCapital = this.getTotalCash() + this.portfolioCurrentValue();
	}

	/**
	 * Constructor get user's information from database by userID
	 */
//	public User(long userID) {
//		UserManager userManager = new UserManager();
//		user = userManager.getUserByID(userID);
//	}

	/**
	 * Add cash to a user
	 */
	public void addCash(double cash) {
		user.setCash(user.getCash() + cash);
		UserManager userManager = new UserManager();
		userManager.update(user);
		//initialCapital += cash;
	}

	/**
	 * Get orders by Date from database.
	 * 
	 * @param date
	 * @return
	 */
	public ArrayList<Order> getOrderByDate(Date date) {
		OrderManager orderManager = new OrderManager();
		AssetManager assetmanager = new AssetManager();
		ArrayList<OrderEntity> orderEntityList = orderManager
				.getOrderByDateAndUserID(user.getUserID(), date);
		ArrayList<Order> orderList = new ArrayList<Order>();
		Asset currentAsset;

		for (OrderEntity currentOrder : orderEntityList) {
			currentAsset = new Asset(assetmanager.getAssetByID(currentOrder
					.getAssetID()));
			orderList.add(new Order(currentAsset, currentOrder.getOrderType(),
					currentOrder.getPrice(), currentOrder.getVolume()));
		}
		return orderList;
	}

	/**
	 * Get portfolioList by date from database
	 */
	public ArrayList<PortfolioEntry> getPortfolioByDate(Date date) {
		ArrayList<PortfolioEntry> portfolioList = new ArrayList<PortfolioEntry>();
		PortfolioManager portfolioManager = new PortfolioManager();
		AssetManager assetManager = new AssetManager();
		ArrayList<PortfolioEntity> portfolioEntityList = portfolioManager
				.getPortfoliosOfUserIDUntilDate(user.getUserID(), date);

		PortfolioEntry curPortfolioEntry;
		AssetEntity assetEntity;

		if (portfolioEntityList != null && portfolioEntityList.size() > 0) {
			

			for (PortfolioEntity portfolioEntity : portfolioEntityList) {
				Date latestDate = new PriceManager().getLatestDateOfAssetIDUntiltDate(portfolioEntity
						.getAssetID(), date);
				assetEntity = assetManager.getAssetByID(portfolioEntity
						.getAssetID());
				curPortfolioEntry = new PortfolioEntry(new Asset(assetEntity),
						portfolioEntity.getPrice(), portfolioEntity.getVolume());

				curPortfolioEntry.updateCurrentPriceToDate(latestDate);
				portfolioList.add(curPortfolioEntry);
			}
		}
		return portfolioList;
	}

	public void addPortfolioToDatabase() {
		PortfolioManager portfolioManager = new PortfolioManager();

		for (PortfolioEntry curPortfolioEntry : curPortfolioList) {
			portfolioManager.add(new PortfolioEntity(user.getUserID(),
					curPortfolioEntry.getAsset().getAssetID(),
					curPortfolioEntry.getBuyPrice(), curPortfolioEntry
							.getVolume(), new java.sql.Date(
							new java.util.Date().getTime())));
		}
	}
	
	public void addPortfolioToDatabase(Date date) {
		PortfolioManager portfolioManager = new PortfolioManager();
		//PriceManager priceManager = new PriceManager();

		for (PortfolioEntry curPortfolioEntry : curPortfolioList) {
                    PortfolioEntity portfolioEntity = portfolioManager.getPortfolio(user.getUserID(),
					curPortfolioEntry.getAsset().getAssetID(), date);
                    if (portfolioEntity == null) {
			portfolioManager.add(new PortfolioEntity(user.getUserID(),
					curPortfolioEntry.getAsset().getAssetID(),
					curPortfolioEntry.getBuyPrice(), curPortfolioEntry
							.getVolume(), date));
                    } else {
                        portfolioEntity.setPrice(curPortfolioEntry.getBuyPrice());
                        portfolioEntity.setVolume(curPortfolioEntry.getVolume());
                        portfolioManager.update(portfolioEntity);
                    }
                }
	}

	/**
	 * Add orders to database and update portfolio in database <li>Note: this
	 * method updates database
	 */
	public void addOrderToDatabase() {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = Date.valueOf(dateFormat.format(cal.getTime()));

		OrderManager orderManager = new OrderManager();
		for (Order order : curOrderList) {
			orderManager.add(new OrderEntity(order.getOrderType(), user
					.getUserID(), date, order.getAsset().getAssetID(), order
					.getPrice(), order.getVolume(), true));
		}

		this.executeOrder();

		
		//Update portfolio
		 
		PortfolioManager portfolioManager = new PortfolioManager();
		Date latestDate = portfolioManager.getPortfolioLatestDateOfUserID(user
				.getUserID());

		if (latestDate.equals(date)) { // remove today's porfolio in database
			ArrayList<PortfolioEntity> portfolioEntityList = portfolioManager
					.getPortfolio(user.getUserID(),
							portfolioManager.getLatestDate());
			for (int i = 0; i < portfolioEntityList.size(); i++) {
				portfolioManager.delete(portfolioEntityList.get(i)
						.getPortfolioID());
			}
		}

		for (PortfolioEntry curPortfolioEntry : curPortfolioList) { // add
																	// today's
																	// portfolio
																	// entries
			portfolioManager.add(new PortfolioEntity(user.getUserID(),
					curPortfolioEntry.getAsset().getAssetID(),
					curPortfolioEntry.getBuyPrice(), curPortfolioEntry
							.getVolume(), date));
		}
		

		UserManager usermanager = new UserManager();
		usermanager.update(user);

	}
	
	/**
	 * Add orders to database and update portfolio in database <li>Note: this
	 * method updates database
	 */
	public void addOrderToDatabase(Date date) {
		OrderManager orderManager = new OrderManager();

		long assetID = -1;
		if (!curPortfolioList.isEmpty()) {
			assetID = curPortfolioList.get(0).getAsset().getAssetID();
		}
		
		this.executeOrder();
		
		for (Order order : curOrderList) {
			if (order.getMatched()) {
				orderManager.add(new OrderEntity(order.getOrderType(), user
						.getUserID(), date, order.getAsset().getAssetID(),
						order.getPrice(), order.getVolume(), true));
			}
		}

		/*
		 * Update portfolio
		 */
		PortfolioManager portfolioManager = new PortfolioManager();
		Date latestDate = portfolioManager.getPortfolioLatestDateOfUserID(user
				.getUserID());

		if (latestDate != null && latestDate.equals(date)) { // remove today's porfolio in database
			ArrayList<PortfolioEntity> portfolioEntityList = portfolioManager
					.getPortfolio(user.getUserID(),
							portfolioManager.getLatestDate());
			for (int i = 0; i < portfolioEntityList.size(); i++) {
				portfolioManager.delete(portfolioEntityList.get(i)
						.getPortfolioID());
			}
		}
		
		// add today's portfolio entries
		for (PortfolioEntry curPortfolioEntry : curPortfolioList) { 
			portfolioManager.add(new PortfolioEntity(user.getUserID(),
					curPortfolioEntry.getAsset().getAssetID(),
					curPortfolioEntry.getBuyPrice(), curPortfolioEntry
							.getVolume(), date));
		}
		
		if (curPortfolioList.isEmpty() && assetID != -1) {
			portfolioManager.add(new PortfolioEntity(user.getUserID(),assetID,0,0, date));
		}

		setLatestTradingDate(date);
		updatePortfolioCurrentPrice(date);

		/*
		 * Update user cash
		 */
		UserManager usermanager = new UserManager();
		usermanager.update(user);
	}
	
	public void next() {
		UserManager userManager = new UserManager();
		
		/* 
		 * Cash update
		 */
		user.setCash(user.getCash() + user.getCash01());
		user.setCash01(user.getCash02());
		user.setCash02(user.getCash03());
		user.setCash03(0);
		userManager.update(this.user);
		
                //Add total asset entry to database
                TotalAssetManager totalAssetManager = new TotalAssetManager();
                TotalAssetEntity totalAssetEntity = totalAssetManager.getTotalAssetByUserIDAndDate(this.getUserID(), this.getLatestTradingDate());
                if (totalAssetEntity == null) {
                    totalAssetEntity = new TotalAssetEntity(this.getUserID(), this.getLatestTradingDate(), this.getTotalCash() + this.portfolioCurrentValue());
                    totalAssetManager.add(totalAssetEntity);
                } else {
                    totalAssetEntity.setTotalAsset(this.getTotalCash() + this.portfolioCurrentValue());
                    totalAssetManager.update(totalAssetEntity);
                }
                
                if (totalAssetList.get(totalAssetList.size()-1).getDate().equals(totalAssetEntity.getDate())) {
                	totalAssetList.remove(totalAssetList.size()-1);
                }
                totalAssetList.add(totalAssetEntity);
                
                PriceManager priceManager = new PriceManager();
                setLatestTradingDate(priceManager.getNextDate(new Date(getLatestTradingDate().getTime())) );
                updatePortfolioCurrentPrice(getLatestTradingDate());
	}

	/**
	 * excute orders and update portfolio
	 */
	public void executeOrder() {
		for (Order curOrder : curOrderList) {

			/*
			 * Algorithm :
			 * 
			 * if (buyOrderType) then update cash & validVolume if (asset of
			 * order is not in portfolio) and (buyOrderType) then add Order to
			 * Portfolio if (portfolio has asset in order) then if
			 * (buyOrderType) then updatePortfolio(price, volume) if
			 * (sellOrderType) then if (portfolio.volume < order.volume)
			 * updateOrder removePortfolioEntry updateCash
			 */

			if (!curOrder.getMatched()) {
				// Update user's cash
				if (curOrder.getOrderType()) { // buyOrder Type
					double validCash = user.getCash() - curOrder.getValue();
					if (validCash >= 0)
						user.setCash(validCash);
					else {
						curOrder.setVolume(user.getCash() / curOrder.getPrice());
						user.setCash(user.getCash() - curOrder.getValue());
					}
				}
				
				if (curOrder.getVolume() > 0) {
					// Update user's portfolio
					int i = 0;
					PortfolioEntry curPortfolioEntry = null;
					for (i = 0; i < curPortfolioList.size(); i++) {
						curPortfolioEntry = curPortfolioList.get(i);
						if (curPortfolioEntry.getAsset().getAssetID() == curOrder
								.getAsset().getAssetID())
							break;
					}

					// Portfolio does not have asset in the Order
					if ((i >= curPortfolioList.size())
							&& (curOrder.getOrderType())) { // buyOrder Type
						curPortfolioList.add(new PortfolioEntry(curOrder
								.getAsset(), curOrder.getPrice(), curOrder
								.getVolume()));
						curOrder.setMatched(true);
					} else {
						// Portfolio has the same asset in the Order
						if (i < curPortfolioList.size()) {
							if (curOrder.getOrderType()) { // buyOrder Type
								curPortfolioEntry.updatePortfolio(curOrder
										.getVolume(), curOrder.getPrice());
							} else { // sellOrder Type
								if (curPortfolioEntry.updatePortfolio(-curOrder
										.getVolume(), curOrder.getPrice()) > 0) {
									curOrder.setVolume(curPortfolioEntry
											.getVolume());
									curPortfolioList.remove(curPortfolioEntry);
								}
								user.setCash03(user.getCash03()
										+ curOrder.getValue());
							}
							curOrder.setMatched(true);
						}
					}
				}
			}
		}
	}

	public void executeAlgorithmOrder() {
		if (curOrderList.get(0).getVolume() == -1) {
			for (Order curOrder : curOrderList) {
				// Update Cash
				if (curOrder.getOrderType()) { // buy all
					curOrder.setVolume(user.getCash() / curOrder.getPrice());
					user.setCash(user.getCash() - curOrder.getValue());
				}

				if (curOrder.getVolume() > 0 || curOrder.getVolume() == -1) {
					// Update user's portfolio
					int i = 0;
					PortfolioEntry curPortfolioEntry = null;
					for (i = 0; i < curPortfolioList.size(); i++) {
						curPortfolioEntry = curPortfolioList.get(i);
						if (curPortfolioEntry.getAsset().getAssetID() == curOrder
								.getAsset().getAssetID())
							break;
					}

					// Portfolio does not have asset in the Order
					if ((i >= curPortfolioList.size())
							&& (curOrder.getOrderType())) { // buyOrder
						// Type
						curPortfolioList.add(new PortfolioEntry(curOrder
								.getAsset(), curOrder.getPrice(), curOrder
								.getVolume()));
						curOrder.setMatched(true);
					} else {

						// Portfolio has the same asset in the Order
						if (i < curPortfolioList.size()) {
							if (curOrder.getOrderType()) { // buyOrder Type
								curPortfolioEntry.updatePortfolio(curOrder
										.getVolume(), curOrder.getPrice());
							} else { // sellOrder Type
								if (curPortfolioEntry.updatePortfolio(-curPortfolioEntry.getVolume(), curOrder.getPrice()) > 0) {
									curOrder.setVolume(curPortfolioEntry
											.getVolume());
									curPortfolioList.remove(curPortfolioEntry);
								}
								user.setCash(user.getCash()
										+ curOrder.getValue());
							}
							curOrder.setMatched(true);
						}
					}
				}
			}
		} else {
			executeOrder();
                }
	}

	public double portfolioValueAtDate(Date date) {
		double totalCash = 0;
		PortfolioManager portfolioManager = new PortfolioManager();
		PriceManager priceManager = new PriceManager();

		ArrayList<PortfolioEntity> portfolioEntityList = portfolioManager
				.getPortfoliosOfUserIDUntilDate(user.getUserID(), date);

		if (portfolioEntityList != null && portfolioEntityList.size() > 0) {
			Date curDate = portfolioEntityList.get(0).getDate();
			for (PortfolioEntity portfolioEntity : portfolioEntityList) {

				double curPrice = priceManager.getPriceByAssetIDAndDate(
						portfolioEntity.getAssetID(), curDate).getClose();
				totalCash += curPrice * portfolioEntity.getVolume();
			}
		}
		return totalCash;
	}
	
	public double portfolioBuyValue() {
		double totalCash = 0;
		for (PortfolioEntry curPortfolioEntry : curPortfolioList) {
			totalCash += curPortfolioEntry.getBuyPrice()* curPortfolioEntry.getVolume();
		}
		return totalCash;
	}
	
	public double portfolioCurrentValue() {
		double totalCash = 0;
		for (PortfolioEntry curPortfolioEntry : curPortfolioList) {
			totalCash += curPortfolioEntry.getCurrentPrice() * curPortfolioEntry.getVolume();
		}
		return totalCash;
	}

	/**
	 * @return cash that used in trading until present
	 */
//	public double spentCash() {
//		OrderManager orderManager = new OrderManager();
//		ArrayList<OrderEntity> orderEntityList = orderManager
//				.getOrderByUserID(user.getUserID());
//		double spentCash = 0;
//		for (OrderEntity orderEntity : orderEntityList) {
//			if (orderEntity.getOrderType()) // buy
//				spentCash -= orderEntity.getPrice() * orderEntity.getVolume();
//			else
//				spentCash += orderEntity.getPrice() * orderEntity.getVolume();
//		}
//		return spentCash;
//	}

	/**
	 * @param date
	 * @return cash that used in trading until a specific date
	 */
//	public double spentCash(Date date) {
//		OrderManager orderManager = new OrderManager();
//		ArrayList<OrderEntity> orderEntityList = orderManager
//				.getOrderUntilDateOfUserID(user.getUserID(), date);
//		double spentCash = 0;
//		for (OrderEntity orderEntity : orderEntityList) {
//			if (orderEntity.getOrderType()) // buyOrder Type
//				spentCash -= orderEntity.getPrice() * orderEntity.getVolume();
//			else
//				spentCash += orderEntity.getPrice() * orderEntity.getVolume();
//		}
//		return spentCash;
//	}

	/**
	 * @param date
	 * @return cash in a specific date between begin date and present 
	 */
//	public double getCashByDate(Date date) {
//		return user.getCash() + this.spentCash(date);
//	}

//	public double initialCash() {
//		return user.getCash() - this.spentCash();
//	}

	/**
	 * @param date
	 * @return total capital = cash + porfolio value
	 */
//	public double getCapitalByDate(Date date) {
//		return getCashByDate(date) + portfolioValueAtDate(date);
//	}

	/**
	 * @return
	 */
	public double profit() {
		double curCapital = getTotalCash() + portfolioCurrentValue();
		
		//System.out.println(initialCapital + " " + curCapital);
		//System.out.println((curCapital - initialCapital) / initialCapital);
		return (curCapital - initialCapital) / initialCapital * 100;
	}
	
	public void updatePortfolioCurrentPrice (Date date) {
		for (PortfolioEntry curPortfolioEntry : curPortfolioList) {
			curPortfolioEntry.updateCurrentPriceToDate(date);
		}
	}

	public void removeFromDatabase() {
		(new UserManager()).delete(user.getUserID());
	}

	public void setName(String name) {
		this.user.setName(name);
	}

        public String getName(){
            return this.user.getName();
        }
        
	public long getUserID() {
		return user.getUserID();
	}

	/**
	 * Add a user to database. <li>Note: this method update database
	 */
	public long add() {
		UserManager userManager = new UserManager();
		userManager.add(this.user);
		return this.user.getUserID();
	}

	public void addOrder(Order order) {
		curOrderList.add(order);
	}

	public void removeOrder(Order order) {
		curOrderList.remove(order);
	}

	public void addPortfolio(PortfolioEntry portfolio) {
		curPortfolioList.add(portfolio);
	}

	public void removePortfolio(PortfolioEntry portfolio) {
		curPortfolioList.remove(portfolio);
	}

	public double getCash() {
		return user.getCash();
	}
        
	public double getCash01() {
		return user.getCash01();
	}

        public double getCash02() {
		return user.getCash02();
	}

        public double getCash03() {
		return user.getCash03();
	}

	@Override
	public String toString() {
		return user.getName();
	}

	public ArrayList<Order> getCurOrderList() {
		return curOrderList;
	}
	
	public ArrayList<OrderEntity> getCurOrderEntityList() {
		ArrayList<OrderEntity> curOrderEntityList = new ArrayList<OrderEntity>();
		for (Order curOrder : curOrderList) {
			curOrderEntityList.add(new OrderEntity(curOrder.getOrderType(),
					user.getUserID(), null, curOrder.getAsset().getAssetID(),
					curOrder.getPrice(), curOrder.getVolume(), true));
		}
		return curOrderEntityList;
	}

	public void setCurOrderList(ArrayList<Order> orderList) {
		this.curOrderList = orderList;
	}

	public ArrayList<PortfolioEntry> getCurPortfolioList() {
		return curPortfolioList;
	}


	public void setLatestTradingDate(Date latestTradingDate) {
		this.latestTradingDate = latestTradingDate;
	}

	public Date getLatestTradingDate() {
		return latestTradingDate;
	}
	
        public ArrayList<TotalAssetEntity> getTotalAssetList() {
            return totalAssetList;
        }
	/**
	 * only use this function iff user & his portfilio are first created
	 */
//	public void setInitialCapital() {
//		initialCapital = user.getCash() + portfolioBuyValue();
//	}

    public double getTotalCash() {
        return user.getCash() + user.getCash01() + user.getCash02() + user.getCash03();
    }

}
