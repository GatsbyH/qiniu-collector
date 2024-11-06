package com.sdwu.infrastructure.persistent.redis;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.sdwu.infrastructure.persistent.po.DeveloperPO;
import org.redisson.api.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis 服务 - Redisson
 *
 * @author gatsbyh
 */
@Service("redissonService")
public class RedissonService implements IRedisService {

    @Resource
    private RedissonClient redissonClient;

    public <T> void setValue(String key, T value) {
        redissonClient.<T>getBucket(key).set(value);
    }

    @Override
    public <T> void setValue(String key, T value, long expired) {
        RBucket<T> bucket = redissonClient.getBucket(key);
//        bucket.set(value, Duration.ofMillis(expired));
        bucket.set(value);
        bucket.expire(Duration.ofMillis(expired));
    }

    public <T> T getValue(String key) {
        return redissonClient.<T>getBucket(key).get();
    }

    @Override
    public <T> RQueue<T> getQueue(String key) {
        return redissonClient.getQueue(key);
    }

    @Override
    public <T> RBlockingQueue<T> getBlockingQueue(String key) {
        return redissonClient.getBlockingQueue(key);
    }

    @Override
    public <T> RDelayedQueue<T> getDelayedQueue(RBlockingQueue<T> rBlockingQueue) {
        return redissonClient.getDelayedQueue(rBlockingQueue);
    }

    @Override
    public void setAtomicLong(String key, long value) {
        redissonClient.getAtomicLong(key).set(value);
    }

    @Override
    public Long getAtomicLong(String key) {
        return redissonClient.getAtomicLong(key).get();
    }

    @Override
    public long incr(String key) {
        return redissonClient.getAtomicLong(key).incrementAndGet();
    }

    @Override
    public long incrBy(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(delta);
    }

    @Override
    public long decr(String key) {
        return redissonClient.getAtomicLong(key).decrementAndGet();
    }

    @Override
    public long decrBy(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(-delta);
    }

    @Override
    public void remove(String key) {
        redissonClient.getBucket(key).delete();
    }

    @Override
    public boolean isExists(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    public void addToSet(String key, String value) {
        RSet<String> set = redissonClient.getSet(key);
        set.add(value);
    }

    public boolean isSetMember(String key, String value) {
        RSet<String> set = redissonClient.getSet(key);
        return set.contains(value);
    }

    public void addToList(String key, String value) {
        RList<String> list = redissonClient.getList(key);
        list.add(value);
    }

    public String getFromList(String key, int index) {
        RList<String> list = redissonClient.getList(key);
        return list.get(index);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String key) {
        return redissonClient.getMap(key);
    }

    public void addToMap(String key, String field, String value) {
        RMap<String, String> map = redissonClient.getMap(key);
        map.put(field, value);
    }

    public String getFromMap(String key, String field) {
        RMap<String, String> map = redissonClient.getMap(key);
        return map.get(field);
    }

    @Override
    public <K, V> V getFromMap(String key, K field) {
        return redissonClient.<K, V>getMap(key).get(field);
    }

    public void addToSortedSet(String key, String value) {
        RSortedSet<String> sortedSet = redissonClient.getSortedSet(key);
        sortedSet.add(value);
    }
    @Override
    public void addUser(String key, DeveloperPO user, double talentRank) {
        RScoredSortedSet<DeveloperPO> sortedSet = redissonClient.getScoredSortedSet(key);
        sortedSet.add(talentRank, user);
    }
    @Override
    public  List<DeveloperPO> getUsersByRankDesc(String key, int pageNumber, int pageSize) {
        RScoredSortedSet<DeveloperPO> sortedSet = redissonClient.getScoredSortedSet(key);
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = startIndex + pageSize - 1;

        Collection<DeveloperPO> collection = sortedSet.valueRangeReversed(startIndex, endIndex);

        List<DeveloperPO> users = ListUtil.toList(collection);
        return users;
    }

    @Override
    public void removeUser(String key, DeveloperPO user) {
        RScoredSortedSet<DeveloperPO> sortedSet = redissonClient.getScoredSortedSet(key);
        sortedSet.remove(user);
    }


    @Override
    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    @Override
    public RLock getFairLock(String key) {
        return redissonClient.getFairLock(key);
    }

    @Override
    public RReadWriteLock getReadWriteLock(String key) {
        return redissonClient.getReadWriteLock(key);
    }

    @Override
    public RSemaphore getSemaphore(String key) {
        return redissonClient.getSemaphore(key);
    }

    @Override
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String key) {
        return redissonClient.getPermitExpirableSemaphore(key);
    }

    @Override
    public RCountDownLatch getCountDownLatch(String key) {
        return redissonClient.getCountDownLatch(key);
    }

    @Override
    public <T> RBloomFilter<T> getBloomFilter(String key) {
        return redissonClient.getBloomFilter(key);
    }

    @Override
    public Boolean setNx(String key) {
        return redissonClient.getBucket(key).trySet("lock");
    }

