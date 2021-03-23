package framework;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ScreenShooter {

    // делаем скриншот с помощью библиотеки AShot - на выходе получаем объект типа Screenshot
    private static Screenshot shot() {
        return new AShot().shootingStrategy(ShootingStrategies.simple()).takeScreenshot(DriverFactory.getDriver());
    }

    // конвертируем скриншот в BufferedImage (нужно будет для метода ниже (для копирования файла с одного места в другое)
    private static BufferedImage convertScreenShotToBufferedImage(Screenshot screenshot) {
        return screenshot.getImage();
    }

    // копируем скриншот с tmp фолдера в файл с путем из параметров
    private static File copyFromTempToDestinationFolder(Screenshot originalFileInTempFolder, String pathWhereCopiedFileWillStores) throws IOException {
        // делаем из объекта Screenshot -> BufferedImage
        BufferedImage bufferedImage = convertScreenShotToBufferedImage(originalFileInTempFolder);
        // создаем новый файл куда будем добавлять наш скриншот
        File outputFile = new File(System.currentTimeMillis() + ".png");
        // записываем обьект BufferedImage в формате PNG в файл, который создали выше
        ImageIO.write(bufferedImage, "PNG", outputFile);
        // создаем новый файл, в который будем складывать уже скриншот после копирования с путем с параметров (target folder)
        File screenInProperlyPath = new File(pathWhereCopiedFileWillStores);
        // копируем старый файл из tmp фолдера в новый, который создали выше
        FileUtils.copyFile(outputFile, screenInProperlyPath);
        // удаляем старый файл
        outputFile.deleteOnExit();
        return screenInProperlyPath;
    }

    // прикрепляем скриншот в allure
    private static void attachScreenShotToAllure(String name, File file) {
        try {
            //конвертируем файл в InputStream потому что метод addAttachment работает только с ним
            InputStream fileInputStream = new FileInputStream(file);
            Allure.addAttachment(name, fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // финальный метод, который будет вызываться тогда, когда нужно сделать скриншот
    public static void takeScreenShot(String name) {
        String destinationPath = System.getProperty("user.dir") + "/target/screenShots/" + name + ".png";
        try {
            Screenshot shot = shot();
            File screenShot = copyFromTempToDestinationFolder(shot, destinationPath);
            attachScreenShotToAllure(name, screenShot);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
