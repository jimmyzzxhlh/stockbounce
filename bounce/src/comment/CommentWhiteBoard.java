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
 * 
 */

public class CommentWhiteBoard {

}

