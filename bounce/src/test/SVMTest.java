package test;

import indicator.StockIndicator;
import indicator.StockIndicatorArray;
import indicator.StockIndicatorConst;
import indicator.StockIndicatorParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import svm.SVMTrain;


public class SVMTest {
	public static void main(String args[]) {
//		testSVMUsingFakeData();
		testSVMUsingRealData();
//		testOneClassSVMUsingFakeData();
//		testOneClassSVMUsingRealData();
	}
	
	/**
	 * This is only a sample test of SVM.
	 */
	public static void testSVMUsingFakeData() {
		//Create training data
		double[][] trainData = new double[1000][]; 
		
		//x is always 0, y is 2 * i or -2 * i depending on whether i is > 500 or not.
		//So for value 1, it should happen when y > 1000. For value 0, it should happen when y <= 0 
		for (int i = 0; i < trainData.length; i++){
		    if (i > (trainData.length / 2)){     
		        double[] vals = {1, 0, 2 * i};  
		        trainData[i] = vals;
		    } else {
		        double[] vals = {0, 0, -2 * i}; 
		        trainData[i] = vals;
		    }           
		}
		
		//Create the model
		svm_problem svmProblem = new svm_problem();
	    int dataCount = trainData.length;
	    svmProblem.x = new svm_node[dataCount][];
	    svmProblem.y = new double[dataCount];
	    svmProblem.l = dataCount;
	    
	    //Copy the data from trainData array to each svm_node.
	    //Each entry (svmProblem.x[i]) should be an array of svm_node where
	    //each subscript of the entry represents a feature (index, value).
	    
	    for (int i = 0; i < dataCount; i++){            
	        double[] features = trainData[i];
	        svmProblem.x[i] = new svm_node[features.length - 1];
	        for (int j = 1; j < features.length; j++){
	            svm_node node = new svm_node();
	            node.index = j;
	            node.value = features[j];
	            svmProblem.x[i][j - 1] = node;
	        }           
	        svmProblem.y[i] = features[0];
	    }               

	    //Train the model.
	    svm_parameter param = new svm_parameter();
	    //whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)
	    param.probability = 1;  
	    //set gamma in kernel function (default 1/num_features)
	    param.gamma = 0.5;
	    //set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)
	    param.nu = 0.5;
	    //set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)
	    param.C = 1;
//	    svm_type : set type of SVM (default 0)
//		0 -- C-SVC		(multi-class classification)
//		1 -- nu-SVC		(multi-class classification)
//		2 -- one-class SVM	
//		3 -- epsilon-SVR	(regression)
//		4 -- nu-SVR		(regression)
	    param.svm_type = svm_parameter.C_SVC;
//	    kernel_type : set type of kernel function (default 2)
//		0 -- linear: u'*v
//		1 -- polynomial: (gamma*u'*v + coef0)^degree
//		2 -- radial basis function: exp(-gamma*|u-v|^2)
//		3 -- sigmoid: tanh(gamma*u'*v + coef0)
//		4 -- precomputed kernel (kernel values in training_set_file)
	    param.kernel_type = svm_parameter.LINEAR;
//	    Set cache memory size in MB (default 100)
	    param.cache_size = 2000;
//	    set the epsilon in loss function of epsilon-SVR (default 0.1)
	    param.eps = 0.001;      

	    svm_model model = svm.svm_train(svmProblem, param);
	    
	    //Create test cases and evaluate 
	    double[] testFeature = createTestFeature(1, 0, 1001);
	    testSVMEvaluate(testFeature, model);
	    testFeature = createTestFeature(0, 0, 600);
	    testSVMEvaluate(testFeature, model);
	    testFeature = createTestFeature(0, 0, -1);
	    testSVMEvaluate(testFeature, model);
	   
	}
	
