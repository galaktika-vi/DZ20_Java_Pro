import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreatePoemFile {
    public static void main(String[] args) {

        String desktopPath = System.getProperty("user.home") + "/Desktop";
        String fileName = "PushkinPoem.txt";


        String poem = "У лукоморья дуб зелёный;\n" +
                "Златая цепь на дубе том:\n" +
                "И днём и ночью кот учёный\n" +
                "Всё ходит по цепи кругом;\n" +
                "Идёт направо - песнь заводит,\n" +
                "Налево - сказку говорит.\n";


        File file = new File(desktopPath, fileName);

        try (FileWriter writer = new FileWriter(file)) {
             
            writer.write(poem);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Poem written to " + file.getAbsolutePath());
    }
}
