package ga.brunnofdc.uRanking.Core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Rank implements Comparator<Rank> {

	private int position;
	private double preco;
	private String nome;
	private String id_nome;
	private String tag;
	private List<String> cmds = new ArrayList<>();
	
	public Rank(String id_nome, String nome, String tag, double preco, List<String> cmds,  int position) {
		this.id_nome = id_nome;
		this.preco = preco;
		this.nome = nome;
		this.position = position;
		this.cmds = cmds;
		this.tag = tag;
	}
	
	public int getPosition() {
		return this.position;
	}

	public String getRankName() {
		String name = this.nome.replace("&", "§");
		return name;
		
	}
	
	public String getRankId() {
		
		return this.id_nome;
		
	}
	
	public String getTag() {
		
		return this.tag;
		
	}
	
	public double getPreco() {
		
		return this.preco;
		
	}
	
	public List<String> getCommands() {
		
		return this.cmds;
		
	}
	
	@Override
	public int compare(Rank s1, Rank s2) {
		if (s1.getPosition() > s2.getPosition()) {
			return 1;
		} else if (s1.getPosition() == s2.getPosition()) {
			return 0;
		} else {
			return -1;
		}
	}
}
