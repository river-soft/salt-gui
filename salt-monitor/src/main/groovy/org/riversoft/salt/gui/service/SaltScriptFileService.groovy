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
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import java.nio.charset.Charset

@Slf4j
@Service
class SaltScriptFileService {

    @Value('${salt.user}') private String USER
    @Value('${salt.password}') private String PASSWORD
    @Value('${salt.scripts.directory}') String scriptsDirectory

    @Autowired private SaltClient saltClient

    /**
     * Создание sls файла скрипта
     * @param fileName - название файла
     * @param file - полный путь к файлуContent - содержимое файла скрипта
     */
    void createSaltScriptSlsFile(String fileName, String fileContent) {

        log.debug("Start creating script file with name [${fileName}] on Salt server.")

        def dir = new java.io.File(scriptsDirectory)

        if (!dir.exists()) {
            log.warn("Directoty ${dir.canonicalPath} is not exists, create")
            dir.mkdirs()
        }

        def file = new java.io.File(dir, "${fileName}.sls".toString())

        file.createNewFile()
        file.write(fileContent, 'UTF-8')

        log.debug("Create file [${file.absolutePath}]")
    }

    /**
     * Удаление sls файла скрипта
     * @param fileName - название файла
     */
    void deleteSaltScriptSlsFile(String fileName) {

        String canonicalFilePath = "${scriptsDirectory}/${fileName}.sls"

        def file = new java.io.File(canonicalFilePath)

        if (file.delete()) {

            log.debug("Succesfully delete file ${canonicalFilePath}")
        } else {

            log.warn("Cannot delete file ${canonicalFilePath}")
        }
    }

}
