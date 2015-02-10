package dataAccess.databaseManagement.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataAccess.databaseManagement.ConnectionFactory;
import dataAccess.databaseManagement.entity.FinanceReportEntity;

public class FinanceReportManager {
	private Connection connection = null;
	private PreparedStatement ptmt = null;
	private ResultSet resultSet = null;
	
	public FinanceReportManager(){
		
	}
	
	private Connection getConnection() throws SQLException {
		Connection conn;
		conn = ConnectionFactory.getInstance().getConnection();
		return conn;
	}
	
	public void add(FinanceReportEntity financeReportEntity) {
		String queryString = "INSERT INTO report(report_id, asset_id, quarter, year, shares_outstanding, EPS, current_asset, " +
				"cash_and_cash_equivalents, " +
				"shortterm_financial_investment, shortterm_account_receivables, " +
				"inventory, other_current_assets, non_current_assets, " +
				"longterm_account_receivable, fixed_assets, tangible_fixed_assets_accumulated_depreciation, " +
				"leasing_fixed_assets_accumulated_depreciation,	intangible_fixed_assets_accumulated_depreciation, " +
				"goodwills, " +
				"real_estate_investment, longterm_financial_investments, other_longterm_assets, " +
				"total_asset, " +
				"liabilities, shortterm_liabilities, longterm_liabilities, provision, other_payable, " +
				"owners_equity, expenditures_and_other_funds, owners_capitals, minority_interest, " +
				"total_equity, " +
				"gross_sale_revenues, deduction_revenues, " +
				"net_sales, cost_of_goods_sold, gross_profit, financial_activities_revenues," +
				"financial_expenses, selling_expenses, managing_expenses, net_profit_from_operating_activities, " +
				"other_incomes, other_expenses, other_profits, " +
				"total_profit_before_tax, corporate_income_tax_expenses, " +
				"profit_after_corporate_income_tax, benefits_of_minority_shareholders " +
				") VALUES(" +
				"?,?,?,?,?,?,?,?,?,?," +
				"?,?,?,?,?,?,?,?,?,?," +
				"?,?,?,?,?,?,?,?,?,?," +
				"?,?,?,?,?,?,?,?,?,?," +
				"?,?,?,?,?,?,?,?,?,?)";
		
		try {
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString,
					Statement.RETURN_GENERATED_KEYS);

			ptmt.setNull(1, java.sql.Types.INTEGER);
			ptmt.setLong(2, financeReportEntity.getAssetID());
			ptmt.setInt(3, financeReportEntity.getQuater());
			ptmt.setInt(4, financeReportEntity.getYear());
			ptmt.setLong(5, financeReportEntity.getSharesOutstanding());
			ptmt.setDouble(6, financeReportEntity.getEPS());
			ptmt.setDouble(7, financeReportEntity.getCurrentAssets());
			ptmt.setDouble(8, financeReportEntity.getCashAndCashEquivalents());
			ptmt.setDouble(9, financeReportEntity.getShortTermFinancialInvestment());
			ptmt.setDouble(10, financeReportEntity.getShortTermAccountReceivables());
			ptmt.setDouble(11, financeReportEntity.getInventory());
			ptmt.setDouble(12, financeReportEntity.getOtherCurrentAssets());
			ptmt.setDouble(13, financeReportEntity.getNonCurrentAssets());
			ptmt.setDouble(14, financeReportEntity.getLongTermAccountReceivable());
			ptmt.setDouble(15, financeReportEntity.getFixedAssets());
			ptmt.setDouble(16, financeReportEntity.getTangibleFixedAssetsAccumulatedDepreciation());
			ptmt.setDouble(17, financeReportEntity.getLeasingFixedAssetsAccumulatedDepreciation());
			ptmt.setDouble(18, financeReportEntity.getIntangibleFixedAssetsAccumulatedDepreciation());
			ptmt.setDouble(19, financeReportEntity.getGoodwills());
			ptmt.setDouble(20, financeReportEntity.getRealEstateInvestment());
			ptmt.setDouble(21, financeReportEntity.getLongTermFinancialInvestments());
			ptmt.setDouble(22, financeReportEntity.getOtherLongTermAssets());
			ptmt.setDouble(23, financeReportEntity.getTotalAsset());
			ptmt.setDouble(24, financeReportEntity.getLiabilities());
			ptmt.setDouble(25, financeReportEntity.getShortTermLiabilities());
			ptmt.setDouble(26, financeReportEntity.getLongTermLiabilities());
			ptmt.setDouble(27, financeReportEntity.getProvision());
			ptmt.setDouble(28, financeReportEntity.getOtherPayable());
			ptmt.setDouble(29, financeReportEntity.getOwnersEquity());
			ptmt.setDouble(30, financeReportEntity.getExpendituresAndOtherFunds());
			ptmt.setDouble(31, financeReportEntity.getOwnersCapital());
			ptmt.setDouble(32, financeReportEntity.getMinitoryInterest());
			ptmt.setDouble(33, financeReportEntity.getTotalEquity());
			ptmt.setDouble(34, financeReportEntity.getGrossSaleRevenues());
			ptmt.setDouble(35, financeReportEntity.getDeductionRevenues());
			ptmt.setDouble(36, financeReportEntity.getNetSales());
			ptmt.setDouble(37, financeReportEntity.getCostOfGoodsSold());
			ptmt.setDouble(38, financeReportEntity.getGrossProfit());
			ptmt.setDouble(39, financeReportEntity.getFinancialActivitiesRevenues());
			ptmt.setDouble(40, financeReportEntity.getFinancialExpenses());
			ptmt.setDouble(41, financeReportEntity.getSellingExpenses());
			ptmt.setDouble(42, financeReportEntity.getManagingExpenses());
			ptmt.setDouble(43, financeReportEntity.getNetProfitFromOperatingActivities());
			ptmt.setDouble(44, financeReportEntity.getOtherIncomes());
			ptmt.setDouble(45, financeReportEntity.getOtherExpenses());
			ptmt.setDouble(46, financeReportEntity.getOtherProfits());
			ptmt.setDouble(47, financeReportEntity.getTotalProfitBeforeTax());
			ptmt.setDouble(48, financeReportEntity.getCorporateIncomeTaxExpenses());
			ptmt.setDouble(49, financeReportEntity.getProfitAfterCorporateIncomeTax());
			ptmt.setDouble(50, financeReportEntity.getBenefitsOfMinitoryShareholders());
			ptmt.executeUpdate();

			ResultSet rs = ptmt.getGeneratedKeys();
			long autoIncValue = -1;

			if (rs.next()) {
				autoIncValue = rs.getLong(1);
			}
			financeReportEntity.setReportID(autoIncValue);

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
	
	public ArrayList<FinanceReportEntity> getReportByAssetID(long assetID) {
		try {
						
			String queryString = "SELECT * FROM report WHERE asset_id=?";
			connection = getConnection();
			ptmt = connection.prepareStatement(queryString);
			ptmt.setLong(1, assetID);
			resultSet = ptmt.executeQuery();

			ArrayList<FinanceReportEntity> reportList = new ArrayList<FinanceReportEntity>();

			while (resultSet.next()) {
				FinanceReportEntity financeReportEntity = new FinanceReportEntity();

				financeReportEntity.setReportID(resultSet.getLong("report_id"));
				financeReportEntity.setAssetID(resultSet.getLong("asset_id"));
				financeReportEntity.setQuater(resultSet.getInt("quarter"));
				financeReportEntity.setYear(resultSet.getInt("year"));
				financeReportEntity.setSharesOutstanding(resultSet.getLong("shares_outstanding"));
				financeReportEntity.setEPS(resultSet.getDouble("EPS"));
				financeReportEntity.setCurrentAssets(resultSet.getDouble("current_asset"));
				financeReportEntity.setCashAndCashEquivalents(resultSet.getDouble("cash_and_cash_equivalents"));
				financeReportEntity.setShortTermFinancialInvestment(resultSet.getDouble("shortterm_financial_investment"));
				financeReportEntity.setShortTermAccountReceivables(resultSet.getDouble("shortterm_account_receivables"));
				financeReportEntity.setInventory(resultSet.getDouble("inventory"));
				financeReportEntity.setOtherCurrentAssets(resultSet.getDouble("other_current_assets"));
				financeReportEntity.setNonCurrentAssets(resultSet.getDouble("non_current_assets"));
				financeReportEntity.setLongTermAccountReceivable(resultSet.getDouble("longterm_account_receivable"));
				financeReportEntity.setFixedAssets(resultSet.getDouble("fixed_assets"));
				financeReportEntity.setTangibleFixedAssetsAccumulatedDepreciation(resultSet.getDouble("tangible_fixed_assets_accumulated_depreciation"));
				financeReportEntity.setLeasingFixedAssetsAccumulatedDepreciation(resultSet.getDouble("leasing_fixed_assets_accumulated_depreciation"));
				financeReportEntity.setIntangibleFixedAssetsAccumulatedDepreciation(resultSet.getDouble("intangible_fixed_assets_accumulated_depreciation"));
				financeReportEntity.setGoodwills(resultSet.getDouble("goodwills"));
				financeReportEntity.setRealEstateInvestment(resultSet.getDouble("real_estate_investment"));
				financeReportEntity.setLongTermFinancialInvestments(resultSet.getDouble("longterm_financial_investments"));
				financeReportEntity.setOtherLongTermAssets(resultSet.getDouble("other_longterm_assets"));
				financeReportEntity.setTotalAsset(resultSet.getDouble("total_asset"));
				financeReportEntity.setLiabilities(resultSet.getDouble("liabilities"));
				financeReportEntity.setShortTermLiabilities(resultSet.getDouble("shortterm_liabilities"));
				financeReportEntity.setLongTermLiabilities(resultSet.getDouble("longterm_liabilities"));
				financeReportEntity.setProvision(resultSet.getDouble("provision"));
				financeReportEntity.setOtherPayable(resultSet.getDouble("other_payable"));

				financeReportEntity.setOwnersEquity(resultSet.getDouble("owners_equity"));
				financeReportEntity.setExpendituresAndOtherFunds(resultSet.getDouble("expenditures_and_other_funds"));
				financeReportEntity.setOwnersCapital(resultSet.getDouble("owners_capitals"));
				financeReportEntity.setMinorityInterest(resultSet.getDouble("minority_interest"));
				financeReportEntity.setTotalEquity(resultSet.getDouble("total_equity"));
				financeReportEntity.setGrossSaleRevenues(resultSet.getDouble("gross_sale_revenues"));
				financeReportEntity.setDeductionRevenues(resultSet.getDouble("deduction_revenues"));
				financeReportEntity.setNetSales(resultSet.getDouble("net_sales"));
				financeReportEntity.setCostOfGoodsSold(resultSet.getDouble("cost_of_goods_sold"));
				financeReportEntity.setGrossProfit(resultSet.getDouble("gross_profit"));
				financeReportEntity.setFinancialActivitiesRevenues(resultSet.getDouble("financial_activities_revenues"));
				financeReportEntity.setFinancialExpenses(resultSet.getDouble("financial_expenses"));
				financeReportEntity.setSellingExpenses(resultSet.getDouble("selling_expenses"));
				financeReportEntity.setManagingExpenses(resultSet.getDouble("managing_expenses"));
				financeReportEntity.setNetProfitFromOperatingActivities(resultSet.getDouble("net_profit_from_operating_activities"));
				financeReportEntity.setOtherIncomes(resultSet.getDouble("other_incomes"));
				financeReportEntity.setOtherExpenses(resultSet.getDouble("other_expenses"));
				financeReportEntity.setOtherProfits(resultSet.getDouble("other_profits"));
				financeReportEntity.setTotalProfitBeforeTax(resultSet.getDouble("total_profit_before_tax"));
				financeReportEntity.setCorporateIncomeTaxExpenses(resultSet.getDouble("corporate_income_tax_expenses"));
				financeReportEntity.setProfitAfterCorporateIncomeTax(resultSet.getDouble("profit_after_corporate_income_tax"));
				financeReportEntity.setBenefitsOfMinitoryShareholders(resultSet.getDouble("benefits_of_minority_shareholders"));
											
				reportList.add(financeReportEntity);
			}

			return reportList;
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
