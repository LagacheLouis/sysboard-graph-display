package json;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cern.mpe.systems.core.domain.relation.SystemRelation;
import main.Main;

/**
 * A usefull tool for making a JSON file from a set of relation
 * @author Lagache
 *
 */
public class JSONExporter {
	private static LinkList<Link> list;
	
	/**
	 * Permit to generate the json file for display1
	 * @param relations
	 */
	public static void generateJson(Collection<SystemRelation> relations){
		boolean isFirstTurn = true;
		
		for(SystemRelation rel : relations)
		{
			Map<String, String>  sourceprop = new HashMap<>();
			sourceprop.put("id", IdParser(rel.getSource().getKey().toDbString()));
			sourceprop.put("name", rel.getSource().getName());
			sourceprop.put("type", rel.getSource().getClass().getSimpleName());
			
			Map<String, String>  targetprop = new HashMap<>();;
			targetprop.put("id", IdParser(rel.getTarget().getKey().toDbString()));
			targetprop.put("name", rel.getTarget().getName());
			targetprop.put("type", rel.getTarget().getClass().getSimpleName());
			
			
			Link link=new Link(sourceprop,targetprop);
			if(isFirstTurn){
				isFirstTurn = false;
				list= new LinkList<Link>(link,null);
			}
			else
				list.insert(link,0);
		}
		try (FileWriter file = new FileWriter(Main.path+"\\json.js")) {
			file.write("var data =\n[\n"+list.toString()+"\n]");
			System.out.println("Successfully generate JSON File...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Return the .todbString to a better form 
	 * @param dbString
	 * @return String
	 */
	public static String IdParser(String dbString){
		String[] parts = dbString.split(";");
		String[] typeparts = parts[0].split("=");
		String[] keyparts = parts[1].split("=");
		return typeparts[1]+keyparts[1];
	}
}
