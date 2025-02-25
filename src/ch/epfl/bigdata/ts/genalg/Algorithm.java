package ch.epfl.bigdata.ts.genalg;

import java.util.Random;

/**
 * Created by dorwi on 05.04.14.
 */
public class Algorithm{

    static Random r = new Random(System.currentTimeMillis());
	/* Public methods */

    public static Population evolvePopulation(Population pop){
        Population newPopulation = new Population(pop.size(),false);

        //keep our best individual
        if (Constants.ELITISM){
            newPopulation.saveIndividual(0,pop.getFittest());
        }

        //Crossover population
        int elitismOffset;
        if (Constants.ELITISM){
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        //create new population with crossover
        for (int i = elitismOffset; i<pop.size(); i++){
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            Individual newIndiv = crossover(indiv1,indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }

        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    //Crossover individuals
    private static Individual crossover(Individual indiv1,Individual indiv2){
        Individual newSol = new Individual();
        // Loop through genes
        for (int i=0; i< indiv1.size(); i++){
            //Crossover
            if (Math.random() <= Constants.UNIFORM_RATE){
                newSol.setGene(i,indiv1.getGene(i));
            } else {
                newSol.setGene(i,indiv2.getGene(i));
            }
        }
        return newSol;
    }

    /* mutate the genes of the individual */
    private static void mutate(Individual indiv){
        for (int i=0; i<Constants.NUMBER_OF_GENES; i++){
            if (r.nextDouble()<=Constants.MUTATION_RATE)
                indiv.generate_gene(i);
        }
    }

    //Select individuals for crossover
    private static Individual tournamentSelection(Population pop){
        //Create a tournament population
        Population tournament = new Population(Constants.TOURNAMENT_SIZE,false);
        //for each place get a random individual
        for (int i=0; i< Constants.TOURNAMENT_SIZE; i++){
            int randomId = (int) (Math.random()*pop.size());
            tournament.saveIndividual(i,pop.getIndividual(randomId));
        }
        Individual fittest = tournament.getFittest();
        return fittest;
    }
}