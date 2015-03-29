package comment;

/**
 * 这个地方作为一个白板来讨论
 * 2014/10/16
 * 1. 如何制定策略？
 * 可能的一个方法是在当天收盘之后，计算当天的指标，然后让这个系统去预测。
 * 如果觉得是进场信号，那么在第二天开盘之后进场，此后每一天都做同样的预测，直到出现
 * 反转的预测之后出场。
 * 问题在于如何知道预测是错误的？
 * 2. 一些依赖于EMA的指标，要剔除前面的一些数据，因为EMA一开始的时候是不准确的，
 * 直到过一段时间之后才会消除误差。
 * 3. 提高系统的准确度要做Grid Search来确定C和gamma的值，参见下面的这篇文章：
 * http://www.csie.ntu.edu.tw/~cjlin/papers/guide/guide.pdf
 * 可能需要一些其他的库来做，比如：
 * http://java-ml.sourceforge.net/api/0.1.6/libsvm/GridSearch.html
 * http://java-ml.sourceforge.net/api/0.1.6/libsvm/LibSVM.html (Wrapper of LibSVM)
 * 4. 把所有的Gain/Loss数据画了张图出来，非常有趣的是它几乎完全对称，对称点在1%的Gain地方，
 * 这表明在最近几年里股票算是上涨的。另外值得注意的是在Gain超过100%的情况也有不少，但是很少有
 * Loss超过40%的情况，这大概也间接说明最近几年股票涨的比较多。需要多一点的数据来看看08-09年下跌的情况。
 * 另外，这说明我们的training data是非常imbalanced的，大部分数据集中于Gain在-10%-10%之间，
 * 可以考虑在取样的时候平衡一下，也可以考虑调整参数，参考这里：
 * http://stackoverflow.com/questions/18078084/how-should-i-teach-machine-learning-algorithm-using-data-with-big-disproportion/18088148#18088148
 * http://www.csie.ntu.edu.tw/~cjlin/libsvm/faq.html#f410
 * @author jimmyzzxhlh-Dell
 * 5. 是不是应该在Indicator本身上加一些条件然后再去做training，现在可能有太多noise了，比如说emaDistance似乎明显<75的时候比较好。
 *
 * 一个非常非常基本的关于support vector的介绍：
 * http://stackoverflow.com/questions/9480605/what-is-the-relation-between-the-number-of-support-vectors-and-training-data-and
 * 
 * 
 * 关于新定义的指标：
 * 1. 函数f(x): 换手率和有多少day trading之间的关系，换手率越高的话那么做day trading的人会越多，换手率为x，f(x)为day trading的率
 * 注意换手率可能会超过100%（但是应该非常罕见），f(x)则不会超过100%。
 * 2. 函数g(d): 股票在某一天被抛掉的概率，是个非连续的函数
 * 这些函数应当满足如下关系：
 * 1. 平均下来做day trading的人大概有50%，因此，从网上得到的换手率可以除以2得到实际的换手率
 * c_rate=c_rate/2
 * 2. 平均的持有股票时间=1/(平均的换手率)
 * 3. g(d)在某一段时间内（比如40天）里的和可以认为等于100%，相当于在第0天买的股票会在第1天到第40天之内被全部抛完。
 * sigma(g(d))=1
 * 4. 平均的持有股票时间=sigma(g(d)*d)，相当于期望值。
 * 
 * 12/13/2014
 * 1. 如果当天涨幅>=1%，最高价基本都出现在最低价之后，概率为P(n>=m)。
 * 2. 最高价出现在第n分钟，最低价出现在第m分钟的情况下，如果当天涨幅>=1%，股价的确定方式为：
 *    开盘价->第m分钟最低价->第n分钟最高价->收盘价
 *    概率为P(第m分钟最低价)*P(第n分钟最高价)
 *    假设n>=m（是否需要除以P(n>=m)），研究一下n<m是怎么回事
 *    
 *    可是这里有一个假设是最低价和最高价的出现时间是互相独立的，但实际不可能，因为第n分钟出现最低价但第n+1分钟出现最高价的情况几乎
 *    没有。
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
 * 1. 10周线开始向上翘头（角度>=30%），前面是很平稳的走势或者是向下的趋势。
 * 平稳的走势的定义可以是5周/10周/20周均线之差幅度很小
 * 向下的趋势TBD
 * 2. 周线出现2连阳，影线越短越好，每根线实体涨幅+5%。
 * 3. 出场信号是10周线变的很平（5周没有涨幅）或者斜率向下。
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

