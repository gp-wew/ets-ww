package com.swire.etsww.base;

import io.ebean.EbeanServer;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Query;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.ebean.util.Converters;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BaseController<DOMAIN> {

    private Class c;

    @Autowired
    protected EbeanServer ebeanServer;

    {
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        c= (Class) params[0];
    }

    @RequestMapping(value = "list",method = RequestMethod.GET)
    public String listPage(){
        return controllerMapping()+"/list";
    }

    @RequestMapping(value = "list",method = RequestMethod.POST)
    public String list(HttpServletRequest request, Model model){
//        TODO
//        model.addAttribute("enums", BeansWrapper.getDefaultInstance().getEnumModels());

        Map<String, SearchFilter> filters = Search.parse(request, "search_");
        PageRequest pageRequest = Search.page(request, "order_", listSort(request));

        Query q = ebeanServer.find(c);
        ExpressionList where = q.where();

        filters.forEach((k,v)->{
            String fieldName = v.fieldName;
            Object value = v.value;
            switch (v.operator) {
                case EQ:
                    where.eq(fieldName,value);
                    break;
                case LIKE:
                    where.like(fieldName,"%" + value + "%");
                    break;
                case ILIKE:
                    where.ilike(fieldName,"%" + value + "%");
                    break;
                case GT:
                    where.gt(fieldName,value);
                    break;
                case LT:
                    where.lt(fieldName,value);
                    break;
                case GTE:
                    where.ge(fieldName,value);
                    break;
                case LTE:
                    where.le(fieldName,value);
                    break;
                case IN:
                    where.in(fieldName,value);
                    break;
                case ISNULL:
                    where.isNull(fieldName);
                    break;
                case NOTNULL:
                    where.isNotNull(fieldName);
                    break;
                case NEQ:
                    where.ne(fieldName,value);
                    break;
            }
        });

        if(filters.isEmpty()){
            defaultCondition(request,where);
        }

        PagedList pagedList = where.setMaxRows(pageRequest.getPageSize())
                .setFirstRow((int) pageRequest.getOffset())
                .setOrder(Converters.convertToEbeanOrderBy(pageRequest.getSort()))
                .findPagedList();

        Page page = Converters.convertToSpringDataPage(pagedList, pageRequest.getSort());

        request.setAttribute("page",page);
        return controllerMapping()+"/list";
    }

    protected String controllerMapping() {
        return StringUtils.uncapitalize(c.getSimpleName());
    }

    @RequestMapping("delete")
    @ResponseBody
    public Dwz delete(Integer id){
        ebeanServer.delete(c,id);
        return Dwz.OK;
    }


    @RequestMapping(value = "save")
    @ResponseBody
    public Dwz save(@ModelAttribute("entity") DOMAIN entity,HttpServletRequest request){
        try {
            Object id = PropertyUtils.getSimpleProperty(entity, "id");
            if(id==null){
                ebeanServer.save(entity);
            }else{
                ebeanServer.update(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Dwz.OK;
    }

    @RequestMapping(value = "form")
    public String form(@ModelAttribute("entity") DOMAIN entity){
        return controllerMapping()+"/form";
    }

    @ModelAttribute
    public void getEntity(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if(id==null || -1==id){
            return;
        }

        Object entity = ebeanServer.find(c, id);

        if(entity!=null){
            model.addAttribute("entity",entity);
        }
    }

    protected Sort listSort(HttpServletRequest request){
        List<Sort.Order> orderList=new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC,"id"));
        return Sort.by(orderList);
    }

    protected void defaultCondition(HttpServletRequest request, ExpressionList where){

    }

}
