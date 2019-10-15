public class Model {

    int agent_length;
    double fitness;
    int agent[];

    public Model(int agent_length, double fitness, int[] agent) {
        this.agent_length = agent_length;
        this.fitness = fitness;
        this.agent = agent;
    }

    public int getAgent_length() {
        return agent_length;
    }

    public void setAgent_length(int agent_length) {
        this.agent_length = agent_length;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int[] getAgent() {
        return agent;
    }

    public void setAgent(int[] agent) {
        this.agent = agent;
    }
}