	public static void testOneClassSVMUsingFakeData() {

		//Create training data
		//Create the model
		svm_problem svmProblem = new svm_problem();
	    int dataCount = 1000;
	    svmProblem.x = new svm_node[dataCount][];
	    svmProblem.y = new double[dataCount];
	    svmProblem.l = dataCount;
	    
	    for (int i = 0; i < dataCount; i++){            
	        svmProblem.x[i] = new svm_node[2];
	        svm_node nodeOne = new svm_node();
	        nodeOne.index = 1;
	        nodeOne.value = i + 1000;
	        svm_node nodeTwo = new svm_node();
	        nodeTwo.index = 2;
	        nodeTwo.value = 0;
	        svmProblem.x[i][0] = nodeOne;
	        svmProblem.x[i][1] = nodeTwo;
	        svmProblem.y[i] = 1;
	    }               

	    //Train the model.
	    svm_parameter param = new svm_parameter();
	    //whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)
	    param.probability = 0;  
	    //set gamma in kernel function (default 1/num_features)
	    param.gamma = 0.1;
	    //set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)
	    //parameter nu in (0,1] is an upper bound on the fraction of training errors and a lower bound of the fraction of support vectors.
	    param.nu = 0.1;	    
//	    svm_type : set type of SVM (default 0)
//		0 -- C-SVC		(multi-class classification)
//		1 -- nu-SVC		(multi-class classification)
//		2 -- one-class SVM	
//		3 -- epsilon-SVR	(regression)
//		4 -- nu-SVR		(regression)
	    param.svm_type = svm_parameter.ONE_CLASS;
//	    kernel_type : set type of kernel function (default 2)
//		0 -- linear: u'*v
//		1 -- polynomial: (gamma*u'*v + coef0)^degree
//		2 -- radial basis function: exp(-gamma*|u-v|^2)
//		3 -- sigmoid: tanh(gamma*u'*v + coef0)
//		4 -- precomputed kernel (kernel values in training_set_file)
	    param.kernel_type = svm_parameter.RBF;
//	    Set cache memory size in MB (default 100)
	    param.cache_size = 2000;
//	    set the epsilon in loss function of epsilon-SVR (default 0.1)
	    param.eps = 0.001;      

	    svm_model model = svm.svm_train(svmProblem, param);
	    
	    //Create test cases and evaluate 
	    svm_node[] testNodes = createOneClassTestNodes(-1);
	    testOneClassSVMEvaluate(model, testNodes, -1);
	    testNodes = createOneClassTestNodes(1500.5);
	    testOneClassSVMEvaluate(model, testNodes, 1);
	    testNodes = createOneClassTestNodes(1500);
	    testOneClassSVMEvaluate(model, testNodes, 1);
	    testNodes = createOneClassTestNodes(10000);
	    testOneClassSVMEvaluate(model, testNodes, -1);
	    testNodes = createOneClassTestNodes(2000.5);
	    testOneClassSVMEvaluate(model, testNodes, -1);
	    testNodes = createOneClassTestNodes(2000);
	    testOneClassSVMEvaluate(model, testNodes, -1);
	    testNodes = createOneClassTestNodes(1999);
	    testOneClassSVMEvaluate(model, testNodes, 1);
	    testNodes = createOneClassTestNodes(1998);
	    testOneClassSVMEvaluate(model, testNodes, 1);
	    testNodes = createOneClassTestNodes(1998.01);
	    testOneClassSVMEvaluate(model, testNodes, 1);
	   
	}
	private static void testSVMEvaluate(double[] features, svm_model model) {
		//Notice that the features array can be in double format.
		
		//Copy the feature array to svm node.
		svm_node[] nodes = new svm_node[features.length - 1];
	    for (int i = 1; i < features.length; i++)
	    {
	        svm_node node = new svm_node();
	        node.index = i;
	        node.value = features[i];
	        nodes[i - 1] = node;
	    }

	    int TOTAL_CLASSES = 2;       
	    int[] labels = new int[TOTAL_CLASSES];
	    
//	    For a classification model, this function outputs the name of
//	    labels into an array called label. For regression and one-class
//	    models, label is unchanged.
	    svm.svm_get_labels(model, labels);

//	    double[] prob_estimates = new double[TOTAL_CLASSES];
//	    double v = svm.svm_predict_probability(model, nodes, prob_estimates);
	    
		double[] dec_values = new double[StockIndicatorConst.STOCK_GAIN_CLASSIFICATION_COUNT];
	    double predict = svm.svm_predict_values(model, nodes, dec_values);
	    System.out.println("(Actual:" + features[0] + " Prediction:" + predict + ")");            
	    
	}
	
