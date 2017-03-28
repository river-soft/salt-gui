package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.client.SaltClient
import org.riversoft.salt.gui.datatypes.target.MinionList
import org.riversoft.salt.gui.datatypes.target.Target
import org.riversoft.salt.gui.results.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class SaltScriptFileService {

    @Value('${salt.scripts.directory}')
    private String scriptsDirectory

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Autowired
    private SaltClient saltClient

    /**
     * Создание sls файла скрипта
     * @param fileName - название файла
     * @param fileContent - содержимое файла скрипта
     * @return полный путь к файлу
     */
    String createSaltScriptSlsFile(String fileName, String fileContent) {

        Target<List<String>> minionList = new MinionList(["master"])

        Map<String, Result<Boolean>> isDirExistResult = org.riversoft.salt.gui.calls.modules.File.directoryExists(scriptsDirectory).callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

        def isDirExist = isDirExistResult.find().value?.xor?.right()?.value

        if (!isDirExist) {

            log.warn("Directoty ${scriptsDirectory} is not exists, create.")

            org.riversoft.salt.gui.calls.modules.File.mkdir(scriptsDirectory).callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)
        }

        log.debug("Start creating script file with name [${fileName}].")

        //        org.riversoft.salt.gui.calls.modules.File.touch("${scriptsDirectory}/${fileName}.sls").callAsync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)
        def file = org.riversoft.salt.gui.calls.modules.File.write("${scriptsDirectory}/${fileName}.sls", fileContent).callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

//        String filePath = file.canonicalPath
//        log.debug("Successfully created script file with full path [${filePath}].")

        return file
    }

    /**
     * Удаление sls файла скрипта
     * @param filePath - полный путь к файлу
     */
    def deleteSaltScriptSlsFile(String fileName) {

        Target<List<String>> minionList = new MinionList(["master"])

        log.debug("Start deleting file [${"${scriptsDirectory}/${fileName}.sls"}].")

        Map<String, Result<Boolean>> isFileExistResult = org.riversoft.salt.gui.calls.modules.File.fileExists("${scriptsDirectory}/${fileName}.sls").callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

        def isFileExist = isFileExistResult.find().value?.xor?.right()?.value
        if (!isFileExist) {
            log.error("File [${"${scriptsDirectory}/${fileName}.sls"}] not found.")
            throw new FileNotFoundException("File [${"${scriptsDirectory}/${fileName}.sls"}] not found.")
        }

        Map<String, Result<Boolean>> file = org.riversoft.salt.gui.calls.modules.File.remove("${scriptsDirectory}/${fileName}.sls").callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

        def fileSuccessfullyDeleted = file.find().value?.xor?.right()?.value

        if (fileSuccessfullyDeleted) {
            log.debug("Successfully deleted file [${"${scriptsDirectory}/${fileName}.sls"}].")
        } else {
            log.error("File [${"${scriptsDirectory}/${fileName}.sls"}] not deleted.")
            throw new Exception("File [${"${scriptsDirectory}/${fileName}.sls"}] not deleted.")
        }

        return file
    }

//    /**
//     * Чтение sls файла скрипта
//     * @param filePath - полный путь к файлу
//     * @return содержимое файла в виде строки
//     */
//    static readSaltScriptSlsFile(String filePath) {
//
//        String fileContents = ""
//
//        log.debug("Start reading script file [${filePath}].")
//
//        new File(filePath).eachLine { fileContents += "${it}\n" }
//
//        //TODO FileNotFoundException выбрасывается еслил файл не найден
//
//        log.debug("End reading script file [${filePath}].")
//
//        return fileContents
//    }

//    /**
//     * Редактирование/обновление sls файла скрипта
//     * @param filePath - полный путь файла
//     * @param fileContent - содержимое файла
//     * @param newFileName - новое имя файла
//     * @return полный путь к обновленному файлу
//     */
//    String updateSaltScriptSlsFile(String filePath, String fileContent, String newFileName) {
//
//        File file = new File(filePath)
//
//        log.debug("Start updating file [${filePath}].")
//
//        file.write(fileContent)
//
//        String actualFilePath = file.canonicalPath
//
//        if (newFileName) {
//
//            String newFilePath = "${scriptsDirectory}/${newFileName}.sls"
//
//            log.debug("Start renaiming file [${filePath}] to [${newFilePath}].")
//
//            file.renameTo(newFilePath)
//
//            actualFilePath = new File(newFilePath).canonicalPath
//
//            log.debug("File renamed and new path is [${actualFilePath}].")
//        }
//
//        log.debug("File [${actualFilePath}] updated.")
//
//        return actualFilePath
//    }

}
