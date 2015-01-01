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
 * 
 * 
 * �����¶����ָ�꣺
 * 1. ����f(x): �����ʺ��ж���day trading֮��Ĺ�ϵ��������Խ�ߵĻ���ô��day trading���˻�Խ�࣬������Ϊx��f(x)Ϊday trading����
 * ע�⻻���ʿ��ܻᳬ��100%������Ӧ�÷ǳ���������f(x)�򲻻ᳬ��100%��
 * 2. ����g(d): ��Ʊ��ĳһ�챻�׵��ĸ��ʣ��Ǹ��������ĺ���
 * ��Щ����Ӧ���������¹�ϵ��
 * 1. ƽ��������day trading���˴����50%����ˣ������ϵõ��Ļ����ʿ��Գ���2�õ�ʵ�ʵĻ�����
 * c_rate=c_rate/2
 * 2. ƽ���ĳ��й�Ʊʱ��=1/(ƽ���Ļ�����)
 * 3. g(d)��ĳһ��ʱ���ڣ�����40�죩��ĺͿ�����Ϊ����100%���൱���ڵ�0����Ĺ�Ʊ���ڵ�1�쵽��40��֮�ڱ�ȫ�����ꡣ
 * sigma(g(d))=1
 * 4. ƽ���ĳ��й�Ʊʱ��=sigma(g(d)*d)���൱������ֵ��
 * 
 * 12/13/2014
 * 1. ��������Ƿ�>=1%����߼ۻ�������������ͼ�֮�󣬸���ΪP(n>=m)��
 * 2. ��߼۳����ڵ�n���ӣ���ͼ۳����ڵ�m���ӵ�����£���������Ƿ�>=1%���ɼ۵�ȷ����ʽΪ��
 *    ���̼�->��m������ͼ�->��n������߼�->���̼�
 *    ����ΪP(��m������ͼ�)*P(��n������߼�)
 *    ����n>=m���Ƿ���Ҫ����P(n>=m)�����о�һ��n<m����ô����
 *    
 *    ����������һ����������ͼۺ���߼۵ĳ���ʱ���ǻ�������ģ���ʵ�ʲ����ܣ���Ϊ��n���ӳ�����ͼ۵���n+1���ӳ�����߼۵��������
 *    û�С�
 *    
 * 12/28/2014
 * Dongyue has his operating system's locale set to Japanese so he cannot read Chinese :(
 * TODO (Useful and Optional):
 * Enhancement for estimating the price/volume relationship: (Notice that this is not useful once we have
 * sufficient intraday data)
 * P(m,n) - The probability that low appears at m minute and high appears at n minute.
 * For long white, we have the pattern:
 * Open -> Low -> High -> Close
 * For each interval t that 0 <= t <= m, we have:
 * Price(m,n,t) = t / m * Low + (1 - t / m) * Open
 * (When t = m we have Price(m,n,m) = Low. When t = 0 we have Price(m,n,0) = Open)
 * Similarly, we can get the formula for t > m and also for other patterns such as long black (Open -> High -> Low -> Close).
 * For each formula:
 * P-Low(m,n,t) = The coefficient for Low.
 * P-High(m,n,t) = The coefficient for high.
 * P-Close(m,n,t)
 * P-Open(m,n,t)
 * We can build a hashmap with a key of (P-Low(m,n), P-High(m,n), P-Close(m,n), P-Open(m,n)). The value is the percentage of volume
 * given m,n (i.e. We know at which interval high/low appear, then we will loop through t and add up the volume percentage for the key).
 * Hashmap(P-Low(m,n), P-High(m,n), P-Close(m,n), P-Open(m,n)) = Sigma(Volume%(t)) * P(m,n)
 * Notice that we can normalize the keys so that they have a range of [-100,100]
 * Then, given a real stock candle with Low, High, Close, Open, we just need to do a dot product with the hash map and we can
 * get a price <-> volume percentage mapping. 
 *  
 * TODO (Important):
 * We haven't handled after hour trading appropriately. For now we can assume that interval 0 represents the after hour trading between
 * the close time of the previous day and the open time of the current day.
 * If there is a gap between the candles, then we need to distribute the price / volume of the interval 0 to the gap.
 * 
 *  
 */

public class CommentWhiteBoard {

}

