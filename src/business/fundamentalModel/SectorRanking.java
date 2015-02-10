package business.fundamentalModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;
import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.manager.AssetManager;



public class SectorRanking {
	private static final int TOP = 5;
	
	public static final String[] SECTOR_LIST = { "bank", "insurance", "real_estate", "food_drink", "construction"};
	
//	public static final ArrayList<String> TEST = new ArrayList<String>(Arrays.asList("NHC", "VTS", "DTC", "ACL", "VNM", "SSC", "BMI", "PVI", "SZL", "TDH", "NTL"));
//	public static final ArrayList<String> TEST = new ArrayList<String>(Arrays.asList("DTC", "VTS", "DAC", "VNM", "SSC", "ABT", "BMI", "BVH", "PVI", "NTL", "EFI", "VIC"));
//	public static final ArrayList<String> TEST = new ArrayList<String>(Arrays.asList("NNC", "NHC", "DTC", "KTS", "VNM", "ASM", "PTI", "PVI", "BMI", "NTL", "D11", "HDG"));
//	public static final ArrayList<String> TEST = new ArrayList<String>(Arrays.asList("CTG", "STB", "EIB"));
	public static final ArrayList<String> TEST = new ArrayList<String>(Arrays.asList("CTG","VCB", "EIB"));
	public static final ArrayList<String> BANK = new ArrayList<String>(Arrays.asList("ACB", "CTG", "HBB", "SHB", "STB", "EIB", "VCB", "NVB"));
	public static final ArrayList<String> INSURANCE = new ArrayList<String>(Arrays.asList("BMI", "BVH", "PTI", "PVI"
			, "VNR"
			));
	public static final ArrayList<String> REALESTATE = new ArrayList<String>(
			Arrays.asList("API", "BCI", "CCI", "CCL", "CLG", "CSC", "D11",
					"D2D", "DIG", "DLR", "DRH", "DTA", "DXG", "EFI", "FDC",
					"HAG", "HDC", "HDG", "HQC", "IDJ", "IDV", "IJC", "ITA", "ITC",
					"KAC", "KBC", "KDH","KHA", "LGL",  "LHG", "NBB", "NDN", "NTB",
					"NTL", "NVN", "NVT", "PDR", "PFL", "PIV", "PPI", "PTL", "PV2",
					"PVL", "PVR", "PXA", "PXL", "QCG", "RCL", "SCR", "SJS", "SZL",
					"TDH", "TIG", "TIX", "V11", "VC3", "VCR", "VIC", "VNI", "VPH", "VRC"));
	public static final ArrayList<String> FOODDRINK = new ArrayList<String>(
			Arrays.asList(
					"AAM", "ABT", "ACL", "AGC", "AGD", "AGF", "ANV",
					"ASM", "ATA", "AVF", "BAS", "BBC", "BHS", "BLF", "CAD",
					"CAN", "CMX", "DBC", "FBT", "FDG", "FMC", "GFC", "HAD",
					"HAT", "HHC", "HNM", "HVG", "ICF", "IFS", "KDC", "KTS",
					"LAF", "LSS", "MCF", "MPC", "MSN", "NGC", "NHS", "NSC",
					"SAF", "SBT", "SCD", "SEC"
					, "SGC"
					, "SJ1", "SSC", "TAC",
					"THB", "THV", "TNA", "TRI", "TS4", "VCF", "VDL", "VHC",
					"VLF", "VNH", "VNM", "VTF", "VTL"
					));
	
