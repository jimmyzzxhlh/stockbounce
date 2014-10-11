package test;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVMTest {
	public static void main(String args[]) {
		testSVMUsingFakeData();
	}
	
	
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
	    param.cache_size = 20000;
//	    set the epsilon in loss function of epsilon-SVR (default 0.1)
	    param.eps = 0.001;      

	    svm_model model = svm.svm_train(svmProblem, param);
	    
	    //Create test cases and evaluate 
	    double[] testFeature = createTestFeature(1, 0, 1001);
	    testSVMEvaluate(testFeature, model);
	    testFeature = createTestFeature(0, 0, 500);
	    testSVMEvaluate(testFeature, model);
	    testFeature = createTestFeature(0, 0, -1);
	    testSVMEvaluate(testFeature, model);
	   
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

	    double[] prob_estimates = new double[TOTAL_CLASSES];
	    double v = svm.svm_predict_probability(model, nodes, prob_estimates);
	 
	    for (int i = 0; i < TOTAL_CLASSES; i++) {
	        System.out.print("(" + labels[i] + ":" + prob_estimates[i] + ")");
	    }
	    System.out.println("(Actual:" + features[0] + " Prediction:" + v + ")");            
	    
	}
	
	private static double[] createTestFeature(double result, double x, double y) {
		double[] feature = new double[3];
		feature[0] = result;
		feature[1] = x;
		feature[2] = y;
		return feature;
	}
}
