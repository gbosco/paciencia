
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JLayeredPane;

public class Carta extends JButton{
    public static final int CARTA_LARGURA  = 99;
    public static final int CARTA_ALTURA   = 134;

    public static final int NAIPE_PAUS    = 1;
    public static final int NAIPE_COPAS   = 2;
    public static final int NAIPE_ESPADAS = 3;
    public static final int NAIPE_OUROS   = 4;
    
    private boolean isRevelada;
    private int numero;
    private int naipe;
    private PilhaCarta pilhaAtual;

    public Carta(int numero, int naipe, boolean isRevelada) {
        this.numero = numero;
        this.naipe = naipe;
        this.isRevelada = isRevelada;
        
        configuraImagem();
    }    
    
    public boolean isIsRevelada() {
        return isRevelada;
    }

    public void setIsRevelada(boolean isRevelada) {
        this.isRevelada = isRevelada;
    }

    public PilhaCarta getPilhaAtual() {
        return pilhaAtual;
    }

    public void setPilhaAtual(PilhaCarta pilhaAtual) {
        this.pilhaAtual = pilhaAtual;
    }

    public int getNaipe() {
        return naipe;
    }

    public void setNaipe(int naipe) {
        this.naipe = naipe;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    public void retornaAoMonteAtual(JLayeredPane mesa){
        this.pilhaAtual.forcaInsercao(this, mesa);
    }
    
    public void setPosicao(int x, int y){
        Point pt = new Point(x, y);
        
        Thread t = new Thread(new MovimentoSuave(this, pt));
        t.start();
        
        //this.setBounds(x, y, CARTA_LARGURA, CARTA_ALTURA);
    }
    
    public static boolean verificaIgualdadeCor(Carta cartaA, Carta cartaB){
        int naipeA = cartaA.getNaipe();
        int naipeB = cartaB.getNaipe();
        if((naipeA == NAIPE_COPAS || naipeA == NAIPE_OUROS) && (naipeB == NAIPE_PAUS ||naipeB == NAIPE_ESPADAS)
        ||(naipeA == NAIPE_PAUS || naipeA == NAIPE_ESPADAS) && (naipeB == NAIPE_COPAS ||naipeB == NAIPE_OUROS)){
            return false;
        }else{
            return true;
        }
    }
    
    public void revelar(){
        this.setIsRevelada(true);
        configuraImagem();
    }
    
    public void esconder(){
        this.setIsRevelada(false);
        configuraImagem();
    }
    
    private void configuraImagem(){
        if(isRevelada){
            String imgFoto = "";
            
            switch(this.numero){
                case 1: imgFoto = "A";
                break;
                case 2: imgFoto = "2";
                break;
                case 3: imgFoto = "3";
                break;
                case 4: imgFoto = "4";
                break;
                case 5: imgFoto = "5";
                break;
                case 6: imgFoto = "6";
                break;
                case 7: imgFoto = "7";
                break;
                case 8: imgFoto = "8";
                break;
                case 9: imgFoto = "9";
                break;
                case 10: imgFoto = "10";
                break;
                case 11: imgFoto = "J";
                break;
                case 12: imgFoto = "Q";
                break;
                case 13: imgFoto = "K";
                break;
            }
            
            switch(this.naipe){
                case NAIPE_PAUS: imgFoto += "Paus";
                break;
                case NAIPE_COPAS: imgFoto += "Copas";
                break;
                case NAIPE_ESPADAS: imgFoto += "Espadas";
                break;
                case NAIPE_OUROS: imgFoto += "Ouros";
                
            }
            
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CARTAS/"+imgFoto+".GIF")));
        }else{
            this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/CARTAS/fundo.GIF")));
        }
    }
    
}