	public static final ArrayList<String> CONSTRUCTION = new ArrayList<String>(
			Arrays.asList("B82", "BCC", "BCE", "BHC", "BHT", "BHV", "BMP",
					"BT6", "BTS", "C47", "C92", "CCM", "CDC", "CIC", "CID",
					"CII", "CNT", "CT6", "CTA", "CTD", "CTI", "CTN", "CVN",
					"CVT", "CX8", "CYC", "DAC", "DC2", "DC4", "DCC", "DCT",
					"DHA", "DIC", "DID", "DIH", "DNP", "DTC", "DXV", "FPC",
					"HAS", "HBC", "HCC", "HCT", "HDA", "HHL", "HLY", "HOM",
					"HPR",
					"HPS", "HT1", "HTB", "HTI", "HU3", "HUT", "HVX",
					"ICG", "KBT", "KTT", "L18", "LBM", "LCD", "LCG", "LCS",
					"LHC", "LIG", "LM3", "LM8", "LUT", "MCC", "MCG", "MCL",
					"MCO", "MCV", "MDG", "MEC", "NAV", "NHA", "NHC", "NNC",
					"NSN", 
					"NTP", 
					"PHC", "PHH", "PPG", "PSG", "PTC", "PVA",
					"PVV", "PVX", "PXI", "PXM", "PXS", "PXT", "QCC", "QNC",
					"QTC", "S12", "S27", "S55", "S64", "S74", "S91", 
					"S96",
					"S99", "SAV", "SC5", "SCC", "SCJ", "SCL", "SD1", "SD2",
					"SD3", "SD4", "SD5", "SD6", "SD7", "SD8", "SD9", "SDB",
					"SDD", "SDE", "SDH", "SDJ", "SDN", "SDP", "SDS", "SDT",
					"SDU", "SDY", "SEL", "SHN", "SIC", "SJC", "SJE", "SJM",
					"SKS", "SNG", "SSS", "STL", "TBX", "TCR", "TDC", "TKC",
					"TKU", "TLT", "TMX", "TSM", "TTC", "TV3", "TXM", "UDC",
					"UIC", "V12", "V15", "V21", "VC1", "VC2", "VC5", "VC6",
					"VC7", "VC9", "VCC", "VCG", "VCH", "VCS", "VE1", "VE2",
					"VE3", "VE9", "VHH", "VHL", "VIT", "VMC", "VNE", "VSI",
					"VTA", "VTS", "VTV", "VXB", "XMC", "YBC"));

	
	String sectorName;
	ArrayList<AssetStat> asStatList = new ArrayList<AssetStat>();
	ArrayList<Integer> scoreList = new ArrayList<Integer>();

	public SectorRanking(String sectorName, Date date) {
		this.sectorName = sectorName;
		SectorRanking tempSectorRanking = null;
		ArrayList<String> assL = null;
		if (sectorName == "bank")
			assL = BANK;
		if (sectorName == "insurance")
			assL = INSURANCE;
		if (sectorName == "real_estate")
			assL = REALESTATE;
		if (sectorName == "food_drink")
			assL = FOODDRINK;
		if (sectorName == "construction")
			assL = CONSTRUCTION;
		
			
		tempSectorRanking = new SectorRanking(sectorName, assL, date);
		this.asStatList = tempSectorRanking.getAsStatList();
		this.scoreList = tempSectorRanking.getScoreList();
		
		for (int i = 0; i < asStatList.size(); i++) {
			asStatList.get(i).getStatList().put("Score", (double) (scoreList.get(i)));
		}
			
	}
	
	
	public SectorRanking(String sectorName, ArrayList<String> assetList, Date date) {
		this.sectorName = sectorName;

		/*
		 * get asStatList
		 */
		AssetManager assetManager = new AssetManager();
		for (String curSymbol : assetList) {
			ArrayList<AssetEntity> asList = assetManager.getAssetsBySymbol(curSymbol); 
			if ( asList != null) {
				// TODO add date to asset
				for (AssetEntity curAsset : asList) {
					if (AssetStat.isHOSEorHASTC(curAsset) == 1) {
						AssetStat asStat = new AssetStat(curAsset, date);
						System.out.println(asStat.getAsset().getSymbol());
						if (asStat.isValid("Revenue") ==1)
							asStatList.add(asStat);
					}
				}
			}
		}
		
		
		/*
		 * get scoreList
		 */
		for (AssetStat curAsset : asStatList) {
			int score = 0;
			if (sectorName.equals("bank")) {
				score = getNimScore(curAsset.getAsset().getSymbol()) * 5 
						+ getBadDebtScore(curAsset.getAsset().getSymbol()) * 4
						+ getPEScore(curAsset.getStatList().get("P/E"), asStatList) * 4
						+ getROAScore(curAsset.getStatList().get("ROA"), asStatList) * 3
						+ getROEScore(curAsset.getStatList().get("ROE"), asStatList) * 3
						+ getEAScore(curAsset.getStatList().get("Equity/Asset"), asStatList) * 3;
			} else
				score = getROEScore(curAsset.getStatList().get("ROE"), asStatList) * 5 
				+ getPEScore(curAsset.getStatList().get("P/E"), asStatList) * 5
				+ getROAScore(curAsset.getStatList().get("ROA"), asStatList) * 4
				+ getERScore(curAsset.getStatList().get("EBITDA/Revenue"), asStatList) * 3 	 
				+ getDEScore(curAsset.getStatList().get("TotalDebt/Equity")) * 3;
			
			scoreList.add(score);
		}
		
		
		/*
		 * Sorting
		 */
		for(int i = 0; i <scoreList.size(); i++) {
			for(int j = i + 1; j <scoreList.size(); j++) {
				if (scoreList.get(i) < scoreList.get(j)) {
					Collections.swap(scoreList, i, j);
					Collections.swap(asStatList, i, j);
				}
			}
		}
		
		
	}
	
	
	
