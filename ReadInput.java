package main.java;

import java.io.*;
import java.util.*;

public class ReadInput
{
    public Map<String, Object> data;

    public ReadInput()
    {
        data = new HashMap<String, Object>();
    }

    public int[][] hillClimbing()
    {
        int number_of_caches = (int) data.get("number_of_caches");
        int number_of_videos = (int) data.get("number_of_videos");
        
        int[][] initial_solution = new int[number_of_caches][number_of_videos];
        int initial_score = 0;

        for (int cache = 0; cache < number_of_caches; cache++)
        {
            for (int video = 0; video < number_of_videos; video++)
            {
                initial_solution[cache][video] = 0;
            }
        }

        return hillClimbing(initial_solution, initial_score);
    }

    private int[][] hillClimbing(int[][] solution, int score)
    {
        int number_of_caches = (int) data.get("number_of_caches");
        int number_of_videos = (int) data.get("number_of_videos");

        // Problem: How do I generate multiple solutions, evaluate them and then generate more?
        // It is difficult as it I need to copy the entire array

        int[][] new_solution = new int[number_of_caches][number_of_videos];
        int new_score = 0;

        for (int i = 0; i < number_of_caches; i++)
        {
            for (int j = 0; j < number_of_videos; j++)
            {
                new_solution[i][j] = solution[i][j];
            }
        }

        int[] modified_indexes = new int[2];

        if (isValidSolution(new_solution))
        {
            for (int cache = 0; cache < number_of_caches; cache++)
            {
                for (int video = 0; video < number_of_videos; video++)
                {
                    if (new_solution[cache][video] == 0)
                    {
                        new_solution[cache][video] = 1;

                        int solution_score = fitness(new_solution);

                        if (solution_score > new_score)
                        {
                            new_score = solution_score;
                            modified_indexes[0] = cache;
                            modified_indexes[1] = video;
                        }

                        new_solution[cache][video] = 0;
                    }
                }
            }
        }
        else
        {
            boolean valid_solution = false;

            for (int cache = 0; cache < number_of_caches; cache++)
            {
                if (valid_solution)
                {
                    break;
                }

                for (int video = 0; video < number_of_videos; video++)
                {
                    if (new_solution[cache][video] == 1)
                    {
                        new_solution[cache][video] = 0;

                        valid_solution = isValidSolution(new_solution);
                    }

                    if (valid_solution)
                    {
                        break;
                    }
                }
            }
        }

        if (new_score > score)
        {
            int cache = modified_indexes[0];
            int video = modified_indexes[1];

            new_solution[cache][video] = 1;

            return hillClimbing(new_solution, new_score);
        }

        return solution;
    }

    public int[][] simpleHillClimbing()
    {
        int number_of_caches = (int) data.get("number_of_caches");
        int number_of_videos = (int) data.get("number_of_videos");
        
        int[][] initial_solution = new int[number_of_caches][number_of_videos];
        int initial_score = 0;

        for (int cache = 0; cache < number_of_caches; cache++)
        {
            for (int video = 0; video < number_of_videos; video++)
            {
                initial_solution[cache][video] = 0;
            }
        }

        return simpleHillClimbing(initial_solution, initial_score);
    }

