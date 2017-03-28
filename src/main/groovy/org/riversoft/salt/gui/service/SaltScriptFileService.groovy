package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.AuthModule
import org.riversoft.salt.gui.calls.modules.File
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

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Value('${salt.scripts.directory}')
    private String scriptsDirectory

    @Value('${salt.master_minion.name}')
    private String masterMinionName

    @Autowired
    private SaltClient saltClient

    /**
     * Создание sls файла скрипта
     * @param fileName - название файла
     * @param file - полный путь к файлуContent - содержимое файла скрипта
     */
    void createSaltScriptSlsFile(String fileName, String fileContent) {

        log.debug("Start creating script file with name [${fileName}] on Salt server.")

        String canonicalFilePath = "${scriptsDirectory}/${fileName}.sls"

        Target<List<String>> minionList = new MinionList([masterMinionName])

        Map<String, Result<Boolean>> isDirExistResult = File.directoryExists(scriptsDirectory).callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

        def isDirExist = isDirExistResult.find().value?.xor?.right()?.value

        if (!isDirExist) {

            log.warn("Directoty ${scriptsDirectory} is not exists, create.")

            File.mkdir(scriptsDirectory).callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)
        }

        //File.touch(canonicalFilePath).callAsync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)
        Map<String, Result<String>> fileWriteResult = File.write(canonicalFilePath, fileContent).callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

        def fileSaltResult = fileWriteResult.find().value?.xor?.right()?.value

        log.debug("Response from Salt server on file creation [${fileSaltResult}].")

        log.debug("Successfully created script file with full path [${canonicalFilePath}] on Salt server.")
    }

    /**
     * Удаление sls файла скрипта
     * @param fileName - название файла
     */
    void deleteSaltScriptSlsFile(String fileName) {

        String canonicalFilePath = "${scriptsDirectory}/${fileName}.sls"

        Target<List<String>> minionList = new MinionList([masterMinionName])

        log.debug("Start deleting file [${canonicalFilePath}] from Salt server.")

        //region проверка существует ли файл

        Map<String, Result<Boolean>> isFileExistResult = File.fileExists(canonicalFilePath).callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

        def isFileExist = isFileExistResult.find().value?.xor?.right()?.value
        if (!isFileExist) {
            log.error("File [${"${scriptsDirectory}/${fileName}.sls"}] not found on Salt server.")
            throw new FileNotFoundException("File [${"${scriptsDirectory}/${fileName}.sls"}] not found on Salt server.")
        }

        //endregion

        Map<String, Result<Boolean>> fileRemoveResult = File.remove(canonicalFilePath).callSync(saltClient, minionList, USER, PASSWORD, AuthModule.PAM)

        def fileSuccessfullyDeleted = fileRemoveResult.find().value?.xor?.right()?.value

        if (fileSuccessfullyDeleted) {

            log.debug("Successfully deleted file [${canonicalFilePath}] from Salt server.")

        } else {

            log.error("File [${canonicalFilePath}] not deleted from Salt server.")
            throw new Exception("File [${canonicalFilePath}] not deleted from Salt server.")
        }
    }

}