	public int getPEScore(Double score, ArrayList<AssetStat> asStatList) {
		Double minScore = asStatList.get(0).getStatList().get("P/E");
		for (AssetStat curAsset : asStatList) {
			Double curPE = curAsset.getStatList().get("P/E");
			if (minScore > curPE)
				minScore = curPE;
		}
		
		if (score <= minScore * 1.1)
			return 5;
		if (score <= minScore * 1.2)
			return 4;
		if (score <= minScore * 1.3)
			return 3;
		if (score <= minScore * 1.4)
			return 2;
		if (score <= minScore * 1.5)
			return 1;
		return 0;
	}
	
	
	public int getROEScore(Double score, ArrayList<AssetStat> asStatList) {
		Double maxScore = 0.0;
		for (AssetStat curAsset : asStatList) {
			Double curROE = curAsset.getStatList().get("ROE");
			if (maxScore < curROE)
				maxScore = curROE;
		}
		
		if (score >= maxScore * .9)
			return 5;
		if (score >= maxScore * .8)
			return 4;
		if (score >= maxScore * .7)
			return 3;
		if (score >= maxScore * .6)
			return 2;
		if (score >= maxScore * .5)
			return 1;
		return 0;
	}
	
	public int getROAScore(Double score, ArrayList<AssetStat> asStatList) {
		Double maxScore = 0.0;
		for (AssetStat curAsset : asStatList) {
			Double curROE = curAsset.getStatList().get("ROA");
			if (maxScore < curROE)
				maxScore = curROE;
		}
		
		if (score >= maxScore * .9)
			return 5;
		if (score >= maxScore * .8)
			return 4;
		if (score >= maxScore * .7)
			return 3;
		if (score >= maxScore * .6)
			return 2;
		if (score >= maxScore * .5)
			return 1;
		return 0;
	}
	
	public int getERScore(Double score, ArrayList<AssetStat> asStatList) {
		Double maxScore = 0.0;
		for (AssetStat curAsset : asStatList) {
			Double curER = curAsset.getStatList().get("EBITDA/Revenue");
			if (maxScore < curER)
				maxScore = curER;
		}
		
		if (score >= maxScore * .9)
			return 5;
		if (score >= maxScore * .8)
			return 4;
		if (score >= maxScore * .7)
			return 3;
		if (score >= maxScore * .6)
			return 2;
		if (score >= maxScore * .5)
			return 1;
		return 0;
	}
	
	public int getDEScore(Double score) {
		if (score <= 0.1)
			return 5;
		if (score <= 0.2)
			return 4;
		if (score <= 0.3)
			return 3;
		if (score <= 0.4)
			return 2;
		if (score <= 0.5)
			return 1;
		return 0;
	}
	
	// bank
	public int getEAScore(Double score, ArrayList<AssetStat> asStatList) {
		Double maxScore = 0.0;
		for (AssetStat curAsset : asStatList) {
			Double curEA = curAsset.getStatList().get("Equity/Asset");
			if (maxScore < curEA)
				maxScore = curEA;
		}
		
		if (score >= maxScore * .9)
			return 5;
		if (score >= maxScore * .8)
			return 4;
		if (score >= maxScore * .7)
			return 3;
		if (score >= maxScore * .6)
			return 2;
		if (score >= maxScore * .5)
			return 1;
		return 0;
	}
	
