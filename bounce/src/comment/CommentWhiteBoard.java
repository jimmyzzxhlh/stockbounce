package comment;

/**
 * ����ط���Ϊһ���װ�������
 * 2014/10/16
 * 1. ����ƶ����ԣ�
 * ���ܵ�һ���������ڵ�������֮�󣬼��㵱���ָ�꣬Ȼ�������ϵͳȥԤ�⡣
 * ��������ǽ����źţ���ô�ڵڶ��쿪��֮��������˺�ÿһ�춼��ͬ����Ԥ�⣬ֱ������
 * ��ת��Ԥ��֮�������
 * �����������֪��Ԥ���Ǵ���ģ�
 * 2. һЩ������EMA��ָ�꣬Ҫ�޳�ǰ���һЩ���ݣ���ΪEMAһ��ʼ��ʱ���ǲ�׼ȷ�ģ�
 * ֱ����һ��ʱ��֮��Ż�������
 * 3. ���ϵͳ��׼ȷ��Ҫ��Grid Search��ȷ��C��gamma��ֵ���μ��������ƪ���£�
 * http://www.csie.ntu.edu.tw/~cjlin/papers/guide/guide.pdf
 * ������ҪһЩ�����Ŀ����������磺
 * http://java-ml.sourceforge.net/api/0.1.6/libsvm/GridSearch.html
 * http://java-ml.sourceforge.net/api/0.1.6/libsvm/LibSVM.html (Wrapper of LibSVM)
 * 4. �����е�Gain/Loss���ݻ�����ͼ�������ǳ���Ȥ������������ȫ�Գƣ��ԳƵ���1%��Gain�ط���
 * �����������������Ʊ�������ǵġ�����ֵ��ע�������Gain����100%�����Ҳ�в��٣����Ǻ�����
 * Loss����40%�����������Ҳ���˵����������Ʊ�ǵıȽ϶ࡣ��Ҫ��һ�������������08-09���µ��������
 * ���⣬��˵�����ǵ�training data�Ƿǳ�imbalanced�ģ��󲿷����ݼ�����Gain��-10%-10%֮�䣬
 * ���Կ�����ȡ����ʱ��ƽ��һ�£�Ҳ���Կ��ǵ����������ο����
 * http://stackoverflow.com/questions/18078084/how-should-i-teach-machine-learning-algorithm-using-data-with-big-disproportion/18088148#18088148
 * http://www.csie.ntu.edu.tw/~cjlin/libsvm/faq.html#f410
 * @author jimmyzzxhlh-Dell
 * 5. �ǲ���Ӧ����Indicator�����ϼ�һЩ����Ȼ����ȥ��training�����ڿ�����̫��noise�ˣ�����˵emaDistance�ƺ�����<75��ʱ��ȽϺá�
 *
 * һ���ǳ��ǳ������Ĺ���support vector�Ľ��ܣ�
 * http://stackoverflow.com/questions/9480605/what-is-the-relation-between-the-number-of-support-vectors-and-training-data-and
 */

public class CommentWhiteBoard {

}
