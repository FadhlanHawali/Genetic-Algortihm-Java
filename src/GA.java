import com.oracle.tools.packager.Log;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Arrays;

public class GA {

    Population population = new Population();

    int popSize = 30;
    int generations = 2;
    int init_produksi_count = 8;
    int minInterval = 240;
    int maxInterval = 720;
    float init_produksi[] = new float[init_produksi_count];
    List<Model> agent = new ArrayList<>();
    List<Model> agentSorted = new ArrayList<>();
    List<Model> agentTemp = new ArrayList<>();
    List<Model> newAgent = new ArrayList<>();

    //Permintaan dari minggu ke - 1 sampai minggu ke - 8
    int permintaan[] = {
            342, 405, 563, 484, 313, 435, 462, 411
    };
    public static void main(String[] args) {
        int count=0;
        GA geneticAlgorithm = new GA();
        geneticAlgorithm.agent = geneticAlgorithm.init_agents(geneticAlgorithm.agent,geneticAlgorithm.init_produksi_count);
        geneticAlgorithm.calculateFitness(geneticAlgorithm.agent);

//        while (count<geneticAlgorithm.generations){
        if(geneticAlgorithm.newAgent.isEmpty()){
            geneticAlgorithm.agentTemp = geneticAlgorithm.selection(geneticAlgorithm.agent,geneticAlgorithm.agentTemp);
        }else {
            geneticAlgorithm.agentTemp = geneticAlgorithm.selection(geneticAlgorithm.newAgent,geneticAlgorithm.agentTemp);
        }
        geneticAlgorithm.newAgent = geneticAlgorithm.crossover(geneticAlgorithm.newAgent,geneticAlgorithm.agentTemp);
//            geneticAlgorithm.mutation(geneticAlgorithm.newAgent);
//            count++;
//        }


//        System.out.print("New Child :");
//        for (int i = 0; i< geneticAlgorithm.popSize;i++){
//            geneticAlgorithm.newAgent.add(new Model(geneticAlgorithm.init_produksi_count,0,geneticAlgorithm.crossover()));
//        }

//        for (int i = 0; i< geneticAlgorithm.popSize;i++){
//            for(int j = 0; j < geneticAlgorithm.init_produksi_count;j++){
//                System.out.print("New Agent : " + geneticAlgorithm.newAgent.get(i).agent[j]);
//            }
//            System.out.println("\n");
//        }

    }

    //inisialisasi nilai random terlebih dahulu
    List<Model> init_agents(List<Model> agent, int init_produksi_count){
        int flag = 0;
        do {
            int genes[] = new int[init_produksi_count];
            for (int i = 0; i < init_produksi_count; i++) {
                genes[i] = getRandomNumberInRange(minInterval,maxInterval);
                System.out.print(genes[i] + " ");
            }
            agent.add(new Model(init_produksi_count,0,genes));


            flag++;
            System.out.println("\n");

        }while (flag<popSize);

        System.out.println("List Size :" + agent.size());

        return agent;
    }

    //Kalkulasi fitness tiap agent
    void calculateFitness(List<Model> agent){
        double biayaSimpan;
        double biayaLembur;
        double kerugian;
        int persediaan;
        int c = 100000;
        double totalBiaya;

        for (int i = 0; i<agent.size();i++){
            totalBiaya = 0;
            biayaLembur = 0;
            biayaSimpan = 0;
            kerugian = 0;
            for (int j = 0; j<8;j++){
                //rules for biaya simpan
//                System.out.println("Produksi : " + agent.get(i).agent[j]);
                if (permintaan[j]<agent.get(i).agent[j]){
                    persediaan = agent.get(i).agent[j] - permintaan[j];
                    biayaSimpan = Math.ceil(persediaan/12) * 5000;
                }else {
                    //rules kerugian
                   kerugian = (permintaan[j] - agent.get(i).agent[j]) * 5000;
                }

                //rules for biaya lembur
                if (agent.get(i).agent[j] > 500){
                    persediaan = agent.get(i).agent[j] - permintaan[j];
                    biayaLembur = Math.ceil(persediaan/50) * 60000;
                }
                totalBiaya = totalBiaya + biayaLembur + biayaSimpan + kerugian;
            }
//            System.out.println("Total Biaya : " + totalBiaya);
            agent.get(i).setFitness(c/totalBiaya);
//            System.out.println("Fitness agent ke-" + i + ": " +agent.get(i).getFitness());
        }

    }