	// bank
	public int getBadDebtScore(String symbol) {
		Double score = 0.0, minScore = 0.09; 
		if (symbol.equals("ACB"))
			score = 0.2;
		if (symbol.equals("CTG"))
			score = 0.09;
		if (symbol.equals("EIB"))
			score = 0.7;
		if (symbol.equals("HBB"))
			score = 1.87;
		if (symbol.equals("MBB"))
			score = 1.18;
		if (symbol.equals("NVB"))
			score = 1.14;
		if (symbol.equals("SHB"))
			score = 0.82;
		if (symbol.equals("STB"))
			score = 0.21;
		if (symbol.equals("VCB"))
			score = 2.65;
		
		if (score <= minScore * 1.1)
			return 5;
		if (score <= minScore * 1.2)
			return 4;
		if (score <= minScore * 1.3)
			return 3;
		if (score <= minScore * 1.4)
			return 2;
		if (score <= minScore * 1.5)
			return 1;
		return 0;
	}
	
	// bank
	public int getNimScore(String symbol) {
		Double nim = 0.0, maxNim = 4.92;
		
		if (symbol.equals("ACB"))
				nim = 3.11;
		if (symbol.equals("CTG"))
			nim = 4.92;
		if (symbol.equals("EIB"))
			nim = 3.74;
		if (symbol.equals("HBB"))
			nim = 2.13;
		if (symbol.equals("MBB"))
			nim = 4.3;
		if (symbol.equals("NVB"))
			nim = 3.55;
		if (symbol.equals("SHB"))
			nim = 3.45;
		if (symbol.equals("STB"))
			nim = 4.38;
		if (symbol.equals("VCB"))
			nim = 3.66;
		
		
		if (nim >= maxNim * .9)
			return 5;
		if (nim >= maxNim * .8)
			return 4;
		if (nim >= maxNim * .7)
			return 3;
		if (nim >= maxNim * .6)
			return 2;
		if (nim >= maxNim * .5)
			return 1;
		return 0;
	}
	
	
	
	public ArrayList<AssetStat> getTopSymbol(int top) {
		ArrayList<AssetStat> asTopList = new ArrayList<AssetStat>();
		
		for (int i = 0; i < top; i++) {
			if (i == asStatList.size())
				break;
			asTopList.add(asStatList.get(i));
			System.out.println(asStatList.get(i) + "," +  scoreList.get(i));
		}
		
		return asTopList;
	}
	
	
	