    private int[][] simpleHillClimbing(int[][] solution, int score)
    {
        int number_of_caches = (int) data.get("number_of_caches");
        int number_of_videos = (int) data.get("number_of_videos");

        int[][] new_solution = new int[number_of_caches][number_of_videos];
        int new_score = 0;

        for (int i = 0; i < number_of_caches; i++)
        {
            for (int j = 0; j < number_of_videos; j++)
            {
                new_solution[i][j] = solution[i][j];
            }
        }

        int[] modified_indexes = new int[2];
        boolean improvement = false;

        if (isValidSolution(new_solution))
        {
            for (int cache = 0; cache < number_of_caches; cache++)
            {
                if (improvement)
                {
                    break;
                }

                for (int video = 0; video < number_of_videos; video++)
                {
                    if (improvement)
                    {
                        break;
                    }

                    if (new_solution[cache][video] == 0)
                    {
                        new_solution[cache][video] = 1;

                        int solution_score = fitness(new_solution);

                        if (solution_score > new_score)
                        {
                            new_score = solution_score;
                            modified_indexes[0] = cache;
                            modified_indexes[1] = video;
                            improvement = true;
                        }

                        new_solution[cache][video] = 0;
                    }
                }
            }
        }
        else
        {
            boolean valid_solution = false;

            for (int cache = 0; cache < number_of_caches; cache++)
            {
                if (valid_solution)
                {
                    break;
                }

                for (int video = 0; video < number_of_videos; video++)
                {
                    if (new_solution[cache][video] == 1)
                    {
                        new_solution[cache][video] = 0;

                        valid_solution = isValidSolution(new_solution);
                    }

                    if (valid_solution)
                    {
                        break;
                    }
                }
            }
        }

        if (new_score > score)
        {
            int cache = modified_indexes[0];
            int video = modified_indexes[1];

            new_solution[cache][video] = 1;

            return hillClimbing(new_solution, new_score);
        }

        return solution;
    }

    public int[][] genetic(int population_size, int max_generations, double crossover)
    {
        List<int[][]> population = generatePopulation(population_size);

        for (int generation = 0; generation < max_generations; generation++)
        {
            crossover(population, population_size, crossover);
            mutation(population, population.size());

            while (population.size() > population_size)
            {
                int lowest_score = fitness(population.get(0));
                int lowest_score_index = 0;

                for (int i = 1; i < population.size(); i++)
                {
                    int score = fitness(population.get(i));

                    if (score < lowest_score)
                    {
                        lowest_score = score;
                        lowest_score_index = i;
                    }
                }

                population.remove(lowest_score_index);
            }
        }

        int max_score = 0;
        int max_index = 0;

        for (int i = 0; i < population_size; i++)
        {
            int new_score = fitness(population.get(i));

            if (new_score > max_score)
            {
                max_score = new_score;
                max_index = i;
            }
        }

        return population.get(max_index);
    }

    public ArrayList<int[][]> generatePopulation(int population_size)
    {
        ArrayList<int[][]> population = new ArrayList<>();

        for (int i = 0; i < population_size; i++)
        {
            population.add(generateIndividual());
        }        

        return population;
    }

    public int[][] generateIndividual()
    {
        int number_of_caches = (int) data.get("number_of_caches");
        int number_of_videos = (int) data.get("number_of_videos");

        int[][] individual = new int[number_of_caches][number_of_videos];

        for (int i = 0; i < number_of_caches; i++)
        {
            for (int j = 0; j < number_of_videos; j++)
            {
                individual[i][j] = 0;
            }
        }

        Random random = new Random();
        int number_of_changes = Math.floorDiv(number_of_caches * number_of_videos, 2);

        for (int i = 0; i < number_of_changes; i++)
        {
            int cache = random.nextInt(number_of_caches);
            int video = random.nextInt(number_of_videos);

            int value = random.nextInt(2);

            individual[cache][video] = value;

            if (fitness(individual) == -1)
            {
                individual[cache][video] = 0;
            }
        }

        return individual;
    }

