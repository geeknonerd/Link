package org.geek.link.board;

import org.geek.link.object.LinkInfo;
import org.geek.link.view.Piece;

public interface GameService {
	void start();
	Piece[][] getPieces();
	boolean hasPieces();
	Piece findPiece(float touchX,float touchY);
	LinkInfo link(Piece p1,Piece p2);
}