	// Sector Ranking
	public static void toFile(Date date) {

		TreeMap<String, SectorRanking> sectorMap = new TreeMap<String, SectorRanking>();

		sectorMap.put("bank", new SectorRanking("bank", BANK, date));
//		sectorMap.put("insurance", new SectorRanking("insurance", INSURANCE, date));
//		sectorMap.put("real_estate", new SectorRanking("real_estate", REALESTATE, date));
//		sectorMap.put("food_drink", new SectorRanking("food_drink", FOODDRINK, date));
//		sectorMap.put("construction", new SectorRanking("construction", CONSTRUCTION, date));
		
		Calendar year = Calendar.getInstance();
		year.setTime(date);
		
		try {
			// Create file
			FileWriter fstream = new FileWriter("Bank_" + year.get(Calendar.YEAR) + ".csv");
			BufferedWriter out = new BufferedWriter(fstream);

			for (String curSector : sectorMap.keySet()) {
				out.write(curSector + ",ROE,P/E,ROA,EBITDA/Revenue,TotalDebt/Equity,Year,Score\n");
				for (int i = 0; i < TOP; i++) {
					if (i == sectorMap.get(curSector).getAsStatList().size())
						break;
					out.write(sectorMap.get(curSector).getAsStatList().get(i) + "," +  sectorMap.get(curSector).getScoreList().get(i) + "\n");
				}
				out.write("\n");
				
			}
			
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
	}
	
	
	public ArrayList<AssetStat> getAsStatList() {
		return asStatList;
	}


	public void setAsStatList(ArrayList<AssetStat> asStatList) {
		this.asStatList = asStatList;
	}
	
	public ArrayList<Integer> getScoreList() {
		return scoreList;
	}



	public void setScoreList(ArrayList<Integer> scoreList) {
		this.scoreList = scoreList;
	}

	public static void toFile(String modelName, ArrayList<AssetStat> asStatList, String exchange, Date sDate, Date eDate) {
		String index = null;
		if (modelName.equals("bank"))
			index = "^NGANHANG";
		if (modelName.equals("construction"))
			index = "^XAYDUNG";
		if (modelName.equals("food_drink"))
			index = "^THUCPHAM";
		if (modelName.equals("insurance"))
			index = "^NGANHANG";
		if (modelName.equals("real_estate"))
			index = "^BATDONGSA";
		
		
		FundamentalEvaluation.toFile(modelName, asStatList,index ,exchange, sDate, eDate);
	}


	public static void main(String[] args) {
		
		
		Calendar ca = Calendar.getInstance();
		ca.set(2010, 2, 31);
		
		

//		toFile (ca.getTime());

		
//		String sector = null; // get sectorname from textbox
//		SectorRanking sectorR = new SectorRanking(sector, ca.getTime());
//		ArrayList<AssetStat> secRankList  =  sectorR.getAsStatList();

		
		
//		TreeMap<String, SectorRanking> sectorMap = new TreeMap<String, SectorRanking>();
//		sectorMap.put("bank", new SectorRanking("bank", BANK, ca.getTime()));
//		sectorMap.put("insurance", new SectorRanking("insurance", INSURANCE, ca.getTime()));
//		sectorMap.put("real_estate", new SectorRanking("real_estate", REALESTATE, ca.getTime()));
//		sectorMap.put("food_drink", new SectorRanking("food_drink", FOODDRINK, ca.getTime()));
//		sectorMap.put("construction", new SectorRanking("construction", CONSTRUCTION, ca.getTime()));


		
		ArrayList<AssetStat> asStatList = new ArrayList<AssetStat>();
		/*
		 * get asStatList
		 */
		AssetManager assetManager = new AssetManager();
		for (String curSymbol : TEST) {
			ArrayList<AssetEntity> asList = assetManager.getAssetsBySymbol(curSymbol); 
			if ( asList != null) {
//				asStatList.add(new AssetStat(asList.get(0)));
				AssetStat asStat = new AssetStat(asList.get(0), ca.getTime());
//				if (asStat.isValid() ==1)
					asStatList.add(asStat);
			}
		}
		
		
		
//		ArrayList<AssetStat> asStatTopList = new ArrayList<AssetStat>();
//		/*
//		 * filter top Assets in each Sector
//		 */
//		for (AssetStat curAssetStat : asStatList) {
//			for (String curSector : sectorMap.keySet()) {
//				if (sectorMap.get(curSector).getTopSymbol(20).contains(curAssetStat.getAsset().getSymbol()))
//					asStatTopList.add(curAssetStat);
//			}
//		}

		
		/*
		 * Simulation
		 */
		
		Calendar bDate = Calendar.getInstance();
		Calendar eDate = Calendar.getInstance();
		if (ca.get(Calendar.YEAR) == 2009) {
			bDate.set(2009, 1, 31);
			eDate.set(2010,2, 30);
		}
		
		if (ca.get(Calendar.YEAR) == 2010) {
			bDate.set(2010,2, 31);
			eDate.set(2011,2, 30);
		}
		
		if (ca.get(Calendar.YEAR) == 2011) {
			bDate.set(2011,2, 31);
			eDate.set(2012,2, 30);	
		}

		
//		toFile(bDate.getTime());
		FundamentalEvaluation.toFile("Bank_S", asStatList,"^NGANHANG" ,"INDEX", bDate.getTime(), eDate.getTime());
		
//		FundamentalEvaluation.toFile("Bank", asStatList,"^HASTC" ,"HASTC", bDate.getTime(), eDate.getTime());
		
	}

}