    List<Model> selection(List<Model> agent,List<Model> agentTemp){
        agent.sort(Comparator.comparing(Model::getFitness).reversed());

        System.out.println("SIZE AGENT : " + agent.size());
        for (int i = 0;i < agent.size();i++){
            System.out.println("Fitness Sorted: " + agent.get(i).fitness);
        }

        System.out.println("Yang TERAMBIL UNTUK JADI PARENT : ");
        //Select the best gene in the population
        for (int i = 0; i< Math.floor(0.2 * agent.size());i++){
            agentTemp.add(new Model(agent.get(i).agent_length,agent.get(i).getFitness(),agent.get(i).agent));

        }
        for (int i = 0;i<agentTemp.size();i++){
            for (int j = 0;j<agentTemp.get(i).agent_length;j++){
                System.out.print(agentTemp.get(i).agent[j] + " ");

            }
            System.out.println("\n" + "Fitness : " +agentTemp.get(i).fitness);
        }

        return agentTemp;
    }

    List<Model> crossover(List<Model> newAgent,List<Model> agentTemp) {
        //Masukin seluruh parentnya di the best
        newAgent.addAll(agentTemp);

        for (int i = 0; i<(popSize-agentTemp.size())/2;i++){

            //Cari Parent random
            Random rn = new Random();
            int r1 = getRandomNumberInRange(0,agentTemp.size()-1);
            int r2 = getRandomNumberInRange(0,agentTemp.size()-1);
            while(r1 >= r2) {r1 = getRandomNumberInRange_Seed(rn,0,agentTemp.size()-1); r2 = getRandomNumberInRange_Seed(rn,0,agentTemp.size()-1);}
            int parent1[] = agentTemp.get(r1).agent;
            int parent2[] = agentTemp.get(r2).agent;
            //Cari Batas Split
            int split = getRandomNumberInRange(0,agentTemp.get(0).agent_length);
            System.out.println("\n BATAS SPLIT NYA : " + split);
            int[] child1 = new int[agentTemp.get(0).agent.length];
            int[] child2 = new int[agentTemp.get(0).agent.length];


            System.out.println("\n\nPARENT - 1 : ");
            for (int j = 0;j<parent1.length;j++){
                System.out.print(parent1[j] + " ");
            }
            System.out.println("\nPARENT - 2 : ");
            for (int j = 0;j<parent2.length;j++){
                System.out.print(parent2[j] + " ");
            }
            //Child 1
            int temp1[] = Arrays.copyOfRange(parent1,0,split);
//            System.out.println("\nTEMP - 1 : ");
//            for (int j = 0;j<temp1.length;j++){
//                System.out.print(temp1[j] + " ");
//            }
            int temp2[] = Arrays.copyOfRange(parent2,split,parent2.length);
//            System.out.println("\nTEMP - 2 : ");
//            for (int j = 0;j<temp2.length;j++){
//                System.out.print(temp2[j] + " ");
//            }
            System.arraycopy(temp1,0,child1,0,temp1.length);
            System.arraycopy(temp2,0,child1,temp1.length,temp2.length);

            //Child 2
            temp1 = Arrays.copyOfRange(parent2,0,split);
            temp2 = Arrays.copyOfRange(parent1,split,parent1.length);
            System.arraycopy(temp1,0,child2,0,temp1.length);
            System.arraycopy(temp2,0,child2,temp1.length,temp2.length);

            newAgent.add(new Model(temp1.length,0,child1));
            newAgent.add(new Model(temp2.length,0,child2));

            System.out.println("\nANAK KE-1 : ");
            for (int j = 0;j<child1.length;j++){
                System.out.print(child1[j] + " ");
            }
            System.out.println("\nANAK KE-2 : ");
            for (int j = 0;j<child2.length;j++){
                System.out.print(child2[j] + " ");
            }

        }
        System.out.println("\nSIZE AGENT BARU : " + newAgent.size());

        return newAgent;
    }

//    int[] crossover(){
////        Random rn = new Random();
////        //Select a random crossover point
////        int crossOverPoint = rn.nextInt(agentTemp.get(0).agent_length);
////        for (int i = 0; i < crossOverPoint; i++) {
////            int temp = agentTemp.get(0).agent[i];
////            agentTemp.get(0).agent[i] = agentTemp.get(1).agent[i];
////            agentTemp.get(1).agent[i] = temp;
////        }
//        Random rn = new Random();
//        int l = agentTemp.get(0).getAgent_length();
//        int r1 = getRandomNumberInRange(0,l);
//        int r2 = getRandomNumberInRange(0,l);
//
//
//        while(r1 >= r2) {r1 = getRandomNumberInRange_Seed(rn,0,7); r2 = getRandomNumberInRange_Seed(rn,0,7);}
//        System.out.println("Random number 1 : " + r1);
//        System.out.println("Random number 2 : " + r2);
//        int[] child = new int[l];
//
//        for(int i = 0; i < l; i++){
//            child[i] = -1;
//        }
//
//        System.out.println("\n" +"FLAG 1");
//        //copy elements between r1, r2 from parent1 into child
//        for(int i = r1; i <= r2; i++){
//            child[i] = agentTemp.get(0).agent[i];
//            System.out.print("Child : " + child[i]);
//        }
//
//        System.out.println("\n" + "FLAG 2");
//        //array to hold elements of parent1 which are not in child yet
//        int[] y = new int[l-(r2-r1)-1];
//        int j = 0;
//        for(int i = 0; i < l; i++){
//            if(!arrayContains(child,agentTemp.get(0).agent[i])){
//                y[j] = agentTemp.get(0).agent[i];
//                System.out.print("Child y : " + y[j]);
//                j++;
//            }
//        }
//
//        //rotate parent2
//        //number of places is the same as the number of elements after r2
//        int[] copy = agentTemp.get(1).agent.clone();
//        rotate(copy, l-r2-1);
//        System.out.println("\n" +"FLAG 3");
//        System.out.println("Child copy clone parent 2 : " );
//        for (int i = 0; i<copy.length; i++){
//            System.out.print(copy[i] + " ");
//        }
//
//        System.out.println("\n" +"FLAG 4 :");
//        //now order the elements in y according to their order in parent2
//        int[] y1 = new int[l-(r2-r1)-1];
//        j = 0;
//        for(int i = 0; i < l; i++){
//            if(arrayContains(y,copy[i])){
//                y1[j] = copy[i];
//                System.out.print(y1[j]);
//                j++;
//            }
//        }
//
//        System.out.println("\n" +"FLAG 5");
//        //now copy the remaining elements (i.e. remaining in parent1) into child
//        //according to their order in parent2 .. starting after r2!
//        j = 0;
//        for(int i = 0; i < y1.length; i++){
//            int ci = (r2 + i + 1) % l;// current index
//            child[ci]=y1[i];
//        }
//
//        return child;
//    }

