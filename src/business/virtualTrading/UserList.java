package business.virtualTrading;

import dataAccess.databaseManagement.entity.PortfolioEntity;
import dataAccess.databaseManagement.entity.UserEntity;
import dataAccess.databaseManagement.manager.PortfolioManager;
import dataAccess.databaseManagement.manager.UserManager;
import java.util.ArrayList;

public class UserList {
	private ArrayList<User> userList;

	public UserList() {
		UserManager userManager = new UserManager();
		PortfolioManager portfolioManager = new PortfolioManager();
		userList = new ArrayList<User>();
		UserEntity curUser;

		ArrayList<UserEntity> userEntityList = userManager.getAllUsers();
		for (int i = 0; i < userEntityList.size(); i++) {
			curUser = userEntityList.get(i);
			ArrayList<PortfolioEntity> portfolioEntityList = portfolioManager
					.getPortfolio(curUser.getUserID(),
							portfolioManager
									.getPortfolioLatestDateOfUserID(curUser
											.getUserID()));
			ArrayList<PortfolioEntry> portfolioEntryList = new ArrayList<PortfolioEntry>();

			for (PortfolioEntity portfolioEntity : portfolioEntityList) {
	            portfolioEntryList.add(new PortfolioEntry(portfolioEntity));
	        }
			
			
			userList.add(new User(curUser, portfolioEntryList));	
		}
		
		for (User user : userList) {
			user.updatePortfolioCurrentPrice(user.getLatestTradingDate());
		}
	}

	public void removeUser(User user) {
		this.userList.remove(user);
	}

	public void removeUserFromDatabase(User user) {
		user.removeFromDatabase();
		userList.remove(user);
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}
}
