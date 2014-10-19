package svm;

import indicator.StockIndicator;
import indicator.StockIndicatorArray;
import indicator.StockIndicatorConst;
import indicator.StockIndicatorParser;

import java.util.Date;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

/**
 * http://www.csie.ntu.edu.tw/~cjlin/papers/guide/guide.pdf
 * http://java-ml.sourceforge.net/api/0.1.6/libsvm/GridSearch.html
 * http://java-ml.sourceforge.net/api/0.1.6/libsvm/LibSVM.html (Wrapper of LibSVM)
 * @author jimmyzzxhlh-Dell
 *
 */
public class SVMTrain {
	
	
	private StockIndicatorArray stockIndicatorArray;
	private svm_problem svmProblem;
	private int dataCount;
	private svm_parameter svmParameter;
	private svm_model svmModel;
	
	public StockIndicatorArray getStockIndicatorArray() {
		return stockIndicatorArray;
	}

	public void setStockIndicatorArray(StockIndicatorArray stockIndicatorArray) {
		this.stockIndicatorArray = stockIndicatorArray;
	}

	public SVMTrain() {
		
	}
	
	public void initializeStockIndicatorArray(String directoryName) { 
		stockIndicatorArray = StockIndicatorParser.readCSVFiles(directoryName);
	}
	
	public void initializeStockIndicatorArray(String directoryName, Date startDate, Date endDate) {
		stockIndicatorArray = StockIndicatorParser.readCSVFiles(directoryName, startDate, endDate);
	}
	
	public void setGamma(double gamma) {
		svmParameter.gamma = gamma;		
	}
	
	public void setC(double C) {
		svmParameter.C = C;
	}
	
	public void createProblem() {
		svmProblem = new svm_problem();
	    dataCount = stockIndicatorArray.size();
	    svmProblem.l = dataCount;
	    svmProblem.x = new svm_node[dataCount][];
	    svmProblem.y = new double[dataCount];
	    
	    for (int i = 0; i < dataCount; i++) {
	    	StockIndicator stockIndicator = stockIndicatorArray.get(i);
	    	svm_node[] nodes = createSVMNodeArray(stockIndicator);
	    	if (nodes == null) {
				System.err.println("NAN values found from " + stockIndicator.getSymbol() + " at " + stockIndicator.getDate().toLocaleString());
			}
			
	    	svmProblem.x[i] = nodes;
	        //We need to get the classification instead of the stock gain itself.
	        svmProblem.y[i] = stockIndicatorArray.getStockGainClassification(i);
	    }
	}
	
	public void createDefaultParameter() {	
	    svmParameter = new svm_parameter();
	    //whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)
	    svmParameter.probability = 0;  
	    //set gamma in kernel function (default 1/num_features)
	    //Ideal value of Gamma needs to be searched.
	    svmParameter.gamma = 0.5;
	    //set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)
	    svmParameter.nu = 0.5;
	    //set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)
	    //Ideal value of C needs to be searched.
	    svmParameter.C = 1;
//	    svm_type : set type of SVM (default 0)
//		0 -- C-SVC		(multi-class classification)
//		1 -- nu-SVC		(multi-class classification)
//		2 -- one-class SVM	
//		3 -- epsilon-SVR	(regression)
//		4 -- nu-SVR		(regression)
	    svmParameter.svm_type = svm_parameter.C_SVC;
//	    kernel_type : set type of kernel function (default 2)
//		0 -- linear: u'*v
//		1 -- polynomial: (gamma*u'*v + coef0)^degree
//		2 -- radial basis function: exp(-gamma*|u-v|^2)
//		3 -- sigmoid: tanh(gamma*u'*v + coef0)
//		4 -- precomputed kernel (kernel values in training_set_file)
	    svmParameter.kernel_type = svm_parameter.RBF;
//	    Set cache memory size in MB (default 100)
	    svmParameter.cache_size = 1000;
//	    set the epsilon in loss function of epsilon-SVR (default 0.1)
	    svmParameter.eps = 0.001;     
	    
	}
	
	public void startTraining() {
	    System.out.println("Training SVM model...");
	    svmModel = svm.svm_train(svmProblem, svmParameter);
	}
	
	public double predictSingleDay(StockIndicator stockIndicator) {
		//Notice that if we have done scaling on the training data, then the testing data also needs to be scaled
		//using the same method.
		svm_node[] nodes = createSVMNodeArray(stockIndicator);
		if (nodes == null) {
			System.err.println("NAN values found from " + stockIndicator.getSymbol() + " at " + stockIndicator.getDate().toLocaleString());
		}
		
		int[] labels = new int[StockIndicatorConst.STOCK_GAIN_CLASSIFICATION_COUNT];
		svm.svm_get_labels(svmModel, labels);
		
//	    double[] prob_estimates = new double[StockIndicatorConst.STOCK_GAIN_CLASSIFICATION_COUNT];
//	    double predict = svm.svm_predict_probability(svmModel, nodes, prob_estimates);
		double[] dec_values = new double[StockIndicatorConst.STOCK_GAIN_CLASSIFICATION_COUNT];
	    double predict = svm.svm_predict_values(svmModel, nodes, dec_values);
	 
	    return predict;
	}
	
	public static svm_node[] createSVMNodeArray(StockIndicator stockIndicator) { 
    	double[] indicatorVector = stockIndicator.getNormalizedIndicatorVector();
    	//If any indicator value is NAN, then return null
    	if (indicatorVector == null) return null;
        svm_node[] svmNodeArray = new svm_node[indicatorVector.length];
        for (int i = 0; i < indicatorVector.length; i++){
            svm_node node = new svm_node();
            node.index = i;
            node.value = indicatorVector[i];
            svmNodeArray[i] = node;
        }     
        return svmNodeArray;
	}
	
}