	private static void testOneClassSVMEvaluate(svm_model model, svm_node nodes[], double result) {
//		double[] dec_values = new double[1];
//		double predict = svm.svm_predict_values(model, nodes, dec_values);
//	    System.out.println("(Actual:" + result + " Prediction:" + predict + " Dec_values:" + dec_values[0] + ")");
		double predict = svm.svm_predict(model, nodes);
	    System.out.println("(Actual:" + result + " Prediction:" + predict + ")");
	}
	
	private static double[] createTestFeature(double result, double x, double y) {
		double[] feature = new double[3];
		feature[0] = result;
		feature[1] = x;
		feature[2] = y;
		return feature;
	}
	
	public static svm_node[] createOneClassTestNodes(double x) {
		svm_node[] nodes = new svm_node[2];
		svm_node nodeOne = new svm_node();
		nodeOne.index = 1;
		nodeOne.value = x;
	    nodes[0] = nodeOne;
		svm_node nodeTwo = new svm_node();
		nodeTwo.index = 2;
		nodeTwo.value = 0;
	    nodes[1] = nodeTwo;
	    return nodes;	    
	    
	}
	
	/**
	 * Testing SVM using indicator CSV files.
	 */
	private static void testSVMUsingRealData() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date trainStartDate = null;
		Date trainEndDate = null;
		//Specify the date range to test only a certain period.
		try {
			trainStartDate = formatter.parse("2014-01-01");
			trainEndDate = formatter.parse("2014-06-30");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		SVMTrain svmTrain = new SVMTrain();
		svmTrain.initializeStockIndicatorArray(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH, trainStartDate, trainEndDate);
		StockIndicatorArray stockIndicatorArray = svmTrain.getStockIndicatorArray();
		System.out.println("Total training data: " + stockIndicatorArray.size());
		
		svmTrain.createProblem();
		svmTrain.createDefaultParameter();
		
		svmTrain.startTraining();
		int correct = 0;
		int wrong = 0;
		int predictCorrect = 0;
		int predictWrong = 0;
		//Right now just testing using the trained data (which, ideally, should be 100% accurate)
		for (int i = 0; i < stockIndicatorArray.size(); i++) {
			StockIndicator stockIndicator = stockIndicatorArray.get(i);
			int predict = (int)svmTrain.predictSingleDay(stockIndicator);
			if (predict == 1) {
				if (stockIndicator.getStockGainClassification() == predict) {
					predictCorrect++;
				}
				else if (stockIndicator.getStockGain() >= 10) {
					predictCorrect++;
				}
				else {
					predictWrong++;
				}				
			}
			if (stockIndicator.getStockGainClassification() == predict) {
				correct++;
			}
			else {
				wrong++;
			}					
		}
		double accuracy = correct * 1.0 / (correct + wrong);
		System.out.println("Correct: " + correct + ", Wrong: " + wrong + ", Accuracy: " + accuracy);
		accuracy = predictCorrect * 1.0 / (predictCorrect + predictWrong);
		System.out.println("Predict Correct: " + predictCorrect + ", Wrong: " + predictWrong + ", Accuracy: " + accuracy);
		
//		int sample = 0;
//		for (int i = 0; i < stockIndicatorArray.size(); i++) {
//			if (stockIndicatorArray.getStockGain(i) >= 10) {
//				sample++;
//			}
//		}
//		System.out.println(sample + " out of " + stockIndicatorArray.size() + " actually increased 10%.");
		
//		try {
//			trainStartDate = formatter.parse("2014-03-01");
//			trainEndDate = formatter.parse("2014-03-31");
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		StockIndicatorArray testingStockIndicatorArray = StockIndicatorParser.readCSVFiles(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH, trainStartDate, trainEndDate);
//		int correct = 0;
//		int correctSample = 0;
//		int wrong = 0;
//		int wrongSample = 0;
//		int predictSample = 0;
//		//Right now just testing using the trained data (which, ideally, should be 100% accurate)
//		for (int i = 0; i < testingStockIndicatorArray.size(); i++) {
//			StockIndicator stockIndicator = testingStockIndicatorArray.get(i);
//			int predict = (int)svmTrain.predictSingleDay(stockIndicator);
//			if (predict == 1) {
//				predictSample++;
//				if (stockIndicator.getStockGainClassification() == predict) {
//					correctSample++;
//				}
//				else {
//					wrongSample++;
//				}
//			}
//			if (stockIndicator.getStockGainClassification() == predict) {
//				correct++;
//			}
////			else if ((predict == 1) && (stockIndicator.getStockGain() >= 10)) {
////				//If we are expecting the stock to have gain >= 20% but it actually has gain >= 10% then still count it as correct.
////				correct++;
////			}
//			else {
//				wrong++;
//			}			
//		}
//		double accuracy = correct * 1.0 / (correct + wrong);
//		System.out.println("Correct: " + correct + ", Wrong: " + wrong + ", Accuracy: " + accuracy);
//		System.out.println("Predict >= 10% sample: " + predictSample);
//		System.out.println("Correct Sample: " + correctSample + ", Wrong Sample: " + wrongSample + ", Accuracy: " + (correctSample * 1.0 / predictSample));
		
		
	}
	
