package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class SaltScriptFileService {

    @Value('${salt.scripts.directory}')
    private String scriptsDirectory

    /**
     * Чтение sls файла скрипта
     * @param filePath - полный путь к файлу
     * @return содержимое файла в виде строки
     */
    static readSaltScriptSlsFile(String filePath) {

        String fileContents = ""

        log.debug("Start reading script file [${filePath}].")

        new File(filePath).eachLine { fileContents += "${it}\n" }

        //TODO FileNotFoundException выбрасывается еслил файл не найден

        log.debug("End reading script file [${filePath}].")

        return fileContents
    }

    /**
     * Создание sls файла скрипта
     * @param fileName - название файла
     * @param fileContent - содержимое файла скрипта
     * @return полный путь к файлу
     */
    static createSaltScriptSlsFile(String fileName, String fileContent) {

        def dir = new File("${scriptsDirectory}")

        if (!dir.exists()) {
            log.warn("Directoty ${dir.canonicalPath} is not exists, create.")
            dir.mkdirs()
        }

        //TODO проверять существует ли файл с таким именем?

        log.debug("Start creating script file with name [${fileName}].")

        File file = new File("${scriptsDirectory}/${fileName}.sls")

        String filePath = file.canonicalPath

        file.write(fileContent)

        log.debug("Successfully created script file with full path [${filePath}].")

        return filePath
    }

    /**
     * Редактирование/обновление sls файла скрипта
     * @param filePath - полный путь файла
     * @param fileContent - содержимое файла
     * @param newFileName - новое имя файла
     * @return полный путь к обновленному файлу
     */
    static String updateSaltScriptSlsFile(String filePath, String fileContent, String newFileName) {

        File file = new File(filePath)

        log.debug("Start updating file [${filePath}].")

        file.write(fileContent)

        String actualFilePath = file.canonicalPath

        if (newFileName) {

            String newFilePath = "${scriptsDirectory}/${newFileName}.sls"

            log.debug("Start renaiming file [${filePath}] to [${newFilePath}].")

            file.renameTo(newFilePath)

            actualFilePath = new File(newFilePath).canonicalPath

            log.debug("File renamed and new path is [${actualFilePath}].")
        }

        log.debug("File [${actualFilePath}] updated.")

        return actualFilePath
    }

    /**
     * Удаление sls файла скрипта
     * @param filePath - полный путь к файлу
     */
    static boolean deleteSaltScriptSlsFile(String filePath) {

        log.debug("Start deleting file [${filePath}].")

        File file = new File(filePath)

        if (!file) {
            log.error("File [${filePath}] not found.")
            throw new FileNotFoundException("File [${filePath}] not found.")
        }

        boolean fileSuccessfullyDeleted = file.delete()

        if (fileSuccessfullyDeleted) {
            log.debug("Successfully deleted file [${filePath}].")
        } else {
            log.error("File [${filePath}] not deleted.")
            throw new Exception("File [${filePath}] not deleted.")
        }

        return fileSuccessfullyDeleted
    }
}
