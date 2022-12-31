import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String rootDirectory = JOptionPane.showInputDialog("Enter the root directory of your project:");
        String[] fileExtensions = JOptionPane.showInputDialog("Enter the file extensions for the files you want to count the lines of, seperated by a comma \n (e.g. java, py, cpp):").replaceAll(" ", "").split(",");

        ArrayList<File> files = getAllFilesFromDirectory(new File(rootDirectory));

        long linesOfCode = 0;
        long blankLines = 0;

        for(File file : files){
            String fileName = file.getName();
            if(fileName.contains(".")){
                String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
                for(String allowedExtension : fileExtensions){
                    if(allowedExtension.equals(fileExtension)){
                        List<String> lines = Files.readAllLines(file.toPath());

                        for(String line : lines){
                            if(line.isBlank()){
                                blankLines++;
                            } else{
                                linesOfCode++;
                            }
                        }
                    }
                }
            }
        }

        long totalLines = linesOfCode + blankLines;
        JOptionPane.showMessageDialog(null, "Your project contains:\n\n    - "
                                        + linesOfCode + " lines of source code\n    - "
                                        + blankLines + " blank lines\n\nWhich makes a total of:\n\n    - "
                                        + totalLines + " lines");
    }

    private static ArrayList<File> getAllFilesFromDirectory(File directory) {
        ArrayList<File> files = new ArrayList<>();
        if (!directory.isDirectory()){
            files.add(directory);
            return files;
        }

        try {
            Files.walkFileTree(Path.of(directory.getAbsolutePath()), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    files.add(path.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path path, IOException e) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files;
    }
}