    public void crossover(List<int[][]> population, int population_size, double probability)
    {
        int number_of_caches = (int) data.get("number_of_caches");
        int number_of_videos = (int) data.get("number_of_videos");

        Random random = new Random();

        for (int i = 0; i < population_size; i++)
        {
            if (Math.random() < probability)
            {
                int[][] parent1 = population.get(i);

                int parent2_index = random.nextInt(population_size);

                while (parent2_index == i)
                {
                    parent2_index = random.nextInt(population_size);
                }

                int[][] parent2 = population.get(parent2_index);

                int partition = random.nextInt(number_of_caches);

                int[][] child1 = new int[number_of_caches][number_of_videos];
                int[][] child2 = new int[number_of_caches][number_of_videos];

                for (int cache = 0; cache < partition; cache++)
                {
                    for (int video = 0; video < number_of_videos; video++)
                    {
                        child1[cache][video] = parent1[cache][video];
                        child2[cache][video] = parent2[cache][video];
                    }
                }

                for (int cache = partition; cache < number_of_caches; cache++)
                {
                    for (int video = 0; video < number_of_videos; video++)
                    {
                        child1[cache][video] = parent2[cache][video];
                        child2[cache][video] = parent1[cache][video];
                    }
                }

                population.add(child1);
                population.add(child1);
            }
        }
    }

    public void mutation(List<int[][]> population, int population_size)
    {
        int number_of_caches = (int) data.get("number_of_caches");
        int number_of_videos = (int) data.get("number_of_videos");
        double probability = 1.0 / (double) (number_of_caches * number_of_videos);

        Random random = new Random();

        for (int[][] individual : population)
        {
            if (Math.random() < probability)
            {
                int cache = random.nextInt(number_of_caches);
                int video = random.nextInt(number_of_videos);

                if (individual[cache][video] == 0)
                {
                    individual[cache][video] = 1;
                }
                else if (individual[cache][video] == 1)
                {
                    individual[cache][video] = 0;
                }
            }
        }
    }

    public void testGeneticParameters()
    {
        // Test population size
        // 100 works very well but really long so 50 works well
        int[] population_size = {10, 25, 50, 75, 100, 150, 200, 500, 1000, 2000};

        for (int size : population_size)
        {
            double average_score = 0;
            long start = System.currentTimeMillis();

            for (int i = 0; i < 10; i++)
            {
                average_score += fitness(genetic(size, 1000, 0.1));
            }

            long finish = System.currentTimeMillis();
            double time_elapsed = (double) (finish - start) / 1000.0;

            average_score = average_score / 10;

            System.out.println("Average score for " + size + " population size: " + average_score);
            System.out.println("Time taken for " + size + " population size: " + time_elapsed + " seconds");
        }

        // Test crossover probability
        // Anything between 0.05 and 0.25 is good
        double[] crossover_probabiites = {0.001, 0.05, 0.1, 0.2, 0.25};

        for (double crossover : crossover_probabiites)
        {
            double average_score = 0;
            long start = System.currentTimeMillis();

            for (int i = 0; i < 10; i++)
            {
                average_score += fitness(genetic(50, 1000, crossover));
            }

            long finish = System.currentTimeMillis();
            double time_elapsed = (double) (finish - start) / 1000.0;

            average_score = average_score / 10;

            System.out.println("Average score for " + crossover + " crossover probability: " + average_score);
            System.out.println("Time taken for " + crossover + " crossover probability: " + time_elapsed + " seconds");
        }

        // Test num of generations
        // Any is good
        int[] generation_nums = {100, 500, 1000, 2000, 5000};

        for (int gen : generation_nums)
        {
            double average_score = 0;
            long start = System.currentTimeMillis();

            for (int i = 0; i < 10; i++)
            {
                average_score += fitness(genetic(50, gen, 0.1));
            }

            long finish = System.currentTimeMillis();
            double time_elapsed = (double) (finish - start) / 1000.0;

            average_score = average_score / 10;

            System.out.println("Average score for " + gen + " generations: " + average_score);
            System.out.println("Time taken for " + gen + " generations: " + time_elapsed + " seconds");
        } 
    }

