package reflection;

import data_mapping.Blog;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import static junit.framework.TestCase.fail;

public class ReflectionPerf {
    @Test
    public void noReflectionSettingPerf() throws InterruptedException {
        Blog blog = new Blog();
        CountDownLatch latch = new CountDownLatch(2);
        int callTimes = 100_000_000;
        new Thread(() -> {
            long start = System.nanoTime();
            for (int i = 0; i < callTimes; i++) {
                //blog.setTitle("title-" + i);
            }
            System.out.println("no reflection:" + (System.nanoTime() - start));

            latch.countDown();
        }).start();

        new Thread(() -> {
            Method setTitleMethod = null;
            try {
                setTitleMethod =
                        blog.getClass().getMethod("setTitle", String.class);
                long start = System.nanoTime();
                for (int i = 0; i < callTimes; i++) {
                    setTitleMethod.invoke(blog, "title-" + i);
                }
                System.out.println("   reflection:" + (System.nanoTime() - start));
            } catch (Exception e) {
                fail();
            }

            latch.countDown();
        }).start();

        latch.await();
    }
}
