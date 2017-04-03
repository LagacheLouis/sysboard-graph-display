package json;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Link {
	public Map<String, String>  sourceprop;
	public Map<String, String>  targetprop;
    
	
	public Link() {
            
            // TODO Auto-generated constructor stub
    }

	
    public Link(Map<String, String>  sourceprop, Map<String, String>  targetprop) {
		super();
		this.sourceprop = sourceprop;
		this.targetprop = targetprop;
	}


	@Override
    public String toString() {
		return "\t{\"source\":{"+genJSONfromMap(sourceprop)+"},\"target\":{"+genJSONfromMap(targetprop) +"},\"type\":\"suit\"}";
    }
    
	public String genJSONfromMap(Map<String, String> map){
		String json="";
		
		Set<Entry<String, String>> setMap = map.entrySet();
	      Iterator<Entry<String, String>> iterator = setMap.iterator();
	      while(iterator.hasNext()){
	         Entry<String, String> entry = iterator.next();
	         json += displayProp(entry);
	         if(iterator.hasNext())
	        	 json +=" , ";
	      }
	      
		return json;
	}
	
    public String displayProp(Entry<String, String> entry){
		return "\"" +entry.getKey()+"\" : \"" + entry.getValue() + "\"";
    }
}