	/**
	 * Testing SVM using indicator CSV files.
	 */
	private static void testOneClassSVMUsingRealData() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date trainStartDate = null;
		Date trainEndDate = null;
		//Specify the date range to test only a certain period.
		try {
			trainStartDate = formatter.parse("2011-01-01");
			trainEndDate = formatter.parse("2013-12-31");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		SVMTrain svmTrain = new SVMTrain();
		svmTrain.initializeStockIndicatorArray(StockIndicatorConst.INDICATOR_CSV_DIRECTORY_PATH, trainStartDate, trainEndDate);
		StockIndicatorArray stockIndicatorArray = svmTrain.getStockIndicatorArray();
		System.out.println("Total training data: " + stockIndicatorArray.size());
		System.out.println("Total >= 20% gain training data: " + stockIndicatorArray.getStockGainCount(StockIndicatorConst.STOCK_GAIN_MIN_FOR_ONE_CLASS_SVM));
		
		svmTrain.createProblemForOneClassSVM();
		svmTrain.createDefaultParameterForOneClassSVM();
		
		svmTrain.startTraining();
		int correct = 0;
		int wrong = 0;
		int predictOne = 0;
		int predictZero = 0;
		//Right now just testing using the trained data (which, ideally, should be 100% accurate)
		for (int i = 0; i < stockIndicatorArray.size(); i++) {
			StockIndicator stockIndicator = stockIndicatorArray.get(i);
			int predict = (int)svmTrain.predictSingleDayForOneClassSVM(stockIndicator);
			if (predict == 1) {
				predictOne++;
				if (svmTrain.hasEnoughStockGain(stockIndicator)) {
					correct++;
				}
				else if (stockIndicator.getStockGain() >= 10) {
					correct++;
				}
				else {
					wrong++;
				}				
			}		
			else {
				predictZero++;
			}
		}
		double accuracy = correct * 1.0 / (correct + wrong);
		System.out.println("Correct: " + correct + ", Wrong: " + wrong + ", Accuracy: " + accuracy);
		System.out.println("Predict 1: " + predictOne + ", 0: " + predictZero);
		
	}
}

