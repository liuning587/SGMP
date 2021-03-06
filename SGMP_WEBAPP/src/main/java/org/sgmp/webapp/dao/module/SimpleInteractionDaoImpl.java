package org.sgmp.webapp.dao.module;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.sgmp.webapp.DaoException;
import org.sgmp.webapp.mapper.module.SimpleInteractionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Nick
 *
 */
@Repository
public class SimpleInteractionDaoImpl implements SimpleInteractionDao {

    private static final Logger logger = LoggerFactory.getLogger(SimpleInteractionDaoImpl.class);

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    protected static final int PAGE_SIZE_DEFAULT = 100;

    @Override
    public List<?> getInteractionObjectList(Class<? extends SimpleInteractionMapper> mapperClass, Map<String, Object> params, 
            String start, String limit, String sort, String dir) throws DaoException {
        if(StringUtils.isNotBlank(start) && StringUtils.isNotBlank(limit)) {
            int _start = 0;
            int _limit = PAGE_SIZE_DEFAULT;
            int _end = _start + _limit;
            try {
                _start = Integer.parseInt(start);
                _limit = Integer.parseInt(limit);
                _end = _start + _limit;
            }
            catch(NumberFormatException _nfe) {
                logger.error("getList error", _nfe);
                throw new DaoException("DaoException", _nfe.getCause());
            }
            params.put("_start", _start);
            params.put("_end", _end);
        }
        params.put("_sort", sort);
        params.put("_dir", dir);
        return sqlSessionTemplate.getMapper(mapperClass).getInteractionObjectList(params);
    }

    @Override
    public Integer getInteractionObjectCount(Class<? extends SimpleInteractionMapper> mapperClass, Map<String, Object> params) throws DaoException {
        return sqlSessionTemplate.getMapper(mapperClass).getInteractionObjectCount(params);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getInteractionObject(Class<? extends SimpleInteractionMapper> mapperClass, Map<String, Object> params) throws DaoException {
        return (Map<String, Object>) sqlSessionTemplate.getMapper(mapperClass).getInteractionObject(params);
    }

}
