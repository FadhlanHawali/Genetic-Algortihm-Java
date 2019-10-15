import java.util.Random;

//Individual class
class Individual implements Cloneable{

    int fitness = 0;
    int geneLength;
    int[] genes = new int[geneLength];


    public Individual(int geneLength, int minInterval, int maxInterval) {
        this.geneLength = geneLength;
        //Set genes randomly for each individual
        for (int i = 0; i < genes.length; i++) {
            genes[i] = getRandomNumberInRange(minInterval,maxInterval);
        }

        fitness = 0;
    }

    //Calculate fitness
    public void calcFitness() {

        fitness = 0;
        for (int i = 0; i < 5; i++) {
            if (genes[i] == 1) {
                ++fitness;
            }
        }
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Individual individual = (Individual)super.clone();
        individual.genes = new int[5];
        for(int i = 0; i < individual.genes.length; i++){
            individual.genes[i] = this.genes[i];
        }
        return individual;
    }
}