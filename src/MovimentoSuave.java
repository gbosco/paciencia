
import java.awt.Point;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gustavo
 */
class MovimentoSuave implements Runnable{

    private Carta carta;
    private Point ptDestino;

    public MovimentoSuave(Carta carta, Point ptDestino) {
        this.carta = carta;
        this.ptDestino = ptDestino;
    }



    @Override
    public void run() {
        int xOrigem  = (int) carta.getLocation().getX();
        int yOrigem  = (int) carta.getLocation().getY();
        int xDestino = (int) ptDestino.getX();
        int yDestino = (int) ptDestino.getY();

        
        int deltaX = (int) Math.abs(carta.getLocation().getX() - xDestino);
        int deltaY = (int) Math.abs(carta.getLocation().getY() - yDestino);
        int somaQuadCatetos = (int) (Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        double distanciaEnterPontos = Math.sqrt(somaQuadCatetos);
        
        for (int i = 1; i < (int) distanciaEnterPontos; i++) {
            int correcaoX = 1;
            int correcaoY = 1;
            if(xOrigem > xDestino){
                correcaoX = -1;
            }
            if(yOrigem > yDestino){
                correcaoY = -1;
            }
            int xMov = (int) (xOrigem + ((i / distanciaEnterPontos) * deltaX * correcaoX));
            int yMov = (int) (yOrigem + ((i / distanciaEnterPontos) * deltaY * correcaoY));

            carta.setBounds(xMov,yMov, Carta.CARTA_LARGURA, Carta.CARTA_ALTURA);
            try {
                if(i % 100 == 0){
                    Thread.sleep(50);
                }

            } catch (InterruptedException ex) {

            }
        }
        
        carta.setBounds(xDestino, yDestino, Carta.CARTA_LARGURA, Carta.CARTA_ALTURA);
        //carta.setPosicao();
    }

}