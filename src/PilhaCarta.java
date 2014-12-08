
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public abstract class PilhaCarta extends JPanel{
    private Pilha<Carta> pilhaCartas = new Pilha<Carta>();
    
    public PilhaCarta() {
        super();
        this.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    }
    
    public abstract void setPosicao(int x, int y);
    public abstract boolean permiteInsercao(Carta carta);
    public abstract void posicionaCarta(Carta carta);

    public Pilha<Carta> getPilhaCartas() {
        return pilhaCartas;
    }
    //este método verifica se um ponto qualquer faz parte da pilha atual, ou seja, se pertence ao retângulo que forma nossa pilha de carta
    public boolean isPontoDaPilha(Point ponto){
        //vamos utilizar a implementação pronto do java para verificar se o ponto pertence a nossa pilha
        return this.getBounds().contains(ponto);
    }
    
    public boolean isCartaDaPilha(Carta carta){
        //vamos rerificar se a carta pertence a pilha atual
        return pilhaCartas.contains(carta);
    }
    
    protected void forcaInsercao(Carta carta, JLayeredPane mesa){
        //se a inserção for permitida, vamo configura a pilha atual da carta como ESTE objeto (this)
        carta.setPilhaAtual(this);
        //como a carta será inserida no topo vamo joga-la para frente na visualização
        mesa.moveToFront(carta);
        //adicionamos a carta na pilha atual
        pilhaCartas.push(carta);
        //posicionaremos a carta, cada tipo de pilha fará isso de um jeito
        posicionaCarta(carta);
    }
    
    public boolean insereCarta(Carta carta, JLayeredPane mesa){
        //Vamos verificar se a carta que queremos inserir pode ser inserida naquela contexto
        if(this.permiteInsercao(carta)){
            forcaInsercao(carta, mesa);
            return true;
        }
        return false;
    }
    
    public boolean insereCarta(List<Carta> cartas, JLayeredPane mesa){
        boolean retorno = false;
        for(Carta cartaAtual : cartas){
            retorno = this.insereCarta(cartaAtual, mesa);
            
        }
        return retorno;
    }
    /*Este método remove uma carta da pilha, como se trata de uma pilha, para remover uma carta do meio da pilha precisamos
     primeiramente remover todas as outras que estão acima dela, todas essas carta removidas serão retornada numa lista
     */
    public List<Carta> removeCarta(Carta carta){
        if(!carta.isIsRevelada()){
            return new ArrayList<Carta>();
        }
        
        //Como se trata de um pilha, para removermos uma ÚNICA carta no meio do monte, removeremos um por uma das carta de estão acima dela
        int indiceCarta = this.getPilhaCartas().indexOf(carta);
        List<Carta> cartaRemovidas = new ArrayList<Carta>();
        
        for(int i = this.getPilhaCartas().size() - 1; i >= indiceCarta; i--){
            cartaRemovidas.add(0, this.getPilhaCartas().pop());
        }
        //retornaremos uma lista com as cartas removidas para que possam ser trabalhadas e incluidas e outra pilha
        return cartaRemovidas;
    }    
}
