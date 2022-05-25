import org.junit.Test;
import top.guoshouzhen.socketserver.infrastructure.SnowFlake;

import java.util.HashSet;
import java.util.Set;

/**
 * @author shouzhen.guo
 * @version V1.0
 * @description
 * @date 2022/5/22 12:10
 */

public class TestSonwFlake {
    @Test
    public void testSnowId(){
        SnowFlake snowFlake = new SnowFlake(0L,0L);
        int n = 100;
        Set<Long> st = new HashSet<>(n);

        for(int i = 0; i < n; i++){
            long id = snowFlake.getNextId();
            System.out.println(id);
            st.add(id);
        }

        System.out.println("生成：" + n + "个，重复：" + (n - st.size()) + "个");
    }
}
