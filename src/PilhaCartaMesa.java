
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLayeredPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gustavo
 */
public class PilhaCartaMesa extends PilhaCarta{

    public void setPilhaInicializacao(List<Carta> cartasIniciaisDoMonte, JLayeredPane jlp){
        for(Carta cartaAtu : cartasIniciaisDoMonte){
            cartaAtu.esconder();
            this.forcaInsercao(cartaAtu, jlp);
        }
        if(!cartasIniciaisDoMonte.isEmpty()){
            cartasIniciaisDoMonte.get(cartasIniciaisDoMonte.size() - 1).revelar();
        }
    }
    
    @Override
    public void setPosicao(int x, int y) {
        Rectangle pilhaNaMesa = new Rectangle(x, y, Carta.CARTA_LARGURA + 10, 750);
        this.setBounds(pilhaNaMesa);
    }

    @Override
    public boolean permiteInsercao(Carta carta) {
        if(this.getPilhaCartas().isEmpty()){
            if(carta.getNumero() == 13){
                return true;
            }else{
                return false;
            }
        }else{
            Carta ultimaCartaDaPilha     = this.getPilhaCartas().peek();
            boolean condicaoNumeroMenor  = ultimaCartaDaPilha.getNumero() - carta.getNumero() == 1;
            //vamos verificar se as cartas possuem cor diferente
            boolean condicaoCorDiferente = !Carta.verificaIgualdadeCor(carta, ultimaCartaDaPilha);
            if(condicaoNumeroMenor && condicaoCorDiferente){
                return true;
            }else{
                return false;
            }    
        }
    }

    @Override
    public void posicionaCarta(Carta carta) {
        Point localizacao = this.getLocation();
        carta.setPosicao((int) localizacao.getX() + 5, (int) localizacao.getY() + 5 + ((this.getPilhaCartas().size() - 1) * Carta.CARTA_ALTURA / 4));
    }
    
    
    @Override
    public List<Carta> removeCarta(Carta carta){
        if(!carta.isIsRevelada()){
            return null;
        }
        
        //Como se trata de um pilha, para removermos uma ÚNICA carta no meio do monte, removeremos um por uma das carta de estão acima dela
        int indiceCarta = this.getPilhaCartas().indexOf(carta);
        ArrayList<Carta> cartaRemovidas = new ArrayList<Carta>();
        
        for(int i = this.getPilhaCartas().size() - 1; i >= indiceCarta; i--){
            cartaRemovidas.add(0, this.getPilhaCartas().pop());
        }
        //retornaremos uma lista com as cartas removidas para que possam ser trabalhadas e incluidas e outra pilha
        return cartaRemovidas;
    }
}
