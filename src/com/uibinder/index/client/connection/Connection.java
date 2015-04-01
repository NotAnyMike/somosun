package com.uibinder.index.client.connection;

import java.util.ArrayList;
import java.util.List;

import com.uibinder.index.client.widget.LineWidget;
import com.uibinder.index.client.widget.SubjectWidget;

public class Connection {

	private SubjectWidget from;
	private SubjectWidget to;
	private List<LineWidget> lines = new ArrayList<LineWidget>();
	
	public Connection(SubjectWidget from, SubjectWidget to){
		this.from = from;
		this.to = to;
	}
	
	public void addLine(LineWidget l){
		if(lines.contains(l) == false){
			lines.add(l);
		}
	}
	
	public boolean contains(SubjectWidget from, SubjectWidget to){
		if(this.from == from && this.to == to){
			return true;
		}else{
			return false;
		}
	}

	public SubjectWidget getFrom() {
		return from;
	}

	public void setFrom(SubjectWidget from) {
		this.from = from;
	}

	public SubjectWidget getTo() {
		return to;
	}

	public void setTo(SubjectWidget to) {
		this.to = to;
	}

	public List<LineWidget> getLines() {
		return lines;
	}

	public void setLines(List<LineWidget> lines) {
		this.lines = lines;
	}

	public void hideLines() {
		for(LineWidget l : lines){
			l.hide();
		}
	}
	
	public void showLines(){
		for(LineWidget l : lines){
			l.show();
		}
	}

	public void deleteLines() {
		for(LineWidget l : lines){
			l.removeFromParent();
			lines.remove(l);
		}
	}
	
}
