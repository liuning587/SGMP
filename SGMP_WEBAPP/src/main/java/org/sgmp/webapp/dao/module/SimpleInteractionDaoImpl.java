package org.sgmp.webapp.dao.module;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Nick
 *
 */
@Repository
public class SimpleInteractionDaoImpl implements SimpleInteractionDao {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

}