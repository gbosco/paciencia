
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;


public class PilhaCartaMonte extends PilhaCarta{
    private Pilha<Carta> pilhaCartaEscondida = new Pilha<Carta>();
    private boolean virarTres;
    private ArrayList<Carta> ultimasTres = new ArrayList<Carta>();
    private static int countAux;

    public PilhaCartaMonte(boolean virarTres) {
        this.virarTres = virarTres;
        
        /**
         * @TODO REMOVER
        */
        JLabel pic = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/CARTAS/fundo.GIF")));
        this.add(pic);
        
        countAux = 0;
        
    }
    
    @Override
    public void setPosicao(int x, int y) {
        Rectangle pilhaNaMesa = new Rectangle(x, y, Carta.CARTA_LARGURA, Carta.CARTA_ALTURA);
        this.setBounds(pilhaNaMesa);
    }

    @Override
    public boolean permiteInsercao(Carta carta) {
        return false;
    }

    @Override
    public void posicionaCarta(Carta carta) {
        Point posicaoMonte = this.getLocation();
        if(pilhaCartaEscondida.contains(carta)){
            carta.setPosicao((int) posicaoMonte.getX() ,(int) posicaoMonte.getY());        
        }else{
            carta.setPosicao((int) posicaoMonte.getX() + Carta.CARTA_LARGURA + 15,(int) posicaoMonte.getY());
        }
    }

    @Override
    protected void forcaInsercao(Carta carta, JLayeredPane mesa) {
        super.forcaInsercao(carta, mesa);
        if(virarTres){
            reposicionaTodasTresViradas();
        }
    }
    
    
    
    public void setPilhaInicializacao(List<Carta> cartasIniciaisDoMonte){
        for(Carta cartaAtual : cartasIniciaisDoMonte){
            this.pilhaCartaEscondida.push(cartaAtual);
        }
        //JLabel pic = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/CARTAS/vazio.GIF")));
        ///this.add(pic);
    }

    @Override
    public List<Carta> removeCarta(Carta carta) {
        if(carta == this.getPilhaCartas().peek()){
            return super.removeCarta(carta);
        }else{
            return null;
        }
    }
    
    public void onClick(JLayeredPane mesa, JanelaPaciencia jp){
        
        //Se não houver mais cartas escondicas para ser reveladas, iremos voltar todas as cartas para o monte escondido
        if(this.pilhaCartaEscondida.isEmpty()){
            //enquando a pilha de cartas que amontoamos não estiver vazia, removeremos a carta do topo e voltaremos para o monte escondido
            while(!this.getPilhaCartas().isEmpty()){
                //Vamos remover a ultima carta do topo da pilha...
                Carta carta = this.getPilhaCartas().pop();
                //...e inserir no topo da pilha escondida
                this.pilhaCartaEscondida.push(carta);
                //vamos mandar a carta ser reposicionada!
                posicionaCarta(carta);
            }
            mesa.moveToFront(this);

            //não haverá mais três ultimas viradas pois o monte foi reiniciado
            ultimasTres.clear();
        }else{
            if(virarTres){
                ultimasTres.clear();
                for (int i = 0; i < 3; i++) {
                    if(!this.pilhaCartaEscondida.isEmpty()){
                        Carta carta = this.pilhaCartaEscondida.pop();
                        super.forcaInsercao(carta, mesa);
                        ultimasTres.add(carta);
                    }
                }
                reposicionaTodasTresViradas();
            }else{
                super.forcaInsercao(this.pilhaCartaEscondida.pop(), mesa);
            }
        }
        
        String imgAux = "";
        if(this.pilhaCartaEscondida.isEmpty()){
            imgAux = "vazio";
        }else{
            imgAux = "fundo";
        }
        
        JLabel pic = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/CARTAS/"+imgAux+".GIF")));
        this.removeAll();
        this.add(pic);            
        
        int aux = 1;
        if (countAux++ % 2 == 0) aux = -1;
        jp.setSize(jp.getWidth()+ aux, jp.getHeight() + aux);
        
    }
    
    private void reposicionaTodasTresViradas(){
        //vamos primeiramente jogar TODAS as cartas na posição padrão do monte
        for (int i = 0; i < this.getPilhaCartas().size(); i++) {
            posicionaCarta(this.getPilhaCartas().get(i));
        }
        //vamos capturar qual a posição do monte
        Point posicaoMonte = this.getLocation();
        //vamos pegar todas as ultimas tres viradas e joga-las as posições diferenciadas
        for(int i = 0 ; i < this.ultimasTres.size(); i++){
            //vamos verificar se a carta ainda pertence ao monte, pois ela pode ter sido removidas e estarmos com lixo em nossa referência
            if(this.getPilhaCartas().contains(this.ultimasTres.get(i))){
                this.ultimasTres.get(i).setPosicao((int) posicaoMonte.getX() + Carta.CARTA_LARGURA + 15 + (i * 25),(int) posicaoMonte.getY());
            }
        }
    }
    
    public boolean isReiniciada(){
        if(this.getPilhaCartas().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
}
