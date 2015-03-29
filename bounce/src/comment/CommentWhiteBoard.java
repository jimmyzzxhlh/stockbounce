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
 * We discussed this but we cannot do anything on this because we don't seem to have the data for the before market / after market trading. The first data point may not be the after hour trading.
 * e.g. AAPL 20141218
 * 1418913059,111.6300,111.9000,111.4900,111.8700,2375700
 * The first timestamp is already 8:30:59 so it represents the volume at the first minute.
 * 
 * TODO (20150103):
 * Add ability to download index (Dow Jones, NASDAQ, S&P)
 * Notice that Yahoo does not allow daily Dow Jones index to be downloaded, but only allow intraday, so we need to download intraday data
 * as well.
 * Dow Jones symbol: ^DJI (%5EDJI)
 * Nasdaq symbol: ^IXIC (%5EIXIC)
 * S&P 500: ^GSPC (%5EGSPC)
 * But, there is no "outstanding shares" concept. So how do we compute the turnover rate?
 * Maybe we can use get the symbol lists of each index and sum up the outstanding shares? 
 * 
 * TODO (20150110):
 * 1. Read intraday data to replace the model.
 * 2. Signals - We only find one pattern of signal. Need to find more. Also if we start to buy, when do we sell?
 * 3. Add ability to automatically return stocks that have triggered the signal.   
 * 4. Draw chart.
 * 
 * TODO (20150114):
 * 1. To update daily data in a very fast speed, we need to use the following link to download the daily data.
 * http://download.finance.yahoo.com/d/quotes.csv?s=AAPL+GOOG&f=sohgpc1v
 * s : Symbol
 * o : Open
 * h : High
 * g : Low
 * p : Previous close
 * c1: Change (Close price is Previous close + Change. There doesn't seem to be way to directly download the close price)
 * v : Volume
 * This is the link that we download stock shares outstanding data in REST style, which is much faster since we can pass
 * multiple symbols in one request.
 * We need a batch to automatically update the daily data, BUT, we need to be very careful about stock splitting because that will
 * affect the volume! There is no adjacent close data in the above link. Probably there should be a manual update in this case.
 * Notice that it seems that the volume here is a more accurate volume than the volume from the CSV file itself.
 * 2. After updating the daily data, we also need to update the intraday data accordingly. The intraday data seems to have less volume
 * than it should, so we can just distribute those volume into every interval. We still cannot handle the after hour market volume, though.
 * 
 * TODO
 * 1. 10���߿�ʼ������ͷ���Ƕ�>=30%����ǰ���Ǻ�ƽ�ȵ����ƻ��������µ����ơ�
 * ƽ�ȵ����ƵĶ��������5��/10��/20�ܾ���֮����Ⱥ�С
 * ���µ�����TBD
 * 2. ���߳���2������Ӱ��Խ��Խ�ã�ÿ����ʵ���Ƿ�+5%��
 * 3. �����ź���10���߱�ĺ�ƽ��5��û���Ƿ�������б�����¡�
 * 
 * TODO (2015/3/28)
 * Use trailing stop to calculate performance of the model
 * Ideally: Trigger price = Highest bid price achieved - Trail amount (or Trail amount percentage)
 * With intraday data: update the "highest bid price" minute by minute; sell when the trigger price is obtained
 * Without intraday data:
 * (1) Rising market: open -> low -> high -> close
 * Order is triggered when (open - low) or (high - close) is larger than or equal to the trigger amount
 * (2) Losing market: open -> high -> low -> close
 * Order is triggered when (high - low) is larger than or equal to the trigger amount
 */

public class CommentWhiteBoard {

}

