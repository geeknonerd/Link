package org.geek.link.board;

import java.util.List;

import org.geek.link.object.GameConf;
import org.geek.link.util.ImageUtil;
import org.geek.link.view.Piece;
import org.geek.link.view.PieceImage;

public abstract class AbstractBoard {
	protected abstract List<Piece> createPieces(GameConf config,Piece[][] pieces);
	public Piece[][] create(GameConf config){
		Piece[][] pieces = new Piece[config.getXSize()][config.getYSize()];
		List<Piece> notNullPieces = createPieces(config, pieces);
		List<PieceImage> playImages = ImageUtil.getPlayImages(config.getContext(),notNullPieces.size());
		int imageWidth = playImages.get(0).getImage().getWidth();
		int imageHeight = playImages.get(0).getImage().getWidth();
		for(int i=0;i<notNullPieces.size();i++){
			Piece piece = notNullPieces.get(i);
			piece.setImage(playImages.get(i));
			piece.setBeginX(piece.getIndexX()*imageWidth+config.getBeginImageX());
			piece.setBeginY(piece.getIndexY()*imageHeight+config.getBeginImageY());
			pieces[piece.getIndexX()][piece.getIndexY()] = piece;
		}
		return pieces;
	}
}