    public int[][] simulatedAnnealing()
    {
        double initial_temperature = 1000.0;
        double final_temperature = 0.1;
        double cooling_rate = 0.99;
        int max_iterations = 10000;

        double temperature = initial_temperature;
        Random random = new Random();

        // Initialise current soltion as a starting point
        int[][] current_solution = generateIndividual();

        for (int i = 0; i < max_iterations && temperature > final_temperature; i++)
        {
            // Explore a neighbouring solution
            int[][] neighbouring_solution = generateRandomNeighbour(current_solution);

            int current_score = fitness(current_solution);
            int neighbouring_score = fitness(neighbouring_solution);

            double delta = neighbouring_score - current_score;

            // Problem: Got stuck in a local maxima
            // Solution: This algorithm is usually to get to lower points so change the formula for maximun points

            if (delta > 0 || Math.exp(delta / temperature) > random.nextDouble())
            {
                current_solution = neighbouring_solution;
            }

            temperature *= cooling_rate;
        }

        return current_solution;
    }

    public int[][] generateRandomNeighbour(int[][] solution)
    {
        // This should never be stuck in a cycle where all the solutions are invalid
        // This is due to te fact that generateIndividual always generates valid solutions

        int number_of_caches = (int) data.get("number_of_caches");
        int number_of_videos = (int) data.get("number_of_videos");

        int[][] neighbouring_solution = new int[number_of_caches][number_of_videos];

        for (int cache = 0; cache < number_of_caches; cache++)
        {
            for (int video = 0; video < number_of_videos; video++)
            {
                neighbouring_solution[cache][video] = solution[cache][video];
            }
        }

        Random random = new Random();

        // Problem: Can get stuck with the same solution if the random value is the same as the one already in the solution
        // Solution: Generate 2 to 3 random values
        // I could also guarantee the values are always different

        for (int i = 0; i < number_of_caches; i++)
        {
            int cache = random.nextInt(number_of_caches);
            int video = random.nextInt(number_of_videos);

            int initial_value = neighbouring_solution[cache][video];

            if (initial_value == 0)
            {
                neighbouring_solution[cache][video] = 1;
            }
            else if (initial_value == 1)
            {
                neighbouring_solution[cache][video] = 0;
            }

            if (!isValidSolution(neighbouring_solution))
            {
                neighbouring_solution[cache][video] = initial_value;
            }
        }

        return neighbouring_solution;
    }

    public int fitness(int[][] solution)
    {
        if (!isValidSolution(solution))
        {
            return -1;
        }

        // Compute the cost of downloading the videos from the data centre
        // Find the cost of downloading the same file from a cache server connected to the endpoint 
        Map<String, String> video_ed_request = (HashMap<String, String>) data.get("video_ed_request");
        List<List<Integer>> ed_cache_list = (List<List<Integer>>) data.get("ed_cache_list");
        List<Integer> ep_to_dc_latency = (List<Integer>) data.get("ep_to_dc_latency");
        List<List<Integer>> ep_to_cache_latency = (List<List<Integer>>) data.get("ep_to_cache_latency");

        int total_gain = 0;
        int total_requests = 0;
        
        for (Map.Entry<String, String> set : video_ed_request.entrySet())
        {
            String key = set.getKey();
            String value = set.getValue();

            String[] video_id_ed_id = key.split(",");
            int video_id = Integer.parseInt(video_id_ed_id[0]);
            int ed_id = Integer.parseInt(video_id_ed_id[1]);
            int requests = Integer.parseInt(value);

            int ep_to_dc_download_cost = ep_to_dc_latency.get(ed_id);
            int ep_to_cache_download_cost = ep_to_dc_download_cost; // Default value
            int cache_gain = 0;

            for (int cache_id : ed_cache_list.get(ed_id))
            {
                if (solution[cache_id][video_id] == 1)
                {
                    int current_ep_to_cache_download_cost = ep_to_cache_latency.get(ed_id).get(cache_id);

                    if (current_ep_to_cache_download_cost < ep_to_cache_download_cost)
                    {
                        ep_to_cache_download_cost = current_ep_to_cache_download_cost;
                    }
                }
            }

            cache_gain = ep_to_dc_download_cost - ep_to_cache_download_cost;

            total_gain += requests * cache_gain;
            total_requests += requests;
        }

        return (int) (((float) total_gain / (float) total_requests) * 1000);
    }

