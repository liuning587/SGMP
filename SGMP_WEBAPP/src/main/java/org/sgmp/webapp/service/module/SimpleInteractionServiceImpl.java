package org.sgmp.webapp.service.module;

import org.sgmp.webapp.dao.module.SimpleInteractionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Nick
 *
 */
@Service
public class SimpleInteractionServiceImpl implements SimpleInteractionService {

    @Autowired
    private SimpleInteractionDao simpleInteractionDao;

}
