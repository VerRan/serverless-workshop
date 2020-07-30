package my.service.util;

import java.lang.reflect.Field;

public class CombineBeans {

    /**
     * @Title: combineSydwCore
     * @param sourceBean
     * @param targetBean
     * @return targetBean
     * @return: Object
     */
    @SuppressWarnings("unused")
    public   static  Object combineHackson(Object sourceBean, Object targetBean) {
        try {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = sourceBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (!(sourceField.get(sourceBean) == null)) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        }catch (Exception e){
            e.printStackTrace();
        }
        return targetBean;
    }

}