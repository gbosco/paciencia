
public class Pilha <T> {
     private T elementos[] = (T[]) new Object[0];

    public void push(T object){
        //vamos criar um novo vetor que tenha uma posição a mais que o número de posições do vetor atual, pois adicionares mais um elemento
        Object elementosAux[] = new Object[elementos.length + 1];
        //vamos passar todo os elementos do vetor atual para o vetor auxiliar
        for (int i = 0; i < this.elementos.length; i++) {
            elementosAux[i] = this.elementos[i];
        }
        //vamos adicionar o elemento recebido na ultima posição, pois se trata de uma pilha
        elementosAux[elementosAux.length - 1] = object;
        //vamos atribuir o vetor auxiliar como nossa pilha de elementos
        this.elementos = (T[]) elementosAux;
    }
    
    public T pop(){
        //Se a lsita estiver vazia não poderemos remover nada, então retornaremos um valor nulo
        if(this.isEmpty()){
            return null;
        }
        //vamos criar um vetor com um posição a menos que o vetor de elementos atual, afinal, removeremos uma posição
        Object elementosAux[] = new Object[this.elementos.length - 1];
        //vamos percorrer todas as posições do vetor auxiliar...
        for (int i = 0; i < elementosAux.length; i++) {
            //para cada posição do vetor auxiliar pegaremos os elemento do vetor atual correspondente!
            elementosAux[i] = this.elementos[i];
        }
        //vamos guardar uma instancia do ultimo elemento do vetor atual para retornarmos, esse elemento não pertencerá mais a pilha
        T retorno = this.elementos[this.elementos.length - 1];
        //vamos atrubuir ao vetor atual o vetor auxiar que não possui mais a ultima posição
        this.elementos = (T[]) elementosAux;
        
        return retorno;
        
    }
    /*método criado para "espiar" o elemento que está no topo da lista*/
    public T peek(){
        if(this.isEmpty()){
            return null;
        }
        return this.elementos[this.elementos.length - 1];
    }
    
    public boolean isEmpty(){
        if(this.elementos.length == 0){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean contains(T object){
        //vamos percorrer todas as posições do noso vetor de elementos...
        for (int i = 0; i < this.elementos.length; i++) {
            //caso encontrarmos em um dos nossos elementos o mesmo objeto recebido para verificação retornaremos true, ou seja, o objeto se encontra na pilha
            if(this.elementos[i] == object){
                return true;
            }
        }
        return false;
    }
    
    public int size(){
        return this.elementos.length;
    }
    
    public int indexOf(T object){
        //vamos percorrer todas as posições do noso vetor de elementos...
        for (int i = 0; i < this.elementos.length; i++) {
            //caso encontrarmos em um dos nossos elementos o mesmo objeto recebido para verificação, retornaremos a posição dele
            if(this.elementos[i] == object){
                return i;
            }
        }
        //caso nenhum referencia tenha sido encontrada retornaremos -1 que seria um posição inválida
        return -1;
    }
    
    public T get(int posicao){
        return this.elementos[posicao];
    }
    
}
