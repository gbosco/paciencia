
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JLayeredPane;

public class PilhaCartaFinal extends PilhaCarta{

    @Override
    public void setPosicao(int x, int y) {
        Rectangle pilhaNaMesa = new Rectangle(x, y, Carta.CARTA_LARGURA + 10, Carta.CARTA_ALTURA + 10);
        this.setBounds(pilhaNaMesa);
    }

    @Override
    public boolean permiteInsercao(Carta carta) {
        //Se a pilha estiver vazia...
        if(this.getPilhaCartas().isEmpty()){
            //retorna true apenas se a carta for o ÁS
            return carta.getNumero() == 1;
        }
        //Senão
        else{
            boolean cartaNumeroSubSequente = carta.getNumero() - this.getPilhaCartas().peek().getNumero() == 1;
            boolean cartaNaipeIgual        = carta.getNaipe() == this.getPilhaCartas().peek().getNaipe();

            return cartaNaipeIgual && cartaNumeroSubSequente;
        }
    }

    @Override
    public void posicionaCarta(Carta carta) {
        Point localizacao = this.getLocation();
        carta.setPosicao((int) localizacao.getX() + 5, (int) localizacao.getY() + 5);
    }
    
    @Override
    public boolean insereCarta(List<Carta> cartas, JLayeredPane mesa){
        if(cartas.size() == 1){
            return super.insereCarta(cartas, mesa);
        }else{
            return false;
        }
    }
    
}
