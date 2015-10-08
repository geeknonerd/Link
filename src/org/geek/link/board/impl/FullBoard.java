package org.geek.link.board.impl;

import java.util.ArrayList;
import java.util.List;

import org.geek.link.board.AbstractBoard;
import org.geek.link.object.GameConf;
import org.geek.link.view.Piece;

public class FullBoard extends AbstractBoard {
	protected List<Piece> createPieces(GameConf config, Piece[][] pieces) {
		List<Piece> notNullPieces = new ArrayList<Piece>();
		for(int i=1;i<pieces.length-1;i++){
			for(int j=1;j<pieces[i].length-1;j++){
				Piece piece = new Piece(i,j);
				notNullPieces.add(piece);
			}
		}
		return notNullPieces;
	}
	
}
