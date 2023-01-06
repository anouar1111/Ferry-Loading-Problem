import java.util.ArrayList;
import java.util.Scanner;
import java.util.Hashtable;

class Main {
    private int bestK; //max de voitures qui peuvent etre dans le ferry
    private int[] currX; //tableau qui enregistre une solution courrante
    private int[] bestX; //tableau qui enregistre la meilleure solution possible
    private int nbCases; //nombre de cas a traités
    private int L; // longueur du ferry
    private ArrayList<Integer> length; // list qui store les tailles de voiture
    private int n; //nombre de voitures par cas
    private int newS; //nouvelle longeur restante du cote gauche
    private int SPrime; //variable pour calculer la longueur qui reste du cote droit
    private Hashtable<Integer,Integer> visited ; //Hashtable qui sauvegarde les etats visités ou pas

    int calculateSizeHashTable(){//pour calcul de la dimension de la table de hachage
      return (n * L)/50;
    }

    int hashFunction(int K, int S){//fonction de hachage
      int hash = ((K* S) / (S+5) ) %  calculateSizeHashTable();
      return hash;
    }

    void setPresent(int K,int S){//mettre un etat comme etant visité
      visited.put(hashFunction(K,S),1);
    }

    Boolean present(int K , int S){//verifier si un etat a ete visité
        if(visited.containsKey(hashFunction(K, S))){
            int result = visited.get(hashFunction(K, S));
            if(result == 1){
                return true;
            }
        }
        return false;
    }

    void BacktrackSolve(int currK , int currS){
        if(currK > bestK){//s il y a le K courrant est plus grand que le meilleur K alors on met à jour la meilleure solution
          bestK = currK;
          for(int i=0 ;i<currX.length ;i++ ){
            bestX[i]=currX[i];
          }
        }
        if(currK < n){//il y a encore des voitures à considérer
          if(length.get(currK)<=currS && present(currS-length.get(currK),currK+1 )== false ){//possible d'ajouter la voiture suivante a gauche + l'etat non visité
              currX[currK]=1;//on met la voiture a gauche
              newS = currS - length.get(currK);
              BacktrackSolve(currK+1, newS);
              setPresent(newS ,currK+1);
          }
          if((SPrime >= length.get(currK)) && present(currS,currK+1)== false){//possible d'ajouter la voiture suivante a droite + l'etat non visité
            currX[currK] = 0;//on met la voiture a droite
            SPrime = SPrime - length.get(currK);
            BacktrackSolve(currK+1, currS);
            SPrime = SPrime + length.get(currK);
            setPresent(currS,currK+1);//on met l'etat comme étant visité
          }
        }
    }



    public static void main (String args[]){
      Main main = new Main();
      main.n=0;
      Scanner input = new Scanner(System.in); //pour lire les entrées et inisialiser les variables
      main.nbCases = input.nextInt();
      for(int i=0 ; i < main.nbCases; i++){//on refait le meme processus pour chaque cas
        main.length = new ArrayList<Integer>();
        main.L= input.nextInt()*100;//conversion en centimetre
        int next = input.nextInt();
        while(next !=0){//on lit la taille de chaque voiture
          main.length.add(next);
          next = input.nextInt();
        }
        main.n= main.length.size();
        main.currX = new int[main.n];
        main.bestX = new int[main.n];
        main.visited = new Hashtable<>(main.calculateSizeHashTable());
        main.bestK = -1;
        main.newS = -1;
        main.SPrime = main.L;//toute la longueur du ferry est disponible au debut
        main.BacktrackSolve(0, main.L);
        System.out.println(main.bestK);
        for(int j=0 ; j<main.bestK ; j++){
          if(main.bestX[j]==1){
            System.out.println("starboard");
          }else{
            System.out.println("port");
          }
        }
        if(i!=main.nbCases-1){//pour eviter l espace à la derniere ligne
          System.out.println();
        }
      }
      input.close();
      }

}
