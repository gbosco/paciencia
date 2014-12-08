import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

public class JanelaPaciencia extends JFrame{
public static final int BARRA_SUP          = 30;
    public static final int BARRA_LATERAL  = 4;

    private JLayeredPane mesa = getLayeredPane();

    private List<PilhaCartaFinal> pilhasCartaFinal = new ArrayList<>();
    private List<PilhaCartaMesa> pilhasCartaMesa   = new ArrayList<>();
    private PilhaCartaMonte pilhaCartaMonte;    
    private List<Carta> baralhoCompleto = new ArrayList<Carta>();
    
    private List<Carta> cartasAreaTransferencia;
    
    private MovimentaCarta movimentoCartas = new MovimentaCarta(this, null);
    private Thread thread = new Thread(movimentoCartas);

    private boolean cartaAreaTransferencia = false;
    private JTextField pontuacao = new JTextField("0", 10);
    
    
    public JanelaPaciencia(boolean virarTres) {
        super("Paciência");
        
        pilhaCartaMonte = new PilhaCartaMonte(virarTres);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        FlowLayout flowLayout = new FlowLayout();
        this.setLayout(flowLayout);

        setSize(1200, 1000);
        //vamos percorrer de 1 a 4, ou seja, 4 naipes
        for (int j = 1; j <= 4; j++) {
            //vamos cria 13 cartas para cada naipe
            for (int i = 1; i <= 13; i++) {
                Carta carta = new Carta(i, j, true);
                mesa.add(carta,1);
                //toda carta chamara o mesmo evento...
                addEvento(carta);
                baralhoCompleto.add(carta);
            }    
        }
        //vamos utilizar um método de embaralhamento pronto do Java, do método estatico da classe Collections
        Collections.shuffle(baralhoCompleto);
        //vamos montar as celular(pilhas de carta) que estarão em nossa mesa
        montaCelulas();
        
        this.add(pontuacao, 0);
        pontuacao.setEditable(false);
    }

    
    
