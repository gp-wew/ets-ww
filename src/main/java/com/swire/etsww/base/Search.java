package com.swire.etsww.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.servlet.ServletRequest;
import java.util.*;

public class Search {

    public final static int DEAULT_PAGE_SIZE  = 100;


    public static Map<String, SearchFilter> parse(ServletRequest request, String prefix) {
        Map<String, Object> searchParams = getParametersStartingWith(request, prefix);
        return SearchFilter.parse(searchParams);
    }

    public static void addFilter(Map<String, SearchFilter> filters, String fieldName, SearchFilter.Operator operator, Object value){
        filters.put(new String(fieldName),new SearchFilter(fieldName,operator,value));
    }


    public static PageRequest page(ServletRequest request, String orderPrefix, Sort defaultSort) {
        String _pageSize = request.getParameter("numPerPage");//dwz参数
        int pageSize=DEAULT_PAGE_SIZE;
        if(StringUtils.isNotBlank(_pageSize)){
            pageSize=Integer.valueOf(_pageSize);
        }
        String _pageNumber = request.getParameter("pageNum");//dwz参数
        int pageNumber=0;
        if(StringUtils.isNotBlank(_pageNumber)){
            pageNumber=Integer.valueOf(_pageNumber)-1;
        }

        Map<String, Object> orders = getParametersStartingWith(request, orderPrefix);
        List<Sort.Order> orderList=new ArrayList<>();

        if(orders!=null&&!orders.isEmpty()){
            orders.forEach((k,v)->{
                //order_0_biz orderList.add(0,order)
                //order_1_status orderList.add(1,order)
                Integer idx=null;
                if(k.contains("_")){
                    String[] split = k.split("_");
                    k=split[1];
                    idx=Integer.valueOf(split[0]);
                }
                Sort.Order order = new Sort.Order(Sort.Direction.fromString(v.toString()),k);
                if (idx!=null){
                    orderList.add(idx,order);
                }else {
                    orderList.add(order);
                }
            });
        }
        if(!orderList.isEmpty()){
            return PageRequest.of(pageNumber,pageSize, Sort.by(orderList));
        }
        if(defaultSort!=null){
            return PageRequest.of(pageNumber,pageSize,defaultSort);
        }
        return PageRequest.of(pageNumber,pageSize);
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     *
     * 返回的结果的Parameter名已去除前缀.
     */
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Validate.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        if (prefix == null) {
            prefix = "";
        }
        while ((paramNames != null) && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if ((values == null) || (values.length == 0)) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }
}