    public boolean isValidSolution(int[][] solution)
    {
        // Check that the solution array is of the correct length
        int number_of_caches = (int) data.get("number_of_caches");
        int number_of_videos = (int) data.get("number_of_videos");

        if (number_of_caches != solution.length)
        {
            return false;
        }

        for (int i = 0; i < number_of_caches; i++)
        {
            if (number_of_videos != solution[i].length)
            {
                return false;
            }
        }

        // Check that the sum of file sizes is not overflowing the cache server
        int cache_size = (int) data.get("cache_size");
        int[] video_size_desc = (int[]) data.get("video_size_desc");

        for (int i = 0; i < number_of_caches; i++)
        {
            int cache_i_storage = 0;

            for (int j = 0; j < number_of_videos; j++)
            {
                if (solution[i][j] == 1)
                {
                    cache_i_storage += video_size_desc[j];
                }

                if (cache_i_storage > cache_size)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public void readGoogle(String filename) throws IOException
    {
        // Read in the file
        BufferedReader fin = new BufferedReader(new FileReader(filename));

        /*
         * System Description:
         * 
         * The first line in the input file contains
         * the number of videos, the number of endpoints,
         * the number of requests descriptions, the number of caches,
         * and the size of each cache
         * 
         * e.g. 5 2 4 3 100
         * 
         * 5 videos, 2 endpoints,
         * 4 request descriptions, 3 caches
         * 100MB each
         */
        String system_desc = fin.readLine();
        String[] system_desc_arr = system_desc.split(" ");
        int number_of_videos = Integer.parseInt(system_desc_arr[0]);
        int number_of_endpoints = Integer.parseInt(system_desc_arr[1]);
        int number_of_requests = Integer.parseInt(system_desc_arr[2]);
        int number_of_caches = Integer.parseInt(system_desc_arr[3]);
        int cache_size = Integer.parseInt(system_desc_arr[4]);

        /*
         * Video Size Description:
         * 
         * The second line in the input file contains
         * the size of each video (indexes are important)
         * 
         * e.g. 50 50 80 30 110
         * 
         * Videos 0, 1, 2, 3, 4 have sizes 50MB, 50MB, 80MB, 30MB, 110MB.
         * 
         * The following lines of code read in a line from the input file
         * then splits the line based on the spaces in the line
         * and stores it in a String array
         * then converts those strings and stores them into an int array
         */
        Map<String, String> video_ed_request = new HashMap<String, String>();
        String video_size_desc_str = fin.readLine();
        String[] video_size_desc_arr = video_size_desc_str.split(" ");
        int[] video_size_desc = new int[video_size_desc_arr.length];

        for (int i = 0; i < video_size_desc_arr.length; i++)
        {
            video_size_desc[i] = Integer.parseInt(video_size_desc_arr[i]);
        }

        /*
         * Endpoint Description:
         * 
         * The next section describes each of the endpoints
         * 
         * The first line describing an endpoints contains
         * the latency from the data centre to the endpoint,
         * and the number of caches this endpoint is connected to
         * 
         * e.g. 1000 3
         * 
         * The next lines are dependant on the number of cache
         * servers the endpoint is connected to
         * 
         * The next line describing an endpoint contains
         * the ID of the cache server,
         * and the latency from this cache server to this endpoint
         * 
         * e.g. 0 100
         * 
         * Note: assume ed to be the pov of the cache server to the endpoint
         */
        List<List<Integer>> ed_cache_list = new ArrayList<List<Integer>>();
        List<Integer> ep_to_dc_latency = new ArrayList<Integer>();
        List<List<Integer>> ep_to_cache_latency = new ArrayList<List<Integer>>();

        for (int i = 0; i < number_of_endpoints; i++)
        {
            ep_to_dc_latency.add(0);
            ep_to_cache_latency.add(new ArrayList<Integer>());

            String[] endpoint_desc_arr = fin.readLine().split(" ");
            int dc_latency = Integer.parseInt(endpoint_desc_arr[0]);
            int number_of_cache_i = Integer.parseInt(endpoint_desc_arr[1]);
            ep_to_dc_latency.set(i, dc_latency);

            /* 
             * The endpoint to cache latency to set to the endpoint
             * to data centre latency plus 1
             * 
             * This way it is disadvantagous to get store the video in
             * a cache server
             * 
             * It is also another way to show connections that are
             * not possible
             */
            for (int j = 0; j < number_of_caches; j++)
            {
                ep_to_cache_latency.get(i).add(ep_to_dc_latency.get(i) + 1);
            }

            List<Integer> cache_list = new ArrayList<Integer>();

            for (int j = 0; j < number_of_cache_i; j++)
            {
                String[] cache_desc_arr = fin.readLine().split(" ");
                int cache_id = Integer.parseInt(cache_desc_arr[0]);
                int latency = Integer.parseInt(cache_desc_arr[1]);
                cache_list.add(cache_id);
                ep_to_cache_latency.get(i).set(cache_id, latency);
            }

            ed_cache_list.add(cache_list);
        }

        /* 
         * Request Description:
         * 
         * The last section describing the video descriptions contains
         * the ID of the requested video,
         * the ID of the endpoint from which the request is coming from,
         * and the number of requests
         * 
         * e.g. 3 0 1500
         */
        for (int i = 0; i < number_of_requests; i++)
        {
            String[] request_desc_arr = fin.readLine().split(" ");
            String video_id = request_desc_arr[0];
            String ed_id = request_desc_arr[1];
            String requests = request_desc_arr[2];

            // The key is video_id, ed_if
            // The value is requests
            video_ed_request.put(video_id + "," + ed_id, requests);
        }

        data.put("number_of_videos", number_of_videos);
        data.put("number_of_endpoints", number_of_endpoints);
        data.put("number_of_requests", number_of_requests);
        data.put("number_of_caches", number_of_caches);
        data.put("cache_size", cache_size);
        data.put("video_size_desc", video_size_desc);
        data.put("ep_to_dc_latency", ep_to_dc_latency);
        data.put("ep_to_cache_latency", ep_to_cache_latency);
        data.put("ed_cache_list", ed_cache_list);
        data.put("video_ed_request", video_ed_request);

        fin.close();
    }

    public String toString()
    {
        String result = "";

        // for each endpoint:
        for (int i = 0; i < (Integer) data.get("number_of_endpoints"); i++)
        {
            // endpoint number
            result += "endpoint number " + i + "\n";

            // latency to DC
            int latency_dc = ((List<Integer>) data.get("ep_to_dc_latency")).get(i);
            result += "latency to dc " + latency_dc + "\n";

            // for each cache
            for (int j = 0; j < ((List<List<Integer>>) data.get("ep_to_cache_latency")).get(i).size(); j++)
            {
                int latency_c = ((List<List<Integer>>) data.get("ep_to_cache_latency")).get(i).get(j);
                result += "latency to cache number " + j + " = " + latency_c + "\n";
            }
        }

        return result;
    }

    public static void main(String[] args) throws IOException
    {
        ReadInput ri = new ReadInput();
        ri.readGoogle("input/me_at_the_zoo.in");
        System.out.println(ri.data.get("video_ed_request"));
        System.out.println(ri.toString());

        // Run the optimisation algorithms
        System.out.println(ri.fitness(ri.hillClimbing()));
        System.out.println(ri.fitness(ri.simpleHillClimbing()));
        System.out.println(ri.fitness(ri.genetic(50, 1000, 0.1)));
        ri.testGeneticParameters();
        System.out.println(ri.fitness(ri.simulatedAnnealing()));
    }
}