    public void addEvento(Carta carta){
        carta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                //toda carta que for clicada chamara o mesmo método, para este metodo passaremos a carta que foi clicada
                clickCarta((Carta) evt.getSource());
            }
        });     
    }
    
    private void montaCelulas(){
        //Vamos criar os montes onde serão depositadas as Carta Finais...são 4
        for (int i = 0; i < 4; i++) {
            PilhaCartaFinal pilhaFinal = new PilhaCartaFinal();
            //vamos descolar cada monte criado um pouco mais a direita um do outra, ele deve se descolocar a largura do anterior mais uma pequena margem
            pilhaFinal.setPosicao(350 + i * (Carta.CARTA_LARGURA + 10), 30);
            //vamos adicioar essa pilha na nossa mesa...
            mesa.add(pilhaFinal,1000);
            pilhaFinal.setEnabled(false);
            //vamos guardar a referencia dessa pilha de carta final no array que é atributo desta classe
            pilhasCartaFinal.add(pilhaFinal);
        }
        //vamos criar os montes que o jogador trabalhará com as cartas
        for (int i = 0; i < 7; i++) {
            int posIni = 0;
            int posFim;
            /* i é a posição da pilha na mesa, quanto maior ele for, mais cartas pegará inicialmente
             * estabelecemos uma lógica para saber de qual até qual posição de carta presentes no baralho pegará.
             * a posição inicial é a soma números inteiros, maiores que zero e menores ou igual ao próprio i
             * Por exemplo, a posição inicial da pilha i = 4 é 10, pois 4+3+2+1=10.
             * E esta pilha terá o número de cartas igual a i + 1, então terá tamnho 5, nosso nosso exemplo
             * logo, a posição inicial será 10 e a final 14 (10 + i)... são 5 posições (10,11,12,13,14)
             */
            int aux = i;
            while(aux != 0){
                posIni += aux--;
            }
            posFim = posIni + i;
            //vamos inicializar um ArratList para jogar as carta de inicialização da nossa pilha nele
            ArrayList<Carta> cartasParaMonte = new ArrayList<Carta>();
            //vamos pegar as carta da posição inicial até a final
            for (int j = posIni; j <= posFim; j++) {
                cartasParaMonte.add(baralhoCompleto.get(j));
            }
            //intanciamos uma pilha da mesa
            PilhaCartaMesa pilhaMesa = new PilhaCartaMesa();
            //vamos jogar uma pilha ao lado da outra utilizando para isto o i para saber o deslocamento
            pilhaMesa.setPosicao(25 + i * (Carta.CARTA_LARGURA + 10), 175);
            mesa.add(pilhaMesa,1000);
            pilhaMesa.setEnabled(false);
            pilhasCartaMesa.add(pilhaMesa);
            pilhaMesa.setPilhaInicializacao(cartasParaMonte, mesa);
        }
        
        mesa.add(pilhaCartaMonte);
        pilhaCartaMonte.setPosicao(25, 30);
        
        pilhaCartaMonte.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PilhaCartaMonte pilhaCartaMonte = (PilhaCartaMonte) evt.getSource();
                pilhaCartaMonte.onClick(mesa);
                //cada vez que o monte for clicado e estiver sendo reiniciado, vamo deduzir 30 pontos do jogador
                if(pilhaCartaMonte.isReiniciada()){
                    adicionaPontos(-30);
                }
            }
        });
        
        ArrayList<Carta> cartasMonte = new ArrayList<Carta>();
        for (int i = 28; i < 52; i++) {
            cartasMonte.add(baralhoCompleto.get(i));
        }
        pilhaCartaMonte.setPilhaInicializacao(cartasMonte);
    }
    
    private void clickCarta(Carta carta) {
        //vamos inverter o valor da variável, ou seja, toda vez que clicarmos numa carta vamos inicar um novo estado!
        cartaAreaTransferencia = !cartaAreaTransferencia;
        //se o valor de cartaAreaTransferencia, uma carta acaba de ser clicada
        if(cartaAreaTransferencia){
            /*se a carta que foi clicada não estiver revelava e estiver no topo da lista, vamos apenas revela-lae voltar
             * o valor da variável cartaAreaTransferencia para false
             */
            if(carta.getPilhaAtual().getPilhaCartas().peek() == carta && !carta.isIsRevelada()){
                carta.revelar();
                cartaAreaTransferencia = false;
                //Estabelecemos que cada carta revelada vale 5 pontos
                adicionaPontos(5);
            }else{
                //Caso uma carta já revelada tenha sido clicada vamos então, fazer alguma ações
                cartasAreaTransferencia = new ArrayList<Carta>();
                //vamos pegar todos as pilhas de carta existentes em nossa mesa
                List<PilhaCarta> todasPilhas = this.getTodasPilhas();
                //vamos percorrer todas as pilhas de carta da nossa mesa
                for (PilhaCarta pilhaCarta : todasPilhas) {
                    //vamos verificar se a pilha atual da iteração é a pilha a qual a carta pertence
                    if(pilhaCarta.isCartaDaPilha(carta)){
                        //caso seja a pilhar a qual a carta pertença, vamos revemover a carta e todos as outras que sejam nescessárias!
                        cartasAreaTransferencia = pilhaCarta.removeCarta(carta);
                        
                        //vamos fazer uma verificação para a pontuação..
                        //Se estivermos removendo uma carta do monte final...
                        if(pilhaCarta instanceof PilhaCartaFinal && cartasAreaTransferencia != null){
                            //então vamos remover 10 pontos do jogador para que ele não possa enganar o nosso jogo
                            adicionaPontos(-10);
                        }
                    }
                }
                //caso a lista de carta removidas não seja nula...
                if(cartasAreaTransferencia != null){
                    //vamos setar como lista de cartas para se movimentar conforme o mouse a lista de carta que está na área de transferência
                    movimentoCartas.setListaCartas(cartasAreaTransferencia);
                    //vamos instanciar um nova Thread passando o objecto que seja um instância Runnable
                    thread = new Thread(movimentoCartas);
                    //vamos startar a Thread que movimenta as cartas
                    thread.start();
                }else{
                    //se a lista for nula, não haverá nada na área de transferência
                    cartaAreaTransferencia = false;
                }
            }
            
        }else{
            //se s lista de carta da área de transferência estiver sendo solta em alguma lugar, vamos parar a Thread que movimenta as nossas cartas
            thread.stop();
            //vamos capturar a posição atual do mouse
            Point pontoMouseSolto = this.getPointInFrame();
            //vamos pegar todas as pilhas que temos em nossa mesa
            List<PilhaCarta> todasPilhas = this.getTodasPilhas();
            //vamos cria uma variável para saber se as nossas cartas da área de transferência foram soltas em cima de algum monte, pelo menos
            boolean achouPilha = false;
            //vamos percorrer todas as pilhas de cartas da nossa mesa
            for (PilhaCarta pilhaCarta : todasPilhas) {
                //se o ponto do mouse pertencer a pilha atual da iteração...
                if(pilhaCarta.isPontoDaPilha(pontoMouseSolto)){
                    //então achouPilha será igual a tru, pois o monte foi achado
                    achouPilha = true;
                    //vamos tentar inserir a lista de carta que estão em nossa "área de transferência", caso não seja permitida
                    if(!pilhaCarta.insereCarta(cartasAreaTransferencia, mesa)){
                        /*vamos devolver a lista de carta para o seu antigo monte antes de elas estarem na área de transferência.
                         * Faremos isso com todas!
                         */
                        for(Carta cartaAtual : cartasAreaTransferencia){
                            cartaAtual.retornaAoMonteAtual(mesa);
                        }
                    }else{
                        /*Se entras no else é porque a carta foi aceita...
                         * ...e se estivermos inserindo uma carta na pilha final...
                         */
                        if(pilhaCarta instanceof PilhaCartaFinal){
                            //...então adicionaremos 10 pontos para o jogador
                            adicionaPontos(10);
                        }
                    }
                }
            }
            /*caso a lista de carta da área de transferência tenha sido solta numa ponto que não pertence a nenhum monte, vamos
             * devolve-las para o monte antigo
             */
            if(!achouPilha){
                for(Carta cartaAtual : cartasAreaTransferencia){
                    cartaAtual.retornaAoMonteAtual(mesa);
                }
            }
        }
        
        verificaJogoFinalizado();
    }
    //vamos adicionar todas as pilhas existentes na mesa em uma list de Pilha de Carta, para retornarmos
    private List<PilhaCarta> getTodasPilhas(){
        ArrayList<PilhaCarta> todasPilhas = new ArrayList<PilhaCarta>();
        todasPilhas.addAll(pilhasCartaFinal);
        todasPilhas.addAll(pilhasCartaMesa);
        todasPilhas.add(pilhaCartaMonte);
        return todasPilhas;

    }
    
    private void adicionaPontos(int pontos){
        int pontuacaoAtual = Integer.parseInt(pontuacao.getText());
        int pontuacaoAtualizada = pontuacaoAtual + pontos;
        //não vamos trabalhar com pontuação negativa
        if(pontuacaoAtualizada < 0){
            pontuacao.setText(String.valueOf(0));
        }else{
            pontuacao.setText(String.valueOf(pontuacaoAtualizada));
        }
    }
    private void verificaJogoFinalizado(){
        //vamos inicializar a variavel com valor true e então testar todos os montes
        boolean jogoFinalizado = true;
        for(PilhaCartaFinal pilhaCartaFinal : this.pilhasCartaFinal){
            //caso algum monte tenha tamanho diferente de 13, ele não está cheio! (isso para o monte final)
            if(pilhaCartaFinal.getPilhaCartas().size() != 13){
                jogoFinalizado = false;
                break;
            }
        }
        //se todos estiverem cheios o jogo está acabado!
        if(jogoFinalizado){
            JOptionPane.showMessageDialog(this, "Parabéns! Sua pontuação foi de : " + pontuacao.getText());
        }
    }
    
    private Point getPointInFrame(){
        Point pontoNaTela = MouseInfo.getPointerInfo().getLocation();
        int x = (int) (pontoNaTela.getX() - this.getLocation().getX() + JanelaPaciencia.BARRA_LATERAL);
        int y = (int) (pontoNaTela.getY() - this.getLocation().getY() + JanelaPaciencia.BARRA_SUP);
        Point pontoNoFrame = new Point(x, y);
        
        return pontoNoFrame;
    }
    
    public static void main(String[] args) {
        //new JanelaPaciencia(false).setVisible(true);
        new TelaInicial().setVisible(true);
    }
}