    @Override
    public Boolean setNx(String key, long expired, TimeUnit timeUnit) {
        return redissonClient.getBucket(key).trySet("lock", expired, timeUnit);
    }

    @Override
    public Integer getUsersByRankSize(String key) {
        RScoredSortedSet<DeveloperPO> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.size();
    }

    @Override
    public List<String> getDeveloperNationOptionsByField(String key) {
        // 1. 获取数据
        RScoredSortedSet<DeveloperPO> sortedSet = redissonClient.getScoredSortedSet(key);
        if (sortedSet == null || sortedSet.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 提取、过滤并排序所有合法的国家名称
        List<String> nationList = sortedSet.readAll().stream()
                .map(DeveloperPO::getNation)
                .filter(this::isValidNationName)  // 过滤有效的国家名称
                .distinct()                       // 去重
                .sorted()                         // 按字母顺序排序
                .collect(Collectors.toList());

        return nationList;
    }

    @Override
    public List<DeveloperPO> getUsersByRankDescAndNation(String key, String field, String nation, Integer pageNum, Integer pageSize) {
        // 获取Redisson的排序集合
        RScoredSortedSet<DeveloperPO> sortedSet = redissonClient.getScoredSortedSet(key);

        // 获取所有数据
//        Collection<DeveloperPO> allData = sortedSet.readAll();
        Collection<DeveloperPO> allData = sortedSet.valueRangeReversed(0, 1500);

        // 对所有开发者进行国籍模糊筛选
        List<DeveloperPO> filteredUsers = allData.stream()
                .filter(developer -> {
                    String developerNation = developer.getNation();
                    return developerNation != null && developerNation.contains(nation); // 模糊匹配国籍
                })
                .collect(Collectors.toList());

        // 分页处理
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, filteredUsers.size());

        // 返回分页数据
        return filteredUsers.subList(startIndex, endIndex);
    }

    @Override
    public Integer getUsersSizeByRankAndNation(String key, String field, String nation) {
        // 获取Redisson的排序集合
        RScoredSortedSet<DeveloperPO> sortedSet = redissonClient.getScoredSortedSet(key);

        // 获取所有开发者数据
        Collection<DeveloperPO> allData = sortedSet.readAll();

        // 进行模糊筛选：过滤符合条件的开发者
        long filteredCount = allData.stream()
                .filter(developer -> {
                    String developerNation = developer.getNation();
                    return developerNation != null && developerNation.contains(nation); // 模糊匹配国籍
                })
                .count();

        // 返回符合条件的开发者数量
        return (int) filteredCount;
    }


    private boolean matchNation(String nation, String keyword) {
        if (nation == null || keyword == null) {
            return false;
        }

        // 转换为小写并移除多余空格
        nation = nation.toLowerCase().trim();
        keyword = keyword.toLowerCase().trim();

        // 分词处理（如果需要）
        String[] nationWords = nation.split("\\s+");
        String[] keywordWords = keyword.split("\\s+");

        // 任意词匹配
        for (String keywordWord : keywordWords) {
            boolean matched = false;
            for (String nationWord : nationWords) {
                if (nationWord.contains(keywordWord)) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }

        return true;
    }
    /**
     * 判断国家名称是否合法
     * @param nation 国家名称
     * @return 是否合法
     */
    private boolean isValidNationName(String nation) {
        if (nation == null || nation.trim().isEmpty()) {
            return false;
        }

        // 去除首尾空格
        nation = nation.trim();

        // 检查长度限制
        if (nation.length() > 15 || nation.length() < 2) {
            return false;
        }

        // 统计英文单词数和中文字符数
        int englishWordCount = 0;
        int chineseCharCount = 0;
        boolean inWord = false;

        for (int i = 0; i < nation.length(); i++) {
            char c = nation.charAt(i);

            if (Character.isWhitespace(c)) {
                if (inWord) {
                    englishWordCount++;
                    inWord = false;
                }
            } else if (isChineseChar(c)) {
                chineseCharCount++;
                if (inWord) {
                    englishWordCount++;
                    inWord = false;
                }
            } else if (Character.isLetter(c)) {
                inWord = true;
            } else {
                // 包含其他字符（如数字或特殊符号）则视为无效
                return false;
            }
        }

        // 处理最后一个英文单词
        if (inWord) {
            englishWordCount++;
        }

        // 判断条件：
        // 1. 纯英文：不超过3个单词
        // 2. 纯中文：不超过4个字
        // 3. 不允许中英混合
        if (chineseCharCount > 0 && englishWordCount > 0) {
            return false;
        }

        return (chineseCharCount > 0 && chineseCharCount <= 4) ||
                (englishWordCount > 0 && englishWordCount <= 3);
    }

    /**
     * 判断是否为中文字符
     * @param c 字符
     * @return 是否为中文
     */
    private boolean isChineseChar(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A;
    }
}
