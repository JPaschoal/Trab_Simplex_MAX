package main;

import java.util.Scanner;
import simplex.Tabela;

/**
 *
 * @author jhonatan
 */
public class Main {

    private static int varDecisao;
    private static int rest;
    private static int colunaPivo;
    private static int linhaPivo;
    private static double elementoPivo;

    private static final Scanner SC = new Scanner(System.in);
    private static Tabela tabela = new Tabela();
    private static double razao[];
    private static double nlp[];
    private static double resultadoX[];
    private static double resultadoF[];

    public static void main(String[] args) {

        montarTabela();
        maximizar();

    }

    public static void montarTabela() {
        //Input de dados
        System.out.print("Numero de Variaveis de decisão: ");
        varDecisao = SC.nextInt();
        System.out.print("Numero de Restrições: ");
        rest = SC.nextInt();

        //Tabela
        int linhas = rest + 1;
        int colunas = varDecisao + rest + 1;
        tabela = new Tabela(linhas, colunas);

        // Preenchendo Z
        System.out.println("Digite valores da linha Z: ");
        for (int i = 0; i < varDecisao; i++) {
            System.out.print("x" + (i + 1) + ": ");
            double aux = SC.nextInt();
            tabela.preencherZ((aux * -1), i);
        }
        for (int i = varDecisao + 1; i < tabela.numColuna(); i++) {
            System.out.print("f " + (i - varDecisao) + ": ");
            double aux = SC.nextInt();
            tabela.preencherZ(aux, i);
        }
        System.out.print("b: ");
        double b = SC.nextInt();
        tabela.preencherZ(b, tabela.numColuna() - 1);

        //Preenchendo Rests
        for (int i = 1; i < tabela.numLinhas(); i++) {
            System.out.println("Valores Restrição " + i + ": ");
            for (int j = 0; j < varDecisao; j++) {
                System.out.print("x" + (j + 1) + ": ");
                double aux = SC.nextInt();
                tabela.preencherRest(aux, i, j);
            }
            for (int j = varDecisao; j < tabela.numColuna() - 1; j++) {
                System.out.print("f " + ((j - varDecisao)+ 1) + ": ");
                double aux = SC.nextInt();
                tabela.preencherRest(aux, i, j);
            }
            System.out.print("b: ");
            double bRest = SC.nextInt();
            tabela.preencherRest(bRest, i, tabela.numColuna() - 1);
        }

        System.out.println("-----------------------------------------------1-----------------------------------------------");

        for (int i = 0; i < tabela.numLinhas(); i++) {
            for (int j = 0; j < tabela.numColuna(); j++) {
                double aux = tabela.elemento(i, j);
                System.out.print(aux + " ");
            }
            System.out.println("\n");
        }

    }

    public static void maximizar() {
        int aux1 = verificaVariaveisDecisao();
        int cont = 2;
        while (aux1 != -1) {

            colunaPivo = identificarColunaPivo();

            razao = new double[rest];
            calcularRazao();

            linhaPivo = identificarLinhaPivo();

            elementoPivo = tabela.elemento(linhaPivo, colunaPivo);
            
            System.out.println("____ elemento " + elementoPivo + "linha pivo " + linhaPivo + "coluna pivo " +colunaPivo);
            
            for (int i = 0; i < razao.length; i++) {
                System.out.println(razao[i]);
            }

            calcularNLP();

            calcularZ();

            calcularRest();

            System.out.println("-----------------------------------------------" + cont + "-----------------------------------------------");

            for (int i = 0; i < tabela.numLinhas(); i++) {
                for (int j = 0; j < tabela.numColuna(); j++) {
                    double aux = tabela.elemento(i, j);
                    System.out.print(aux + " ");
                }
                System.out.println("\n");
            }

            aux1 = verificaVariaveisDecisao();
            cont++;
        }

        apresentarResultado();
    }

    public static int identificarColunaPivo() {

        double aux1 = 0;
        for (int i = 0; i < varDecisao; i++) {
            if (aux1 > tabela.elemento(0, i)) {
                aux1 = tabela.elemento(0, i);
            }
        }
        int aux = tabela.retornaPosicao(aux1);

        return aux;
    }

    public static void calcularRazao() {
        int contLin = 1;
        for (int i = 0; i < razao.length; i++) {
            double elementoPivoLinha = tabela.elemento(contLin, colunaPivo);
            double b = tabela.elemento(contLin, tabela.numColuna() - 1);
            if (elementoPivoLinha > 0) {
                razao[i] = b / elementoPivoLinha;
            } else {
                razao[i] = 0;
            }
            contLin += 1;
        }
    }

