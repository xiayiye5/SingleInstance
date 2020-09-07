package com.example.singleinstance

import android.content.Context

/**
 * Kotlin里面的静态类
 */
object TrendsIdUtils {

    //缓存资源id
    private val idMap: HashMap<String, Int> = HashMap()

    private fun getIdByName(context: Context, defType: String, name: String): Int {

        //缓存
        val key = defType + "_" + name
        val value: Int? = idMap.get(key)
        value?.let {
            return it
        }

        //获取资源id
        val identifier = context.resources.getIdentifier(name, defType, context.packageName)
        identifier.let {
            idMap.put(key, identifier)
        }
        return identifier
    }

    /**
     * 获取布局文件的资源ID，defType传 layout
     */
    fun getIdFromLayout(context: Context, name: String): Int {
        return getIdByName(context, "layout", name)
    }
}