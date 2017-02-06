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
     * @param filePath - полный путь файла
     * @return содержимое файла
     */
    def readSaltScriptSlsFile(String filePath) {

        String fileContents = ""

        //TODO logs
        new File(filePath).eachLine { fileContents += "${it}\n" }

        //TODO FileNotFoundException выбрасывается еслил файл не найден

        return fileContents
    }

    /**
     * Создание sls файла скрипта
     * @param fileName - название файла
     * @param fileContent - содержимое файла скрипта
     * @return полный путь файла
     */
    def createSaltScriptSlsFile(String fileName, String fileContent) {

        def dir = new File("${scriptsDirectory}")

        if (!dir.exists()) {
            log.warn("Directoty ${dir.canonicalPath} is not exists, create.")
            dir.mkdirs()
        }

        //TODO проверять существует ли файл с таким именем?

        File file = new File("${scriptsDirectory}/${fileName}.sls")

        String filePath = file.canonicalPath

        file.write(fileContent)

        return filePath
    }

    /**
     * Редактирование/обновление sls файла скрипта
     * @param filePath - полный путь файла
     * @param fileContent - содержимое файла
     */
    def updateSaltScriptSlsFile(String filePath, String fileContent) {
        //TODO implement
    }

    /**
     * Удаление sls файла скрипта
     * @param filePath - полный путь файла
     */
    def deleteSaltScriptSlsFile(String filePath) {
        //TODO implement
    }
}