    public static int identificarLinhaPivo() {
        double aux1 = razao[0];
        for (int i = 0; i < razao.length; i++) {
            if (aux1 > razao[i] && razao[i] != 0) {
                aux1 = razao[i];
            }
        }
        for (int i = 0; i < razao.length; i++) {
            if (aux1 == razao[i]) {
                return i + 1;
            }
        }
        return -1;
    }

    public static void calcularNLP() {
        //Array que guarda NLP
        nlp = new double[tabela.numColuna()];

        //For que percorre as colunas da tabela na linha pivo dividindo a linha pelo elemento pivo
        for (int i = 0; i < tabela.numColuna(); i++) {

            double aux = tabela.elemento(linhaPivo, i) / elementoPivo;

            nlp[i] = aux;
        }
        //Passando o valor do array nlp para a tabela no lugar da antiga linha pivo
        for (int i = 0; i < tabela.numColuna(); i++) {

            tabela.substituirNLP(linhaPivo, i, nlp);

        }
    }

    public static void calcularZ() {
        //array que guarda valores da nova Z
        double novaZ[] = new double[tabela.numColuna()];
        //Elemento pivo da linha Z
        double elementoPivoZ = tabela.elemento(0, colunaPivo);

        for (int i = 0; i < tabela.numColuna(); i++) {

            double aux1 = nlp[i];

            double aux = (aux1 * Math.abs(elementoPivoZ)) + tabela.elemento(0, i);

            novaZ[i] = aux;

        }

        for (int i = 0; i < tabela.numColuna(); i++) {

            tabela.substituirZ(novaZ, i);

        }

    }

    public static void calcularRest() {

        double restricao[] = new double[tabela.numColuna()];

        for (int i = 1; i < tabela.numLinhas(); i++) {
            if (i != linhaPivo) {

                double elementoPivoRest = tabela.elemento(i, colunaPivo);

                for (int j = 0; j < tabela.numColuna(); j++) {

                    double aux1 = nlp[j];

                    double aux = (aux1 * (elementoPivoRest * (-1))) + tabela.elemento(i, j);

                    restricao[j] = aux;

                }

                for (int j = 0; j < tabela.numColuna(); j++) {

                    tabela.substituirRest(restricao, i, j);

                }
            }
        }

    }

    public static int verificaVariaveisDecisao() {
        for (int i = 0; i < varDecisao; i++) {
            if (tabela.elemento(0, i) < 0) {
                return 1;
            }
        }
        return -1;
    }

    private static void apresentarResultado() {

        verificaBaseX();
        verificaBaseRest();
        
        System.out.println("Solução Otima: ");
        for (int i = 0; i < resultadoX.length; i++) {
            System.out.println("x" + (i + 1) + " = " + resultadoX[i]);
        }
        System.out.println("");
        System.out.println("Lucro Maximo = " + tabela.elemento(0, (tabela.numColuna() - 1)));
        
        System.out.println("");
        System.out.println("Folgas: ");
        for (int i = 0; i < resultadoF.length; i++) {
            System.out.println("f" + (i + 1) + " = " + resultadoF[i]);
        }
        
        System.out.println("");
        System.out.println("Preço sombra: ");
        for (int i = varDecisao; i < tabela.numColuna() - 1; i++) {
            System.out.println("f" + ((i + 1) - varDecisao) + " = " + tabela.elemento(0, i));
        }

    }

    private static void verificaBaseX() {

        resultadoX = new double[varDecisao];

        for (int i = 0; i < varDecisao; i++) {

            int contZero = 0;
            int contUm = 0;
            int pos = 0;

            for (int j = 0; j < tabela.numLinhas(); j++) {
                if (tabela.elemento(j, i) == 1) {
                    contUm++;
                    pos = j;
                } else if (tabela.elemento(j, i) == 0) {
                    contZero++;
                } else {
                    resultadoX[i] = 0;
                }
            }
           
            if (contZero == (tabela.numLinhas() - 1) && contUm == 1) {
                resultadoX[i] = tabela.elemento(pos, (tabela.numColuna() - 1));
            }
        }

    }

    private static void verificaBaseRest() {

        resultadoF = new double[rest];

        for (int i = 0; i < rest; i++) {

            int contZero = 0;
            int contUm = 0;
            int pos = 0;

            for (int j = 0; j < tabela.numLinhas(); j++) {
                if (tabela.elemento(j, i + varDecisao) == 1) {
                    contUm++;
                    pos = j;
                } else if (tabela.elemento(j, i + varDecisao) == 0) {
                    contZero++;
                } else {
                    resultadoX[i] = 0;
                }
            }
            
            if (contZero == (tabela.numLinhas() - 1) && contUm == 1) {
                resultadoF[i] = tabela.elemento(pos, (tabela.numColuna() - 1));
            }
        }

    }
}
