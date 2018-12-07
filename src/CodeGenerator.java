import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class CodeGenerator {
    public void generateCode(String outPath, String textPath) throws IOException, InterruptedException {
        ProcessBuilder proc = new ProcessBuilder("python", "src/generator/generator.py", outPath, textPath);
        log(proc);
    }

    public void runCode(String path) throws IOException, InterruptedException {
        ProcessBuilder proc = new ProcessBuilder("python", path);
        log(proc);
    }

    public void log(ProcessBuilder ps) throws IOException, InterruptedException {
        ps.redirectErrorStream(true);

        Process pr = ps.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

        String line;
        System.out.println("\n\nCode: output:");
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }

        pr.waitFor();
        in.close();
    }


}