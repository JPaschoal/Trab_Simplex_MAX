package simplex;

import java.util.Scanner;

/**
 *
 * @author jhonatan
 */

public class Tabela {
    
    private final Scanner sc = new Scanner(System.in);
    
    
    // Matriz [coluna][Linha]
    private double tabela[][];
    
    public Tabela(){
        tabela = new double [2][3];
    }
    
    /**
     * Construtor parametrizado com Variaveis de Decisao e Restriçoes
     * @param varDecisao
     * @param rest 
     */
    public Tabela(int linhas, int colunas){
 
        tabela = new double [linhas][colunas];
        
    }
    /**
     * Preencher Z passando como parametro valor e pos(x1, x2... , f1 , f2 , ... )
     * @param valor
     * @param pos 
     */
    public void preencherZ(double valor, int pos){
        if(pos < tabela[0].length){
            tabela[0][pos] = valor;
        }
        else{
            throw new RuntimeException("Posição invalida");
        }
    }
    /**
     * Peenche restrições passando como parametro valor, posição da linha e coluna
     * @param valor
     * @param linha
     * @param coluna 
     */
    public void preencherRest(double valor, int linha, int coluna){
        if(linha < tabela.length && coluna < tabela[0].length){
            tabela[linha][coluna] = valor;
        }
        else{
            throw new RuntimeException("Posição invalida");
        }
    }
    /**
     * Retorna numero de colunas
     * @return 
     */
    public int numColuna(){
        return tabela[0].length;
    }
    /**
     * Retorna numero de linhas
     * @return 
     */
    public int numLinhas(){
        return tabela.length;
    }
    
    /**
     * Retorna elemento da posição parametrizada
     * @param pos1
     * @param pos2
     * @return 
     */
    public double elemento(int pos1, int pos2){
        return tabela[pos1][pos2];
    }
    
    public void substituirNLP(int linhaPivo, int coluna, double nlp[]){
        tabela[linhaPivo][coluna] = nlp[coluna];
    }
    
    public void substituirZ(double novaZ[], int coluna){
        tabela[0][coluna] = novaZ[coluna];
    }
    
    public void substituirRest(double rest[],int linha, int coluna){
        tabela[linha][coluna] = rest[coluna];
    }
    
    public int retornaPosicao(double elemento){
        
        for (int i = 0; i < tabela[0].length; i++) {
            if(elemento == tabela[0][i]){
                return i;
            }
        }
        
        return -1;
    }
    
}
