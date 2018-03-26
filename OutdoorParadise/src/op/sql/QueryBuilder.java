package op.sql;

import java.util.LinkedList;

public class QueryBuilder {

	private LinkedList<IQueryEntry> entries = new LinkedList<IQueryEntry>();
	
	public QueryBuilder(){}
	
	public void add(IQueryEntry entry){
		entries.add(entry);
	}
	
	public String buildQueryString(){
		String query = "";
		
		for(int i = 0; i < entries.size(); i++){
			query = query + entries.get(i).getEntryString() + (i + 1 < entries.size() ? " " : "");
		}
		
		entries.clear();
		return query;
	}
	
	public static class QueryEntrySelect implements IQueryEntry{

		private final String selector;
		
		public QueryEntrySelect(String selector){
			this.selector = selector;
		}
		
		@Override
		public String getEntryString() {
			return "SELECT " + selector;
		}
		
	}
	
	public static class QueryEntryFrom implements IQueryEntry{

		private final String s;
		
		public QueryEntryFrom(String s){
			this.s = s;
		}
		
		@Override
		public String getEntryString() {
			return "FROM " + s;
		}
		
	}
}
