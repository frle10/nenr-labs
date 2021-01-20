package hr.fer.zemris.nenr.neurofuzzy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static hr.fer.zemris.nenr.neurofuzzy.Util.*;

/**
 * This program demos the functionality of the ANFIS neuro-fuzzy system by making
 * it approximate a real function of two variables.
 * <p>
 * Number of rules is a user entered parameter which directly impacts the number of
 * parameters the ANFIS system needs to learn.
 * 
 * @author Ivan Skorupan
 */
public class NeuroFuzzyDemo {
	
	/**
	 * Path to dataset export file.
	 */
	private static final String DATASET_EXPORT_PATH = "dataset.dat";
	
	/**
	 * Path to learned function export file.
	 */
	private static final String LEARNED_FUNCTION_EXPORT_PATH = "learned-function.dat";
	
	/**
	 * Path to deviations export file.
	 */
	private static final String DEVIATIONS_EXPORT_PATH = "deviations.dat";
	
	/**
	 * Path to fuzzy sets export file.
	 */
	private static final String FUZZY_SETS_EXPORT_PATH = "fuzzy-sets.dat";
	
	/**
	 * Path to mean squared error export file.
	 */
	private static final String ERROR_EXPORT_PATH = "error.dat";
	
	/**
	 * Starting point of this program's execution.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("The program expects exactly one argument (number of rules for the ANFIS system).");
			return;
		}
		
		int numberOfRules;
		try {
			numberOfRules = Integer.parseInt(args[0]);
			
			if (numberOfRules < 1)
				throw new NumberFormatException();
		} catch (NumberFormatException ex) {
			System.err.println("Number of rules must be a positive integer.");
			return;
		}
		
		Dataset trainingSet = generateDemoTrainingDataset();
		
//		Path datasetExportPath = Paths.get(DATASET_EXPORT_PATH);
//		try {
//			trainingSet.exportToFile(datasetExportPath);
//		} catch (IOException e) {
//			System.err.println("There was an error while exporting dataset to a file.");
//			e.printStackTrace();
//			return;
//		}
		
		Path errorExportPath = Paths.get(ERROR_EXPORT_PATH);
		
		ANFIS anfis = new ANFIS(numberOfRules);
		try {
			anfis.fit(trainingSet, false, true, true, errorExportPath);
		} catch (IOException e) {
			System.err.println("There was an error while exporting mean squared error to a file during training.");
			e.printStackTrace();
		}
		
//		Path learnedFunctionExportPath = Paths.get(LEARNED_FUNCTION_EXPORT_PATH);
//		Path deviationsExportPath = Paths.get(DEVIATIONS_EXPORT_PATH);
//		Path fuzzySetsExportPath = Paths.get(FUZZY_SETS_EXPORT_PATH);
		
//		try {
//			anfis.exportLearnedFunction(trainingSet, learnedFunctionExportPath);
//		} catch (IOException e) {
//			System.err.println("There was an error while exporting the learned function to a file.");
//			e.printStackTrace();
//		}
//		
//		try {
//			anfis.exportDeviations(trainingSet, deviationsExportPath);
//		} catch (IOException e) {
//			System.err.println("There was an error while exporting the deviations to a file.");
//			e.printStackTrace();
//		}
		
//		try {
//			anfis.exportFuzzySets(fuzzySetsExportPath);
//		} catch (IOException e) {
//			System.err.println("There was an error while exporting fuzzy sets to a file.");
//			e.printStackTrace();
//		}
	}

}
