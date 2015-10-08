package org.geek.link.board.impl;

import java.util.ArrayList;
import java.util.List;

import org.geek.link.board.AbstractBoard;
import org.geek.link.object.GameConf;
import org.geek.link.view.Piece;

public class HorizontalBoard extends AbstractBoard {
	protected List<Piece> createPieces(GameConf config, Piece[][] pieces) {
		List<Piece> notNullPieces = new ArrayList<Piece>();
		for(int i=0;i<pieces.length;i++){
			for(int j=0;j<pieces[i].length;j++){
				if (j%2==0) {
					Piece piece = new Piece(i, j);
					notNullPieces.add(piece);
				}
			}
		}
		return notNullPieces;
	}
}
