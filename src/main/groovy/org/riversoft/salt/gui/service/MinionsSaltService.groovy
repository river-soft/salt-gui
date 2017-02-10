package org.riversoft.salt.gui.service

import groovy.util.logging.Slf4j
import org.riversoft.salt.gui.client.SaltClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Slf4j
@Service
class MinionsSaltService {

    //region injection

    @Value('${salt.user}')
    private String USER

    @Value('${salt.password}')
    private String PASSWORD

    @Autowired
    private SaltClient saltClient

    //endregion

    /**
     * Принятие синьона на сервере salt
     * @param minionName - название миньона
     */
    def acceptMinion(String minionName) {

        //TODO implementation

//        WheelResult<Object> keyResults = Key.accept(minionName).callSync(
//                saltClient, USER, PASSWORD, AuthModule.PAM);

//        Key.Names keys = keyResults.getData().getResult();
    }

    /**
     * Отклонение миньона на сервере salt
     * @param minionName - название миньона
     */
    def rejectMinion(String minionName) {

        //TODO implementation
    }

}
