
import java.awt.*;
import java.util.List;

class MovimentaCarta implements Runnable{
    private List<Carta> listaCartas;
    private JanelaPaciencia janela;

    public MovimentaCarta(JanelaPaciencia janela, List<Carta> listaCartas) {
        this.janela = janela;
        this.listaCartas = listaCartas;
    }

    public List<Carta> getListaCartas() {
        return listaCartas;
    }

    public void setListaCartas(List<Carta> listaCartas) {
        this.listaCartas = listaCartas;
    }

    @Override
    public void run() {
        Robot robot = null;

        try {
            robot = new Robot();
        } catch (AWTException ex) {
            
        }

        PointerInfo pointerInfo;
        Point point;

        while(true){
            int fatorCorretor = 0;
            for(Carta cartaAtual : listaCartas){
                pointerInfo = MouseInfo.getPointerInfo();
                point = pointerInfo.getLocation();
                Point pointCarta = new Point((int)point.getX() - janela.getX() - JanelaPaciencia.BARRA_LATERAL - (Carta.CARTA_LARGURA/2), (int)point.getY() - janela.getY() - JanelaPaciencia.BARRA_SUP - (Carta.CARTA_ALTURA/2) + fatorCorretor);
                cartaAtual.setLocation(pointCarta);
                fatorCorretor += Carta.CARTA_ALTURA / 4;
                janela.getLayeredPane().moveToFront(cartaAtual);
            }
            robot.delay(50);
        }
    }
}