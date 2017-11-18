package edw.util;

import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CmdUtils {
    private static final Logger logger = Logger.getLogger(CmdUtils.class.getName());
    private static ExecutorService executor = Executors.newFixedThreadPool(5);
    private static Runnable createReaderCallable(final InputStream is, final StringBuilder sb,
                                                 int maxResultLen, final String errorMsg) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                final BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is)
                );
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (sb.length() + line.length() >= maxResultLen) {
                            break;
                        }
                        sb.append(line).append("\n");
                    }
                } catch (IOException e) {
                    logger.log(Level.WARNING, errorMsg, e);
                }
            }
        };
        return runnable;
    }

    public static String[] executeBash(String[] command, String dir, Map<String,String> env, int maxResultLen)
    {
        ProcessBuilder pb = new ProcessBuilder(command);
        if (dir != null) {
            pb.directory(new File(dir));
        }
        if (env != null) {
            Map<String, String> currEnv = pb.environment();
            for (Map.Entry<String, String> entry: env.entrySet()) {
                currEnv.put(entry.getKey(), entry.getValue());
            }
        }

        String commandStr = Joiner.on(", ").join(command);
        try {
            Process p = pb.start();
            final StringBuilder inputSb = new StringBuilder();
            Runnable inputRunnable= createReaderCallable(p.getInputStream(), inputSb, maxResultLen,
                    "Error reading output of command: " + commandStr);
            Thread inputThread = new Thread(inputRunnable);
            final StringBuilder errorSb = new StringBuilder();
            Runnable errorRunnable = createReaderCallable(p.getErrorStream(), errorSb, maxResultLen,
                    "Error reading error of command: " + commandStr);
            Thread errorThread = new Thread(inputRunnable);
            inputThread.start();
            errorThread.start();
            int exitCode = p.waitFor();
            inputThread.join();
            errorThread.join();
            return new String[]{String.valueOf(exitCode), inputSb.toString(), errorSb.toString()};

        } catch (IOException e) {
            logger.log(Level.WARNING, "Error executing command: " + commandStr, e);
            return new String[]{"-1", null, e.getMessage()};
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Error executing command: " + commandStr, e);
            return new String[]{"-1", null, e.getMessage()};
        }
    }
}
