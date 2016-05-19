package models;

import java.sql.ResultSet;
import java.util.ArrayList;

import controllers.DatabaseController;

public class Competition {
	
	private int id;
	private String name;
	private String owner;
	private int membersTotal;
	
	private ArrayList<String> members;
	
	public Competition(int id, String name, String owner, int membersTotal) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.membersTotal = membersTotal;
		
		members = new ArrayList<>();		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public ArrayList<String> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<String> members) {
		this.members = members;
		System.out.println("De members in de competitie: " + this.members + " Size: " + this.members.size());
	}
	
	public void addMember(String member) {
		members.add(member);
	}
	
	public int getMembersTotal() {
		return membersTotal;
	}
	
	public void setMembersTotal(DatabaseController databaseController){
		
		ResultSet rs = databaseController.selectQuery("SELECT COUNT(account_naam) as aantalmembers FROM deelnemer WHERE comp_id = '" + this.id + "'");
		
		
		
		try {
			rs.first();
			
			while (rs.next()) {
				membersTotal  = rs.getInt("aantalmembers");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Er is iets fout gegaan bij het ophalen van het aantal members.");
		}
		
		
	}
	
}