    ArrayList<Model> mutation(ArrayList<Model> newAgent){
        //SWAP MUTATION
        Random rn = new Random();
        //Select a random mutation point
        int mutationPoint = getRandomNumberInRange_Seed(rn,0,newAgent.get(0).agent_length);
        int mutationPoint2 = getRandomNumberInRange_Seed(rn,0,newAgent.get(0).agent_length);
        int split = getRandomNumberInRange(0,newAgent.size());

        int temp = newAgent.get(split).agent[mutationPoint];
        newAgent.get(split).agent[mutationPoint] = newAgent.get(split).agent[mutationPoint2];
        newAgent.get(split).agent[mutationPoint2] = temp;


//        mutationPoint = getRandomNumberInRange_Seed(rn2,0,7);
//        mutationPoint2 = getRandomNumberInRange_Seed(rn2,0,7);
//
//        temp = newAgent.get(1).agent[mutationPoint];
//        newAgent.get(1).agent[mutationPoint] = newAgent.get(1).agent[mutationPoint2];
//        newAgent.get(1).agent[mutationPoint2] = temp;

        return newAgent;
    }

    private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        double d = min + r.nextDouble() * (max - min);
        return (int)d;
    }

    private static int getRandomNumberInRange_Seed(Random r, int min, int max){
        if (min >= max){
            throw new IllegalArgumentException("max must be greater than min");
        }

        return r.nextInt((max-min)+1)+min;
    }

    public static boolean arrayContains(int[] arr, int e){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == e)
                return true;
        }
        return false;
    }

    /**
     * rotates (right) an int array a number of places
     *
     * @param arr the int array
     * @param order the number of places to rotate arr
     */
    public static void rotate(int[] arr, int order) {
        int offset = arr.length - order % arr.length;
        if (offset > 0) {
            int[] copy = arr.clone();
            for (int i = 0; i < arr.length; ++i) {
                int j = (i + offset) % arr.length;
                arr[i] = copy[j];
            }
        }
    }
}
