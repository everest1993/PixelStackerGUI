package it.luigivannozzi.pixelstacker.model;

import it.luigivannozzi.pixelstacker.controller.ExecController;
import it.luigivannozzi.pixelstacker.utils.Config;
import it.luigivannozzi.pixelstacker.utils.Router;
import it.luigivannozzi.pixelstacker.view.pages.HomePageCreator;
import javafx.util.Duration;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

// classe responsabile dell'invocazione del process builder per chiamare gli script di stacking CLI
public class ScriptRunner {

    private final ExecController controller;
    private final List<String> cmd;

    public ScriptRunner(List<String> cmd) {
        controller = ExecController.getInstance();
        this.cmd = cmd;
    }

    // metodo per avviare lo script tramite process builder
    // recupera i messaggi e i log dello script attraverso InputStreamReader per aggiornare la progress bar
    // nella pagina di esecuzione
    public void runStacking() throws IOException, InterruptedException {

        controller.updateProgress(0.0);
        controller.stepThenPrefill(0.0, Duration.ZERO, 0.33, Duration.seconds(20));

        // crea il processo contenente il comando
        ProcessBuilder pb = new ProcessBuilder(cmd);

        // imposta la working dir solo se l'eseguibile ha un percorso esplicito
        File first = new File(cmd.getFirst());
        File parent = first.getParentFile();
        if (parent != null) {
            pb.directory(parent);
        }

        // merge stderr (errori e warnings) in stdout (output standard)
        pb.redirectErrorStream(true);

        Process p = pb.start();

        // p.getInputStream() ritorna lo stdout del processo Python (flusso di byte).
        // InputStreamReader() converte i byte in caratteri secondo la codifica di default (sistema)
        // BufferedReader() avvolge l’InputStreamReader per leggere il testo in modo efficiente con readLine()
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("[PYTHON] " + line);

                if(line.equalsIgnoreCase("STEP 1/3")) {
                    controller.stepThenPrefill(1.0 / 3.0, Duration.millis(2000),
                            0.6, Duration.seconds(20));
                }
                if(line.equalsIgnoreCase("STEP 2/3")) {
                    controller.stepThenPrefill(2.0 / 3.0, Duration.millis(2000),
                            0.99, Duration.seconds(30));
                }
                if (line.equalsIgnoreCase("STEP 3/3")) {
                    controller.finishAndNavigateHome(javafx.util.Duration.seconds(1),
                            () -> Router.getInstance().navigate(new HomePageCreator().createPage()));
                }
            }
        }

        // timeout di sicurezza > tempo di esecuzione su hardware di riferimento * 2 (documentazione)
        boolean finished = p.waitFor(5, TimeUnit.MINUTES);
        if (!finished) {
            // termina forzatamente il processo
            p.destroyForcibly();
            throw new RuntimeException("Stacking scaduto per timeout.");
        }

        int code = p.exitValue();
        if (code != 0) {
            throw new RuntimeException("Stacking fallito. Exit code=" + code + "\n");
        }
    }

    // genera un nome file/path univoco in modo da non sovrascrivere eventuali omonimi
    public static String getOutputPathAndName() {
        File outDir = Config.getInstance().getOutputDir();
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return outDir.toPath().resolve("output_" + ts + ".tif").toString();
    }

    public static String getExecutable() {
        String osName  = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        boolean isWin  = osName.contains("win");
        boolean isMac  = osName.contains("mac") || osName.contains("darwin");
        String osSubdir = isWin ? "win" : (isMac ? "mac" : "linux");

        String bundledName = isWin ? "pixelstacker.exe" : "pixelstacker";

        try {
            java.net.URL loc = ScriptRunner.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation();

            File jarOrDir;
            jarOrDir = new File(loc.toURI()).getAbsoluteFile();
            File contents = jarOrDir;

            for (int i = 0; i < 5 && contents != null; i++) {
                if ("Contents".equals(contents.getName())) break;
                contents = contents.getParentFile();
            }

            if (contents != null && "Contents".equals(contents.getName())) {
                File inBundle = new File(
                        new File(new File(contents, "cli-bin"), osSubdir),
                        bundledName
                );
                if (inBundle.exists()) {
                    if (!isWin) {
                        boolean ignored = inBundle.setExecutable(true, false);
                    }
                    System.out.println("[CLI] Using bundled executable: " + inBundle.getAbsolutePath());
                    return inBundle.getAbsolutePath();
                }
            }
        } catch (Exception ignored) {
        }

        java.nio.file.Path cliRoot;
        String envRoot = System.getenv("PIXELSTACKER_CLI_ROOT");
        if (envRoot != null && !envRoot.isBlank()) {
            cliRoot = java.nio.file.Paths.get(envRoot);
        } else {
            java.nio.file.Path guiDir = java.nio.file.Paths.get(System.getProperty("user.dir"));
            java.nio.file.Path parent = (guiDir.getParent() != null ? guiDir.getParent() : guiDir);
            cliRoot = parent.resolve("PixelStackerCLI");
        }

        String cliName = isWin ? "pixelstacker-cli.exe" : "pixelstacker-cli";
        java.nio.file.Path cliExe = isWin
                ? cliRoot.resolve(".venv").resolve("Scripts").resolve(cliName)
                : cliRoot.resolve(".venv").resolve("bin").resolve(cliName);

        File cliFile = cliExe.toFile();
        if (cliFile.exists() && (isWin || cliFile.canExecute())) {
            System.out.println("[CLI] Using dev venv executable: " + cliFile.getAbsolutePath());
            return cliFile.getAbsolutePath();
        }

        String fallback;
        fallback = cliName;
        System.out.println("[CLI] Using executable from PATH: " + fallback);
        return fallback;
    }
}