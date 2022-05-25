package top.guoshouzhen.socketserver.infrastructure;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description 雪花算法
 * @date 2022/5/22 11:14
 */
public class SnowFlake {
    /**
     * 定义基准时间戳
     */
    private static final long START_TIMESTAMP = 1653189368000L;

    /**
     * 机器id所占用位数
     */
    private static final long MACHINE_BIT = 5L;

    /**
     * 数据中心id所占用位数
     */
    private static final long DATACENTER_BIT = 5L;

    /**
     * 序列号所占用位数
     */
    private static final long SEQUENCE_BIT = 12L;

    /**
     * 机器id最大值（将-1左移，在与-1异或）补码运算，效率高
     */
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_BIT);

    /**
     * 数据中心最大值
     */
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_BIT);

    /**
     * 序列号最大值
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 各部分位移数
     */
    private static final long DATACENTER_LEFT = SEQUENCE_BIT;
    private static final long MACHINE_LEFT = SEQUENCE_BIT + DATACENTER_BIT;
    private static final long TIMESTAMP_LEFT = SEQUENCE_BIT + DATACENTER_BIT + MACHINE_BIT;

    /**
     * 机器id
     */
    private final long lMachineId;

    /**
     * 数据中心id
     */
    private final long lDataCenterId;

    /**
     * 序列号
     */
    private long lSequence = 0L;

    /**
     * 上一次使用的时间戳
     */
    private long lLastTimestamp = -1L;

    /**
     * 构造器
     * @author shouzhen.guo
     * @date 2022/5/22 11:39
     * @param lMachineId 机器id
     * @param lDataCenterId 数据中心id
     */
    public SnowFlake(long lMachineId, long lDataCenterId){
        if(lMachineId > MAX_MACHINE_ID || lMachineId < 0){
            throw new IllegalArgumentException(String.format("指定的机器id不在有效范围内：[0,%d]", MAX_MACHINE_ID));
        }
        if(lDataCenterId > MAX_DATACENTER_ID || lDataCenterId < 0){
            throw new IllegalArgumentException(String.format("指定的数据中心id不在有效范围内：[0,%d]", MAX_DATACENTER_ID));
        }
        this.lMachineId = lMachineId;
        this.lDataCenterId = lDataCenterId;
    }

    /**
     * 生成下一个id
     * @author shouzhen.guo
     * @date 2022/5/22 11:40
     * @return long
     */
    public synchronized long getNextId(){
        long lCurrTimestamp = getNewTimestamp();
        if(lCurrTimestamp < lLastTimestamp){
            throw new RuntimeException("系统时钟回拨，拒绝生成ID");
        }
        if(lCurrTimestamp == lLastTimestamp){
            //相同毫秒内，序列号自增（保证不会超出序列号最大值）
            lSequence = (lSequence + 1) & MAX_SEQUENCE;
            if(lSequence == 0L){
                //同一毫秒内，序列号用完了，就原地等待获取下一个大于最后一次使用时间戳的时间戳
                lCurrTimestamp = getNextTimestamp();
            }
        }else{
            //不同毫秒，序列号置为0
            lSequence = 0L;
        }
        lLastTimestamp = lCurrTimestamp;
        return (lCurrTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT | lMachineId << MACHINE_LEFT | lDataCenterId << DATACENTER_LEFT | lSequence;
    }

    /**
     * 获取系统当前时间戳
     * @author shouzhen.guo
     * @date 2022/5/22 11:41
     * @return long
     */
    private long getNewTimestamp(){
        return System.currentTimeMillis();
    }

    /**
     * 原地等待，获取下一个大于最后一次使用时间戳的时间戳
     * @author shouzhen.guo
     * @date 2022/5/22 11:50
     * @return long
     */
    private long getNextTimestamp(){
        long tms = getNewTimestamp();
        while(tms <= lLastTimestamp){
            tms = getNewTimestamp();
        }
        return tms;
    }
}
